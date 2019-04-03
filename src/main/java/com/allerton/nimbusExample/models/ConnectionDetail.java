package com.allerton.nimbusExample.models;

import annotation.annotations.keyvalue.KeyValueStore;
import annotation.annotations.persistent.Attribute;

@KeyValueStore(keyType = String.class)
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
