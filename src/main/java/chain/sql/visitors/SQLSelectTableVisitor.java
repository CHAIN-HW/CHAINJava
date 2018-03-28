package chain.sql.visitors;

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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SQLSelectTableVisitor implements StatementVisitor, SelectVisitor, FromItemVisitor {
    String replacementTableName;

    public SQLSelectTableVisitor (String replacementTableName) {
        this.replacementTableName = replacementTableName;
    }

    protected void warnNotImplemented(String param) {
        System.err.println("SQLSelectTableVisitor: Method not implemented - visit(" + param + ")");
    }

    @Override
    public void visit(PlainSelect plainSelect) {
        FromItem fi = plainSelect.getFromItem();
        fi.accept(this);
    }

    @Override
    public void visit(Select select) {
        select.getSelectBody().accept(this);
    }

    @Override
    public void visit(Table tableName) {
        tableName.setName(this.replacementTableName);
    }

    @Override
    public void visit(SetOperationList setOpList) { warnNotImplemented("SetOperationList"); }

    @Override
    public void visit(WithItem withItem) { warnNotImplemented("WithItem"); }

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
    public void visit(SubSelect subSelect) { warnNotImplemented("SubSelect"); }

    @Override
    public void visit(SubJoin subjoin) { warnNotImplemented("SubJoin"); }

    @Override
    public void visit(LateralSubSelect lateralSubSelect) { warnNotImplemented("LateralSubSelect"); }

    @Override
    public void visit(ValuesList valuesList) { warnNotImplemented("ValuesList"); }

    @Override
    public void visit(TableFunction tableFunction) { warnNotImplemented("TableFunction"); }
}
