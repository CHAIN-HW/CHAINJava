package chain.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * @author Lewis McNeill
 *
 * Stores the tables present in the database
 */
public class SQLDatabase {


    private Map<String, SQLTable> dbTables;
    private Connection connection;

    /**
     * SQLDatabase constructor
     */
    SQLDatabase(Connection connection) throws SQLException {
        this.dbTables = new HashMap<>();
        this.connection = connection;
        retrieveDbStructure();
    }

    /**
     * Retrieves structure of database
     */
    private void retrieveDbStructure() throws SQLException {
        SQLQueryRunner runner = new SQLQueryRunner("SHOW TABLES", this.connection);
        ResultSet tables  = runner.getResults();
        while(tables.next()) {
            String table = tables.getString(1);
            dbTables.put(table, new SQLTable(table, retrieveTableColumns(table)));
        }
    }

    /**
     * Retrieves all the column names belonging to a table
     * @param tableName Table for which the columns should be found
     * @return A Set of strings containing the column names
     * @throws SQLException Handles any exceptions causes by Mysql
     */
    private Set<String> retrieveTableColumns(String tableName) throws SQLException {

        SQLQueryRunner runner = new SQLQueryRunner("DESCRIBE " + tableName, this.connection);
        ResultSet tableColumns = runner.getResults();
        Set<String>  columnSet  = new HashSet<>();
        while(tableColumns.next())
            columnSet.add(tableColumns.getString(1));
        return columnSet;
    }


    /**
     * Retrieves SQLTable object related to name of table
     * @param tableName Name of table being retrieved
     * @return SQLTable if table exists otherwise returns null
     */
    public SQLTable getTable(String tableName)
    {
        return dbTables.get(tableName);
    }

    /**
     * Checks if table exists in database
     * @param tableName Name of table being checked
     * @return True if table exists false otherwise
     */
    public boolean containsTable(String tableName)
    {
        return dbTables.containsKey(tableName);
    }
}
