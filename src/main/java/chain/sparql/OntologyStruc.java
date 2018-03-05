package chain.sparql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/* Author Tanya Howden
 * Diana Bental
 * Date September 2017
 * Modified December 2017
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
public class OntologyStruc {

	//FIELDS
	private String name;
	private String link;
	private HashSet<String> values ; //  DB
	
	//CONSTRUCTOR
	public OntologyStruc(String ontName, String ontLink, String[] ontProperties){
		name=ontName;
		link=ontLink;

		values = new HashSet<String> () ;
		if(ontProperties != null) {
			for(String p:ontProperties) {
				values.add(p) ;
			}
		}
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
	

	//New DB - return the values whose names are in this ontology
	public HashSet<String> getValues() {
		return values;
	}
	
	// Test if a value is in the ontology
	public boolean hasValue(String Value) {
		if(values.isEmpty()) return false ;
		return values.contains(Value) ;		
	}
	
	//Return the value prefixed by the ontology name
	// e.g. dbo:river
	public String prefixedValue(String value) {
		if(values.isEmpty()) return value ; //Maybe return "" instead?
		return name+":"+value ;
	}
	
	//Return the value prefixed by the fully resolved link
	// e.g. http://dbpedia.org/ontology/river
	public String resolvedValue(String value) {
		if(values.isEmpty()) return value ; //Maybe return "" instead?
		String temp = link ;
		temp.replace("^<", "") ;
		temp.replace(">$", "") ;
		return temp+value ;
	}
	
	// Return the Sparql header for this ontology e.g.
	// PREFIX dbo: <http://dbpedia.org/ontology/>
	public String makePrefixHeader() {
		return "PREFIX " + name + ": " + link + "\n";
	}
	
}
