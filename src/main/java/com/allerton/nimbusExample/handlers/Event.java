package com.allerton.nimbusExample.handlers;

import com.nimbusframework.nimbuscore.annotation.annotations.document.DocumentStore;
import com.nimbusframework.nimbuscore.annotation.annotations.persistent.Attribute;
import com.nimbusframework.nimbuscore.annotation.annotations.persistent.Key;

import java.util.Objects;

@DocumentStore
public class Event {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return numberInterested == event.numberInterested &&
                Objects.equals(id, event.id) &&
                Objects.equals(name, event.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, numberInterested);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", numberInterested=" + numberInterested +
                '}';
    }

    @Key
    private String id;

    @Attribute
    private String name;

    @Attribute
    private int numberInterested = 0;


    public Event() {}

    public Event(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void addInterested() {
        numberInterested++;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumberInterested() {
        return numberInterested;
    }

    public void setNumberInterested(int numberInterested) {
        this.numberInterested = numberInterested;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
