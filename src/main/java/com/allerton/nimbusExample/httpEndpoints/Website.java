package com.allerton.nimbusExample.httpEndpoints;

import annotation.annotations.deployment.FileUpload;
import annotation.annotations.file.FileStorageBucket;

@FileStorageBucket(
        bucketName = "NimbusExampleWebsite",
        staticWebsite = true
)
@FileUpload(bucketName = "NimbusExampleWebsite", localPath = "src/website", targetPath = "")
public class Website {}
