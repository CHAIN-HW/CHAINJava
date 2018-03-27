package chain.sql;

import java.util.List;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.util.TablesNamesFinder;

/**
 * SQLQueryAnalyser
 *
 * Class for performing analysis of SQL queries
 * leveraging the JSQLParser library.
 *
 * @author Daniel Waghorn
 */
public class SQLQueryAnalyser {

    protected Statement stmt;

    /**
     * @param query
     * @throws ChainDataSourceException
     */
    public SQLQueryAnalyser(String query) throws ChainDataSourceException {
        try {
            this.stmt = CCJSqlParserUtil.parse(query);
        } catch (JSQLParserException ex) {
            throw new ChainDataSourceException(
                    "Failed to analyse query. Please ensure a valid SQL query is supplied",
                    ex
            );
        }
    }


    public void analyseAndRepair() {
//        StatementVisitor visitor = new RepairVisitor();
//        stmt.accept(visitor);
    }

    /**
     * @return
     */
    public Statement getStatement() {
        return stmt;
    }

    /**
     * @return
     */
    public List<String> getTables() {
        return (new TablesNamesFinder()).getTableList(this.stmt);
    }


}
