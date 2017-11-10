package tests;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chain_source.Run_CHAIn;

/* Author Tanya Howden
 * Date September 2017
 * Modified
 */

/*
 * 
 * Responsible for testing Run_CHAIn.java that
 * runs CHAIn from start to finish where we test
 * for different cases such as invalid queries, 
 * no results from SPSM and calling CHAIn with
 * more than one target schema for comparison.
 * 
 */
public class Run_CHAIn_Test_Cases {

	private String query, queryType, targetSchemas;
	private Run_CHAIn chain = new Run_CHAIn();
	
	//for writing results
	private static File testRes;
	private PrintWriter fOut;
	private static boolean alreadyWritten;
	
	@BeforeClass
	public static void beforeAll(){
		System.out.println("These tests are responsible for testing Run_CHAIn.java to test running\n"
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
				fOut.write("Testing Results for Run_CHAIn_Test_Cases.java\n\n");
				alreadyWritten = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	@Test
	//basic sepa query that fails to return results
	//produces 1 new query that runs successfully
	public void test1(){
		System.out.println("\nRunning test 8.1\n");
		
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
		
		fOut.write("Test 8.1\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		chain.startCHAIn(query, queryType, targetSchemas, 10, 0.5, 5, fOut);
	}
	
	@Test
	//invalid query
	public void test2(){
		System.out.println("\nRunning test 8.2\n");
		
		query="PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
				+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
				+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
				+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
				+ "SELECT *  \n"
				+ "FROM <queryData/sepa/sepa_datafiles/water.n3>\n"
				+ "WHERE { ?id sepaw:timePeriod ?timePeriod;\n"
				+ "random:geo ?geo  ;\n"
				+ "sepaw:measure ?measure ;\n"
				+ "sepaw:resource ?resource .}"
				+ "\n\n";
		
		queryType="sepa";
		targetSchemas="water(timePeriod,geo,measure,resource)";
		
		fOut.write("Test 8.2\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		chain.startCHAIn(query, queryType, targetSchemas, 10, 0.5, 5, fOut);
	}
	
	@Test
	//basic sepa query that returns no results
	//fixing results in 0 matches from spsm
	//so no new queries are created
	public void test3(){
		System.out.println("\nRunning test 8.3\n");
		
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
		
		fOut.write("Test 8.3\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		chain.startCHAIn(query, queryType, targetSchemas, 10, 0.5, 5, fOut);
	}
	
	@Test
	//basic dbpedia query that returns no results
	//repair work by chain returns 1 new query that 
	//runs successfully
	public void test4(){
		System.out.println("\nRunning test 8.4\n");
		
		query= "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
				+ "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
				+ "PREFIX  res: <http://dbpedia.org/resource/> \n"
				+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
				+ "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
				+ "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
				+ "SELECT DISTINCT *  \n"
				+ "WHERE { ?id rdf:type dbo:City ;\n"
				+ "dbo:country ?country ;\n"
				+ "dbo:population ?population .}\n"
				+ "LIMIT 10\n\n";
		
		queryType="dbpedia";
		targetSchemas="City(country,populationTotal)";
		
		fOut.write("Test 8.4\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		chain.startCHAIn(query, queryType, targetSchemas, 10, 0.5, 5, fOut);
	}
	
	@Test
	//basic dbpedia query that already runs successfully
	public void test5(){
		System.out.println("\nRunning test 8.5\n");
		
		query= "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:River ;\n"
		          + "dbo:length ?length ;\n"
		          + ".}\n"
		          + "LIMIT 10\n\n";
		
		queryType="dbpedia";
		targetSchemas="River(length)";
		
		fOut.write("Test 8.5\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		chain.startCHAIn(query, queryType, targetSchemas, 10, 0.5, 5, fOut);
	}
	
	@Test
	//query doesn't run successfully first time
	//repair work creates 2 new queries
	//only one of these new queries runs successfully
	public void test6(){
		System.out.println("\nRunning test 8.6\n");
		
		query= "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
		          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
		          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
		          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
		          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
		          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
		          + "SELECT DISTINCT *  \n"
		          + "WHERE { ?id rdf:type dbo:River ;\n"
		          + "dbo:lengthTest ?lengthTest ;\n"
		          + ".}\n"
		          + "LIMIT 10\n\n";
		
		queryType="dbpedia";
		targetSchemas="River(length) ; River(size)";
		
		fOut.write("Test 8.6\n");
		fOut.write("Running CHAIn with initial query,\n\n"+query);
		
		chain.startCHAIn(query, queryType, targetSchemas, 10, 0.5, 5, fOut);
	}
	
	
	@After
	public void cleanUp(){
		fOut.close();
	}

}
