package chain.sql.visitors;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Commit;
import net.sf.jsqlparser.statement.SetStatement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.AlterView;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.merge.Merge;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.upsert.Upsert;
import java.util.Map;

public class SQLSelectColumnVisitor implements StatementVisitor, SelectVisitor, SelectItemVisitor, ExpressionVisitor {

    Map<String, String> replacements;

    public SQLSelectColumnVisitor(Map<String, String> replacements) {
        this.replacements = replacements;
    }

    protected boolean canReplaceColumn(String columnName) {
        return replacements.keySet().contains(columnName);
    }

    protected String columnReplacement(String columnName) {
        if (!canReplaceColumn(columnName)) return columnName;
        return replacements.get(columnName);
    }

    @Override
    public void visit(PlainSelect plainSelect) {
        for(SelectItem selectItem : plainSelect.getSelectItems()) {
            selectItem.accept(this);
        }

        Expression where = plainSelect.getWhere();
        if (where != null) where.accept(this);
    }


    @Override
    public void visit(SelectExpressionItem selectExpressionItem) {
        Expression expr = selectExpressionItem.getExpression();
        if (expr instanceof Column) {
            Column col = (Column) expr;
            String name = col.getColumnName();
            if (canReplaceColumn(name)) {
                // We need to replace the column
                col.setColumnName(columnReplacement(name));
            }
        }
    }

    protected void warnNotImplemented(String param) {
        System.err.println("SQLSelectTableVisitor: Method not implemented - visit(" + param + ")");
    }

    @Override
    public void visit(Select select) {
        select.getSelectBody().accept(this);
    }

    @Override
    public void visit(SetOperationList setOpList) { warnNotImplemented("setOpList"); }

    @Override
    public void visit(WithItem withItem) { warnNotImplemented("withItem"); }

    @Override
    public void visit(Commit commit) { warnNotImplemented("Commit"); }

    @Override
    public void visit(Delete delete) { warnNotImplemented("Delete"); }

    @Override
    public void visit(Update update) { warnNotImplemented("Update"); }

    @Override
    public void visit(Insert insert) { warnNotImplemented("Insert"); }

    @Override
    public void visit(Replace replace) { warnNotImplemented("Replace"); }

    @Override
    public void visit(Drop drop) { warnNotImplemented("Drop"); }

    @Override
    public void visit(Truncate truncate) { warnNotImplemented("Truncate"); }

    @Override
    public void visit(CreateIndex createIndex) { warnNotImplemented("CreateIndex"); }

    @Override
    public void visit(CreateTable createTable) { warnNotImplemented("CreateTable"); }

    @Override
    public void visit(CreateView createView) { warnNotImplemented("CreateView"); }

    @Override
    public void visit(AlterView alterView) { warnNotImplemented("AlterView"); }

    @Override
    public void visit(Alter alter) { warnNotImplemented("Alter"); }

    @Override
    public void visit(Statements stmts) { warnNotImplemented("Statements"); }

    @Override
    public void visit(Execute execute) { warnNotImplemented("Execute"); }

    @Override
    public void visit(SetStatement set) { warnNotImplemented("SetStatement"); }

    @Override
    public void visit(Merge merge) { warnNotImplemented("Merge"); }

    @Override
    public void visit(Upsert upsert) { warnNotImplemented("Upsert"); }

    @Override
    public void visit(AllColumns allColumns) {
        warnNotImplemented("AllColumns");
    }

    @Override
    public void visit(AllTableColumns allTableColumns) { warnNotImplemented("AllTableColumns"); }

    @Override
    public void visit(NullValue nullValue) { warnNotImplemented("NullValue"); }

    @Override
    public void visit(Function function) { warnNotImplemented("Function"); }

    @Override
    public void visit(SignedExpression signedExpression) { warnNotImplemented("SignedExpression"); }

    @Override
    public void visit(JdbcParameter jdbcParameter) { warnNotImplemented("JdbcParameter"); }

    @Override
    public void visit(JdbcNamedParameter jdbcNamedParameter) { warnNotImplemented("JdbcNamedParameter"); }

    @Override
    public void visit(DoubleValue doubleValue) { warnNotImplemented("DoubleValue"); }

    @Override
    public void visit(LongValue longValue) { warnNotImplemented("LongValue"); }

    @Override
    public void visit(HexValue hexValue) { warnNotImplemented("HexValue"); }

    @Override
    public void visit(DateValue dateValue) { warnNotImplemented("DateValue"); }

    @Override
    public void visit(TimeValue timeValue) { warnNotImplemented("TimeValue"); }

    @Override
    public void visit(TimestampValue timestampValue) { warnNotImplemented("TimestampValue"); }

    @Override
    public void visit(Parenthesis parenthesis) { warnNotImplemented("Parenthesis"); }

    @Override
    public void visit(StringValue stringValue) { /* Noop */ }

    @Override
    public void visit(Addition addition) { warnNotImplemented("Addition"); }

    @Override
    public void visit(Division division) { warnNotImplemented("Division"); }

    @Override
    public void visit(Multiplication multiplication) { warnNotImplemented("Multiplication"); }

    @Override
    public void visit(Subtraction subtraction) { warnNotImplemented("Subtraction"); }

    @Override
    public void visit(AndExpression andExpression) {
        andExpression.getLeftExpression().accept(this);
        andExpression.getRightExpression().accept(this);
    }

    @Override
    public void visit(OrExpression orExpression) {
        orExpression.getLeftExpression().accept(this);
        orExpression.getRightExpression().accept(this);
    }

    @Override
    public void visit(Between between) {
        between.getLeftExpression().accept(this);
    }

    @Override
    public void visit(EqualsTo equalsTo) {
        equalsTo.getLeftExpression().accept(this);
        equalsTo.getRightExpression().accept(this);
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        greaterThan.getLeftExpression().accept(this);
        greaterThan.getRightExpression().accept(this);
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        greaterThanEquals.getLeftExpression().accept(this);
        greaterThanEquals.getRightExpression().accept(this);
    }

    @Override
    public void visit(InExpression inExpression) {
        inExpression.getLeftExpression().accept(this);
    }

    @Override
    public void visit(IsNullExpression isNullExpression) {
        isNullExpression.getLeftExpression().accept(this);
    }

    @Override
    public void visit(LikeExpression likeExpression) {
        likeExpression.getLeftExpression().accept(this);
        likeExpression.getRightExpression().accept(this);
    }

    @Override
    public void visit(MinorThan minorThan) {
        minorThan.getLeftExpression().accept(this);
        minorThan.getRightExpression().accept(this);
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        minorThanEquals.getLeftExpression().accept(this);
        minorThanEquals.getRightExpression().accept(this);
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        notEqualsTo.getLeftExpression().accept(this);
        notEqualsTo.getRightExpression().accept(this);
    }

    @Override
    public void visit(Column tableColumn) {
        String name = tableColumn.getColumnName();
        if (canReplaceColumn(name)) {
            // We need to replace the column
            tableColumn.setColumnName(columnReplacement(name));
        }
    }

    @Override
    public void visit(SubSelect subSelect) { warnNotImplemented("SubSelect"); }

    @Override
    public void visit(CaseExpression caseExpression) { warnNotImplemented("CaseExpression"); }

    @Override
    public void visit(WhenClause whenClause) { warnNotImplemented("WhenClause"); }

    @Override
    public void visit(ExistsExpression existsExpression) { warnNotImplemented("ExistsExpression"); }

    @Override
    public void visit(AllComparisonExpression allComparisonExpression) { warnNotImplemented("AllComparisonExpression"); }

    @Override
    public void visit(AnyComparisonExpression anyComparisonExpression) { warnNotImplemented("AnyComparisonException"); }

    @Override
    public void visit(Concat concat) { warnNotImplemented("Concat"); }

    @Override
    public void visit(Matches matches) { warnNotImplemented("Matches"); }

    @Override
    public void visit(BitwiseAnd bitwiseAnd) { warnNotImplemented("BitwiseAnd"); }

    @Override
    public void visit(BitwiseOr bitwiseOr) { warnNotImplemented("BitwiseOr"); }

    @Override
    public void visit(BitwiseXor bitwiseXor) { warnNotImplemented("BitwiseXor"); }

    @Override
    public void visit(CastExpression cast) { warnNotImplemented("CastExpression"); }

    @Override
    public void visit(Modulo modulo) { warnNotImplemented("Modulo"); }

    @Override
    public void visit(AnalyticExpression aexpr) { warnNotImplemented("AnalyticExpression"); }

    @Override
    public void visit(WithinGroupExpression wgexpr) { warnNotImplemented("WithinGroupExpression"); }

    @Override
    public void visit(ExtractExpression eexpr) { warnNotImplemented("ExtractExpression"); }

    @Override
    public void visit(IntervalExpression iexpr) { warnNotImplemented("IntervalExpression"); }

    @Override
    public void visit(OracleHierarchicalExpression oexpr) { warnNotImplemented("OracleHierarchicalExpression"); }

    @Override
    public void visit(RegExpMatchOperator rexpr) { warnNotImplemented("RegExpMatchOperator"); }

    @Override
    public void visit(JsonExpression jsonExpr) { warnNotImplemented("JsonExpression"); }

    @Override
    public void visit(JsonOperator jsonExpr) { warnNotImplemented("JsonOperator"); }

    @Override
    public void visit(RegExpMySQLOperator regExpMySQLOperator) { warnNotImplemented("RegExpMySQLOperator");
    }

    @Override
    public void visit(UserVariable var) { warnNotImplemented("UserVariable"); }

    @Override
    public void visit(NumericBind bind) { warnNotImplemented("NumericBind"); }

    @Override
    public void visit(KeepExpression aexpr) { warnNotImplemented("KeepExpression"); }

    @Override
    public void visit(MySQLGroupConcat groupConcat) { warnNotImplemented("MySQLGroupConcat"); }

    @Override
    public void visit(RowConstructor rowConstructor) { warnNotImplemented("RowConstructor"); }

    @Override
    public void visit(OracleHint hint) { warnNotImplemented("OracleHint"); }

    @Override
    public void visit(TimeKeyExpression timeKeyExpression) { warnNotImplemented("TimeKeyExpression"); }

    @Override
    public void visit(DateTimeLiteralExpression literal) { warnNotImplemented("DateTimeLiteralExpression"); }

    @Override
    public void visit(NotExpression aThis) { warnNotImplemented("NotExpression"); }
}
