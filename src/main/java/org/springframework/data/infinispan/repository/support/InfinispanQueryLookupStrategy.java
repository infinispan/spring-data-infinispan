package org.springframework.data.infinispan.repository.support;

import java.lang.reflect.Method;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.springframework.data.infinispan.repository.query.InfinispanPartTreeQuery;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.util.Assert;

public class InfinispanQueryLookupStrategy implements QueryLookupStrategy {

   private final QueryMethodEvaluationContextProvider evaluationContextProvider;
   private final KeyValueOperations keyValueOperations;
   private final Class<? extends AbstractQueryCreator<?, ?>> queryCreator;
   private final RemoteCacheManager remoteCacheManager;

   public InfinispanQueryLookupStrategy(QueryLookupStrategy.Key key,
                                        QueryMethodEvaluationContextProvider evaluationContextProvider,
                                        KeyValueOperations keyValueOperations,
                                        Class<? extends AbstractQueryCreator<?, ?>> queryCreator,
                                        RemoteCacheManager remoteCacheManager) {

      this.evaluationContextProvider = evaluationContextProvider;
      this.keyValueOperations = keyValueOperations;
      this.queryCreator = queryCreator;
      this.remoteCacheManager = remoteCacheManager;
   }

   public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory projectionFactory,
                                       NamedQueries namedQueries) {

      InfinispanQueryMethod queryMethod = new InfinispanQueryMethod(method, metadata, projectionFactory);

      if (queryMethod.hasAnnotatedQuery()) {
         return new IckleQueryRepositoryQuery(queryMethod, remoteCacheManager);
      }

      return new InfinispanPartTreeQuery(queryMethod, evaluationContextProvider, this.keyValueOperations, new InfinispanPartTreeQuery.ConstructorCachingQueryCreatorFactory(this.queryCreator), queryMethod.getKeySpace(), remoteCacheManager);
   }

}
