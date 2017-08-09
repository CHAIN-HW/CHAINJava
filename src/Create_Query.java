import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.*;

/*
 *
 * Responsible for using the repaired schema
 * to create either a sepa or dbpedia query, this 
 * query is added as a field onto the Match_Struc 
 * object structure
 * 
 * This class is tested in Create_Query_Test_Cases.java
 * 
 */
public class Create_Query {
	
	public static void main(String [] args){
		Create_Query queryCreator = new Create_Query();
		Call_SPSM spsmCall = new Call_SPSM();
		Repair_Schema getRepairedSchema = new Repair_Schema();

		String source="surfaceWaterBodies(subBasinDistrict,riverName,altitudeTypology,associatedGroundwaterId,nonsense)";
		String target="surfaceWaterBodies(subBasinDistrict,riverName,altitudeTypology,associatedGroundwaterId,nonsense)";
		
		//String source="River(Mountain(elevation))";
		//String target="River(Mountain(elevation))";
		
		ArrayList<Match_Struc> finalRes = new ArrayList<Match_Struc>();
		
		finalRes = spsmCall.getSchemas(finalRes, source, target);

		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = queryCreator.createQueryPrep(finalRes, "sepa", "queryData/sepa/sepa_datafiles/",0);
		//finalRes = queryCreator.createQueryPrep(finalRes, "dbpedia", null, 30);
		
		System.out.println("\nQuery created is: \n\n" + finalRes.get(0).getQuery());
	}
	
	//sets up ready to create query for each repaired schema in list of match structures
	public ArrayList<Match_Struc> createQueryPrep(ArrayList<Match_Struc> matchRes, String queryType, String datasetDir, int noResults){
		String type="";
		
		//first start off by checking query type required
		if(queryType == null){
			System.out.println("Please enter the type of query you want, either 'sepa' or 'dbpedia' as a parameter");
			return matchRes;
		}else{
			type=queryType;
		}
		
		//for each of the match items, we want to create a query
		String query="";
		for(int i = 0 ; i < matchRes.size(); i++){
			
			Match_Struc curr = matchRes.get(i);
			
			//call the appropriate method based
			//on the type of query
			if(type.equals("sepa")){
				query = createSepaQuery(curr, datasetDir, noResults);
				curr.setQuery(query);
			}else{
				query = createDbpediaQuery(curr, noResults);
				curr.setQuery(query);
			}
		}
		
		return matchRes;
	}
	
	//creates structure for sepa query
	public String createSepaQuery(Match_Struc matchDetails, String datafileDir, int noResults){
		System.out.println("Creating sepa query");
		
		ArrayList<Ontology_Struc> ontologies = new ArrayList<Ontology_Struc>();
		String query="";
		
		//start off by selecting ontology file
		try{
			String jsonTxt = new String(Files.readAllBytes(Paths.get("queryData/sepa/sepa_ontology.json")), StandardCharsets.UTF_8);
			JSONArray jsonArr = new JSONArray(jsonTxt);
			
			//now call function to get prefix
			ontologies = setupPrefixes(jsonArr, ontologies);
			
			//write prefix part of query
			query = query + writePrefix(ontologies);
			
			//start writing main bulk of query
			String filename = matchDetails.getRepairedSchemaTree().getValue() + ".n3";
			
			String dbDir = datafileDir + filename;
			query = query + "\nSELECT *\n" + "FROM <"+ dbDir + ">\n"+"WHERE { ?id ";
			
			//then start getting different parts of data to search for
			List<Node> schemaChildren = matchDetails.getRepairedSchemaTree().getChildren();
			
			if(schemaChildren.size()>0){
				query=query+ dataMatching(schemaChildren,ontologies);
			}else{
				query = query + "\n.}\n";
			}
			
			//final line, limit
			if(noResults != 0){
				//set limit
				query = query + "LIMIT " + noResults;
			}	
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return query;
	}
	
	//creates structure for dbpedia query
	public String createDbpediaQuery(Match_Struc matchDetails, int noResults){
		System.out.println("Creating dbpedia query");
		
		ArrayList<Ontology_Struc> ontologies = new ArrayList<Ontology_Struc>();
		String query="";
		
		//start off by selecting ontology file
		try{
			String jsonTxt = new String(Files.readAllBytes(Paths.get("queryData/dbpedia/dbpedia_ontology.json")), StandardCharsets.UTF_8);
			JSONArray jsonArr = new JSONArray(jsonTxt);
			
			//now call function to get prefix
			ontologies = setupPrefixes(jsonArr, ontologies);
			
			//write prefix part of query
			query = query + writePrefix(ontologies);
			
			//start writing main bulk of query
			query = query + "\nSELECT DISTINCT*\n"+"WHERE { ?id rdf:type ";
			
			String predicate="";
			for(int j = 0 ; j < ontologies.size() ; j++){
				Ontology_Struc currentOntology = ontologies.get(j);
				String[] properties = currentOntology.getProperties();
				
				if(!(properties == null)){
					if((Arrays.asList(properties).indexOf(matchDetails.getRepairedSchemaTree().getValue())) != -1){
						predicate = currentOntology.getName() + ":" + matchDetails.getRepairedSchemaTree().getValue();
						break;
					}
				}
				//query = query + currentOntology.getName() + ":" + matchDetails.getRepairedSchemaTree().getValue();
			}
			
			if(predicate.equals("")){
				return "";
			}else{
				query = query + predicate;
			}
			
			//then start getting different parts of data to search for
			List<Node> schemaChildren = matchDetails.getRepairedSchemaTree().getChildren();
			
			if(schemaChildren.size() > 0){
				query = query + ";\n";
				query=query+ dataMatching(schemaChildren,ontologies);
			}else{
				query = query + ".}\n";
			}
			
			//final line, limit
			if(noResults != 0){
				//set limit
				query = query + "LIMIT " + noResults;
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return query;
	}
	
	//sets up prefix links for the queries
	public ArrayList<Ontology_Struc> setupPrefixes(JSONArray jsonArr, ArrayList<Ontology_Struc> ontologies){
		String name,link,propString;
		String[] properties;
		
		Ontology_Struc ontology;
		
		//for each element in json arr create prefix string
		for(int i = 0 ; i < jsonArr.length() ; i++){
				
			try {
				JSONObject current = jsonArr.getJSONObject(i);
				
				name = current.getString("name");
				link = current.getString("link");
				
				if(current.has("properties")){
					propString = current.getString("properties");
					properties = propString.split(",");	
				}else{
					properties=null;
				}
				
				ontology = new Ontology_Struc(name,link,properties);
				ontologies.add(ontology);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return ontologies;
	}
	
	//writes the prefixes and links onto query string
	public String writePrefix(ArrayList<Ontology_Struc> ontologies){
		String currPrefixStr="",finalStr="";
		
		for(int i = 0 ; i < ontologies.size() ; i++){
			
			Ontology_Struc curr = ontologies.get(i);
			currPrefixStr = "PREFIX " + curr.getName() + ": " + curr.getLink() + "\n";
			
			finalStr = finalStr + currPrefixStr;
		}
		
		return finalStr;
	}
	
	//finds which ontology file the keywork relates to
	public String dataMatching(List<Node> children, ArrayList<Ontology_Struc> ontologies){
		String dataDetails="";
		
		for(int i = 0 ; i < children.size() ; i++){
			
			//get the names of the schema parameters
			//then see what ontology file they fall under
			//by looking at properties
			String paramName = children.get(i).getValue();
			
			for(int j = 0 ; j < ontologies.size() ; j++){

				Ontology_Struc currentOntology = ontologies.get(j);
				
				String[] properties = currentOntology.getProperties();
				
				if(!(properties == null)){
					
					int index;
					
					if((index = Arrays.asList(properties).indexOf(paramName)) != -1){
						String search = currentOntology.getName() + ":" + currentOntology.getProperties()[index] + " ?" + paramName;
						
						//check if it's the last one because otherwise
						//we want to add ;
						if(i+1 >= children.size()){
							search = search + "\n.}\n";
						}else{
							search=search+" ;\n";
						}
						
						dataDetails = dataDetails + search;
						break;
					}
				}			
			}
		}
		
		return dataDetails;
	}
}
