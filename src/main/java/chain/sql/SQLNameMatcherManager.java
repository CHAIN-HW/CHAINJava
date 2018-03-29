package chain.sql;

import chain.sql.visitors.SPSMMatchingException;
import it.unitn.disi.smatch.SMatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SQLNameMatcherManager
 * @author 
 * 
 * Create a map from an incorrect name to its replacement matches.
 * Matches are found using the SPSMMatcher. This class organises the data.
 *
 */
public class SQLNameMatcherManager {

    private List<String> tableNames;
    private SQLDatabase database;
    private SPSMMatcher matcher;

    /**
     * Constructor.
     * @param tableNames - a list of strings of table names to be mapped.
     * @param database - the database which contains the given tables.
     * @param matcher - instance of the SPSMMatcher class which performs the matches.
     */
    public SQLNameMatcherManager(List<String> tableNames, SQLDatabase database, SPSMMatcher matcher) {
        this.tableNames = tableNames;
        this.database = database;
        this.matcher = matcher;
    }

    
    /**
     * Gets a HashMap from the old table names to the new matched table names.
     * @return HashMap mapping old table names to replacement table names.
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
