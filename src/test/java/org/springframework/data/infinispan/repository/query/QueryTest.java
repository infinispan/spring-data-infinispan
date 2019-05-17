package org.springframework.data.infinispan.repository.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.infinispan.test.example.TestData.ELAIA;
import static org.infinispan.test.example.TestData.OIHANA;

import java.util.List;

import javax.annotation.Resource;

import org.infinispan.test.common.InfinispanSpringDataTest;
import org.infinispan.test.example.Person;
import org.infinispan.test.example.PersonRepository;
import org.junit.jupiter.api.Test;

public class QueryTest extends InfinispanSpringDataTest {

   @Resource
   private PersonRepository personRepository;

   @Test
   public void findByFirstname() {
      List<Person> emptyPeople = personRepository.findByFirstname("nobody");
      assertThat(emptyPeople).isEmpty();

      List<Person> people = personRepository.findByFirstname("oihana");
      assertThat(people).containsExactly(OIHANA);
   }

   @Test
   public void findByFirstnameNot() {
      List<Person> people = personRepository.findByFirstnameNot("elaia");
      assertThat(people).containsExactly(OIHANA);
   }

   @Test
   public void findByLastnameIsNull() {
      List<Person> people = personRepository.findByLastnameIsNull();
      assertThat(people).containsExactly(ELAIA);
   }

   @Test
   public void findByLastnameIsNotNull() {
      List<Person> people = personRepository.findByLastnameIsNotNull();
      assertThat(people).containsExactly(OIHANA);
   }

   @Test
   public void findByIsBasqueTrue() {
      List<Person> people = personRepository.findByIsBasqueTrue();
      assertThat(people).containsExactlyInAnyOrder(OIHANA, ELAIA);
   }

   @Test
   public void findByIsBigSisterFalse() {
      List<Person> people = personRepository.findByIsBigSisterFalse();
      assertThat(people).containsExactlyInAnyOrder(ELAIA);
   }

   @Test
   public void countByFirstname() {
      assertThat(personRepository.countByFirstname("nobody")).isEqualTo(0);
      assertThat(personRepository.countByFirstname("oihana")).isEqualTo(1);
   }
}
