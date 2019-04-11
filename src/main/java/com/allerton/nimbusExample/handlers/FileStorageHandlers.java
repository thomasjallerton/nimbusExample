package com.allerton.nimbusExample.handlers;

import com.nimbusframework.nimbuscore.annotation.annotations.deployment.AfterDeployment;
import com.nimbusframework.nimbuscore.annotation.annotations.file.FileStorageEventType;
import com.nimbusframework.nimbuscore.annotation.annotations.file.UsesFileStorageClient;
import com.nimbusframework.nimbuscore.annotation.annotations.function.*;
import com.nimbusframework.nimbuscore.annotation.annotations.notification.UsesNotificationTopic;
import com.nimbusframework.nimbuscore.clients.ClientBuilder;
import com.nimbusframework.nimbuscore.clients.file.FileStorageClient;
import com.nimbusframework.nimbuscore.clients.function.BasicServerlessFunctionClient;
import com.nimbusframework.nimbuscore.clients.notification.NotificationClient;
import com.nimbusframework.nimbuscore.clients.notification.Protocol;
import com.nimbusframework.nimbuscore.wrappers.file.models.FileStorageEvent;
import com.nimbusframework.nimbuscore.wrappers.queue.models.QueueEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
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

    @BasicServerlessFunction
    public long getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        return cal.getTimeInMillis();
    }

    @HttpServerlessFunction(path="files", method = HttpMethod.POST)
    @UsesBasicServerlessFunctionClient
    @UsesFileStorageClient(bucketName = FILE_BUCKET)
    public String newFile(String contents) {
        BasicServerlessFunctionClient functionClient = ClientBuilder.getBasicServerlessFunctionClient();
        FileStorageClient fileClient = ClientBuilder.getFileStorageClient(FILE_BUCKET);

        String fileName = functionClient.invoke(FileStorageHandlers.class, "getCurrentTime", long.class).toString();

        fileClient.saveFile(fileName + ".txt", contents);
        return "Successfully saved file";
    }
}
