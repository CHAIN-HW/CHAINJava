package chain.sql;

import it.unitn.disi.smatch.SMatchException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SQLNameMatcherManager
 *
 * Manages the process of using WordNetMatcher to find replacement table names
 */
public class SQLNameMatcherManager {

    private List<String> queryColumnNames;
    private List<String> queryTableNames;
    private SQLDatabase database;
    private WordNetMatcher tableMatcher;

    /**
     * Constructor for SQLNameMatcherManager that generates its own WordNetMatcher
     * @param queryTableNames List of names for which replacement should be found
     * @param database Structure of database being queried
     */
    public SQLNameMatcherManager(List<String> queryTableNames, List<String> queryColumnNames, SQLDatabase database) {
        this.queryTableNames = queryTableNames;
        this.queryColumnNames = queryColumnNames;
        this.database = database;
        this.tableMatcher = new WordNetMatcher(database.getTableNames());
    }

    /**
     * Constructor for SQLNameMatcherManager that takes a predefined WordNetMatcher object
     * @param queryTableNames List of names for which replacement should be found
     * @param database Structure of database being queried
     * @param matcher A precreated WordNetMatcher object
     */
    public SQLNameMatcherManager(List<String> queryTableNames, List<String> queryColumnNames, SQLDatabase database, WordNetMatcher matcher) {
        this.queryTableNames = queryTableNames;
        this.queryColumnNames = queryColumnNames;
        this.database = database;
        this.tableMatcher = matcher;
    }

    /**
     * Goes through each table name provided to it and attempts to find a replacement
     * @return Replacement names as a map in the form <orginal, replacement>
     * @throws SMatchException
     * @throws WordNetMatchingException
     */
    public Map<String, String> getReplacementTableNames() throws SMatchException, WordNetMatchingException {
        Map<String, String> replacements = new HashMap<>();

        for(String tableName : queryTableNames) {
            if(!database.containsTable(tableName)) {
                String replacement = tableMatcher.match(tableName);
                replacements.put(tableName, replacement);
            }
        }

        return replacements;
    }

    /**
     *
     * @return Map of original column name to replacement column name
     * @throws SMatchException
     */
    public Map<String, String> getReplacementColumnNames() throws SMatchException, NoReplacementFoundException {
        Map<String, String> replacements = new HashMap<>();
        Collection<SQLTable> tables = database.getTables();


            for(String originalColumnName : queryColumnNames)
            {
                boolean cont = false;
                for(SQLTable table: tables) {
                    if(table.containsColumn(originalColumnName))
                        cont = true;
                }
                if(cont) continue;

                Map<String,String> resultOfReplacementSearch = getReplacementColumnNamesFromTables(originalColumnName, tables);

                if(resultOfReplacementSearch.isEmpty())
                    throw new NoReplacementFoundException(originalColumnName);

                replacements.putAll(resultOfReplacementSearch);
            }

        return replacements;
    }


    private Map<String, String> getReplacementColumnNamesFromTables(String originalColumnName, Collection<SQLTable> tables) throws SMatchException {
        Map<String, String> replacements = new HashMap<>();


            // TODO: if column names are with table names, only check for that table
        for (SQLTable table : tables) {
            try {
                WordNetMatcher columnMatcher = new WordNetMatcher(table.getColumnNames());
                if (!table.containsColumn(originalColumnName)) {
                    String replacement = columnMatcher.match(originalColumnName);
                    replacements.put(originalColumnName, replacement);
                }

            } catch(WordNetMatchingException e){
               // e.printStackTrace();
            }
        }
        return replacements;
    }

}
