package com.allerton.nimbusExample.handlers;

import annotation.annotations.function.WebSocketServerlessFunction;
import com.allerton.nimbusExample.models.WebSocketMessage;
import wrappers.websocket.models.WebSocketEvent;

public class WebSocketApi {

    @WebSocketServerlessFunction(routeKey = "$connect")
    public void onConnect(WebSocketEvent event) {
        System.out.println("New connection from " + event.getRequestContext().getConnectionId());
    }

    @WebSocketServerlessFunction(routeKey = "$disconnect")
    public void onDisconnect(WebSocketEvent event) {
        System.out.println(event.getRequestContext().getConnectionId() + " disconnected");
    }

    @WebSocketServerlessFunction(routeKey = "message")
    public void onMessage(WebSocketMessage message, WebSocketEvent event) {
        System.out.println("Got " + message + " from " + event.getRequestContext().getConnectionId());
    }
}
