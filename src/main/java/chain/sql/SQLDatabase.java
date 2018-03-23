package chain.sql;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lewis McNeill
 *
 * Stores the tables present in the database
 */
public class SQLDatabase {

    private Map<String, SQLTable> dbTables;

    public SQLDatabase()
    {
        dbTables = new HashMap<>();
    }


    /**
     * Retrieves structure of database
     */
    private void getTableStruct()
    {

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
