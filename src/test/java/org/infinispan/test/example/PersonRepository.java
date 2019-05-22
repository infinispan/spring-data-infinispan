package org.infinispan.test.example;


import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.infinispan.repository.InfinispanRepository;

public interface PersonRepository extends InfinispanRepository<Person, String> {
   List<Person> findByFirstname(String firstname);

   List<Person> findByFirstnameIgnoreCase(String firstname);

   List<Person> findByFirstnameNot(String firstname);

   List<Person> findByFirstnameNotIgnoreCase(String firstname);

   List<Person> findByLastnameIsNull();

   List<Person> findByLastnameIsNotNull();

   List<Person> findByBasqueTrue();

   List<Person> findByBigSisterFalse();

   List<Person> findByAgeBetween(int min, int max);

   List<Person> findByAgeBefore(int age);

   List<Person> findByAgeIsLessThan(int age);

   List<Person> findByAgeIsLessThanEqual(int age);

   List<Person> findByAgeAfter(int age);

   List<Person> findByAgeGreaterThan(int age);

   List<Person> findByAgeGreaterThanEqual(int age);

   List<Person> findByAgeIsIn(Collection<Integer> ages);

   List<Person> findByAgeIsNotIn(Collection<Integer> ages);

   List<Person> findByFirstnameLike(String firstName);

   List<Person> findByFirstnameNotLike(String firstName);

   List<Person> findByFirstnameStartingWith(String firstName);

   List<Person> findByFirstnameEndingWith(String firstName);

   List<Person> findByFirstnameContaining(Collection<String> firstNames);

   List<Person> findByFirstnameNotContaining(Collection<String> firstNames);

   List<Person> findByFirstnameOrLastname(String firstName, String lastName);

   List<Person> findByFirstnameOrAgeBetween(String firstName, int ageMin, int ageMax);

   List<Person> findByFirstnameAndLastname(String firstname, String lastname);

   List<Person> findByBigSisterFalse(Sort sort);

   Long countByFirstname(String firstname);
}
