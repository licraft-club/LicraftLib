package com.licrafter.lib.db;

import com.licrafter.lib.db.table.TableInterface;
import com.licrafter.lib.log.BLog;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by shell on 2019/5/18.
 * <p>
 * Gmail: shellljx@gmail.com
 */
public class Mysql extends Dao {
    private String database;

    public Mysql(JavaPlugin plugin, String hostname, String database, String username, String password, String prefix, boolean certificate, boolean ssl, boolean autoReconnect) {
        super(plugin, "com.mysql.jdbc.Driver", "jdbc:mysql://" + hostname + "/" + database + "?useUnicode=true&characterEncoding=UTF-8&autoReConnect=" + autoReconnect + "&useSSL=" + ssl
                + "&verifyServerCertificate=" + certificate, username, password, prefix);
        this.database = database;
    }

    @Override
    public void initialize(TableInterface... tableInterface) {
        try {
            super.initialize(tableInterface);
        } catch (Exception e) {
            BLog.consoleMessage("&cmysql initialize error: " + e.getMessage());
        }
    }

    @Override
    protected void setupConfig() throws SQLException {

    }

    @Override
    public boolean isTable(String table) {
        Statement statement;
        ConnectionWrapper conn = getConnection();
        if (conn == null)
            return false;
        try {
            statement = conn.createStatement();
        } catch (SQLException e) {
            BLog.consoleMessage("&cCould not check if its table, SQLException: " + e.getMessage());
            return false;
        }
        try {
            ResultSet tables = conn.getMetaData().getTables(null, null, table, null);
            if (tables.next()) {
                tables.close();
                return true;
            }
            tables.close();
            return false;
        } catch (SQLException e) {
            BLog.consoleMessage("Not a table |" + "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME ='" + table + "';" + "|");
        }
        try {

            PreparedStatement insert = conn.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME ='" + table + "';");
            ResultSet res = insert.executeQuery();
            if (res.next()) {
                res.close();
                insert.close();
                return true;
            }
            res.close();
            insert.close();
            return false;
        } catch (SQLException e) {
            BLog.consoleMessage("Not a table |" + "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME ='" + table + "';" + "|");
            close(statement);
            return false;
        }
    }

    @Override
    public boolean createTable(String query) throws SQLException {
        BLog.consoleMessage(query);
        Statement statement = null;
        if (query == null || query.equals("")) {
            BLog.consoleMessage("&cCould not create table: query is empty or null.");
            return false;
        }
        ConnectionWrapper conn = getConnection();
        if (conn == null)
            return false;
        try {
            statement = conn.createStatement();
            statement.execute(query);
            statement.close();
        } catch (SQLException e) {
            BLog.consoleMessage("&cCould not create table, SQLException: " + e.getMessage());
            close(statement);
            return false;
        } finally {
            close(statement);
        }
        return true;
    }

    @Override
    public boolean isCollumn(String table, String collumn) {
        Statement statement;
        try {
            statement = getConnection().createStatement();
        } catch (SQLException e) {
            BLog.consoleMessage("&cCould not check if its collumn, SQLException: " + e.getMessage());
            return false;
        }
        try {
            statement.executeQuery("SELECT `" + collumn + "` FROM `" + table + "`;");
            statement.close();
            return true;
        } catch (SQLException e) {
            BLog.consoleMessage("Not a culumn |" + "SELECT " + collumn + " FROM " + table + "|");
            close(statement);
            return false;
        }
    }

    @Override
    public boolean addCollumn(String table, String collumn, String type) {
        Statement statement;
        try {
            statement = getConnection().createStatement();
        } catch (SQLException e) {
            BLog.consoleMessage("&cCould not add new collumn, SQLException: " + e.getMessage());
            return false;
        }
        try {
            BLog.consoleMessage("Creating culumn |" + "ALTER TABLE `" + table + "` ADD COLUMN `" + collumn + "` " + type + ";" + "|");
            statement.executeUpdate("ALTER TABLE `" + table + "` ADD COLUMN `" + collumn + "` " + type + ";");
            statement.close();
            return true;
        } catch (SQLException e) {
            close(statement);
            return false;
        }
    }

    @Override
    public boolean truncate(String table) {
        Statement statement = null;
        String query = null;
        try {
            if (!this.isTable(table)) {
                BLog.consoleMessage("&cTable \"" + table + "\" does not exist.");
                return false;
            }
            statement = getConnection().createStatement();
            query = "DELETE FROM " + table + ";";
            statement.executeUpdate(query);
            statement.close();

            return true;
        } catch (SQLException e) {
            BLog.consoleMessage("&cCould not wipe table, SQLException: " + e.getMessage());
            close(statement);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean drop(String table) {
        Statement statement = null;
        String query = null;
        try {
            if (!this.isTable(table)) {
                BLog.consoleMessage("&cTable \"" + table + "\" does not exist.");
                return false;
            }
            statement = getConnection().createStatement();
            query = "DROP TABLE IF EXISTS `" + table + "`;";
            statement.executeUpdate(query);
            statement.close();

            return true;
        } catch (SQLException e) {
            BLog.consoleMessage("&cCould not wipe table, SQLException: " + e.getMessage());
            close(statement);
            e.printStackTrace();
            return false;
        }
    }
}
