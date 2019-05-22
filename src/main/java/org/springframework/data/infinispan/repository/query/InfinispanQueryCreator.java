package org.springframework.data.infinispan.repository.query;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

import org.infinispan.commons.util.ReflectionUtil;
import org.infinispan.query.dsl.FilterConditionContext;
import org.infinispan.query.dsl.QueryBuilder;
import org.infinispan.query.dsl.QueryFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Sort;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;

/**
 * InfinispanQueryCreator extends {@link AbstractQueryCreator} and translates into Infinispan {@link
 * FilterConditionContext}
 *
 * @author Katia Aresti
 */
public class InfinispanQueryCreator extends AbstractQueryCreator<KeyValueQuery<FilterConditionContext>, FilterConditionContext> {

   private QueryFactory queryFactory;

   public InfinispanQueryCreator(PartTree tree) {
      super(tree);
   }

   public InfinispanQueryCreator(PartTree tree, ParameterAccessor parameters, QueryFactory queryFactory) {
      super(tree, parameters);
      this.queryFactory = queryFactory;
   }

   @Override
   protected FilterConditionContext create(Part part, Iterator<Object> iterator) {
      return createFilterConditionContext(part, iterator);
   }

   @Override
   protected FilterConditionContext and(Part part, FilterConditionContext base, Iterator<Object> iterator) {
      FilterConditionContext filterConditionContext = createFilterConditionContext(part, iterator);
      resetQueryBuilder(filterConditionContext);
      return base.and(filterConditionContext);
   }

   @Override
   protected FilterConditionContext or(FilterConditionContext base, FilterConditionContext criteria) {
      resetQueryBuilder(criteria);
      return base.or(criteria);
   }

   private void resetQueryBuilder(FilterConditionContext criteria) {
      // If we don't reset to null query builder we get ISPN014810: The given condition is already in use by another builder
      Field queryFactory = ReflectionUtil.getField("queryBuilder", criteria.getClass());
      ReflectionUtil.setAccessibly(criteria, queryFactory, null);
   }

   @Override
   protected KeyValueQuery<FilterConditionContext> complete(FilterConditionContext criteria, Sort sort) {
      KeyValueQuery<FilterConditionContext> keyValueQuery = new KeyValueQuery<>(criteria);
      keyValueQuery.setSort(sort);
      return keyValueQuery;
   }

   private FilterConditionContext createFilterConditionContext(Part part, Iterator iterator) {
      String property = part.getProperty().toDotPath();
      Part.Type type = part.getType();
      QueryBuilder from = queryFactory.from(part.getProperty().getOwningType().getType());

      Object firstParam = null;
      if (iterator.hasNext()) {
         firstParam = iterator.next();
      }

      switch (type) {
         case BETWEEN:
            return from.having(property).between(firstParam, iterator.next());
         case BEFORE:
         case LESS_THAN:
            return from.having(property).lt(firstParam);
         case LESS_THAN_EQUAL:
            return from.having(property).lte(firstParam);
         case AFTER:
         case GREATER_THAN:
            return from.having(property).gt(firstParam);
         case GREATER_THAN_EQUAL:
            return from.having(property).gte(firstParam);
         case IN:
            if (firstParam instanceof Collection) {
               return from.having(property).in((Collection) firstParam);
            } else throw new IllegalArgumentException("IN query needs a Collection");

         case NOT_IN:
            if (firstParam instanceof Collection) {
               return from.not().having(property).in((Collection) firstParam);
            } else throw new IllegalArgumentException("NOT IN query needs a Collection");

         case TRUE:
            return from.having(property).eq(true);
         case FALSE:
            return from.having(property).eq(false);
         case IS_NULL:
            return from.having(property).isNull();
         case IS_NOT_NULL:
            return from.not().having(property).isNull();
         case SIMPLE_PROPERTY:
            if (shouldIgnoreCase(part, firstParam)) {
               return from.having(property).like(((String) firstParam).toLowerCase());
            } else {
               return from.having(property).eq(firstParam);
            }
         case NEGATING_SIMPLE_PROPERTY:
            if (shouldIgnoreCase(part, firstParam)) {
               return from.not().having(property).like(((String) firstParam).toLowerCase());
            } else {
               return from.not().having(property).eq(firstParam);
            }
         case LIKE:
            return from.having(property).like("%" + firstParam + "%");
         case NOT_LIKE:
            return from.not().having(property).like("%" + firstParam + "%");
         case STARTING_WITH:
            return from.having(property).like(firstParam + "%");
         case ENDING_WITH:
            return from.having(property).like("%" + firstParam);
         case CONTAINING:
            if (firstParam instanceof Collection) {
               return from.having(property).containsAny((Collection) firstParam);
            } else {
               return from.having(property).contains(firstParam);
            }
         case NOT_CONTAINING:
            if (firstParam instanceof Collection) {
               return from.not().having(property).containsAll((Collection) firstParam);
            } else {
               return from.not().having(property).contains(firstParam);
            }
         case REGEX:
            throw new UnsupportedOperationException("Infinispan query supports JPA like Query, not Regex");
         default:
            throw new InvalidDataAccessApiUsageException(String.format("Unsupported type '%s'", type));
      }
   }

   private boolean shouldIgnoreCase(Part part, Object param) {
      return param instanceof String && part.shouldIgnoreCase().equals(Part.IgnoreCaseType.ALWAYS);
   }
}
