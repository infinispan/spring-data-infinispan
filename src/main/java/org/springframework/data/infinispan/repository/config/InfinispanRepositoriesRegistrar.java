package org.springframework.data.infinispan.repository.config;

import java.lang.annotation.Annotation;

import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

/**
 * Infinispan specific {@link ImportBeanDefinitionRegistrar}.
 *
 * @author Katia Aresti
 */
public class InfinispanRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {
   @Override
   protected Class<? extends Annotation> getAnnotation() {
      return EnableInfinispanRepositories.class;
   }

   @Override
   protected RepositoryConfigurationExtension getExtension() {
      return new InfinispanRepositoryConfigurationExtension();
   }
}
