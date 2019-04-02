package com.allerton.nimbusExample.handlers;

import annotation.annotations.document.UsesDocumentStore;
import annotation.annotations.function.WebSocketServerlessFunction;
import annotation.annotations.websocket.UsesServerlessFunctionWebSocketClient;
import clients.ClientBuilder;
import clients.document.DocumentStoreClient;
import clients.websocket.ServerlessFunctionWebSocketClient;
import com.allerton.nimbusExample.models.Message;
import com.allerton.nimbusExample.models.UserDetail;
import com.allerton.nimbusExample.models.WebSocketMessage;
import wrappers.websocket.models.WebSocketEvent;

public class WebSocketApi {

    private ServerlessFunctionWebSocketClient webSocketClient = ClientBuilder.getServerlessFunctionWebSocketClient();
    private DocumentStoreClient<UserDetail> userDetails = ClientBuilder.getDocumentStoreClient(UserDetail.class);

    @WebSocketServerlessFunction(routeKey = "$connect")
    @UsesDocumentStore(dataModel = UserDetail.class)
    public void onConnect(WebSocketEvent event) {
        String username = event.getQueryStringParameters().get("user");
        userDetails.put(new UserDetail(username, event.getRequestContext().getConnectionId()));
    }

    @WebSocketServerlessFunction(routeKey = "$disconnect")
    @UsesDocumentStore(dataModel = UserDetail.class)
    public void onDisconnect(WebSocketEvent event) {
        String username = event.getQueryStringParameters().get("user");
        userDetails.deleteKey(username);
    }

    @WebSocketServerlessFunction(routeKey = "sendMessage")
    @UsesServerlessFunctionWebSocketClient
    @UsesDocumentStore(dataModel = UserDetail.class)
    public void onMessage(WebSocketMessage message, WebSocketEvent event) {
        UserDetail userDetail = userDetails.get(message.getRecipient());
        System.out.println("Sending message " + message.getMessage() + " to " + userDetail.getUsername());
        webSocketClient.sendToConnectionConvertToJson(userDetail.getConnectionId(), new Message(message.getMessage(), "TODO"));
    }
}
