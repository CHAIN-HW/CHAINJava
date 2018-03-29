package chain.sql;

import chain.sql.visitors.SPSMMatchingException;
import it.unitn.disi.smatch.SMatchException;

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

    public Map<String, String> getReplacementTableNames() throws SMatchException, SPSMMatchingException {
        Map<String, String> replacements = new HashMap<>();

        for(String tableName : tableNames) {
            if(!database.containsTable(tableName)) {
                String replacement = matcher.match(tableName);
            }
        }

        return replacements;
    }



}
