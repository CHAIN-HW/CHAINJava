package chain.sql;

public class NoReplacementFoundException extends Exception {

    public NoReplacementFoundException(String columnName) {
        super("No replacement column name found for: " + columnName);
    }
}
