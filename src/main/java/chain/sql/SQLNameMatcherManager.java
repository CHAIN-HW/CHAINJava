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

    private List<String> tableNames;
    private SQLDatabase database;
    private SPSMMatcher matcher;

    /**
     * Constructor for SQLNameMatcherManager that generates its own SPSMMatcher
     * @param tableNames List of names for which replacement should be found
     * @param database Structure of database being queried
     */
    public SQLNameMatcherManager(List<String> tableNames, SQLDatabase database) {
        this.tableNames = tableNames;
        this.database = database;
        this.matcher = new SPSMMatcher(database.getTableNames());
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
        this.matcher = matcher;
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
                String replacement = matcher.match(tableName);
                replacements.put(tableName, replacement);
            }
        }

        return replacements;
    }



}
