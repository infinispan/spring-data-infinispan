package org.springframework.data.infinispan.repository.query;

import java.lang.reflect.Constructor;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.springframework.beans.BeanUtils;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.data.keyvalue.repository.query.KeyValuePartTreeQuery;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.data.util.StreamUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 *
 * @author Katia Aresti
 */
public class InfinispanPartTreeQuery extends KeyValuePartTreeQuery {

   private final QueryMethod queryMethod;
   private final KeyValueOperations keyValueOperations;
   private final ConstructorCachingQueryCreatorFactory queryCreatorFactory;
   private final RemoteCacheManager remoteCacheManager;
   private final String keySpace;

   public InfinispanPartTreeQuery(
         QueryMethod queryMethod, QueryMethodEvaluationContextProvider evaluationContextProvider,
         KeyValueOperations keyValueOperations,
         ConstructorCachingQueryCreatorFactory queryCreatorFactory,
         String keySpace,
         RemoteCacheManager remoteCacheManager) {
      super(queryMethod, evaluationContextProvider, keyValueOperations, queryCreatorFactory);
      this.queryMethod = queryMethod;
      this.keyValueOperations = keyValueOperations;
      this.queryCreatorFactory = queryCreatorFactory;
      this.remoteCacheManager = remoteCacheManager;
      this.keySpace = keySpace;
   }

   public static class ConstructorCachingQueryCreatorFactory
         implements QueryCreatorFactory<AbstractQueryCreator<KeyValueQuery<?>, ?>> {

      private final Class<?> type;
      private final @Nullable
      Constructor<? extends AbstractQueryCreator<?, ?>> constructor;

      public ConstructorCachingQueryCreatorFactory(Class<? extends AbstractQueryCreator<?, ?>> type) {

         this.type = type;
         this.constructor = ClassUtils.getConstructorIfAvailable(type, PartTree.class, ParameterAccessor.class);
      }

      @Override
      public AbstractQueryCreator<KeyValueQuery<?>, ?> queryCreatorFor(PartTree partTree, ParameterAccessor accessor) {

         Assert.state(constructor != null,
               () -> String.format("No constructor (PartTree, ParameterAccessor) could be found on type %s!", type));
         return (AbstractQueryCreator<KeyValueQuery<?>, ?>) BeanUtils.instantiateClass(constructor, partTree, accessor);
      }
   }

   @Override
   public Object execute(Object[] parameters) {
      KeyValueQuery<?> query = prepareQuery(parameters);

      if (isFindQuery()) {
         return find(query, queryMethod);
      }

      throw new UnsupportedOperationException(String.format("Query method '%s' not supported.", queryMethod.getName()));
   }

   public KeyValueQuery<?> createQuery(ParameterAccessor accessor) {

      PartTree tree = new PartTree(getQueryMethod().getName(), getQueryMethod().getEntityInformation().getJavaType());

      InfinispanQueryCreator infinispanQueryCreator = new InfinispanQueryCreator(tree, accessor);
      infinispanQueryCreator.setRemoteCache(remoteCacheManager.getCache(keySpace));
      KeyValueQuery<?> query = infinispanQueryCreator.createQuery();

      if (tree.isLimiting()) {
         query.setRows(tree.getMaxResults());
      }
      return query;
   }

   private boolean isFindQuery() {
      return queryMethod.isCollectionQuery() || queryMethod.isQueryForEntity() || queryMethod.isStreamQuery();
   }

   private Object find(final KeyValueQuery<?> query, final QueryMethod queryMethod) {
      Iterable<?> resultSet = this.keyValueOperations.find(query, queryMethod.getEntityInformation().getJavaType());

      Object result = resultSet;
      if (isSingleResult(queryMethod)) {
         result = resultSet.iterator().hasNext() ? resultSet.iterator().next() : null;
      } else if (queryMethod.isStreamQuery()) {
         result = StreamUtils.createStreamFromIterator(resultSet.iterator());
      }

      return result;
   }

   private boolean isSingleResult(QueryMethod queryMethod) {
      return !queryMethod.isCollectionQuery() && !queryMethod.isPageQuery() && !queryMethod.isSliceQuery()
            && !queryMethod.isStreamQuery();
   }
}
