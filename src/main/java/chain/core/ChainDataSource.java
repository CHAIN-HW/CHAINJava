package chain.core;

public interface ChainDataSource {

    public void getSchemaOntology();

    public ChainResultSet executeQuery(String query);

}
