package com.allerton.nimbusExample.handlers;


import com.allerton.nimbusExample.models.ConnectionDetail;
import com.allerton.nimbusExample.models.Message;
import com.allerton.nimbusExample.models.UserDetail;
import com.allerton.nimbusExample.models.WebSocketMessage;
import com.nimbusframework.nimbuscore.annotation.annotations.deployment.AfterDeployment;
import com.nimbusframework.nimbuscore.annotation.annotations.document.UsesDocumentStore;
import com.nimbusframework.nimbuscore.annotation.annotations.function.EnvironmentVariable;
import com.nimbusframework.nimbuscore.annotation.annotations.function.WebSocketServerlessFunction;
import com.nimbusframework.nimbuscore.annotation.annotations.keyvalue.UsesKeyValueStore;
import com.nimbusframework.nimbuscore.annotation.annotations.websocket.UsesServerlessFunctionWebSocketClient;
import com.nimbusframework.nimbuscore.clients.ClientBuilder;
import com.nimbusframework.nimbuscore.clients.document.DocumentStoreClient;
import com.nimbusframework.nimbuscore.clients.keyvalue.KeyValueStoreClient;
import com.nimbusframework.nimbuscore.clients.websocket.ServerlessFunctionWebSocketClient;
import com.nimbusframework.nimbuscore.wrappers.websocket.models.WebSocketEvent;

public class WebSocketApi {

    private ServerlessFunctionWebSocketClient webSocketClient = ClientBuilder.getServerlessFunctionWebSocketClient();
    private DocumentStoreClient<UserDetail> userDetails = ClientBuilder.getDocumentStoreClient(UserDetail.class);
    private KeyValueStoreClient<String, ConnectionDetail> connectionDetails = ClientBuilder.getKeyValueStoreClient(String.class, ConnectionDetail.class);

    @WebSocketServerlessFunction(topic = "$connect")
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

    @WebSocketServerlessFunction(topic = "$disconnect")
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

    @WebSocketServerlessFunction(topic = "sendMessage")
    @UsesServerlessFunctionWebSocketClient
    @UsesDocumentStore(dataModel = UserDetail.class)
    @UsesKeyValueStore(dataModel = ConnectionDetail.class)
    @EnvironmentVariable(key = "IMPORTANT", value = "VALUE", testValue = "NOTVALUE")
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
        UserDetail bob = new UserDetail("sian", null);

        userDetails.put(thomas);
        userDetails.put(bob);
    }
}
