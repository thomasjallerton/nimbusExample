import com.allerton.nimbusExample.models.Event;
import com.nimbusframework.nimbuscore.annotation.annotations.function.HttpMethod;
import com.nimbusframework.nimbuscore.testing.LocalNimbusDeployment;
import com.nimbusframework.nimbuscore.testing.document.LocalDocumentStore;
import com.nimbusframework.nimbuscore.testing.http.HttpRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EventAPITests {

    @Test
    public void addEventDoesAddToTable() {
        LocalNimbusDeployment localNimbusDeployment =
                LocalNimbusDeployment.getNewInstance("com.allerton.nimbusExample");

        LocalDocumentStore<Event> localEvents =
                localNimbusDeployment.getDocumentStore(Event.class);

        //Make sure events is empty
        assertEquals(0, localEvents.size());

        HttpRequest request = new HttpRequest(
                "event",
                HttpMethod.POST);

        Event eventRequest = new Event("TestEvent", "testEventId");
        eventRequest.setId("testEventId");

        request.setBodyFromObject(eventRequest);

        localNimbusDeployment.sendHttpRequest(request);

        assertEquals(1, localEvents.size());
        assertEquals(eventRequest, localEvents.get("testEventId"));

    }

    @Test
    public void addEventWithIdDoesAddToTable() {
        LocalNimbusDeployment localNimbusDeployment =
                LocalNimbusDeployment.getNewInstance("com.allerton.nimbusExample");

        LocalDocumentStore<Event> localEvents =
                localNimbusDeployment.getDocumentStore(Event.class);

        //Make sure events is empty
        assertEquals(0, localEvents.size());

        HttpRequest request = new HttpRequest(
                "event/testEventId",
                HttpMethod.POST);


        request.setBodyFromObject("eventName");

        localNimbusDeployment.sendHttpRequest(request);

        assertEquals(1, localEvents.size());

        assertNotNull(localEvents.get("testEventId"));
    }
}
