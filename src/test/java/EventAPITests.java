import com.allerton.nimbusExample.models.Event;
import com.nimbusframework.nimbuscore.annotation.annotations.function.HttpMethod;
import com.nimbusframework.nimbuscore.testing.LocalNimbusDeployment;
import com.nimbusframework.nimbuscore.testing.document.LocalDocumentStore;
import com.nimbusframework.nimbuscore.testing.http.HttpRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        Event eventRequest = new Event("TestEvent");
        eventRequest.setId("testEventId");

        request.setBodyFromObject(eventRequest);

        localNimbusDeployment.sendHttpRequest(request);

        assertEquals(1, localEvents.size());
        assertEquals(eventRequest, localEvents.get("testEventId"));

    }
}
