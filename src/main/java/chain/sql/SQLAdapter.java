package chain.sql;

import it.unitn.disi.smatch.SMatchException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


// TODO: Better implementations for different SQL databases
// Should create sub classes for other SQL database formats and remove constructors (except the Connection one)
// These subclasses should prepend the database type information and register the correct driver.

/**
 * SQLAdapter implements the ChainDataSource for SQL databases
 *
 * It establishes a connection with the database and implements the necessary public functions to run chain on queries
 * and repair them
 */
public class SQLAdapter implements SQLChainDataSource  {

    private Connection connection;

    public SQLAdapter(String databaseUrl, String databaseUsername, String databasePassword) throws ChainDataSourceException {
        try {
            registerDriver(databaseUrl);
            connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
        } catch (SQLException e) {
            throw new ChainDataSourceException("Failed to establish database connection", e);
        }
    }

    public SQLAdapter(String databaseUrl) throws ChainDataSourceException {
        try {
            registerDriver(databaseUrl);
            connection = DriverManager.getConnection(databaseUrl);
        } catch (SQLException e) {
            throw new ChainDataSourceException("Failed to establish database connection", e);
        }
    }

    public SQLAdapter(Connection connection)  {
        this.connection = connection;
    }

    /**
     * Registers the correct database driver by dynamically loading it so the connection can be made.  See https://www.tutorialspoint.com/jdbc/jdbc-db-connections.htm
     * @param hostname The hostname of the connection
     * @throws SQLException Thrown if no driver class could be loaded
     */
    private void registerDriver(String hostname) throws ChainDataSourceException {
        String driverClassName;
        try {
            driverClassName = getDriverNameFromHostname(hostname);
        } catch(SQLException e) {
            throw new ChainDataSourceException("Could not get name of driver from hostname: " + hostname, e);
        }

        try {
            Class.forName(driverClassName);
        }
        catch(ClassNotFoundException e) {
            throw new ChainDataSourceException("Could not load driver class: " + driverClassName + "\nThis may be as there is no dependency installed for this type of driver", e);
        }
    }

    /**
     * Gets the class name of the required driver based off the hostname.  See https://www.tutorialspoint.com/jdbc/jdbc-db-connections.htm
     * @param hostname The hostname of the connection
     * @return The name of the driver class
     * @throws SQLException Thrown if no driver class could be found for that name
     */
    private String getDriverNameFromHostname(String hostname) throws SQLException {
        if (hostname.startsWith("jdbc:mysql:"))
            return "com.mysql.cj.jdbc.Driver";
        else if (hostname.startsWith("jdbc:oracle:"))
            return "oracle.jdbc.driver.OracleDriver";
        else if (hostname.startsWith("jdbc:db2:"))
            return "COM.ibm.db2.jdbc.net.DB2Driver";
        else if (hostname.startsWith("jdbc:sybase:"))
            return "com.sybase.jdbc.SybDriver";
        else throw new SQLException("Could not find driver for url: " + hostname);
    }

    /**
     * Gets the adaptors connection
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Closes the adaptor connection
     * @throws ChainDataSourceException if unable to exit cleanly.
     */
    public void closeConnection() throws ChainDataSourceException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new ChainDataSourceException("Failed to close database connection: " + e.getMessage(), e);
        }
    }

    @Override
    public void getSchemaOntology() {

    }

    @Override
    public ResultSet executeQuery(String query) throws ChainDataSourceException { throw new NotImplementedException(); }


    public String getRepairedQuery(String query) throws ChainDataSourceException {
        try {
            SQLDatabase db = new SQLDatabase(connection);
            SQLQueryRepair queryRepair = new SQLQueryRepair(query, db);
            return queryRepair.runRepairer();

        } catch (SQLException | SMatchException | WordNetMatchingException e) {



            throw new ChainDataSourceException("Could not get repaired query: " + query, e);
        } // catch wrong structure
    }
}
