package chain_tests.core;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import chain.core.CallSPSM;
import chain.core.MatchStruc;
import org.junit.*;
import org.junit.runners.MethodSorters;

/* Author Tanya Howden
 * Author Diana Bental
 * Date September 2017
 * Modified December 2017
 * added assertions
 */

/*
 * Responsible for testing the output results from
 * CallSPSM.java which takes in source and target
 * schemas and then calls SPSM before reading in
 * results as serialised object
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SPSMTestCases extends BaseTest {
	private CallSPSM methodCaller;
	private ArrayList<MatchStruc> results;
	private String source,target;
	
	//for writing results
	private static File testRes;
	private PrintWriter fOut;
	private static boolean alreadyWritten;
	
	@BeforeClass
	public static void beforeAll(){
		System.out.println("These tests are responsible for testing the output results from CallSPSM.java which takes in a source"
							+"\nand target schema before calling SPSM and reading in the results as serialised objects.");
		System.out.println("\nThe results from these tests can be found in outputs/testing/Call_SPSM_Tests.txt\n");
		
		alreadyWritten = false;
		try{
			testRes = new File("outputs/testing/Call_SPSM_Tests.txt");
			testRes.createNewFile();
			
			new PrintWriter("outputs/testing/Call_SPSM_Tests.txt").close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Before
	public void setup(){
		methodCaller = new CallSPSM();
		results = new ArrayList<MatchStruc>();
		
		try{
			fOut = new PrintWriter(new FileWriter(testRes,true));
			
			if(alreadyWritten==false){
				fOut.write("Testing Results for SPSMTestCases.java\n\n");
				alreadyWritten = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	//no source or target
	public void test1_1_1() {
		System.out.println("\nRunning test 1.1.1");
		
		source=""; 
		target="";
		
		results = methodCaller.callSPSM(results, source, target);
		
		fOut.write("Test 1.1.1\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results == null){
			fOut.write("Null Results! \n\n");
		}else{
			fOut.write("Expected Result: results.size() == 0 \n");
			fOut.write("Actual Result: results.size() == "+results.size()+"\n\n");
		}
		
		assertTrue(results.size() == 0) ;
	}
	
	@Test
	//no target but one source
	public void test1_2_1(){
		System.out.println("\nRunning test 1.2.1");
		
		source="author(name)";
		target="";
		
		results = methodCaller.callSPSM(results,source,target);
		
		fOut.write("Test 1.2.1\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results == null){
			fOut.write("Null Results! \n\n");
		}else{
			fOut.write("Expected Result: results.size() == 0 \n");
			fOut.write("Actual Result: results.size() == "+results.size()+"\n\n"); 
		}
		
		assertTrue(results.size() == 0) ;
	}
	
	@Test
	//no source but one target
	public void test1_2_2(){
		System.out.println("\nRunning test 1.2.2");
		
		source="";
		target="author(name)";
		
		results = methodCaller.callSPSM(results,source,target);
		
		fOut.write("Test 1.2.2\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results == null){
			fOut.write("Null Results! \n\n");
		}else{
			
			fOut.write("Expected Result: results.size() == 0 \n");
			fOut.write("Actual Result: results.size() == "+results.size()+"\n\n"); 
		}
		
		assertTrue(results.size() == 0) ;
	}
	
	@Test
	//one source and one target
	public void test1_2_3(){
		System.out.println("\nRunning test 1.2.3");
		
		source="author(name)";
		target="author(name)";
		
		results = methodCaller.callSPSM(results,source,target);
		
		fOut.write("Test 1.2.3\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		fOut.write("Expected Result: similarity == 1.0"+" & numMatches == 2 \n");
		
		if(results == null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			if(results.size() > 0){
				MatchStruc res = results.get(0);
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n");
				assertTrue(res.getSimValue() == 1.0) ;
				assertTrue(res.getNumMatchComponents() == 2) ;
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail() ;
				
			}			 
		}
		assertTrue(results.size() == 1) ;
	}
	
	@Test
	//one source w multiple targets
	public void test1_2_4(){
		System.out.println("\nRunning test 1.2.4");
		
		source="author(name)";
		target="document(title,author) ; author(name,document) ; reviewAuthor(firstname,lastname,review)";
		
		results = methodCaller.callSPSM(results, source, target);
		
		int[] answer = {2,2};
		double[] simValues = {1.0,0.75};
		String[] schemas = {"author(name,document)", "reviewAuthor(firstname,lastname,review)"} ;
		
		fOut.write("Test 1.2.4\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			assertTrue(results.size() == 2) ;
			if(results.size() > 0){
				// System.out.println(results.size()); 
				for(int i = 0 ; i < results.size() ; i++){
					MatchStruc currRes = results.get(i);
					// System.out.println("Target: "+currRes.getDatasetSchema()+"\n");
					// System.out.println("Expected Result: similarity == "+simValues[i]+" & numMatches == "+answer[i]+"\n");
					// System.out.println("Actual Result: similarity == "+currRes.getSimValue()+" & numMatches == "+currRes.getNumMatchComponents()+"\n\n");
					assertTrue(currRes.getDatasetSchema().contains(schemas[i]));
					assertTrue(currRes.getSimValue() == simValues[i]) ;
					assertTrue(currRes.getNumMatchComponents() == answer[i]) ;
					
					fOut.write("Target: "+currRes.getDatasetSchema()+"\n");
					fOut.write("Expected Result: similarity == "+simValues[i]+" & numMatches == "+answer[i]+"\n");
					fOut.write("Actual Result: similarity == "+currRes.getSimValue()+" & numMatches == "+currRes.getNumMatchComponents()+"\n\n"); 
				}
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail() ;
			}
		}
	}
	
	@Test
	//one source w two targets
	public void test1_3_1(){
		System.out.println("\nRunning test 1.3.1");
		
		source="author(name)";
		target="document(title,author) ; conferenceMember(name)";
		
		results = methodCaller.callSPSM(results,source,target);
		
		fOut.write("Test 1.3.1\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			fOut.write("Expected Result: results.size() == 0 \n");
			fOut.write("Actual Result: results.size() == "+results.size()+"\n\n");
		}
		assertTrue(results.size() == 0) ;
	}
	
	@Test
	public void test1_3_2(){
		System.out.println("\nRunning test 1.3.2");
		
		source="author(name)";
		target="author(name) ; document(title,author)";
		
		results = methodCaller.callSPSM(results, source, target);
		
		fOut.write("Test 1.3.2\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			// System.out.println(results.size());
			assertTrue(results.size() == 1) ;
			if(results.size() > 0){
				MatchStruc currRes = results.get(0);
				// System.out.println("Target: "+currRes.getDatasetSchema()+"\n");
				// System.out.println("Expected Result: similarity == 1.0"+" & numMatches == 2 \n");
				// System.out.println("Actual Result: similarity == "+currRes.getSimValue()+" & numMatches == "+currRes.getNumMatchComponents()+"\n\n"); 
				
				
				fOut.write("Target: "+currRes.getDatasetSchema()+"\n");
				fOut.write("Expected Result: similarity == 1.0"+" & numMatches == 2 \n");
				fOut.write("Actual Result: similarity == "+currRes.getSimValue()+" & numMatches == "+currRes.getNumMatchComponents()+"\n\n"); 
				assertTrue(currRes.getDatasetSchema().contains("author(name)")) ;
				assertTrue(currRes.getSimValue() == 1.0) ;
				assertTrue(currRes.getNumMatchComponents() == 2) ;
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail() ;
			}
		}
	}
	
	@Test
	public void test1_3_3(){
		System.out.println("\nRunning test 1.3.3");
		
		source="author(name)";
		target="author(name) ; document(title,author) ; paperWriter(firstname,surname,paper) ; reviewAuthor(firstname,lastname,review)";
		
		results = methodCaller.callSPSM(results,source,target);
		
		fOut.write("Test 1.3.3\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		int[] answer={2,2,2};
		double[] simVals = {1.0,0.5,0.75};
		String[] schemas = {"author(name)", "paperWriter(firstname,surname,paper)",
				"reviewAuthor(firstname,lastname,review)"} ;
		
		if(results == null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			assertTrue(results.size() == 3) ;
			if(results.size()>0){
				for(int i = 0 ; i < results.size() ; i++){
					MatchStruc currRes = results.get(i);
					// System.out.println("Target: "+currRes.getDatasetSchema()+"\n");
					assertTrue(currRes.getDatasetSchema().contains(schemas[i]));
					assertTrue(currRes.getSimValue() == simVals[i]) ;
					assertTrue(currRes.getNumMatchComponents() == answer[i]) ;
					
					fOut.write("Target: "+currRes.getDatasetSchema()+"\n");
					fOut.write("Expected Result: similarity == "+simVals[i]+" & numMatches == "+answer[i]+"\n");
					fOut.write("Actual Result: similarity == "+currRes.getSimValue()+" & numMatches == "+currRes.getNumMatchComponents()+"\n\n");
				}
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail() ;
			}
		}
	}
	
	@Test
	public void test1_4_1(){
		System.out.println("\nRunning test 1.4.1");
		
		source="author";
		target="writer";
		
		results = methodCaller.callSPSM(results,source,target);
		
		fOut.write("Test 1.4.1\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
	
		if(results==null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			assertTrue(results.size() == 1) ;
			if(results.size()>0){
				
				MatchStruc res = results.get(0);
				
				assertTrue(res.getSimValue() == 1.0) ;
				assertTrue(res.getNumMatchComponents() == 1) ;
				
				fOut.write("Expected Result: similarity == 1.0"+" & numMatches == 1 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail();
			}
		}
	}
	
	@Test
	public void test1_4_2(){
		System.out.println("\nRunning test 1.4.2");
		
		source="author";
		target="document";
		
		results = methodCaller.callSPSM(results, source, target);
		
		fOut.write("Test 1.4.2\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results == null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{			
			fOut.write("Expected Result: results.size() == 0 \n");
			fOut.write("Actual Result: results.size() == "+results.size()+"\n\n");
			assertTrue(results.size() == 0) ;
		}
	}
	
	@Test
	public void test1_4_3(){
		System.out.println("\nRunning test 1.4.3");
		
		source="author(name)";
		target="document(name)";
		
		results = methodCaller.callSPSM(results, source, target);
		
		fOut.write("Test 1.4.3\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results == null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			fOut.write("Expected Result: results.size() == 0 \n");
			fOut.write("Actual Result: results.size() == "+results.size()+"\n\n"); 
			assertTrue(results.size() == 0) ;
		}
	}
	
	@Test
	public void test1_4_4(){
		System.out.println("\nRunning test 1.4.4");
		
		source="author(name)";
		target="reviewWriter(review,name)";
		
		results = methodCaller.callSPSM(results, source, target);
		
		fOut.write("Test 1.4.4\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results == null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			assertTrue(results.size() == 1) ;
			if(results.size() > 0){
				MatchStruc res = results.get(0);
				
				assertTrue(res.getSimValue() == 0.5) ;
				assertTrue(res.getNumMatchComponents() == 2) ;
				assertTrue(res.getDatasetSchema().contains(target)) ;
				
				fOut.write("Expected Result: similarity == 0.5"+" & numMatches == 2 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail() ;
			}	
		}
	}
	
	@Test
	public void test1_4_5(){
		System.out.println("\nRunning test 1.4.5");
		
		source="reviewWriter(document,date,name)";
		target="author(name,email,coAuthors,writePaper,submitPapers,review)";
		
		results = methodCaller.callSPSM(results,source,target);
		
		fOut.write("Test 1.4.5\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results == null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			assertTrue(results.size() == 1) ;
			if(results.size() > 0){
				MatchStruc res = results.get(0);
				
				assertTrue(res.getSimValue() == 0.5) ;
				assertTrue(res.getNumMatchComponents() == 3) ;
				assertTrue(res.getDatasetSchema().contains(target)) ;
				
				fOut.write("Target: "+res.getDatasetSchema()+"\n");
				fOut.write("Expected Result: similarity == 0.5"+" & numMatches == 3 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail() ;
			}
		}
	}
	
	@Test
	public void test1_5_1(){
		System.out.println("\nRunning test 1.5.1");
		
		source="review(date(day,month,year))";
		target="document(date(day,month,year))";
		
		results = methodCaller.callSPSM(results,source,target);
		
		fOut.write("Test 1.5.1\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		
		if(results==null){
			fOut.write("Null Results! \n\n");
			System.out.println("Null results") ;
			fail("Null results") ;
		}else{
			// System.out.println(results.size()) ;
			assertTrue(results.size() == 1) ;
			if(results.size() > 0){
				MatchStruc res = results.get(0);
				
				// System.out.println("Expected Result: similarity == 0.5"+" & numMatches == 5 \n");
				// System.out.println("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n");	
				// System.out.println(res.getDatasetSchema());
				assertTrue(res.getSimValue() == 0.5) ;
				assertTrue(res.getNumMatchComponents() == 5) ;
				assertTrue(res.getDatasetSchema().contains(target)) ;
				
				fOut.write("Expected Result: similarity == 0.5"+" & numMatches == 5 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n");			
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail("Empty results") ;
				System.out.println("Empty results") ;
			}
		}
	}
	
	@Test
	public void test1_5_2(){
		System.out.println("\nRunning test 1.5.2");
		
		source = "review(publication(day,month,year))";
		target= "review(date(day,month,year))";
		
		results = methodCaller.callSPSM(results, source, target);
		
		fOut.write("Test 1.5.2\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			assertTrue(results.size() == 1) ;
			if(results.size() > 0){
				MatchStruc res = results.get(0);
				assertTrue(res.getSimValue() == 0.6) ;
				assertTrue(res.getNumMatchComponents() == 5) ;
				assertTrue(res.getDatasetSchema().contains(target)) ;
				
				fOut.write("Expected Result: similarity == 0.6"+" & numMatches == 5 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail();
			}
		}
	}
	
	@Test
	public void test1_5_3(){
		System.out.println("\nRunning test 1.5.3");
		
		source="review(publication(day,month,year))";
		target= "document(date(day,month,year))";
		
		results = methodCaller.callSPSM(results, source, target);
		
		fOut.write("Test 1.5.3\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			assertTrue(results.size() == 0) ;
			fOut.write("Expected Result: results.size() == 0 \n");
			fOut.write("Actual Result: results.size() == "+results.size()+"\n\n"); 
		}
	}
	
	@Test
	public void test1_5_4(){
		System.out.println("\nRunning test 1.5.4");
		
		source="review(category(day,month,year))";
		target="review(date(day,month,year))";
		
		results = methodCaller.callSPSM(results, source, target);
		
		fOut.write("Test 1.5.4\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		fOut.write("Expected Result: similarity == 0.19999999999999996"+" & numMatches == 1 \n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			assertTrue(results.size() == 1) ;
			if(results.size() > 0){
				MatchStruc res = results.get(0);
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n");
				assertTrue((res.getSimValue() < 0.2) && (res.getSimValue() > 0.199)) ;
				assertTrue(res.getNumMatchComponents() == 1) ;
				assertTrue(res.getDatasetSchema().contains(target)) ;
				
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail();
			}			
		}
	}
	
	@Test
	public void test1_5_5(){
		System.out.println("\nRunning test 1.5.5");
		
		source="review(category(day,month,year))";
		target="document(date(day,month,year))";
		
		results = methodCaller.callSPSM(results,source,target);			
		
		fOut.write("Test 1.5.5\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			// System.out.println(results.size());
			assertTrue(results.size() == 1) ;
			if(results.size()>0){
				MatchStruc res = results.get(0);
				
				// System.out.println("Target: "+res.getDatasetSchema()+"\n");
				// System.out.println("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n"); 
				
				
				assertTrue((res.getSimValue() < 0.1) && (res.getSimValue() > 0.099)) ;
				assertTrue(res.getNumMatchComponents() == 1) ;
				assertTrue(res.getDatasetSchema().contains(target)) ;

				fOut.write("Expected Result: similarity == 0.9999999999999998"+" & numMatches == 1 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail();
			}			
		}
	}
	
	@Test
	public void test1_5_6(){
		System.out.println("\nRunning test 1.5.6");
		
		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="conference(paper(title,document(date(day,month,year),writer(name(first,second)))))";
		
		results = methodCaller.callSPSM(results, source, target);
		
		fOut.write("Test 1.5.6\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
			fail();
		}else{
			assertTrue(results.size() == 1) ;
			if(results.size()>0){
				MatchStruc res = results.get(0);
				fOut.write("Expected Result: similarity == 0.625"+" & numMatches == 12 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n");
				assertTrue(res.getSimValue() == 0.625) ;
				assertTrue(res.getNumMatchComponents() == 12) ;
				assertTrue(res.getDatasetSchema().contains(target)) ;
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail() ;
			}
		}
	}
	
	@Test
	public void test1_5_7(){
		System.out.println("\nRunning test 1.5.7");
		
		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="conference(paper(title,document(category(day,month,year),writer(name(first,second)))))";
		
		results = methodCaller.callSPSM(results,source,target);
		
		fOut.write("Test 1.5.7\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
	
		if(results==null){
			fOut.write("Null Results! \n\n");
			fail();
		}else{
			assertTrue(results.size() == 1) ;
			if(results.size()>0){
				MatchStruc res = results.get(0);
				
				fOut.write("Expected Result: similarity == 0.45833333333333337"+" & numMatches == 8 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n");
			
				assertTrue((res.getSimValue() < 0.46) && (res.getSimValue() > 0.458)) ;
				assertTrue(res.getNumMatchComponents() == 8) ;
				assertTrue(res.getDatasetSchema().contains(target)) ;
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail();
			}
		}
	}
	
	@Test
	public void test1_5_8(){
		System.out.println("\nRunning test 1.5.8");
		
		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="event(paper(title,document(category(day,month,year),writer(name(first,second)))))";
		
		results = methodCaller.callSPSM(results,source,target);
		
		fOut.write("Test 1.5.8\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");		
	
		if(results==null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{			
			fOut.write("Expected Result: results.size() == 0 \n");
			fOut.write("Actual Result: results.size() == "+results.size()+"\n\n");
			assertTrue(results.size() == 0) ;
		}
	}
	
	@Test
	public void test1_6_1(){
		System.out.println("\nRunning test 1.6.1");
		
		source="conferenceDocument(nameOfAuthor)";
		target="conferenceReview(authorName)";
		
		results = methodCaller.callSPSM(results,source,target);
		
		fOut.write("Test 1.6.1\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
				
		if(results==null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			assertTrue(results.size() == 1) ;
			if(results.size()>0){
				MatchStruc res = results.get(0);
				
				assertTrue(res.getSimValue() == 0.5) ;
				assertTrue(res.getNumMatchComponents() == 2) ;
				assertTrue(res.getDatasetSchema().contains(target)) ;
				
				fOut.write("Expected Result: similarity == 0.5"+" & numMatches ==2 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail() ;
			}
		}
	}
	
	@Test 
	public void test1_6_2(){
		System.out.println("\nRunning test 1.6.2");
		
		source="conference_document(name_of_author)";
		target="conference_review(author_name)";
		
		results = methodCaller.callSPSM(results, source, target);
		
		fOut.write("Test 1.6.2\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
				
		if(results==null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			assertTrue(results.size() == 1) ;
			if(results.size()>0){
				MatchStruc res = results.get(0);
				
				fOut.write("Expected Result: similarity == 0.5"+" & numMatches == 2 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n");
				assertTrue(res.getSimValue() == 0.5) ;
				assertTrue(res.getNumMatchComponents() == 2) ;
				assertTrue(res.getDatasetSchema().contains(target)) ;
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail() ;
			}
			
		}
	}
	
	@Test
	public void test1_6_3(){
		System.out.println("\nRunning test 1.6.3");
		
		source="conference_document(name_of_author)";
		target="ConferenceReview(authorName)";
		
		results = methodCaller.callSPSM(results, source, target);
		
		fOut.write("Test 1.6.3\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
			
		if(results==null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			assertTrue(results.size() == 1) ;
			if(results.size()>0){
				MatchStruc res = results.get(0);
				
				fOut.write("Expected Result: similarity == 0.5"+" & numMatches == 2 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n");
				assertTrue(res.getSimValue() == 0.5) ;
				assertTrue(res.getNumMatchComponents() == 2) ;
				assertTrue(res.getDatasetSchema().contains(target)) ;
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail() ;
			}
		}
	}
	
	@Test
	public void test1_6_4(){
		System.out.println("\nRunning test 1.6.4");
		
		source="conference document(name of author)";
		target="conference review(author name)";
		
		results = methodCaller.callSPSM(results,source,target);
		
		fOut.write("Test 1.6.4\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
				
		if(results==null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			assertTrue(results.size() == 1) ;
			if(results.size()>0){
				MatchStruc res = results.get(0);
				
				fOut.write("Expected Result: similarity == 0.5"+" & numMatches == 2 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n");
				assertTrue(res.getSimValue() == 0.5) ;
				assertTrue(res.getNumMatchComponents() == 2) ;
				assertTrue(res.getDatasetSchema().contains(target)) ;
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail() ;
			}
		}
	}
	
	@Test
	public void test1_6_5(){
		System.out.println("\nRunning test 1.6.5");
		
		source="conference document(nameOfAuthor)";
		target="conference review(authorName)";
		
		results = methodCaller.callSPSM(results,source,target);
	
		fOut.write("Test 1.6.5\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			assertTrue(results.size() == 1) ;
			if(results.size()>0){
				MatchStruc res = results.get(0);
				
				fOut.write("Expected Result: similarity == 0.5"+" & numMatches == 2 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n");
				assertTrue(res.getSimValue() == 0.5) ;
				assertTrue(res.getNumMatchComponents() == 2) ;
				assertTrue(res.getDatasetSchema().contains(target)) ;
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail();
			}
		}
	}
	
	@Test
	public void test1_6_6(){
		System.out.println("\nRunning test 1.6.6");
		
		source="conferencedocument(nameofauthor)";
		target="conference review(authorname)";
		
		results = methodCaller.callSPSM(results, source, target);
		
		fOut.write("Test 1.6.6\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
			fail() ;
		}else{
			if(results.size() >0){
				assertTrue(results.size() == 1) ;
				MatchStruc res = results.get(0);
				
				fOut.write("Expected Result: similarity == 0.5"+" & numMatches == 2 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n");
				assertTrue(res.getSimValue() == 0.5) ;
				assertTrue(res.getNumMatchComponents() == 2) ;
				assertTrue(res.getDatasetSchema().contains(target)) ;
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
				fail() ;
			}
		}
	}
	
	@Test
	public void test1_6_8(){
		System.out.println("\nRunning test 1.6.8");
		
		source="auto(brand,name,color)";
		target="car(year,brand,colour)";
		
		results = methodCaller.callSPSM(results, source, target);
		
		fOut.write("Test 1.6.7\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
			fail();
		}else{
			assertTrue(results.size() == 1) ;
			MatchStruc res = results.get(0);
			
			assertTrue(res.getSimValue() == 0.75) ;
			assertTrue(res.getNumMatchComponents() == 3) ;
			assertTrue(res.getDatasetSchema().contains(target)) ;
			
			// System.out.println("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatchComponents()+"\n\n");
			
			fOut.write("Expected Result: results.size() == 1 \n");
			fOut.write("Actual Result: results.size() == "+results.size()+"\n\n"); 
			
		}
	}
	
	
	@After
	public void cleanUp(){
		fOut.close();
	}

}







