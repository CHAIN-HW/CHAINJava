package chain.sql;

import it.unitn.disi.smatch.SMatchException;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import static org.junit.Assert.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * SQLNameMatcherManagerTest
 * 
 * Tests the SQLNameMatcherManager class. It will create a mock database
 * and mock SPSMMatcher. It will then create an instance of SQLNameMatcherManager
 * using the mock database and matcher before running the tests.
 *
 */
public class SQLNameMatcherManagerTest {

    private SQLNameMatcherManager matcher;
    private SQLDatabase database;
    private WordNetMatcher wordNetMatcher;


    @Before
    public void setup() {
        database = mock(SQLDatabase.class);
        wordNetMatcher = mock(WordNetMatcher.class);
        List<String> tableNames = new ArrayList<>();
        tableNames.add("user");

        List<String> columnNames = new ArrayList<>();
        tableNames.add("surname");




    }

    
    /**
     * Test 11.1
	 * Tests the getReplacementTableNames function of the SQLNameMatcherManager class.
	 * It will check that 1 match is returned and that “users” is matched to “user”.
     * @throws SMatchException
     */
    @Test
    public void testTableNameReplacement() throws WordNetMatchingException, SMatchException {
        List<String> brokenTableNames = new ArrayList<>();
        brokenTableNames.add("user");

        matcher = new SQLNameMatcherManager(brokenTableNames, null, database, wordNetMatcher);

        Set<String> realTableNames = new HashSet<String>();
        realTableNames.add("users");
        realTableNames.add("roles");

        when(database.getTableNames()).thenReturn(realTableNames);
        when(database.containsTable("user")).thenReturn(false);
        when(wordNetMatcher.match("user")).thenReturn("users");

        Map<String, String> matches = matcher.getReplacementTableNames();

        assertEquals(1, matches.size());
        assertEquals("users", matches.get("user"));
    }

    @Test
    public void getReplacementColumnNames() throws NoReplacementFoundException, SMatchException {
        List<String> brokenColumnNames = new ArrayList<>();
        brokenColumnNames.add("surname");

        matcher = new SQLNameMatcherManager(null, brokenColumnNames, database, wordNetMatcher);

        Set<String> columns = new HashSet<>();
        columns.add("firstname");
        columns.add("lastname");
        columns.add("address");

        SQLTable testTable = new SQLTable("users", columns);
        Collection<SQLTable> testCollection = new ArrayList<>();
        testCollection.add(testTable);
        when(database.getTables()).thenReturn(testCollection);

        Map<String, String> result = matcher.getReplacementColumnNames();

        assertTrue(result.get("surname").equals("lastname"));
    }

    @Test(expected = NoReplacementFoundException.class)
    public void getReplacementColumnNamesNoReplacementFoundException() throws NoReplacementFoundException, SMatchException {
        List<String> brokenColumnNames = new ArrayList<>();
        brokenColumnNames.add("kettle");

        matcher = new SQLNameMatcherManager(null, brokenColumnNames, database, wordNetMatcher);

        Set<String> columns = new HashSet<>();
        columns.add("firstname");
        columns.add("lastname");
        columns.add("address");

        SQLTable testTable = new SQLTable("users", columns);
        Collection<SQLTable> testCollection = new ArrayList<>();
        testCollection.add(testTable);
        when(database.getTables()).thenReturn(testCollection);

        matcher.getReplacementColumnNames();
    }
}
