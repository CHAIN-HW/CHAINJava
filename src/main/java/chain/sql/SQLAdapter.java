package chain.sql;

import chain.core.ChainDataSource;
import chain.core.ChainResultSet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLAdapter implements ChainDataSource  {

    private Connection connection;

    public SQLAdapter(String databaseUrl, String databaseUsername, String databasePassword) throws SQLException {
        connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
    }

    public SQLAdapter(String databaseUrl) throws SQLException {
        connection = DriverManager.getConnection(databaseUrl);
    }

    public SQLAdapter(Connection connection)  {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void getSchemaOntology() {

    }

    @Override
    public ChainResultSet executeQuery(String query) {
        return null;
    }
}
