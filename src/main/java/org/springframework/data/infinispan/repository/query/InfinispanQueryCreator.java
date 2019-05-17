package org.springframework.data.infinispan.repository.query;

import java.util.Iterator;

import org.infinispan.query.dsl.FilterConditionContextQueryBuilder;
import org.infinispan.query.dsl.QueryBuilder;
import org.infinispan.query.dsl.QueryFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Sort;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;

public class InfinispanQueryCreator extends AbstractQueryCreator<KeyValueQuery<QueryBuilder>, FilterConditionContextQueryBuilder> {

   private QueryFactory queryFactory;

   public InfinispanQueryCreator(PartTree tree) {
      super(tree);
   }

   public InfinispanQueryCreator(PartTree tree, ParameterAccessor parameters, QueryFactory queryFactory) {
      super(tree, parameters);
      this.queryFactory = queryFactory;
   }

   @Override
   protected FilterConditionContextQueryBuilder create(Part part, Iterator<Object> iterator) {
      return queryBuilder(part, iterator);
   }

   @Override
   protected FilterConditionContextQueryBuilder and(Part part, FilterConditionContextQueryBuilder base, Iterator<Object> iterator) {
      FilterConditionContextQueryBuilder andFilter = queryBuilder(part, iterator);
      return base.and(andFilter);
   }

   @Override
   protected FilterConditionContextQueryBuilder or(FilterConditionContextQueryBuilder base, FilterConditionContextQueryBuilder criteria) {
      return base.or(criteria);
   }

   @Override
   protected KeyValueQuery<QueryBuilder> complete(FilterConditionContextQueryBuilder criteria, Sort sort) {
      //TODO: SORT!!!!

      return new KeyValueQuery<>(criteria);
   }

   private FilterConditionContextQueryBuilder queryBuilder(Part part, Iterator iterator) {
      String property = part.getProperty().toDotPath();
      Part.Type type = part.getType();

      QueryBuilder from = queryFactory.from(part.getProperty().getOwningType().getType().getName());

      switch (type) {
         case TRUE:
            return from.having(property).eq(true);
         case FALSE:
            return from.having(property).eq(false);
         case IS_NULL:
            return from.having(property).isNull();
         case IS_NOT_NULL:
            return from.not().having(property).isNull();
         case SIMPLE_PROPERTY:
            return from.having(property).eq(iterator.next());
         case NEGATING_SIMPLE_PROPERTY:
            return from.not().having(property).eq(iterator.next());
         default:
            throw new InvalidDataAccessApiUsageException(String.format("Unsupported type '%s'", type));
      }
   }
}
