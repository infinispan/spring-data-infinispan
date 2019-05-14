package org.infinispan.test.example;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.infinispan.repository.config.EnableInfinispanRepositories;

@Configuration
@EnableInfinispanRepositories(basePackages = "org.infinispan.test.example")
public class TestConfig {

}
