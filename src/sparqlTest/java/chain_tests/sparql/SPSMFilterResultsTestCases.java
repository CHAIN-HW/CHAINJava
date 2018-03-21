package chain_tests.sparql;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import chain.sparql.BestMatchResults;
import chain.sparql.CallSPSM;
import chain.sparql.MatchStruc;
import org.junit.*;

/* Author Tanya Howden
 * Author Diana Bental
 * Date September 2017
 * Date December 2017
 * Modified
 * add assertions
 */

/*
 * Responsible for testing the whole of task 1 which involves
 * methods from both CallSPSM.java and BestMatchResults.java
 * to make sure that we get results from SPSM that have been filtered
 * and sorted
 */
public class SPSMFilterResultsTestCases extends BaseTest {

	private CallSPSM spsmCall;
	private BestMatchResults filterResCall;
	private ArrayList<MatchStruc> finalRes;
	private String target, source;
	private static int counter;
	
	//for writing results
	private static File testRes;
	private PrintWriter fOut;
	private static boolean alreadyWritten;
	
	@BeforeClass
	public static void beforeAll(){
		System.out.println("These tests are responsible for testing the output results from CallSPSM.java & BestMatchResults.java\n"
				+ "which makes sure that we get results returned from SPSM that have then been filtered and sorted by CHAIn.");
		System.out.println("\nThe results from these tests can be found in outputs/testing/Task_1_Tests.txt\n");
		
		alreadyWritten = false;
		counter=1;
		try{
			testRes = new File("outputs/testing/Task_1_Tests.txt");
			testRes.createNewFile();
			
			new PrintWriter("outputs/testing/Task_1_Tests.txt").close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		
	@Before
	public void setup(){
		spsmCall = new CallSPSM();
		filterResCall = new BestMatchResults();
		
		try{
			fOut = new PrintWriter(new FileWriter(testRes,true));
			
			if(alreadyWritten==false){
				fOut.write("Testing Results for SPSMFilterResultsTestCases.java\n\n");
				alreadyWritten = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void successMultiCall(){
		System.out.println("\nRunning test successMultiCall");
		
		source = "author(name)";
		target = "author(name) ; document(title,author) ; paperWriter(firstname,surname,paper) ; reviewAuthor(firstname,lastname,review)";
		finalRes = new ArrayList<MatchStruc>();
		
		finalRes= spsmCall.callSPSM(finalRes, source, target);
		
		assertEquals(3,finalRes.size());

		finalRes = filterResCall.getThresholdAndFilter(finalRes, 0.6, 0);
		
		fOut.write("Test 3."+counter+" - success multi call\n");
		fOut.write("Calling SPSM with source, "+source+" & target, "+target+"\n");
		fOut.write("Calling with threshold: "+0.6+" & limit: "+0+" \n");
		
		fOut.write("Expected Result: results.size() == 2 \n");
		
		if(finalRes!=null){
			fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n\n");
		}else{
			fOut.write("Null Results! \n\n");
		}
		
		assertEquals(2,finalRes.size());
		
		counter++;
	}
	
	@Test
	public void successSingleCall(){
		System.out.println("\nRunning test successSingleCall");
		
		source="author(name)";
		target="author(name)";
		finalRes = new ArrayList<MatchStruc>();
		
		finalRes=spsmCall.callSPSM(finalRes, source, target);
		
		assertEquals(1,finalRes.size());

		finalRes = filterResCall.getThresholdAndFilter(finalRes, 0.6, 0);
		
		fOut.write("Test 3."+counter+" - success single call\n");
		fOut.write("Calling SPSM with source, "+source+" & target, "+target+"\n");
		fOut.write("Calling with threshold: "+0.6+" & limit: "+0+" \n");
		
		fOut.write("Expected Result: results.size() == 1 \n");
		
		if(finalRes!=null){
			fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n\n");			
		}else{
			fOut.write("Null Results! \n\n");
		}
		
		assertEquals(1,finalRes.size());
		
		counter++;
	}
	
	@Test
	public void failSingleCall(){
		System.out.println("\nRunning test failSingleCall");
		
		source="author(name)";
		target="document(title,author)";
		finalRes = new ArrayList<MatchStruc>();
		
		finalRes=spsmCall.callSPSM(finalRes, source, target);
		
		assertEquals(0,finalRes.size());
		
		finalRes = filterResCall.getThresholdAndFilter(finalRes, 0.0, 0);
		
		fOut.write("Test 3."+counter+" - fail single call\n");
		fOut.write("Calling SPSM with source, "+source+" & target, "+target+"\n");
		fOut.write("Calling with threshold: "+0.0+" & limit: "+0+" \n");
		
		fOut.write("Expected Result: results.size() == 0 \n");

		if(finalRes!=null){
			fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n\n");			
		}else{

		}
		assertEquals(0,finalRes.size());
		
		counter++;
	}
	
	@Test
	public void successWithLimit(){
		System.out.println("\nRunning test successWithLimit");
		
		source = "author(name)";
		target = "author(name) ; document(title,author) ; paperWriter(firstname,surname,paper) ; reviewAuthor(firstname,lastname,review)";
		finalRes = new ArrayList<MatchStruc>();
		
		finalRes=spsmCall.callSPSM(finalRes, source, target);
		
		assertEquals(3,finalRes.size());
		
		finalRes = filterResCall.getThresholdAndFilter(finalRes, 0.0, 2);
		
		fOut.write("Test 3."+counter+" - success with limit\n");
		fOut.write("Calling SPSM with source, "+source+" & target, "+target+"\n");
		fOut.write("Calling with threshold: "+0.0+" & limit: "+2+" \n");
		
		fOut.write("Expected Result: results.size() == 2 \n");
		
		if(finalRes!=null){
			fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n\n");	
		}else{
			fOut.write("Null Results! \n\n");
		}
		
		assertEquals(2,finalRes.size());
		
		counter++;
	}
	
	@Test
	public void failWithLimit(){
		System.out.println("\nRunning test failWithLimit");
		
		source="author(name)";
		target="document(title,author)";
		finalRes = new ArrayList<MatchStruc>();
		
		finalRes=spsmCall.callSPSM(finalRes, source, target);
		
		assertEquals(0,finalRes.size());
		
		finalRes = filterResCall.getThresholdAndFilter(finalRes,0.0,2);
		
		fOut.write("Test 3."+counter+" - fail with limit\n");
		fOut.write("Calling SPSM with source, "+source+" & target, "+target+"\n");
		fOut.write("Calling with threshold: "+0.0+" & limit: "+2+" \n");
		
		fOut.write("Expected Result: results.size() == 0 \n");

		if(finalRes!=null){
			fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n\n");			
		}else{
			fOut.write("Null Results! \n\n");
		}
		
		assertEquals(0,finalRes.size());
		counter++;
	}
	
	@After
	public void cleanUp(){
		fOut.close();
	}

}
