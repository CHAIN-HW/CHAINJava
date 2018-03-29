package chain.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SQLNameMatcher {

    private List<String> tableNames;
    private SQLDatabase database;
    private SPSMMatcher matcher;

    public SQLNameMatcher(List<String> tableNames, SQLDatabase database, SPSMMatcher matcher) {
        this.tableNames = tableNames;
        this.database = database;
        this.matcher = matcher;
    }

    public Map<String, String> getReplacementTableNames() {
        Map<String, String> replacements = new HashMap<>();

        for(String tableName : tableNames) {
            if(!database.containsTable(tableName)) {
                String replacement = matcher.match(tableName, database.getTableNames());
            }
        }

        return replacements;
    }



}
