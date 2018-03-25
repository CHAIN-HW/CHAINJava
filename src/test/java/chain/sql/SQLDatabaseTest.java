package chain.sql;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;

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
        database = new SQLDatabase(new SQLAdapter("jdbc:mysql://localhost:3306/weakdb", "lm357", "abclm357354"));
    }

    @Test
    public void getTable() {
        assertThat( database.getTable("users"), instanceOf(SQLTable.class));
        assertNull("Returned type was not null",database.getTable("false_table"));
    }

    @Test
    public void containsTable() {
        assertTrue("Could not find table called users", database.containsTable( "users"));
        assertFalse("Found table passwords which should not exist",database.containsTable("passwords"));
    }
}