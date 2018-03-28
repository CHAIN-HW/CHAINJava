package chain.sql;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class SQLQueryAnalyserTest {
    protected SQLQueryAnalyser analyser;

    @Before
    public void setUp() throws Exception {
        this.analyser = new SQLQueryAnalyser("SELECT *" +
                "FROM customers " +
                "WHERE name = 'Lewis'" +
                "AND gender = 'M'");
    }

    @Test
    public void getStatement() {
        assertNotNull("Analyser failed in construction", this.analyser);
    }

    @Test
    public void getTables() {
        List<String> expected = Arrays.asList("customers");
        assertThat(this.analyser.getTables(), is(expected));
    }

    @Test (expected = ChainDataSourceException.class)
    public void testExceptionThrownOnInvalidQuery() throws ChainDataSourceException {
        new SQLQueryAnalyser("Invalid SQL Query");
    }

    @Test
    public void setSelectTableName() {
        assertEquals(this.analyser.toSQL(), "SELECT * FROM customers WHERE name = 'Lewis' AND gender = 'M'");
        this.analyser.setSelectTableName("users");
        assertEquals(this.analyser.toSQL(), "SELECT * FROM users WHERE name = 'Lewis' AND gender = 'M'");
    }

    @Test
    public void getWhereColumns() {
        List<String> columns = this.analyser.getWhereColumns();
        List<String> expected = Arrays.asList("name", "gender");
        assertThat(columns, is(expected));
    }
}
