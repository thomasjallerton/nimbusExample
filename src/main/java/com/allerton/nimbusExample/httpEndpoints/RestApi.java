package com.allerton.nimbusExample.httpEndpoints;

import annotation.annotations.document.UsesDocumentStore;
import annotation.annotations.function.HttpMethod;
import annotation.annotations.function.HttpServerlessFunction;
import annotation.annotations.keyvalue.UsesKeyValueStore;
import clients.ClientBuilder;
import clients.document.DocumentStoreClient;
import clients.keyvalue.KeyValueStoreClient;
import com.allerton.nimbusExample.models.ConnectionDetail;
import com.allerton.nimbusExample.models.UserDetail;

import java.util.List;
import java.util.Map;

public class RestApi {

    private DocumentStoreClient<UserDetail> userDetails = ClientBuilder.getDocumentStoreClient(UserDetail.class);
    private KeyValueStoreClient<String, ConnectionDetail> connectionDetails = ClientBuilder.getKeyValueStoreClient(String.class, ConnectionDetail.class);

    @HttpServerlessFunction(method = HttpMethod.GET, path = "userdetail")
    @UsesDocumentStore(dataModel = UserDetail.class)
    public List<UserDetail> getUserDetails() {
        return userDetails.getAll();
    }

    @HttpServerlessFunction(method = HttpMethod.GET, path = "connectiondetail")
    @UsesKeyValueStore(dataModel = ConnectionDetail.class)
    public Map<String, ConnectionDetail> getConnectionDetail() {
        return connectionDetails.getAll();
    }
}
