package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import chain.core.CallSPSM;
import chain.core.MatchStruc;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import chain.core.RepairSchema;

/* Author Tanya Howden
 * Author Diana Bental
 * Date September 2017
 * Date December 2017
 * Modified added assertions
 */

/*
 * Responsible for testing the element of CHAIn
 * that after calling SPSM and filtering the results
 * will try to create a repaired schema based on the 
 * match data between the source and target schemas
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RepairSchemaTestCases extends tests.BaseTest  {

	private CallSPSM spsmCall;
	private RepairSchema getRepairedSchema;
	
	private ArrayList<MatchStruc> finalRes;
	private String target, source;
	private String test;
	private int expectedNumMatches ;
	int expectedSizeFirst ; // Expected size of first match (if any matches are produced)
	
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
		spsmCall = new CallSPSM();
		getRepairedSchema = new RepairSchema();
		
		try{
			fOut = new PrintWriter(new FileWriter(testRes,true));
			
			if(alreadyWritten==false){
				fOut.write("Testing Results for RepairSchemaTestCases.java\n\n");
				alreadyWritten = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void test4101(){
		System.out.println("\nRunning test 4.1.1");
		
		source="auto(brand,name,color)";
		target="car(year,brand,colour)";
		finalRes = new ArrayList<MatchStruc>();
		
		//call appropriate methods
		finalRes=spsmCall.callSPSM(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.repairSchemas(finalRes);
		}
		
		fOut.write("Test 4.1.1\n");
		fOut.write("Calling SPSM with source, "+source+" & target, "+target+"\n");
		
		fOut.write("Expected Result: repaired schema == 'car(colour,brand)' \n");
		
		assertTrue(finalRes.size() == 1) ; // SPSM is now working Dec2017
		
		// System.out.println(finalRes.size());
		
		if(finalRes!=null){
			if(finalRes.size() == 0){
				//then we have no results so end test
				fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
				fOut.write("Empty results returned. \n\n");
				fail();
			}else{
				//we can test repaired schema
				String repairedSchema = finalRes.get(0).getRepairedSchema();
				System.out.println("Expected Result: repaired schema == 'car(colour,brand)'");
				System.out.println("Actual Result: repaired schema == '" + repairedSchema + "' \n");
				fOut.write("Actual Result: repaired schema == '" + repairedSchema + "' \n\n");
				// System.out.println(finalRes.get(0)) ; // Print the whole match structure
				assertTrue(repairedSchema.contains("car(colour,brand)")) ;	
			}
		}else{
			fOut.write("Null Results! \n\n");
			fail() ;
		}
	}
	
	@Test
	public void test4102(){
		System.out.println("\nRunning test 4.1.2");
		
		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="conference(paper(title,document(date(day,month,year),writer(name(first,second)))))";
		finalRes = new ArrayList<MatchStruc>();
		
		//call appropriate methods
		finalRes=spsmCall.callSPSM(finalRes, source, target);
	
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.repairSchemas(finalRes);
		}
		
		fOut.write("Test 4.1.2\n");
		fOut.write("Calling SPSM with source, "+source+" & target, "+target+"\n");
		
		System.out.println("Expected Result: repaired schema == 'conference(paper(title,document(date(day,month,year),writer(name(first,second)))))'");
		fOut.write("Expected Result: repaired schema == 'conference(paper(title,document(date(day,month,year),writer(name(first,second)))))' \n");
		
		assertTrue(finalRes.size() == 1);
		assertTrue(finalRes.get(0).getRepairedSchema().contains("conference(")) ;
		
		if(finalRes!=null){
			if(finalRes.size() == 0){
				//then we have no results so end test
				fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
				fOut.write("Empty results returned. \n\n");
				fail() ;
				
			}else{
				// System.out.println(finalRes.size()) ;
				//we can test repaired schema
				MatchStruc result = finalRes.get(0);
				fOut.write("Actual Result: repaired schema == '" + result.getRepairedSchema() + "' \n\n");
				System.out.println("Actual Result: repaired schema == '" + result.getRepairedSchema());
				assertTrue(result.getRepairedSchema().contains("conference(")) ;
				assertTrue(result.getRepairedSchema().contains("writer(")) ;
				assertTrue(result.getRepairedSchema().contains("date(")) ;
				assertTrue(result.getRepairedSchema().contains("title")) ;
				assertTrue(result.getRepairedSchema().contains("month")) ;
			}
		}else{
			fail() ;
			fOut.write("Null Results! \n\n");
		}
		
		
	}
	
	@Test
	public void test4103(){
		System.out.println("\nRunning test 4.1.3");
		
		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="conference(paper(title,document(category(day,month,year),writer(name(first,second)))))";
		finalRes = new ArrayList<MatchStruc>();
		
		//call appropriate methods
		finalRes = spsmCall.callSPSM(finalRes, source, target);
	
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.repairSchemas(finalRes);
		}
		
		fOut.write("Test 4.1.3\n");
		fOut.write("Calling SPSM with source, "+source+" & target, "+target+"\n");
		
		fOut.write("Expected Result: repaired schema == 'conference(paper(title,document(writer(name(first,second)))))' \n");
		System.out.println("Expected Result: repaired schema == 'conference(paper(title,document(writer(name(first,second)))))' \n");
		
		assertTrue(finalRes.size() == 1) ; // SPSM is expected to run correctly - Dec2017
		
		
		if(finalRes!=null){
			if(finalRes.size() == 0){
				//then we have no results so end test
				fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n");
				fOut.write("Empty results returned. \n\n");
				fail() ;
			}else{
				//we can test repaired schema
				String rSchema = finalRes.get(0).getRepairedSchema();
				fOut.write("Actual Result: repaired schema == '" + rSchema + "' \n\n");
				System.out.println("Actual Result: repaired schema == '" + rSchema + "' \n\n");
				assertTrue(rSchema.contains("conference(paper(")) ;
				assertTrue(rSchema.contains("document(writer(name(")) ;
				assertTrue(rSchema.contains("second"));
				assertTrue(rSchema.contains("first"));
				assertTrue(rSchema.contains("title"));
			}
		}else{
			fOut.write("Null Results! \n\n");
			fail() ;
		}
		
	}
	
	@Test
	public void test4104(){
		testRepairSchema("","","4.1.4",0, 0) ;
	}
	
	@Test
	public void test4105(){
		source="author(name)";
		target="";
		test = "4.1.05" ;
		expectedNumMatches = 0 ;
		expectedSizeFirst = 0 ;
		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4106(){
		source="";
		target="author(name)";
		test = "4.1.06" ;
		expectedNumMatches = 0 ;
		expectedSizeFirst = 0 ;
		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4107(){
		source="author(name)";
		target="author(name)";
		test = "4.1.07" ;
		expectedNumMatches = 1 ;
		expectedSizeFirst = 2 ;
		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4108(){

		source="author(name)";
		target="document(title,author) ; author(name,document) ; reviewAuthor(firstname,lastname,review)";		
		test = "4.1.08" ;
		expectedNumMatches = 2 ;
		expectedSizeFirst = 2 ;
		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4109(){
		source="author(name)";
		target="document(title,author) ; conferenceMember(name)";
		test = "4.1.09" ;
		expectedNumMatches = 0 ;
		expectedSizeFirst = 0 ;
		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4110(){

		source="author(name)";
		target="author(name) ; document(title,author) ; paperWriter(firstname,surname,paper) ; reviewAuthor(firstname,lastname,review)";
		test = "4.1.10" ;
		expectedNumMatches = 3 ;
		expectedSizeFirst = 2 ;
		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4111(){
		source="author";
		target="writer";
		test = "4.1.11" ;
		expectedNumMatches = 1 ;
		expectedSizeFirst = 1 ;
		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4112(){
		source="author";
		target="document";
		test = "4.1.12" ;
		expectedNumMatches = 0 ;
		expectedSizeFirst = 0 ;
		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4113(){

		source="author(name)";
		target="document(name)";
		test = "4.1.13" ;
		expectedNumMatches = 0 ;
		expectedSizeFirst = 0 ;
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4114(){

		source="author(name)";
		target="reviewWriter(review,name)";
		test = "4.1.14" ;
		expectedNumMatches = 1 ;
		expectedSizeFirst = 2 ;
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4115(){

		source="reviewWriter(document,date,name)";
		target="author(name,email,coAuthors,writePaper,submitPapers,review)";
		test = "4.1.15" ;
		expectedNumMatches = 1 ;
		expectedSizeFirst = 3 ;
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4116(){

		source="review(date(day,month,year))";
		target="document(date(day,month,year))";
		test = "4.1.16" ;
		expectedNumMatches = 1 ;
		expectedSizeFirst = 5 ;
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4117(){

		source = "review(publication(day,month,year))";
		target= "review(date(day,month,year))";
		test = "4.1.17" ;
		expectedNumMatches = 1 ;
		expectedSizeFirst = 5 ;
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4118(){
		source="review(publication(day,month,year))";
		target= "document(date(day,month,year))";
		test = "4.1.18" ;
		expectedNumMatches = 0 ;
		expectedSizeFirst = 0 ;
		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4119(){
		source="review(category(day,month,year))";
		target="review(date(day,month,year))";
		test = "4.1.19" ;
		expectedNumMatches = 1 ;
		expectedSizeFirst = 1 ;
		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4120(){
		source="review(category(day,month,year))";
		target="document(date(day,month,year))";
		test = "4.1.20" ;
		expectedNumMatches = 1 ;
		expectedSizeFirst = 1 ;
		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}

	@Test
	public void test4121(){
		
		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="conference(paper(title,document(date(day,month,year),writer(name(first,second)))))";
		test = "4.1.21" ;
		expectedNumMatches = 1 ;
		expectedSizeFirst = 12 ;

		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4122(){
		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="conference(paper(title,document(category(day,month,year),writer(name(first,second)))))";
		test = "4.1.22" ;
		expectedNumMatches = 1 ;
		expectedSizeFirst = 8 ;
		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4123(){

		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="event(paper(title,document(category(day,month,year),writer(name(first,second)))))";
		test = "4.1.23" ;
		expectedNumMatches = 0 ;
		expectedSizeFirst = 0 ;
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4124(){

		source="conferenceDocument(nameOfAuthor)";
		target="conferenceReview(authorName)";
		test = "4.1.24" ;
		expectedNumMatches = 1 ;
		expectedSizeFirst = 2 ;
		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4125(){

		source="conference_document(name_of_author)";
		target="conference_review(author_name)";
		test = "4.1.25" ;
		expectedNumMatches = 1 ;
		expectedSizeFirst = 2 ;
		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4126(){
		source="conference_document(name_of_author)";
		target="ConferenceReview(authorName)";
		test = "4.1.26" ;
		expectedNumMatches = 1 ;
		expectedSizeFirst = 2 ;
		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4127(){

		source="conference document(name of author)";
		target="conference review(author name)";
		test = "4.1.27" ;
		expectedNumMatches = 1 ;
		expectedSizeFirst = 2 ;
		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4128(){

		source="conference document(nameOfAuthor)";
		target="conference review(authorName)";
		test = "4.1.28" ;
		expectedNumMatches = 1 ;
		expectedSizeFirst = 2 ;

		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4129(){
		source="conferencedocument(nameofauthor)";
		target="conference review(authorname)";
		test = "4.1.29" ;
		expectedNumMatches = 1 ;
		expectedSizeFirst = 2 ;
		

		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	
	@Test
	public void test4130(){

		source="auto(brand,name,color)";
		target="car(year,brand,colour)";
		test = "4.1.30" ;
		expectedNumMatches = 1 ;
		expectedSizeFirst = 3 ;
		
		testRepairSchema(source, target, test, expectedNumMatches, expectedSizeFirst) ;
	}
	

	
	private void testRepairSchema(String source, String target, String test,
			int expectedNumMatches, int expectedSizeFirst) {
		// 
		System.out.println("Test "+ test) ;
		System.out.println("Source " + source) ;
		System.out.println("Target " + target) ;
		System.out.println("Expected number of repairs: " + expectedNumMatches) ;
		
		
		CallSPSM spsmCall = new CallSPSM();
		RepairSchema getRepairedSchema = new RepairSchema();
		
		ArrayList<MatchStruc> finalRes = new ArrayList<MatchStruc>();
		
		finalRes=spsmCall.callSPSM(finalRes, source, target);
		
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.repairSchemas(finalRes);
		} 
		
		if(finalRes == null) {
			if (expectedNumMatches > 0) {
				System.out.println("Test failed: repairs were expected but no matches returned.") ;
				fail() ;
			}
		} else if (finalRes.size() == 0) {
			if (expectedNumMatches > 0) {
				System.out.println("Test failed: repairs were expected but no matches returned.") ;
				fail() ;
			} else {
				System.out.println("Empty results returned. \n\n");
			}
		}
		else {
			System.out.println("Number of matches / repairs: "+finalRes.size());
			if (expectedNumMatches !=  finalRes.size()) {
				fail() ;			
			}
			
			//we can test repaired schema
			for(int i = 0 ; i < finalRes.size() ; i++){
				MatchStruc currRes = finalRes.get(i);
				
				System.out.println("Repaired schema: "+currRes.getRepairedSchema());
				System.out.println("Similarity == "+currRes.getSimValue()+" & size of matched structure == "+currRes.getNumMatchComponents());
				
				// Just check the size of the first match
				if(i==0) {
					if(currRes.getNumMatchComponents() == expectedSizeFirst) 
						System.out.println("Repaired schema size matches expected size") ;
						else {
							System.out.println("Test Error: Repaired schema size " + currRes.getNumMatchComponents() + "expected size :" + expectedSizeFirst) ;
							fail() ;
					
				}
				System.out.println() ;
				}
			}
		}

	}
	
	@After
	public void cleanUp(){
		fOut.close();
	}
}
