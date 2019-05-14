package org.springframework.data.infinispan.repository.support;

import java.io.Serializable;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.repository.support.KeyValueRepositoryFactory;
import org.springframework.data.keyvalue.repository.support.KeyValueRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;

public class InfinispanRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
      extends KeyValueRepositoryFactoryBean<T, S, ID> {

   @Autowired(required = false)
   private RemoteCacheManager remoteCacheManager;

   public InfinispanRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
      super(repositoryInterface);
   }

   @Override
   protected KeyValueRepositoryFactory createRepositoryFactory(KeyValueOperations operations,
                                                               Class<? extends AbstractQueryCreator<?, ?>> queryCreator,
                                                               Class<? extends RepositoryQuery> repositoryQueryType) {
      return new InfinispanRepositoryFactory(operations, queryCreator, remoteCacheManager);
   }
}
