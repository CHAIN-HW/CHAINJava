package chain_source;
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
 * Responsible for taking in a Match_Struc 
 * object and running the query that is 
 * stored as part of that structure.
 * 
 * This class is tested in Run_Query_Test_Cases.java
 * 
 */
public class Run_Query {
	//main method, used for testing the class
	public static void main (String[] args){
		Run_Query run = new Run_Query();
		Create_Query queryCreator = new Create_Query();
		Call_SPSM spsmCall = new Call_SPSM();
		Repair_Schema getRepairedSchemas = new Repair_Schema();

		// Example of creating and running a Sepa query
//		String source="waterBodyPressures(dataSource,identifiedDate,affectsGroundwater,waterBodyId)";
//		String target="waterBodyPressures(dataSource,identifiedDate,affectsGroundwater,waterBodyId)";
//		String queryType = "sepa";
//		String dataLocation = "queryData/sepa/sepa_datafiles/" ;
//		String ontologyPath = "queryData/sepa/sepa_ontology.json" ;
//		int  maxValues = 0 ;
//		String query = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
//				+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
//				+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
//				+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
//				+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
//				+ "SELECT *  \n"
//				+ "FROM <queryData/sepa/sepa_datafiles/waterBodyPressures.n3>\n"
//				+ "WHERE {\n ?id sepaw:dataSource ?dataSource;\n"
//				+ "sepaw:identifiedDate  ?identifiedDate  ;\n"
//				+ "sepaw:affectsGroundwater ?affectsGroundwater ;\n"
//				+ "sepaw:waterBodyId ?waterBodyId .}" ;
		
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
		
		
		// sensor readings test
//		String source="sensorReadings(sensorName,themeName,typeName,suspect,value,units,timestamp)";
//		String target="sensorReadings(sensorName,themeName,typeName,suspect,value,units,timestamp)";
//		String queryType = "urbobvs";
//		String dataLocation = "queryData/urbanobservatory/uo_datafiles/" ;
//		String ontologyPath = "queryData/urbanobservatory/uo_ontology/sensorReadings_ontology.json" ;
//		int  maxValues = 10 ;
//		String query = "PREFIX urbobvs: <https://urbanobservatory.ac.uk/sensorReadings#> \n" 
//				+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
//				+ "SELECT * FROM <queryData/urbanobservatory/uo_datafiles/sensorReadings.nt> \n"
//				+ "WHERE {\n ?s urbobvs:sensorName ?sensorName ; \n"
//				+ "urbobvs:themeName ?themeName ; \n"
//				+ "urbobvs:typeName ?typeName ; \n"
//				+ "urbobvs:suspect ?suspect ; \n"
//				+ "urbobvs:value ?value ; \n"
//				+ "urbobvs:units ?units ; \n"
//				+ "urbobvs:timestamp ?timestamp . } \n"
//				+ "LIMIT 10" ;

//		// sensor themes test
//		String source = "sensorThemes(themeName,typeName,groundHeightAboveSeaLevel,sensorHeightAboveGround,thirdParty,sensorCentroidLongitude,sensorName,brokerName,sensorCentroidLatitude,locationWKT)";
//		String target = "sensorThemes(themeName,typeName,groundHeightAboveSeaLevel,sensorHeightAboveGround,thirdParty,sensorCentroidLongitude,sensorName,brokerName,sensorCentroidLatitude,locationWKT)";
//		String queryType = "urbobvs";
//		String dataLocation = "queryData/urbanobservatory/uo_datafiles/";
//		String ontologyPath = "queryData/urbanobservatory/uo_ontology/sensorThemes_ontology.json";
//		int maxValues = 10;
//		String query = "PREFIX urbobvs: <https://urbanobservatory.ac.uk/sensorThemes#> \n"
//				+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
//				+ "SELECT * FROM <queryData/urbanobservatory/uo_datafiles/sensorThemes.nt> \n"
//				+ "WHERE {\n ?s urbobvs:themeName ?themeName ; \n" 
//				+ "urbobvs:typeName ?typeName ; \n"
//				+ "urbobvs:groundHeightAboveSeaLevel ?groundHeightAboveSeaLevel ; \n" 
//				+ "urbobvs:sensorHeightAboveGround ?sensorHeightAboveGround ; \n" 
//				+ "urbobvs:thirdParty ?thirdParty ; \n"
//				+ "urbobvs:sensorCentroidLongitude ?sensorCentroidLongitude ; \n" 
//				+ "urbobvs:sensorName ?sensorName ; \n" 
//				+ "urbobvs:brokerName ?brokerName ; \n"
//				+ "urbobvs:sensorCentroidLatitude ?sensorCentroidLatitude ; \n"
//				+ "urbobvs:locationWKT ?locationWKT .} \n"
//				+ "LIMIT 10";

		// themes test
//		String source="theme(themeName)";
//		String target="theme(themeName)";
//		String queryType = "urbobvs";
//		String dataLocation = "queryData/urbanobservatory/uo_datafiles/" ;
//		String ontologyPath = "queryData/urbanobservatory/uo_ontology/theme_ontology.json" ;
//		int  maxValues = 0 ;
//		String query = "PREFIX urbobvs: <https://urbanobservatory.ac.uk/theme#> \n" 
//				+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
//				+ "SELECT * FROM <queryData/urbanobservatory/uo_datafiles/theme.nt> \n"
//				+ "WHERE {\n ?s urbobvs:themeName ?themeName .} \n";
//		
		// types test
		String source="type(lowerLimit,upperLimit,typeName,units,themeName)";
		String target="type(lowerLimit,upperLimit,typeName,units,themeName)";
		String queryType = "urbobvs";
		String dataLocation = "queryData/urbanobservatory/uo_datafiles/" ;
		String ontologyPath = "queryData/urbanobservatory/uo_ontology/type_ontology.json" ;
		int  maxValues = 10 ;
		String query = "PREFIX urbobvs: <https://urbanobservatory.ac.uk/type#> \n" 
				+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ "SELECT * FROM <queryData/urbanobservatory/uo_datafiles/type.nt> \n"
				+ "WHERE {\n ?s urbobvs:lowerLimit ?lowerLimit ; \n"
				+ "urbobvs:upperLimit ?upperLimit ; \n"
				+ "urbobvs:typeName ?typeName ; \n"
				+ "urbobvs:units ?units ; \n"
				+ "urbobvs:themeName ?themeName . } \n"
				+ "LIMIT 10" ;			
		
		// extract data from the original query, ready to build it into the new query
		Query_Data queryData = new Query_Data(query) ;
		System.out.println(queryData) ;
		
		ArrayList<Match_Struc> finalRes = new ArrayList<Match_Struc>();
	
		
		// Extract the schemas from the source query and call SPSM
		
		spsmCall.callSPSM(finalRes, source, target);
		
		for (Match_Struc f:finalRes) {
			System.out.println("Match_Struc:" + f);
		}
		

		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchemas.repairSchemas(finalRes);
		}
		
		for (Match_Struc f:finalRes) {
			System.out.println("Match_Struc:" + f);
		}
		
		finalRes = queryCreator.createQueries(finalRes, queryData, queryType, dataLocation, ontologyPath, maxValues);
		
				
		//select first element in list and run that query
		Match_Struc first = finalRes.get(0);
		run.runQuery(first, queryType, dataLocation);
		
		
	}
	
	
	public ResultSet runQuery(Match_Struc current, String queryType, String datasetDir){
			
		if (current.getRepairedSchema() != null && !current.getRepairedSchema().isEmpty()) {
			System.out.println("Repaired schema: "+current.getRepairedSchema());
			System.out.println("Similarity == "+current.getSimValue()+" & size of matched structure == "+current.getNumMatchComponents()+"\n"); 
		}
		
		if (queryType.equals("dbpedia")) {
			return runDbpediaQuery(current.getQuery(), current);
		} else if (queryType.equals("sepa") || queryType.equals("urbobvs")) {
			return runTheQuery(current.getQuery(), datasetDir, current, queryType);
		}
		else {
			System.out.println("Please choose either 'dbpedia' or 'sepa'");
			return null;
		}

		
	}
	
	public ResultSet runTheQuery(String query, String datasetToUseDir, Match_Struc currMatchStruc, String queryType) {
		System.out.println("Running the query,");
		System.out.println("\n\nQuery:\t" + query);

		try {

			// create query object
			// DB - move inside try/catch to catch invalid query
			Query queryObj = QueryFactory.create(query);

			// load model locally
			String dbDir = null;
			if(queryType.equals("sepa")) {
				dbDir = datasetToUseDir + currMatchStruc.getRepairedSchemaTree().getValue() + ".n3";
			}
			else if(queryType.equals("urbobvs")) {
				dbDir = datasetToUseDir + currMatchStruc.getRepairedSchemaTree().getValue() + ".ttl";
			}
			Model model = FileManager.get().loadModel(dbDir);

			// query execution factory
			QueryExecution qexec = QueryExecutionFactory.create(queryObj, model);

			System.out.println("\nResults:\n");
			// execute and print results to console
			// Use a factory so that its possible to keep and copy the results after
			// printing them
			ResultSet results = ResultSetFactory.copyResults(qexec.execSelect());

			if (results == null) {
				return null;
			} else {
				// Need to create a copy because ResultSetFormatter is destructive
				ResultSetFormatter.out(System.out, ResultSetFactory.copyResults(results));
				return results;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Run_Query.java: QUERY ERROR!");
			return null;
		}

	}

	
	//runs a sepa query
//	public ResultSet runSepaQuery(String query, String datasetToUseDir, Match_Struc currMatchStruc){
//		System.out.println("Running sepa query,");
//		System.out.println("\n\nQuery:\t" + query);	
//		
//		try{
//			
//			//create query object
//			// DB - move inside try/catch to catch invalid query
//			Query queryObj = QueryFactory.create(query);
//			
//			//load model locally
//			String dbDir = datasetToUseDir + currMatchStruc.getRepairedSchemaTree().getValue() + ".n3";
//			Model model = FileManager.get().loadModel(dbDir);
//		
//			//query execution factory
//			QueryExecution qexec = QueryExecutionFactory.create(queryObj, model);
//		
//			
//			System.out.println("\nResults:\n");
//			//execute and print results to console
//			// Use a factory so that its possible to keep and copy the results after printing them
//			ResultSet results = ResultSetFactory.copyResults(qexec.execSelect());
//			
//			if (results == null) {
//			    return null;
//			} else {
//				// Need to create a copy because ResultSetFormatter is destructive
//				ResultSetFormatter.out(System.out, ResultSetFactory.copyResults(results));
//				return results;
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//			System.out.println("Run_Query.java: QUERY ERROR!");
//			return null;
//		}
//		
//	}
	
	//runs a dbpedia query
	public ResultSet runDbpediaQuery(String query, Match_Struc currMatchStruc){
		System.out.println("\n\nRunning dbpedia query,");
		System.out.println("\nQuery:\t"+query);

		//create query object
		try{

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
				System.out.println("Run_Query.java: QUERY ERROR!");
				return null;
			}
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("Run_Query.java: Jena QueryFactory Cannot interpret the input as a valid SPARQL query.");
			return null;
		}
		
		
	}
}
