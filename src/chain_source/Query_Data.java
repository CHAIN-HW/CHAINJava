/* Author Diana Bental
 * Date December 2017
 * Modified
 */

package chain_source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.impl.LiteralLabel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.sparql.core.TriplePath;
import com.hp.hpl.jena.sparql.syntax.ElementPathBlock;
import com.hp.hpl.jena.sparql.syntax.ElementVisitorBase;
import com.hp.hpl.jena.sparql.syntax.ElementWalker;

// Build up the information about the query that will be needed to do
// predicate replacement into the query
// and data repair

public class Query_Data {

	public ArrayList<String> uriObjectValues; // e.g.
												// ["http://dbpedia.org/ontology/City",
												// .. ]
	public ArrayList<Node> literalObjectValues; // e.g. ["Bob", "cat", 320,
												// 25.6, 2005-01-01T00:00:00Z,
												// ...]
	public HashMap<String, String> prefixToURIMaps; // e.g.
													// {"rdf"="http://www.w3.org/1999/02/22-rdf-syntax-ns#",
													// ...}
	public HashMap<String, String> localNameToPrefixMaps; // e.g. {"date"="dc",
															// "dateTime"="xsd",
															// "pages"="dc",
															// ....}
	public HashMap<String, Node> localPropertyNameToLiteralObjectMaps; // {"date"="2005-01-01T00:00:00Z",
																		// pages="320",
																		// subject="Childrens
																		// fiction",
																		// ...}
	public HashMap<String, String> localPropertyNameToURIObjectMaps; // {"City"="http://dbpedia.org/resource/London",
																		// ...}
	public HashMap<String, String> resolvedURItoPrefixAndLocalNameMaps; // e.g.
																		// {"http://dbpedia.org/ontology/City"="dbo:City"}
	public String originalQuery;

	public Query_Data() {

		uriObjectValues = new ArrayList<String>();
		literalObjectValues = new ArrayList<Node>();
		prefixToURIMaps = new HashMap<String, String>();
		localNameToPrefixMaps = new HashMap<String, String>();
		localPropertyNameToLiteralObjectMaps = new HashMap<String, Node>();
		localPropertyNameToURIObjectMaps = new HashMap<String, String>();
		resolvedURItoPrefixAndLocalNameMaps = new HashMap<String, String>();
		originalQuery = "";

	}

	public Query_Data(String query) {

		uriObjectValues = new ArrayList<String>();
		literalObjectValues = new ArrayList<Node>();
		prefixToURIMaps = new HashMap<String, String>();
		localNameToPrefixMaps = new HashMap<String, String>();
		localPropertyNameToLiteralObjectMaps = new HashMap<String, Node>();
		localPropertyNameToURIObjectMaps = new HashMap<String, String>();
		resolvedURItoPrefixAndLocalNameMaps = new HashMap<String, String>();
		originalQuery = "";

		// Create the query in a standard format
		try {
			// System.out.println("\nQuery_Data.java: checkpoint 1");
			// System.out.println("\n" + query + "\n");
			Query q = QueryFactory.create(query);
			// System.out.println("\nQuery_Data.java: checkpoint 2");
			String originalQueryString = q.toString();
			// System.out.println("\nQuery_Data.java: Original query:\n" + originalQueryString);
			originalQuery = originalQueryString;

			findObjectValues(q);

			// System.out.println("Literal object values found: " +
			// literalObjectValues);
			// System.out.println("URI object values found: " +
			// uriObjectValues);
			// System.out.println("Property and URI values: " +
			// localPropertyNameToURIObjectMaps) ;
			// System.out.println("Property and Literal values: " +
			// localPropertyNameToLiteralObjectMaps) ;

			parsePrefixMapsFromQuery(q);
			// System.out.println("prefixToURIMaps: " + prefixToURIMaps) ;

			parseLocalNametoPrefixMapsFromQuery(q);
			// System.out.println("localNameToPrefixMaps: " +
			// localNameToPrefixMaps) ;
			// System.out.println("localPropertyNameToDataMaps: " +
			// localPropertyNameToDataMaps) ;

			makeObjUriToPrefixTable();
			// System.out.println("resolvedURItoPrefixAndLocalNameMaps: " +
			// resolvedURItoPrefixAndLocalNameMaps) ;
			// System.out.println("Query_Data:" + this);
		} catch (Exception e) {
			System.out.println("Query_Data.java: Failed to parse the query string.");

		}

	}

	// Create a table to map fully resolved URIS to Prefix:LocalName
	// resolvedURItoPrefixAndLocalNameMaps
	// {http://dbpedia.org/ontology/City=dbo:City,
	// http://dbpedia.org/resource/New_Jersey=res:New_Jersey}
	// This needs to be done after other data structures have been created
	private void makeObjUriToPrefixTable() {

		for (String localName : localNameToPrefixMaps.keySet()) {
			// System.out.println("localName "+ localName);
			String prefix = localNameToPrefixMaps.get(localName);
			// System.out.println(" prefix "+ prefix);
			String uri = prefixToURIMaps.get(prefix);
			// System.out.println(" uri "+ uri);
			String prefixedName = prefix + ":" + localName;
			String fullURI = uri + localName;

			if (uriObjectValues.contains(fullURI)) {
				resolvedURItoPrefixAndLocalNameMaps.put(fullURI, prefixedName);
			}
		}
	}

	// Parse a query to pick out all the objects in a ?s ?p ?o triple

	// Process literals, i.e. numbers or quoted strings
	// Create a list of these values; also build the list of mappings from
	// property to literal value
	// e.g. literalObjectValues ["Bob", "cat", 320, 25.6, 2005-01-01T00:00:00Z]
	// localPropertyNameToLiteralObjectMaps: {"date"="2005-01-01T00:00:00Z",
	// pages="320", subject="Childrens fiction"}
	//

	// Process URIs which are of the form prefix:localName or
	// <http://...localname>
	// and return them in <http://...localname> form (only)
	// Create a list of these values; also build the list of mappings from
	// property to value
	// e.g.
	// uriObjectValues [http://dbpedia.org/ontology/City,
	// http://dbpedia.org/resource/New_Jersey]
	// localPropertyNameToURIObjectMaps: {type=http://dbpedia.org/ontology/City,
	// isPartOf=http://dbpedia.org/resource/New_Jersey}

	private void findObjectValues(Query query) {

		// A list of all the object values
		literalObjectValues = new ArrayList<Node>();

		// This will walk through all parts of the query

		ElementWalker.walk(query.getQueryPattern(),
				// For each element...
				new ElementVisitorBase() {
					// ...when it's a block of triples...
					public void visit(ElementPathBlock el) {
						// ...go through all the triples...
						Iterator<TriplePath> triples = el.patternElts();
						while (triples.hasNext()) {

							// Get the next triple
							TriplePath triple = triples.next();
							// System.out.println(triple) ;

							// Split into subject, rdf-predicate and object
							Node sparqlSubject = triple.getSubject();
							Node sparqlPredicate = triple.getPredicate();
							Node sparqlObject = triple.getObject();

							// System.out.println(sparqlSubject) ;
							// System.out.println(sparqlPredicate) ;
							// System.out.println(sparqlObject) ;

							if (sparqlObject.isLiteral()) {
								LiteralLabel thing = sparqlObject.getLiteral();
								String lform = thing.getLexicalForm();
								// System.out.println("Sparql Object: " + sparqlObject);
								// sparqlObject.getLiteralLexicalForm() ;
								// System.out.println(thing);
								literalObjectValues.add(sparqlObject);
								if (sparqlPredicate.isURI()) {
									String localPredName = sparqlPredicate.getLocalName();
									localPropertyNameToLiteralObjectMaps.put(localPredName, sparqlObject);
								}
							}

							if (sparqlObject.isURI()) {
								String objectURIText = sparqlObject.getURI();
								// System.out.println(thing);
								uriObjectValues.add(objectURIText);
								if (sparqlPredicate.isURI()) {
									String localPredName = sparqlPredicate.getLocalName();
									localPropertyNameToURIObjectMaps.put(localPredName, objectURIText);
								}
							}

						}
					}
				});
	}

	// Create a hashmap of each local name linked to its prefix
	private void parseLocalNametoPrefixMapsFromQuery(Query q) {
		// Use Jena to standardise the query format so we can parse it
		String queryString = q.toString(); // and then back to a string

		// The single line
		// ?uri rdf:type dbo:City .
		// should give two pairs - type, rdf and City, dbo

		// Remove all quoted strings from the query so we don't get confused by
		// them
		// queryString.replaceAll("(?:([\"'`])[^\\1]*?\\1)\\s+|\r?\n", "");
		queryString = queryString.replaceAll("\".+?\"", "");
		queryString = queryString.replaceAll("\'.+?\'", "");
		// System.out.println("Query String without quoted text:");
		// System.out.println(queryString) ;

		// Split the query string into segments using whitespace (includes
		// newlines and tabs)
		String[] qStrings = queryString.split("\\s");

		// Look for word_123_etc:word_123_etc
		// Ignore all segments that don't contain this pattern.
		String pattern1 = "(\\w+):(\\w+)";
		Pattern r1 = Pattern.compile(pattern1);

		localNameToPrefixMaps = new HashMap<String, String>();

		for (String qs : qStrings) {
			Matcher m = r1.matcher(qs);
			if (m.find()) {
				String prefix = m.group(1);
				String tail = m.group(2);
				// System.out.println("Prefix--" + prefix + "--"+tail+"--");
				localNameToPrefixMaps.put(tail, prefix);
			}
		}
	}

	// Given a SPARQL query pick out from the heading the Prefix mappings
	// and return a hashmap prefix -> iri
	private void parsePrefixMapsFromQuery(Query q) {
		// Use Jena to standardise the query format so we can parse it
		String queryString = q.toString(); // and then back to a string

		// Split the string into lines
		String[] qStrings = queryString.split("\n");

		// The pattern - look for lines with PREFIX
		// Parse e.g. PREFIX dbo: <http://dbpedia.org/ontology/>
		// to return
		// dbo
		// http://dbpedia.org/ontology/
		// Ignore all lines that don't follow the pattern.
		String pattern1 = "PREFIX\\s+(\\S+):\\s*<(.*)>";
		Pattern r1 = Pattern.compile(pattern1);

		prefixToURIMaps = new HashMap<String, String>();

		for (String qs : qStrings) {
			Matcher m = r1.matcher(qs);
			// System.out.println(qs);
			if (m.find()) {
				String prefix = m.group(1);
				String iri = m.group(2);
				// System.out.println("Prefix--" + prefix + "--"+iri+"--");
				prefixToURIMaps.put(prefix, iri);
			}
		}
	}

	public String toString() {
		return "Query_Data: " + "\n    uriObjectValues: " + uriObjectValues + "\n    literalObjectValues: "
				+ literalObjectValues + "\n    prefixToURIMaps: " + prefixToURIMaps + "\n    localNameToPrefixMaps: "
				+ localNameToPrefixMaps + "\n    localPropertyNameToURIObjectMaps: " + localPropertyNameToURIObjectMaps
				+ "\n    localPropertyNameToLiteralObjectMaps: " + localPropertyNameToLiteralObjectMaps
				+ "\n    resolvedURItoPrefixAndLocalNameMaps: " + resolvedURItoPrefixAndLocalNameMaps;

	}

}
