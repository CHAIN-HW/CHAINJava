package chain_source;
 import java.util.ArrayList;
 
 /* Author Tanya Howden
  * Date September 2017
  * Modified
  */

/*
 * Structure that is being used to store
 * the results from SPSM and matches.
 * 
 * Represents one match between a source schema and a target schema, with its similarity score
 * and the repaired schema
 * and the SPARQL query that is generated from the repaired schema
 * 
 * This structure will have a similarity value,
 * the name of the target, a list of the individual
 * matches and relations as well as storing how many
 * individual matches this target has

 * 
 */
public class Match_Struc{
	
	//fields
	private double similarity;         // the similarity score for this SPSM schema match 
	private String dataset;			   // the target schema for this match
	private ArrayList<String[]> matches;      // components of this match
	private Node repairedSchemaTree;   // One repaired schema, as a tree
	private String repairedSchema;     // One repaired schema, as a string
	private int numMatches;   // The number of matching components (atoms) in the repaired query
	private String query;     // The query - either the initial query, or created from the repaired schema
	private String querySchema; // The original query schema
	private String querySchemaHead; // The head of the original query schema
	
	//constructor
	public Match_Struc(double sim, String targetSchema){
		similarity=sim;
		dataset=targetSchema;
		matches=new ArrayList<String[]>();
		numMatches=0;
		repairedSchemaTree=null;
		repairedSchema="";
		query="";
		querySchema="";
		querySchemaHead="";
	}
	
	//constructor 
	public Match_Struc(){
		similarity=0;
		dataset="";
		matches=new ArrayList<String[]>();
		numMatches=0;
		repairedSchemaTree=null;
		repairedSchema="";
		query="";
		querySchema="";
		querySchemaHead="";
	}
	
	//public methods
	public void setSimilarity(double sim){
		similarity=sim;
	}
	
	public void setDatasetSchema(String targetSchema){
		dataset=targetSchema;
	}
	
	public void addMatch(String[] match){
		matches.add(match); 
		numMatches++;
	}
	
	public void setQuerySchema(String qSchema){
		querySchema = qSchema;
	}
	
	public void setQuerySchemaHead(String qSchemaHead){
		querySchemaHead = qSchemaHead;
	}
	
	public int getNumMatches(){
		return numMatches;
	}
	
	public double getSimValue(){
		return similarity;
	}
	
	public String getDatasetSchema(){
		return dataset;
	}
	
	public ArrayList<String[]> getMatches(){
		return matches;
	}
	
	public String[] getMatchAtIndex(int index){
		return matches.get(index);
	}
	
	public void setRepairedSchemaTree(Node schemaTree){
		repairedSchemaTree = schemaTree;
	}
	
	public void setRepairedSchema(String stringSchema){
		repairedSchema = stringSchema;
	}
	
	public String getRepairedSchema(){
		return repairedSchema;
	}
	
	public Node getRepairedSchemaTree(){
		return repairedSchemaTree;
	}
	
	public void setQuery(String matchQuery){
		query=matchQuery;
	}
	
	public String getQuery(){
		return query;
	}
	
	public String getQuerySchema(){
		return querySchema;
	}
	
	public String getQuerySchemaHead(){
		return querySchemaHead;
	}
}