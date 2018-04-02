package chain.sql;

import java.util.ArrayList;
import java.util.List;

import chain.sql.visitors.GetSelectColumnVisitor;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
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
     * Constructor
     * @param query - the SQL query to be analysed.
     * @throws ChainDataSourceException An exception related to data sources
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


    /**
     * Get the statement associated with this instance of SQLQueryAnalyser
     * @return the statement provided by parsing the query.
     */
    public Statement getStatement() {
        return stmt;
    }

    /**
     * Get a list of the tables associated with this statement.
     * @return a list of Strings containing table names.
     */
    public List<String> getTables() {
        return (new TablesNamesFinder()).getTableList(this.stmt);
    }

    /**
     * 
     * @return The column names
     */
    public List<String> getColumns() {
        Select fullStatement = (Select) this.stmt;
        PlainSelect sel = (PlainSelect) fullStatement.getSelectBody();
        Expression where = sel.getWhere();
        List<String> columnNames = recurseWhereToColumns(where);
        columnNames.addAll(getColumnNamesFromSelect(sel));
        return columnNames;
    }

    /**
     * 
     * @param where
     * @return An ArrayList of column names
     */
    // TODO: could probably return the actual column objects and update them instead of having to go back in with the visitor
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

    private List<String> getColumnNamesFromSelect(PlainSelect select) {
        GetSelectColumnVisitor visitor = new GetSelectColumnVisitor();
        for(SelectItem selItem : select.getSelectItems()) {
            selItem.accept(visitor);
        }
        return visitor.getColumnNames();
    }

    /**
     * Converts the analysed query back into a string
     * @return the query as a string
     */
    public String toSQL() { return this.stmt.toString(); }
}
