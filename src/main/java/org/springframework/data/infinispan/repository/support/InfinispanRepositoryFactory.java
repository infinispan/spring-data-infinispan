package org.springframework.data.infinispan.repository.support;

import java.util.Optional;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.repository.query.SpelQueryCreator;
import org.springframework.data.keyvalue.repository.support.KeyValueRepositoryFactory;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.util.Assert;

public class InfinispanRepositoryFactory extends KeyValueRepositoryFactory {

   private static final Class<SpelQueryCreator> DEFAULT_QUERY_CREATOR = SpelQueryCreator.class;

   private final KeyValueOperations keyValueOperations;
   private final Class<? extends AbstractQueryCreator<?, ?>> queryCreator;
   private final RemoteCacheManager remoteCacheManager;

   public InfinispanRepositoryFactory(KeyValueOperations keyValueOperations, RemoteCacheManager remoteCacheManager) {
      this(keyValueOperations, DEFAULT_QUERY_CREATOR, remoteCacheManager);
   }

   public InfinispanRepositoryFactory(KeyValueOperations keyValueOperations,
                                      Class<? extends AbstractQueryCreator<?, ?>> queryCreator,
                                      RemoteCacheManager remoteCacheManager) {

      super(keyValueOperations, queryCreator);

      this.keyValueOperations = keyValueOperations;
      this.queryCreator = queryCreator;
      this.remoteCacheManager = remoteCacheManager;
   }

   @Override
   protected Optional<QueryLookupStrategy> getQueryLookupStrategy(QueryLookupStrategy.Key key,
                                                                  QueryMethodEvaluationContextProvider evaluationContextProvider) {
      return Optional
            .of(new InfinispanQueryLookupStrategy(key, evaluationContextProvider, keyValueOperations, queryCreator,
                  remoteCacheManager));
   }

   @Override
   public <T, ID> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
      PersistentEntity<T, ?> entity = (PersistentEntity<T, ?>) keyValueOperations.getMappingContext()
            .getPersistentEntity(domainClass);
      Assert.notNull(entity, "Entity must not be 'null'.");
      return new InfinispanEntityInformation<>(entity);
   }
}
