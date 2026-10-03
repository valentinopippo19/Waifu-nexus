package com.waifu.persistence;

/** Singleton que representa la conexión compartida a la persistencia. */
public final class DatabaseConnection {
    private static final DatabaseConnection INSTANCE = new DatabaseConnection();

    private DatabaseConnection() { }

    public static DatabaseConnection getInstance() { return INSTANCE; }

    public void open() { /* conexión simulada */ }
}
