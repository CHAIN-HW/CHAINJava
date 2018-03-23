package chain.sql;

import java.sql.*;

public class SQLQueryRunner {


    private ResultSet resultSet;
    private Statement stmt;
    private String query;

    public SQLQueryRunner(String query, Connection connection) throws SQLException {
        stmt = connection.createStatement();
        this.query = query;
    }

    public ResultSet getResults() throws SQLException {
        if(resultSet==null)
            runQuery();
        return resultSet;
    }

    public ResultSet runQuery() throws SQLException {
        resultSet = stmt.executeQuery(query);
        return resultSet;
    }


}
