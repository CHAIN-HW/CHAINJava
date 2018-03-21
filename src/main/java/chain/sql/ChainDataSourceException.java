package chain.sql;

/**
 * ChainDataSourceException
 *
 * Custom exception class for unifying errors that may occur
 * in the data connection and analysis lifecycle for CHAIn
 *
 * @author Daniel Waghorn
 * @extends java.lang.Exception
 */

public class ChainDataSourceException extends Exception {
    public ChainDataSourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
