package org.infinispan.test.common;

import static org.infinispan.query.remote.client.ProtobufMetadataManagerConstants.PROTOBUF_METADATA_CACHE_NAME;
import static org.infinispan.test.example.TestData.ELAIA;
import static org.infinispan.test.example.TestData.OIHANA;

import java.io.IOException;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.marshall.ProtoStreamMarshaller;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.annotations.ProtoSchemaBuilder;
import org.infinispan.test.example.Person;
import org.infinispan.test.example.TestConfig;
import org.infinispan.test.jupiter.InfinispanServerExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * JUnit 5 base clase for testing using {@link InfinispanServerExtension}
 */
@SpringJUnitConfig({TestConfig.class, InfinispanSpringDataTest.InfinispanServerConfg.class})
public abstract class InfinispanSpringDataTest {

   @RegisterExtension
   public static InfinispanServerExtension server = InfinispanServerExtension.builder().withCaches("people").stopAfterAll(false).build();

   @Configuration
   public static class InfinispanServerConfg {
      @Bean
      public RemoteCacheManager remoteCacheManager() {
         RemoteCacheManager cacheManager = server.hotRodClient();
         return cacheManager;
      }
   }

   @BeforeAll
   public static void serializationContext() throws IOException {
      // Get the serialization context of the client
      RemoteCacheManager remoteCacheManager = server.hotRodClient();
      SerializationContext ctx = ProtoStreamMarshaller.getSerializationContext(remoteCacheManager);

      // Use ProtoSchemaBuilder to define a Protobuf schema on the client
      ProtoSchemaBuilder protoSchemaBuilder = new ProtoSchemaBuilder();
      String fileName = "person.proto";
      String protoFile = protoSchemaBuilder
            .packageName(Person.class.getPackage().getName())
            .fileName(fileName)
            .addClass(Person.class)
            .build(ctx);

      // Retrieve metadata cache
      RemoteCache<String, String> metadataCache =
            remoteCacheManager.getCache(PROTOBUF_METADATA_CACHE_NAME);

      // Define the new schema on the server too
      metadataCache.put(fileName, protoFile);
   }

   @BeforeEach
   public void insertData(@Autowired RemoteCacheManager cacheManager) {
      RemoteCache<String, Person> people = cacheManager.getCache("people");
      people.put(OIHANA.getId(), OIHANA);
      people.put(ELAIA.getId(), ELAIA);
   }

   @AfterEach
   public void clear(@Autowired RemoteCacheManager cacheManager) {
      RemoteCache<String, Person> people = cacheManager.getCache("people");
      people.clear();
   }
}
