package org.springframework.data.infinispan.repository.query;

import java.util.Iterator;

import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryBuilder;
import org.infinispan.query.dsl.QueryFactory;
import org.infinispan.query.dsl.SortOrder;
import org.springframework.data.domain.Sort;
import org.springframework.data.keyvalue.core.SortAccessor;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;

/**
 * Infinispan sort accessor modifies the {@link QueryBuilder} in case a sorting must be applied
 *
 * @author Katia Aresti
 */
public class InfinispanSortAccessor implements SortAccessor<QueryBuilder> {

   @Override
   public QueryBuilder resolve(KeyValueQuery<?> query) {
      Sort sort = query.getSort();
      QueryBuilder criteria = (QueryBuilder) query.getCriteria();

      if (sort != null && sort.isSorted()) {
         Iterator<Sort.Order> iterator = sort.iterator();
         while (iterator.hasNext()) {
            Sort.Order next = iterator.next();
            criteria.orderBy(next.getProperty(), next.getDirection().isAscending() ? SortOrder.ASC : SortOrder.DESC);
         }
      }
      return criteria;
   }

}
