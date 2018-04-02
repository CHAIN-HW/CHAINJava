package chain.sql;

import java.util.Set;


/**
 * SQLTable
 *
 * Represents an the structure of an sql table
 */
public class SQLTable {

    private String tableName;
    private Set<String> columnNames;

    /**
     * Constructs an object for an individual table
     * @param tableName Name of the table
     * @param columnNames Set of columns stored in the table
     */
    SQLTable(String tableName, Set<String> columnNames)
    {
        this.tableName = tableName;
        this.columnNames = columnNames;
    }

    /**
     * getTableName
     *
     * Returns the name of the current table instance.
     *
     * @return The table name as string
     */
    public String getTableName()
    {
        return this.tableName;
    }

    /**
     * getColumnNames
     *
     * Returns a set of column names in the table.
     *
     * @return Set containing all column names
     */
    public Set<String> getColumnNames()
    {
        return this.columnNames;
    }

    /**
     * Checks if column exists in table
     * @param column Name of column being looked for
     * @return True if column exists false otherwise
     */
    public boolean containsColumn(String column)
    {
        return columnNames.contains(column);
    }
}
