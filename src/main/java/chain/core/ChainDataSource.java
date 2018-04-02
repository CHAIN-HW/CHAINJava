package chain.core;

import chain.sql.ChainDataSourceException;

public interface ChainDataSource {

    String getRepairedQuery(String s) throws ChainDataSourceException;
}
