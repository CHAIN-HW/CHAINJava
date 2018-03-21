package chain.sparql;
import java.util.ArrayList;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;

/* Author Tanya Howden
 * Author Diana Bental
 * Date September 2017
 * Modified November 2017
 */


/*
 * 
 * Responsible for taking in a MatchStruc
 * object and running the query that is 
 * stored as part of that structure.
 * 
 * This class is tested in Run_Query_Test_Cases.java
 * 
 */
public class RunQuery {
	//main method, used for testing the class
	public static void main (String[] args){
		RunQuery run = new RunQuery();
		CreateQuery queryCreator = new CreateQuery();
		CallSPSM spsmCall = new CallSPSM();
		RepairSchema getRepairedSchemas = new RepairSchema();

		// Example of creating and running a Sepa query
		String source="waterBodyPressures(dataSource,identifiedDate,affectsGroundwater,waterBodyId)";
		String target="waterBodyPressures(dataSource,identifiedDate,affectsGroundwater,waterBodyId)";
		String queryType = "sepa";
		String dataLocation = "queryData/sepa/sepa_datafiles/" ;
		String ontologyPath = "queryData/sepa/sepa_ontology.json" ;
		int  maxValues = 0 ;
		String query = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
				+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
				+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
				+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
				+ "SELECT *  \n"
				+ "FROM <queryData/sepa/sepa_datafiles/waterBodyPressures.n3>\n"
				+ "WHERE {\n ?id sepaw:dataSource ?dataSource;\n"
				+ "sepaw:identifiedDate  ?identifiedDate  ;\n"
				+ "sepaw:affectsGroundwater ?affectsGroundwater ;\n"
				+ "sepaw:waterBodyId ?waterBodyId .}" ;
		
		// Example of creating and running a dbpedia query
//		String source="City(country,populationTotal)";
//		String target="City(country,populationTotal)";
//		String queryType = "dbpedia"; 
//		String dataLocation = null ;
//		String ontologyPath = "queryData/dbpedia/dbpedia_ontology.json" ;
//		int  maxValues = 30 ;
//		String query =	"PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
//				+ "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
//				+ "PREFIX  res: <http://dbpedia.org/resource/> \n"
//				+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
//				+ "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
//				+ "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
//				+ "SELECT DISTINCT *  \n"
//				+ "WHERE { ?id rdf:type dbo:City ;\n"
//				+ "dbo:country ?country ;\n"
//				+ "dbo:populationTotal ?populationTotal .}\n"
//				+ "LIMIT 20" ;
		
		// extract data from the original query, erady to build it into the new query
		QueryData queryData = new QueryData(query) ;
		System.out.println(queryData) ;
		
		ArrayList<MatchStruc> finalRes = new ArrayList<MatchStruc>();
	
		
		// Extract the schemas from the source query and call SPSM
		
		spsmCall.callSPSM(finalRes, source, target);
		
		for (MatchStruc f:finalRes) {
			System.out.println("MatchStruc:" + f);
		}
		

		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchemas.repairSchemas(finalRes);
		}
		
		for (MatchStruc f:finalRes) {
			System.out.println("MatchStruc:" + f);
		}
		
		finalRes = queryCreator.createQueries(finalRes, queryData, queryType, dataLocation, ontologyPath, maxValues);
		
				
		//select first element in list and run that query
		MatchStruc first = finalRes.get(0);
		run.runQuery(first, queryType, dataLocation);
		
		
	}
	
	
	public ResultSet runQuery(MatchStruc current, String queryType, String datasetDir){
			
		if (current.getRepairedSchema() != null && !current.getRepairedSchema().isEmpty()) {
			System.out.println("Repaired schema: "+current.getRepairedSchema());
			System.out.println("Similarity == "+current.getSimValue()+" & size of matched structure == "+current.getNumMatchComponents()+"\n"); 
		}
		
		if(queryType.equals("dbpedia")){
			return runDbpediaQuery(current.getQuery(),current);
		}else if(queryType.equals("sepa")){
			return runSepaQuery(current.getQuery(),datasetDir,current);
		}else{
			System.out.println("Please choose either 'dbpedia' or 'sepa'");
			return null;
		}
		
	}
	
	//runs a sepa query
	public ResultSet runSepaQuery(String query, String datasetToUseDir, MatchStruc currMatchStruc){
		System.out.println("Running sepa query,");
		System.out.println("\n\nQuery:\t" + query);	
		
		try{
			
			//create query object
			// DB - move inside try/catch to catch invalid query
			Query queryObj = QueryFactory.create(query);
			
			//load model locally
			String dbDir = datasetToUseDir + currMatchStruc.getRepairedSchemaTree().getValue() + ".n3";
			Model model = FileManager.get().loadModel(dbDir);
		
			//query execution factory
			QueryExecution qexec = QueryExecutionFactory.create(queryObj, model);
		
			
			System.out.println("\nResults:\n");
			//execute and print results to console
			// Use a factory so that its possible to keep and copy the results after printing them
			ResultSet results = ResultSetFactory.copyResults(qexec.execSelect());
			
			if (results == null) {
			    return null;
			} else {
				// Need to create a copy because ResultSetFormatter is destructive
				ResultSetFormatter.out(System.out, ResultSetFactory.copyResults(results));
				return results;
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("RunQuery.java: QUERY ERROR!");
			return null;
		}
		
	}
	
	//runs a dbpedia query
	public ResultSet runDbpediaQuery(String query, MatchStruc currMatchStruc){
		System.out.println("\n\nRunning dbpedia query,");
		System.out.println("\nQuery:\t"+query);

		//create query object
		Query queryObj = QueryFactory.create(query);
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", queryObj);	
		
		try{			
			System.out.println("\nResults:\n");
			
			//execute and print results to console
			// Use a factory so that its possible to keep and copy the results after printing them
			ResultSet results = ResultSetFactory.copyResults(qexec.execSelect());
			
//			if (results == null || !results.hasNext()) {
			if (results == null) {
			    return null;
			} else {
				// Need to create a copy because ResultSetFormatter is destructive
				ResultSetFormatter.out(System.out, ResultSetFactory.copyResults(results));
				// ResultSetFormatter.out(System.out, results);
				return results;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("RunQuery.java: QUERY ERROR!");
			return null;
		}
		
		
	}
}
