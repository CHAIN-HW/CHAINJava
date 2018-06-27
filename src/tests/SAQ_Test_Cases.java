package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import chain_source.Run_CHAIn;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

/* Author Diana Bental
 * Date 24 April 2018
 * Modified
 */

/*
 * 
 * Runs CHAIN with SAQ 2015 test data set - schemas and queries
 * Incomplete
 */
public class SAQ_Test_Cases {
	
	// SAQTest 1 country in which the Ganges start

	private static String q1_saq = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
	 "PREFIX saq: <http://saq/ontology/>  " + 
	 "SELECT DISTINCT ?uri  " + 
	 "WHERE {  " + 
	 "        ?uri rdf:type saq:Country .  " + 
	 "		saq:Ganges saq:startAt ?uri .  " + 
	 "}";

	private static String q1_res = "PREFIX dbo: <http://dbpedia.org/ontology/>  " + 
	 "PREFIX res: <http://dbpedia.org/resource/>  " + 
	 "SELECT DISTINCT ?uri  " + 
	 "WHERE {  " + 
	 "        res:Ganges dbo:sourceCountry ?uri . " + 
	 "}";

	String target1_res = "river(sourceCountry)";
	
	// SAQTest 2 John F. Kennedy's vice president

	private static String q2_saq = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
	 "PREFIX saq: <http://saq/ontology/>  " + 
	 "SELECT DISTINCT ?uri  " + 
	 "WHERE {  " + 
	 "        ?uri vicePresidentOf John_F._Kennedy .   " + 
	 "}";

	private static String q2_res = "PREFIX dbo: <http://dbpedia.org/ontology/>  " + 
	 "PREFIX res: <http://dbpedia.org/resource/>  " + 
	 "SELECT DISTINCT ?uri  " + 
	 "WHERE { res:John_F._Kennedy dbo:vicePresident ?uri .  " + 
	 "}";

	// SAQTest 3 Free University of Amsterdam number of students

	private static String q3_saq = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
	 "PREFIX saq: <http://saq/ontology/>  " + 
	 "SELECT DISTINCT ?num  " + 
	 "WHERE {  " + 
	 "      Free_University_of_Amsterdam numberOfStudents ?num .  " + 
	 "}";

	private static String q3_res = "PREFIX dbo: <http://dbpedia.org/ontology/>  " + 
	 "PREFIX res: <http://dbpedia.org/resource/>  " + 
	 "SELECT DISTINCT ?num  " + 
	 "WHERE {  " + 
	 "        res:VU_University_Amsterdam dbo:numberOfStudents ?num .  " + 
	 "}";

	// SAQTest 4 countries which are part of the Himalayan mountain system

	private static String q4_saq = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
	 "PREFIX saq: <http://saq/ontology/>  " + 
	 "SELECT DISTINCT ?uri  " + 
	 "WHERE {  " + 
	 "		?uri type Country . " + 
	 "        ?uri partOf Himalayan_mountain_system .  " + 
	 "}";

	private static String q4_res = "PREFIX dbp: <http://dbpedia.org/property/>  " + 
	 "PREFIX res: <http://dbpedia.org/resource/>  " + 
	 "SELECT DISTINCT ?uri  " + 
	 "WHERE {  " + 
	 "        res:Himalayas dbp:country ?uri .  " + 
	 "}";

	// SAQTest 5 Salt Lake City time zone

	private static String q5_saq = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
	 "PREFIX saq: <http://saq/ontology/>  " + 
	 "SELECT DISTINCT ?uri  " + 
	 "WHERE {  " + 
	 "       Salt_Lake_City timezone ?uri .  " + 
	 "}";

	private static String q5_res = "PREFIX res: <http://dbpedia.org/resource/>  " + 
	 "PREFIX dbp: <http://dbpedia.org/property/>  " + 
	 "SELECT DISTINCT ?uri  " + 
	 "WHERE {  " + 
	 "        res:Salt_Lake_City dbp:timezone ?uri .  " + 
	 "}";

	// SAQTest 6 U.S. states which are in the same time zone of Utah

	private static String q6_saq = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
	 "PREFIX saq: <http://saq/ontology/>  " + 
	 "SELECT DISTINCT ?uri  " + 
	 "WHERE {  " + 
	 "        ?uri type U.S._State .  " + 
	 "        ?uri timezone ?tz .  " + 
	 "		Utah timezone ?tz .  " + 
	 "		FILTER (?uri != Utah)  " + 
	 "}";

	private static String q6_res = "PREFIX res: <http://dbpedia.org/resource/>  " + 
	 "PREFIX dbp: <http://dbpedia.org/property/>  " + 
	 "PREFIX yago: <http://dbpedia.org/class/yago/>  " + 
	 "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  " + 
	 "SELECT DISTINCT ?uri  " + 
	 "WHERE {  " + 
	 "        res:Utah dbp:timezone ?x .   " + 
	 "        ?uri rdf:type yago:StatesOfTheUnitedStates .   " + 
	 "        ?uri dbp:timezone ?x .  " + 
	 "        FILTER (?uri != res:Utah)  " + 
	 "}";

	// SAQTest 7 capitals of all countries in Africa

	private static String q7_saq = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
	 "PREFIX saq: <http://saq/ontology/>  " + 
	 "SELECT DISTINCT ?capital  " + 
	 "WHERE {  " + 
	 "        ?country type Country . " + 
	 "		?country locatedIn Africa . " + 
	 "        ?country capital ?capital .  " + 
	 "}";

	private static String q7_res = "PREFIX dbo: <http://dbpedia.org/ontology/>  " + 
	 "PREFIX yago: <http://dbpedia.org/class/yago/>  " + 
	 "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  " + 
	 "SELECT DISTINCT ?uri  " + 
	 "WHERE {  " + 
	 "        ?states rdf:type yago:AfricanCountries .  " + 
	 "        ?states dbo:capital ?uri .  " + 
	 "}";

	// SAQTest 8 lakes in Denmark

	private static String q8_saq = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
	 "PREFIX saq: <http://saq/ontology/>  " + 
	 "SELECT DISTINCT ?uri  " + 
	 "WHERE {  " + 
	 "        ?uri locatedIn Denmark .  " + 
	 "        ?uri type Lake .  " + 
	 "}";

	private static String q8_res = "PREFIX dbo: <http://dbpedia.org/ontology/>  " + 
	 "PREFIX res: <http://dbpedia.org/resource/>  " + 
	 "PREFIX yago: <http://dbpedia.org/class/yago/>  " + 
	 "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  " + 
	 "SELECT DISTINCT ?uri  " + 
	 "WHERE {  " + 
	 "        { ?uri rdf:type dbo:Lake .   " + 
	 "          ?uri dbo:country res:Denmark . }  " + 
	 "        UNION  " + 
	 "        { ?uri rdf:type yago:LakesOfDenmark . }  " + 
	 "}";

	// SAQTest 9 number of missions of the Soyuz program

	private static String q9_saq = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
	 "PREFIX saq: <http://saq/ontology/>  " + 
	 "SELECT DISTINCT ?num  " + 
	 "WHERE {  " + 
	 "        Soyuz_program numberOfMissions ?num .  " + 
	 "}";

	private static String q9_res = "PREFIX dbp: <http://dbpedia.org/property/>  " + 
	 "PREFIX res: <http://dbpedia.org/resource/>  " + 
	 "SELECT COUNT(DISTINCT ?uri) " + 
	 "WHERE {  " + 
	 "        ?uri dbp:programme res:Soyuz_programme .  " + 
	 "}";

	// SAQTest 10 Was Aristotle influenced by Socrates?

	private static String q10_saq = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
	 "PREFIX saq: <http://saq/ontology/>  " + 
	 "ASK  " + 
	 "WHERE {  " + 
	 "        Aristotle influencedBy Socrates .  " + 
	 "}";

	private static String q10_res = "PREFIX dbo: <http://dbpedia.org/ontology/>  " + 
	 "PREFIX res: <http://dbpedia.org/resource/>  " + 
	 "ASK  " + 
	 "WHERE {  " + 
	 "        res:Socrates dbo:influenced res:Aristotle .  " + 
	 "}";

	// SAQTest 11 Danish movies

	private static String q11_saq = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
	 "PREFIX saq: <http://saq/ontology/>  " + 
	 "SELECT DISTINCT ?uri  " + 
	 "WHERE {  " + 
	 "        ?uri type DanishMovie .   " + 
	 "}";

	private static String q11_res = "PREFIX dbo: <http://dbpedia.org/ontology/>  " + 
	 "PREFIX res: <http://dbpedia.org/resource/>  " + 
	 "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  " + 
	 "SELECT DISTINCT ?uri  " + 
	 "WHERE {  " + 
	 "        ?uri rdf:type dbo:Film .   " + 
	 "        ?uri dbo:country res:Denmark .  " + 
	 "}";

	// SAQTest 12 launch pads operated by NASA

	private static String q12_saq = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
	 "PREFIX saq: <http://saq/ontology/>  " + 
	 "SELECT DISTINCT ?uri  " + 
	 "WHERE {  " + 
	 "		?uri type LaunchPad . " + 
	 "        ?uri operatedBy NASA .  " + 
	 "}";

	private static String q12_res = "PREFIX dbo: <http://dbpedia.org/ontology/>  " + 
	 "PREFIX res: <http://dbpedia.org/resource/>  " + 
	 "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  " + 
	 "SELECT DISTINCT ?uri  " + 
	 "WHERE {  " + 
	 "        ?uri rdf:type dbo:LaunchPad .   " + 
	 "        ?uri dbo:operator res:NASA .  " + 
	 "}";

	private static final int UNKNOWNSTATUS = 0 ;
	private static final int INITIALQUERYSUCCESS = 5 ;
	private static final int INVALIDQUERY = 6 ;
	private static final int SPSMFAILURE = 7 ;
	private static final int NOMATCHESFROMSPSM = 8 ;
	private static final int REPAIREDQUERYRUNERROR = 9 ;
	private static final int REPAIREDQUERYRESULTS = 10 ;
	private static final int REPAIREDQUERYNORESULTS = 11 ;
	private static final int DATAREPAIREDWITHRESULTS = 12 ;

	private String queryType;
	private Run_CHAIn chain = new Run_CHAIn();
	
	//for writing results
	private static File testRes;
	private PrintWriter fOut;
	private static boolean alreadyWritten;
	
	
	
	@BeforeClass
	public static void beforeAll(){
		System.out.println("These tests use Run_CHAIn.java on the  SAQ training set");
		System.out.println("\nThe results from these tests can be found in outputs/testing/SAQ_Training_Cases.txt\n");

		alreadyWritten = false;
		try{
			testRes = new File("outputs/testing/SAQ_Training_Cases.txt");
			testRes.createNewFile();
			
			new PrintWriter("outputs/testing/SAQ_Training_Cases.txt").close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Before
	public void setup(){
		queryType="";
		
		try{
			fOut = new PrintWriter(new FileWriter(testRes,true));
			
			if(alreadyWritten==false){
				fOut.write("Results for SAQ_Training_Cases.java\n\n");
				alreadyWritten = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	@Test
	public void test_1_a_res() {
	    runSAQ("Test 1_a_res", q1_res,  target1_res, INITIALQUERYSUCCESS);
	}

	@Test
	public void test_1_b_saq() {
	    runSAQ("Test 1_b_saq", q1_saq,  target1_res, NOMATCHESFROMSPSM);
	}

	public void runSAQ(String testID, String query, String targetSchemas, int expectedOutcome){
		
		System.out.println("\nRunning test " + testID);
		
		fOut.write("Test " + testID + "\n\n");
		fOut.write("Query " +  query + "\n\n");
		fOut.write("Schema: " + targetSchemas +"\n\n") ;
		
		System.out.println("Query " +  query + "\n");
		System.out.println("Schema: " + targetSchemas +"\n") ;
		
		
		queryType="dbpedia";
		
		String dataDir = null;
		String ontologyPath = "queryData/dbpedia/dbpedia_ontology.json";
		
		
//		int status = chain.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, 0.5, 5, fOut);
		int status = chain.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, 0, 5, fOut);
		
		System.out.println("Status: " +  status + "\n");
		assertTrue(status == expectedOutcome) ;
	}
	
	@After
	public void cleanUp(){
		fOut.close();
	}

}
