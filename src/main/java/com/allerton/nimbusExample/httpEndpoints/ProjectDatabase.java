package com.allerton.nimbusExample.httpEndpoints;

import com.nimbusframework.nimbuscore.annotation.annotations.database.DatabaseLanguage;
import com.nimbusframework.nimbuscore.annotation.annotations.database.RelationalDatabase;

@RelationalDatabase(
        name = "NimbusExampleDatabase",
        username = "master",
        password = "87sfghdh892sk",
        databaseLanguage = DatabaseLanguage.MYSQL
)
public class ProjectDatabase {}
