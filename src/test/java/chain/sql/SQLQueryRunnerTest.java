package chain.sql;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * SQLQueryRunnerTest
 * 
 * This class creates and connects to a mock database using the
 * SQLMockDatabaseClass before using it to test the SQLQueryRunner class.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SQLQueryRunnerTest {

    private Connection conn;

    @Before
    public void setup() throws SQLException {
        SQLMockDatabase mockDb = new SQLMockDatabase();
        conn = mockDb.getConnection();

        ResultSet rsUsers = mock(ResultSet.class);
        ResultSet rsUsers2 = mock(ResultSet.class);

        mockDb.returnWhenQuerying("SELECT * FROM users", rsUsers, rsUsers2);

        when(rsUsers.next()).thenReturn(true);
        when(rsUsers.getInt("id")).thenReturn(1);
        when(rsUsers.getString("firstname")).thenReturn("john");
        when(rsUsers.getString("lastname")).thenReturn("smith");

        when(rsUsers2.next()).thenReturn(true);
        when(rsUsers2.getInt("id")).thenReturn(2);
        when(rsUsers2.getString("firstname")).thenReturn("steve");
        when(rsUsers2.getString("lastname")).thenReturn("johnson");
    }

    @After
    public void close() throws SQLException {
        conn.close();
    }

    /**
     * Test 14.1
	 * This test checks the getResults function by comparing the results
	 * returned to the strings which should have been returned by running
	 * the query.
     * @throws SQLException
     */
    @Test
    public void getResults() throws SQLException {
        String query = "SELECT * FROM users";
        SQLQueryRunner queryRunner = new SQLQueryRunner(query, conn);
        ResultSet resultSet = queryRunner.getResults();
        testResults(resultSet, 1, "john", "smith");
        ResultSet resultSet2 = queryRunner.getResults();
        testResults(resultSet2, 1, "john", "smith");
    }

    /**
     * Test 14.2
	 * Tests the runQuery function by comparing the results returned to
	 * the strings which should have been returned by running the query.
     * @throws SQLException
     */
    @Test
    public void runQuery() throws SQLException {
        String query = "SELECT * FROM users";
        SQLQueryRunner queryRunner = new SQLQueryRunner(query, conn);
        ResultSet resultSet = queryRunner.runQuery();
        testResults(resultSet, 1, "john", "smith");
        ResultSet resultSet2 = queryRunner.runQuery();
        testResults(resultSet2, 2, "steve", "johnson");
    }

    /**
     * Function to make the assertions on the results passed in to it from
     * the above tests.
     * @param resultSet
     * @param expectedId
     * @param expectedFirst
     * @param expectedSecond
     * @throws SQLException
     */
    private void testResults(ResultSet resultSet, int expectedId, String expectedFirst, String expectedSecond) throws SQLException {
        resultSet.next();

        int id  = resultSet.getInt("id");
        String first = resultSet.getString("firstname");
        String last = resultSet.getString("lastname");

        assertEquals("Result set id does not match on query run", expectedId, id);
        assertEquals("Result set firstname does not match on query run", expectedFirst, first);
        assertEquals("Result set lastname does not match on query run", expectedSecond, last);

        resultSet.close();
    }

}