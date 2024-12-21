package com.github.deroq1337.bansystem.bungee.data.database;

import com.github.deroq1337.bansystem.bungee.data.database.result.DBResult;
import com.github.deroq1337.bansystem.bungee.data.database.result.DBRow;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class MySQL {

    private @NotNull final HikariConfig config;
    private Optional<HikariDataSource> dataSource = Optional.empty();

    public MySQL() {
        this.config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/ban-system?autoReconnect=true"); // TODO: config file
        config.setUsername("root");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }

    public void connect() {
        if (dataSource.isEmpty()) {
            dataSource = Optional.of(new HikariDataSource(config));
            System.out.println("Initialized DataSource");
        }
    }

    public void disconnect() {
        dataSource.ifPresent(ds -> {
            ds.close();
            System.out.println("Closed DataSource");
        });
    }

    public @NotNull CompletableFuture<Integer> update(@NotNull String sql, @NotNull Object... params) {
        return dataSource.map(ds -> CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement(sql);
                bindParams(statement, params);
                return statement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error executing update: " + e.getMessage());
                return -1;
            }
        })).orElseThrow(() -> new RuntimeException("Error creating query: no MySQL connection established"));
    }

    public @NotNull CompletableFuture<Integer> update(@NotNull Connection connection, @NotNull String sql, @NotNull Object... params) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                bindParams(statement, params);
                return statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public @NotNull CompletableFuture<Void> executeTransaction(@NotNull Function<Connection, CompletableFuture<Integer>> consumer) {
        return dataSource.map(ds -> CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection()) {
                connection.setAutoCommit(false);

                try {
                    consumer.apply(connection).join();
                    connection.commit();
                    System.out.println("Transaction committed successfully");
                } catch (Exception e) {
                    try {
                        connection.rollback();
                        System.err.println("Transaction rolled back due to error: " + e.getMessage());
                    } catch (SQLException e1) {
                        System.err.println("Error during rollback: " + e1.getMessage());
                    }
                } finally {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Error getting connection for transaction: " + e.getMessage());
            }
        })).orElseThrow(() -> new RuntimeException("Error creating query: no MySQL connection established"));
    }

    public @NotNull CompletableFuture<DBResult> query(@NotNull String sql, @NotNull Object... params) {
        return dataSource.map(ds -> CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement(sql);
                bindParams(statement, params);

                ResultSet result = statement.executeQuery();
                List<DBRow> rows = new ArrayList<>();
                while (result.next()) {
                    ResultSetMetaData resultMetaData = result.getMetaData();
                    Map<String, Object> columns = new HashMap<>();

                    for (int columnIndex = 1; columnIndex <= resultMetaData.getColumnCount(); columnIndex++) {
                        String columnName = resultMetaData.getColumnName(columnIndex);
                        columns.put(columnName, result.getObject(columnName));
                    }

                    rows.add(new DBRow(columns));
                }

                return new DBResult(rows);
            } catch (SQLException e) {
                System.err.println("Error executing query: " + e.getMessage());
                return new DBResult(new ArrayList<>());
            }
        })).orElseThrow(() -> new RuntimeException("Error creating query: no MySQL connection established"));
    }

    private @NotNull Connection getConnection() {
        return dataSource.map(ds -> {
            try {
                return ds.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException("Error getting connection from pool: " + e.getMessage());
            }
        }).orElseThrow(() -> new RuntimeException("DataSource was not initialized yet"));
    }

    private void bindParams(@NotNull PreparedStatement statement, @NotNull Object... params) throws SQLException {
        for (int index = 0; index < params.length; index++) {
            int paramIndex = index + 1;
            statement.setObject(paramIndex, params[index]);
        }
    }
}