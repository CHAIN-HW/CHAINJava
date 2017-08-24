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
import chain_source.Match_Struc;
import chain_source.Repair_Schema;

/*
 * Responsible for testing the element of CHAIn
 * that after calling SPSM and filtering the results
 * will try to create a repaired schema based on the 
 * match data between the source and target schemas
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Repair_Schema_Test_Cases {

	private Call_SPSM spsmCall;
	private Repair_Schema getRepairedSchema;
	
	private ArrayList<Match_Struc> finalRes;
	private String target, source;
	
	//for writing results
	private static File testRes;
	private PrintWriter fOut;
	private static boolean alreadyWritten;
	
	@BeforeClass
	public static void beforeAll(){
		System.out.println("These tests are responsible for testing the element of CHAIn that after calling SPSM\n"
				+"and filtering the results will try to create a repaired schema based on the match data between\n"
				+"the source and target schemas.");
		System.out.println("\nThe results from these tests can be found in outputs/testing/Schema_Repair_Tests.txt\n");

		
		alreadyWritten = false;
		try{
			testRes = new File("outputs/testing/Schema_Repair_Tests.txt");
			testRes.createNewFile();
			
			new PrintWriter("outputs/testing/Schema_Repair_Tests.txt").close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	
	@Before
	public void setup(){
		spsmCall = new Call_SPSM();
		getRepairedSchema = new Repair_Schema();
		
		try{
			fOut = new PrintWriter(new FileWriter(testRes,true));
			
			if(alreadyWritten==false){
				fOut.write("Testing Results for Repair_Schema_Test_Cases.java\n\n");
				alreadyWritten = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void test11(){
		System.out.println("\nRunning test 4.1.1");
		
		source="auto(brand,name,color)";
		target="car(year,brand,colour)";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		fOut.write("Test 4.1.1\n");
		fOut.write("Calling SPSM with source, "+source+" & target, "+target+"\n");
		
		fOut.write("Expected Result: repaired schema == 'car(colour,brand)' \n");
		
		if(finalRes!=null){
			if(finalRes.size() == 0){
				//then we have no results so end test
				fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
				fOut.write("Empty results returned. \n\n");
			}else{
				//we can test repaired schema
				String rSchema = finalRes.get(0).getRepairedSchema();
				fOut.write("Actual Result: repaired schema == '" + rSchema + "' \n\n");
			}
		}else{
			fOut.write("Null Results! \n\n");
		}
	}
	
	@Test
	public void test56(){
		System.out.println("\nRunning test 4.5.6");
		
		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="conference(paper(title,document(date(day,month,year),writer(name(first,second)))))";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes=spsmCall.getSchemas(finalRes, source, target);
	
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		fOut.write("Test 4.5.6\n");
		fOut.write("Calling SPSM with source, "+source+" & target, "+target+"\n");
		
		fOut.write("Expected Result: repaired schema == 'conference(paper(title,document(date(day,month,year),writer(name(first,second)))))' \n");
		
		if(finalRes!=null){
			if(finalRes.size() == 0){
				//then we have no results so end test
				fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
				fOut.write("Empty results returned. \n\n");
			}else{
				//we can test repaired schema
				Match_Struc result = finalRes.get(0);
				
				fOut.write("Actual Result: repaired schema == '" + result.getRepairedSchema() + "' \n\n");
			}
		}else{
			fOut.write("Null Results! \n\n");
		}
	}
	
	@Test
	public void test57(){
		System.out.println("\nRunning test 4.5.7");
		
		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="conference(paper(title,document(category(day,month,year),writer(name(first,second)))))";
		finalRes = new ArrayList<Match_Struc>();
		
		//call appropriate methods
		finalRes = spsmCall.getSchemas(finalRes, source, target);
	
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		fOut.write("Test 4.5.7\n");
		fOut.write("Calling SPSM with source, "+source+" & target, "+target+"\n");
		
		fOut.write("Expected Result: repaired schema == 'conference(paper(title,document(writer(name(first,second)))))' \n");
		
		if(finalRes!=null){
			if(finalRes.size() == 0){
				//then we have no results so end test
				fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
				fOut.write("Empty results returned. \n\n");
			}else{
				//we can test repaired schema
				String rSchema = finalRes.get(0).getRepairedSchema();
				fOut.write("Actual Result: repaired schema == '" + rSchema + "' \n\n");
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
