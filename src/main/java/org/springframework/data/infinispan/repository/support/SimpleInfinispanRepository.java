package org.springframework.data.infinispan.repository.support;

import java.io.Serializable;

import org.springframework.data.infinispan.repository.InfinispanRepository;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.repository.support.SimpleKeyValueRepository;
import org.springframework.data.repository.core.EntityInformation;

public class SimpleInfinispanRepository<T extends Serializable, ID extends Serializable>
        extends SimpleKeyValueRepository<T, ID>
        implements InfinispanRepository<T, ID> {

    public SimpleInfinispanRepository(EntityInformation<T, ID> metadata, KeyValueOperations operations) {
        super(metadata, operations);
    }

}
