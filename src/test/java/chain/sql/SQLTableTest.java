package chain.sql;

import org.junit.Before;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * @author Lewis McNeill
 *
 * Unit tests for the SQLTable class
 */
public class SQLTableTest {

    private SQLTable table;
    private Set<String> columns;

    public SQLTableTest() {
        columns = new HashSet<>();
        columns.add("username");
        columns.add("password");
        columns.add("address");
    }

    @Before
    public void setup()
    {
     table = new SQLTable("users", columns);
    }

    /**
     * Test 15.1
     * Tests the getTableName function of SQLTable by asserting that
     * the return value is equal to "users".
     */
    @Test
    public void getTableName() {
        String tableName = table.getTableName();
        assertTrue("Table name should be 'users' not " + tableName, tableName.equals("users"));
    }

    /**
     * Test 15.2
     * Tests the getColumnName function of SQLTable by checking that
     * it returns a set. This set will contain the table names as strings.
     */
    @Test
    public void getColumnNames() {
        assertThat(table.getColumnNames(), instanceOf(Set.class));
    }

    /**
     * Test 15.3
     * Check that the containsColumn function works for a column name
     * that does not exist in the table and one that does exist.
     */
    @Test
    public void containsColumn() {
        assertTrue("Column name username should exist", table.containsColumn("username"));
        assertFalse("Column name email should not exist", table.containsColumn("email"));
    }
}