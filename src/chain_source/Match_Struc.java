package chain_source;
 import java.util.ArrayList;

/*
 * Structure that is being used to store
 * the results from SPSM.
 * 
 * This structure will have a similarity value,
 * the name of the target, a list of the individual
 * matches and relations as well as storing how many
 * individual matches this target has
 * 
 */
public class Match_Struc{
	
	//fields
	private double similarity;
	private String dataset;
	private ArrayList<String[]> matches;
	private Node repairedSchemaTree;
	private String repairedSchema;
	private int numMatches;
	private String query;
	
	//constructor
	public Match_Struc(double sim, String targetSchema){
		similarity=sim;
		dataset=targetSchema;
		matches=new ArrayList<String[]>();
		numMatches=0;
		repairedSchemaTree=null;
		repairedSchema="";
		query="";
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
}