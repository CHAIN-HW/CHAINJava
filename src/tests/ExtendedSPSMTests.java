package tests;

/*
 * Author Diana Bental
 * Date February 2018
 */

/* trying out SPSM with some example schemas
 * Focussing on nested schemas
 * and property paths
 * 
 */

import static org.junit.Assert.*;

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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class ExtendedSPSMTests {
	private Call_SPSM methodCaller;
	private ArrayList<Match_Struc> results;
	private String source,target;
	
	//for writing results
	private static File testRes;
	private PrintWriter fOut;
	private static boolean alreadyWritten;
	
	@BeforeClass
	public static void beforeAll(){
		System.out.println("These tests look at the results of calls to Call_SPSM.java using nested \n"
				+ "schema structures.\n"
				+"Some of them are nested using the predicate(args) notation, others / notation (ontology?)\n"
				+ "and others are a mix.\n");
		System.out.println("\nThe results from these tests can be found in outputs/testing/Extended_SPSM_Tests.txt\n");
		
		alreadyWritten = false;
		try{
			testRes = new File("outputs/testing/Extended_SPSM_Tests.txt");
			testRes.createNewFile();
			
			new PrintWriter("outputs/testing/Extended_SPSM_Tests.txt").close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Before
	public void setup(){
		methodCaller = new Call_SPSM();
		results = new ArrayList<Match_Struc>();
		
		try{
			fOut = new PrintWriter(new FileWriter(testRes,true));
			
			if(alreadyWritten==false){
				fOut.write("Testing Results for ExtendedSPSMTests.java\n\n");
				alreadyWritten = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void test_01() {
		
		results = test("01", "", "");
				
		assertTrue(results.size() == 0) ;
	
	}
	
	@Test
	// The example from the original SPSM paper
	// This works almost as described in the paper but Number_of_bottles and
	// Quantity do not match correctly.
	public void test_01a() {
		
		results = test("01a",
				"get_wine(Region, Country, Color, Price, Number_of_bottles)", 
				"get_wine(Region(Country, Area), Colour, Cost, Year, Quantity)");
				
		assertTrue(results.size() == 1) ;
	
		checkEquality(results.get(0).getSimValue(), 0.5) ;
	}
	
	@Test
	// A variation of the SPSM query, which works fully
	public void test_01b() {
		
		results = test("01b",
				"get_wine(Region, Country, Color, Price, Amount)", 
				"get_wine(Region(Country, Area), Colour, Cost, Year, Quantity)");
				
		assertTrue(results.size() == 1) ;
	
		checkEquality(results.get(0).getSimValue(), 0.67) ;
	}
	
	@Test
	// A variation of the SPSM query, which works fully
	public void test_01c() {
		
		results = test("01c",
				"get_wine(Region, Country, Color, Price, Year, Amount)", 
				"get_wine(Region(Country, Area), Colour, Cost, Quantity)");
				
		assertTrue(results.size() == 1) ;
	
		checkEquality(results.get(0).getSimValue(), 0.57) ;
	}
	
	@Test
	public void test_02() {
		
		results = test("02", "?x foaf:mbox <mailto:alice@example>", "?x foaf:mbox <mailto:alice@example>");
				
		assertTrue(results.size() == 1) ;
	
		checkEquality(results.get(0).getSimValue(), 1.0) ;
	}
	


	@Test
	public void test_03() {
		
		results = test("03", "?x(foaf:mbox)", "?x(foaf:mbox)");
				
		assertTrue(results.size() == 1) ;
		checkEquality(results.get(0).getSimValue(), 1.0) ;
	}
	
	@Test
	public void test_04() {
		
		results = test("04", "?x(foaf:mbox, foaf:knows)", "?x(foaf:mbox, foaf:knows)");
				
		assertTrue(results.size() == 1) ;
		checkEquality(results.get(0).getSimValue(), 1.0) ;
	}
	
	@Test
	public void test_05a() {
		
		results = test("05a", "?x(foaf:mbox, foaf:knows/foaf:knows)", "?x(foaf:mbox, foaf:knows)");
				
		assertTrue(results.size() == 1) ;
		// Nesting foaf:knows using / gives an exact match though the source depth is greater
		checkEquality(results.get(0).getSimValue(), 1.0) ;
	}
	
	@Test
	public void test_05b() {
		
		results = test("05b", "?x(foaf:mbox, foaf:knows(foaf:knows))", "?x(foaf:mbox, foaf:knows)");
				
		assertTrue(results.size() == 1) ;
		// nesting foaf:knows using () reduces the similarity when the source depth is greater
		checkEquality(results.get(0).getSimValue(), 0.5) ;
	}
	
	
	@Test
	public void test_06a() {
		
		results = test("06a", "?x(foaf:mbox, foaf:knows/foaf:knows/foaf:name)", "?x(foaf:mbox, foaf:knows/foaf:name)");
				
		assertTrue(results.size() == 1) ;
		// Nesting foaf:knows using / gives an exact match though the source depth is greater
		checkEquality(results.get(0).getSimValue(), 1.0) ;
	}
	
	@Test
	public void test_06b() {
		
		results = test("06b", "?x(foaf:mbox, foaf:knows(foaf:knows(foaf:name)))", "?x(foaf:mbox, foaf:knows(foaf:name))");
				
		assertTrue(results.size() == 1) ;
		// nesting foaf:knows using () reduces the similarity when the source depth is greater
		checkEquality(results.get(0).getSimValue(), 0.6) ;
	}
	
	@Test
	public void test_06c() {
		
		results = test("06c", "?x(foaf:mbox, foaf:knows/foaf:knows/foaf:name)", "?x(foaf:mbox, foaf:knows/foaf:age)");
				
		assertTrue(results.size() == 1) ;
		// Using / with a different terminating term does not seem to recognise  foaf:knows at all
		// Only matches ?x and ?mbox
		checkEquality(results.get(0).getSimValue(), 0.67) ;
	}
	
	@Test
	public void test_06d() {
		
		results = test("06d", "?x(foaf:mbox, foaf:knows(foaf:knows(foaf:name)))", "?x(foaf:mbox, foaf:knows(foaf:age))");
				
		assertTrue(results.size() == 1) ;
		// Using () with a different terminating term recognises  foaf:knows
		// Matches ?x  ?mbox and ?knows
		checkEquality(results.get(0).getSimValue(), 0.6) ;
	}
	
	@Test
	public void test_06e() {
		
		results = test("06e", "?x(foaf:mbox, foaf:knows/foaf:knows/foaf:name)", "?x(foaf:mbox, foaf:knows/foaf:knows/foaf:age)");
				
		assertTrue(results.size() == 1) ;
		// Using / with a different terminating term at the same depth does not seem to recognise  foaf:knows at all
		// Only matches ?x and ?mbox
		checkEquality(results.get(0).getSimValue(), 0.67) ;
	}
	
	@Test
	public void test_06f() {
		
		results = test("06f", "?x(foaf:mbox, foaf:knows(foaf:knows(foaf:name)))", "?x(foaf:mbox, foaf:knows(foaf:knows(foaf:age))");
				
		assertTrue(results.size() == 1) ;
		// Using () with a different terminating term  
		// matches ?x  ?mbox ?knows  ?name < ag
		checkEquality(results.get(0).getSimValue(), 0.9) ;
	}
	
	
	@Test
	public void test_07a() {
		
		results = test("07a", "?x(foaf:mbox, foaf:knows(foaf:knows))", "?x(foaf:mbox, foaf:knows/foaf:knows)" );
				
		assertTrue(results.size() == 1) ;
		// Using two different nesting methods reduces the similarity though the depth is the same
		// Only matches ?x and ?mbox
		checkEquality(results.get(0).getSimValue(), 0.5) ;
	}
	
	@Test
	public void test_07b() {
		
		results = test("07b", "?x(foaf:mbox, foaf:knows(foaf:knows(foaf:name)))", "?x(foaf:mbox, foaf:knows/foaf:knows/foaf:age)");
				
		assertTrue(results.size() == 1) ;
		// Using / with a different terminating term does not seem to recognise  foaf:knows at all
		// Only matches ?x and ?mbox
		checkEquality(results.get(0).getSimValue(), 0.4) ;
	}
	
	@Test
	public void test_08a() {
		
		results = test("08a", "?x(foaf:mbox, foaf:knows/foaf:knows(foaf:name))", "?x(foaf:mbox, foaf:knows(foaf:name)");
				
		assertTrue(results.size() == 1) ;
		// Combining / with () 
		// Matches ?x and ?mbox; recognises foaf:knows/foaf:knows as equivalent to foaf:knows
		// but doesn't recognise the nested foaf:name term.
		checkEquality(results.get(0).getSimValue(), 0.75) ;
	}
	
	@Test
	public void test_08b() {
		
		results = test("08b", "?x(foaf:mbox, foaf:knows/foaf:knows(foaf:name))", "?x(foaf:mbox, foaf:knows(foaf:age)");
				
		assertTrue(results.size() == 1) ;
		// Combining / with () 
		// Matches ?x and ?mbox; recognises foaf:knows/foaf:knows as equivalent to foaf:knows
		// but doesn't match the nested foaf:name term to foaf:age
		// What is weird is that the similarity vlaue is HIGHER than the previous test.
		checkEquality(results.get(0).getSimValue(), 0.875) ;
	}
	
	@Test
	public void test_08c() {
		
		results = test("08c", "?x(foaf:mbox, foaf:knows/foaf:knows/foaf:knows/foaf:knows/foaf:knows(foaf:name))", "?x(foaf:mbox, foaf:knows(foaf:name)");
				
		assertTrue(results.size() == 1) ;
		// Combining / with () 
		// Matches ?x and ?mbox; recognises foaf:knows/.../foaf:knows as equivalent to foaf:knows
		// but doesn't match the nested foaf:name term 
		checkEquality(results.get(0).getSimValue(), 0.75) ;
	}
	
	
	@Test
	public void test_08d() {
		
		results = test("08d", "?x(foaf:mbox, foaf:knows/foaf:knows/foaf:knows/foaf:knows/foaf:knows/foaf:name(fred))", "?x(foaf:mbox, foaf:knows/foaf:name(fred)");
				
		assertTrue(results.size() == 1) ;
		// Combining / with () 
		// Matches ?x and ?mbox; recognises foaf:knows/foaf:knows/foaf:name as equivalent to foaf:knows/foaf:name
		// but doesn't match the nested fred term 
		checkEquality(results.get(0).getSimValue(), 0.75) ;
	}
	
	@Test
	public void test_08e() {
		
		results = test("08e", "?x(foaf:mbox, foaf:knows/foaf:knows/foaf:knows/foaf:knows/foaf:knows/foaf:name(fred))", "?x(foaf:mbox, foaf:knows/foaf:age(fred)");
				
		assertTrue(results.size() == 1) ;
		// Combining / with () 
		// Only matches ?x and ?mbox
		checkEquality(results.get(0).getSimValue(), 0.5) ;
	}
	
	@Test
	public void test_08f() {
		
		results = test("08f", "?x(foaf:mbox, foaf:knows/foaf:knows/foaf:friend/foaf:knows/foaf:knows/foaf:name(fred))", "?x(foaf:mbox, foaf:knows/foaf:name(fred)");
				
		assertTrue(results.size() == 1) ;
		// Combining / with () 
		// Matches ?x and ?mbox, matches foaf/knows.../foaf:friend/foaf:knows/foaf:name < foaf:knows/foaf:name
		
		checkEquality(results.get(0).getSimValue(), 0.625) ;
	}
	
	@Test
	public void test_08g() {
		
		results = test("08g", "?x(foaf:mbox, foaf:knows/foaf:knows/foaf:friend/foaf:knows/foaf:knows/foaf:name(person(fred, george)))", "?x(foaf:mbox, foaf:knows/foaf:name(person(fred, andy))");
				
		assertTrue(results.size() == 1) ;
		// Combining / with () 
		// Matches ?x and ?mbox, matches foaf/knows.../foaf:friend/foaf:knows/foaf:name < foaf:knows/foaf:name
		// foaf/knows.../foaf:friend/foaf:knows/foaf:name(person) < foaf:knows/foaf:name(person)
		// foaf/knows.../foaf:friend/foaf:knows/foaf:name(person, fred) < foaf:knows/foaf:name(person, fred)
		
		checkEquality(results.get(0).getSimValue(), 0.583) ;
	}
	
	private ArrayList<Match_Struc> test(String testId, String source, String target) {
		
		fOut.println("\nTest " + testId + "\n" ) ;
		System.out.println("\nTest " + testId + "\n" ) ;
		fOut.println("Calling SPSM with source "+source + " & target(s)  " +target);
		System.out.println("Calling SPSM with source "+source + " & target(s)  " +target); ;
		
		results = methodCaller.callSPSM(results, source, target);
		
		if(results == null){
			fOut.println("Null Results. \n");
			System.out.println("Null Results. \n");
		}else{
			fOut.println(results+"\n");
			System.out.println(results+"\n");
		}
		
		return results;
			
	}
	
	private void checkEquality(double actual, double expected) {

		assertTrue( actual > expected - 0.01) ;
		assertTrue( actual < expected + 0.01) ;
	}
	
	@After
	public void cleanUp(){
		fOut.close();
	}

}
