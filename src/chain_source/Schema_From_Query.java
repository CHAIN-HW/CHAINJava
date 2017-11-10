package chain_source;
 import java.util.*;

 /* Author Tanya Howden
  * Date September 2017
  * Modified
  */

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.sparql.core.Var;

/*
 * 
 * Responsible for taking in a query (either sepa or dbpedia)
 * and creating a CHAIn schema based on this query.
 * 
 * This class is tested in Schema_From_Query_Test_Cases.java
 * 
 */
public class Schema_From_Query {
	
	//main method used for testing implementation
	public static void main(String [] args){
		Schema_From_Query getSchema = new Schema_From_Query();
		
		String query="PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:Person ;\n"
		          + "dbo:occupation ?occupation ;\n"
		          + "dbo:birthPlace ?birthPlace .}\n"
		          + "LIMIT 20\n\n";
		
		String queryType="dbpedia";
		
		Match_Struc res = getSchema.getSchemaFromQuery(query, queryType);
		System.out.println("Schema created from query: \n\n"+res.getRepairedSchema());
		System.out.println("Query Schema "+ res.getQuerySchema());
		System.out.println("Query Schema Head "+ res.getQuerySchemaHead());
		System.out.println("Dataset Schema "+ res.getDatasetSchema());
	}
	
	//first step to creating schema from query
	//works out next step based on type of query that is getting passed in
	public Match_Struc getSchemaFromQuery(String query, String queryType){ 
		//result that will be returned
		//that will contain the query string
		//schema string and schema tree
		Match_Struc result = new Match_Struc();
		
		
		if(queryType.equals("sepa")){
			result = schemaFromSepaQuery(query, result);
		}else if(queryType.equals("dbpedia")){
			result = schemaFromDbpediaQuery(query, result);
		}else{
			System.out.println("Please choose either a sepa or dbpedia query to parse.");
			return null;
		}
		
		return result;
	}
	
	//create the schema for a sepa query that has been passed in as a parameter
	public Match_Struc schemaFromSepaQuery(String query, Match_Struc res){
		String schema="";
		String predicate = "" ;
		
		if(!query.equals("")){
			
			try{
				//create query object
				Query sepaQuery = QueryFactory.create(query);
				
				//first get the name of the dataset file
				//this will be the predicate of our schema
				String datasetFile = sepaQuery.getGraphURIs().get(0);
				
				//split at / character and get last element
				//which will be name of file
				String[] datasetFileArr = datasetFile.split("/");
				
				datasetFile = datasetFileArr[datasetFileArr.length-1];
				
				//then remove the .n3 from the filename and we have our predicate
				datasetFile = datasetFile.substring(0, datasetFile.length() - 3);
				
				predicate = datasetFile ;
				System.out.println("Predicate " + predicate) ;
				
				//add to schema string
				schema = datasetFile + "(";
				
				//then get the variables that will make up
				//parameters in our schema
				List<Var> projectVars = sepaQuery.getProjectVars();
				
				//loop for each project variable apart from first which is id
				//and add as parameter to schema
				for(int i = 1 ; i < projectVars.size() ; i++){
					//add param to schema string
					String param = projectVars.get(i).getName();
					
					schema = schema + param;
					
					//as long as it isn't the last param, add comma
					if(i != projectVars.size()-1){
						schema=schema+",";
					}
				}
				
				//add final bracket
				schema=schema+")";
				
			}catch(Exception e){
				//invalid query, return null
				System.out.println("Invalid Query, returning null.\n");
				return null;
			}
		}
		
		
		//set string and tree equivalent of schema in our structure
		//and return
		System.out.println("Predicate " + predicate) ;
		res.setRepairedSchema(schema);
		res.setRepairedSchemaTree(createTreeFromSchemaString(schema));
		res.setQuerySchema(schema);
		res.setQuerySchemaHead(predicate);
		System.out.println("Schema head in setter " + res.getQuerySchemaHead()) ;
		
		return res;
	}
	
	//create the schema for a dbpedia query that has been passed in as a parameter
	public Match_Struc schemaFromDbpediaQuery(String query, Match_Struc res){
		String schema="";
		String predicate = "";
		
		if(!query.equals("")){

			try{
				
				//create query object
				Query dbpediaQuery = QueryFactory.create(query);
				
				//get the predicate of the schema
				//get query pattern to get predicate value
				String queryPattern = dbpediaQuery.getQueryPattern().toString();
				
				//then get the variables that will make up
				//parameters in our schema
				List<Var> projectVars = dbpediaQuery.getProjectVars();
				
				//then get the first value from this list and set as predicate
				String[] patternArr = queryPattern.split("\n")[0].split("/");
				predicate = patternArr[patternArr.length-1];
				System.out.println("Predicate " + predicate) ;
				
				if(projectVars.size() > 1){
					predicate = predicate.trim().substring(0,predicate.length()-3);
				}else{
					predicate = predicate.trim().substring(0,predicate.length()-2);
				}
				
				
				//start schema string
				schema = predicate + "(";
				
						
				//loop for each project variable apart from first which is id
				//and add as parameter to schema
				for(int i = 1 ; i < projectVars.size() ; i++){
					//add param to schema string
					String param = projectVars.get(i).getName();
							
					schema = schema + param;
							
					//as long as it isn't the last param, add comma
					if(i != projectVars.size()-1){
						schema=schema+",";
					}
				}
						
				//add final bracket
				schema=schema+")";
				
			}catch(Exception e){
				//invalid query, returning null
				System.out.println("Invalid Query, returning null.\n");
				return null;
			}
		}
		
		//set string and tree equivalent of schema in our structure
		//and return
		
		res.setRepairedSchema(schema);
		res.setRepairedSchemaTree(createTreeFromSchemaString(schema));
		res.setQuerySchema(schema);
		res.setQuerySchemaHead(predicate);
		System.out.println("Schema head in setter " + res.getQuerySchemaHead()) ;
		
		return res;
	}
	
	//create tree structure from schema string that has just
	//been created based on the query
	public Node createTreeFromSchemaString(String schemaStr){
		Node root;
		
		String[] schemaArr = schemaStr.split("[,)(]");
		
		//first element is predicate
		//therefore root of our tree
		root = new Node(schemaArr[0]);
		
		//then we need to add children to our root node
		for(int i = 1 ; i < schemaArr.length ; i++){
			root.addChild(new Node(schemaArr[i]));
		}
		
		return root;
	}
	
}
