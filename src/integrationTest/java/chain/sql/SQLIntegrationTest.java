package chain.sql;

import chain.core.ChainDataSource;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.sql.Connection;

public class SQLIntegrationTest {



    @Before


    @Test
    public void integrationTest() throws ChainDataSourceException {
        Connection connection = mock(Connection.class);

        ChainDataSource dataSource = new SQLAdapter(connection);
        dataSource.executeQuery("SELECT * from user");


    }

}
