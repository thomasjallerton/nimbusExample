package handlers;

import annotation.annotations.deployment.AfterDeployment;
import annotation.annotations.file.FileStorageEventType;
import annotation.annotations.file.UsesFileStorageClient;
import annotation.annotations.function.FileStorageServerlessFunction;
import annotation.annotations.notification.UsesNotificationTopic;
import clients.ClientBuilder;
import clients.file.FileStorageClient;
import clients.notification.NotificationClient;
import clients.notification.Protocol;
import wrappers.file.models.FileStorageEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class FileStorageHandlers {

    private final String FILE_UPDATE_TOPIC = "fileUpdates";
    private final String FILE_BUCKET = "nimbusExampleBucket";
    private NotificationClient notificationClient = ClientBuilder.getNotificationClient(FILE_UPDATE_TOPIC);
    private FileStorageClient fileStorageClient = ClientBuilder.getFileStorageClient(FILE_BUCKET);

    @FileStorageServerlessFunction(bucketName = FILE_BUCKET, eventType = FileStorageEventType.OBJECT_CREATED)
    @UsesNotificationTopic(topic = FILE_UPDATE_TOPIC)
    @UsesFileStorageClient(bucketName = FILE_BUCKET)
    public void newObject(FileStorageEvent event) {
        String message = "New file added: " + event.getKey() + " with size " + event.getSize() + " bytes";

        String result;
        if (event.getSize() < 10000 && event.getKey().endsWith(".txt")) {
            InputStream file = fileStorageClient.getFile(event.getKey());
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(file))) {
                result = buffer.lines().collect(Collectors.joining("\n"));
                message += " Content: " + result;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Sending message " + message);
        notificationClient.notify(message);
    }

    @FileStorageServerlessFunction(bucketName = FILE_BUCKET, eventType = FileStorageEventType.OBJECT_DELETED)
    @UsesNotificationTopic(topic = FILE_UPDATE_TOPIC)
    public void deletedObj(FileStorageEvent event) {
        String message = "File deleted: " + event.getKey();
        notificationClient.notify(message);
    }

    @AfterDeployment
    @UsesNotificationTopic(topic = FILE_UPDATE_TOPIC)
    public String addSubscription() {
        String id = notificationClient.createSubscription(Protocol.SMS, "+447872646190");
        return "Added subscription with ID: " + id;
    }
}
