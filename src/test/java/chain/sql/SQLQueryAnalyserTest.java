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
        this.analyser = new SQLQueryAnalyser("SELECT * FROM customers");
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
}
