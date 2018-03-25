package chain.sql;

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
    private SQLAdapter adapter;

    /**
     * SQLDatabase constructor
     */
    SQLDatabase(SQLAdapter adapter) throws SQLException {
        this.dbTables = new HashMap<>();
        this.adapter = adapter;
        retrieveDbStructure();
        this.adapter.closeConnection();
    }

    /**
     * Retrieves structure of database
     */
    private void retrieveDbStructure() throws SQLException {
        Statement state = this.adapter.getConnection().createStatement();
        state.executeQuery("SHOW TABLES");
        ResultSet tables  = state.getResultSet();

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

        Statement getColumns = this.adapter.getConnection().createStatement();
        ResultSet tableColumns;

        getColumns.executeQuery("DESCRIBE " + tableName);
        Set<String>  columnSet  = new HashSet<>();
        tableColumns = getColumns.getResultSet();
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
