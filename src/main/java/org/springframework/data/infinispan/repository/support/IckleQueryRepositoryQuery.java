package org.springframework.data.infinispan.repository.support;

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
      String queryString = queryMethod.getAnnotatedQuery();

      QueryFactory queryFactory = Search.getQueryFactory(remoteCacheManager.getCache(keySpace));
      return queryFactory.create(queryString).list();
   }

   @Override
   public QueryMethod getQueryMethod() {
      return queryMethod;
   }
}
