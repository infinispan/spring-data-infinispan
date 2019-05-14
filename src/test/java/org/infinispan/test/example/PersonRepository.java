package org.infinispan.test.example;


import java.util.List;

import org.springframework.data.infinispan.repository.InfinispanRepository;

public interface PersonRepository extends InfinispanRepository<Person, String> {
   List<Person> findByFirstname(String firstname);
}
