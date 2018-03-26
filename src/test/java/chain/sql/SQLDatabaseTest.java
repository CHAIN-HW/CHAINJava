package chain.sql;

import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;


/**
 * @author Lewis McNeill
 *
 * Unit tests for the SQLDatabase class
 */
public class SQLDatabaseTest {

    private SQLDatabase database;

    @Before
    public void setup() throws SQLException {
        SQLMockDatabase mockDb = new SQLMockDatabase();

        ResultSet rsTables = mock(ResultSet.class);
        ResultSet rsColumns1 = mock(ResultSet.class);
        ResultSet rsColumns2 = mock(ResultSet.class);

        when(rsTables.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rsTables.getString(1)).thenReturn("users").thenReturn("roles");

        when(rsColumns1.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rsColumns1.getString(1)).thenReturn("username").thenReturn("password");

        when(rsColumns2.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rsColumns2.getString(1)).thenReturn("access").thenReturn("level");

        mockDb.returnWhenQuerying("SHOW TABLES", rsTables);
        mockDb.returnWhenQuerying("DESCRIBE users", rsColumns1);
        mockDb.returnWhenQuerying("DESCRIBE roles", rsColumns2);

        database = new SQLDatabase(mockDb.getConnection());
    }



    @Test
    public void getTable() {
        assertThat( database.getTable("users"), instanceOf(SQLTable.class));
        assertNull("Returned type was not null",database.getTable("false_table"));
    }

    @Test
    public void containsTable() {
        assertTrue("Could not find table called users", database.containsTable( "users"));
        assertTrue("Could not find table called roles", database.containsTable( "roles"));
        assertFalse("Found table passwords which should not exist",database.containsTable("passwords"));
    }
}