package chain.sql;

import chain.core.ChainDataSource;
import it.unitn.disi.smatch.SMatchException;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
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
    public void runRepairer() throws ChainDataSourceException, SQLException, SPSMMatchingException, SMatchException {
        SQLQueryRepair queryRepair = new SQLQueryRepair( "SELECT * from user", mockDb);
        String repairedQuery = queryRepair.runRepairer();
        assertEquals("SELECT * FROM users", repairedQuery);
    }


    @Test
    public void testSetColumns() throws SQLException, ChainDataSourceException {
        SQLQueryRepair queryRepair = new SQLQueryRepair( "SELECT surname from users", mockDb);
        Map<String, String> test = new HashMap<>();
        test.put("surname", "lastname");
        queryRepair.repairColumnNames(test);

        String query = queryRepair.getQueryFromTree();
        System.out.println(query);
        assertEquals("SELECT lastname FROM users", query);
    }
}