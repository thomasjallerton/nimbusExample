package com.allerton.nimbusExample.models;

import annotation.annotations.document.DocumentStore;
import annotation.annotations.persistent.Attribute;
import annotation.annotations.persistent.Key;

@DocumentStore
public class UserDetail {

    @Key
    private String username = "";

    @Attribute
    private String currentWebsocket = null;

    public UserDetail() {}

    public UserDetail(String username, String currentWebsocket) {
        this.username = username;
        this.currentWebsocket = currentWebsocket;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrentWebsocket() {
        return currentWebsocket;
    }

    public void setCurrentWebsocket(String currentWebsocket) {
        this.currentWebsocket = currentWebsocket;
    }
}
