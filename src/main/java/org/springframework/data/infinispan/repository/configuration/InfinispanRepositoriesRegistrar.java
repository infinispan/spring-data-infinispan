package org.springframework.data.infinispan.repository.configuration;

import java.lang.annotation.Annotation;

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

public class InfinispanRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {
   @Override
   protected Class<? extends Annotation> getAnnotation() {
      return EnableInfinispanRepositories.class;
   }

   @Override
   protected RepositoryConfigurationExtension getExtension() {
      return null;
   }
}
