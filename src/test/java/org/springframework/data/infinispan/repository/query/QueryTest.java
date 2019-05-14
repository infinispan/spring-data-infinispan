package org.springframework.data.infinispan.repository.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.test.common.InfinispanSpringDataTest;
import org.infinispan.test.example.Person;
import org.infinispan.test.example.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class QueryTest extends InfinispanSpringDataTest {

   @Autowired
   private PersonRepository personRepository;

   @Test
   public void findByFirstname(@Autowired RemoteCacheManager remoteCacheManager) {
      List<Person> emptyPeople = this.personRepository.findByFirstname("katia");
      assertTrue(emptyPeople.isEmpty());
      List<Person> people = this.personRepository.findByFirstname("oihana");
      assertEquals(1, people.size());
      Person person = people.get(0);
      assertEquals("oihana", person.getFirstname());
   }
}
