package tests;

import java.util.ArrayList;

import chain_source.Call_SPSM;
import chain_source.Match_Struc;
import chain_source.Repair_Schema;

public class Repair_Schema_Tests_DB {

	public static void main(String[] args) {
		// 
			
		String source="auto(brand,name,color)";
		String target="car(year,brand,colour)";
		String test = "4.1.1";
		
		test_DB_01(test, source, target) ;
		
		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="conference(paper(title,document(date(day,month,year),writer(name(first,second)))))";
		test = "4.5.6" ;
		
		test_DB_01(test, source, target) ;
		
		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="conference(paper(title,document(category(day,month,year),writer(name(first,second)))))";
		test = "4.5.7" ;
		
		test_DB_01(test, source, target) ;
		
		source="";
		target="";
		test = "1.1.1" ;
		
		test_DB_01(test, source, target) ;
		
		source="author(name)";
		target="";
		test = "1.2.1" ;
		
		test_DB_01(test, source, target) ;
		
		source="";
		target="author(name)";
		test = "1.2.2" ;
		
		test_DB_01(test, source, target) ;
		
		source="author(name)";
		target="author(name)";
		test = "1.2.3" ;
		
		test_DB_01(test, source, target) ;
		
		source="author(name)";
		target="author(name)";
		test = "1.2.3" ;
		
		test_DB_01(test, source, target) ;
		
		source="author(name)";
		target="document(title,author) ; author(name,document) ; reviewAuthor(firstname,lastname,review)";		
		test = "1.2.4" ;
		test_DB_01(test, source, target) ;
		
		source="author(name)";
		target="document(title,author) ; conferenceMember(name)";
		test = "1.3.1" ;
		test_DB_01(test, source, target) ;
		
		source="author(name)";
		target="author(name) ; document(title,author) ; paperWriter(firstname,surname,paper) ; reviewAuthor(firstname,lastname,review)";
		test = "1.3.3" ;
		
		test_DB_01(test, source, target) ;
		
		source="author";
		target="writer";
		test = "1.4.1" ;
		
		test_DB_01(test, source, target) ;
		
		
		source="author";
		target="document";
		test = "1.4.2" ;
		
		test_DB_01(test, source, target) ;
		
		source="author(name)";
		target="document(name)";
		test = "1.4.3" ;
		
		test_DB_01(test, source, target) ;
		
		source="author(name)";
		target="reviewWriter(review,name)";
		test = "1.4.4" ;
		
		test_DB_01(test, source, target) ;
		
		source="reviewWriter(document,date,name)";
		target="author(name,email,coAuthors,writePaper,submitPapers,review)";
		test = "1.4.5" ;
		
		test_DB_01(test, source, target) ;
		
		source="review(date(day,month,year))";
		target="document(date(day,month,year))";
		test = "1.5.1" ;
		
		test_DB_01(test, source, target) ;
		
		source = "review(publication(day,month,year))";
		target= "review(date(day,month,year))";
		test = "1.5.2" ;
		
		test_DB_01(test, source, target) ;
		
		source="review(publication(day,month,year))";
		target= "document(date(day,month,year))";
		test = "1.5.3" ;
		
		test_DB_01(test, source, target) ;
		
		source="review(category(day,month,year))";
		target="review(date(day,month,year))";
		test = "1.5.4" ;
		
		test_DB_01(test, source, target) ;
		
		source="review(category(day,month,year))";
		target="document(date(day,month,year))";
		test = "1.5.5" ;
		
		test_DB_01(test, source, target) ;
		
		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="conference(paper(title,document(date(day,month,year),writer(name(first,second)))))";
		test = "1.5.6" ;
		
		test_DB_01(test, source, target) ;
		
		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="conference(paper(title,document(category(day,month,year),writer(name(first,second)))))";
		test = "1.5.7" ;
		
		test_DB_01(test, source, target) ;
		
		source="conference(paper(title,review(date(day,month,year),author(name(first,second)))))";
		target="event(paper(title,document(category(day,month,year),writer(name(first,second)))))";
		test = "1.5.8" ;
		
		test_DB_01(test, source, target) ;
		
		source="conferenceDocument(nameOfAuthor)";
		target="conferenceReview(authorName)";
		test = "1.6.1" ;
		
		test_DB_01(test, source, target) ;
		
		source="conference_document(name_of_author)";
		target="conference_review(author_name)";
		test = "1.6.2" ;
		
		test_DB_01(test, source, target) ;
		
		source="conference_document(name_of_author)";
		target="ConferenceReview(authorName)";
		test = "1.6.3" ;
		
		test_DB_01(test, source, target) ;
		
		source="conference document(name of author)";
		target="conference review(author name)";
		test = "1.6.4" ;
		
		test_DB_01(test, source, target) ;
		
		source="conference document(nameOfAuthor)";
		target="conference review(authorName)";
		test = "1.6.5" ;
		
		test_DB_01(test, source, target) ;
		
		source="conferencedocument(nameofauthor)";
		target="conference review(authorname)";
		test = "1.6.6" ;
		
		test_DB_01(test, source, target) ;
		
		source="auto(brand,name,color)";
		target="car(year,brand,colour)";
		test = "1.6.8" ;
		
		test_DB_01(test, source, target) ;		
		
		
		

	}

	private static void test_DB_01(String test, String source, String target) {
		// 
		System.out.println("Test "+ test) ;
		System.out.println("Source " + source) ;
		System.out.println("Target " + target) ;
		
		Call_SPSM spsmCall = new Call_SPSM();
		Repair_Schema getRepairedSchema = new Repair_Schema();
		
		ArrayList<Match_Struc> finalRes = new ArrayList<Match_Struc>();
		
		finalRes=spsmCall.getSchemas(finalRes, source, target);
		
		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.prepare(finalRes);
		}
		
		if(finalRes!=null){
			System.out.println("Number of matches / repairs: "+finalRes.size());
			if(finalRes.size() == 0){
				//then we have no results so end test
				System.out.println("Empty results returned. \n\n");
			}else{
				//we can test repaired schema
				for(int i = 0 ; i < finalRes.size() ; i++){
					Match_Struc currRes = finalRes.get(i);
					
					System.out.println("Repaired schema: "+currRes.getRepairedSchema());
					System.out.println("Similarity == "+currRes.getSimValue()+" & size of matched structure == "+currRes.getNumMatches()+"\n"); 
				}
				// String rSchema = finalRes.get(0).getRepairedSchema();
				// System.out.println("Actual Result: repaired schema == '" + rSchema + "' \n\n");
			}
		}else{
			System.out.println("Null Results! \n\n");
		}
		
		
	}

}
