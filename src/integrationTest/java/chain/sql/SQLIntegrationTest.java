package chain.sql;

import chain.core.ChainDataSource;
import org.junit.Test;
import static junit.framework.TestCase.assertEquals;


public class SQLIntegrationTest {
    @Test
    public void integrationTest() throws ChainDataSourceException {
            ChainDataSource dataSource = new SQLAdapter("jdbc:mysql://mysql-server-1.macs.hw.ac.uk/dac31", "dac31", "sql");

            String repairedQuery = dataSource.getRepairedQuery("SELECT * from user");

            assertEquals("SELECT * FROM users", repairedQuery);

            repairedQuery = dataSource.getRepairedQuery("SELECT surname from user");

            assertEquals("SELECT lastname FROM users", repairedQuery);
    }

}
