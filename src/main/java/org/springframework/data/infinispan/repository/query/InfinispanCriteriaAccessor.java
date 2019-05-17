package org.springframework.data.infinispan.repository.query;

import org.infinispan.query.dsl.QueryBuilder;
import org.springframework.data.keyvalue.core.CriteriaAccessor;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;

/**
 * Provides the mechanism to convert a KeyValueQuery into an Infinispan QueryBuilder
 *
 * @author Katia Aresti
 */
public class InfinispanCriteriaAccessor implements CriteriaAccessor<QueryBuilder> {

   @Override
   public QueryBuilder resolve(KeyValueQuery<?> query) {
      if (query.getCriteria() instanceof QueryBuilder) {
         return (QueryBuilder) query.getCriteria();
      }

      return null;
   }
}
