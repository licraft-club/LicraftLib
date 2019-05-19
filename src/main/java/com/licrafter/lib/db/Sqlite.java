package com.licrafter.lib.db;

import com.licrafter.lib.db.table.TableInterface;
import com.licrafter.lib.log.BLog;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by shell on 2019/5/18.
 * <p>
 * Gmail: shellljx@gmail.com
 */
public class Sqlite extends Dao {

    public Sqlite(JavaPlugin plugin, File file) {
        super(plugin, "org.sqlite.JDBC", "jdbc:sqlite:" + file.getPath(), null, null, "");
    }

    @Override
    public void initialize(TableInterface... tableInterface) {
        try {
            super.initialize(tableInterface);
        } catch (Exception e) {
            BLog.consoleMessage("&csqlite initialize error: " + e.getMessage());
        }
    }

    @Override
    protected void setupConfig() throws SQLException {

    }

    @Override
    public boolean isTable(String table) {
        DatabaseMetaData md = null;
        try {
            md = this.getConnection().getMetaData();
            ResultSet tables = md.getTables(null, null, table, null);
            if (tables.next()) {
                tables.close();
                return true;
            }
            tables.close();
            return false;
        } catch (SQLException e) {
            BLog.consoleMessage("&cCould not check if table \"" + table + "\" exists, SQLException: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean createTable(String query) throws SQLException {
        Statement statement = null;
        try {
            if (query == null || query.equals("")) {
                BLog.consoleMessage("&cCould not create table: query is empty or null.");
                return false;
            }

            statement = getConnection().createStatement();
            statement.execute(query);
            statement.close();
            return true;
        } catch (SQLException e) {
            BLog.consoleMessage("&cCould not create table, SQLException: " + e.getMessage());
            close(statement);
            return false;
        }
    }

    @Override
    public boolean isCollumn(String table, String collumn) {
        DatabaseMetaData md = null;
        try {
            md = this.getConnection().getMetaData();
            ResultSet tables = md.getColumns(null, null, table, collumn);
            if (tables.next()) {
                tables.close();
                return true;
            }
            tables.close();
            return false;
        } catch (SQLException e) {
            BLog.consoleMessage("&cCould not check if table \"" + table + "\" exists, SQLException: " + e.getMessage());
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
            statement.executeQuery("ALTER TABLE `" + table + "` ADD `" + collumn + "` " + type);
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
            query = "DELETE FROM `" + table + "`;";
            statement.executeQuery(query);
            statement.close();
            return true;
        } catch (SQLException e) {
            if (!(e.getMessage().toLowerCase().contains("locking") || e.getMessage().toLowerCase().contains("locked")) &&
                    !e.toString().contains("not return ResultSet"))
                BLog.consoleMessage("&cError in wipeTable() query: " + e);
            close(statement);
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
            statement.executeQuery(query);
            statement.close();
            return true;
        } catch (SQLException e) {
            if (!(e.getMessage().toLowerCase().contains("locking") || e.getMessage().toLowerCase().contains("locked")) &&
                    !e.toString().contains("not return ResultSet"))
                BLog.consoleMessage("&cError in dropTable() query: " + e);
            close(statement);
            return false;
        } finally {
            close(statement);
        }
    }
}
