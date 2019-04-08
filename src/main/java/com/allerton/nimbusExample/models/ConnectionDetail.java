package com.allerton.nimbusExample.models;


import com.nimbusframework.nimbuscore.annotation.annotations.keyvalue.KeyValueStore;
import com.nimbusframework.nimbuscore.annotation.annotations.persistent.Attribute;

@KeyValueStore(keyType = String.class, readCapacityUnits = 1, writeCapacityUnits = 1)
public class ConnectionDetail {

    public ConnectionDetail() {}

    public ConnectionDetail(String username) {
        this.username = username;
    }

    @Attribute
    private String username = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
