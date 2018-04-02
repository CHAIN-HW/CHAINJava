package chain_tests.sparql;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import chain.sparql.JWICaller;
import chain.sparql.StringParser;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import edu.mit.jwi.IDictionary;

/* Author Diana Bental
 * Date November 2017
 * Modified
 */

/*
 * Responsible for testing JWICaller.java
 * which uses JWI to call WordNet
 * 
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class JWICallerTestCases extends BaseTest {
	private JWICaller JWIcaller ;
	private IDictionary dict ;
	
	private ArrayList<String> words = new ArrayList<String>() ;	
	private Set <String> associatedWords = new HashSet <String>() ;
	
	//for writing results
	private static File testRes;
	private PrintWriter fOut;
	private static boolean alreadyWritten;
	
	@BeforeClass
	public static void beforeAll(){
		System.out.println("These tests are responsible for testing JWICaller.java to ensure that\n"
				+"the JWI caller correctly splits a camel-case or similar word sequence into separate one-word components\n"
				+"and then calls JWI to get the associated words from WordNet.");
		System.out.println("\nThe results from these tests can be found in outputs/testing/JWI_Caller_Test.txt\n");

		alreadyWritten = false;
		try{
			testRes = new File("outputs/testing/JWI_Caller_Test.txt");
			testRes.createNewFile();
			
			new PrintWriter("outputs/testing/JWI_Caller_Test.txt").close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	
	@Before
	public void setup(){
		
		associatedWords.clear() ;
		
		try{
			fOut = new PrintWriter(new FileWriter(testRes,true));
			
			if(alreadyWritten==false){
				fOut.write("Testing Results for JWICallerTestCases.java\n\n");
				alreadyWritten = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Failed to open test results file.") ; 
		}
		
		try {dict = JWIcaller.openDictionary () ;
		
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Failed to open dictinary.") ;
		}
	}
	

	@Test
	public void test_jwi_01() {
		String testNum = "01" ;
		String w = "waterBodyMeasures" ;
		int expected = 559 ;
		
		System.out.println("\nRunning test JWI "+testNum+ " : " +w);
		fOut.write("\nTest JWI " +testNum+"\n");
		fOut.write(w+"\n");
		
		words = StringParser.splitCamelCase(w) ;
		
		for(String word: words) {
			associatedWords = JWICaller.getAssociatedWords(dict, word, associatedWords) ;
		}
		System.out.println(associatedWords) ;
		System.out.println(associatedWords.size()) ; 
		fOut.write("associatedWords:"+associatedWords+"\n") ;
		fOut.write("Word count:"+associatedWords.size()+" (Expected: "+expected+")\n") ;
		assertEquals(associatedWords.size(), expected) ;
	}
	
	@Test
	public void test_jwi_02() {
		
		String testNum = "02" ;
		String w = "waterBodyPressures" ;
		int expected = 428 ;
		
		System.out.println("\nRunning test JWI "+testNum+ " : " +w);
		fOut.write("\nTest JWI " +testNum+"\n");
		fOut.write(w+"\n");
		
		words = StringParser.splitCamelCase(w) ;	
		
		for(String word: words) {
			associatedWords = JWICaller.getAssociatedWords(dict, word, associatedWords) ;
		}
		System.out.println(associatedWords) ;
		System.out.println(associatedWords.size()) ; 
		fOut.write("associatedWords:"+associatedWords+"\n") ;
		fOut.write("Word count:"+associatedWords.size()+" (Expected: "+expected+")\n") ;
		assertEquals(associatedWords.size(), expected) ;
	}
	
	@Test
	public void test_jwi_03() {
		
		// test on two strings in sequence
		
		String testNum = "03" ;
		String w = "conferenceDocument" ;
		int expected = 122 ;

		System.out.println("\nRunning test JWI "+testNum+ " Part 1 : " +w);
		fOut.write("\nTest JWI " +testNum+" Part 1\n");
		fOut.write(w+"\n");
		
		words = StringParser.splitCamelCase(w) ;	
		
		for(String word: words) {
			associatedWords = JWICaller.getAssociatedWords(dict, word, associatedWords) ;
		}
		System.out.println(associatedWords) ;
		System.out.println(associatedWords.size()) ; 
		
		fOut.write("associatedWords:"+associatedWords+"\n") ;
		fOut.write("Word count:"+associatedWords.size()+" (Expected: "+expected+")\n") ;
		assertEquals(associatedWords.size(), expected) ;

		w = "conferenceReview" ;
		expected = 201; // the associated words are incremented
		
		System.out.println("\nPart 2 with: " +w);
		fOut.write("\nPart 2 \n");
		fOut.write(w+"\n");
		
		words = StringParser.splitCamelCase(w) ;	
		
		for(String word: words) {
			associatedWords = JWICaller.getAssociatedWords(dict, word, associatedWords) ;
		}
		System.out.println(associatedWords) ;
		System.out.println(associatedWords.size()) ; 
		fOut.write("associatedWords:"+associatedWords+"\n") ;
		fOut.write("Word count:"+associatedWords.size()+" (Expected: "+expected+")\n") ;
		assertEquals(associatedWords.size(), expected) ;
	}
	
	@Test
	public void test_jwi_04() {
		
		// Test on empty string
		String testNum = "04" ;
		String w = "" ;
		
		System.out.println("\nRunning test JWI "+testNum+ " : " +w);
		fOut.write("\nTest JWI " +testNum+"\n");
		fOut.write(w+"\n");
		
		// splitCamelCase run on an empty string gives one empty string
		words = StringParser.splitCamelCase(w) ;
		assertEquals(words.size(), 1) ;
		assertTrue(words.toArray()[0].equals("")) ;
		
		
		try {
			for(String word: words) {
				associatedWords = JWICaller.getAssociatedWords(dict, word, associatedWords) ;
			}
			fail("Should get an exception from a null word") ;
			
		} catch (Exception e){
			System.out.println("We have an exception (as expected) from a null word");
			fOut.write("We have an exception (as expected) from a null word\n");
		}	

	}
	
	@After
	public void cleanUp(){
		fOut.close();
	}

}
