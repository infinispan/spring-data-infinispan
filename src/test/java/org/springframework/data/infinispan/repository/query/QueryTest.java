package org.springframework.data.infinispan.repository.query;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.infinispan.test.common.InfinispanSpringDataTest;
import org.infinispan.test.example.Person;
import org.infinispan.test.example.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class QueryTest extends InfinispanSpringDataTest {

   @Autowired
   private PersonRepository personRepository;

   @Test
   public void findByFirstname() {
      List<Person> emptyPeople = personRepository.findByFirstname("katia");
      assertThat(emptyPeople).isEmpty();

      List<Person> people = personRepository.findByFirstname("oihana");
      assertThat(people).hasSize(1);
      Person person = people.get(0);
      assertThat(person.getFirstname()).isEqualTo("oihana");
   }

   @Test
   public void countByFirstname() {
      assertThat(personRepository.countByFirstname("katia")).isEqualTo(0);
      assertThat(personRepository.countByFirstname("oihana")).isEqualTo(1);
   }
}
