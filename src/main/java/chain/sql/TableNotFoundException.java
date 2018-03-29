package chain.sql;

/**
 * @author Lewis McNeill
 *
 * TableNotFoundException
 *
 * Custom exception class for errors that may occur due to
 * tables being requested that do not exist
 */
class TableNotFoundException extends Exception{

    TableNotFoundException(String tableName) {
        super("Table: " + tableName + " could not be found");
    }

}
