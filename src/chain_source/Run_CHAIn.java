package chain_source;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.hp.hpl.jena.query.ResultSet;

/* Author Tanya Howden
 * Date September 2017
 * Modified Diana Bental
 * Date December 2017
 */

/*
 * 
 * Responsible for running CHAIn when passed
 * in a query and target schema(s) for the dataset
 * that is trying to be queried.
 * 
 * This class is tested in Run_CHAIn_Test_Cases.java
 * 
 */
public class Run_CHAIn {
	
	// Return Status
	private static final int UNKNOWNSTATUS = 0 ;
	private static final int INITIALQUERYSUCCESS = 5 ;
	private static final int INVALIDQUERY = 6 ;
	private static final int SPSMFAILURE = 7 ;
	private static final int NOMATCHESFROMSPSM = 8 ;
	private static final int REPAIREDQUERYRUNERROR = 9 ;
	private static final int REPAIREDQUERYRESULTS = 10 ;
	private static final int REPAIREDQUERYNORESULTS = 11 ;
	private static final int DATAREPAIREDWITHRESULTS = 12 ;

	private Schema_From_Query getSchema = new Schema_From_Query();
	private Call_SPSM spsm = new Call_SPSM();
	private Best_Match_Results filterRes = new Best_Match_Results();
	private Repair_Schema repairSchema = new Repair_Schema();
	private Create_Query createQuery = new Create_Query();
	private Run_Query runQuery = new Run_Query();
	
	//main method for testing during implementation
	public static void main(String[] args){
		Run_CHAIn run_CHAIn = new Run_CHAIn();
		
		//need to pass in the query
		//and also target schemas for dataset
		//we are trying to query
//		String query = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
//					+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
//					+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
//					+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
//					+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
//					+ "SELECT *  \n"
//					+ "FROM <queryData/sepa/sepa_datafiles/waterBodyPressurestest.n3>\n"
//					+ "WHERE { ?id sepaw:dataSource ?dataSource;\n"
//					+ "sepaw:identifiedDate  ?identifiedDate  ;\n"
//					+ "sepaw:affectsGroundwater ?affectsGroundwater ;\n"
//					+ "sepaw:waterBodyId ?waterBodyId .}"
//					+ "\n\n";
//		String queryType="sepa";
//		double simThresholdVal = 0.3;
//		String dataDir = "queryData/sepa/sepa_datafiles/";
//		String ontologyPath = "queryData/sepa/sepa_ontology.json";
//		String targetSchemas="places(dataSource, identifiedDate, affectsGroundwater, waterBodyId) ; waterBodyPressures(dataSource, identifiedDate, affectsGroundwater, waterBodyId)" ;
//		
		
		/*
		 * Urban Observatory data can be in .ttl (Turtle) or .nt (N-Triples) format 
		 * the current implementation however uses .ttl
		 * 
		 * and it works as expected (might have to change Run_Query and Create_Query
		 * in order to support the different files)
		 * 
		 * */
		
//		String query = "PREFIX urbobvs: <https://urbanobservatory.ac.uk/sensorReadings#> \n" 
//				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
//				+ "SELECT * FROM <queryData/urbanobservatory/uo_datafiles/sensorReadingsTest.ttl> \n"
//				+ "WHERE {\n ?s urbobvs:sensorName ?sensorName ; \n"
//				+ "urbobvs:themeName ?themeName ; \n"
//				+ "urbobvs:typeName ?typeName ; \n"
//				+ "urbobvs:suspect ?suspect ; \n"
//				+ "urbobvs:value ?value ; \n"
//				+ "urbobvs:units ?units ; \n"
//				+ "urbobvs:timestamp ?timestamp . } \n"
//				+ "LIMIT 10" ;
//		String queryType = "urbobvs";
//		double simThresholdVal = 0.3;
//		String dataDir = "queryData/urbanobservatory/uo_datafiles/";
//		String ontologyPath = "queryData/urbanobservatory/uo_ontology/sensorReadings_ontology.json";
//		String targetSchemas = "sensorReadings(sensorName,themeName,typeName,suspect,value,units,timestamp) ; readings(sensorName,themeName,typeName,suspect,value,units,timestamp) ";
	
		
//		String query = "PREFIX urbobvs: <https://urbanobservatory.ac.uk/sensorThemes#> \n"
//				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
//				+ "SELECT * FROM <queryData/urbanobservatory/uo_datafiles/sensorThemesTest.ttl> \n"
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
//		String queryType = "urbobvs";
//		double simThresholdVal = 0.3;
//		String dataDir = "queryData/urbanobservatory/uo_datafiles/";
//		String ontologyPath = "queryData/urbanobservatory/uo_ontology/sensorThemes_ontology.json";
//		String targetSchemas = "sensorThemes(themeName,typeName,groundHeightAboveSeaLevel,sensorHeightAboveGround,thirdParty,sensorCentroidLongitude,sensorName,brokerName,sensorCentroidLatitude,locationWKT)";
//		
		
		/* the following 2 queries don't seem to be working when in plural form e.g. themes, types
		 * but they do work in their plural form in Run_Query.java
		 * 
		 * the issue is with the narrowing down of schemas - 
		 * it doesn't like plural names for the schema head
		 * 
		 * had to change the files so that they were in singular form
		 * e.g. theme, type
		 * 
		 * */
		
		String query = "PREFIX urbobvs: <https://urbanobservatory.ac.uk/theme#> \n" 
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ "SELECT * FROM <queryData/urbanobservatory/uo_datafiles/themeTest.ttl> \n"
				+ "WHERE {\n ?s urbobvs:themeName ?themeName .} \n";
		String queryType = "urbobvs";
		double simThresholdVal = 0.3;
		String dataDir = "queryData/urbanobservatory/uo_datafiles/";
		String ontologyPath = "queryData/urbanobservatory/uo_ontology/theme_ontology.json";
		String targetSchemas = "theme(themeName)";
		
	
//		String query = "PREFIX urbobvs: <https://urbanobservatory.ac.uk/type#> \n" 
//				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
//				+ "SELECT * FROM <queryData/urbanobservatory/uo_datafiles/typeTest.ttl> \n"
//				+ "WHERE {\n ?s urbobvs:lowerLimit ?lowerLimit ; \n"
//				+ "urbobvs:upperLimit ?upperLimit ; \n"
//				+ "urbobvs:typeName ?typeName ; \n"
//				+ "urbobvs:units ?units ; \n"
//				+ "urbobvs:themeName ?themeName . } \n"
//				+ "LIMIT 10" ;		
//		String queryType = "urbobvs";
//		double simThresholdVal = 0.3;
//		String dataDir = "queryData/urbanobservatory/uo_datafiles/";
//		String ontologyPath = "queryData/urbanobservatory/uo_ontology/type_ontology.json";
//		String targetSchemas = "type(lowerLimit,upperLimit,typeName,units,themeName)";
		
		
		//note if there are several schemas here they are separated with ';'
		//-- Two target schemas
		// String targetSchemas="waterBodyPressures(dataSource, identifiedDate, affectsGroundwater, waterBodyId) ; waterBodyMeasures(dataSource, identifiedDate, affectsGroundwater, waterBodyId)";
		//-- No target schemas
		// String targetSchemas="";
		//-- One target schema
		// String targetSchemas="waterBodyPressures(dataSource, identifiedDate, affectsGroundwater, waterBodyId)" ;
		//-- No narrowed schemas
		// String targetSchemas="places(dataSource, identifiedDate, affectsGroundwater, waterBodyId)" ;
		//-- Two targets, one  narrowed target schema
		
		
		run_CHAIn.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, simThresholdVal, 5, null);
	}
	
	public int runCHAIn(String query, String queryType, String targetSchemas, String dataDir, String ontologyPath, int queryLim, double simThresholdVal, int resLimit, PrintWriter fOut){
		
		int result_status = UNKNOWNSTATUS; 
		
		//first step is trying to run the initial query
		Match_Struc current = new Match_Struc();
		current.setQuery(query);	
		ResultSet queryRunResults = runQuery.runQuery(current, queryType, dataDir);
		
		if((queryRunResults != null) && (queryRunResults.hasNext())){
			//query has run successfully!
			//no need to repair
			System.out.println("Query has run successfully first time. CHAIn has finished running.");
			
			if(fOut!=null ){
				fOut.write("Query has run successfully first time.\n\n CHAIn has finished running.\n\n\n");
				return INITIALQUERYSUCCESS ;
			}
			
		}else{
			//has not run successfully, need to call SPSM & start repair work
			System.out.println("Query has not run successfully.");
			System.out.println("CHAIn will now try to repair this query.\n");
			
			if(fOut!=null){
				fOut.write("Query has NOT run successfully the first time.\n\nCalling SPSM to try and create new query\n");
			}
			
			current = getSchema.getSchemaFromQuery(query, queryType);
			
			if(current==null){
				//then we have a synactically invalid query, or one that CHAIn can't parse into a schema
				//terminate chain with appropriate message
				if(fOut!=null){
					fOut.write("This SPARQL query cannot be parsed into a schema, terminating...\n\n");
				}
				System.out.println("This SPARQL query cannot be parsed into a schema, Terminating.");
				
				return INVALIDQUERY;
			}
			
			// Parse the query and store useful information about names, prefixes, literals
			Query_Data queryData = new Query_Data(query) ;
			
			ArrayList<Match_Struc> repairedQueries = createRepairedQueries(current, queryData, targetSchemas, queryType, dataDir, ontologyPath, queryLim, simThresholdVal, resLimit);
			if(repairedQueries == null) {
				System.out.println("\nSPSM Failure. Terminating.");
				if(fOut!=null){
					fOut.write("\nSPSM Failure. Terminating.\n\n");
				}
				return SPSMFAILURE ;
				
			} else if (repairedQueries.size() == 0){
				//no results returned from spsm
				System.out.println("\nNo results from SPSM. Terminating.");
				if(fOut!=null){
					fOut.write("There have been 0 matches returned from SPSM, terminating...\n\n");
				}
				return NOMATCHESFROMSPSM ;
			}else{
				System.out.println("Now running the new queries that have been created...");
				if(fOut!=null){
					fOut.write("Now running the new queries that have been created...\n\n");
				}
				
				ResultSet resultsFromARepairedQuery;
				
				// Print all the match structures with their repaired queries
//				 for (Match_Struc r:repairedQueries) {
//				 	System.out.println("Match_Struc:" + r);
//				 }
				
				for(int i = 0 ; i < repairedQueries.size() ; i++){
					//try running new queries
					Match_Struc curr = repairedQueries.get(i);
					if(fOut!=null){
						fOut.write("Target Schema, " + curr.getDatasetSchema() + ", has created the following query:\n\n"+curr.getQuery()+"\n\n");
					}
						
					resultsFromARepairedQuery = runRepairedQueries(curr, queryType, dataDir);
					if(resultsFromARepairedQuery == null){
						System.out.println("This new query has NOT run successfully.");
						fOut.write("This new query has NOT run successfully.\n\n\n");
						return REPAIREDQUERYRUNERROR ;
					} else if (!resultsFromARepairedQuery.hasNext()){
						System.out.println("This new query has run with no results.");
						if(fOut!=null ){
							fOut.write("This new query has run with no results.\n\n\n");
						}
						
						resultsFromARepairedQuery = dataRepair(queryType, curr, queryData, dataDir, queryLim, ontologyPath, fOut) ;
						
						if (!resultsFromARepairedQuery.hasNext() && result_status != REPAIREDQUERYRESULTS){
							result_status = REPAIREDQUERYNORESULTS ;
							
						} else if (result_status != REPAIREDQUERYRESULTS) {
							result_status = DATAREPAIREDWITHRESULTS ;
						}
						// if (result_status != REPAIREDQUERYRESULTS) {
						// 	result_status = REPAIREDQUERYNORESULTS ;
						// }
					} else {			
						System.out.println("This new query has run successfully with results.");
						if(fOut!=null ){
							fOut.write("This new query has run successfully.");
						}
						result_status = REPAIREDQUERYRESULTS ;
					}
				}
			}
		}
		return result_status; 
	}
	
	public ResultSet dataRepair(String queryType, Match_Struc curr, Query_Data queryData, String dataDir, int queryLim, String ontologyPath, PrintWriter fOut) {
		
		System.out.println("Attempting data repair.");
		fOut.write("Attempting data repair.\n\n\n");
		
		ArrayList<Ontology_Struc> ontologies = createQuery.make_ontologies(ontologyPath) ;
		// Make the new (open) query
		String newQuery = createQuery.createOpenQuery(queryType, curr, queryData, dataDir, 
				queryLim, ontologies) ;
		// System.out.println("Run_CHAIn: "+ newQuery);
		curr.setQuery(newQuery);
		// Then run it
		return runQuery.runQuery(curr, queryType, dataDir);		
	}
		
	public ArrayList<Match_Struc> createRepairedQueries(Match_Struc current, Query_Data queryData, String targetSchemas, String queryType, String dataset, String ontFile, int queryLim, double simThresholdVal, int resLimit){
		
		//Narrow down the target schemas by filtering them against the associated words in the source schema
		System.out.println("All Target Schemas: " + targetSchemas);
		
		targetSchemas = Narrow_Down.narrowDown(current.getQuerySchemaHead(), targetSchemas) ;
		System.out.println("Narrowed Target Schemas: " + targetSchemas);
		
		//start off by calling SPSM with schema created from query
		//and target schemas passed in originally
		ArrayList<Match_Struc> matches = new ArrayList<Match_Struc>();
		matches = spsm.callSPSM(matches, current.getQuerySchema(), targetSchemas);
		
		if(matches == null){
			System.out.println("Null Results due to SPSM Error.");
			return null;
		}else if(matches.size()==0){
			//there are no results from spsm
			System.out.println("There are no matches returned from SPSM.");
		}else{
			//spsm has returned something
			//filter results
			matches = filterRes.getThresholdAndFilter(matches, simThresholdVal, resLimit);
			
			//return repaired schema
			matches = repairSchema.repairSchemas(matches);
			
			//then create a query from the repaired schema
			matches = createQuery.createQueries(matches, queryData, queryType, dataset, ontFile, queryLim);
		}
		
		return matches;
	}
	
	public ResultSet runRepairedQueries(Match_Struc matchStructure, String queryType, String dataset){
		
		try{
			//running new queries
			System.out.println("\nRunning New Query\n");
			ResultSet result = runQuery.runQuery(matchStructure, queryType, dataset);	
			return result ;
		}catch(Exception e){
			System.out.println("Error running query");
			return null;
		}
		
	}
	
}
