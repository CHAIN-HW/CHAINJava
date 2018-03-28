package chain.sql;

import java.util.ArrayList;
import java.util.List;

import chain.sql.visitors.SQLSelectTableVisitor;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
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

    public List<String> getWhereColumns() {
        Select fullStatement = (Select) this.stmt;
        PlainSelect sel = (PlainSelect) fullStatement.getSelectBody();
        Expression where = sel.getWhere();

        return this.recurseWhereToColumns(where);
    }

    private List<String> recurseWhereToColumns(Expression where) {
        if (where instanceof Column) {
            ArrayList<String> ret = new ArrayList<>();
            ret.add(((Column) where).getColumnName());
            return ret;
        }

        ArrayList<String> ret = new ArrayList<>();
        if (where instanceof BinaryExpression) {
            ret.addAll(this.recurseWhereToColumns(((BinaryExpression) where).getLeftExpression()));
            ret.addAll(this.recurseWhereToColumns(((BinaryExpression) where).getRightExpression()));
        }
        return ret;
    }

    public String toSQL() { return this.stmt.toString(); }
}
