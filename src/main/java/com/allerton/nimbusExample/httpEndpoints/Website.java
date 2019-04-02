package com.allerton.nimbusExample.httpEndpoints;

import annotation.annotations.deployment.FileUpload;
import annotation.annotations.file.FileStorageBucket;


@FileStorageBucket(
        bucketName = Website.WEBSITE_BUCKET,
        staticWebsite = true
)
@FileUpload(bucketName = Website.WEBSITE_BUCKET,
            localPath = "src/website",
            targetPath = "",
            substituteNimbusVariables = true)
public class Website {
    public static final String WEBSITE_BUCKET = "NimbusExampleWebsite";
}

