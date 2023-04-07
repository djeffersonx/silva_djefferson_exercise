package com.ecore.roles.utils;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2DataBaseExtension implements BeforeAllCallback, AfterEachCallback {

    private ApplicationContext applicationContext;
    private DataSource dataSource;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        applicationContext = SpringExtension.getApplicationContext(extensionContext);
        dataSource = applicationContext.getBean(DataSource.class);
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        truncateAllTables();
    }

    private void truncateAllTables() throws SQLException {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement foreignKeyCheck = setForeignKeyChecks(connection);
                PreparedStatement tables = listTables(connection)) {

            try (ResultSet tablesRes = tables.executeQuery()) {
                foreignKeyCheck.setBoolean(1, false);
                foreignKeyCheck.executeUpdate();
                while (tablesRes.next()) {
                    String table = tablesRes.getString(1);
                    try (PreparedStatement truncateTable =
                            connection.prepareStatement("TRUNCATE TABLE " + table + " RESTART IDENTITY")) {
                        truncateTable.executeUpdate();
                    }
                }
            } finally {
                foreignKeyCheck.setBoolean(1, true);
                foreignKeyCheck.executeUpdate();
            }
        }
    }

    private static PreparedStatement listTables(Connection connection) throws SQLException {
        return connection.prepareStatement(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = SCHEMA() and TABLE_NAME not in ('flyway_schema_history')");
    }

    private static PreparedStatement setForeignKeyChecks(Connection connection) throws SQLException {
        return connection.prepareStatement("SET FOREIGN_KEY_CHECKS = ?");
    }

}
