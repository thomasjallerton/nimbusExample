package com.allerton.nimbusExample.httpEndpoints;

import annotation.annotations.deployment.AfterDeployment;
import annotation.annotations.file.FileStorageBucket;
import annotation.annotations.file.UsesFileStorageClient;
import clients.ClientBuilder;
import clients.file.FileStorageClient;

@FileStorageBucket(
        bucketName = "NimbusExampleWebsite",
        staticWebsite = true
)
public class Website {

    @AfterDeployment
    @UsesFileStorageClient(bucketName = "NimbusExampleWebsite")
    public String uploadFiles() {
        FileStorageClient websiteStorage = ClientBuilder.getFileStorageClient("NimbusExampleWebsite");

        websiteStorage.saveFileWithContentType("index.html",
                "<html>\n" +
                        "<p>Hello World!</p>\n" +
                        "<p>This is nimbus example's webpage</p>\n" +
                        "<p>&nbsp;</p>\n" +
                        "</html>", "text/html");

        return "Successfully saved index file";
    }
}
