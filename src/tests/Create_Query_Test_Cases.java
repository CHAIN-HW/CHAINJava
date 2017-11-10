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
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/", 0);
		
		fOut.write("Test 5.1.1 - sepa query\n");
		
		if(finalRes!=null){
			if(finalRes.size() == 0){	
				//then we have no results so end test
				fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
				fOut.write("Empty results returned. \n\n");
			}else{
				Match_Struc current = finalRes.get(0);
					
				fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
				fOut.write("Expected Result:\n\n" + 
					
						"PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
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
						+ "\n\n");
				
				fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
			}
		}else{
			fOut.write("Null Results! \n\n");
		}
	}
	
	@Test //Sepa
	public void test12(){
		System.out.println("\nRunning test 5.1.2 - sepa query");
		
		source="water(timePeriod, geo, measure, resource)";
		target="water(timePeriod, geo, measure, resource)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/",0);
		
		fOut.write("Test 5.1.2 - sepa query\n");
		
		if(finalRes!=null){
			if(finalRes.size() == 0){	
				//then we have no results so end test
				fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
				fOut.write("Empty results returned. \n\n");
			}else{
				Match_Struc current = finalRes.get(0);
					
				fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
				fOut.write("Expected Result:\n\n" + 
					
						"PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
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
						+ "\n\n");
				
				fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
			}
		}else{
			fOut.write("Null Results! \n\n");
		}
	}

	@Test //Sepa
	public void test13(){
		System.out.println("\nRunning test 5.1.3 - sepa query");
		
	  source="waterBodyMeasures(timePeriod, geo, measure, resource)";
	  target="waterBodyMeasures(timePeriod, geo, measure, resource)";
	  finalRes = new ArrayList<Match_Struc>();
	  
	  //call appropriate methods
	  finalRes=spsmCall.getSchemas(finalRes, source, target);
	  
	  if(finalRes!=null && finalRes.size()!=0){
	    finalRes = getRepairedSchema.prepare(finalRes);
	  }
	  
	  finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/", 0);
	  
	  fOut.write("Test 5.1.3 - sepa query\n");
	  
	  if(finalRes!=null){
	    if(finalRes.size() == 0){	
	      //then we have no results so end test
	      fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
	      fOut.write("Empty results returned. \n\n");
	    }else{
	      Match_Struc current = finalRes.get(0);
	        
	      fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
	      fOut.write("Expected Result:\n\n" + 
	        
	          "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
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
	          + "\n\n");
	      
	      fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
	    }
	  }else{
	    fOut.write("Null Results! \n\n");
	  }
	}
	
	@Test //Sepa
	public void test14(){
		System.out.println("\nRunning test 5.1.4 - sepa query");
		
		source="waterBodyPressures(identifiedDate,waterBodyId,assessmentCategory,source)";
		target="waterBodyPressures(identifiedDate,waterBodyId,assessmentCategory,source)";
		finalRes = new ArrayList<Match_Struc>();
	  
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
	  
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
	  
		finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/", 0);
	  
		fOut.write("Test 5.1.4 - sepa query\n");
	  
		if(finalRes!=null){
			if(finalRes.size() == 0){	
				//then we have no results so end test
				fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
				fOut.write("Empty results returned. \n\n");
			}else{
				Match_Struc current = finalRes.get(0);
	        
				fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
				fOut.write("Expected Result:\n\n" + 
	        
	          	"PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
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
	          	+ "\n\n");
	      
				fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
			}
		}else{
			fOut.write("Null Results! \n\n");
		}
	}
	
	@Test //Sepa
	public void test15(){
	  System.out.println("\nRunning test 5.1.5 - sepa query");
		
	  source="waterBodyMeasures(waterBodyId,secondaryMeasure,dataSource)";
	  target="waterBodyMeasures(waterBodyId,secondaryMeasure,dataSource)";
	  finalRes = new ArrayList<Match_Struc>();
	  
	  //call appropriate methods
	  finalRes=spsmCall.getSchemas(finalRes, source, target);
	  
	  if(finalRes!=null && finalRes.size()!=0){
	    finalRes = getRepairedSchema.prepare(finalRes);
	  }
	  
	  finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/", 0);
	  
	  fOut.write("Test 5.1.5 - sepa query\n");
	  
	  if(finalRes!=null){
	    if(finalRes.size() == 0){	
	      //then we have no results so end test
	      fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
	      fOut.write("Empty results returned. \n\n");
	    }else{
	      Match_Struc current = finalRes.get(0);
	        
	      fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
	      fOut.write("Expected Result:\n\n" + 
	        
	          "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          + "SELECT *  \n"
	          + "FROM <queryData/sepa/sepa_datafiles/waterBodyMeasures.n3>\n"
	          + "WHERE { ?id sepaw:waterBodyId ?waterBodyId;\n"
	          + "sepaw:secondaryMeasure ?secondaryMeasure  ;\n"
	          + "sepaw:dataSource ?dataSource .}"
	          + "\n\n");
	      
	      fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
	    }
	  }else{
	    fOut.write("Null Results! \n\n");
	  }
	}
	
	@Test //Sepa
	public void test16(){
	  System.out.println("\nRunning test 5.1.6 - sepa query");
		
	  source="surfaceWaterBodies(riverName,associatedGroundwaterId)";
	  target="surfaceWaterBodies(riverName,associatedGroundwaterId)";
	  finalRes = new ArrayList<Match_Struc>();
	  
	  //call appropriate methods
	  finalRes=spsmCall.getSchemas(finalRes, source, target);
	  
	  if(finalRes!=null && finalRes.size()!=0){
	    finalRes = getRepairedSchema.prepare(finalRes);
	  }
	  
	  finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/", 0);
	  
	  fOut.write("Test 5.1.6 - sepa query\n");
	  
	  if(finalRes!=null){
	    if(finalRes.size() == 0){	
	      //then we have no results so end test
	      fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
	      fOut.write("Empty results returned. \n\n");
	    }else{
	      Match_Struc current = finalRes.get(0);
	        
	      fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
	      fOut.write("Expected Result:\n\n" + 
	        
	          "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          + "SELECT *  \n"
	          + "FROM <queryData/sepa/sepa_datafiles/surfaceWaterBodies.n3>\n"
	          + "WHERE { ?id sepaw:riverName ?riverName;\n"
	          + "sepaw:associatedGroundwaterId ?associatedGroundwaterId .}"
	          + "\n\n");
	      
	      fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
	    }
	  }else{
	    fOut.write("Null Results! \n\n");
	  }
	}
	
	@Test //Sepa
	public void test17(){
	  System.out.println("\nRunning test 5.1.7 - sepa query");
		
	  source="bathingWaters(catchment, localAuthority, lat, long)";
	  target="bathingWaters(catchment, localAuthority, lat, long)";
	  finalRes = new ArrayList<Match_Struc>();
	  
	  //call appropriate methods
	  finalRes=spsmCall.getSchemas(finalRes, source, target);
	  
	  if(finalRes!=null && finalRes.size()!=0){
	    finalRes = getRepairedSchema.prepare(finalRes);
	  }
	  
	  finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/", 0);
	  
	  fOut.write("Test 5.1.7 - sepa query\n");
	  
	  if(finalRes!=null){
	    if(finalRes.size() == 0){	
	      //then we have no results so end test
	      fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
	      fOut.write("Empty results returned. \n\n");
	    }else{
	      Match_Struc current = finalRes.get(0);
	        
	      fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
	      fOut.write("Expected Result:\n\n" + 
	        
	          "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          + "SELECT *  \n"
	          + "FROM <queryData/sepa/sepa_datafiles/bathingWaters.n3>\n"
	          + "WHERE { ?id sepaloc:catchment ?catchment;\n"
	          + "sepaloc:localAuthority ?localAuthority  ;\n"
	          + "geo:lat ?lat ;\n"
	          + "geo:long ?long .}"
	          + "\n\n");
	      
	      fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
	    }
	  }else{
	    fOut.write("Null Results! \n\n");
	  }
	}
	
	@Test //Sepa
	public void test18(){
	  System.out.println("\nRunning test 5.1.8 - sepa query");
		
	  source="surfaceWaterBodies(subBasinDistrict,riverName,altitudeTypology,associatedGroundwaterId)";
	  target="surfaceWaterBodies(subBasinDistrict,riverName,altitudeTypology,associatedGroundwaterId)";
	  finalRes = new ArrayList<Match_Struc>();
	  
	  //call appropriate methods
	  finalRes=spsmCall.getSchemas(finalRes, source, target);
	  
	  if(finalRes!=null && finalRes.size()!=0){
	    finalRes = getRepairedSchema.prepare(finalRes);
	  }
	  
	  finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/",0);
	  
	  fOut.write("Test 5.1.8 - sepa query\n");
	  
	  if(finalRes!=null){
	    if(finalRes.size() == 0){	
	      //then we have no results so end test
	      fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
	      fOut.write("Empty results returned. \n\n");
	    }else{
	      Match_Struc current = finalRes.get(0);
	        
	      fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
	      fOut.write("Expected Result:\n\n" + 
	        
	          "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
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
	          + "\n\n");
	      
	      fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
	    }
	  }else{
	    fOut.write("Null Results! \n\n");
	  }
	}
	
	@Test //Sepa
	public void test19(){
	  System.out.println("\nRunning test 5.1.9 - sepa query");
		
	  source="bathingWaters(bathingWaterId)";
	  target="bathingWaters(bathingWaterId)";
	  finalRes = new ArrayList<Match_Struc>();
	  
	  //call appropriate methods
	  finalRes=spsmCall.getSchemas(finalRes, source, target);
	  
	  if(finalRes!=null && finalRes.size()!=0){
	    finalRes = getRepairedSchema.prepare(finalRes);
	  }
	  
	  finalRes = createQuery.createQueryPrep(finalRes, "sepa","queryData/sepa/sepa_datafiles/", 0);
	  
	  fOut.write("Test 5.1.9 - sepa query\n");
	  
	  if(finalRes!=null){
	    if(finalRes.size() == 0){	
	      //then we have no results so end test
	      fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
	      fOut.write("Empty results returned. \n\n");
	    }else{
	      Match_Struc current = finalRes.get(0);
	        
	      fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
	      fOut.write("Expected Result:\n\n" + 
	        
	          "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          + "SELECT *  \n"
	          + "FROM <queryData/sepa/sepa_datafiles/bathingWaters.n3>\n"
	          + "WHERE { ?id sepaw:bathingWaterId ?bathingWaterId;\n"
	          + ".}"
	          + "\n\n");
	      
	      fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
	    }
	  }else{
	    fOut.write("Null Results! \n\n");
	  }
	}
	
	@Test //Dbpedia
	public void test21(){
		System.out.println("\nRunning test 5.2.1 - dbpedia query");
		
		source="City(country,populationTotal)";
		target="City(country,populationTotal)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "dbpedia",null,20);
		
		fOut.write("Test 5.2.1 - dbpedia query\n");
		
		if(finalRes!=null){
			if(finalRes.size() == 0){
				//then we have no results so end test
				fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
				fOut.write("Empty results returned. \n\n");
			}else{
				Match_Struc current = finalRes.get(0);
					
				fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
				fOut.write("Expected Result:\n\n" + 
					
						"PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
						+ "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
						+ "PREFIX  res: <http://dbpedia.org/resource/> \n"
						+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
						+ "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
						+ "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
						+ "SELECT DISTINCT *  \n"
						+ "WHERE { ?id rdf:type dbo:City ;\n"
						+ "dbo:country ?country ;\n"
						+ "dbo:populationTotal ?populationTotal .}\n"
						+ "LIMIT 20\n\n");
				
				fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
			}
		}else{
			fOut.write("Null Results! \n\n");
		}
	}
	
	@Test //Dbpedia
	public void test22(){
		System.out.println("\nRunning test 5.2.2 - dbpedia query");
		
		source="Country";
		target="Country";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		finalRes = createQuery.createQueryPrep(finalRes, "dbpedia",null,20);
		
		fOut.write("Test 5.2.2 - dbpedia query\n");
		
		if(finalRes!=null){
			if(finalRes.size() == 0){
				//then we have no results so end test
				fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
				fOut.write("Empty results returned. \n\n");
			}else{
				Match_Struc current = finalRes.get(0);
					
				fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
				fOut.write("Expected Result:\n\n" + 
					
						"PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
						+ "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
						+ "PREFIX  res: <http://dbpedia.org/resource/> \n"
						+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
						+ "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
						+ "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
						+ "SELECT DISTINCT *  \n"
						+ "WHERE { ?id rdf:type dbo:City .}\n"
						+ "LIMIT 20\n\n");
				
				fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
			}
		}else{
			fOut.write("Null Results! \n\n");
		}
	}
	
	@Test //Dbpedia
	public void test23(){
	  System.out.println("\nRunning test 5.2.3 - dbpedia query");
		
	  source="Astronaut(nationality)";
	  target="Astronaut(nationality)";
	  finalRes = new ArrayList<Match_Struc>();
	  
	  //call appropriate methods
	  finalRes=spsmCall.getSchemas(finalRes, source, target);
	  
	  if(finalRes!=null && finalRes.size()!=0){
	    finalRes = getRepairedSchema.prepare(finalRes);
	  }
	  
	  finalRes = createQuery.createQueryPrep(finalRes, "dbpedia",null,20);
	  
	  fOut.write("Test 5.2.3 - dbpedia query\n");
	  
	  if(finalRes!=null){
	    if(finalRes.size() == 0){
	      //then we have no results so end test
	      fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
	      fOut.write("Empty results returned. \n\n");
	    }else{
	      Match_Struc current = finalRes.get(0);
	        
	      fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
	      fOut.write("Expected Result:\n\n" + 
	        
	          "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type Astronaut ;\n"
	          + "dbo:nationality ?nationality ;\n"
	          + ".}\n"
	          + "LIMIT 20\n\n");
	      
	      fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
	    }
	  }else{
	    fOut.write("Null Results! \n\n");
	  }
	}
	
	@Test //Dbpedia
	public void test24(){
	  System.out.println("\nRunning test 5.2.4 - dbpedia query");
		
	  source="Mountain(elevation)";
	  target="Mountain(elevation)";
	  finalRes = new ArrayList<Match_Struc>();
	  
	  //call appropriate methods
	  finalRes=spsmCall.getSchemas(finalRes, source, target);
	  
	  if(finalRes!=null && finalRes.size()!=0){
	    finalRes = getRepairedSchema.prepare(finalRes);
	  }
	  
	  finalRes = createQuery.createQueryPrep(finalRes, "dbpedia",null,20);
	  
	  fOut.write("Test 5.2.4 - dbpedia query\n");
	  
	  if(finalRes!=null){
	    if(finalRes.size() == 0){
	      //then we have no results so end test
	      fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
	      fOut.write("Empty results returned. \n\n");
	    }else{
	      Match_Struc current = finalRes.get(0);
	        
	      fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
	      fOut.write("Expected Result:\n\n" + 
	        
	          "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:Mountain ;\n"
	          + "dbo:elevation ?elevation ;\n"
	          + ".}\n"
	          + "LIMIT 20\n\n");
	      
	      fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
	    }
	  }else{
	    fOut.write("Null Results! \n\n");
	  }
	}
	
	@Test //Dbpedia
	public void test25(){
	  System.out.println("\nRunning test 5.2.5 - dbpedia query");	
		
	  source="Person(occupation, birthPlace)";
	  target="Person(occupation, birthPlace)";
	  finalRes = new ArrayList<Match_Struc>();
	  
	  //call appropriate methods
	  finalRes=spsmCall.getSchemas(finalRes, source, target);
	  
	  if(finalRes!=null && finalRes.size()!=0){
	    finalRes = getRepairedSchema.prepare(finalRes);
	  }
	  
	  finalRes = createQuery.createQueryPrep(finalRes, "dbpedia",null,20);
	  
	  fOut.write("Test 5.2.5 - dbpedia query\n");
	  
	  if(finalRes!=null){
	    if(finalRes.size() == 0){
	      //then we have no results so end test
	      fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
	      fOut.write("Empty results returned. \n\n");
	    }else{
	      Match_Struc current = finalRes.get(0);
	        
	      fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
	      fOut.write("Expected Result:\n\n" + 
	        
	          "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:Person ;\n"
	          + "dbo:occupation ?occupation ;\n"
	          + "dbo:birthPlace ?birthPlace .}\n"
	          + "LIMIT 20\n\n");
	      
	      fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
	    }
	  }else{
	    fOut.write("Null Results! \n\n");
	  }
	}
	
	@Test //Dbpedia
	public void test26(){
	  System.out.println("\nRunning test 5.2.6 - dbpedia query");
	  
	  source="Person(occupation, instrument)";
	  target="Person(occupation, instrument)";
	  finalRes = new ArrayList<Match_Struc>();
	  
	  //call appropriate methods
	  finalRes=spsmCall.getSchemas(finalRes, source, target);
	  
	  if(finalRes!=null && finalRes.size()!=0){
	    finalRes = getRepairedSchema.prepare(finalRes);
	  }
	  
	  finalRes = createQuery.createQueryPrep(finalRes, "dbpedia",null,20);
	  
	  fOut.write("Test 5.2.6 - dbpedia query\n");
	  
	  if(finalRes!=null){
	    if(finalRes.size() == 0){
	      //then we have no results so end test
	      fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
	      fOut.write("Empty results returned. \n\n");
	    }else{
	      Match_Struc current = finalRes.get(0);
	        
	      fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
	      fOut.write("Expected Result:\n\n" + 
	        
	          "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:Person ;\n"
	          + "dbo:occupation ?occupation ;\n"
	          + "dbo:instrument ?instrument .}\n"
	          + "LIMIT 20\n\n"); 
	      
	      fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
	    }
	  }else{
	    fOut.write("Null Results! \n\n");
	  }
	}
	
	@Test //Dbpedia
	public void test27(){
	  System.out.println("\nRunning test 5.2.7 - dbpedia query");
		
	  source="Cave(location)";
	  target="Cave(location)";
	  finalRes = new ArrayList<Match_Struc>();
	  
	  //call appropriate methods
	  finalRes=spsmCall.getSchemas(finalRes, source, target);
	  
	  if(finalRes!=null && finalRes.size()!=0){
	    finalRes = getRepairedSchema.prepare(finalRes);
	  }
	  
	  finalRes = createQuery.createQueryPrep(finalRes, "dbpedia",null,20);
	  
	  fOut.write("Test 5.2.7 - dbpedia query\n");
	  
	  if(finalRes!=null){
	    if(finalRes.size() == 0){
	      //then we have no results so end test
	      fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
	      fOut.write("Empty results returned. \n\n");
	    }else{
	      Match_Struc current = finalRes.get(0);
	        
	      fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
	      fOut.write("Expected Result:\n\n" + 
	        
	          "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:Cave ;\n"
	          + "dbo:location ?location ;\n"
	          + ".}\n"
	          + "LIMIT 20\n\n");
	      
	      fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
	    }
	  }else{
	    fOut.write("Null Results! \n\n");
	  }
	}
	
	@Test //Dbpedia
	public void test28(){
	  System.out.println("\nRunning test 5.2.8 - dbpedia query");
		
	  source="FormulaOneRacer(races)";
	  target="FormulaOneRacer(races)";
	  finalRes = new ArrayList<Match_Struc>();
	  
	  //call appropriate methods
	  finalRes=spsmCall.getSchemas(finalRes, source, target);
	  
	  if(finalRes!=null && finalRes.size()!=0){
	    finalRes = getRepairedSchema.prepare(finalRes);
	  }
	  
	  finalRes = createQuery.createQueryPrep(finalRes, "dbpedia",null,20);
	  
	  fOut.write("Test 5.2.8 - dbpedia query\n");
	  
	  if(finalRes!=null){
	    if(finalRes.size() == 0){
	      //then we have no results so end test
	      fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
	      fOut.write("Empty results returned. \n\n");
	    }else{
	      Match_Struc current = finalRes.get(0);
	        
	      fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
	      fOut.write("Expected Result:\n\n" + 
	        
	          "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:FormulaOneRacer ;\n"
	          + "dbo:races ?races ;\n"
	          + ".}\n"
	          + "LIMIT 20\n\n");
	      
	      fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
	    }
	  }else{
	    fOut.write("Null Results! \n\n");
	  }
	}
	
	@Test //Dbpedia
	public void test29(){
	  System.out.println("\nRunning test 5.2.9 - dbpedia query");	
		
	  source="River(length)";
	  target="River(length)";
	  finalRes = new ArrayList<Match_Struc>();
	  
	  //call appropriate methods
	  finalRes=spsmCall.getSchemas(finalRes, source, target);
	  
	  if(finalRes!=null && finalRes.size()!=0){
	    finalRes = getRepairedSchema.prepare(finalRes);
	  }
	  
	  finalRes = createQuery.createQueryPrep(finalRes, "dbpedia",null,20);
	  
	  fOut.write("Test 5.2.9 - dbpedia query\n");
	  
	  if(finalRes!=null){
	    if(finalRes.size() == 0){
	      //then we have no results so end test
	      fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
	      fOut.write("Empty results returned. \n\n");
	    }else{
	      Match_Struc current = finalRes.get(0);
	        
	      fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
	      fOut.write("Expected Result:\n\n" + 
	        
	          "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:River ;\n"
	          + "dbo:length ?length ;\n"
	          + ".}\n"
	          + "LIMIT 20\n\n");
	      
	      fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
	    }
	  }else{
	    fOut.write("Null Results! \n\n");
	  }
	}
	
	@Test //Dbpedia
	public void test210(){
	  System.out.println("\nRunning test 5.2.10 - dbpedia query");
		
	  source="Royalty(parent)";
	  target="Royalty(parent)";
	  finalRes = new ArrayList<Match_Struc>();
	  
	  //call appropriate methods
	  finalRes=spsmCall.getSchemas(finalRes, source, target);
	  
	  if(finalRes!=null && finalRes.size()!=0){
	    finalRes = getRepairedSchema.prepare(finalRes);
	  }
	  
	  finalRes = createQuery.createQueryPrep(finalRes, "dbpedia",null,20);
	  
	  fOut.write("Test 5.2.10 - dbpedia query\n");
	  
	  if(finalRes!=null){
	    if(finalRes.size() == 0){
	      //then we have no results so end test
	      fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
	      fOut.write("Empty results returned. \n\n");
	    }else{
	      Match_Struc current = finalRes.get(0);
	        
	      fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
	      fOut.write("Expected Result:\n\n" + 
	        
	          "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:Royalty ;\n"
	          + "dbo:parent ?parent ;\n"
	          + ".}\n"
	          + "LIMIT 20\n\n");
	      
	      fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
	    }
	  }else{
	    fOut.write("Null Results! \n\n");
	  }
	}
	
	@Test //Dbpedia
	public void test211(){
	  System.out.println("\nRunning test 5.2.11 - dbpedia query");
		
	  source="river(length)";
	  target="river(length)";
	  finalRes = new ArrayList<Match_Struc>();
	  
	  //call appropriate methods
	  finalRes=spsmCall.getSchemas(finalRes, source, target);
	  
	  if(finalRes!=null && finalRes.size()!=0){
	    finalRes = getRepairedSchema.prepare(finalRes);
	  }
	  
	  finalRes = createQuery.createQueryPrep(finalRes, "dbpedia",null,20);
	  
	  fOut.write("Test 5.2.11 - dbpedia query\n");
	  
	  if(finalRes!=null){
	    if(finalRes.size() == 0){
	      //then we have no results so end test
	      fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
	      fOut.write("Empty results returned. \n\n");
	    }else{
	      Match_Struc current = finalRes.get(0);
	        
	      fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
	      fOut.write("Expected Result:\n\n" + 
	        
	          "");
	      
	      fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
	    }
	  }else{
	    fOut.write("Null Results! \n\n");
	  }
	}
	
	@Test //Dbpedia
	public void test212(){
	  System.out.println("\nRunning test 5.2.12 - dbpedia query");
		
	  source="Stream(length)";
	  target="Stream(length)";
	  finalRes = new ArrayList<Match_Struc>();
	  
	  //call appropriate methods
	  finalRes=spsmCall.getSchemas(finalRes, source, target);
	  
	  if(finalRes!=null && finalRes.size()!=0){
	    finalRes = getRepairedSchema.prepare(finalRes);
	  }
	  
	  finalRes = createQuery.createQueryPrep(finalRes, "dbpedia",null,20);
	  
	  fOut.write("Test 5.2.12 - dbpedia query\n");
	  
	  if(finalRes!=null){
	    if(finalRes.size() == 0){
	      //then we have no results so end test
	      fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
	      fOut.write("Empty results returned. \n\n");
	    }else{
	      Match_Struc current = finalRes.get(0);
	        
	      fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
	      fOut.write("Expected Result:\n\n" + 
	        
	          "");
	      
	      fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
	    }
	  }else{
	    fOut.write("Null Results! \n\n");
	  }
	}
	
	@Test //Dbpedia
	public void test213(){
	  System.out.println("\nRunning test 5.2.13 - dbpedia query");
		
	  source="River(Mountain(elevation))";
	  target="River(Mountain(elevation))";
	  finalRes = new ArrayList<Match_Struc>();
	  
	  //call appropriate methods
	  finalRes=spsmCall.getSchemas(finalRes, source, target);
	  
	  if(finalRes!=null && finalRes.size()!=0){
	    finalRes = getRepairedSchema.prepare(finalRes);
	  }
	  
	  finalRes = createQuery.createQueryPrep(finalRes, "dbpedia",null,20);
	  
	  fOut.write("Test 5.2.13 - dbpedia query\n");
	  
	  if(finalRes!=null){
	    if(finalRes.size() == 0){
	      //then we have no results so end test
	      fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
	      fOut.write("Empty results returned. \n\n");
	    }else{
	      Match_Struc current = finalRes.get(0);
	        
	      fOut.write("Creating query from schema, "+current.getDatasetSchema() + "\n");
	      fOut.write("Expected Result:\n\n" + 
	        
	          "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:River;\n"
	          + "dbo:Mountain ?Mountain ;\n"
	          + ".}\n"
	          + "LIMIT 20\n\n");
	      
	      fOut.write("Actual Result: \n\n" + current.getQuery() + "\n\n");
	    }
	  }else{
	    fOut.write("Null Results! \n\n");
	  }
	}
	
	@After
	public void cleanUp(){
		fOut.close();
	}
}
