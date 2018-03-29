package chain.sql.visitors;

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

public class SQLSelectColumnVisitor implements StatementVisitor, SelectVisitor, SelectItemVisitor {

    Map<String, String> replacements;

    public SQLSelectColumnVisitor(Map<String, String> replacements) {
        this.replacements = replacements;
    }

    @Override
    public void visit(PlainSelect plainSelect) {
        FromItem fi = plainSelect.getFromItem();

        for(SelectItem selectItem : plainSelect.getSelectItems()) {
            selectItem.accept(this);
        }

    }


    @Override
    public void visit(SelectExpressionItem selectExpressionItem) {
        System.out.println("Expression: " + selectExpressionItem.toString());

        if(selectExpressionItem.getExpression() instanceof Column) {
            Column oldColumn = (Column) selectExpressionItem.getExpression();
            if(replacements.keySet().contains(oldColumn.getName(false))) {
                // We need to replace the column

                Column newColumn = new Column(oldColumn.getTable(), replacements.get(oldColumn.getName(false)));

                selectExpressionItem.setExpression(newColumn);
            }
        }



//        selectExpressionItem.setExpression(new Expression;
//        selectExpressionItem.accept(this);
    }

    @Override
    public void visit(Select select) {
        select.getSelectBody().accept(this);
    }

    @Override
    public void visit(SetOperationList setOpList) {

    }

    @Override
    public void visit(WithItem withItem) {

    }

    @Override
    public void visit(Commit commit) {

    }

    @Override
    public void visit(Delete delete) {

    }

    @Override
    public void visit(Update update) {

    }

    @Override
    public void visit(Insert insert) {

    }

    @Override
    public void visit(Replace replace) {

    }

    @Override
    public void visit(Drop drop) {

    }

    @Override
    public void visit(Truncate truncate) {

    }

    @Override
    public void visit(CreateIndex createIndex) {

    }

    @Override
    public void visit(CreateTable createTable) {

    }

    @Override
    public void visit(CreateView createView) {

    }

    @Override
    public void visit(AlterView alterView) {

    }

    @Override
    public void visit(Alter alter) {

    }

    @Override
    public void visit(Statements stmts) {

    }

    @Override
    public void visit(Execute execute) {

    }

    @Override
    public void visit(SetStatement set) {

    }

    @Override
    public void visit(Merge merge) {

    }

    @Override
    public void visit(Upsert upsert) {

    }

    @Override
    public void visit(AllColumns allColumns) {
//        allColumns.accept(this);
        System.out.println("All collumns: " + allColumns.toString());
    }

    @Override
    public void visit(AllTableColumns allTableColumns) {
//        allTableColumns.accept(this);
        System.out.println("All collumns: " + allTableColumns.toString());
    }

}
