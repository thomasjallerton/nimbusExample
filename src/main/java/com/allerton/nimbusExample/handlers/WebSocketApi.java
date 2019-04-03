package com.allerton.nimbusExample.handlers;

import annotation.annotations.deployment.AfterDeployment;
import annotation.annotations.document.UsesDocumentStore;
import annotation.annotations.function.WebSocketServerlessFunction;
import annotation.annotations.keyvalue.UsesKeyValueStore;
import annotation.annotations.websocket.UsesServerlessFunctionWebSocketClient;
import clients.ClientBuilder;
import clients.document.DocumentStoreClient;
import clients.keyvalue.KeyValueStoreClient;
import clients.websocket.ServerlessFunctionWebSocketClient;
import com.allerton.nimbusExample.models.ConnectionDetail;
import com.allerton.nimbusExample.models.Message;
import com.allerton.nimbusExample.models.UserDetail;
import com.allerton.nimbusExample.models.WebSocketMessage;
import wrappers.websocket.models.WebSocketEvent;

public class WebSocketApi {

    private ServerlessFunctionWebSocketClient webSocketClient = ClientBuilder.getServerlessFunctionWebSocketClient();
    private DocumentStoreClient<UserDetail> userDetails = ClientBuilder.getDocumentStoreClient(UserDetail.class);
    private KeyValueStoreClient<String, ConnectionDetail> connectionDetails = ClientBuilder.getKeyValueStoreClient(String.class, ConnectionDetail.class);

    @WebSocketServerlessFunction(routeKey = "$connect")
    @UsesDocumentStore(dataModel = UserDetail.class)
    @UsesKeyValueStore(dataModel = ConnectionDetail.class)
    public void onConnect(WebSocketEvent event) throws Exception {
        String connectionId = event.getRequestContext().getConnectionId();
        String username = event.getQueryStringParameters().get("user");
        UserDetail validUser = userDetails.get(username);
        if (validUser != null) {
            ConnectionDetail connectionDetail = new ConnectionDetail(username);
            System.out.println("Adding " + connectionDetail.getUsername() + " with connection " + connectionId);
            connectionDetails.put(connectionId, connectionDetail);
            if (validUser.getCurrentWebsocket() != null) {
                connectionDetails.delete(validUser.getCurrentWebsocket());
            }
            validUser.setCurrentWebsocket(connectionId);
            userDetails.put(validUser);
        } else {
            throw new Exception("Not a valid user");
        }
    }

    @WebSocketServerlessFunction(routeKey = "$disconnect")
    @UsesDocumentStore(dataModel = UserDetail.class)
    @UsesKeyValueStore(dataModel = ConnectionDetail.class)
    public void onDisconnect(WebSocketEvent event) {
        String connectionId = event.getRequestContext().getConnectionId();
        ConnectionDetail disconnectedUser = connectionDetails.get(connectionId);
        if (disconnectedUser != null) {
            UserDetail validUser = userDetails.get(disconnectedUser.getUsername());
            if (validUser != null) {
                validUser.setCurrentWebsocket(null);
                userDetails.put(validUser);
            }
        }
        connectionDetails.delete(event.getRequestContext().getConnectionId());
    }

    @WebSocketServerlessFunction(routeKey = "sendMessage")
    @UsesServerlessFunctionWebSocketClient
    @UsesDocumentStore(dataModel = UserDetail.class)
    @UsesKeyValueStore(dataModel = ConnectionDetail.class)
    public void onMessage(WebSocketMessage message, WebSocketEvent event) {
        UserDetail userDetail = userDetails.get(message.getRecipient());
        ConnectionDetail connectionDetail = connectionDetails.get(event.getRequestContext().getConnectionId());
        if (userDetail != null && connectionDetail != null) {
            System.out.println();
            webSocketClient.sendToConnectionConvertToJson(userDetail.getCurrentWebsocket(),
                    new Message(message.getMessage(), connectionDetail.getUsername()));
        }
    }

    @AfterDeployment
    @UsesDocumentStore(dataModel = UserDetail.class)
    public void setupBasicUsers() {
        UserDetail thomas = new UserDetail("thomas", null);
        UserDetail sian = new UserDetail("sian", null);

        userDetails.put(thomas);
        userDetails.put(sian);
    }
}
