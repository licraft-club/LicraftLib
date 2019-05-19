package com.licrafter.lib.db;

import com.licrafter.lib.db.table.FieldleInterface;
import com.licrafter.lib.db.table.TableInterface;
import com.licrafter.lib.log.BLog;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by shell on 2019/5/18.
 * <p>
 * Gmail: shellljx@gmail.com
 */
public abstract class Dao {
    private ConnectPool connectPool;
    protected JavaPlugin plugin;
    protected TableInterface[] tableInterface;

    protected Dao(JavaPlugin plugin, String driverName, String url, String username, String password, String prefix) {
        this.plugin = plugin;
        try {
            connectPool = new ConnectPool(plugin, driverName, url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            BLog.warning(plugin, e.getLocalizedMessage());
        }
    }


    public void initialize(TableInterface... tableInterface) {
        this.tableInterface = tableInterface;
        try {
            setUp();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public final synchronized void setUp() throws SQLException {

        try {
            for (TableInterface table : tableInterface) {
                createDefaultTable(table);
            }
            checkDefaultCollumns();
        } catch (Exception e) {
        }
    }

    private boolean createDefaultTable(TableInterface table) {
        if (isTable(table.getTableName()))
            return true;
        try {
            createTable(table.getSql());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkDefaultCollumns() {
        for (TableInterface table : tableInterface) {
            for (FieldleInterface field : table.getFieldsInterface()) {
                if (isCollumn(table.getTableName(), field.getName()))
                    continue;
                addCollumn(table.getTableName(), field.getName(), field.getType());
            }
        }

        return true;
    }

    protected abstract void setupConfig() throws SQLException;

    public abstract boolean isTable(String table);

    public abstract boolean createTable(String query) throws SQLException;

    public abstract boolean isCollumn(String table, String collumn);

    public abstract boolean addCollumn(String table, String collumn, String type);

    public abstract boolean truncate(String table);

    public abstract boolean drop(String table);

    /**
     * Get a database connection
     *
     * @return DBConnection object
     * @throws SQLException
     */
    protected ConnectionWrapper getConnection() {
        try {
            return connectPool.getConnection();
        } catch (SQLException e) {
            BLog.consoleMessage("Unable to connect to the database: " + e.getMessage());
            return null;
        }
    }

    /**
     * Close all active database handles
     */
    public synchronized void closeConnections() {
        connectPool.closeConnection();
    }

    protected static void close(ResultSet res) {
        if (res != null)
            try {
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    protected static void close(Statement stmt) {
        if (stmt != null)
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}
