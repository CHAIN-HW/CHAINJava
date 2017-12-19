package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import chain_source.Call_SPSM;
import chain_source.Create_Query;
import chain_source.Match_Struc;
import chain_source.Query_Data;
import chain_source.Repair_Schema;

/* Author Tanya Howden
 * Date September 2017
 * Modified
 */

/*
 * Responsible for testing Create_Query.java to ensure
 * that after calling SPSM and repairing the target schema
 * we can create the correct sepa or dbpedia query
 * 
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Create_Query_Test_Cases {
	
	private static String Q5_1_1 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
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
	
	private static String T5_1_1 = "sepaw:affectsGroundwater ?affectsGroundwater" ;
	
	private static String Q5_1_2 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
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
	
	private static String T5_1_2 = "geo:geo ?geo" ;
	
	private static String Q5_1_3 = 	 "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
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
	
	private static String T5_1_3 = "sepaw:measure ?measure" ;

	private static String Q5_1_4 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
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
	
	private static String T5_1_4 = "sepaw:identifiedDate ?identifiedDate" ;
	
	private static String Q5_1_5 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          + "SELECT *  \n"
	          + "FROM <queryData/sepa/sepa_datafiles/waterBodyMeasures.n3>\n"
	          + "WHERE { ?id sepaw:waterBodyId ?waterBodyId;\n"
	          + "sepaw:secondaryMeasure ?secondaryMeasure  ;\n"
	          + "sepaw:dataSource ?dataSource .}" ;
	
	private static String T5_1_5 = "sepaw:dataSource ?dataSource" ;
	
	private static String Q5_1_6 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          + "SELECT *  \n"
	          + "FROM <queryData/sepa/sepa_datafiles/surfaceWaterBodies.n3>\n"
	          + "WHERE { ?id sepaw:riverName ?riverName;\n"
	          + "sepaw:associatedGroundwaterId ?associatedGroundwaterId .}" ;
	
	private static String T5_1_6 = "FROM <queryData/sepa/sepa_datafiles/surfaceWaterBodies.n3>" ;
	
	private static String Q5_1_7 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
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
	
	private static String T5_1_7 = "?id" ;
	
	
	private static String Q5_1_8 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
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
	
	private static String T5_1_8 = "sepaw:riverName ?riverName" ;
	
	private static String Q5_1_9 = 	"PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          + "SELECT *  \n"
	          + "FROM <queryData/sepa/sepa_datafiles/bathingWaters.n3>\n"
	          + "WHERE { ?id sepaw:bathingWaterId ?bathingWaterId;\n"
	          + ".}" ;
	
	private static String T5_1_9 = "sepaw:bathingWaterId ?bathingWaterId" ;
	
	private static String Q5_2_1 =	"PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
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
	
	private static String T5_2_1 = "dbo:populationTotal ?populationTotal" ;
	
	private static String Q5_2_2 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
			+ "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
			+ "PREFIX  res: <http://dbpedia.org/resource/> \n"
			+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
			+ "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
			+ "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
			+ "SELECT DISTINCT *  \n"
			+ "WHERE { ?id rdf:type dbo:City .}\n"
			+ "LIMIT 20" ;
	
	private static String T5_2_2 = "?id rdf:type dbo:City" ;
	
	private static String Q5_2_3 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
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
	private static String T5_2_3 = "dbo:nationality ?nationality" ;
	
	private static String Q5_2_4 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
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
	private static String T5_2_4 = "SELECT DISTINCT *" ;
	
	private static String Q5_2_5 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
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
	
	private static String T5_2_5 = "dbp: <http://dbpedia.org/property/>" ;
	
	private static String Q5_2_6 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
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
	private static String T5_2_6 = "dbo:instrument ?instrument" ;
	
	
	private static String Q5_2_7 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
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
	private static String T5_2_7 = "rdf:type dbo:Cave" ;
	
	private static String Q5_2_8 =  "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
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
	private static String T5_2_8 = "dbo:races ?races" ;
	
	private static String Q5_2_9 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
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
	private static String T5_2_9 = "dbo:length ?length" ;
	
	private static String Q5_2_10 =  "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
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
	private static String T5_2_10 = "dbo:parent ?parent" ;
	
	private static String Q5_2_11 = "" ;
	private static String T5_2_11 = "" ;
			
	private static String Q5_2_12 = "" ;
	private static String T5_2_12 = "" ;
	
	private static String Q5_2_13 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:River;\n"
	          + "dbo:Mountain ?Mountain ;\n"
	          + ".}\n"
	          + "LIMIT 20" ;
	private static String T5_2_13 = "dbo:Mountain ?Mountain" ;
	

	private static String Q5_3_1 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
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
	private static String T5_3_1 = "sepaw:identifiedDate \"2009-09-03\"" ;
	
	
	
	private static String Q5_3_3 =  "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
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
	private static String T5_3_3 = "dbo:races \"202\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger>" ;
	
	
	
	private Call_SPSM spsmCall;
	private Repair_Schema getRepairedSchema;
	private Create_Query createQuery;
	
	private ArrayList<Match_Struc> finalRes;
	private String target, source;
	
	//for writing results
	private static File testRes;
	private PrintWriter fOut;
	private static boolean alreadyWritten;
	
	@BeforeClass
	public static void beforeAll(){
		System.out.println("These tests are responsible for testing Create_Query.java to ensure that\n"
				+"after calling SPSM and repairing the target schema, we can create the correct sepa or dbpedia query.");
		System.out.println("\nThe results from these tests can be found in outputs/testing/Create_Queries_Test.txt\n");

		alreadyWritten = false;
		try{
			testRes = new File("outputs/testing/Create_Queries_Tests.txt");
			testRes.createNewFile();
			
			new PrintWriter("outputs/testing/Create_Queries_Tests.txt").close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	
	@Before
	public void setup(){
		spsmCall = new Call_SPSM();
		getRepairedSchema = new Repair_Schema();
		createQuery = new Create_Query();
		
		try{
			fOut = new PrintWriter(new FileWriter(testRes,true));
			
			if(alreadyWritten==false){
				fOut.write("Testing Results for Create_Query_Test_Cases.java\n\n");
				alreadyWritten = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	@Test //Sepa
	public void test11(){
		System.out.println("\nRunning test 5.1.1 - sepa query");
		
		source="waterBodyPressures(dataSource,identifiedDate,affectsGroundwater,waterBodyId)";
		target="waterBodyPressures(dataSource,identifiedDate,affectsGroundwater,waterBodyId)";
		testSepa("5.1.1", source,  target, Q5_1_1, T5_1_1) ;
	}
	
	@Test //Sepa
	public void test12(){
		System.out.println("\nRunning test 5.1.2 - sepa query");
		
		source="water(timePeriod, geo, measure, resource)";
		target="water(timePeriod, geo, measure, resource)";
		testSepa("5.1.2", source,  target, Q5_1_2, T5_1_2) ;
	}

	@Test //Sepa
	public void test13(){
		System.out.println("\nRunning test 5.1.3 - sepa query");
		
	  source="waterBodyMeasures(timePeriod, geo, measure, resource)";
	  target="waterBodyMeasures(timePeriod, geo, measure, resource)";
	  testSepa("5.1.3", source,  target, Q5_1_3, T5_1_3) ;
	}
	
	@Test //Sepa
	public void test14(){
		System.out.println("\nRunning test 5.1.4 - sepa query");
		
		source="waterBodyPressures(identifiedDate,waterBodyId,assessmentCategory,source)";
		target="waterBodyPressures(identifiedDate,waterBodyId,assessmentCategory,source)";
		testSepa("5.1.4", source,  target, Q5_1_4, T5_1_4) ;
	}
	
	@Test //Sepa
	public void test15(){
	  System.out.println("\nRunning test 5.1.5 - sepa query");
		
	  source="waterBodyMeasures(waterBodyId,secondaryMeasure,dataSource)";
	  target="waterBodyMeasures(waterBodyId,secondaryMeasure,dataSource)";
	  testSepa("5.1.5", source,  target, Q5_1_5, T5_1_5) ;
	}
	
	@Test //Sepa
	public void test16(){
	  System.out.println("\nRunning test 5.1.6 - sepa query");
		
	  source="surfaceWaterBodies(riverName,associatedGroundwaterId)";
	  target="surfaceWaterBodies(riverName,associatedGroundwaterId)";
	  testSepa("5.1.6", source,  target, Q5_1_6, T5_1_6) ;
	}
	
	@Test //Sepa
	public void test17(){
	  System.out.println("\nRunning test 5.1.7 - sepa query");
		
	  source="bathingWaters(catchment, localAuthority, lat, long)";
	  target="bathingWaters(catchment, localAuthority, lat, long)";
	  testSepa("5.1.7", source,  target, Q5_1_7, T5_1_7) ;
	}
	
	@Test //Sepa
	public void test18(){
	  System.out.println("\nRunning test 5.1.8 - sepa query");	
	  source="surfaceWaterBodies(subBasinDistrict,riverName,altitudeTypology,associatedGroundwaterId)";
	  target="surfaceWaterBodies(subBasinDistrict,riverName,altitudeTypology,associatedGroundwaterId)";
	  testSepa("5.1.8", source,  target, Q5_1_8, T5_1_8) ;
	}
	
	@Test //Sepa
	public void test19(){
	  System.out.println("\nRunning test 5.1.9 - sepa query");		
	  source="bathingWaters(bathingWaterId)";
	  target="bathingWaters(bathingWaterId)";
	  testSepa("5.1.9", source,  target, Q5_1_9, T5_1_9) ;
	}
	
	@Test //Dbpedia
	public void test21(){
		System.out.println("\nRunning test 5.2.1 - dbpedia query");
		
		source="City(country,populationTotal)";
		target="City(country,populationTotal)";
		testDBP("5.2.1", source,  target, Q5_2_1, T5_2_1) ;
	}
	
	@Test //Dbpedia
	public void test22(){
		System.out.println("\nRunning test 5.2.2 - dbpedia query");
		
		source="City";
		target="City";
		testDBP("5.2.2", source,  target, Q5_2_2, T5_2_2) ;
	}
	
	@Test //Dbpedia
	public void test23(){
	  System.out.println("\nRunning test 5.2.3 - dbpedia query");
		
	  source="Astronaut(nationality)";
	  target="Astronaut(nationality)";
	  testDBP("5.2.3", source,  target, Q5_2_3, T5_2_3) ;
	}
	
	@Test //Dbpedia
	public void test24(){
	  System.out.println("\nRunning test 5.2.4 - dbpedia query");
		
	  source="Mountain(elevation)";
	  target="Mountain(elevation)";
	  testDBP("5.2.4", source,  target, Q5_2_4, T5_2_4) ;
	  
	}
	
	@Test //Dbpedia
	public void test25(){
	  System.out.println("\nRunning test 5.2.5 - dbpedia query");	
		
	  source="Person(occupation, birthPlace)";
	  target="Person(occupation, birthPlace)";
	  testDBP("5.2.5", source,  target, Q5_2_5, T5_2_5) ;
	}
	
	@Test //Dbpedia
	public void test26(){
	  System.out.println("\nRunning test 5.2.6 - dbpedia query");
	  
	  source="Person(occupation, instrument)";
	  target="Person(occupation, instrument)";
	  testDBP("5.2.6", source,  target, Q5_2_6, T5_2_6) ;
	  }
	
	@Test //Dbpedia
	public void test27(){
	  System.out.println("\nRunning test 5.2.7 - dbpedia query");
		
	  source="Cave(location)";
	  target="Cave(location)";
	  testDBP("5.2.7", source,  target, Q5_2_7, T5_2_7) ;
	  }
	
	@Test //Dbpedia
	public void test28(){
	  System.out.println("\nRunning test 5.2.8 - dbpedia query");
		
	  source="FormulaOneRacer(races)";
	  target="FormulaOneRacer(races)";
	  testDBP("5.2.8", source,  target, Q5_2_8, T5_2_8) ;
	}
	
	@Test //Dbpedia
	public void test29(){
	  System.out.println("\nRunning test 5.2.9 - dbpedia query");	
		
	  source="River(length)";
	  target="River(length)";
	  testDBP("5.2.9", source,  target, Q5_2_9, T5_2_9) ;
	}
	
	@Test //Dbpedia
	public void test210(){
	  System.out.println("\nRunning test 5.2.10 - dbpedia query");
		
	  source="Royalty(parent)";
	  target="Royalty(parent)";
	  testDBP("5.2.10", source,  target, Q5_2_10, T5_2_10) ;
	}
	
	@Test //Dbpedia
	public void test211(){
	  System.out.println("\nRunning test 5.2.11 - dbpedia query");
		
	  source="river(length)";
	  target="river(length)";
	  testDBP("5.2.11", source,  target, Q5_2_11, T5_2_11) ;
	  }
	
	@Test //Dbpedia
	public void test212(){
	  System.out.println("\nRunning test 5.2.12 - dbpedia query");
		
	  source="Stream(length)";
	  target="Stream(length)";
	  testDBP("5.2.12", source,  target, Q5_2_12, T5_2_12) ;
	}
	
	@Test //Dbpedia
	public void test213(){
	  System.out.println("\nRunning test 5.2.13 - dbpedia query");
		
	  source="River(Mountain(elevation))";
	  target="River(Mountain(elevation))";
	  testDBP("5.2.13", source,  target, Q5_2_13, T5_2_13) ;
	}
	
	@Test //Sepa
	public void test301(){
		System.out.println("\nRunning test 5.3.1 - sepa query");
		
		source="waterBodyPressures(source,identifiedDate,assessmentCategory,waterBodyId)";
		target="waterBodyPressures(source,identifiedDate,assessmentCategory,waterBodyId)";
		testSepa("5.3.1", source,  target, Q5_3_1, T5_3_1) ;
	}
	
	@Test //Sepa
	public void test302(){
		System.out.println("\nRunning test 5.3.2 - sepa query");
		// The query and target schemas are slightly different; and there is an additional
		// property in the schema that was not in the original query
		
		source="waterBodyPressures(source,identifiedDate,assessmentCategory,affectsGroundwater,waterBodyId)";
		target="waterBodyPressures(dataSource,identifiedDate,assessmentCategory,affectsGroundwater,waterBodyId)";	  
		testSepa("5.3.2", source,  target, Q5_3_1, T5_3_1) ;
		

	}
	
	@Test //Dbpedia
	public void test303(){
	  System.out.println("\nRunning test 5.3.3 - dbpedia query");
	  source="FormulaOneRacer(races)";
	  target="FormulaOneRacer(races)";	  
	  testDBP("5.3.3", source,  target, Q5_3_3, T5_3_3) ;
	  
	}
	
	private void testSepa(String testID, String source, String target, String query, String testStr) {
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.callSPSM(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.repairSchemas(finalRes);
		}
		
		
		Query_Data queryData = new Query_Data(query) ;
		
		finalRes = createQuery.createQueries(finalRes, queryData, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0);
		
		fOut.write("Test " +  testID + " - sepa query\n");
		
		
		
		if(finalRes!=null){
			if(finalRes.size() == 0){	
				//then we have no results so end test
			
				fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
				fOut.write("Empty results returned. \n\n");
				fail() ; // We expect all tests to return more than zero matches
			}else{
				Match_Struc current = finalRes.get(0);
			
				fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
				fOut.write("Expected Result:\n\n" + query 
						+ "\n\n");
				
				fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
				assertTrue(current.getQuery().contains(testStr)) ;
			}
		}else{
			fOut.write("Null Results! \n\n");
			fail() ; // We expect all tests to return something
		}
		
	}


	
	private void testDBP(String testID, String source, String target, String query, String testStr) {
		  finalRes = new ArrayList<Match_Struc>();
		  
		  //call appropriate methods
		  finalRes=spsmCall.callSPSM(finalRes, source, target);
		  
		  if(finalRes!=null && finalRes.size()!=0){
		    finalRes = getRepairedSchema.repairSchemas(finalRes);
		  }
		  
		  Query_Data queryData = new Query_Data(query) ;
		  finalRes = createQuery.createQueries(finalRes, queryData, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20);
		  
		  fOut.write("Test " + testID + " - dbpedia query\n");
		  
		  if(finalRes!=null){
		    if(finalRes.size() == 0){
		      //then we have no results so end test
		    	
		      fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
		      fOut.write("Empty results returned. \n\n");
		      fail() ; // We expect all tests to return more than zero matches
		    }else{
		      Match_Struc current = finalRes.get(0);
		      	
		      fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
		      fOut.write("Expected Result:\n\n" + query
		        
		         
		          + "\n\n");
		      
		      fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
		      assertTrue(current.getQuery().contains(testStr)) ;
		    }
		  }else{
		    fOut.write("Null Results! \n\n");
		    fail() ; // We expect all tests to return something
		  }
	}
	
	@After
	public void cleanUp(){
		fOut.close();
	}
}
