package chain.sql;

import chain.sql.visitors.SQLSelectTableVisitor;
import it.unitn.disi.smatch.SMatchException;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.SelectUtils;
import org.slf4j.impl.StaticMarkerBinder;

import java.sql.Connection;
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

    /**
     * Constructor for SQLQueryRepair
     * @param query is the SQL query that being repaired
     * @param db is the structure of the database being queried
     * @throws ChainDataSourceException
     * @throws SQLException
     */
    public SQLQueryRepair(String query, SQLDatabase db) throws ChainDataSourceException, SQLException {
        this.analyser = new SQLQueryAnalyser(query);
        this.stmt = analyser.getStatement();
        this.db = db;
    }

    /**
     * Runs the repair process and returns the repaired query
     * @return repaired query as a string
     * @throws SPSMMatchingException
     * @throws SMatchException
     */
    public String runRepairer() throws SPSMMatchingException, SMatchException {
        repairTables();
        return this.analyser.toSQL();
    }

    /**
     * Repairs table names present in the query
     * @throws SPSMMatchingException
     * @throws SMatchException
     */
    private void repairTables() throws SPSMMatchingException, SMatchException {
        List<String> tables =  this.analyser.getTables();
        SQLNameMatcherManager manager = new SQLNameMatcherManager(tables, this.db);
        Map<String, String> newTableNames = manager.getReplacementTableNames();

        for(String key: newTableNames.keySet() )
            repairTableName(newTableNames.get(key));

    }

    /**
     * Repairs column names present in the query
     */
    private void repairColumns()
    {

    }

    /**
     * Replaces table names
     * @param name what the table should be renamed
     */
    private void repairTableName(String name) {
        SQLSelectTableVisitor visitor = new SQLSelectTableVisitor(name);
        stmt.accept(visitor);
    }

    private void repairColumnName()
    {

    }






}
