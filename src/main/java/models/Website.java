package models;

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

        websiteStorage.saveHtmlFile("index.html",
                "<html>\n" +
                        "<p>Hello World!</p>\n" +
                        "<p>Welcome to my website&nbsp;this is my first <strong>s3</strong> static hosted site.</p>\n" +
                        "<p>&nbsp;</p>\n" +
                        "</html>");

        return "Successfully saved index file";
    }
}
