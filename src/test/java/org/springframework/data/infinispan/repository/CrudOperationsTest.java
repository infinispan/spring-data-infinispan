package org.springframework.data.infinispan.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.infinispan.test.example.TestData.ELAIA;
import static org.infinispan.test.example.TestData.OIHANA;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.test.common.InfinispanSpringDataTest;
import org.infinispan.test.example.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

/**
 * Verifies the integration of Infinispan with the {@link CrudRepository}
 *
 * @author Katia Aresti
 */
public class CrudOperationsTest extends InfinispanSpringDataTest {

   @Resource
   private CrudRepository<Person, String> personRepository;

   static RemoteCache<String, Person> peopleCache;

   @BeforeAll
   public static void beforeAll(@Autowired RemoteCacheManager remoteCacheManager) {
      peopleCache = remoteCacheManager.getCache("people");
   }

   @Test
   public void findAll() {
      long peopleCacheSize = peopleCache.values().size();

      Iterable<Person> all = personRepository.findAll();
      long personRepoSize = Lists.newArrayList(all).size();

      assertThat(peopleCacheSize).isEqualTo(2);
      assertThat(personRepoSize).isEqualTo(2);
   }

   @Test
   public void findById() {
      Person cachePerson = peopleCache.get(OIHANA.getId());
      Optional<Person> repoPerson = personRepository.findById(OIHANA.getId());

      assertThat(cachePerson).isEqualTo(OIHANA);
      assertThat(repoPerson.isPresent()).isTrue();
      assertThat(repoPerson.get()).isEqualTo(OIHANA);

      Person notExisting = peopleCache.get("nil");
      Optional<Person> repoNotExisting = personRepository.findById("nil");
      assertThat(notExisting).isNull();
      assertThat(repoNotExisting.isPresent()).isFalse();
   }

   @Test
   public void findAllById() {
      Iterable<String> ids = Arrays.asList(OIHANA.getId(), ELAIA.getId(), "nil");

      Map<String, Person> cacheAllById = peopleCache.getAll(Sets.newHashSet(ids));

      List<Person> repositoryAllById = Lists.newArrayList(personRepository.findAllById(ids));

      assertThat(cacheAllById).hasSize(2);
      assertThat(cacheAllById).containsOnlyKeys(OIHANA.getId(), ELAIA.getId());
      assertThat(cacheAllById).containsValues(OIHANA, ELAIA);

      assertThat(repositoryAllById).hasSize(2);
      assertThat(repositoryAllById).containsExactlyInAnyOrder(OIHANA, ELAIA);
   }

   @Test
   public void count() {
      long size = peopleCache.size();
      long count = personRepository.count();
      assertThat(size).isEqualTo(2);
      assertThat(count).isEqualTo(2);
   }

   @Test
   public void existsById() {
      assertThat(personRepository.existsById(OIHANA.getId())).isTrue();
      assertThat(personRepository.existsById("nil")).isFalse();
   }

   @Test
   public void saveAndDeleteById() {
      Person person = new Person("3", "julien", "rossito", false, false);

      Person save = personRepository.save(person);
      assertThat(save).isEqualTo(person);

      assertThat(peopleCache.get("3")).isEqualTo(person);

      personRepository.deleteById("3");
      assertThat(peopleCache.get("3")).isNull();
   }

   @Test
   public void saveAllAndDeleteAll() {
      Person person1 = new Person("3", "julien", "rossito", false, false);
      Person person2 = new Person("4", "ramon", "steinur", false, false);

      Iterable<Person> people = Arrays.asList(person1, person2);
      personRepository.saveAll(people);

      assertThat(peopleCache.get("3")).isEqualTo(person1);
      assertThat(peopleCache.get("4")).isEqualTo(person2);

      personRepository.deleteAll(people);
      assertThat(peopleCache.get("3")).isNull();
      assertThat(peopleCache.get("4")).isNull();
   }

   @Test
   public void saveAndDelete() {
      Person person1 = new Person("3", "julien", "rossito", false, false);
      personRepository.save(person1);

      assertThat(peopleCache.get("3")).isEqualTo(person1);

      personRepository.delete(person1);
      assertThat(peopleCache.get("3")).isNull();
   }
}
