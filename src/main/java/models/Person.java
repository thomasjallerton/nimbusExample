package models;

import annotation.annotations.document.DocumentStore;
import annotation.annotations.persistent.Attribute;
import annotation.annotations.persistent.Key;

@DocumentStore
@DocumentStore(stage = "prod")
public class Person {

    @Key
    public String fullName;

    @Attribute
    public int age;

}
