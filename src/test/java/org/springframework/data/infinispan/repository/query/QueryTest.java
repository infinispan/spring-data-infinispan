package org.springframework.data.infinispan.repository.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.infinispan.test.example.TestData.ELAIA;
import static org.infinispan.test.example.TestData.OIHANA;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.infinispan.test.common.InfinispanSpringDataTest;
import org.infinispan.test.example.Person;
import org.infinispan.test.example.PersonRepository;
import org.junit.jupiter.api.Test;

/**
 * Verifies query support Infinispan and Spring-Data
 *
 * @author Katia Aresti
 */
public class QueryTest extends InfinispanSpringDataTest {

   @Resource
   private PersonRepository personRepository;

   @Test
   public void findByFirstname() {
      List<Person> emptyPeople = personRepository.findByFirstname("Oihana");
      assertThat(emptyPeople).isEmpty();

      List<Person> people = personRepository.findByFirstname("oihana");
      assertThat(people).containsExactly(OIHANA);
   }

   @Test
   public void findByFirstnameIgnoreCase() {
      List<Person> people = personRepository.findByFirstnameIgnoreCase("OIHANA");
      assertThat(people).containsExactly(OIHANA);
   }

   @Test
   public void findByFirstnameNot() {
      List<Person> people = personRepository.findByFirstnameNot("elaia");
      assertThat(people).containsExactly(OIHANA);

      List<Person> people1 = personRepository.findByFirstnameNot("Elaia");
      assertThat(people1).containsExactly(OIHANA, ELAIA);
   }

   @Test
   public void findByFirstnameNotIgnoreCase() {
      List<Person> people = personRepository.findByFirstnameNotIgnoreCase("ELAIA");
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
   public void findByAgeBetween() {
      List<Person> people = personRepository.findByAgeBetween(2, 4);
      assertThat(people).containsExactlyInAnyOrder(OIHANA);
   }

   @Test
   public void findByAgeBefore() {
      List<Person> people = personRepository.findByAgeBefore(2);
      assertThat(people).containsExactlyInAnyOrder(ELAIA);
   }

   @Test
   public void findByAgeAfter() {
      List<Person> people = personRepository.findByAgeAfter(2);
      assertThat(people).containsExactlyInAnyOrder(OIHANA);
   }

   @Test
   public void findByAgeGreaterThan() {
      List<Person> people = personRepository.findByAgeGreaterThan(2);
      assertThat(people).containsExactlyInAnyOrder(OIHANA);
   }

   @Test
   public void findByAgeGreaterThanEqual() {
      List<Person> people = personRepository.findByAgeGreaterThanEqual(3);
      assertThat(people).containsExactlyInAnyOrder(OIHANA);
   }

   @Test
   public void findByAgeIsLessThan() {
      List<Person> people = personRepository.findByAgeIsLessThan(2);
      assertThat(people).containsExactlyInAnyOrder(ELAIA);
   }

   @Test
   public void findByAgeIsLessThanEqual() {
      List<Person> people = personRepository.findByAgeIsLessThanEqual(1);
      assertThat(people).containsExactlyInAnyOrder(ELAIA);
   }

   @Test
   public void findByAgeIsIn() {
      List<Person> people = personRepository.findByAgeIsIn(Arrays.asList(0,1,2));
      assertThat(people).containsExactlyInAnyOrder(ELAIA);
   }

   @Test
   public void findByAgeIsNotIn() {
      List<Person> people = personRepository.findByAgeIsNotIn(Arrays.asList(0,1,2));
      assertThat(people).containsExactlyInAnyOrder(OIHANA);
   }

   @Test
   public void findByFirstnameLike() {
      List<Person> people = personRepository.findByFirstnameLike("ha");
      assertThat(people).containsExactlyInAnyOrder(OIHANA);
   }

   @Test
   public void findByFirstnameNotLike() {
      List<Person> people = personRepository.findByFirstnameNotLike("ha");
      assertThat(people).containsExactlyInAnyOrder(ELAIA);
   }

   @Test
   public void findByFirstnameStartingWith() {
      List<Person> people = personRepository.findByFirstnameStartingWith("ela");
      assertThat(people).containsExactlyInAnyOrder(ELAIA);
   }

   @Test
   public void findByFirstnameEndingWith() {
      List<Person> people = personRepository.findByFirstnameEndingWith("na");
      assertThat(people).containsExactlyInAnyOrder(OIHANA);
   }

   @Test
   public void findByFirstnameContaining() {
      List<Person> people = personRepository.findByFirstnameContaining(Arrays.asList("oihana", "elaia"));
      assertThat(people).containsExactlyInAnyOrder(OIHANA, ELAIA);
   }

   @Test
   public void findByFirstnameNotContaining() {
      List<Person> people = personRepository.findByFirstnameNotContaining(Arrays.asList("oihana", "elaia"));
      assertThat(people).isEmpty();

      List<Person> people1 = personRepository.findByFirstnameNotContaining(Arrays.asList("elaia"));
      assertThat(people1).containsExactlyInAnyOrder(OIHANA);

      List<Person> people2 = personRepository.findByFirstnameNotContaining(Arrays.asList("oihana"));
      assertThat(people2).containsExactlyInAnyOrder(ELAIA);
   }

   @Test
   public void countByFirstname() {
      assertThat(personRepository.countByFirstname("nobody")).isEqualTo(0);
      assertThat(personRepository.countByFirstname("oihana")).isEqualTo(1);
   }
}
