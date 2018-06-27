package chain_source;
 import java.util.*;

import com.hp.hpl.jena.graph.Node;

/* Author Tanya Howden
  * Date September 2017
  * Modified Diana Bental November 2017
  * - use the property names for schema predicate and parameters instead of variable names
  */

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.sparql.core.TriplePath;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.syntax.ElementPathBlock;
import com.hp.hpl.jena.sparql.syntax.ElementVisitorBase;
import com.hp.hpl.jena.sparql.syntax.ElementWalker;

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
		// System.out.println("Schema created from query: \n\n"+res.getRepairedSchema());
		// System.out.println("Query Schema "+ res.getQuerySchema());
		// System.out.println("Query Schema Head "+ res.getQuerySchemaHead());
		// System.out.println("Dataset Schema "+ res.getDatasetSchema());
		
		
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
		// System.out.println("Schema_From_Query: " + result) ;
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
				res.setQuery(sepaQuery.toString());
				
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
				
				//add to schema string
				schema = datasetFile + "(";
				
				// Temp structure for elements of a CHAIn schema
				ArrayList<String> chainParameters = new ArrayList<String>();
				
				ElementWalker.walk(sepaQuery.getQueryPattern(),
					    // For each element...
					    new ElementVisitorBase() {
					        // ...when it's a block of triples...
					        public void visit(ElementPathBlock el) {
					            // ...go through all the triples...
					            Iterator<TriplePath> triples = el.patternElts();
					            while (triples.hasNext()) {
					            	
					                // Get the next triple
					            	TriplePath triple = triples.next() ;
					            	// System.out.println(triple) ;
					            	
					            	// Split into subject, rdf-predicate and object
					            	Node subjectNode = triple.getSubject() ;
					            	Node objectNode = triple.getObject() ;
					            	Node predicateNode = triple.getPredicate();
					            	
					            	// Parse the rdf-predicate into the namespace (prefix) and value
					            	String nameSpace = predicateNode.getNameSpace(); 
					            	String localName = predicateNode.getLocalName() ;
					            		
					            	// The rdf predicate becomes a CHAIN parameter
					       
					            	chainParameters.add(predicateNode.getLocalName()) ;
					            	
					            }
					        }
					    }
					);
				
				int size = chainParameters.size() ;
				
				if (size > 0) {
					for(int i = 1 ; i < size ; i++) {
						schema = schema + chainParameters.get(i-1) + ",";
					}
					schema = schema + chainParameters.get(size-1) ;
				}
				//add final bracket
				schema=schema+")";
				
				System.out.println("Schema_From_Query: Chain schema "+schema);				
				
			
				
			}catch(Exception e){
				//invalid query, return null
				System.out.println("Cannot parse this Sparql query, returning null.\n");
				return null;
			}
		}
		
		
		//set string and tree equivalent of schema in our structure
		//and return
		// System.out.println("Schema_From_Query: Predicate " + predicate) ;
		//res.setRepairedSchema(schema);
		//res.setRepairedSchemaTree(createTreeFromSchemaString(schema));
		res.setQuerySchema(schema);
		res.setQuerySchemaHead(predicate);
		// System.out.println("Schema_From_Query: Schema head in setter " + res.getQuerySchemaHead()) ;
		
		return res;
	}
	
	//create the schema for a dbpedia query that has been passed in as a parameter
	public Match_Struc schemaFromDbpediaQuery(String query, Match_Struc res){
		String schema="";
		String predicate = "";
		
		if(!query.equals("")){

			try{
			
				
				//create query object
				// Parse with Jena - this will raise an exception if Jena can't parse it
				Query dbpediaQuery = QueryFactory.create(query);
				res.setQuery(dbpediaQuery.toString());
				
				// Temp structure for elements of a CHAIn schema
				ArrayList<String> chainParameters = new ArrayList<String>();
				ArrayList<String> chainPredicates = new ArrayList<String>() ;
				
				ElementWalker.walk(dbpediaQuery.getQueryPattern(),
					    // For each element...
					    new ElementVisitorBase() {
					        // ...when it's a block of triples...
					        public void visit(ElementPathBlock el) {
					            // ...go through all the triples...
					            Iterator<TriplePath> triples = el.patternElts();
					            while (triples.hasNext()) {
					            	 
					                // Get the next triple
					            	TriplePath triple = triples.next() ;
					            	// System.out.println(triple) ;
					            	
					            	// Split into subject, rdf-predicate and object
					            	Node subjectNode = triple.getSubject() ;
					            	Node objectNode = triple.getObject() ;
					            	Node predicateNode = triple.getPredicate();
					            	
					            	// Parse the rdf-predicate into the namespace (prefix) and value
					            	String nameSpace = predicateNode.getNameSpace(); 
					            	String localName = predicateNode.getLocalName() ;
					            	
					            	// If it's an rdf:type then its object value becomes the CHAIN predicate 
					            	if (nameSpace.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#") &&
					            			localName.equals("type")) {
					            		chainPredicates.add(objectNode.getLocalName()) ;
					            	}
					            	// If it's anything else then the rdf predicate becomes a CHAIN parameter
					            	else {
					            		chainParameters.add(predicateNode.getLocalName()) ;
					            	}
					            			
					            	
					            	
					            }
					        }
					    }
					);
				
				//start schema string
				if(chainPredicates.isEmpty()) {
					System.out.println("No schema predicate has been identified.") ;
					return null ;
				} else {
					predicate = chainPredicates.get(0) ;
					schema = predicate + "(";
				
					int size = chainParameters.size() ;
				
					if (size > 0) {
						for(int i = 1 ; i < size ; i++) {
							schema = schema + chainParameters.get(i-1) + ",";
						}
						schema = schema + chainParameters.get(size-1) ;
					}
				
					schema = schema + ")" ;
				
					System.out.println("Chain schema "+schema);
				}
				
			}catch(Exception e){
				//invalid query, returning null
				System.out.println("Cannot parse this Sparql query, returning null.\n");
				return null;
			}
		}
		
		//set string and tree equivalent of schema in our structure
		//and return
		
		// res.setRepairedSchema(schema);
		// res.setRepairedSchemaTree(createTreeFromSchemaString(schema));
		res.setQuerySchema(schema);
		res.setQuerySchemaHead(predicate);
		// System.out.println("Schema head in setter " + res.getQuerySchemaHead()) ;
		
		return res;
	}
	
	// Create tree structure from schema string that has just
	// been created based on the query
	// Not a recursive parser: this assumes a flat schema structure.
	public NodeCHAIN createTreeFromSchemaString(String schemaStr){
		NodeCHAIN root;
		
		String[] schemaArr = schemaStr.split("[,)(]");
		
		//first element is predicate
		//therefore root of our tree
		root = new NodeCHAIN(schemaArr[0]);
		
		//then we need to add children to our root node
		for(int i = 1 ; i < schemaArr.length ; i++){
			root.addChild(new NodeCHAIN(schemaArr[i]));
		}
		
		return root;
	}
	
}
