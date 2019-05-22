package org.springframework.data.infinispan.repository.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.infinispan.test.example.TestData.ELAIA;
import static org.infinispan.test.example.TestData.JULIEN;
import static org.infinispan.test.example.TestData.OIHANA;
import static org.infinispan.test.example.TestData.RAMON;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.infinispan.test.common.InfinispanSpringDataTest;
import org.infinispan.test.example.Person;
import org.infinispan.test.example.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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
      List<Person> people = personRepository.findByBasqueTrue();
      assertThat(people).containsExactlyInAnyOrder(OIHANA, ELAIA);
   }

   @Test
   public void findByIsBigSisterFalse() {
      List<Person> people = personRepository.findByBigSisterFalse();
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
      List<Person> people = personRepository.findByAgeIsIn(Arrays.asList(0, 1, 2));
      assertThat(people).containsExactlyInAnyOrder(ELAIA);
   }

   @Test
   public void findByAgeIsNotIn() {
      List<Person> people = personRepository.findByAgeIsNotIn(Arrays.asList(0, 1, 2));
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
   public void findByFirstnameOrLastname() {
      List<Person> people1 = personRepository.findByFirstnameOrLastname(ELAIA.getFirstname(), OIHANA.getLastname());
      assertThat(people1).containsExactlyInAnyOrder(ELAIA, OIHANA);
   }

   @Test
   public void findByFirstnameOrAgeBetween() {
      List<Person> people1 = personRepository.findByFirstnameOrAgeBetween(ELAIA.getFirstname(), 1, 10);
      assertThat(people1).containsExactlyInAnyOrder(ELAIA, OIHANA);
   }

   @Test
   public void findByFirstnameAndLastname() {
      List<Person> people1 = personRepository.findByFirstnameAndLastname(ELAIA.getFirstname(), OIHANA.getLastname());
      assertThat(people1).isEmpty();
      List<Person> people2 = personRepository.findByFirstnameAndLastname(OIHANA.getFirstname(), OIHANA.getLastname());
      assertThat(people2).containsExactlyInAnyOrder(OIHANA);
   }

   @Test
   public void findByFirstnameSorted() {
      personRepository.save(JULIEN);
      personRepository.save(RAMON);
      List<Person> people = personRepository.findByBigSisterFalse(Sort.by("id"));
      assertThat(people).containsExactly(ELAIA, JULIEN, RAMON);
   }

   @Test
   public void findPageable() {
      Iterable<Person> people = personRepository.findAll(Sort.by("firstname").ascending());
      assertThat(people).hasSize(2);
      assertThat(people).containsExactly(ELAIA, OIHANA);

      Iterable<Person> people2 = personRepository.findAll(Sort.by("firstname").descending());
      assertThat(people2).hasSize(2);
      assertThat(people2).containsExactly(OIHANA, ELAIA);
   }

   @Test
   public void findPageableSorted() {
      personRepository.save(JULIEN);
      personRepository.save(RAMON);
      Page<Person> people = personRepository.findAll(PageRequest.of(0, 2, Sort.by("id").descending()));
      assertThat(people).hasSize(2);
      assertThat(people).containsExactly(RAMON, JULIEN);
      Page<Person> people1 = personRepository.findAll(PageRequest.of(1, 2, Sort.by("id").descending()));
      assertThat(people1).containsExactly(ELAIA, OIHANA);
      assertThat(people).hasSize(2);
   }

   @Test
   public void findByFirstnameRegex() {
      assertThrows(UnsupportedOperationException.class, () -> personRepository.findByFirstnameRegex("o*"));
   }

   @Test
   public void findOneByFirstnameAsync() throws Exception {
      CompletableFuture<Person> personCf = personRepository.findOneByFirstname(OIHANA.getFirstname());
      Person person = personCf.get(1000, TimeUnit.SECONDS);

      assertThat(person).isEqualTo(OIHANA);
   }

   @Test
   public void peopleWithTheirFirstnameIsOihana() {
      List<Person> people = personRepository.peopleWithTheirFirstnameIsOihana();
      assertThat(people).containsExactly(OIHANA);
   }

   @Test
   public void countByFirstname() {
      assertThat(personRepository.countByFirstname("nobody")).isEqualTo(0);
      assertThat(personRepository.countByFirstname("oihana")).isEqualTo(1);
   }
}
