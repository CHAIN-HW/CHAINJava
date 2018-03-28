package chain.sql;

import java.util.List;

import chain.sql.visitors.SQLSelectTableVisitor;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;

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

    public void setSelectTableName(String name) {
        SQLSelectTableVisitor visitor = new SQLSelectTableVisitor(name);
        stmt.accept(visitor);
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

    public String toSQL() { return this.stmt.toString(); }
}
