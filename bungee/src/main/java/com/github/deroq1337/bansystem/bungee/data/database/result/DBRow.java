package com.github.deroq1337.bansystem.bungee.data.database.result;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

public record DBRow(@NotNull Map<String, Object> columns) {

    public @NotNull Set<String> getColumnNames() {
        return columns.keySet();
    }

    public <T> @NotNull T getValue(@NotNull String columnName, @NotNull Class<T> clazz) {
        if (!columns.containsKey(columnName)) {
            throw new NoSuchElementException("Column '" + columnName + "' not found in row");
        }

        return Optional.ofNullable((T) columns.get(columnName))
                .orElseThrow(() -> new NullPointerException("Value of column '" + columnName + "' is null"));
    }

    public <T> Optional<T> getValueOptional(@NotNull String columnName, @NotNull Class<T> clazz) {
        Optional<T> optional = Optional.ofNullable((T) columns.get(columnName.toLowerCase()));
        if (optional.isEmpty()) {
            System.out.println("Value of column '" + columnName + "' is empty");
        }

        return optional;
    }
}