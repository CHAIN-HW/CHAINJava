package chain.sql.visitors;

import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GetSelectColumnVisitor implements SelectItemVisitor {

    List<String> columnNames;

    public GetSelectColumnVisitor() {
        columnNames = new ArrayList<String>();
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    @Override
    public void visit(AllColumns allColumns) {

    }

    @Override
    public void visit(AllTableColumns allTableColumns) {
        allTableColumns.accept(this);
    }

    @Override
    public void visit(SelectExpressionItem selectExpressionItem) {
        if(selectExpressionItem.getExpression() instanceof Column) {
            columnNames.add(((Column)selectExpressionItem.getExpression()).getColumnName());
        }
    }
}
