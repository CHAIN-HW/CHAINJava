package chain.core;

import chain.sql.ChainDataSourceException;

public interface ChainDataSource {

    public void getSchemaOntology();

    String getRepairedQuery(String s) throws ChainDataSourceException;
}
