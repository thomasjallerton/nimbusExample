package com.allerton.nimbusExample.handlers;

import com.nimbusframework.nimbuscore.annotation.annotations.document.UsesDocumentStore;
import com.nimbusframework.nimbuscore.annotation.annotations.function.HttpMethod;
import com.nimbusframework.nimbuscore.annotation.annotations.function.HttpServerlessFunction;
import com.nimbusframework.nimbuscore.clients.ClientBuilder;
import com.nimbusframework.nimbuscore.clients.document.DocumentStoreClient;
import com.nimbusframework.nimbuscore.wrappers.http.models.HttpEvent;

public class EventAPI {

    private DocumentStoreClient<Event> events = ClientBuilder.getDocumentStoreClient(Event.class);

    @HttpServerlessFunction(path = "event/{id}", method = HttpMethod.GET)
    @UsesDocumentStore(dataModel = Event.class)
    public Event getEvent(HttpEvent httpRequest) {
        String id = httpRequest.getPathParameters().get("id");
        System.out.println("GETTING EVENT WITH ID " + id);
        return events.get(id);
    }

    @HttpServerlessFunction(path = "event", method = HttpMethod.POST)
    @UsesDocumentStore(dataModel = Event.class)
    public String newEvent(Event newEvent) {
        events.put(newEvent);
        return "Successfully created event";
    }

    @HttpServerlessFunction(path = "event/{id}", method = HttpMethod.POST)
    @UsesDocumentStore(dataModel = Event.class)
    public String newEventWithId(String name, HttpEvent request) {
        String id = request.getPathParameters().get("id");
        System.out.println(id);
        Event newEvent = new Event(name, id);
        events.put(newEvent);
        return "Successfully created event " + newEvent;
    }

    @HttpServerlessFunction(
            path = "event/{id}/interest",
            method = HttpMethod.POST)
    @UsesDocumentStore(dataModel = Event.class)
    public String addInterest(HttpEvent httpRequest) {
        String id = httpRequest.getPathParameters().get("id");
        Event event = events.get(id);
        if (event != null) {
            event.addInterested();
            events.put(event);
            return "Event now has " + event.getNumberInterested()
                    + " people interested";
        } else {
            return "Event does not exist";
        }
    }
}
