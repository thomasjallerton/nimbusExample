package com.allerton.nimbusExample.httpEndpoints;

import com.allerton.nimbusExample.models.ConnectionDetail;
import com.allerton.nimbusExample.models.UserDetail;
import com.nimbusframework.nimbuscore.annotation.annotations.document.UsesDocumentStore;
import com.nimbusframework.nimbuscore.annotation.annotations.function.HttpMethod;
import com.nimbusframework.nimbuscore.annotation.annotations.function.HttpServerlessFunction;
import com.nimbusframework.nimbuscore.annotation.annotations.keyvalue.UsesKeyValueStore;
import com.nimbusframework.nimbuscore.clients.ClientBuilder;
import com.nimbusframework.nimbuscore.clients.document.DocumentStoreClient;
import com.nimbusframework.nimbuscore.clients.keyvalue.KeyValueStoreClient;
import com.nimbusframework.nimbuscore.wrappers.http.models.HttpResponse;

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

    @HttpServerlessFunction(method = HttpMethod.POST, path = "register")
    @UsesDocumentStore(dataModel = UserDetail.class)
    public void register(String username) {
        userDetails.put(new UserDetail(username, null));
    }

    @HttpServerlessFunction(method = HttpMethod.POST, path = "person")
    public HttpResponse getPerson() {
        return new HttpResponse().withBody("Not Implemented").withStatusCode(501);
    }

    public class Person {
        public String firstName;
        public String lastName;

        @Override
        public String toString() {
            return "Person{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    '}';
        }
    }
}
