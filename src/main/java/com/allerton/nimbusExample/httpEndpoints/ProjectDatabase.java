package com.allerton.nimbusExample.httpEndpoints;

import annotation.annotations.database.DatabaseLanguage;
import annotation.annotations.database.RelationalDatabase;

@RelationalDatabase(
        name = "NimbusExampleDatabase",
        username = "master",
        password = "87sfghdh892sk",
        databaseLanguage = DatabaseLanguage.MYSQL
)
public class ProjectDatabase {}
