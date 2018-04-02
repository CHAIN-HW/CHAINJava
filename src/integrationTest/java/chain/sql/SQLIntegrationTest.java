package chain.sql;

import org.junit.Before;
import org.junit.Test;
import java.sql.ResultSet;
import java.sql.SQLException;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class SQLIntegrationTest {

    SQLChainDataSource dataSource;

    @Before
    public void setup() throws SQLException {
        SQLMockDatabase mockDb = new SQLMockDatabase();

        ResultSet resultSetWHERE;
        ResultSet resultSetSELECT;
        ResultSet resultSetFROM;

        SQLException exception;

        exception = mock(SQLException.class);

        // setup mocked result sets

        resultSetWHERE = mock(ResultSet.class);
        resultSetSELECT = mock(ResultSet.class);
        resultSetFROM = mock(ResultSet.class);

        when(resultSetWHERE.next()).thenReturn(true);
        when(resultSetSELECT.next()).thenReturn(true);
        when(resultSetFROM.next()).thenReturn(true);

        when(resultSetWHERE.getString(1)).thenReturn("Lewis");
        when(resultSetSELECT.getString(1)).thenReturn("McNeill");
        when(resultSetFROM.getString(1)).thenReturn("John");

        mockDb.throwWhenQuerying("SELECT firstname FROM users WHERE surname = 'McNeill'", exception);
        mockDb.returnWhenQuerying("SELECT firstname FROM users WHERE lastname = 'McNeill'", resultSetWHERE);

        mockDb.throwWhenQuerying("SELECT surname FROM users", exception);
        mockDb.returnWhenQuerying("SELECT lastname FROM users", resultSetSELECT);

        mockDb.throwWhenQuerying("SELECT firstname FROM user", exception);
        mockDb.returnWhenQuerying("SELECT firstname FROM users", resultSetFROM);

        // -- setup results for db representation

        ResultSet rsTables = mock(ResultSet.class);
        ResultSet rsColumns1 = mock(ResultSet.class);
        ResultSet rsColumns2 = mock(ResultSet.class);

        when(rsTables.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rsTables.getString(1)).thenReturn("users").thenReturn("roles");

        when(rsColumns1.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rsColumns1.getString(1)).thenReturn("firstname").thenReturn("lastname");

        when(rsColumns2.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rsColumns2.getString(1)).thenReturn("access").thenReturn("level");

        mockDb.returnWhenQuerying("SHOW TABLES", rsTables);
        mockDb.returnWhenQuerying("DESCRIBE users", rsColumns1);
        mockDb.returnWhenQuerying("DESCRIBE roles", rsColumns2);

        dataSource = new SQLAdapter(mockDb.getConnection());
    }

    /**
     * Tests that the correct repaired query is returned from an incorrect column name in the WHERE clause
     * @throws ChainDataSourceException If an error occurs in the test
     */
    @Test
    public void testGetRepairedQueryWHEREColumn() throws ChainDataSourceException {
        String repairedQuery = dataSource.getRepairedQuery("SELECT firstname from users WHERE surname = 'McNeill'");
        assertEquals("SELECT firstname FROM users WHERE lastname = 'McNeill'", repairedQuery);
    }

    /**
     * Tests that the correct repaired query is returned from an incorrect column name in the SELECT clause
     * @throws ChainDataSourceException If an error occurs in the test
     */
    @Test
    public void testGetRepairedQuerySELECTColumn() throws ChainDataSourceException {
        String repairedQuery = dataSource.getRepairedQuery("SELECT surname from users");
        assertEquals("SELECT lastname FROM users", repairedQuery);
    }

    /**
     * Tests that the correct repaired query is returned from an incorrect column name in the FROM clause
     * @throws ChainDataSourceException If an error occurs in the test
     */
    @Test
    public void testGetRepairedQueryFROMtableName() throws ChainDataSourceException {
        String repairedQuery = dataSource.getRepairedQuery("SELECT firstname from user");
        assertEquals("SELECT firstname FROM users", repairedQuery);
    }

    /**
     * Tests that the correct repaired query is executed from an incorrect column name in the WHERE clause
     * @throws ChainDataSourceException If an error occurs in the test
     */
    @Test
    public void testExecuteQueryWHEREColumn() throws SQLException, ChainDataSourceException {
        ResultSet rs = dataSource.executeQuery("SELECT firstname FROM users WHERE surname = 'McNeill'");
        assertTrue("No items in result set", rs.next());
        assertEquals("Incorrect item from result set","Lewis", rs.getString(1));
    }

    /**
     * Tests that the correct repaired query is executed from an incorrect column name in the SELECT clause
     * @throws ChainDataSourceException If an error occurs in the test
     */
    @Test
    public void testExecuteQuerySELECTColumn() throws SQLException, ChainDataSourceException {
        ResultSet rs = dataSource.executeQuery("SELECT surname FROM users");
        assertTrue("No items in result set", rs.next());
        assertEquals("Incorrect item from result set","McNeill", rs.getString(1));
    }

    /**
     * Tests that the correct repaired query is executed from an incorrect column name in the FROM clause
     * @throws ChainDataSourceException If an error occurs in the test
     */
    @Test
    public void testExecuteQueryFROMtableName() throws SQLException, ChainDataSourceException {
        ResultSet rs = dataSource.executeQuery("SELECT firstname FROM user");
        assertTrue("No items in result set", rs.next());
        assertEquals("Incorrect item from result set","John", rs.getString(1));
    }
}
