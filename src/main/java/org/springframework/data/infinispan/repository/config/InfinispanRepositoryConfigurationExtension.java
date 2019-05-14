package org.springframework.data.infinispan.repository.config;

import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.data.infinispan.InfinispanKeyValueAdapter;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryConfigurationSource;

public class InfinispanRepositoryConfigurationExtension extends KeyValueRepositoryConfigurationExtension {

   @Override
   public String getModuleName() {
      return "Infinispan";
   }

   @Override
   protected String getModulePrefix() {
      return "infinispan";
   }

   @Override
   protected String getDefaultKeyValueTemplateRef() {
      return "keyValueTemplate";
   }

   @Override
   public void registerBeansForRoot(BeanDefinitionRegistry registry, RepositoryConfigurationSource configurationSource) {
      RootBeanDefinition infinispanKeyValueAdapterDefinition = new RootBeanDefinition(InfinispanKeyValueAdapter.class);
      ConstructorArgumentValues constructorArguments = new ConstructorArgumentValues();
      constructorArguments
            .addIndexedArgumentValue(0, new RuntimeBeanReference("remoteCacheManager"));

      infinispanKeyValueAdapterDefinition.setConstructorArgumentValues(constructorArguments);
      registerIfNotAlreadyRegistered(() -> infinispanKeyValueAdapterDefinition, registry, "infinispanKeyValueAdapter",
            configurationSource);

      super.registerBeansForRoot(registry, configurationSource);
   }

   @Override
   protected AbstractBeanDefinition getDefaultKeyValueTemplateBeanDefinition(RepositoryConfigurationSource configurationSource) {
      RootBeanDefinition keyValueTemplateDefinition = new RootBeanDefinition(KeyValueTemplate.class);
      ConstructorArgumentValues constructorArgumentValuesForKeyValueTemplate = new ConstructorArgumentValues();
      constructorArgumentValuesForKeyValueTemplate
            .addIndexedArgumentValue(0, new RuntimeBeanReference("infinispanKeyValueAdapter"));
      constructorArgumentValuesForKeyValueTemplate
            .addIndexedArgumentValue(1, new RuntimeBeanReference(MAPPING_CONTEXT_BEAN_NAME));

      keyValueTemplateDefinition.setConstructorArgumentValues(constructorArgumentValuesForKeyValueTemplate);

      return keyValueTemplateDefinition;
   }
}
