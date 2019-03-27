package com.allerton.nimbusExample.httpEndpoints;

import annotation.annotations.document.DocumentStore;
import annotation.annotations.persistent.Attribute;
import annotation.annotations.persistent.Key;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@DocumentStore(stages = {"dev", "prod"})
public class Person {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age &&
                Objects.equals(fullName, person.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, age);
    }

    @Key
    public String fullName = "";

    @Attribute
    public int age = 0;
}
