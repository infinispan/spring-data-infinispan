package org.springframework.data.infinispan.repository.support;

import java.util.Collection;
import java.util.stream.Collectors;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.QueryFactory;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;

public class IckleQueryRepositoryQuery implements RepositoryQuery {

   private final InfinispanQueryMethod queryMethod;
   private final String keySpace;
   private final RemoteCacheManager remoteCacheManager;

   public IckleQueryRepositoryQuery(InfinispanQueryMethod queryMethod,
                                    RemoteCacheManager remoteCacheManager) {
      this.queryMethod = queryMethod;
      this.keySpace = queryMethod.getKeySpace();
      this.remoteCacheManager = remoteCacheManager;
   }

   @Override
   public Object execute(Object[] parameters) {
      String queryStringTemplate = queryMethod.getAnnotatedQuery();
      String queryString = String.format(queryStringTemplate, formatParameters(parameters));

      QueryFactory queryFactory = Search.getQueryFactory(remoteCacheManager.getCache(keySpace));

      return queryFactory.create(queryString).list();
   }

   private Object[] formatParameters(Object[] parameters) {
      Object[] result = new Object[parameters.length];
      for (int i = 0; i < parameters.length; i++) {
         if (parameters[i] instanceof Collection) {
            result[i] = formatCollection((Collection) parameters[i]);
         } else {
            result[i] = parameters[i];
         }
      }
      return result;
   }

   private static String formatCollection(Collection<?> collection) {
      return String.format("(%s)", collection.stream().map(Object::toString).collect(Collectors.joining(",")));
   }

   @Override
   public QueryMethod getQueryMethod() {
      return queryMethod;
   }
}
