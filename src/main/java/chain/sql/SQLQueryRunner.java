package chain.sql;

import java.sql.*;

/**
 * 
 * @author 
 * 
 * Runs a given query on the database connected to through the given connection.
 *
 */
public class SQLQueryRunner {


    private ResultSet resultSet;
    private Statement stmt;
    private String query;

    /**
     * Constructor
     * @param query - the query to be run.
     * @param connection - the connection to the database on which to run the query.
     * @throws SQLException
     */
    public SQLQueryRunner(String query, Connection connection) throws SQLException {
        stmt = connection.createStatement();
        this.query = query;
    }

    /**
     * Get the result set returned by running the query.
     * @return results given by running query on the database.
     * @throws SQLException
     */
    public ResultSet getResults() throws SQLException {
        if(resultSet==null)
            runQuery();
        return resultSet;
    }

    /**
     * Runs the given query on the given database.
     * @return results given by running the query on the database.
     * @throws SQLException
     */
    public ResultSet runQuery() throws SQLException {
        resultSet = stmt.executeQuery(query);
        return resultSet;
    }


}
