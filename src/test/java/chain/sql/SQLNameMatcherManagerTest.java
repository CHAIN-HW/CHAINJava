package chain.sql;

import it.unitn.disi.smatch.SMatchException;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import static org.junit.Assert.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        matcher = new SQLNameMatcherManager(tableNames, database, wordNetMatcher);
    }


    @Test
    public void testTableNameReplacement() throws WordNetMatchingException, SMatchException {
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
}
