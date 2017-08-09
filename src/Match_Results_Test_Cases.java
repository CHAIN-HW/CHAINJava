import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.*;

/*
 * Responsible for testing the Best_Match_Results.java file
 * to make sure that when we pass in the results we get results 
 * that are over the threshold value that are sorted and we only
 * have n number of results as requested
 */
public class Match_Results_Test_Cases {

	private Best_Match_Results methodCall;
	private ArrayList<Match_Struc> finalRes;
	private static int counter;
	
	//for writing results
	private static File testRes;
	private PrintWriter fOut;
	private static boolean alreadyWritten;
	
	@BeforeClass
	public static void beforeAll(){
		System.out.println("These tests are responsible for testing from Best_Match_Results.java to make sure that when\n"
				+"we pass in the results from SPSM, we get results that are over the threshold value that are sorted\n"
				+"and only have n number of results as requested.");
		System.out.println("\nThe results from these tests can be found in outputs/tests/Filter_Limit_Tests.txt\n");
		
		alreadyWritten=false;
		counter=1;
		
		try{
			testRes = new File("outputs/testing/Filter_Limit_Test.txt");
			testRes.createNewFile();
			
			new PrintWriter("outputs/testing/Filter_Limit_Test.txt").close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Before
	public void setup(){
		methodCall = new Best_Match_Results();
		finalRes = new ArrayList<Match_Struc>();
		
		try{
			fOut = new PrintWriter(new FileWriter(testRes,true));
			
			if(alreadyWritten==false){
				fOut.write("Testing Results for Match_Results_Test_Cases.java\n\n");
				alreadyWritten = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void emptyMatches(){
		System.out.println("\nRunning test emptyMatches");
		
		ArrayList<Match_Struc> res = new ArrayList<Match_Struc>();
		finalRes = methodCall.getThresholdAndFilter(res, 0.2, 0);
		
		fOut.write("Test "+counter+" - empty matches\n");
		fOut.write("Calling with threshold: "+0.2+" & limit: "+0+" on an empty list of matches \n");
		
		fOut.write("Expected Result: results.size() == 0 \n");
		fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n\n");
		
		counter++;
	}
	
	@Test
	public void singleSuccMatch(){
		System.out.println("\nRunning test singleSuccMatch");
		
		ArrayList<Match_Struc> res = new ArrayList<Match_Struc>();
		res.add(new Match_Struc(0.2,"author(name,document)"));
		
		finalRes = methodCall.getThresholdAndFilter(res, 0.1, 0);
		
		fOut.write("Test "+counter+" - single success match\n");
		fOut.write("Calling with threshold: "+0.1+" & limit: "+0+" on a single match \n");
		
		fOut.write("Expected Result: results.size() == 1 \n");
		fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n\n");
		
		counter++;
	}
	
	@Test
	public void singleFailMatch(){
		System.out.println("\nRunning test singleFailMatch");
		
		ArrayList<Match_Struc> res = new ArrayList<Match_Struc>();
		res.add(new Match_Struc(0.2,"author(name,document)"));
		
		finalRes = methodCall.getThresholdAndFilter(res, 0.5, 0 );
		
		fOut.write("Test "+counter+" - single fail match\n");
		fOut.write("Calling with threshold: "+0.5+" & limit: "+0+" on a single match \n");
		
		fOut.write("Expected Result: results.size() == 0 \n");
		fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n\n");
		
		counter++;
	}
	
	@Test
	public void multiSuccMatch(){
		System.out.println("\nRunning test multiSuccMatch");
		
		ArrayList<Match_Struc> res = new ArrayList<Match_Struc>();
		res.add(new Match_Struc(0.2,"author(name,document)"));
		res.add(new Match_Struc(0.7,"author(name,document)"));
		res.add(new Match_Struc(0.1,"author(name,document)"));
		res.add(new Match_Struc(0.9,"author(name,document)"));
		res.add(new Match_Struc(0.5,"author(name,document)"));
		
		finalRes = methodCall.getThresholdAndFilter(res, 0.6, 0);
		
		fOut.write("Test "+counter+" - multiple successes match\n");
		fOut.write("Calling with threshold: "+0.6+" & limit: "+0+" on multiple matches \n");
		
		fOut.write("Expected Result: results.size() == 2 \n");
		fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n\n");
		
		counter++;
	}
	
	@Test
	public void multiSuccMatch2(){
		System.out.println("\nRunning test multiSuccMatch2");
		
		ArrayList<Match_Struc> res = new ArrayList<Match_Struc>();
		res.add(new Match_Struc(0.2,"author(name,document)"));
		res.add(new Match_Struc(0.7,"author(name,document)"));
		res.add(new Match_Struc(0.1,"author(name,document)"));
		res.add(new Match_Struc(0.9,"author(name,document)"));
		res.add(new Match_Struc(0.5,"author(name,document)"));
		
		finalRes = methodCall.getThresholdAndFilter(res, 0.2, 0);
		
		fOut.write("Test "+counter+" - multiple successes match\n");
		fOut.write("Calling with threshold: "+0.2+" & limit: "+0+" on multiple matches \n");
		
		fOut.write("Expected Result: results.size() == 4 \n");
		fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n\n");
		
		counter++;
	}
	
	@Test
	public void multiFailMatch(){
		System.out.println("\nRunning test multiFailMatch");
		
		ArrayList<Match_Struc> res = new ArrayList<Match_Struc>();
		res.add(new Match_Struc(0.2,"author(name,document)"));
		res.add(new Match_Struc(0.7,"author(name,document)"));
		res.add(new Match_Struc(0.1,"author(name,document)"));
		res.add(new Match_Struc(0.9,"author(name,document)"));
		res.add(new Match_Struc(0.5,"author(name,document)"));
		
		finalRes = methodCall.getThresholdAndFilter(res, 1.0, 0);
		
		fOut.write("Test "+counter+" - multiple fail matches\n");
		fOut.write("Calling with threshold: "+1.0+" & limit: "+0+" on multiple matches \n");
		
		fOut.write("Expected Result: results.size() == 0 \n");
		fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n\n");
		
		counter++;
	}
	
	@Test
	public void successWithLimit(){
		System.out.println("\nRunning test successWithLimit");
		
		ArrayList<Match_Struc> res = new ArrayList<Match_Struc>();
		res.add(new Match_Struc(0.2,"author(name,document)"));
		res.add(new Match_Struc(0.7,"author(name,document)"));
		res.add(new Match_Struc(0.1,"author(name,document)"));
		res.add(new Match_Struc(0.9,"author(name,document)"));
		res.add(new Match_Struc(0.5,"author(name,document)"));
		
		finalRes = methodCall.getThresholdAndFilter(res, 0.2, 3);
		
		fOut.write("Test "+counter+" - success with limit\n");
		fOut.write("Calling with threshold: "+0.2+" & limit: "+3+" on multiple matches \n");
		
		fOut.write("Expected Result: results.size() == 3 \n");
		fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n\n");
		
		counter++;
	}
	
	@Test
	public void successWithLargeLimit(){
		System.out.println("\nRunning test successWithLargeLimit");
		
		ArrayList<Match_Struc> res = new ArrayList<Match_Struc>();
		res.add(new Match_Struc(0.2,"author(name,document)"));
		res.add(new Match_Struc(0.7,"author(name,document)"));
		res.add(new Match_Struc(0.1,"author(name,document)"));
		res.add(new Match_Struc(0.9,"author(name,document)"));
		res.add(new Match_Struc(0.5,"author(name,document)"));
		
		finalRes = methodCall.getThresholdAndFilter(res, 0.2, 5);
		
		fOut.write("Test "+counter+" - success with large limit\n");
		fOut.write("Calling with threshold: "+0.2+" & limit: "+5+" on multiple matches \n");
		
		fOut.write("Expected Result: results.size() == 4 \n");
		fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n\n");
		
		counter++;
	}
	
	@Test
	public void failWithLimit(){
		System.out.println("\nRunning test failWithLimit");
		
		ArrayList<Match_Struc> res = new ArrayList<Match_Struc>();
		res.add(new Match_Struc(0.2,"author(name,document)"));
		res.add(new Match_Struc(0.7,"author(name,document)"));
		res.add(new Match_Struc(0.1,"author(name,document)"));
		res.add(new Match_Struc(0.9,"author(name,document)"));
		res.add(new Match_Struc(0.5,"author(name,document)"));
		
		finalRes = methodCall.getThresholdAndFilter(res, 1.0, 5);
		
		fOut.write("Test "+counter+" - fail with limit\n");
		fOut.write("Calling with threshold: "+1.0+" & limit: "+5+" on multiple matches \n");
		
		fOut.write("Expected Result: results.size() == 0 \n");
		fOut.write("Actual Result: results.size() == "+finalRes.size()+"\n\n");
		
		counter++;
	}
	
	@After
	public void cleanUp(){
		fOut.close();
	}
}
