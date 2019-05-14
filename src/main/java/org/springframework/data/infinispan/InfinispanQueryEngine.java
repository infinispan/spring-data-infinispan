package org.springframework.data.infinispan;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map.Entry;

import org.infinispan.query.dsl.QueryBuilder;
import org.springframework.data.infinispan.repository.query.InfinispanCriteriaAccessor;
import org.springframework.data.keyvalue.core.QueryEngine;

/**
 * Implementation of {@code findBy*()} and {@code countBy*{}} queries.
 *
 * @author Katia Aresti
 */
public class InfinispanQueryEngine extends QueryEngine<InfinispanKeyValueAdapter, QueryBuilder, Comparator<Entry<?, ?>>> {

   public InfinispanQueryEngine() {
      super(new InfinispanCriteriaAccessor(), null);
   }

   @Override
   public Collection<?> execute(QueryBuilder criteria, Comparator<Entry<?, ?>> sort, long offset, int rows, String keyspace) {
      if (offset > 0)
         criteria.startOffset(offset);

      if (rows > 0)
         criteria.maxResults(rows);

      return criteria.build().list();
   }

   @Override
   public long count(QueryBuilder query, String keyspace) {
      return query.select("id").build().getResultSize();
   }
}
