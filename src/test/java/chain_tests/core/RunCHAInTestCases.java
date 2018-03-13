package chain_tests.core;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import chain.core.RunCHAIn;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/* Author Tanya Howden
 * Date September 2017
 * Modified
 */

/*
 * 
 * Responsible for testing RunCHAIn.java that
 * runs CHAIn from start to finish where we test
 * for different cases such as invalid queries, 
 * no results from SPSM and calling CHAIn with
 * more than one target schema for comparison.
 * 
 */
public class RunCHAInTestCases extends BaseTest {
	
	private static final int UNKNOWNSTATUS = 0 ;
	private static final int INITIALQUERYSUCCESS = 5 ;
	private static final int INVALIDQUERY = 6 ;
	private static final int SPSMFAILURE = 7 ;
	private static final int NOMATCHESFROMSPSM = 8 ;
	private static final int REPAIREDQUERYRUNERROR = 9 ;
	private static final int REPAIREDQUERYRESULTS = 10 ;
	private static final int REPAIREDQUERYNORESULTS = 11 ;
	private static final int DATAREPAIREDWITHRESULTS = 12 ;

	private String query, queryType, targetSchemas;
	private RunCHAIn chain = new RunCHAIn();
	
	//for writing results
	private static File testRes;
	private PrintWriter fOut;
	private static boolean alreadyWritten;
	
	@BeforeClass
	public static void beforeAll(){
		System.out.println("These tests are responsible for testing RunCHAIn.java to test running\n"
				+"CHAIn with different scenarios.");
		System.out.println("\nThe results from these tests can be found in outputs/testing/Run_CHAIn_Tests.txt\n");

		alreadyWritten = false;
		try{
			testRes = new File("outputs/testing/Run_CHAIn_Tests.txt");
			testRes.createNewFile();
			
			new PrintWriter("outputs/testing/Run_CHAIn_Tests.txt").close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Before
	public void setup(){
		query="";
		queryType="";
		targetSchemas="";
		
		try{
			fOut = new PrintWriter(new FileWriter(testRes,true));
			
			if(alreadyWritten==false){
				fOut.write("Testing Results for RunCHAInTestCases.java\n\n");
				alreadyWritten = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	@Test
	//basic sepa query that fails to return results
	//produces 1 new query that runs successfully
	public void test8_0_1(){
		System.out.println("\nRunning test 8.0.1\n");
		
		query="PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
				+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
				+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
				+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
				+ "SELECT *  \n"
				+ "FROM <queryData/sepa/sepa_datafiles/waterBodyPressurestest.n3>\n"
				+ "WHERE { ?id sepaw:dataSource ?dataSource;\n"
				+ "sepaw:identifiedDate  ?identifiedDate  ;\n"
				+ "sepaw:affectsGroundwater ?affectsGroundwater ;\n"
				+ "sepaw:waterBodyId ?waterBodyId .}"
				+ "\n\n";
		
		queryType="sepa";
		targetSchemas="waterBodyPressures(dataSource, identifiedDate, affectsGroundwater, waterBodyId)";
		String dataDir = "queryData/sepa/sepa_datafiles/";
		String ontologyPath = "queryData/sepa/sepa_ontology.json";
		
		fOut.write("\n\nTest 8.0.1\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		int status = chain.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, 0.5, 5, fOut);
		assertTrue(status == REPAIREDQUERYRESULTS); 
	}
	
	@Test
	//invalid query
	public void test8_0_2(){
		System.out.println("\nRunning test 8.0.2\n");
		
		query="PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
				+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
				+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
				+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
				+ "SELECT *  \n"
				+ "FROM <queryData/sepa/sepa_datafiles/water.n3>\n"
				+ "WHERE { ?id sepaw:timePeriod ?timePeriod;\n"
				+ "random:geo ?geo  ;\n"   // The invalid bit - an undefined prefix
				+ "sepaw:measure ?measure ;\n"
				+ "sepaw:resource ?resource .}"
				+ "\n\n";
		
		queryType="sepa";
		targetSchemas="water(timePeriod,geo,measure,resource)";
		String dataDir = "queryData/sepa/sepa_datafiles/";
		String ontologyPath = "queryData/sepa/sepa_ontology.json";
		
		fOut.write("\n\nTest 8.0.2\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		int status = chain.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, 0.5, 5, fOut);
		assertTrue(status == INVALIDQUERY); 
	}
	
	@Test
	//basic sepa query that returns no results
	//fixing results in 0 matches from spsm
	//so no new queries are created
	public void test8_0_3(){
		System.out.println("\nRunning test 8.0.3\n");
		
		query= "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
		          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
		          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
		          + "SELECT *  \n"
		          + "FROM <queryData/sepa/sepa_datafiles/surfaceWaterBodies.n3>\n"
		          + "WHERE { ?id sepaw:river ?river;\n"
		          + "sepaw:associatedGroundwaterId ?associatedGroundwaterId .}"
		          + "\n\n";
		
		queryType="sepa";
		targetSchemas="water(timePeriod,geo,measure,resource)";
		String dataDir = "queryData/sepa/sepa_datafiles/";
		String ontologyPath = "queryData/sepa/sepa_ontology.json";
		
		fOut.write("\n\nTest 8.0.3\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		int status = chain.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, 0.5, 5, fOut);
		assertTrue(status == NOMATCHESFROMSPSM) ;
	
	}

	@Test
	// Sepa query that should run first time - but doesn't because we aren't set up to run directly
	// from a local data file that's named in a query.
	public void test8_0_4(){
		System.out.println("\nRunning test 8.0.4\n");
		
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
				+ "LIMIT 10 \n\n";
		
		queryType="sepa";
		targetSchemas="waterBodyPressures(dataSource, identifiedDate, affectsGroundwater, waterBodyId)";
		String dataDir = "queryData/sepa/sepa_datafiles/";
		String ontologyPath = "queryData/sepa/sepa_ontology.json";
		
		fOut.write("\n\nTest 8.0.4\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		int status = chain.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, 0.5, 5, fOut);
		assertTrue(status == REPAIREDQUERYRESULTS); 
	}
	
	@Test
	// Sepa query with data in it
	public void test8_0_5(){
		System.out.println("\nRunning test 8.0.5\n");
		
		query="PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          	+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          	+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          	+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          	+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          	+ "SELECT *  \n"
	          	+ "FROM <queryData/sepa/sepa_datafiles/waterBodyPressures.n3>\n"
	          	+ "WHERE { ?id sepaw:identifiedDate \"2008-04-01\";\n"
	          	+ "sepaw:waterBodyId sepaidw:20304  ;\n"
	          	+ "sepaw:assessmentCategory ?assessmentCategory ;\n"
	          	+ "sepaw:source \"Lake\" .}" ;
		
		queryType="sepa";
		targetSchemas="waterBodyPressures(identifiedDate,waterBodyId,assessmentCategory,source)";
		String dataDir = "queryData/sepa/sepa_datafiles/";
		String ontologyPath = "queryData/sepa/sepa_ontology.json";
		
		fOut.write("\n\nTest 8.0.5\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		int status = chain.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, 0.5, 5, fOut);
		assertTrue(status == REPAIREDQUERYRESULTS); 
	}
	
	@Test
	// Sepa query with non-matching data in it - requires data repair
	public void test8_0_6(){
		System.out.println("\nRunning test 8.0.6\n");
		
		query="PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          	+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          	+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          	+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          	+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          	+ "SELECT *  \n"
	          	+ "FROM <queryData/sepa/sepa_datafiles/waterBodyPressures.n3>\n"
	          	+ "WHERE { ?id sepaw:identifiedDate \"2008-04-01\";\n"
	          	+ "sepaw:waterBodyId sepaidw:20308xxx  ;\n" // wont match
	          	+ "sepaw:assessmentCategory ?assessmentCategory ;\n"
	          	+ "sepaw:source \"Lake\" .}" ;
		
		queryType="sepa";
		targetSchemas="waterBodyPressures(identifiedDate,waterBodyId,assessmentCategory,source)";
		String dataDir = "queryData/sepa/sepa_datafiles/";
		String ontologyPath = "queryData/sepa/sepa_ontology.json";
		
		fOut.write("\n\nTest 8.0.6\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		int status = chain.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, 0.5, 5, fOut);
		assertTrue(status == DATAREPAIREDWITHRESULTS); 
	}
	
	
	@Test
	//basic dbpedia query that returns no results
	//repair work by chain_tests returns 1 new query that
	//runs successfully with results.
	public void test8_1_1(){
		System.out.println("\nRunning test 8.1.1\n");
		
		query= "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
				+ "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
				+ "PREFIX  res: <http://dbpedia.org/resource/> \n"
				+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
				+ "PREFIX yago: <hhtp://dbpedia.org/class/yago/> \n\n"
				+ "SELECT DISTINCT *  \n"
				+ "WHERE { ?id rdf:type dbo:City ;\n"
				+ "dbo:country ?country ;\n"
				+ "dbo:population ?population .}\n" // this term doesn't match anything
				+ "LIMIT 10\n\n";
		
		queryType="dbpedia";
		targetSchemas="City(country,populationTotal)";
		String dataDir = null;
		String ontologyPath = "queryData/dbpedia/dbpedia_ontology.json";
		
		fOut.write("\n\nTest 8.1.1\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		int status = chain.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, 0.5, 5, fOut);
		assertTrue(status == REPAIREDQUERYRESULTS) ;
	}
	
	@Test
	//basic dbpedia query that  already runs successfully and does not require repair.
	public void test8_1_2(){
		System.out.println("\nRunning test 8.1.2\n");
		
		query= "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yago/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:River ;\n"
		          + "dbo:length ?length ;\n"
		          + ".}\n"
		          + "LIMIT 10\n\n";
		
		queryType="dbpedia";
		targetSchemas="River(length)";
		String dataDir = null;
		String ontologyPath = "queryData/dbpedia/dbpedia_ontology.json";
		
		fOut.write("\n\nTest 8.1.2\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		int status = chain.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, 0.5, 5, fOut);
		assertTrue(status == INITIALQUERYSUCCESS) ;
	}
	
	@Test
	//Query doesn't run successfully first time - returns no results.
	//Repair work creates 2 new queries
	//Both run successfully(?)
	// Possibly the second should not be created as it uses a term that is not in the ontology
	public void test8_1_3(){
		System.out.println("\nRunning test 8.1.3\n");
		
		query= "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yago/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:River ;\n"
		          + "dbo:lengthTest ?lengthTest ;\n"
		          + ".}\n"
		          + "LIMIT 10\n\n";
		
		queryType="dbpedia";
		targetSchemas="River(length) ; River(size)";
		String dataDir = null;
		String ontologyPath = "queryData/dbpedia/dbpedia_ontology.json";
		
		fOut.write("Test 8.1.3\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		int status = chain.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, 0.5, 5, fOut);
		// System.out.println("Status: " + status);
		assertTrue(status == REPAIREDQUERYRESULTS) ;
	}
	
	@Test
	//query doesn't run successfully first time
	//repair work creates 2 new queries
	//both run successfully
	public void test8_1_4(){
		System.out.println("\nRunning test 8.1.4\n");
		
		query= "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yago/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:River ;\n"
		          + "dbo:lengthTest \"99300.0\"^^<http://www.w3.org/2001/XMLSchema#double> ;\n"
		          + ".}\n"
		          + "LIMIT 10\n\n";
		
		queryType="dbpedia";
		targetSchemas="River(length) ; River(size)";
		String dataDir = null;
		String ontologyPath = "queryData/dbpedia/dbpedia_ontology.json";
		
		fOut.write("Test 8.1.4\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		int status = chain.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, 0.5, 5, fOut);
		assertTrue(status == REPAIREDQUERYRESULTS) ;
	}
	
	@Test
	//query doesn't run successfully first time
	//repair work creates 2 new queries
	//Both run successfully
	public void test8_1_5(){
		System.out.println("\nRunning test 8.1.5\n");
		
		query= "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yago/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:River ;\n"
		          + "dbo:lengthTest 99300.0 ;\n"
		          + ".}\n"
		          + "LIMIT 10\n\n";
		
		queryType="dbpedia";
		targetSchemas="River(length) ; River(size)";
		String dataDir = null;
		String ontologyPath = "queryData/dbpedia/dbpedia_ontology.json";
		
		fOut.write("\n\nTest 8.1.5\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		int status = chain.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, 0.5, 5, fOut);
		assertTrue(status == REPAIREDQUERYRESULTS) ;
	}
	
	@Test
	//query doesn't run successfully first time
	//repair work creates 2 new queries
	//Only one returns data.
	public void test8_1_6(){
		System.out.println("\nRunning test 8.1.6\n");
		
		query= "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yago/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:River ;\n"
		          + "dbo:lengthTest \"99300.0\" ;\n"
		          + ".}\n"
		          + "LIMIT 10\n\n";
		
		queryType="dbpedia";
		targetSchemas="River(length) ; River(size)";
		String dataDir = null;
		String ontologyPath = "queryData/dbpedia/dbpedia_ontology.json";
		
		fOut.write("Test 8.1.6\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		int status = chain.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, 0.5, 5, fOut);
		assertTrue(status == REPAIREDQUERYRESULTS) ;
	}
	
	@Test
	//query doesn't run successfully first time
	//repair work creates 2 new queries
	//Both run successfully
	public void test8_1_7(){
		System.out.println("\nRunning test 8.1.7\n");
		
		query= "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yago/> \n\n"
		          + "PREFIX w3: <http://www.w3.org/2001/XMLSchema#> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:River ;\n"
		          + "dbo:lengthTest \"99300.0\"^^w3:double ;\n"
		          + ".}\n"
		          + "LIMIT 10\n\n";
		
		queryType="dbpedia";
		targetSchemas="River(length) ; River(size)";
		String dataDir = null;
		String ontologyPath = "queryData/dbpedia/dbpedia_ontology.json";
		
		fOut.write("\n\nTest 8.1.7\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		int status = chain.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, 0.5, 5, fOut);
		assertTrue(status == REPAIREDQUERYRESULTS) ;
	}
	
	@After
	public void cleanUp(){
		fOut.close();
	}

}
