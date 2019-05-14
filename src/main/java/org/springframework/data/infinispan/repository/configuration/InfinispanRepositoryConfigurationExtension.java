package org.springframework.data.infinispan.repository.configuration;

import org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension;

public class InfinispanRepositoryConfigurationExtension extends KeyValueRepositoryConfigurationExtension {
   @Override
   protected String getDefaultKeyValueTemplateRef() {
      return "infinispanKeyValueTemplate";
   }
}
