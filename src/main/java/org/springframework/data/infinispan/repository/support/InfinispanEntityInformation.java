package org.springframework.data.infinispan.repository.support;

import org.springframework.data.annotation.Id;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.repository.core.support.PersistentEntityInformation;

/**
 * @author Katia Aresti
 */
class InfinispanEntityInformation<T, ID> extends PersistentEntityInformation<T, ID> {

    InfinispanEntityInformation(PersistentEntity<T, ?> entity) {
        super(entity);
        if (!entity.hasIdProperty()) {
            throw new MappingException(
                    String.format("Entity %s requires a field annotated with %s", entity.getName(),
                            Id.class.getName()));
        }
    }
}
