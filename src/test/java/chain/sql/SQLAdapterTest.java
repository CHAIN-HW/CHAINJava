package chain.sql;

import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class SQLAdapterTest {

    @Ignore("Forming connection will not work on travis - not really testing anything else")
    @Test
    public void getConnection() throws SQLException {
        SQLAdapter adapter = new SQLAdapter("jdbc:mysql://mysql-server-1.macs.hw.ac.uk/dac31", "dac31", "sql");
        assertNotNull(adapter.getConnection());
    }

    @Test
    public void getSchemaOntology() {
    }

    @Test
    public void executeQuery() {
    }
}