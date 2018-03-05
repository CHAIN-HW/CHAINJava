package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import chain.core.MatchStruc;
import chain.sparql.SchemaFromQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/* Author Tanya Howden
 * Date September 2017
 * Modified
 */

/*
 * Responsible for testing SchemaFromQuery.java to ensure
 * that we can create a valid schema from a sepa or dbpedia
 * query.
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SchemaFromQueryTestCases {

	private SchemaFromQuery schemaCreator;
	private String query;
	private MatchStruc res;
	
	//for writing results
	private static File testRes;
	private PrintWriter fOut;
	private static boolean alreadyWritten;
	
	@BeforeClass
	public static void beforeAll(){
		System.out.println("These tests are responsible for testing SchemaFromQuery.java to ensure that\n"
				+"we can create a valid schema from a sepa or dbpedia query.");
		System.out.println("\nThe results from these tests can be found in outputs/testing/Schema_From_Query_Tests.txt\n");

		alreadyWritten = false;
		try{
			testRes = new File("outputs/testing/Schema_From_Query_Tests.txt");
			testRes.createNewFile();
			
			new PrintWriter("outputs/testing/Schema_From_Query_Tests.txt").close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Before
	public void setup(){
		schemaCreator = new SchemaFromQuery();
		query="";
		
		try{
			fOut = new PrintWriter(new FileWriter(testRes,true));
			
			if(alreadyWritten==false){
				fOut.write("Testing Results for SchemaFromQueryTestCases.java\n\n");
				alreadyWritten = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void test11(){
		System.out.println("\nRunning test 7.1.1 - sepa query");
		
		query="PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
				+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
				+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
				+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
				+ "SELECT *  \n"
				+ "FROM <queryData/sepa/sepa_datafiles/waterBodyPressures.n3>\n"
				+ "WHERE { ?id sepaw:dataSource ?dataSource;\n"
				+ "sepaw:identifiedDate  ?identifiedDate  ;\n"
				+ "sepaw:affectsGroundwater ?affectsGroundwater ;\n"
				+ "sepaw:waterBodyId ?waterBodyId .}"
				+ "\n\n";
		
		fOut.write("Test 7.1.1 - sepa query\n");
		fOut.write("Creating schema from query,\n\n"+query + "\n\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "sepa");
		
		fOut.write("Expected Result: waterBodyPressures(dataSource, identifiedDate, affectsGroundwater, waterBodyId)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");
		assertTrue(res.getQuerySchema().contains("waterBodyPressures(")) ;
		assertTrue(res.getQuerySchema().contains("identifiedDate")) ;
	}
	
	@Test
	public void test12(){
		System.out.println("\nRunning test 7.1.2 - sepa query");
		
		query=	"PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
				+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
				+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
				+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
				+ "SELECT *  \n"
				+ "FROM <queryData/sepa/sepa_datafiles/water.n3>\n"
				+ "WHERE { ?id sepaw:timePeriod ?timePeriod;\n"
				+ "geo:geo ?geo  ;\n"
				+ "sepaw:measure ?measure ;\n"
				+ "sepaw:resource ?resource .}"
				+ "\n";
		
		fOut.write("Test 7.1.2 - sepa query\n");
		fOut.write("Creating schema from query,\n\n"+query + "\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "sepa");
		
		fOut.write("Expected Result: water(timePeriod, geo, measure, resource)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");
		assertTrue(res.getQuerySchema().contains("water(")) ;
		assertTrue(res.getQuerySchema().contains("timePeriod")) ;
	}
	
	@Test
	public void test13(){
		System.out.println("\nRunning test 7.1.3 - sepa query");
		
		query=	"PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
		          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
		          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
		          + "SELECT *  \n"
		          + "FROM <queryData/sepa/sepa_datafiles/waterBodyMeasures.n3>\n"
		          + "WHERE { ?id sepaw:timePeriod ?timePeriod;\n"
		          + "geo:geo ?geo  ;\n"
		          + "sepaw:measure ?measure ;\n"
		          + "sepaw:resource ?resource .}"
		          + "\n";
		
		fOut.write("Test 7.1.3 - sepa query\n");
		fOut.write("Creating schema from query,\n\n"+query + "\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "sepa");
		
		fOut.write("Expected Result: waterBodyMeasures(timePeriod, geo, measure, resource)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");
		
		assertTrue(res.getQuerySchema().contains("waterBodyMeasures(")) ;
		assertTrue(res.getQuerySchema().contains("resource")) ;
	}
	
	@Test
	public void test14(){
		System.out.println("\nRunning test 7.1.4 - sepa query");
		
		query=	"PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          	+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          	+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          	+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          	+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          	+ "SELECT *  \n"
	          	+ "FROM <queryData/sepa/sepa_datafiles/waterBodyPressures.n3>\n"
	          	+ "WHERE { ?id sepaw:identifiedDate ?identifiedDate;\n"
	          	+ "sepaw:waterBodyId ?waterBodyId  ;\n"
	          	+ "sepaw:assessmentCategory ?assessmentCategory ;\n"
	          	+ "sepaw:source ?source .}"
	          	+ "\n";
		
		fOut.write("Test 7.1.4 - sepa query\n");
		fOut.write("Creating schema from query,\n\n"+query + "\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "sepa");
		
		fOut.write("Expected Result: waterBodyPressures(identifiedDate, waterBodyId, assessmentCategory, source)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");
		
		assertTrue(res.getQuerySchema().contains("waterBodyPressures(")) ;
		assertTrue(res.getQuerySchema().contains("waterBodyId")) ;
	}
	
	@Test
	public void test15(){
		System.out.println("\nRunning test 7.1.5 - sepa query");
		
		query=	"PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
		          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
		          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
		          + "SELECT *  \n"
		          + "FROM <queryData/sepa/sepa_datafiles/waterBodyMeasures.n3>\n"
		          + "WHERE { ?id sepaw:waterBodyId ?waterBodyId;\n"
		          + "sepaw:secondaryMeasure ?secondaryMeasure  ;\n"
		          + "sepaw:dataSource ?dataSource .}"
		          + "\n";
		
		fOut.write("Test 7.1.5 - sepa query\n");
		fOut.write("Creating schema from query,\n\n"+query + "\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "sepa");
		
		fOut.write("Expected Result: waterBodyMeasures(waterBodyId, secondaryMeasure, dataSource)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");
		
		assertTrue(res.getQuerySchema().contains("waterBodyMeasures(")) ;
		assertTrue(res.getQuerySchema().contains("dataSource")) ;
	}
	
	@Test
	public void test16(){
		System.out.println("\nRunning test 7.1.6 - sepa query");
		
		query=	"PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
		          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
		          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
		          + "SELECT *  \n"
		          + "FROM <queryData/sepa/sepa_datafiles/surfaceWaterBodies.n3>\n"
		          + "WHERE { ?id sepaw:riverName ?riverName;\n"
		          + "sepaw:associatedGroundwaterId ?associatedGroundwaterId .}"
		          + "\n";
		
		fOut.write("Test 7.1.6 - sepa query\n");
		fOut.write("Creating schema from query,\n\n"+query + "\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "sepa");
		
		fOut.write("Expected Result: surfaceWaterBodies(riverName, associatedGroundwaterId)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");
		
		assertTrue(res.getQuerySchema().contains("surfaceWaterBodies(")) ;
		assertTrue(res.getQuerySchema().contains("riverName")) ;
	}
	
	@Test
	public void test17(){
		System.out.println("\nRunning test 7.1.7 - sepa query");
		
		query=	"PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
		          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
		          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
		          + "SELECT *  \n"
		          + "FROM <queryData/sepa/sepa_datafiles/bathingWaters.n3>\n"
		          + "WHERE { ?id sepaidloc:catchment ?catchment;\n"
		          + "sepaidloc:localAuthority ?localAuthority  ;\n"
		          + "geo:lat ?lat ;\n"
		          + "geo:long ?long .}"
		          + "\n";
		
		fOut.write("Test 7.1.7 - sepa query\n");
		fOut.write("Creating schema from query,\n\n"+query + "\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "sepa");
		
		fOut.write("Expected Result: bathingWaters(catchment, localAuthority, lat, long)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");
		
		assertTrue(res.getQuerySchema().contains("bathingWaters(")) ;
		assertTrue(res.getQuerySchema().contains("catchment")) ;
	}
	
	@Test
	public void test18(){
		System.out.println("\nRunning test 7.1.8 - sepa query");
		
		query=	"PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
		          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
		          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
		          + "SELECT *  \n"
		          + "FROM <queryData/sepa/sepa_datafiles/surfaceWaterBodies.n3>\n"
		          + "WHERE { ?id sepaw:altitudeTypology ?altitudeTypology;\n"
		          + "sepaw:associatedGrounwaterId ?associatedGroundwaterId  ;\n"
		          + "sepaw:riverName ?riverName ;\n"
		          + "sepaw:subBasinDistrict ?subBasinDistrict .}"
		          + "\n";
		
		fOut.write("Test 7.1.8 - sepa query\n");
		fOut.write("Creating schema from query,\n\n"+query + "\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "sepa");
		
		fOut.write("Expected Result: surfaceWaterBodies(altitudeTypology, associatedGroundwaterId, riverName, subBasinDistrict)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");
		
		assertTrue(res.getQuerySchema().contains("surfaceWaterBodies(")) ;
		assertTrue(res.getQuerySchema().contains("altitudeTypology")) ;
	}
	
	@Test
	public void test19(){
		System.out.println("\nRunning test 7.1.9 - sepa query");
		
		query=	"PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
		          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
		          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
		          + "SELECT *  \n"
		          + "FROM <queryData/sepa/sepa_datafiles/bathingWaters.n3>\n"
		          + "WHERE { ?id sepaw:bathingWaterId ?bathingWaterId;\n"
		          + ".}"
		          + "\n";
		
		fOut.write("Test 7.1.9 - sepa query\n");
		fOut.write("Creating schema from query,\n"+query + "\n\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "sepa");
		
		fOut.write("Expected Result: bathingWaters(bathingWaterId)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");
		
		assertTrue(res.getQuerySchema().contains("bathingWaters(bathingWaterId)")) ;
	
	}
	
	@Test
	public void test110(){
		System.out.println("\nRunning test 7.1.10 - sepa query");
		
		query=	"PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
		          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
		          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "SELECT *  \n"
		          + "FROM <queryData/sepa/sepa_datafiles/bathingWaters.n3>\n"
		          + "WHERE { ?id sepaw:bathingWaterId ?bathingWaterId;\n"
		          + ".}"
		          + "\n";
		
		fOut.write("Test 7.1.10 - sepa query\n");
		fOut.write("Creating schema from query,\n"+query + "\n\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "sepa");
		
		fOut.write("Expected Result: null\n");
		
		assertTrue(res == null) ;
		
		if(res == null){
			fOut.write("Actual Result: " + res + "\n\n");
		}else{
			fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");
		}
	}
	
	@Test
	public void test21(){
		System.out.println("\nRunning test 7.2.1 - dbpedia query");
		
		query=	"PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
				+ "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
				+ "PREFIX  res: <http://dbpedia.org/resource/> \n"
				+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
				+ "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
				+ "SELECT DISTINCT *  \n"
				+ "WHERE { ?id rdf:type dbo:City ;\n"
				+ "dbo:country ?country ;\n"
				+ "dbo:populationTotal ?populationTotal .}\n"
				+ "LIMIT 20\n";
		
		fOut.write("Test 7.2.1 - dbpedia query\n");
		fOut.write("Creating schema from query,\n"+query + "\n\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "dbpedia");
		
		fOut.write("Expected Result: City(country,populationTotal)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");
	}
	
	@Test
	public void test22(){
		System.out.println("\nRunning test 7.2.2 - dbpedia query");
		
		query=	"PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
				+ "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
				+ "PREFIX  res: <http://dbpedia.org/resource/> \n"
				+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
				+ "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
				+ "SELECT DISTINCT *  \n"
				+ "WHERE { ?id rdf:type dbo:City .}\n"
				+ "LIMIT 20\n";
		
		fOut.write("Test 7.2.2 - dbpedia query\n");
		fOut.write("Creating schema from query,\n"+query + "\n\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "dbpedia");
		
		fOut.write("Expected Result: City()\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");
		
		assertTrue(res.getQuerySchema().contains("City()")) ;
		
	}
	
	@Test
	public void test23(){
		System.out.println("\nRunning test 7.2.3 - dbpedia query");
		
		query=	"PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:Astronaut ;\n"
		          + "dbo:nationality ?nationality ;\n"
		          + ".}\n"
		          + "LIMIT 20\n";
		
		fOut.write("Test 7.2.3 - dbpedia query\n");
		fOut.write("Creating schema from query,\n"+query + "\n\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "dbpedia");
		
		fOut.write("Expected Result: Astronaut(nationality)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");	
		
		assertTrue(res.getQuerySchema().contains("Astronaut(nationality)")) ;
	}
	
	@Test
	public void test24(){
		System.out.println("\nRunning test 7.2.4 - dbpedia query");
		
		query=	"PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:Mountain ;\n"
		          + "dbo:elevation ?elevation ;\n"
		          + ".}\n"
		          + "LIMIT 20\n";
		
		fOut.write("Test 7.2.4 - dbpedia query\n");
		fOut.write("Creating schema from query,\n"+query + "\n\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "dbpedia");
		
		fOut.write("Expected Result: Mountain(elevation)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");	
		
		assertTrue(res.getQuerySchema().contains("Mountain(elevation)")) ;
	}
	
	@Test
	public void test25(){
		System.out.println("\nRunning test 7.2.5 - dbpedia query");
		
		query=	"PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:Person ;\n"
		          + "dbo:occupation ?occupation ;\n"
		          + "dbo:birthPlace ?birthPlace .}\n"
		          + "LIMIT 20\n";
		
		fOut.write("Test 7.2.5 - dbpedia query\n");
		fOut.write("Creating schema from query,\n"+query + "\n\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "dbpedia");
		
		fOut.write("Expected Result: Person(occupation,birthPlace)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");	
		
		assertTrue(res.getQuerySchema().contains("Person(")) ;
		assertTrue(res.getQuerySchema().contains("birthPlace")) ;
		
	}
	
	@Test
	public void test26(){
		System.out.println("\nRunning test 7.2.6 - dbpedia query");
		
		query=	"PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:Person ;\n"
		          + "dbo:occupation ?occupation ;\n"
		          + "dbo:instrument ?instrument .}\n"
		          + "LIMIT 20\n";
		
		fOut.write("Test 7.2.6 - dbpedia query\n");
		fOut.write("Creating schema from query,\n"+query + "\n\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "dbpedia");
		
		fOut.write("Expected Result: Person(occupation,instrument)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");		
		
		assertTrue(res.getQuerySchema().contains("Person(")) ;
		assertTrue(res.getQuerySchema().contains("occupation")) ;
	}
	
	@Test
	public void test27(){
		System.out.println("\nRunning test 7.2.7 - dbpedia query");
		
		query=	"PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:Cave ;\n"
		          + "dbo:location ?location ;\n"
		          + ".}\n"
		          + "LIMIT 20\n";
		
		fOut.write("Test 7.2.7 - dbpedia query\n");
		fOut.write("Creating schema from query,\n"+query + "\n\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "dbpedia");
		
		fOut.write("Expected Result: Cave(location)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");	
		
		assertTrue(res.getQuerySchema().contains("Cave(location)")) ;
	}
	
	@Test
	public void test28(){
		System.out.println("\nRunning test 7.2.8 - dbpedia query");
		
		query=	"PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:FormulaOneRacer ;\n"
		          + "dbo:races ?races ;\n"
		          + ".}\n"
		          + "LIMIT 20\n";
		
		fOut.write("Test 7.2.8 - dbpedia query\n");
		fOut.write("Creating schema from query,\n"+query + "\n\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "dbpedia");
		
		fOut.write("Expected Result: FormulaOneRacer(races)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");	
		assertTrue(res.getQuerySchema().contains("FormulaOneRacer(races)")) ;
		
	}
	
	@Test
	public void test29(){
		System.out.println("\nRunning test 7.2.9 - dbpedia query");
		
		query=	"PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:River ;\n"
		          + "dbo:length ?length ;\n"
		          + ".}\n"
		          + "LIMIT 20\n";
		
		fOut.write("Test 7.2.9 - dbpedia query\n");
		fOut.write("Creating schema from query,\n"+query + "\n\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "dbpedia");
		
		fOut.write("Expected Result: River(length)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");	
		assertTrue(res.getQuerySchema().contains("River(length)")) ;
	}
	
	@Test
	public void test210(){
		System.out.println("\nRunning test 7.2.10 - dbpedia query");
		
		query=	"PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:Royalty ;\n"
		          + "dbo:parent ?parent ;\n"
		          + ".}\n"
		          + "LIMIT 20\n";
		
		fOut.write("Test 7.2.10 - dbpedia query\n");
		fOut.write("Creating schema from query,\n"+query + "\n\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "dbpedia");
		
		fOut.write("Expected Result: Royalty(parent)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");	
		assertTrue(res.getQuerySchema().contains("Royalty(parent)")) ;
	}
	
	@Test
	public void test211(){
		System.out.println("\nRunning test 7.2.11 - dbpedia query");
		
		query=	"";
		
		fOut.write("Test 7.2.11 - dbpedia query\n");
		fOut.write("Creating schema from query,\n"+query + "\n\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "dbpedia");
		
		fOut.write("Expected Result: \n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");	
		
		assertTrue(res.getQuerySchema().isEmpty()) ;
	}
	
	@Test
	public void test212(){
		System.out.println("\nRunning test 7.2.12 - dbpedia query");
		
		query=	"PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:River;\n"
		          + "dbo:Mountain ?Mountain ;\n"
		          + ".}\n"
		          + "LIMIT 20\n";
		
		fOut.write("Test 7.2.12 - dbpedia query\n");
		fOut.write("Creating schema from query,\n"+query + "\n\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "dbpedia");
		
		fOut.write("Expected Result: River(Mountain)\n");
		fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");			
	}
	
	@Test
	public void test213(){
		System.out.println("\nRunning test 7.2.13 - dbpedia query");
		
		query=	 "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:River;\n"
		          + "dbo:Mountain ?Mountain ;\n"
		          + ".}\n"
		          + "LIMIT 20\n";
		
		fOut.write("Test 7.2.13 - dbpedia query\n");
		fOut.write("Creating schema from query,\n"+query + "\n\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "dbpedia");
		
		fOut.write("Expected Result: null\n");
		
		assertTrue(res == null) ;
		
		if(res == null){
			fOut.write("Actual Result: " + res + "\n\n");
		}else{
			fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");
		}
	}
	
	@Test
	public void test2301(){
		System.out.println("\nRunning test 7.3.01 - data literal testing query");
		
		query=	"PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX dc:   <http://purl.org/dc/elements/1.1/> "
				+ "PREFIX :     <http://example/ns#> "
				+ "PREFIX xsd:    <http://www.w3.org/2001/XMLSchema#> "
				+ "SELECT ?book  "
				+ "WHERE { ?book rdf:type <http://purl.org/dc/elements/1.1/book> ;"
				+ "              rdf:subject    \"Childrens fiction\" ; "
				+ "	             dc:title       \"The Wind in the Willows\"@en ; "
				+ "              dc:author      \"Kenneth Graeme\" ; "
				+ "              dc:pages      320 ; "
				+ "              dc:category   25.6 ; "
				+ "              dc:date       \"2005-01-01T00:00:00Z\"^^xsd:dateTime "
				+ "}" ;
		
		
		fOut.write("Test 7.3.01 - data literal testing query\n");
		fOut.write("Creating schema from query,\n"+query + "\n\n");
		
		//get results
		res = schemaCreator.getSchemaFromQuery(query, "dbpedia");
		
		fOut.write("Expected Result: book(subject,title,author,pages,category,date)\n");
		
		if(res == null){
			fOut.write("Actual Result: " + res + "\n\n");
		}else{
			fOut.write("Actual Result: " + res.getQuerySchema() + "\n\n");
		}
		
		assertTrue(res.getQuerySchema().contains("book(")) ;
		assertTrue(res.getQuerySchema().contains("date")) ;
	}
	
	@After
	public void cleanUp(){
		fOut.close();
	}
}
