package org.springframework.data.infinispan.repository;

import java.io.Serializable;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface InfinispanRepository<T extends Serializable, ID extends Serializable>
      extends KeyValueRepository<T, ID> {
}
