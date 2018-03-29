package chain.sql;

import it.unitn.disi.smatch.SMatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLNameMatcherManager {

    private List<String> columnNames;
    private List<String> tableNames;
    private SQLDatabase database;
    private SPSMMatcher tableMatcher;

    public SQLNameMatcherManager(List<String> tableNames, List<String> columnNames, SQLDatabase database) {
        this.tableNames = tableNames;
        this.database = database;
        this.tableMatcher = new SPSMMatcher(database.getTableNames());

        this.columnNames = columnNames;
    }

    public SQLNameMatcherManager(List<String> tableNames, SQLDatabase database, SPSMMatcher matcher) {
        this.tableNames = tableNames;
        this.database = database;
        this.tableMatcher = matcher;
    }

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
