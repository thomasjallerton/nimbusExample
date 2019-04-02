package com.allerton.nimbusExample.models;

import annotation.annotations.document.DocumentStore;
import annotation.annotations.persistent.Attribute;
import annotation.annotations.persistent.Key;

@DocumentStore
public class UserDetail {

    @Key
    private String username = "";

    @Attribute
    private String connectionId = "";

    public UserDetail() {}

    public UserDetail(String username, String connectionId) {
        this.username = username;
        this.connectionId = connectionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }
}
