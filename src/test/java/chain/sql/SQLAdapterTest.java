package chain.sql;

import org.junit.Ignore;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class SQLAdapterTest {


    private SQLAdapter adapter;

    @Before
    public void setup() throws ChainDataSourceException {
        adapter = new SQLAdapter("jdbc:mysql://mysql-server-1.macs.hw.ac.uk/dac31", "dac31", "sql");
    }

    @Ignore("Forming connection will not work on travis - not really testing anything else")
    @Test
    public void getConnection() {
        assertNotNull(adapter.getConnection());
    }

    @Test
    public void getSchemaOntology() {
    }

    @Test
    public void executeQuery() {

    }
}