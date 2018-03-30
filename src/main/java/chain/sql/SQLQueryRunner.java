package chain.sql;

import java.sql.*;

/**
 * SQLQueryRunner
 *
 * Runs a specified query on a database
 */
public class SQLQueryRunner {


    private ResultSet resultSet;
    private Statement stmt;
    private String query;

    /**
     * Constructor for the SQLQueryRunner class
     * @param query is the SQL which is to be run
     * @param connection a connection object used to connect to the database
     * @throws SQLException
     */
    public SQLQueryRunner(String query, Connection connection) throws SQLException {
        stmt = connection.createStatement();
        this.query = query;
    }

    /**
     * Returns the result of the query and runs the query if not already run
     * @return resultSet containing the results of the query
     * @throws SQLException Thrown if the SQL query is invalid
     */
    public ResultSet getResults() throws SQLException {
        if(resultSet==null)
            runQuery();
        return resultSet;
    }

    /**
     * Executes the SQL query and returns the result
     * @return resultSet containing the results of the query
     * @throws SQLException Thrown if the SQL query is invalid
     */
    public ResultSet runQuery() throws SQLException {
        resultSet = stmt.executeQuery(query);
        return resultSet;
    }


}
