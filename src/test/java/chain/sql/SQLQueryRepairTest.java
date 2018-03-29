package chain.sql;

import chain.core.ChainDataSource;
import it.unitn.disi.smatch.SMatchException;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SQLQueryRepairTest {

    private SQLQueryRepair queryRepair;
    private SQLDatabase mockDb;

    @Before
    public void setUp() throws SQLException {
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
}