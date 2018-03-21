package chain.sql;

import chain.core.ChainDataSource;
import chain.core.ChainResultSet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


// TODO: Better implementations for different SQL databases
// Should create sub classes for other SQL database formats and remove constructors (except the Connection one)
// These subclasses should prepend the database type information and register the correct driver.

/**
 * SQLAdapter implements the ChainDataSource for SQL databases
 *
 * It establishes a connection with the database and implements the necessary public functions to run chain on queries
 * and repair them
 *
 */
public class SQLAdapter implements ChainDataSource  {

    private Connection connection;

    public SQLAdapter(String databaseUrl, String databaseUsername, String databasePassword) throws SQLException {
        registerDriver(databaseUrl);
        connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
    }

    public SQLAdapter(String databaseUrl) throws SQLException {
        registerDriver(databaseUrl);
        connection = DriverManager.getConnection(databaseUrl);
    }

    public SQLAdapter(Connection connection)  {
        this.connection = connection;
    }

    /**
     * Registers the correct database driver by dynamically loading it so the connection can be made.  See https://www.tutorialspoint.com/jdbc/jdbc-db-connections.htm
     * @param hostname The hostname of the connection
     * @throws SQLException Thrown if no driver class could be loaded
     */
    private void registerDriver(String hostname) throws SQLException {
        String driverClassName = getDriverNameFromHostname(hostname);
        try {
            // TODO: will need dependencies for all drivers (other than mySQL)
            Class.forName(driverClassName);
        }
        catch(ClassNotFoundException ex) {
            throw new SQLException("Could not load driver class: " + driverClassName + "\nThis may be as there is no dependency installed for this type of driver");
        }
    }

    /**
     * Gets the class name of the required driver based off the hostname.  See https://www.tutorialspoint.com/jdbc/jdbc-db-connections.htm
     * @param hostname The hostname of the connection
     * @return The name of the driver class
     * @throws SQLException Thrown if no driver class could be found for that name
     */
    private String getDriverNameFromHostname(String hostname) throws SQLException {
        String driverClassName;
        if(hostname.startsWith("jdbc:mysql:"))
            driverClassName = "com.mysql.jdbc.Driver";
        else if(hostname.startsWith("jdbc:oracle:"))
            driverClassName = "oracle.jdbc.driver.OracleDriver";
        else if(hostname.startsWith("jdbc:db2:"))
            driverClassName = "COM.ibm.db2.jdbc.net.DB2Driver";
        else if(hostname.startsWith("jdbc:sybase:"))
            driverClassName = "com.sybase.jdbc.SybDriver";
        else throw new SQLException("Could not find driver for url: " + hostname);
        return driverClassName;
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
