package org.infinispan.test.example;

import java.io.Serializable;

import org.infinispan.protostream.annotations.ProtoField;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import lombok.Data;

@KeySpace("people")
@Data
public class Person implements Serializable {

   @Id
   @ProtoField(number = 1)
   String id;

   @ProtoField(number = 2)
   String firstname;

   @ProtoField(number = 3)
   String lastname;

   public Person() {
   }

   public Person(String firstname, String lastname) {
      this.firstname = firstname;
      this.lastname = lastname;
   }
}
