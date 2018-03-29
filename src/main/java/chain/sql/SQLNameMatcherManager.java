package chain.sql;

import it.unitn.disi.smatch.SMatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SQLNameMatcherManager
 *
 * Manages the process of using SPSMMatcher to find replacement table names
 */
public class SQLNameMatcherManager {

    private List<String> columnNames;
    private List<String> tableNames;
    private SQLDatabase database;
    private SPSMMatcher tableMatcher;

    /**
     * Constructor for SQLNameMatcherManager that generates its own SPSMMatcher
     * @param tableNames List of names for which replacement should be found
     * @param database Structure of database being queried
     */
    public SQLNameMatcherManager(List<String> tableNames, List<String> columnNames, SQLDatabase database) {
        this.tableNames = tableNames;
        this.database = database;
        this.tableMatcher = new SPSMMatcher(database.getTableNames());
        this.columnNames = columnNames;
    }

    /**
     * Constructor for SQLNameMatcherManager that takes a predefined SPSMMatcher object
     * @param tableNames List of names for which replacement should be found
     * @param database Structure of database being queried
     * @param matcher A precreated SPSMMatcher object
     */
    public SQLNameMatcherManager(List<String> tableNames, SQLDatabase database, SPSMMatcher matcher) {
        this.tableNames = tableNames;
        this.database = database;
        this.tableMatcher = matcher;
    }

    /**
     * Goes through each table name provided to it and attempts to find a replacement
     * @return Replacement names as a map in the form <orginal, replacement>
     * @throws SMatchException
     * @throws SPSMMatchingException
     */
    public Map<String, String> getReplacementTableNames() throws SMatchException, SPSMMatchingException {
        Map<String, String> replacements = new HashMap<>();

        for(String tableName : tableNames) {
            if(!database.containsTable(tableName)) {
                String replacement = tableMatcher.match(tableName);
                replacements.put(tableName, replacement);
            }
        }

        return replacements;
    }

    public Map<String, String> getReplacementColumnNames() throws SMatchException, SPSMMatchingException {
        Map<String, String> replacements = new HashMap<>();

        for(SQLTable table : database.getTables()) {
            getReplacementColumnNamesFromTable(replacements, table);
        }
        return replacements;
    }

    private void getReplacementColumnNamesFromTable(Map<String, String> replacements, SQLTable table) throws SMatchException, SPSMMatchingException {

        try {
            SPSMMatcher columnMatcher = new SPSMMatcher(table.getColumnNames());

            // TODO: if column names are with table names, only check for that table
            for(String columnName : columnNames) {
                if(!table.containsColumn(columnName)) {
                    String replacement = columnMatcher.match(columnName);
                    replacements.put(columnName, replacement);
                }
            }
        } catch(SPSMMatchingException e) {
            // Do nothing, no match in this table  TODO: find out if none was found in any table and throw exception
        }
    }

}
