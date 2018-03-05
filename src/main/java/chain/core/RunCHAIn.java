package chain.core;
import java.io.PrintWriter;
import java.util.ArrayList;

import chain.sparql.*;
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
public class RunCHAIn {
	
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

	private SchemaFromQuery getSchema = new SchemaFromQuery();
	private CallSPSM spsm = new CallSPSM();
	private BestMatchResults filterRes = new BestMatchResults();
	private RepairSchema repairSchema = new RepairSchema();
	private CreateQuery createQuery = new CreateQuery();
	private RunQuery runQuery = new RunQuery();
	
	//main method for testing during implementation
	public static void main(String[] args){
		RunCHAIn runCHAIn = new RunCHAIn();
		
		//need to pass in the query
		//and also target schemas for dataset
		//we are trying to query
		String query = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
					+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
					+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
					+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
					+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
					+ "SELECT *  \n"
					+ "FROM <queryData/sepa/sepa_datafiles/waterBodyPressurestest.n3>\n"
					+ "WHERE { ?id sepaw:dataSource ?dataSource;\n"
					+ "sepaw:identifiedDate  ?identifiedDate  ;\n"
					+ "sepaw:affectsGroundwater ?affectsGroundwater ;\n"
					+ "sepaw:waterBodyId ?waterBodyId .}"
					+ "\n\n";
		String queryType="sepa";
		double simThresholdVal = 0.3;
		String dataDir = "queryData/sepa/sepa_datafiles/";
		String ontologyPath = "queryData/sepa/sepa_ontology.json";
		
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
		String targetSchemas="places(dataSource, identifiedDate, affectsGroundwater, waterBodyId) ; waterBodyPressures(dataSource, identifiedDate, affectsGroundwater, waterBodyId)" ;
		
		
		runCHAIn.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, simThresholdVal, 5, null);
	}
	
	public int runCHAIn(String query, String queryType, String targetSchemas, String dataDir, String ontologyPath, int queryLim, double simThresholdVal, int resLimit, PrintWriter fOut){
		
		int resultStatus = UNKNOWNSTATUS;
		
		//first step is trying to run the initial query
		MatchStruc current = new MatchStruc();
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
				//then we have an invalid query
				//terminate chain with appropriate message
				if(fOut!=null){
					fOut.write("Invalid SPARQL query, please enter a valid query, terminating...\n\n");
				}
				System.out.println("Invalid SPARQL query, Terminating.");
				
				return INVALIDQUERY;
			}
			
			// Parse the query and store useful information about names, prefixes, literals
			QueryData queryData = new QueryData(query) ;
			
			ArrayList<MatchStruc> repairedQueries = createRepairedQueries(current, queryData, targetSchemas, queryType, dataDir, ontologyPath, queryLim, simThresholdVal, resLimit);
			
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
				
				ResultSet repairedQueryResult;
				
				// Print all the match structures with their repaired queries
				// for (MatchStruc r:repairedQueries) {
				// 	System.out.println("MatchStruc:" + r);
				// }
				
				for(int i = 0 ; i < repairedQueries.size() ; i++){
					//try running new queries
					MatchStruc curr = repairedQueries.get(i);
					if(fOut!=null){
						fOut.write("Target Schema, " + curr.getDatasetSchema() + ", has created the following query:\n\n"+curr.getQuery()+"\n\n");
					}
						
					repairedQueryResult = runRepairedQueries(curr, queryType, dataDir);
					if(repairedQueryResult == null){
						System.out.println("This new query has NOT run successfully.");
						fOut.write("This new query has NOT run successfully.\n\n\n");
						return REPAIREDQUERYRUNERROR ;
					} else if (!repairedQueryResult.hasNext()){
						System.out.println("This new query has run with no results.");
						if(fOut!=null ){
							fOut.write("This new query has run with no results.\n\n\n");
						}
						
						repairedQueryResult = dataRepair(queryType, curr, queryData, dataDir, queryLim, ontologyPath, fOut) ;
						
						if (!repairedQueryResult.hasNext() && resultStatus != REPAIREDQUERYRESULTS){
							resultStatus = REPAIREDQUERYNORESULTS ;
							
						} else if (resultStatus != REPAIREDQUERYRESULTS) {
							resultStatus = DATAREPAIREDWITHRESULTS ;
						}
						// if (resultStatus != REPAIREDQUERYRESULTS) {
						// 	resultStatus = REPAIREDQUERYNORESULTS ;
						// }
					} else {			
						System.out.println("This new query has run successfully with results.");
						if(fOut!=null ){
							fOut.write("This new query has run successfully.");
						}
						resultStatus = REPAIREDQUERYRESULTS ;
					}
				}
			}
		}
		return resultStatus;
	}
	
	public ResultSet dataRepair(String queryType, MatchStruc curr, QueryData queryData, String dataDir, int queryLim, String ontologyPath, PrintWriter fOut) {
		
		System.out.println("Attempting data repair.");
		fOut.write("Attempting data repair.\n\n\n");
		
		ArrayList<OntologyStruc> ontologies = createQuery.make_ontologies(ontologyPath) ;
		// Make the new (open) query
		String newQuery = createQuery.createOpenQuery(queryType, curr, queryData, dataDir, 
				queryLim, ontologies) ;
		// System.out.println("RunCHAIn: "+ newQuery);
		curr.setQuery(newQuery);
		// Then run it
		return runQuery.runQuery(curr, queryType, dataDir);		
	}
		
	public ArrayList<MatchStruc> createRepairedQueries(MatchStruc current, QueryData queryData, String targetSchemas, String queryType, String dataset, String ontFile, int queryLim, double simThresholdVal, int resLimit){
		
		//Narrow down the target schemas by filtering them against the associated words in the source schema
		System.out.println("All Target Schemas: " + targetSchemas);
		
		targetSchemas = NarrowDown.narrowDown(current.getQuerySchemaHead(), targetSchemas) ;
		
		System.out.println("Narrowed Target Schemas: " + targetSchemas);
		
		//start off by calling SPSM with schema created from query
		//and target schemas passed in originally
		ArrayList<MatchStruc> matches = new ArrayList<MatchStruc>();
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
	
	public ResultSet runRepairedQueries(MatchStruc matchStructure, String queryType, String dataset){
		
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
