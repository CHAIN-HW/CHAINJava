package chain.sql;

import chain.sql.visitors.SQLSelectColumnVisitor;
import chain.sql.visitors.SQLSelectTableVisitor;
import it.unitn.disi.smatch.SMatchException;
import net.sf.jsqlparser.statement.Statement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * SQLQueryRepair
 *
 * Repair a repair process on the defined query
 */
public class SQLQueryRepair {

    private SQLQueryAnalyser analyser;
    private SQLDatabase db;
    private Statement stmt;
    private SQLNameMatcherManager manager;


    /**
     * Constructor for SQLQueryRepair
     * @param query is the SQL query that being repaired
     * @param db is the structure of the database being queried
     * @throws ChainDataSourceException
     * @throws SQLException
     */
    public SQLQueryRepair(String query, SQLDatabase db) throws ChainDataSourceException {
        this.analyser = new SQLQueryAnalyser(query);
        this.stmt = analyser.getStatement();
        this.db = db;
        List<String> tables =  this.analyser.getTables();
        List<String> columns = this.analyser.getColumns();

        manager = new SQLNameMatcherManager(tables, columns, this.db);
    }

    /**
     * Runs the repair process and returns the repaired query
     * @return repaired query as a string
     * @throws WordNetMatchingException
     * @throws SMatchException
     */
    public String runRepairer() throws ChainDataSourceException {
        try {
            repairTables();
            repairColumns();
            return getQueryFromTree();
        } catch (SMatchException | WordNetMatchingException | NoReplacementFoundException e) {
            throw new ChainDataSourceException("Failed to repair query", e);
        }
    }

    /**
     * Gets the query from the tree.  If repairs have been done, it will be repaired
     * @return The query from the AST
     */
    public String getQueryFromTree() {
        return this.analyser.toSQL();
    }

    /**
     * Repairs table names present in the query
     * @throws WordNetMatchingException
     * @throws SMatchException
     */
    private void repairTables() throws WordNetMatchingException, SMatchException {
        Map<String, String> newTableNames = manager.getReplacementTableNames();

        for(String key: newTableNames.keySet() )
            repairTableName(newTableNames.get(key));
    }

    /**
     * Repairs column names present in the query
     */
    private void repairColumns() throws SMatchException, NoReplacementFoundException {
        Map<String, String> columnReplacements = manager.getReplacementColumnNames();
        repairColumnNames(columnReplacements);
    }

    /**
     * Replaces table names
     * @param name what the table should be renamed
     */
    private void repairTableName(String name) {
        SQLSelectTableVisitor visitor = new SQLSelectTableVisitor(name);
        stmt.accept(visitor);
    }

    public void repairColumnNames(Map<String, String> columnReplacements) {
        SQLSelectColumnVisitor visitor = new SQLSelectColumnVisitor(columnReplacements);
        stmt.accept(visitor);
    }
}
