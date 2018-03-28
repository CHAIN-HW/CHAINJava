package chain.sql;

public class TableNotFoundException extends Exception{

    public TableNotFoundException(String tableName) {
        super("Table: " + tableName + " could not be found");
    }

}
