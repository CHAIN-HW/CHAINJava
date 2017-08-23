import java.io.*;

import java.util.*;
import org.junit.*;
import org.junit.runners.MethodSorters;

/*
 * Responsible for testing the output results from
 * Call_SPSM.java which takes in source and target
 * schemas and then calls SPSM before reading in
 * results as serialised object
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SPSM_Test_Cases {
	private Call_SPSM methodCaller;
	private ArrayList<Match_Struc> results;
	private String source,target;
	
	//for writing results
	private static File testRes;
	private PrintWriter fOut;
	private static boolean alreadyWritten;
	
	@BeforeClass
	public static void beforeAll(){
		System.out.println("These tests are responsible for testing the output results from Call_SPSM.java which takes in a source"
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
		methodCaller = new Call_SPSM();
		results = new ArrayList<Match_Struc>();
		
		try{
			fOut = new PrintWriter(new FileWriter(testRes,true));
			
			if(alreadyWritten==false){
				fOut.write("Testing Results for SPSM_Test_Cases.java\n\n");
				alreadyWritten = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	//no source or target
	public void test11() {
		System.out.println("\nRunning test 1.1.1");
		
		source=""; 
		target="";
		
		results = methodCaller.getSchemas(results, source, target);
		
		fOut.write("Test 1.1.1\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results == null){
			fOut.write("Null Results! \n\n");
		}else{
			fOut.write("Expected Result: results.size() == 0 \n");
			fOut.write("Actual Result: results.size() == "+results.size()+"\n\n");
		}
	}
	
	@Test
	//no target but one source
	public void test21(){
		System.out.println("\nRunning test 1.2.1");
		
		source="author(name)";
		target="";
		
		results = methodCaller.getSchemas(results,source,target);
		
		fOut.write("Test 1.2.1\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results == null){
			fOut.write("Null Results! \n\n");
		}else{
			fOut.write("Expected Result: results.size() == 0 \n");
			fOut.write("Actual Result: results.size() == "+results.size()+"\n\n"); 
		}
	}
	
	@Test
	//no source but one target
	public void test22(){
		System.out.println("\nRunning test 1.2.2");
		
		source="";
		target="author(name)";
		
		results = methodCaller.getSchemas(results,source,target);
		
		fOut.write("Test 1.2.2\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results == null){
			fOut.write("Null Results! \n\n");
		}else{
			
			fOut.write("Expected Result: results.size() == 0 \n");
			fOut.write("Actual Result: results.size() == "+results.size()+"\n\n"); 
		}
	}
	
	@Test
	//one source and one target
	public void test23(){
		System.out.println("\nRunning test 1.2.3");
		
		source="author(name)";
		target="author(name)";
		
		results = methodCaller.getSchemas(results,source,target);
		
		fOut.write("Test 1.2.3\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		fOut.write("Expected Result: similarity == 1.0"+" & numMatches == 2 \n");
		
		if(results == null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size() > 0){
				Match_Struc res = results.get(0);
				
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatches()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}			 
		}
	}
	
	@Test
	//one source w multiple targets
	public void test24(){
		System.out.println("\nRunning test 1.2.4");
		
		source="author(name)";
		target="document(title,author) ; author(name,document) ; reviewAuthor(firstname,lastname,review)";
		
		results = methodCaller.getSchemas(results, source, target);
		
		int[] answer = {2,2};
		double[] simValues = {1.0,0.75};
		
		fOut.write("Test 1.2.4\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size() > 0){
				for(int i = 0 ; i < results.size() ; i++){
					Match_Struc currRes = results.get(i);
					
					fOut.write("Target: "+currRes.getDatasetSchema()+"\n");
					fOut.write("Expected Result: similarity == "+simValues[i]+" & numMatches == "+answer[i]+"\n");
					fOut.write("Actual Result: similarity == "+currRes.getSimValue()+" & numMatches == "+currRes.getNumMatches()+"\n\n"); 
				}
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}
		}
	}
	
	@Test
	//one source w two targets
	public void test31(){
		System.out.println("\nRunning test 1.3.1");
		
		source="author(name)";
		target="document(title,author) ; conferenceMember(name)";
		
		results = methodCaller.getSchemas(results,source,target);
		
		fOut.write("Test 1.3.1\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{
			fOut.write("Expected Result: results.size() == 0 \n");
			fOut.write("Actual Result: results.size() == "+results.size()+"\n\n");
		}
	}
	
	@Test
	public void test32(){
		System.out.println("\nRunning test 1.3.2");
		
		source="author(name)";
		target="author(name) ; document(title,author)";
		
		results = methodCaller.getSchemas(results, source, target);
		
		fOut.write("Test 1.3.2\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size() > 0){
				Match_Struc currRes = results.get(0);
				
				fOut.write("Target: "+currRes.getDatasetSchema()+"\n");
				fOut.write("Expected Result: similarity == 1.0"+" & numMatches == 2 \n");
				fOut.write("Actual Result: similarity == "+currRes.getSimValue()+" & numMatches == "+currRes.getNumMatches()+"\n\n"); 
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}
		}
	}
	
	@Test
	public void test33(){
		System.out.println("\nRunning test 1.3.3");
		
		source="author(name)";
		target="author(name) ; document(title,author) ; paperWriter(firstname,surname,paper) ; reviewAuthor(firstname,lastname,review)";
		
		results = methodCaller.getSchemas(results,source,target);
		
		fOut.write("Test 1.3.3\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		int[] answer={2,2,2};
		double[] simVals = {1.0,0.5,0.75};
		
		if(results == null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size()>0){
				for(int i = 0 ; i < results.size() ; i++){
					Match_Struc currRes = results.get(i);
					
					fOut.write("Target: "+currRes.getDatasetSchema()+"\n");
					fOut.write("Expected Result: similarity == "+simVals[i]+" & numMatches == "+answer[i]+"\n");
					fOut.write("Actual Result: similarity == "+currRes.getSimValue()+" & numMatches == "+currRes.getNumMatches()+"\n\n");
				}
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}
		}
	}
	
	@Test
	public void test41(){
		System.out.println("\nRunning test 1.4.1");
		
		source="author";
		target="writer";
		
		results = methodCaller.getSchemas(results,source,target);
		
		fOut.write("Test 1.4.1\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
	
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size()>0){
				Match_Struc res = results.get(0);
				
				fOut.write("Expected Result: similarity == 1.0"+" & numMatches == 1 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatches()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}
		}
	}
	
	@Test
	public void test42(){
		System.out.println("\nRunning test 1.4.2");
		
		source="author";
		target="document";
		
		results = methodCaller.getSchemas(results, source, target);
		
		fOut.write("Test 1.4.2\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results == null){
			fOut.write("Null Results! \n\n");
		}else{			
			fOut.write("Expected Result: results.size() == 0 \n");
			fOut.write("Actual Result: results.size() == "+results.size()+"\n\n");
		}
	}
	
	@Test
	public void test43(){
		System.out.println("\nRunning test 1.4.3");
		
		source="author(name)";
		target="document(name)";
		
		results = methodCaller.getSchemas(results, source, target);
		
		fOut.write("Test 1.4.3\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results == null){
			fOut.write("Null Results! \n\n");
		}else{
			fOut.write("Expected Result: results.size() == 0 \n");
			fOut.write("Actual Result: results.size() == "+results.size()+"\n\n"); 
		}
	}
	
	@Test
	public void test44(){
		System.out.println("\nRunning test 1.4.4");
		
		source="author(name)";
		target="reviewWriter(review,name)";
		
		results = methodCaller.getSchemas(results, source, target);
		
		fOut.write("Test 1.4.4\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results == null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size() > 0){
				Match_Struc res = results.get(0);
				
				fOut.write("Expected Result: similarity == 0.5"+" & numMatches == 2 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatches()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}	
		}
	}
	
	@Test
	public void test45(){
		System.out.println("\nRunning test 1.4.5");
		
		source="reviewWriter(document,date,name)";
		target="author(name,email,coAuthors,writePaper,submitPapers,review)";
		
		results = methodCaller.getSchemas(results,source,target);
		
		fOut.write("Test 1.4.5\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results == null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size() > 0){
				Match_Struc res = results.get(0);
				
				fOut.write("Target: "+res.getDatasetSchema()+"\n");
				fOut.write("Expected Result: similarity == 0.5"+" & numMatches == 3 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatches()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}
		}
	}
	
	@Test
	public void test51(){
		System.out.println("\nRunning test 1.5.1");
		
		source="review(date(day,month,year))";
		target="document(date(day,month,year))";
		
		results = methodCaller.getSchemas(results,source,target);
		
		fOut.write("Test 1.5.1\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size() > 0){
				Match_Struc res = results.get(0);
				
				fOut.write("Expected Result: similarity == 1.0"+" & numMatches == 5 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatches()+"\n\n");			
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}
		}
	}
	
	@Test
	public void test52(){
		System.out.println("\nRunning test 1.5.2");
		
		source = "review(publication(day,month,year))";
		target= "review(date(day,month,year))";
		
		results = methodCaller.getSchemas(results, source, target);
		
		fOut.write("Test 1.5.2\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size() > 0){
				Match_Struc res = results.get(0);
				
				fOut.write("Expected Result: similarity == 0.6"+" & numMatches == 5 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatches()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}
		}
	}
	
	@Test
	public void test53(){
		System.out.println("\nRunning test 1.5.3");
		
		source="review(publication(day,month,year))";
		target= "document(date(day,month,year))";
		
		results = methodCaller.getSchemas(results, source, target);
		
		fOut.write("Test 1.5.3\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{
			fOut.write("Expected Result: results.size() == 0 \n");
			fOut.write("Actual Result: results.size() == "+results.size()+"\n\n"); 
		}
	}
	
	@Test
	public void test54(){
		System.out.println("\nRunning test 1.5.4");
		
		source="review(category(day,month,year))";
		target="review(date(day,month,year))";
		
		results = methodCaller.getSchemas(results, source, target);
		
		fOut.write("Test 1.5.4\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		fOut.write("Expected Result: similarity == 0.19999999999999996"+" & numMatches == 1 \n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size() > 0){
				Match_Struc res = results.get(0);
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatches()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}			
		}
	}
	
	@Test
	public void test55(){
		System.out.println("\nRunning test 1.5.5");
		
		source="review(category(day,month,year))";
		target="document(date(day,month,year))";
		
		results = methodCaller.getSchemas(results,source,target);			
		
		fOut.write("Test 1.5.5\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size()>0){
				Match_Struc res = results.get(0);

				fOut.write("Expected Result: similarity == 0.9999999999999998"+" & numMatches == 1 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatches()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}			
		}
	}
	
	@Test
	public void test56(){
		System.out.println("\nRunning test 1.5.6");
		
		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="conference(paper(title,document(date(day,month,year),writer(name(first,second)))))";
		
		results = methodCaller.getSchemas(results, source, target);
		
		fOut.write("Test 1.5.6\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size()>0){
				Match_Struc res = results.get(0);
				fOut.write("Expected Result: similarity == 0.625"+" & numMatches == 12 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatches()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}
		}
	}
	
	@Test
	public void test57(){
		System.out.println("\nRunning test 1.5.7");
		
		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="conference(paper(title,document(category(day,month,year),writer(name(first,second)))))";
		
		results = methodCaller.getSchemas(results,source,target);
		
		fOut.write("Test 1.5.7\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
	
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size()>0){
				Match_Struc res = results.get(0);
				
				fOut.write("Expected Result: similarity == 0.45833333333333337"+" & numMatches == 8 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatches()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}
		}
	}
	
	@Test
	public void test58(){
		System.out.println("\nRunning test 1.5.8");
		
		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="event(paper(title,document(category(day,month,year),writer(name(first,second)))))";
		
		results = methodCaller.getSchemas(results,source,target);
		
		fOut.write("Test 1.5.8\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");		
	
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{			
			fOut.write("Expected Result: results.size() == 0 \n");
			fOut.write("Actual Result: results.size() == "+results.size()+"\n\n"); 
		}
	}
	
	@Test
	public void test61(){
		System.out.println("\nRunning test 1.6.1");
		
		source="conferenceDocument(nameOfAuthor)";
		target="conferenceReview(authorName)";
		
		results = methodCaller.getSchemas(results,source,target);
		
		fOut.write("Test 1.6.1\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
				
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size()>0){
				Match_Struc res = results.get(0);
				
				fOut.write("Expected Result: similarity == 0.5"+" & numMatches ==2 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatches()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}
		}
	}
	
	@Test 
	public void test62(){
		System.out.println("\nRunning test 1.6.2");
		
		source="conference_document(name_of_author)";
		target="conference_review(author_name)";
		
		results = methodCaller.getSchemas(results, source, target);
		
		fOut.write("Test 1.6.2\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
				
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size()>0){
				Match_Struc res = results.get(0);
				
				fOut.write("Expected Result: similarity == 0.5"+" & numMatches == 2 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatches()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}
			
		}
	}
	
	@Test
	public void test63(){
		System.out.println("\nRunning test 1.6.3");
		
		source="conference_document(name_of_author)";
		target="ConferenceReview(authorName)";
		
		results = methodCaller.getSchemas(results, source, target);
		
		fOut.write("Test 1.6.3\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
			
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size()>0){
				Match_Struc res = results.get(0);
				
				fOut.write("Expected Result: similarity == 0.5"+" & numMatches == 2 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatches()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}
		}
	}
	
	@Test
	public void test64(){
		System.out.println("\nRunning test 1.6.4");
		
		source="conference document(name of author)";
		target="conference review(author name)";
		
		results = methodCaller.getSchemas(results,source,target);
		
		fOut.write("Test 1.6.4\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
				
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size()>0){
				Match_Struc res = results.get(0);
				
				fOut.write("Expected Result: similarity == 0.5"+" & numMatches == 2 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatches()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}
		}
	}
	
	@Test
	public void test65(){
		System.out.println("\nRunning test 1.6.5");
		
		source="conference document(nameOfAuthor)";
		target="conference review(authorName)";
		
		results = methodCaller.getSchemas(results,source,target);
	
		fOut.write("Test 1.6.5\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size()>0){
				Match_Struc res = results.get(0);
				
				fOut.write("Expected Result: similarity == 0.5"+" & numMatches == 2 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatches()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}
		}
	}
	
	@Test
	public void test66(){
		System.out.println("\nRunning test 1.6.6");
		
		source="conferencedocument(nameofauthor)";
		target="conference review(authorname)";
		
		results = methodCaller.getSchemas(results, source, target);
		
		fOut.write("Test 1.6.6\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{
			if(results.size() >0){
				Match_Struc res = results.get(0);
				
				fOut.write("Expected Result: similarity == 0.5"+" & numMatches == 2 \n");
				fOut.write("Actual Result: similarity == "+res.getSimValue()+" & numMatches == "+res.getNumMatches()+"\n\n");
			}else{
				fOut.write("Empty Results - zero matches! \n\n");
			}
		}
	}
	
	@Test
	public void test67(){
		System.out.println("\nRunning test 1.6.8");
		
		source="auto(brand,name,color)";
		target="car(year,brand,colour)";
		
		results = methodCaller.getSchemas(results, source, target);
		
		fOut.write("Test 1.6.7\n");
		fOut.write("Calling SPSM with source, "+source + " & target(s), " +target+"\n");
		
		if(results==null){
			fOut.write("Null Results! \n\n");
		}else{
			fOut.write("Expected Result: results == null \n");
			fOut.write("Actual Result: results == "+results+"\n\n"); 
		}
	}
	
	
	@After
	public void cleanUp(){
		fOut.close();
	}

}







