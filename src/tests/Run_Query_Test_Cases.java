package tests;

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
import chain_source.Repair_Schema;
import chain_source.Run_Query;

/*
 * Responsible for testing Run_Query.java to ensure
 * that after creating a query, we are able to run it 
 * without any errors.
 * 
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Run_Query_Test_Cases {

	private Call_SPSM spsmCall;
	private Repair_Schema getRepairedSchema;
	private Create_Query createQuery;
	private Run_Query run;
	
	private ArrayList<Match_Struc> finalRes;
	private String target, source;
	
	//for writing results
	private static File testRes;
	private PrintWriter fOut;
	private static boolean alreadyWritten;
	
	@BeforeClass
	public static void beforeAll(){
		System.out.println("These tests are responsible for testing Run_Query.java to ensure that\n"
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
		spsmCall = new Call_SPSM();
		getRepairedSchema = new Repair_Schema();
		createQuery = new Create_Query();
		run = new Run_Query();
		
		try{
			fOut = new PrintWriter(new FileWriter(testRes,true));
			
			if(alreadyWritten==false){
				fOut.write("Testing Results for Run_Query_Test_Cases.java\n\n");
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
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/", 0);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.1.1 - sepa query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "sepa", "queryData/sepa/sepa_datafiles/"))==true){
				fOut.write("\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test12(){
		System.out.println("\nRunning test 6.1.2 - sepa query");
		
		source="water(timePeriod, geo, measure, resource)";
		target="water(timePeriod, geo, measure, resource)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/", 0);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.1.2 - sepa query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "sepa", "queryData/sepa/sepa_datafiles/"))==true){
				fOut.write("\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test13(){
		System.out.println("\nRunning test 6.1.3 - sepa query");
		
		source="waterBodyMeasures(timePeriod, geo, measure, resource)";
		target="waterBodyMeasures(timePeriod, geo, measure, resource)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/", 0);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.1.3 - sepa query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "sepa", "queryData/sepa/sepa_datafiles/"))==true){
				fOut.write("\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test14(){
		System.out.println("\nRunning test 6.1.4 - sepa query");
		
		source="waterBodyPressures(identifiedDate,waterBodyId,assessmentCategory,source)";
		target="waterBodyPressures(identifiedDate,waterBodyId,assessmentCategory,source)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/", 0);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.1.4 - sepa query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "sepa", "queryData/sepa/sepa_datafiles/"))==true){
				fOut.write("\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test15(){
		System.out.println("\nRunning test 6.1.5 - sepa query");
		
		source="waterBodyMeasures(waterBodyId,secondaryMeasure,dataSource)";
		target="waterBodyMeasures(waterBodyId,secondaryMeasure,dataSource)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/", 0);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.1.5 - sepa query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "sepa", "queryData/sepa/sepa_datafiles/"))==true){
				fOut.write("\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test16(){
		System.out.println("\nRunning test 6.1.6 - sepa query");
		
		source="surfaceWaterBodies(riverName,associatedGroundwaterId)";
		target="surfaceWaterBodies(riverName,associatedGroundwaterId)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/", 0);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.1.6 - sepa query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "sepa", "queryData/sepa/sepa_datafiles/"))==true){
				fOut.write("\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test17(){
		System.out.println("\nRunning test 6.1.7 - sepa query");
		
		source="bathingWaters(catchment, localAuthority, lat, long)";
		target="bathingWaters(catchment, localAuthority, lat, long)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/", 0);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.1.7 - sepa query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "sepa", "queryData/sepa/sepa_datafiles/"))==true){
				fOut.write("\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test18(){
		System.out.println("\nRunning test 6.1.8 - sepa query");
		
		source="surfaceWaterBodies(subBasinDistrict,riverName,altitudeTypology,associatedGroundwaterId)";
		target="surfaceWaterBodies(subBasinDistrict,riverName,altitudeTypology,associatedGroundwaterId)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/", 0);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.1.8 - sepa query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "sepa", "queryData/sepa/sepa_datafiles/"))==true){
				fOut.write("\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test19(){
		System.out.println("\nRunning test 6.1.9 - sepa query");
		
		source="bathingWaters(bathingWaterId)";
		target="bathingWaters(bathingWaterId)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/", 0);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.1.9 - sepa query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "sepa", "queryData/sepa/sepa_datafiles/"))==true){
				fOut.write("\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test110(){
		System.out.println("\nRunning test 6.1.10 - sepa query");
		
		source="waterBodyTemperatures(dataSource, identifiedDate, affectsGroundwater,waterBodyId)";
		target="waterBodyTemperatures(dataSource, identifiedDate, affectsGroundwater,waterBodyId)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/", 0);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.1.10 - sepa query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "sepa", "queryData/sepa/sepa_datafiles/"))==true){
				fOut.write("\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test21(){
		System.out.println("\nRunning test 6.2.1 - dbpedia query");
		
		source="City(country,populationTotal)";
		target="City(country,populationTotal)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "dbpedia", null, 20);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.2.1 - dbpedia query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "dbpedia", null))==true){
				fOut.write("\n\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\n\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\n\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test22(){
		System.out.println("\nRunning test 6.2.2 - dbpedia query");
		
		source="Country";
		target="Country";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "dbpedia", null, 20);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.2.2 - dbpedia query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "dbpedia", null))==true){
				fOut.write("\n\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\n\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\n\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test23(){
		System.out.println("\nRunning test 6.2.3 - dbpedia query");
		
		source="Astronaut(nationality)";
		target="Astronaut(nationality)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "dbpedia", null, 20);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.2.3 - dbpedia query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "dbpedia", null))==true){
				fOut.write("\n\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\n\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\n\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test24(){
		System.out.println("\nRunning test 6.2.4 - dbpedia query");
		
		source="Mountain(elevation)";
		target="Mountain(elevation)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "dbpedia", null, 20);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.2.4 - dbpedia query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "dbpedia", null))==true){
				fOut.write("\n\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\n\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\n\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test25(){
		System.out.println("\nRunning test 6.2.5 - dbpedia query");
		
		source="Person(occupation, birthPlace)";
		target="Person(occupation, birthPlace)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "dbpedia", null, 20);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.2.5 - dbpedia query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "dbpedia", null))==true){
				fOut.write("\n\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\n\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\n\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test26(){
		System.out.println("\nRunning test 6.2.6 - dbpedia query");
		
		source="Person(occupation, instrument)";
		target="Person(occupation, instrument)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "dbpedia", null, 20);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.2.6 - dbpedia query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "dbpedia", null))==true){
				fOut.write("\n\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\n\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\n\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test27(){
		System.out.println("\nRunning test 6.2.7 - dbpedia query");
		
		source="Cave(location)";
		target="Cave(location)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "dbpedia", null, 20);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.2.7 - dbpedia query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "dbpedia", null))==true){
				fOut.write("\n\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\n\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\n\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test28(){
		System.out.println("\nRunning test 6.2.8 - dbpedia query");
		
		source="FormulaOneRacer(races)";
		target="FormulaOneRacer(races)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "dbpedia", null, 20);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.2.8 - dbpedia query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "dbpedia", null))==true){
				fOut.write("\n\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\n\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\n\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}	
	}
	
	@Test
	public void test29(){
		System.out.println("\nRunning test 6.2.9 - dbpedia query");
		
		source="River(length)";
		target="River(length)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "dbpedia", null, 20);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.2.9 - dbpedia query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "dbpedia", null))==true){
				fOut.write("\n\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\n\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\n\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test210(){
		System.out.println("\nRunning test 6.2.10 - dbpedia query");
		
		source="Royalty(parent)";
		target="Royalty(parent)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "dbpedia", null, 20);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.2.10 - dbpedia query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "dbpedia", null))==true){
				fOut.write("\n\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\n\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\n\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test211(){
		System.out.println("\nRunning test 6.2.11 - dbpedia query");
		
		source="river(length)";
		target="river(length)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "dbpedia", null, 20);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.2.11 - dbpedia query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "dbpedia", null))==true){
				fOut.write("\n\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\n\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\n\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test212(){
		System.out.println("\nRunning test 6.2.12 - dbpedia query");
		
		source="Stream(length)";
		target="Stream(length)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "dbpedia", null, 20);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.2.12 - dbpedia query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "dbpedia", null))==true){
				fOut.write("\n\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\n\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\n\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test213(){
		System.out.println("\nRunning test 6.2.13 - dbpedia query");
		
		source="River(Mountain(elevation))";
		target="River(Mountain(elevation))";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "dbpedia", null, 20);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.2.13 - dbpedia query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "dbpedia", null))==true){
				fOut.write("\n\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\n\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\n\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test214(){
		System.out.println("\nRunning test 6.2.14 - dbpedia query");
		
		source="Brokentype(brokenproperty)";
		target="Brokentype(brokenproperty)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "dbpedia", null, 20);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.2.14 - dbpedia query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "dbpedia", null))==true){
				fOut.write("\n\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\n\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\n\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	@Test
	public void test215(){
		System.out.println("\nRunning test 6.2.15 - dbpedia query");
		
		source="River(assembly, manufacturer, place)";
		target="River(assembly, manufacturer, place)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "dbpedia", null, 20);
		Match_Struc current = finalRes.get(0);
		
		fOut.write("Test 6.2.15 - dbpedia query\n");
		fOut.write("Trying to run query: \n\n" + current.getQuery());
		
		try{
			if((run.runQuery(current, "dbpedia", null))==true){
				fOut.write("\n\nQuery HAS run successfully.\n\n");
			}else{
				fOut.write("\n\nQuery HAS run successfully but NO data has been returned.\n\n");
			}
		}catch(Exception e){
			fOut.write("\n\nQuery has NOT run successfully with error message,\n");
			fOut.write(e.toString()+"\n\n");
		}
	}
	
	
	@After
	public void cleanUp(){
		fOut.close();
	}

}
