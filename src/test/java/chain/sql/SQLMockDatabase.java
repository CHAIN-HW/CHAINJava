package chain.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Provides mock connection for testing MySQL queries
 */
public class SQLMockDatabase {

    private Connection connection;
    private Statement statement;


    SQLMockDatabase() throws SQLException {
        this.connection = mock(Connection.class);
        statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);
    }

    /**
     * Mocks the results for a specified query
     * @param query Query being mocked
     * @param rs Mocked ResultSet being returned
     * @throws SQLException Throws exception if issue with SQL query
     */
    public void returnWhenQuerying(String query, ResultSet rs) throws SQLException {
        when(statement.executeQuery(query)).thenReturn(rs);
    }

    public void returnWhenQuerying(String query, ResultSet rs, ResultSet rs2) throws SQLException {
        when(statement.executeQuery(query)).thenReturn(rs).thenReturn(rs2);
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }



}
