package com.allerton.nimbusExample.handlers;

import annotation.annotations.function.WebSocketServerlessFunction;
import annotation.annotations.websocket.UsesServerlessFunctionWebSocketClient;
import clients.ClientBuilder;
import clients.websocket.ServerlessFunctionWebSocketClient;
import com.allerton.nimbusExample.models.WebSocketMessage;
import wrappers.websocket.models.WebSocketEvent;

public class WebSocketApi {

    private ServerlessFunctionWebSocketClient webSocketClient = ClientBuilder.getServerlessFunctionWebSocketClient();

    @WebSocketServerlessFunction(routeKey = "$connect")
    public void onConnect(WebSocketEvent event) {
        System.out.println("New connection from " + event.getRequestContext().getConnectionId());
    }

    @WebSocketServerlessFunction(routeKey = "$disconnect")
    public void onDisconnect(WebSocketEvent event) {
        System.out.println(event.getRequestContext().getConnectionId() + " disconnected");
    }

    @WebSocketServerlessFunction(routeKey = "message")
    @UsesServerlessFunctionWebSocketClient
    public void onMessage(WebSocketMessage message, WebSocketEvent event) {
        System.out.println("Got " + message.getMessage() + " from " + event.getRequestContext().getConnectionId());
        webSocketClient.sendToConnectionConvertToJson(event.getRequestContext().getConnectionId(), "Got Message");
    }
}
