package chain.sql;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class SQLQueryRunnerTest {

    private Connection conn;

    @Before
    public void setup() throws SQLException {
        SQLAdapter sql = new SQLAdapter("jdbc:mysql://mysql-server-1.macs.hw.ac.uk/dac31", "dac31", "sql");
        conn = sql.getConnection();
    }

    @After
    public void close() throws SQLException {
        conn.close();
    }

    @Test
    public void getResults() throws SQLException {
        String query = "SELECT * FROM users";
        SQLQueryRunner queryRunner = new SQLQueryRunner(query, conn);
        ResultSet resultSet = queryRunner.getResults();
        testResults(resultSet);
    }

    @Test
    public void runQuery() throws SQLException {
        String query = "SELECT * FROM users";
        SQLQueryRunner queryRunner = new SQLQueryRunner(query, conn);
        ResultSet resultSet = queryRunner.runQuery();
        testResults(resultSet);
    }

    private void testResults(ResultSet resultSet) throws SQLException {
        resultSet.next();

        int id  = resultSet.getInt("id");
        String first = resultSet.getString("firstname");
        String last = resultSet.getString("lastname");

        assertEquals("Result set id does not match on query run", 1, id);
        assertEquals("Result set firstname does not match on query run", "lewis", first);
        assertEquals("Result set lastname does not match on query run", "mcneill", last);

        resultSet.close();
    }
}