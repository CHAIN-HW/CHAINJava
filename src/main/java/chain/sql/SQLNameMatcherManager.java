package chain.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLNameMatcherManager {

    private List<String> tableNames;
    private SQLDatabase database;
    private SPSMMatcher matcher;

    public SQLNameMatcherManager(List<String> tableNames, SQLDatabase database, SPSMMatcher matcher) {
        this.tableNames = tableNames;
        this.database = database;
        this.matcher = matcher;
    }

    public Map<String, String> getReplacementTableNames() {
        Map<String, String> replacements = new HashMap<>();

        for(String tableName : tableNames) {
            if(!database.containsTable(tableName)) {
                String replacement = matcher.match(tableName, database.getTableNames());
                replacements.put(tableName, replacement);
            }
        }

        return replacements;
    }



}
