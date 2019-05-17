package org.infinispan.test.example;


import java.util.List;

import org.springframework.data.infinispan.repository.InfinispanRepository;

public interface PersonRepository extends InfinispanRepository<Person, String> {
   List<Person> findByFirstname(String firstname);

   List<Person> findByFirstnameNot(String firstname);

   List<Person> findByLastnameIsNull();

   List<Person> findByLastnameIsNotNull();

   List<Person> findByIsBasqueTrue();

   List<Person> findByIsBigSisterFalse();

   Long countByFirstname(String firstname);
}
