package org.springframework.data.infinispan.repository.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.infinispan.test.common.InfinispanSpringDataTest;
import org.infinispan.test.example.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EnableInfinispanRepositoriesTest extends InfinispanSpringDataTest {

   @Autowired
   private PersonRepository personRepository;

   @Test
   public void repositoryCorrectlyEnabled() {
      assertThat(personRepository).isNotNull();
   }
}
