/**
 * 
 */
package chain.sparql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import chain.core.CallSPSM;
import chain.core.MatchStruc;
import chain.core.RepairSchema;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;

/**
 * @author Diana Bental
 * @date 10 January 2017
 *
 * Repair a query by replacement of matching properties and removal of
 * non-matching ones.
 * Creates the repaired query from the original query, plus the SPSM matches,
 * plus information about data bindings previously extracted from 
 * the original query.
 */
public class RepairQuery {
	
	private static int WITHDATA = 1 ;
	private static int NODATA = 0 ;  // We can't do queries with no data at present

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CallSPSM spsmCall = new CallSPSM();
		RepairSchema getRepairedSchema = new RepairSchema();
		
//		String source="River(Mountain,explorer,elevation,location)";
//		String target="River(Mountain,elevation,location)";
//		String qtype = "dbpedia" ;
//		String dataDirectory = null ;
//		String ontologyPath = "queryData/dbpedia/dbpedia_ontology.json" ;
//		int maxResults = 30; 
//		String originalQuery = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
//		          + "PREFIX dbp: <http://dbpedia.org/property/>   \n"
//		          + "PREFIX res: <http://dbpedia.org/resource/> \n"
//		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
//		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
//		          + "PREFIX yago: <hhtp://dbpedia.org/class/yago/> \n\n"
//		          + "SELECT DISTINCT *  \n"
//		          + "WHERE { ?id rdf:type dbo:River;\n"
//		          + "dbp:Mountain ?Mountain ;\n"
//		          + "dbp:location dbo:USA ; \n"
//		          + "dbp:explorer dbo:Livingston; \n"
//		          + "dbp:elevation 1000 ;\n"
//		          + ".}\n"
//		          + "LIMIT 20" ;
		
//		String source="City(country, populationTotal)";
//		String target="City(country, populationTotal)";
		
//		String source="City(country)";
//		String target="City(country)";
//		
//		String qtype = "dbpedia" ;
//		String dataDirectory = null ;
//		String ontologyPath = "queryData/dbpedia/dbpedia_ontology.json" ;
//		int maxResults = 30; 
//		
//		String originalQuery = "PREFIX dbot: <http://dbpedia.org/ontology/>  \n"
//				+ "PREFIX res: <http://dbpedia.org/resource/>   \n"
//				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>   \n"
//				+ "SELECT DISTINCT ?uri   \n"
//				+ "WHERE {   \n"
//				+ "    ?uri rdf:type dbot:City .   \n"
//				+ "    ?uri dbot:country res:Germany .   \n"
//				+ "    ?uri dbot:populationTotal ?population .   \n"
//				+ "    FILTER ( ?population > 250000 ) }   \n" ;
		
//		String source="Mountain(elevation)";
//		String target="Mountain(elevation)";
//		String qtype = "dbpedia" ;
//		String dataDirectory = null ;
//		String ontologyPath = "queryData/dbpedia/dbpedia_ontology.json" ;
//		int maxResults = 30; 
//		String originalQuery = "PREFIX dbo: <http://dbpedia.org/ontology/>  \n"
//				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  \n"
//				+ "SELECT DISTINCT ?uri  \n"
//				+ "WHERE {  \n"
//				+ "   ?uri rdf:type dbo:Mountain .  \n"
//				+ "   ?uri dbo:elevation ?elevation .  \n"
//				+ "}  \n"
//				+ "ORDER BY DESC(?elevation)  \n"
//				+ "OFFSET 1 LIMIT 1  \n" ;

		
		
		String source="surfaceWaterBodies(subBasinDistrict,riverName,altitudeTypology,associatedGroundwaterId,nonsense)";
		String target="surfaceWaterBodies(subBasinDistrict,riverName,altitudeTypology,associatedGroundwaterId,nonsense)";
		String qtype = "sepa" ;
		String dataDirectory = "queryData/sepa/sepa_datafiles/";
		String ontologyPath = "queryData/sepa/sepa_ontology.json" ;
		int maxResults = 0;
		String originalQuery = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
		          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
		          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
		          + "SELECT *  \n"
		          + "FROM <queryData/sepa/sepa_datafiles/surfaceWaterBodies.n3>\n"
		          + "WHERE { ?id sepaw:altitudeTypology ?altitudeTypology;\n"
		          + "sepaw:associatedGrounwaterId ?associatedGroundwaterId  ;\n"
		          + "sepaw:riverName ?riverName ;\n"
		          + "sepaw:subBasinDistrict ?subBasinDistrict .}" ;
		
		
		
		QueryData queryData = new QueryData(originalQuery) ;
		
		System.out.println("Original query (Jena formatted)\n" + queryData.originalQuery) ;
		
		
		
		ArrayList<MatchStruc> finalRes = new ArrayList<MatchStruc>();
		finalRes = spsmCall.callSPSM(finalRes, source, target);

		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.repairSchemas(finalRes);
		}
		
		System.out.println(queryData) ;		
		// System.out.println(finalRes.get(0).getRepairedSchemaTree()) ;
	
		
		finalRes = repairQueries(finalRes, queryData, qtype, dataDirectory,ontologyPath,maxResults);
		

	}
	
	// Set up a map of old properties (with old prefixes) to new properties (with new prefixes)
	private static HashMap<String, String> getMatchedProperties(HashMap<String, String> matches,
			HashMap<String, String> localNameToPrefixMaps, ArrayList<OntologyStruc> ontologies) {
		
		HashMap <String, String> resolvedMatches = new HashMap <String, String>() ;
		for(Map.Entry<String, String> entry : matches.entrySet()){ 
			String s = entry.getKey() ;
			String t = entry.getValue() ;
			String rS = localNameToPrefixMaps.get(s) + ":" + s ;
			for(OntologyStruc currentOntology:ontologies) {
				if(currentOntology.hasValue(t)) {
					String rT = currentOntology.getName()+ ":" + t; ;
			
					resolvedMatches.put(rS, rT) ;
				}
			}
		}
		return resolvedMatches;
	}

	// Make a set of all the old property names (with prefixes) which are unmatched
	private static HashSet <String> getUnMatchedProperties(Set<String> matchedProperties, HashSet<String> localPropertyNames, HashMap<String, String> localNameToPrefixMaps) {
		HashSet <String> unMatched = new HashSet <String>();
		for(String property:localPropertyNames) {
			if(!matchedProperties.contains(property)) {
				unMatched.add(localNameToPrefixMaps.get(property)+":"+property);
			}
		}
		return unMatched ;
	}


	// Create repaired repaired queries for all matches returned by SPSM
	// This is done with object values instantiated (if possible)
	public static ArrayList<MatchStruc> repairQueries(ArrayList<MatchStruc> matchRes, QueryData queryData, String queryType, String datasetDir, String ontologyFilePath, int noResults) {
			return  repairQueries(matchRes, queryData, queryType, datasetDir,  ontologyFilePath,  WITHDATA,  noResults) ;
	}
 
	public static ArrayList<MatchStruc> repairQueries(ArrayList<MatchStruc> matchRes, QueryData queryData,
													  String queryType, String datasetDir, String ontologyFilePath, int withBindings, int noResults) {
		
		
		// System.out.println("Original query (Jena formatted)\n" + queryData.originalQuery) ;
		
		// Read the ontology file and make the (target) ontology structure
		ArrayList<OntologyStruc> ontologies = CreateQuery.make_ontologies(ontologyFilePath) ;
		
		//for each of the match items, we want to create a query
		if(ontologies != null) {
			for(int i = 0 ; i < matchRes.size(); i++){
				MatchStruc matchDetails = matchRes.get(i);
					
				//call the appropriate method based
				//on the type of query
				String query = repairQuery(matchDetails, queryData, queryType, datasetDir, withBindings, noResults, ontologies) ;
				// System.out.println("CreateQuery: Query created is: "+ query);
				matchDetails.setQuery(query);
			}
		}
				
		return matchRes;

	}

	public static String repairQuery(MatchStruc matchDetails, QueryData queryData, String queryType, String datasetDir, int withBindings,
									 int noResults, ArrayList<OntologyStruc> ontologies) {
		
		HashMap <String, String> matches = matchDetails.getMatches() ;
		
		System.out.println("matches (from SPSM) " + matches) ;
		
		// Identify the unmatched source properties
		HashSet <String> unMatched = getUnMatchedProperties(matches.keySet(), queryData.localPropertyNames, queryData.localNameToPrefixMaps) ;
		System.out.println("unMatched " + unMatched) ;
		
		String query = queryData.originalQuery ;
		// Identify the matched properties (and predicate, if it's a dbpedia query)
		HashMap <String, String> matched = getMatchedProperties(matches, queryData.localNameToPrefixMaps, ontologies) ;		
		// Replace all the matched properties (and their prefixes) with new properties (and prefixes)
		// and the  predicate (for a dbpedia-style query)
		System.out.println("matched: " + matched);
		for(String sourceProp: matched.keySet()){
			String targetProp = matched.get(sourceProp) ;
			query = query.replaceAll(sourceProp, targetProp) ;	
		}
		System.out.println("query (with matched predicates replaced):\n"+query);
		
		// Put all the new PREFIX headers at the start of the repaired query
		ArrayList<String> newQuerySplit = new ArrayList<String>() ;
		for(int i = 0 ; i < ontologies.size() ; i++){
			String ont = ontologies.get(i).getName() ;
			String link = ontologies.get(i).getLink() ;
			newQuerySplit.add(ontologies.get(i).makePrefixHeader()) ;
		}
		
		// System.out.println("newQuerySplit"+newQuerySplit) ;
		
		// Divide the query into lines, and flag unwanted lines for removal; copy other lines to the new query
		ArrayList<String> splitQuery = new ArrayList<>(Arrays.asList(query.split("\n"))) ;
		boolean copyLine = true ;
		for(String line:splitQuery) {
			copyLine = true ;
			// Don't copy the old PREFIX headers
			if(line.contains("PREFIX ")) copyLine = false ;
			else {
				for(String ignoredProp: unMatched) {
					// Copy the predicate for dbpedia style queries
					if(queryType.equals("dbpedia") && ignoredProp.equals("rdf:type")) continue ;  // include rdf:type for dbpedia queries
					// Remove any lines that contain a property that SPSM didn't match
					if(line.contains(ignoredProp)) {
						copyLine = false ;
					}
					// Remove any lines that contain a variable which was bound by a property that SPSM didn't match
					String localIgnoredProp = ignoredProp.split(":")[1] ;
					String ignoredVar = queryData.localPropertyNameToVariableMaps.get(localIgnoredProp) ;
					if(ignoredVar!=null && line.contains(ignoredVar)) copyLine = false ;
				}
			}
			if(copyLine) newQuerySplit.add(line) ;
			else if (line.contains("{")) newQuerySplit.add("{") ; // Deal with deleting the first  line after WHERE
			else if (line.contains("}")) newQuerySplit.add("}") ;
		}
		
		// System.out.println("newQuerySplit"+newQuerySplit) ;
		
		query = String.join("\n", newQuerySplit);
		
		try {
			Query q = QueryFactory.create(query);
			query = q.toString();
			System.out.println("Final Query:\n" + query);
		
		} catch (Exception e) {
			
			System.out.println("RepairQuery.java: The repaired query string doesn't parse as correct Sparql.");
			System.out.println(query) ;
			query="";

		}
		
		
		return query;
	}
}
