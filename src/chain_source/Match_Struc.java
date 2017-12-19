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
	private String datasetSchema;      // the target schema for this match
	private ArrayList<String[]> matchComponents;   // components of this match
	private NodeCHAIN repairedSchemaTree;   // One repaired schema, as a tree
	private String repairedSchema;     // One repaired schema, as a string
	private int numMatchComponents;   // The number of matching components (atoms) in the repaired query
	private String query;     // The query - either the initial query, or null if the original schema wont parse, or created from the repaired schema
	private String querySchema; // The original query schema
	private String querySchemaHead; // The head of the original query schema
	
	//constructor
	public Match_Struc(double sim, String targetSchema){
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
	public Match_Struc(){
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
	
	public String getTarget(String Source){
		String Target = "";
		for (String[] m: matchComponents) {
			String[] SourcesString = m[0].split(",") ;
			String[] TargetsString = m[2].split(",") ;
			for(int i= 0; i < SourcesString.length; i++ ) {
				if (SourcesString[i].equals(Source))
				{
					Target = TargetsString[i] ;
					return Target ;
				}
			}
			
		}
		return Target;		
	}
	
	public String getSource(String Target){
		String Source = "";
		for (String[] m: matchComponents) {
			String[] SourcesString = m[0].split(",") ;
			String[] TargetsString = m[2].split(",") ;
			for(int i= 0; i < TargetsString.length; i++ ) {
				if (TargetsString[i].equals(Target))
				{
					Source = SourcesString[i] ;
					return Source ;
				}
			}
			
		}
		return Source;		
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
		
		return("Match_Struc: \n" 
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