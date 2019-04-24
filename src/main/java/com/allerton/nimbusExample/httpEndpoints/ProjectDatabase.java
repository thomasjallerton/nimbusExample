package com.allerton.nimbusExample.httpEndpoints;

import com.nimbusframework.nimbuscore.annotation.annotations.database.DatabaseLanguage;
import com.nimbusframework.nimbuscore.annotation.annotations.database.RelationalDatabase;
import com.nimbusframework.nimbuscore.annotation.annotations.database.UsesRelationalDatabase;
import com.nimbusframework.nimbuscore.annotation.annotations.deployment.AfterDeployment;
import com.nimbusframework.nimbuscore.clients.ClientBuilder;
import com.nimbusframework.nimbuscore.clients.rdbms.DatabaseClient;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;

@RelationalDatabase(
        name = "NimbusExampleDatabase",
        username = "master",
        password = "87sfghdh892sk",
        databaseLanguage = DatabaseLanguage.MYSQL
)
public class ProjectDatabase {

    @AfterDeployment
    @UsesRelationalDatabase(dataModel = ProjectDatabase.class)
    public String migrationSchema()  {
        DatabaseClient databaseClient = ClientBuilder.getDatabaseClient(ProjectDatabase.class);
        Connection connection = databaseClient.getConnection("nimbusExample", true);

        try {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new liquibase.Liquibase("changelog.xml", new ClassLoaderResourceAccessor(), database);

            liquibase.update(new Contexts(), new LabelExpression());

        } catch (LiquibaseException exception) {
            return "Liquibase failed: " + exception.getLocalizedMessage();
        }
        return "Successfully migrated";
    }
}
