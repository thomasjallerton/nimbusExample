package com.allerton.nimbusExample.handlers;

import com.allerton.nimbusExample.models.Message;
import com.nimbusframework.nimbuscore.annotation.annotations.deployment.AfterDeployment;
import com.nimbusframework.nimbuscore.annotation.annotations.function.QueueServerlessFunction;
import com.nimbusframework.nimbuscore.annotation.annotations.queue.UsesQueue;
import com.nimbusframework.nimbuscore.clients.ClientBuilder;
import com.nimbusframework.nimbuscore.clients.queue.QueueClient;

public class QueueHandlers {

    @QueueServerlessFunction(id = "MessageQueue", batchSize = 1)
    public void queueHandler(Message message) {
        System.out.println("Processed message " + message);
    }

    @AfterDeployment
    @UsesQueue(id = "MessageQueue")
    public String initialMessage() {
        QueueClient queueClient = ClientBuilder.getQueueClient("MessageQueue");
        queueClient.sendMessageAsJson(new Message("There was a new deployment", "NIMBUS EXAMPLE"));
        return "Sent message";
    }

}
