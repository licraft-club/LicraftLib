package com.licrafter.lib.db;

import java.sql.*;

/**
 * Created by shell on 2019/5/18.
 * <p>
 * Gmail: shellljx@gmail.com
 */
public class ConnectionWrapper {
    private Connection connection;

    public ConnectionWrapper(Connection connection) {
        this.connection = connection;
    }

    public synchronized boolean isClosed() {
        try {
            return connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    public synchronized boolean isValid(int timeout) throws SQLException {
        try {
            return connection.isValid(timeout);
        } catch (AbstractMethodError e) {
            return true;
        }
    }

    public synchronized void closeConnection() throws SQLException {
        connection.close();
    }

    public synchronized Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    public synchronized PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public synchronized PreparedStatement prepareStatement(String sql, int returnGeneratedKeys) throws SQLException {
        return connection.prepareStatement(sql, returnGeneratedKeys);
    }

    public synchronized void setAutoCommit(Boolean mode) throws SQLException {
        connection.setAutoCommit(mode);
    }

    public synchronized void commit() throws SQLException {
        connection.commit();
    }

    public synchronized DatabaseMetaData getMetaData() throws SQLException {
        return connection.getMetaData();
    }
}
