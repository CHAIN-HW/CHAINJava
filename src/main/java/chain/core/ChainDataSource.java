package chain.core;

import chain.sql.ChainDataSourceException;

public interface ChainDataSource {

    public void getSchemaOntology();

    public ChainResultSet executeQuery(String query) throws ChainDataSourceException;

}
