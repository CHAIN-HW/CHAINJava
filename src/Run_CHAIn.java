import java.io.PrintWriter;
import java.util.ArrayList;

/*
 * 
 * Responsible for running CHAIn when passed
 * in a query and target schema(s) for the dataset
 * that is trying to be queried.
 * 
 */
public class Run_CHAIn {

	private Schema_From_Query getSchema = new Schema_From_Query();
	private Call_SPSM spsm = new Call_SPSM();
	private Best_Match_Results filterRes = new Best_Match_Results();
	private Repair_Schema repairSchema = new Repair_Schema();
	private Create_Query createQuery = new Create_Query();
	private Run_Query runQuery = new Run_Query();
	
	//main method for testing during implementation
	public static void main(String[] args){
		Run_CHAIn runCHAIn = new Run_CHAIn();
		
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
		
		//note if there are several schemas here they are seperated with ';'
		String targetSchemas="waterBodyPressures(dataSource, identifiedDate, affectsGroundwater, waterBodyId)";
		
		runCHAIn.startCHAIn(query, queryType, targetSchemas, 10, simThresholdVal, 5, null);
	}
	
	public void startCHAIn(String query, String queryType, String targetSchemas, int queryLim, double simThresholdVal, int resLimit, PrintWriter fOut){
		//first step is trying to run the query
		Match_Struc current = new Match_Struc();
		current = getSchema.getSchemaFromQuery(query, queryType);
		
		if(current==null){
			//then we have an invalid query
			//terminate chain with appropriate message
			fOut.write("Invalid query, terminating...\n\n");
			System.out.println("Terminating.");
			
			return;
		}
		
		current.setQuery(query);
		
		boolean queryRunStatus = runQuery.runQuery(current, queryType, "queryData/sepa/sepa_datafiles/");
		
		if(queryRunStatus == true){
			//query has run successfully!
			//no need to repair
			System.out.println("Query has run successfully!");
			
			if(fOut!=null){
				fOut.write("Query has run successfully first time.\n\n CHAIn has finished running.\n\n\n");
			}
			
		}else{
			//has not run successfully, need to call SPSM & start repair work
			System.out.println("Query has not run successfully");
			System.out.println("CHAIn will now try to repair this query.\n");
			
			ArrayList<Match_Struc> results = startRepair(current, targetSchemas, queryType, "queryData/sepa/sepa_datafiles/", queryLim, simThresholdVal, resLimit);
			
			if(results == null || results.size() == 0){
				//no results returned from spsm
				System.out.println("\nTerminating.");
				
				if(fOut!=null){
					fOut.write("Query has NOT run successfully the first time.\n\nCalling SPSM to try and create new query\n");
					fOut.write("There have been 0 matches returned from SPSM, terminating...\n\n");
				}
			}else{
				
				if(fOut!=null){
					fOut.write("Query has NOT run successfully the first time.\n\nCalling SPSM has returned " + results.size() + " match(es).\n");
					fOut.write("Now running the new queries that have been created...\n\n");
				}
				
				boolean successRun;
				for(int i = 0 ; i < results.size() ; i++){
					//try running new queries
					Match_Struc curr = results.get(i);
					successRun = runRepairedQueries(curr, queryType, "queryData/sepa/sepa_datafiles/");
					
					
					if(fOut!=null){
						fOut.write("Target Schema, " + curr.getDatasetSchema() + ", has created the following query:\n\n"+curr.getQuery()+"\n\n");
						if(successRun){
							fOut.write("This new query has run successfully!!\n\n\n");
						}else{
							fOut.write("This new query has NOT run successfully!!\n\n\n");
						}
					}
					
				}
				
			}
			
		}
	}
	
	public ArrayList<Match_Struc> startRepair(Match_Struc current, String targetSchemas, String queryType, String dataset, int queryLim, double simThresholdVal, int resLimit){
		
		//start off by calling SPSM with schema created from query
		//and target schemas passed in originally
		ArrayList<Match_Struc> results = new ArrayList<Match_Struc>();
		results = spsm.getSchemas(results, current.getRepairedSchema(), targetSchemas);
		
		if(results == null){
			System.out.println("Null Results due to SPSM Error.");
			return null;
		}else if(results.size()==0){
			//there are no results from spsm
			System.out.println("There are no matches returned from SPSM.");
		}else{
			//spsm has returned something
			//filter results
			results = filterRes.getThresholdAndFilter(results, simThresholdVal, resLimit);
			
			//return repaired schema
			results = repairSchema.prepare(results);
			
			//then create a query from the repaired schema
			results = createQuery.createQueryPrep(results, queryType, dataset, queryLim);
		}
		
		return results;
	}
	
	public boolean runRepairedQueries(Match_Struc res, String queryType, String dataset){
		
		try{
			//running new queries
			System.out.println("\nRunning New Query\n");
			runQuery.runQuery(res, queryType, dataset);	
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	
}
