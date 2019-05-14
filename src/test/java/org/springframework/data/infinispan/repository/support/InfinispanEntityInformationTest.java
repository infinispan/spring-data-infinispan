package org.springframework.data.infinispan.repository.support;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Serializable;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.test.common.InfinispanSpringDataTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.infinispan.InfinispanKeyValueAdapter;
import org.springframework.data.keyvalue.annotation.KeySpace;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.mapping.PersistentEntity;

public class InfinispanEntityInformationTest extends InfinispanSpringDataTest {

   @KeySpace
   class NoId implements Serializable {

   }

   @Test
   public void noIdThrowsException(@Autowired RemoteCacheManager remoteCacheManager) {
      KeyValueOperations keyValueOperations = new KeyValueTemplate(new InfinispanKeyValueAdapter(remoteCacheManager));
      PersistentEntity<?, ?> persistentEntity = keyValueOperations.getMappingContext().getPersistentEntity(NoId.class);
      assertThrows(MappingException.class, () -> new InfinispanEntityInformation<>(persistentEntity));
   }
}