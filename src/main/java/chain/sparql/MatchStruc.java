package chain.sparql;

import java.util.ArrayList;
import java.util.HashMap;

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
public class MatchStruc {
	
	//fields
	private double similarity;         // the similarity score for this SPSM schema match 
	private String datasetSchema;      // the target schema for this match
	private ArrayList<String[]> matchComponents;   // components of this match
	private NodeCHAIN repairedSchemaTree;   // One repaired schema, as a tree
	private String repairedSchema;     // One repaired schema, as a string
	private int numMatchComponents;   // The number of matching components (atoms) in the repaired query
	private String query;     // The query - either the initial query, or null if the original schema wont parse, or created from the repaired schema
	private String querySchema; // The original query schema
	private String querySchemaHead; // The head of the original query schema
	
	//constructor
	public MatchStruc(double sim, String targetSchema){
		similarity=sim;
		datasetSchema=targetSchema;
		matchComponents=new ArrayList<String[]>();
		numMatchComponents=0;
		repairedSchemaTree=null;
		repairedSchema="";
		query="";
		querySchema="";
		querySchemaHead="";
	}
	
	//constructor 
	public MatchStruc(){
		similarity=0;
		datasetSchema="";
		matchComponents=new ArrayList<String[]>();
		numMatchComponents=0;
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
		datasetSchema=targetSchema;
	}
	
	public void addMatch(String[] match){
		matchComponents.add(match); 
		numMatchComponents++;
	}
	
	public void setQuerySchema(String qSchema){
		querySchema = qSchema;
	}
	
	public void setQuerySchemaHead(String qSchemaHead){
		querySchemaHead = qSchemaHead;
	}
	
	public int getNumMatchComponents(){
		return numMatchComponents;
	}
	
	public String getTarget(String source){
		String target = "";
		for (String[] m: matchComponents) {
			String[] sourcesString = m[0].split(",") ;
			String[] targetsString = m[2].split(",") ;
			for(int i= 0; i < sourcesString.length; i++ ) {
				if (sourcesString[i].equals(source))
				{
					target = targetsString[i] ;
					return target ;
				}
			}
			
		}
		return target;
	}
	
	public String getSource(String target){
		String source = "";
		for (String[] m: matchComponents) {
			String[] sourcesString = m[0].split(",") ;
			String[] targetsString = m[2].split(",") ;
			for(int i= 0; i < targetsString.length; i++ ) {
				if (targetsString[i].equals(target))
				{
					source = sourcesString[i] ;
					return source ;
				}
			}
			
		}
		return source;
	}
	
	public double getSimValue(){
		return similarity;
	}
	
	public String getDatasetSchema(){
		return datasetSchema;
	}
	
	public ArrayList<String[]> getMatchComponents(){
		return matchComponents;
	}
	
	public String[] getMatchAtIndex(int index){
		return matchComponents.get(index);
	}
	
	public void setRepairedSchemaTree(NodeCHAIN schemaTree){
		repairedSchemaTree = schemaTree;
	}
	
	public void setRepairedSchema(String stringSchema){
		repairedSchema = stringSchema;
	}
	
	public String getRepairedSchema(){
		return repairedSchema;
	}
	
	public NodeCHAIN getRepairedSchemaTree(){
		return repairedSchemaTree;
	}
	
	public String getRepairedPredicate(){
		return getRepairedSchemaTree().getValue();
	}
	
	// Paramaters are just the names of the children
	public ArrayList<String> getRepairedParams(){
		return getRepairedSchemaTree().getChildrenValues();
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
	
	public HashMap <String, String> getMatches() {
		HashMap <String, String> matches = new HashMap <String, String>();
		for (String[] m: matchComponents) {
			String source = m[0] ;
			String target = m[2] ;
			String[] sources = source.split(",");
			String[] targets = target.split(",");
			for(int i = 0; i < sources.length; i++) {
				matches.put(sources[i],targets[i]) ;
			}
		}
		
		return matches ;
	}
	

	
	
	public String toString() {
		String matchesStr = new String () ;
		matchesStr = "";
		for (String[] m: matchComponents) {
			matchesStr += "(" ;
			matchesStr += m[0] ;
			matchesStr += ")" ;
			matchesStr += m[1] ;
			matchesStr += "(" ;
			matchesStr += m[2] ;
			matchesStr += ")" ;
			matchesStr += "\n" ;
		}
		
		return("MatchStruc: \n"
				+ "similarity: " + similarity + "\n"
				+ "dataset: " + datasetSchema + "\n"
				+ "matchComponents: " + matchesStr + "\n"
				+ "repairedSchemaTree: " + repairedSchemaTree + "\n"
				+ "repairedSchema: " + repairedSchema + "\n"
				+ "numMatchComponents: " + numMatchComponents + "\n"
				+ "query: " + query + "\n"
				+ "querySchema: " + querySchema + "\n"
				+ "querySchemaHead: " + querySchemaHead  + "\n"
				) ;
	}



}