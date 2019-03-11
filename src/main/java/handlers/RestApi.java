package handlers;

import annotation.annotations.document.UsesDocumentStore;
import annotation.annotations.function.HttpMethod;
import annotation.annotations.function.HttpServerlessFunction;
import clients.ClientBuilder;
import clients.document.DocumentStoreClient;
import models.Person;
import wrappers.http.models.HttpEvent;

public class RestApi {

    private DocumentStoreClient<Person> personStore = ClientBuilder.getDocumentStoreClient(Person.class);

    @HttpServerlessFunction(method = HttpMethod.GET, path = "person")
    @HttpServerlessFunction(method = HttpMethod.GET, path = "person", stage = "prod")
    @UsesDocumentStore(dataModel = Person.class)
    @UsesDocumentStore(dataModel = Person.class, stage = "prod")
    public Person getPerson(HttpEvent httpEvent) {
        String name = httpEvent.getQueryStringParameters().get("name");
        return personStore.get(name);
    }

    @HttpServerlessFunction(method = HttpMethod.POST, path = "person")
    @HttpServerlessFunction(method = HttpMethod.POST, path = "person", stage = "prod")
    @UsesDocumentStore(dataModel = Person.class)
    @UsesDocumentStore(dataModel = Person.class, stage = "prod")
    public String addPerson(Person person) {
        personStore.put(person);
        return "Successfully added";
    }
}
