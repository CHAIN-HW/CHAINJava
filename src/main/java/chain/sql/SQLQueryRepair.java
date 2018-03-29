package chain.sql;

import chain.sql.visitors.SQLSelectTableVisitor;
import it.unitn.disi.smatch.SMatchException;
import net.sf.jsqlparser.statement.Statement;
import org.slf4j.impl.StaticMarkerBinder;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.List;
import java.util.Map;

/**
 * @author "Lewis McNeill"
 *
 */
public class SQLQueryRepair {

    private SQLQueryAnalyser analyser;
    private SQLDatabase db;
    private Statement stmt;


    public SQLQueryRepair(String query, SQLDatabase db) throws ChainDataSourceException, SQLException {
        this.analyser = new SQLQueryAnalyser(query);
        this.stmt = analyser.getStatement();
        this.db = db;
    }

    public String runRepairer() throws SPSMMatchingException, SMatchException {
        repairTables();
        return this.analyser.toSQL();
    }

    private void repairTables() throws SPSMMatchingException, SMatchException {
        List<String> tables =  this.analyser.getTables();
        SQLNameMatcherManager manager = new SQLNameMatcherManager(tables, this.db);
        Map<String, String> newTableNames = manager.getReplacementTableNames();

        for(String key: newTableNames.keySet() )
            repairTableName(newTableNames.get(key));

    }

    private void repairColumns()
    {

    }

    private void repairTableName(String name) {
        SQLSelectTableVisitor visitor = new SQLSelectTableVisitor(name);
        stmt.accept(visitor);
    }






}
