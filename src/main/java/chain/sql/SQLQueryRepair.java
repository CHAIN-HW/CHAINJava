package chain.sql;

import chain.sql.visitors.SQLSelectColumnVisitor;
import chain.sql.visitors.SQLSelectTableVisitor;
import it.unitn.disi.smatch.SMatchException;
import net.sf.jsqlparser.statement.Statement;

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
    private SQLNameMatcherManager manager;

    public SQLQueryRepair(String query, SQLDatabase db) throws ChainDataSourceException, SQLException {
        this.analyser = new SQLQueryAnalyser(query);
        this.stmt = analyser.getStatement();
        this.db = db;
        List<String> tables =  this.analyser.getTables();
        List<String> columns = this.analyser.getColumns();

        manager = new SQLNameMatcherManager(tables, columns, this.db);
    }

    public String runRepairer() throws SPSMMatchingException, SMatchException {
        repairTables();
        repairColumns();
        return getQueryFromTree();
    }

    /**
     * Gets the query from the tree.  If repairs have been done, it will be repaired
     * @return The query from the AST
     */
    public String getQueryFromTree() {
        return this.analyser.toSQL();
    }

    private void repairTables() throws SPSMMatchingException, SMatchException {
        Map<String, String> newTableNames = manager.getReplacementTableNames();

        for(String key: newTableNames.keySet() )
            repairTableName(newTableNames.get(key));
    }

    private void repairColumns() throws SPSMMatchingException, SMatchException {
        Map<String, String> columnReplacements = manager.getReplacementColumnNames();
        repairColumnNames(columnReplacements);
    }

    private void repairTableName(String name) {
        SQLSelectTableVisitor visitor = new SQLSelectTableVisitor(name);
        stmt.accept(visitor);
    }

    public void repairColumnNames(Map<String, String> columnReplacements) {
        SQLSelectColumnVisitor visitor = new SQLSelectColumnVisitor(columnReplacements);
        stmt.accept(visitor);
    }






}
