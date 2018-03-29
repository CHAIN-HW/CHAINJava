package chain.sql;

import it.unitn.disi.smatch.SMatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLNameMatcherManager {

    private List<String> tableNames;
    private SQLDatabase database;
    private SPSMMatcher matcher;

    public SQLNameMatcherManager(List<String> tableNames, SQLDatabase database) {
        this.tableNames = tableNames;
        this.database = database;
        this.matcher = new SPSMMatcher(database.getTableNames());
    }

    public SQLNameMatcherManager(List<String> tableNames, SQLDatabase database, SPSMMatcher matcher) {
        this.tableNames = tableNames;
        this.database = database;
        this.matcher = matcher;
    }

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
