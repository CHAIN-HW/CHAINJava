import java.util.ArrayList;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;

public class Run_Query {

	//main method, used for testing the class
	public static void main (String[] args){
		Run_Query run = new Run_Query();
		Create_Query queryCreator = new Create_Query();
		Call_SPSM spsmCall = new Call_SPSM();
		Repair_Schema getRepairedSchema = new Repair_Schema();

		String source="waterBodyPressurestest(dataSource,identifiedDate,affectsGroundwater,waterBodyId)";
		String target="waterBodyPressurestest(dataSource,identifiedDate,affectsGroundwater,waterBodyId)";
		
		//String source="City(country,populationTotal)";
		//String target="City(country,populationTotal)";
		
		ArrayList<Match_Struc> finalRes = new ArrayList<Match_Struc>();
		
		spsmCall.getSchemas(finalRes, source, target);

		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = queryCreator.createQueryPrep(finalRes, "sepa", "queryData/sepa/sepa_datafiles/",0);
		//finalRes = queryCreator.createQueryPrep(finalRes, "dbpedia", null, 30);
		
		//select first element in list and run that query
		Match_Struc current = finalRes.get(0);
		run.runQuery(current, "sepa", "queryData/sepa/sepa_datafiles/");
		//run.runQuery(current, "dbpedia", null);
	}
	
	
	public boolean runQuery(Match_Struc current, String queryType, String datasetDir){
		
		if(queryType.equals("dbpedia")){
			return runDbpediaQuery(current.getQuery(),current);
		}else if(queryType.equals("sepa")){
			return runSepaQuery(current.getQuery(),datasetDir,current);
		}else{
			System.out.println("Please choose either 'dbpedia' or 'sepa'");
			return false;
		}
		
	}
	
	//runs a sepa query
	public boolean runSepaQuery(String query, String datasetToUseDir, Match_Struc currMatchStruc){
		System.out.println("Running sepa query based on,");
		System.out.println("Repaired Schema: "+currMatchStruc.getDatasetSchema());
		System.out.println("\n\nQuery:\t" + query);
		
		//create query object
		Query queryObj = QueryFactory.create(query);
		
		//load model locally
		String dbDir = datasetToUseDir + currMatchStruc.getRepairedSchemaTree().getValue() + ".n3";
		Model model = FileManager.get().loadModel(dbDir);
		
		//query execution factory
		QueryExecution queryExec = QueryExecutionFactory.create(queryObj, model);
		
		try{	
			System.out.println("\nResults:\n");
			//execute and print results to console
			ResultSet results = queryExec.execSelect();
			
			if (results == null || !results.hasNext()) {
			    return false;
			} else {
				ResultSetFormatter.out(System.out, results);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return true;
	}
	
	//runs a dbpedia query
	public boolean runDbpediaQuery(String query, Match_Struc currMatchStruc){
		System.out.println("\n\nRunning dbpedia query based on,");
		System.out.println("Repaired Schema: "+currMatchStruc.getDatasetSchema());
		System.out.println("\nQuery:\t"+query);
		
		//create query object
		Query queryObj = QueryFactory.create(query);
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", queryObj);
		
		try{
			System.out.println("\nResults:\n");
			
			//execute and print results to console
			ResultSet results = qexec.execSelect();
			
			if (results == null || !results.hasNext()) {
			    return false;
			} else {
				ResultSetFormatter.out(System.out, results);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return true;
	}
}
