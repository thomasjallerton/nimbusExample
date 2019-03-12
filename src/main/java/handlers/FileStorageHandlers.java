package handlers;

import annotation.annotations.deployment.AfterDeployment;
import annotation.annotations.file.FileStorageEventType;
import annotation.annotations.function.FileStorageServerlessFunction;
import annotation.annotations.notification.UsesNotificationTopic;
import clients.ClientBuilder;
import clients.notification.NotificationClient;
import clients.notification.Protocol;
import wrappers.file.models.FileStorageEvent;

public class FileStorageHandlers {

    private final String FILE_UPDATE_TOPIC = "fileUpdates";
    private NotificationClient notificationClient = ClientBuilder.getNotificationClient(FILE_UPDATE_TOPIC);

    @FileStorageServerlessFunction(bucketName = "exampleBucket", eventType = FileStorageEventType.OBJECT_CREATED)
    @UsesNotificationTopic(topic = FILE_UPDATE_TOPIC)
    public void newObject(FileStorageEvent event) {
        String message = "New file added: " + event.getKey() + " with size " + event.getSize() + " bytes";
        notificationClient.notify(message);
    }

    @FileStorageServerlessFunction(bucketName = "exampleBucket", eventType = FileStorageEventType.OBJECT_DELETED)
    @UsesNotificationTopic(topic = FILE_UPDATE_TOPIC)
    public void deletedObj(FileStorageEvent event) {
        String message = "New file added: " + event.getKey() + " with size " + event.getSize() + " bytes";
        notificationClient.notify(message);
    }

    @AfterDeployment
    @UsesNotificationTopic(topic = FILE_UPDATE_TOPIC)
    public String addSubscription() {
        String id = notificationClient.createSubscription(Protocol.SMS, "+447872646190");
        return "Added subscription with ID: " + id;
    }
}
