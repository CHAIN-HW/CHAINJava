package tests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import chain.core.CallSPSM;
import chain.core.MatchStruc;
import chain.sparql.CreateQuery;
import chain.sparql.RunQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.hp.hpl.jena.query.ResultSet;

import chain.sparql.QueryData;
import chain.core.RepairSchema;

/* Author Tanya Howden
 * Author Diana Bental
 * Date September 2017
 * Modified December 2017
 * Additional tests
 */

/*
 * Responsible for testing RunQuery.java to ensure
 * that after creating a query, we are able to run it 
 * without any errors.
 * 
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RunQueryTestCases extends tests.BaseTest {
	
	private static final int EXCEPTION = 1 ;
	private static final int WITHRESULTS = 2 ;
	private static final int NORESULTS = 3 ;
	private static final int FAILURE = 4 ;
	
	private static String Q6_1_1 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
			+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
			+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
			+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
			+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
			+ "SELECT *  \n"
			+ "FROM <queryData/sepa/sepa_datafiles/waterBodyPressures.n3>\n"
			+ "WHERE {\n ?id sepaw:dataSource ?dataSource;\n"
			+ "sepaw:identifiedDate  ?identifiedDate  ;\n"
			+ "sepaw:affectsGroundwater ?affectsGroundwater ;\n"
			+ "sepaw:waterBodyId ?waterBodyId .}" ;

	private static String Q6_1_2 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
			+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
			+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
			+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
			+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
			+ "SELECT *  \n"
			+ "FROM <queryData/sepa/sepa_datafiles/water.n3>\n"
			+ "WHERE { ?id sepaw:timePeriod ?timePeriod;\n"
			+ "geo:geo ?geo  ;\n"
			+ "sepaw:measure ?measure ;\n"
			+ "sepaw:resource ?resource .}" ;
	
	private static String Q6_1_3 = 	 "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
     + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
     + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
     + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
     + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
     + "SELECT *  \n"
     + "FROM <queryData/sepa/sepa_datafiles/waterBodyMeasures.n3>\n"
     + "WHERE { ?id sepaw:timePeriod ?timePeriod;\n"
     + "geo:geo ?geo  ;\n"
     + "sepaw:measure ?measure ;\n"
     + "sepaw:resource ?resource .}" ;

	private static String Q6_1_4 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
          	+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
          	+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
          	+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
          	+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
          	+ "SELECT *  \n"
          	+ "FROM <queryData/sepa/sepa_datafiles/waterBodyPressures.n3>\n"
          	+ "WHERE { ?id sepaw:identifiedDate ?identifiedDate;\n"
          	+ "sepaw:waterBodyId ?waterBodyId  ;\n"
          	+ "sepaw:assessmentCategory ?assessmentCategory ;\n"
          	+ "sepaw:source ?source .}" ;
	
	private static String Q6_1_5 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          + "SELECT *  \n"
	          + "FROM <queryData/sepa/sepa_datafiles/waterBodyMeasures.n3>\n"
	          + "WHERE { ?id sepaw:waterBodyId ?waterBodyId;\n"
	          + "sepaw:secondaryMeasure ?secondaryMeasure  ;\n"
	          + "sepaw:dataSource ?dataSource .}" ;
	
	private static String Q6_1_6 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          + "SELECT *  \n"
	          + "FROM <queryData/sepa/sepa_datafiles/surfaceWaterBodies.n3>\n"
	          + "WHERE { ?id sepaw:riverName ?riverName;\n"
	          + "sepaw:associatedGroundwaterId ?associatedGroundwaterId .}" ;
	
	private static String Q6_1_7 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          + "SELECT *  \n"
	          + "FROM <queryData/sepa/sepa_datafiles/bathingWaters.n3>\n"
	          + "WHERE { ?id sepaloc:catchment ?catchment;\n"
	          + "sepaloc:localAuthority ?localAuthority  ;\n"
	          + "geo:lat ?lat ;\n"
	          + "geo:long ?long .}" ;
	
	private static String Q6_1_8 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
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
	
	private static String Q6_1_9 = 	"PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          + "SELECT *  \n"
	          + "FROM <queryData/sepa/sepa_datafiles/bathingWaters.n3>\n"
	          + "WHERE { ?id sepaw:bathingWaterId ?bathingWaterId;\n"
	          + ".}" ;
	
	private static String Q6_2_1 =	"PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
			+ "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
			+ "PREFIX  res: <http://dbpedia.org/resource/> \n"
			+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
			+ "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
			+ "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
			+ "SELECT DISTINCT *  \n"
			+ "WHERE { ?id rdf:type dbo:City ;\n"
			+ "dbo:country ?country ;\n"
			+ "dbo:populationTotal ?populationTotal .}\n"
			+ "LIMIT 20" ;
	
	private static String Q6_2_2 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
			+ "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
			+ "PREFIX  res: <http://dbpedia.org/resource/> \n"
			+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
			+ "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
			+ "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
			+ "SELECT DISTINCT *  \n"
			+ "WHERE { ?id rdf:type dbo:City .}\n"
			+ "LIMIT 20" ;
	
	private static String Q6_2_3 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type Astronaut ;\n"
	          + "dbo:nationality ?nationality ;\n"
	          + ".}\n"
	          + "LIMIT 20";
	
	private static String Q6_2_4 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:Mountain ;\n"
	          + "dbo:elevation ?elevation ;\n"
	          + ".}\n"
	          + "LIMIT 20" ;
	
	private static String Q6_2_5 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:Person ;\n"
	          + "dbo:occupation ?occupation ;\n"
	          + "dbo:birthPlace ?birthPlace .}\n"
	          + "LIMIT 20" ;
	
	private static String Q6_2_6 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:Person ;\n"
	          + "dbo:occupation ?occupation ;\n"
	          + "dbo:instrument ?instrument .}\n"
	          + "LIMIT 20" ;
	
	private static String Q6_2_7 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:Cave ;\n"
	          + "dbo:location ?location ;\n"
	          + ".}\n"
	          + "LIMIT 20" ;
	
	private static String Q6_2_8 =  "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:FormulaOneRacer ;\n"
	          + "dbo:races ?races ;\n"
	          + ".}\n"
	          + "LIMIT 20" ;
	
	private static String Q6_2_9 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:River ;\n"
	          + "dbo:length ?length ;\n"
	          + ".}\n"
	          + "LIMIT 20" ;
	
	private static String Q6_2_10 =  "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:Royalty ;\n"
	          + "dbo:parent ?parent ;\n"
	          + ".}\n"
	          + "LIMIT 20" ;
	
	private static String Q6_2_11 =  "" ;
	
	private static String Q6_3_1 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
          	+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
          	+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
          	+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
          	+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
          	+ "SELECT *  \n"
          	+ "FROM <queryData/sepa/sepa_datafiles/waterBodyPressures.n3>\n"
          	+ "WHERE { ?id sepaw:identifiedDate \"2009-09-03\";\n"
          	+ "sepaw:waterBodyId sepaidw:100053  ;\n"
          	+ "sepaw:assessmentCategory ?assessmentCategory ;\n"
          	+ "sepaw:source \"SEPA\" .}" ;
	
	private static String Q6_3_3 =  "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:FormulaOneRacer ;\n"
	          + "dbo:races \"202\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\n"
	          + ".}\n"
	          + "LIMIT 20" ;
	
	private static String Q6_3_4 =  "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:FormulaOneRacer ;\n"
	          + "dbo:races \"202\" ;\n"
	          + ".}\n"
	          + "LIMIT 20" ;
	
	
	private CallSPSM spsmCall;
	private RepairSchema getRepairedSchema;
	private CreateQuery createQuery;
	private RunQuery run;
	
	private ArrayList<MatchStruc> finalRes;
	private String target, source;
	
	//for writing results
	private static File testRes;
	private PrintWriter fOut;
	private static boolean alreadyWritten;
	
	@BeforeClass
	public static void beforeAll(){
		System.out.println("These tests are responsible for testing RunQuery.java to ensure that\n"
				+"after creating a query, we are able to run it without any errors.");
		System.out.println("\nThe results from these tests can be found in outputs/testing/Run_Queries_Test.txt\n");

		alreadyWritten = false;
		try{
			testRes = new File("outputs/testing/Run_Queries_Tests.txt");
			testRes.createNewFile();
			
			new PrintWriter("outputs/testing/Run_Queries_Tests.txt").close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	
	@Before
	public void setup(){
		spsmCall = new CallSPSM();
		getRepairedSchema = new RepairSchema();
		createQuery = new CreateQuery();
		run = new RunQuery();
		
		try{
			fOut = new PrintWriter(new FileWriter(testRes,true));
			
			if(alreadyWritten==false){
				fOut.write("Testing Results for RunQueryTestCases.java\n\n");
				alreadyWritten = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void test11(){
		System.out.println("\nRunning test 6.1.1 - sepa query");	
		source="waterBodyPressures(dataSource,identifiedDate,affectsGroundwater,waterBodyId)";
		target="waterBodyPressures(dataSource,identifiedDate,affectsGroundwater,waterBodyId)";
		testSepa("6.1.1", source, target, Q6_1_1, WITHRESULTS) ;
		
	}
	
	@Test
	public void test12(){
		System.out.println("\nRunning test 6.1.2 - sepa query");
		
		source="water(timePeriod, geo, measure, resource)";
		target="water(timePeriod, geo, measure, resource)";
		testSepa("6.1.2", source, target, Q6_1_2, FAILURE) ; //Failure due to no file

	}
	
	@Test
	public void test13(){
		System.out.println("\nRunning test 6.1.3 - sepa query");
		
		source="waterBodyMeasures(timePeriod, geo, measure, resource)";
		target="waterBodyMeasures(timePeriod, geo, measure, resource)";
		testSepa("6.1.3", source, target, Q6_1_3, NORESULTS) ;
		
	}
	
	@Test
	public void test14(){
		System.out.println("\nRunning test 6.1.4 - sepa query");
		
		source="waterBodyPressures(identifiedDate,waterBodyId,assessmentCategory,source)";
		target="waterBodyPressures(identifiedDate,waterBodyId,assessmentCategory,source)";
		testSepa("6.1.4", source, target, Q6_1_4, WITHRESULTS) ;
	}
	
	@Test
	public void test15(){
		System.out.println("\nRunning test 6.1.5 - sepa query");
		
		source="waterBodyMeasures(waterBodyId,secondaryMeasure,dataSource)";
		target="waterBodyMeasures(waterBodyId,secondaryMeasure,dataSource)";
		testSepa("6.1.5", source, target, Q6_1_5, WITHRESULTS) ;
	}
	
	@Test
	public void test16(){
		System.out.println("\nRunning test 6.1.6 - sepa query");
		
		source="surfaceWaterBodies(riverName,associatedGroundwaterId)";
		target="surfaceWaterBodies(riverName,associatedGroundwaterId)";
		testSepa("6.1.6", source, target, Q6_1_6, WITHRESULTS) ;
	}
	
	@Test
	public void test17(){
		System.out.println("\nRunning test 6.1.7 - sepa query");
		
		source="bathingWaters(catchment, localAuthority, lat, long)";
		target="bathingWaters(catchment, localAuthority, lat, long)";
		testSepa("6.1.7", source, target, Q6_1_7, NORESULTS) ;
	}
	
	@Test
	public void test18(){
		System.out.println("\nRunning test 6.1.8 - sepa query");
		
		source="surfaceWaterBodies(subBasinDistrict,riverName,altitudeTypology,associatedGroundwaterId)";
		target="surfaceWaterBodies(subBasinDistrict,riverName,altitudeTypology,associatedGroundwaterId)";
		testSepa("6.1.8", source, target, Q6_1_8, WITHRESULTS) ;
	}
	
	@Test
	public void test19(){
		System.out.println("\nRunning test 6.1.9 - sepa query");
		
		source="bathingWaters(bathingWaterId)";
		target="bathingWaters(bathingWaterId)";
		testSepa("6.1.9", source, target, Q6_1_9, WITHRESULTS) ;
	}
	
	@Test
	public void test110(){
		System.out.println("\nRunning test 6.1.10 - sepa query");
		
		source="waterBodyTemperatures(dataSource, identifiedDate, affectsGroundwater,waterBodyId)";
		target="waterBodyTemperatures(dataSource, identifiedDate, affectsGroundwater,waterBodyId)";
		testSepa("6.1.10", source, target, Q6_1_9, FAILURE) ;
	}
	
	@Test
	public void test21(){
		System.out.println("\nRunning test 6.2.1 - dbpedia query");		
		source="City(country,populationTotal)";
		target="City(country,populationTotal)";
		testDBP("6.2.1", source, target, Q6_2_1, WITHRESULTS) ;
	}
	
	@Test
	public void test22(){
		System.out.println("\nRunning test 6.2.2 - dbpedia query");
		
		source="Country";
		target="Country";		
		testDBP("6.2.2", source, target, Q6_2_2, WITHRESULTS) ; //Parsing error
	}
	
	@Test
	public void test23(){
		System.out.println("\nRunning test 6.2.3 - dbpedia query");		
		source="Astronaut(nationality)";
		target="Astronaut(nationality)";
		testDBP("6.2.3", source, target, Q6_2_3, WITHRESULTS) ;  //Parsing error - not clear why???
		
	}
	
	@Test
	public void test24(){
		System.out.println("\nRunning test 6.2.4 - dbpedia query");	
		source="Mountain(elevation)";
		target="Mountain(elevation)";	
		testDBP("6.2.4", source, target, Q6_2_4, WITHRESULTS) ;

	}
	
	@Test
	public void test25(){
		System.out.println("\nRunning test 6.2.5 - dbpedia query");		
		source="Person(occupation, birthPlace)";
		target="Person(occupation, birthPlace)";
		testDBP("6.2.5", source, target, Q6_2_5, WITHRESULTS) ;	
	}
	
	@Test
	public void test26(){
		System.out.println("\nRunning test 6.2.6 - dbpedia query");		
		source="Person(occupation, instrument)";
		target="Person(occupation, instrument)";		
		testDBP("6.2.6", source, target, Q6_2_6, WITHRESULTS) ;

	}
	
	@Test
	public void test27(){
		System.out.println("\nRunning test 6.2.7 - dbpedia query");		
		source="Cave(location)";
		target="Cave(location)";		
		testDBP("6.2.7", source, target, Q6_2_7, WITHRESULTS) ;
		
	}
	
	@Test
	public void test28(){
		System.out.println("\nRunning test 6.2.8 - dbpedia query");	
		source="FormulaOneRacer(races)";
		target="FormulaOneRacer(races)";	
		testDBP("6.2.8", source, target, Q6_2_8, WITHRESULTS) ;

	}
	
	@Test
	public void test29(){
		System.out.println("\nRunning test 6.2.9 - dbpedia query");		
		source="River(length)";
		target="River(length)";		
		testDBP("6.2.9", source, target, Q6_2_9, WITHRESULTS) ;

	}
	
	@Test
	public void test210(){
		System.out.println("\nRunning test 6.2.10 - dbpedia query");		
		source="Royalty(parent)";
		target="Royalty(parent)";		
		testDBP("6.2.10", source, target, Q6_2_10, WITHRESULTS) ;

	}
	
	@Test
	public void test211(){
		System.out.println("\nRunning test 6.2.11 - dbpedia query");		
		source="river(length)";
		target="river(length)";		
		testDBP("6.2.11", source, target, Q6_2_11, EXCEPTION) ; // expect parse error due to empty query
	}
	
	@Test
	public void test212(){
		System.out.println("\nRunning test 6.2.12 - dbpedia query");
		
		source="Stream(length)";
		target="Stream(length)";
		
		testDBP("6.2.12", source, target, Q6_2_11, EXCEPTION) ; // expect parse error due to empty query

	}
	
	@Test
	public void test213(){
		System.out.println("\nRunning test 6.2.13 - dbpedia query");
		
		source="River(Mountain(elevation))";
		target="River(Mountain(elevation))";
		
		testDBP("6.2.13", source, target, Q6_2_9, NORESULTS) ; // Expect no match for Mountain

	}
	
	@Test
	public void test214(){
		System.out.println("\nRunning test 6.2.14 - dbpedia query");	
		source="Brokentype(brokenproperty)";
		target="Brokentype(brokenproperty)";		
		testDBP("6.2.14", source, target, Q6_2_11, EXCEPTION) ; // Empty query 

	}
	
	@Test
	public void test215(){
		System.out.println("\nRunning test 6.2.15 - dbpedia query");		
		source="River(assembly, manufacturer, place)";
		target="River(assembly, manufacturer, place)";		
		testDBP("6.2.15", source, target, Q6_2_9, NORESULTS) ;
		
	}
	
	@Test
	public void test303(){
		System.out.println("\nRunning test 6.3.3 - dbpedia query");		
		source="FormulaOneRacer(races)";
		target="FormulaOneRacer(races)";		
		testDBP("6.3.3", source, target, Q6_3_3, WITHRESULTS) ; // Query with data
	}
	
	@Test
	public void test304(){
		System.out.println("\nRunning test 6.3.4 - dbpedia query");		
		source="FormulaOneRacer(races)";
		target="FormulaOneRacer(races)";		
		testDBP("6.3.4", source, target, Q6_3_4, NORESULTS) ; // Query with data
	}
	

	private void testSepa(String testID, String source, String target, String query, int ExpectedResult) {
		finalRes = new ArrayList<MatchStruc>();
		
		//call appropriate methods
		finalRes=spsmCall.callSPSM(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.repairSchemas(finalRes);
		}
		
		QueryData queryData = new QueryData(query) ;
		System.out.println(queryData) ;
		finalRes = createQuery.createQueries(finalRes, queryData, "sepa","queryData/sepa/sepa_datafiles/", "queryData/sepa/sepa_ontology.json",0);
		// Run the first query only
		MatchStruc current = finalRes.get(0);
		
		fOut.write("Test " + testID + " - sepa query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			ResultSet result = run.runQuery(current, "sepa", "queryData/sepa/sepa_datafiles/") ;
			if((result != null) && result.hasNext() ){
				// System.out.println("Result: match");
				fOut.write("\nQuery HAS run successfully.\n\n");
				assertTrue(ExpectedResult == WITHRESULTS) ;
			}else if(result != null) {
				fOut.write("\nQuery HAS run successfully but NO data has been returned.\n\n");
				assertTrue(ExpectedResult == NORESULTS) ;
				// System.out.println("Result: empty");
			} else {
				fOut.write("\n\nQuery has NOT run successfully") ;
				// System.out.println("Result: failure");
				assertTrue(ExpectedResult == FAILURE) ;
				}
		}catch(Exception e){
			fOut.write("\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
			// System.out.println("Result: exception");
			assertTrue(ExpectedResult == EXCEPTION) ;
		}
	}
	
	private void testDBP(String testID, String source, String target, String query, int ExpectedResult) {
		finalRes = new ArrayList<MatchStruc>();
		finalRes=spsmCall.callSPSM(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.repairSchemas(finalRes);
		}
		
		QueryData queryData = new QueryData(query) ;
		System.out.println(queryData) ;
		finalRes = createQuery.createQueries(finalRes, queryData, "dbpedia", null, "queryData/dbpedia/dbpedia_ontology.json",20);
		// Run the first query only
		MatchStruc current = finalRes.get(0);
		
		fOut.write("Test " + testID + " - dbpedia query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			ResultSet result = run.runQuery(current, "dbpedia", null) ;
			if((result != null) && result.hasNext() ){
				fOut.write("\n\nQuery HAS run successfully.\n\n");
				assertTrue(ExpectedResult == WITHRESULTS) ;
			}else if(result != null) {
				fOut.write("\n\nQuery HAS run successfully but NO data has been returned.\n\n");
				assertTrue(ExpectedResult == NORESULTS) ;
			} else {
				fOut.write("\n\nQuery has NOT run successfully") ;
				assertTrue(ExpectedResult == FAILURE) ;
				}
		}catch(Exception e){
			fOut.write("\n\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
			assertTrue(ExpectedResult == EXCEPTION) ;
		}
		
		
	}
	
	
	@After
	public void cleanUp(){
		fOut.close();
	}

}
