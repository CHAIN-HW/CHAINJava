package chain_source;

/* Author Tanya Howden
 * Date September 2017
 * Modified
 */
/*
 * 
 * Structure that is used to store the 
 * ontology structure being used to 
 * create the queries
 * 
 * Stores the name of the ontology,
 * any links and the associated
 * properties/key words
 * 
 */
public class Ontology_Struc {

	//FIELDS
	private String name;
	private String link;
	private String[] properties;
	
	//CONSTRUCTOR
	public Ontology_Struc(String ontName, String ontLink, String[] ontProperties){
		name=ontName;
		link=ontLink;
		properties=ontProperties;
	}
	
	//PUBLIC METHODS
	//set the name
	public void setName(String ontName){
		name=ontName;
	}
	
	//returns the name
	public String getName(){
		return name;
	}
	
	//sets the link
	public void setLink(String ontLink){
		link=ontLink;
	}
	
	//returns the link
	public String getLink(){
		return link;
	}
	
	//sets the properties
	public void setProperties(String[] ontProperties){
		properties=ontProperties;
	}
	
	//returns the properties
	public String[] getProperties(){
		return properties;
	}
}
