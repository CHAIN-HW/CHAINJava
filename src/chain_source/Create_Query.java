package chain_source;
import java.nio.charset.StandardCharsets;

/* Author Tanya Howden
 * Date September 2017
 * Modified
 */
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.json.*;

import com.hp.hpl.jena.graph.Node;

/*
 *
 * @author Diana Bental
 * @author Tanya Howden
 * 
 * Date September 2017
 * Modified December 2017
 */
/*
 * Creates a (repaired) query directly from a (repaired) schema,
 * plus information about data bindings previously extracted from 
 * the original query.
 * 
 * This query may be created without either any data bindings,
 * or with all the original data bindings
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
	
	private static int WITHDATA = 1 ;
	private static int NODATA = 0 ;
		
	public static void main(String [] args){
		Create_Query queryCreator = new Create_Query();
		Call_SPSM spsmCall = new Call_SPSM();
		Repair_Schema getRepairedSchema = new Repair_Schema();

//		String source="surfaceWaterBodies(subBasinDistrict,riverName,altitudeTypology,associatedGroundwaterId,nonsense)";
//		String target="surfaceWaterBodies(subBasinDistrict,riverName,altitudeTypology,associatedGroundwaterId,nonsense)";
//		String qtype = "sepa" ;
//		String dataDirectory = "queryData/sepa/sepa_datafiles/";
//		String ontologyPath = "queryData/sepa/sepa_ontology.json" ;
//		int maxResults = 0;
//		String originalQuery = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
//		          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
//		          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
//		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
//		          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
//		          + "SELECT *  \n"
//		          + "FROM <queryData/sepa/sepa_datafiles/surfaceWaterBodies.n3>\n"
//		          + "WHERE { ?id sepaw:altitudeTypology ?altitudeTypology;\n"
//		          + "sepaw:associatedGrounwaterId ?associatedGroundwaterId  ;\n"
//		          + "sepaw:riverName ?riverName ;\n"
//		          + "sepaw:subBasinDistrict ?subBasinDistrict .}" ;
//		
		
		String source="River(Mountain,elevation)";
		String target="River(Mountain,elevation)";
		String qtype = "dbpedia" ;
		String dataDirectory = null ;
		String ontologyPath = "queryData/dbpedia/dbpedia_ontology.json" ;
		int maxResults = 30; 
		String originalQuery = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:Mountain ;\n"
		          + "dbo:elevation 10000 ;\n"
		          + ".}\n"
		          + "LIMIT 20" ;
		
		Query_Data queryData = new Query_Data(originalQuery) ;
		
		ArrayList<Match_Struc> finalRes = new ArrayList<Match_Struc>();
		
		finalRes = spsmCall.callSPSM(finalRes, source, target);

		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.repairSchemas(finalRes);
		}
		
	
		finalRes = queryCreator.createQueries(finalRes, queryData , qtype, dataDirectory, ontologyPath, WITHDATA, maxResults);
		// finalRes = queryCreator.createQueries(finalRes, queryData , qtype, dataDirectory, ontologyPath, NODATA, maxResults);
		
		System.out.println("\nQuery created is: \n\n" + finalRes.get(0).getQuery());
	}
	
	
	// By default - create bindings for object variables if possible
	public ArrayList<Match_Struc> createQueries(ArrayList<Match_Struc> matchRes, Query_Data queryData, String queryType, String datasetDir, String ontologyFilePath, int noResults) {
		return  createQueries(matchRes, queryData, queryType, datasetDir,  ontologyFilePath,  WITHDATA,  noResults) ;
	}
	
	// Create queries without any bindings for object variables
	public ArrayList<Match_Struc> createOpenQueries(ArrayList<Match_Struc> matchRes, Query_Data queryData, String queryType, String datasetDir, String ontologyFilePath, int noResults) {
		return  createQueries(matchRes, queryData, queryType, datasetDir,  ontologyFilePath,  NODATA,  noResults) ;
	}
	
	//sets up ready to create query for each repaired schema in list of match structures
	//Parameter sets whether to bind object variables or not
	public ArrayList<Match_Struc> createQueries(ArrayList<Match_Struc> matchRes, Query_Data queryData, String queryType, String datasetDir, String ontologyFilePath, int withBindings, int noResults){
		String type="";
		
		//first start off by checking query type required
		if(queryType == null){
			System.out.println("Please enter the type of query you want, either 'sepa' or 'dbpedia' as a parameter");
			return matchRes;
		}else{
			type=queryType;
		}
		
		// Read the ontology file and make the (target) ontology structure
		ArrayList<Ontology_Struc> ontologies = make_ontologies(ontologyFilePath) ;
		
		//for each of the match items, we want to create a query
		if(ontologies != null) {
			for(int i = 0 ; i < matchRes.size(); i++){
				Match_Struc matchDetails = matchRes.get(i);
			
				//call the appropriate method based
				//on the type of query
				String query = createQuery(type, matchDetails, queryData, datasetDir, withBindings, noResults, ontologies) ;
				System.out.println("Create_Query: Query created is: "+ query);
				matchDetails.setQuery(query);
			}
		}
		
		return matchRes;
	}
	
	// Read the ontology file and make the (target) ontology structure
	public static ArrayList<Ontology_Struc> make_ontologies(String ontologyFilePath) {
		ArrayList<Ontology_Struc> ontologies = new ArrayList<Ontology_Struc>();
		try{
			String jsonTxt = new String(Files.readAllBytes(Paths.get(ontologyFilePath)), StandardCharsets.UTF_8);
			
			JSONArray jsonArr = new JSONArray(jsonTxt);
			
			// read the ontology file and create the structures
			ontologies = makeOntologyStructures(jsonArr, ontologies);
		} catch(Exception e){
			e.printStackTrace();
		}
		return ontologies ;
	}
			

	public String createOpenQuery(String type, Match_Struc matchDetails, Query_Data queryData, String datasetDir, 
			int noResults, ArrayList<Ontology_Struc> ontologies) {
		
		return createQuery(type, matchDetails, queryData, datasetDir, NODATA, noResults, ontologies) ;
		
	}
	
	public String createQuery(String type, Match_Struc matchDetails, Query_Data queryData, String datasetDir, 
			int withBindings, int noResults,
			ArrayList<Ontology_Struc> ontologies) {
		
		if(type.equals("sepa") || type.equals("urbobvs")){
			return createTheQuery(matchDetails, queryData, datasetDir, withBindings, noResults, ontologies,type);

		}
		else{
			return createDbpediaQuery(matchDetails, queryData, withBindings, noResults, ontologies);
			
		}
	}
	
	//creates structure for urban observatory query
		public String createTheQuery(Match_Struc matchDetails, Query_Data queryData, String datafileDir, 
				int withBindings, int noResults, ArrayList<Ontology_Struc> ontologies, String type){
			System.out.println("Creating query");
			
			String query="";		
				
				//write prefix part of query
				query = query + writePrefixHeaders(ontologies);
				
				//Use the predicate to locate the file
				// String filename = matchDetails.getRepairedSchemaTree().getValue() + ".n3";
				String filename = null;
				
				if(type.equals("sepa")) {
					filename = matchDetails.getRepairedPredicate() + ".n3";
				}
				else if(type.equals("urbobvs")) {
					filename = matchDetails.getRepairedPredicate() + ".ttl";
				}
				
				String dbDir = datafileDir + filename;
				query = query + "\nSELECT *\n" + "FROM <"+ dbDir + ">\n"+"WHERE {  \n ?id";
				
				//then start getting different parts of data to search for
				ArrayList<String> schemaChildren = matchDetails.getRepairedParams();
				
				
				
				if(schemaChildren.size()>0){
					if(withBindings == WITHDATA ) {		
						query=query+ writeDataMatching(schemaChildren, matchDetails, queryData, ontologies);
					} else { //create an open query with no data
						query=query+ writeEmptyDataMatching(schemaChildren,ontologies);
					}
				}
				
				// Finish the query
				query = query + "\n}\n";
				
				//final line, limit
				if(noResults != 0){
					//set limit
					query = query + "LIMIT " + noResults;
				}	
				
			
			return query;
		}

	
	
	//creates structure for sepa query
//	public String createSepaQuery(Match_Struc matchDetails, Query_Data queryData, String datafileDir, 
//			int withBindings, int noResults, ArrayList<Ontology_Struc> ontologies){
//		System.out.println("Creating sepa query");
//		
//		String query="";		
//			
//			//write prefix part of query
//			query = query + writePrefixHeaders(ontologies);
//			
//			//Use the predicate to locate the file
//			// String filename = matchDetails.getRepairedSchemaTree().getValue() + ".n3";
//			String filename = matchDetails.getRepairedPredicate() + ".n3";
//			
//			String dbDir = datafileDir + filename;
//			query = query + "\nSELECT *\n" + "FROM <"+ dbDir + ">\n"+"WHERE {  \n ?id";
//			
//			//then start getting different parts of data to search for
//			ArrayList<String> schemaChildren = matchDetails.getRepairedParams();
//			
//			
//			
//			if(schemaChildren.size()>0){
//				if(withBindings == WITHDATA ) {		
//					query=query+ writeDataMatching(schemaChildren, matchDetails, queryData, ontologies);
//				} else { //create an open query with no data
//					query=query+ writeEmptyDataMatching(schemaChildren,ontologies);
//				}
//			}
//			
//			// Finish the query
//			query = query + "\n}\n";
//			
//			//final line, limit
//			if(noResults != 0){
//				//set limit
//				query = query + "LIMIT " + noResults;
//			}	
//			
//		
//		return query;
//	}

	//creates structure for dbpedia query
	public String createDbpediaQuery(Match_Struc matchDetails, Query_Data queryData, 
			int withBindings, int noResults, ArrayList<Ontology_Struc> ontologies){
		System.out.println("Creating dbpedia query ");

		String query="";

		//write prefix part of query
		query = query + writePrefixHeaders(ontologies);

		//start writing main bulk of query
		query = query + "\nSELECT DISTINCT *\n"+"WHERE { ?id rdf:type ";

		String predicate="";
		for(int j = 0 ; j < ontologies.size() ; j++){
			Ontology_Struc currentOntology = ontologies.get(j);
			HashSet<String> values = currentOntology.getValues() ;
			if(values != null) {
				String predicateName = matchDetails.getRepairedSchemaTree().getValue() ;
				if(values.contains(predicateName)) {
					predicate = currentOntology.getName() + ":" + predicateName ;				
				}

			}
		}

		if(predicate.equals("")){
			return "";
		}else{
			query = query + predicate;
		}

		//then start getting different parts of data to search for
		ArrayList<String> schemaChildren = matchDetails.getRepairedParams();
		
		
		if(schemaChildren.size()>0){
			query = query + ";\n";

			if(withBindings == WITHDATA ) {	
				query=query+ writeDataMatching(schemaChildren, matchDetails, queryData, ontologies);
			} else {
				query=query+ writeEmptyDataMatching(schemaChildren,ontologies);
			}
		}


		query = query + "}\n";

		//final line, limit
		if(noResults != 0){
			//set limit
			query = query + "LIMIT " + noResults;
		}	

		return query;
	}

	// Make the ontology structure for the target
	public static ArrayList<Ontology_Struc> makeOntologyStructures(JSONArray jsonArr, ArrayList<Ontology_Struc> ontologies){
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
	public static String writePrefixHeaders(ArrayList<Ontology_Struc> ontologies){
		String finalStr="";
		
		for(int i = 0 ; i < ontologies.size() ; i++){
			finalStr += ontologies.get(i).makePrefixHeader() ;
		}
		
		return finalStr;
	}
	
	// Write out all the property ?variable pairs for the WHERE part of the  query
	// e.g. dbp:river ?river ;
	// If the property does not belong to a known ontology then write nothing.
	public String writeEmptyDataMatching(ArrayList<String> schemaChildren, ArrayList<Ontology_Struc> ontologies){
		String dataDetails="";

		for(int i = 0 ; i < schemaChildren.size() ; i++){

			//get the names of the schema parameters
			//then see what ontology  they fall under
			//by looking at properties.
			String paramName = schemaChildren.get(i);

			for(Ontology_Struc currentOntology:ontologies) {					
				if(currentOntology.hasValue(paramName)) {
					// e.g. dbp:river ?river ;
					String search = " " + currentOntology.prefixedValue(paramName) + " ?" + paramName;
					//For all but the last child, add a semi-colon
					if(i+1 < schemaChildren.size()){
						search=search+" ;\n";
					}		
					dataDetails = dataDetails + search;
					break; // Stop searching now we've found a matching ontology
				}

			}
		}

		return dataDetails;
	}
	
	// Write out all the property ?variable or
	// ?property "data" pairs for the WHERE part of the  query
	// e.g. dbp:river ?river ;
	//      dbp:river "Thames" ;
	//      dbp:river <http://rivers.ont.com/Thames> ;
	// If the property does not belong to a known ontology then write nothing.
	// Include data items in the query.
	public String writeDataMatching(ArrayList<String> schemaChildren, Match_Struc matchDetails, Query_Data queryData, ArrayList<Ontology_Struc> ontologies){
		String dataDetails="";

		for(int i = 0 ; i < schemaChildren.size() ; i++){

			//get the names of the schema parameters
			
			String paramName = schemaChildren.get(i).replaceAll("[()]", ""); //HACK - remove dodgy parentheses
			
			String objectText = writeObject(paramName, matchDetails, queryData) ;
			
			System.out.println("writeDataMatching " + objectText + " paramName " + paramName);
			
			
		
				
			//then see what ontology the parameter falls under
			for(Ontology_Struc currentOntology:ontologies) {	
				System.out.println("ontologies " + currentOntology.getName() + currentOntology.getValues() ) ;
				
				if(currentOntology.hasValue(paramName)) {
					System.out.println("ontology has " + paramName) ;
					// e.g. dbp:river ?river ;
					String search = " " + currentOntology.prefixedValue(paramName) + objectText;
					//For all but the last child, add a semi-colon
					if(i+1 < schemaChildren.size()){
						search=search+" ;\n";
					}		
					dataDetails = dataDetails + search;
					break; // Stop searching now we've found a matching ontology
				}

			}
		}

		return dataDetails;
	}

	private String writeObject(String paramName, Match_Struc matchDetails, Query_Data queryData) {
		
		// Find the original name
		String sourceName = matchDetails.getSource(paramName);
			
		HashMap <String, Node> literals = queryData.localPropertyNameToLiteralObjectMaps ;		
		Node literalObject = literals.get(sourceName) ;
		// System.out.println("Create_Query:writeObject: literalObject  "+ literalObject);
		HashMap <String, String> uris = queryData.localPropertyNameToURIObjectMaps ;
		String uriObject = uris.get(sourceName) ;
					
		String objectText = " ?" + paramName;
					
		if(literalObject != null) {
			
			// If the literal is more than a simple quoted string then the datatype or  language information will be picked out of the query at this stage
			// and put into the query string.
			objectText =  " " + literalObject.toString() ;
			
			// Put angle brackets round the data type, if there is one
			String objectType = literalObject.getLiteralDatatypeURI() ;	
			if(objectType != null && !objectType.isEmpty()) {
				objectText = objectText.replaceAll(objectType, "<"+objectType+">") ;
			}
						
		} else if (uriObject != null) {
				objectText = " <" + uriObject + ">" ;
		}
					
		return objectText;

		
	}
}
