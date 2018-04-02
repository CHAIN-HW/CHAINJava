package chain.sql;

import it.unitn.disi.smatch.SMatchException;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SQLQueryRepairTest {

    private SQLDatabase mockDb;

    @Before
    public void setUp() {
        Set<String> mockTables = new HashSet<>();
        mockTables.add("users");
        mockTables.add("roles");

        this.mockDb = mock(SQLDatabase.class);
        when(mockDb.containsTable("user")).thenReturn(false);
        when(mockDb.getTableNames()).thenReturn(mockTables);
    }


    @Test
    public void runRepairer() throws ChainDataSourceException, SQLException, WordNetMatchingException, SMatchException {
        SQLQueryRepair queryRepair = new SQLQueryRepair( "SELECT * from user", mockDb);
        String repairedQuery = queryRepair.runRepairer();
        assertEquals("SELECT * FROM users", repairedQuery);
    }


    @Test
    public void testSetColumns() throws SQLException, ChainDataSourceException {
        SQLQueryRepair queryRepair = new SQLQueryRepair( "SELECT surname FROM users", mockDb);
        Map<String, String> test = new HashMap<>();
        test.put("surname", "lastname");
        queryRepair.repairColumnNames(test);

        String query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users", query);

        // Test EqualTo expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname = 'McNeill'", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname = 'McNeill'", query);

        // Test GreaterThan expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname > 'McNeill'", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname > 'McNeill'", query);

        // Test GreaterThanEqual expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname >= 'McNeill'", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname >= 'McNeill'", query);

        // Test LessThan expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname < 'McNeill'", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname < 'McNeill'", query);

        // Test LessThanEqual expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname <= 'McNeill'", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname <= 'McNeill'", query);

        // Test NotEqual expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname != 'McNeill'", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname != 'McNeill'", query);

        // Test NotEqual expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname IS NULL", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname IS NULL", query);

        // Test Like expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname LIKE '%McNeill%'", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname LIKE '%McNeill%'", query);

        // Test In expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname IN ('McNeill', 'Union')", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname IN ('McNeill', 'Union')", query);

        // Test OR expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname = 'McNeill' OR surname = 'Union'", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname = 'McNeill' OR lastname = 'Union'", query);

        // Test AND expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname = 'McNeill' AND surname = 'Union'", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname = 'McNeill' AND lastname = 'Union'", query);

        // Test BETWEEN expressions
        queryRepair = new SQLQueryRepair("SELECT surname FROM users WHERE surname BETWEEN 0 AND 10", mockDb);
        queryRepair.repairColumnNames(test);
        query = queryRepair.getQueryFromTree();
        assertEquals("SELECT lastname FROM users WHERE lastname BETWEEN 0 AND 10", query);
    }
}