package org.infinispan.test.example;

import java.io.Serializable;
import java.util.Objects;

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

   @ProtoField(number = 4, defaultValue = "false")
   boolean isBasque;

   @ProtoField(number = 5, defaultValue = "false")
   boolean isBigSister;

   @ProtoField(number = 6, defaultValue = "0")
   int age;

   public Person() {
   }

   public Person(String id, String firstname, String lastname, boolean isBasque, boolean isBigSister, int age) {
      this.id = id;
      this.firstname = firstname;
      this.lastname = lastname;
      this.isBasque = isBasque;
      this.isBigSister = isBigSister;
      this.age = age;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Person person = (Person) o;
      return Objects.equals(id, person.id) &&
            Objects.equals(firstname, person.firstname) &&
            Objects.equals(lastname, person.lastname) &&
            Objects.equals(isBasque, person.isBasque) &&
            Objects.equals(isBigSister, person.isBigSister) &&
            Objects.equals(age, person.age);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, firstname, lastname, isBasque, isBigSister, age);
   }
}
