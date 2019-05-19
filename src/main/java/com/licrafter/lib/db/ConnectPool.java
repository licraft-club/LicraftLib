package com.licrafter.lib.db;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by shell on 2019/5/18.
 * <p>
 * Gmail: shellljx@gmail.com
 */
public class ConnectPool {

    private ConnectionWrapper connectionWrapper;
    private String url;
    private String username;
    private String password;

    public ConnectPool(JavaPlugin plugin, String driverName, String url, String username, String password) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        Driver driver = (Driver) Class.forName(driverName, true, plugin.getClass().getClassLoader()).newInstance();
        DriverManager.registerDriver(driver);
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public synchronized ConnectionWrapper getConnection() throws SQLException {
        if (connectionWrapper != null && (connectionWrapper.isClosed() || !connectionWrapper.isValid(1))) {
            try {
                connectionWrapper.closeConnection();
            } catch (SQLException e) {
            }
            connectionWrapper = null;
        }

        if (connectionWrapper == null) {
            Connection conn = DriverManager.getConnection(url, username, password);
            connectionWrapper = new ConnectionWrapper(conn);
        }

        return connectionWrapper;
    }

    public synchronized void closeConnection() {
        if (connectionWrapper != null) {
            try {
                connectionWrapper.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
