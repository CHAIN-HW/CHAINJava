package chain_tests.sparql;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import chain.sparql.CreateQuery;
import chain.sparql.QueryData;
import chain.sparql.RepairQuery;
import chain_tests.core.BaseTest;
import chain.core.CallSPSM;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;

import chain.core.MatchStruc;
import chain.core.RepairSchema;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;

/* Author Diana Bental
 * Date January 2017
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class SAQCreateQueryTests extends BaseTest {
	
	// 1 SAQ  cosmonauts

	// 1 Resolved

	private static String q01_res = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
	    + "PREFIX dbo: <http://dbpedia.org/ontology/> "
	    + "PREFIX res: <http://dbpedia.org/resource/> "
	    + "SELECT DISTINCT ?uri "
	    + "WHERE { "
	    + "?uri rdf:type dbo:Astronaut . "
	    + "{ ?uri dbo:nationality res:Russia . } "
	    + "UNION "
	    + "{ ?uri dbo:nationality res:Soviet_Union . }"
	    + "}" ;
	
//	private static String q01_res = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
//		    + "PREFIX dbo: <http://dbpedia.org/ontology/> "
//		    + "PREFIX res: <http://dbpedia.org/resource/> "
//		    + "SELECT DISTINCT ?uri "
//		    + "WHERE { "
//		    + "?uri rdf:type dbo:Astronaut . "
//		    + "?uri dbo:nationality res:Russia . "
//		    + "}" ;
	


	private static String schema_01 = "Astronaut(nationality)" ;
	
	private static final String [] tests_01 = {"rdf:type dbo:Astronaut", 
			"dbo:nationality", "res:Russia", "res:Soviet_Union"} ;

	// 2 SAQ German cities with more than 250000 inhabitants

	// 2 Resolved

	private static String q02_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	    + "PREFIX res: <http://dbpedia.org/resource/> "
	    + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
	    + "SELECT DISTINCT ?uri "
	    + "WHERE { "
	    + "{ ?uri rdf:type dbo:City . } "
	    + "UNION "
	    + "{ ?uri rdf:type dbo:Town . } "
	    + "?uri dbo:country res:Germany . "
	    + "?uri dbo:populationTotal ?population . "
	    + "FILTER ( ?population > 250000 ) } " ;

	private static String schema_02a = "City(country, populationTotal)" ;
	private static String schema_02b = "town(country, populationTotal)" ;
	
	private static final String [] tests_02 = {"rdf:type dbo:City", 
			"dbo:country res:Germany" , "dbo:populationTotal ?population"};


	// 3 Resolved

	// 3 SAQ mayor of Berlin


	private static String q03_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        res:Berlin dbo:leader ?uri . "
	       + "} " ;

	private static String schema_03 = "entity(leader)";
	
	private static final String [] tests_03 = {"dbo:leader"} ;
	
	// 4 SAQ second highest mountain on Earth

	// 4 Resolved

	private static String q04_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri rdf:type dbo:Mountain . "
	       + "        ?uri dbo:elevation ?elevation . "
	       + "} "
	       + "ORDER BY DESC(?elevation) "
	       + "OFFSET 1 LIMIT 1 " ;

	private static String schema_04 = "mountain(elevation)" ;
	
	private static final String [] tests_04 = {"rdf:type dbo:Mountain", 
			"dbo:elevation ?elevation", "ORDER BY DESC(?elevation"} ;

	// 5 SAQ professional skateboarders from Sweden

	// 5 Resolved

	private static String q05_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri dbo:occupation res:Skateboarding . "
	       + "      { ?uri dbo:birthPlace res:Sweden . } "
	       + "      UNION "
	       + "      { ?uri dbo:birthPlace ?place . "
	       + "        ?place dbo:country res:Sweden . } "
	       + "} " ;

	private static String schema_05a = "entity(occupation, birthPlace)" ;
	
	private static final String [] tests_05a = {"dbo:occupation res:Skateboarding",
			"dbo:birthPlace res:Sweden", "dbo:birthPlace ?place"} ;

	private static String schema_05b = "entity(occupation, birthPlace, country)" ;
	
	private static final String [] tests_05b = {"dbo:occupation res:Skateboarding",
			"dbo:birthPlace res:Sweden", "dbo:birthPlace ?place", "dbo:country res:Sweden"} ;

	// 6 SAQ band leaders that play trumpet

	// 6 Resolved

	private static String q06_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri dbo:occupation res:Bandleader . "
	       + "        ?uri dbo:instrument res:Trumpet . "
	       + "} " ;

	private static String schema_06="entity(occupation, instrument)" ;
	
	private static final String [] tests_06 = {"dbo:occupation res:Bandleader", "dbo:instrument res:Trumpet"};

	// 7 SAQ countries have more than ten caves

	//7 Resolved

	private static String q07_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	     + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri rdf:type dbo:Country . "
	       + "        ?cave rdf:type dbo:Cave . "
	       + "      { ?cave dbo:location ?uri . } "
	       + "      UNION "
	       + "      { ?cave dbo:location ?loc . "
	       + "        ?loc dbo:country ?uri . } "
	       + "} "
	       + "GROUP BY ?uri "
	       + "HAVING(COUNT(?cave) > 10) " ;


	private static String schema_07a = "Country(location)" ;
	private static String schema_07b = "Cave(location)" ;

	private static final String [] tests_07a = {""};
	private static final String [] tests_07b = {""};

	// 8 SAQ Formula 1 driver with the most races

	// 8 Resolved

	private static String q08_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri rdf:type dbo:FormulaOneRacer . "
	       + "        ?uri dbo:races ?x . "
	       + "} "
	       + "ORDER BY DESC(?x) "
	       + "OFFSET 0 LIMIT 1 " ;

	private static String schema_08 = "formulaOneRacer(races)" ;
	
	private static final String [] tests_08 = {"rdf:type dbo:FormulaOneRacer", 
			"dbo:races ?x", "ORDER BY DESC(?x)", "OFFSET  0", "LIMIT   1"} ;

	// 9 SAQ youngest player in the Premier League

	// 9 resolved

	private static String q09_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri dbo:team ?x . "
	       + "        ?x dbo:league res:Premier_League . "
	       + "        ?uri dbo:birthDate ?y . "
	       + "} "
	       + "ORDER BY DESC(?y) "
	       + "OFFSET 0 LIMIT 1 " ;

	private static String schema_09a = "entity(team, league, birthDate)" ;
	private static String schema_09b = "entity(team, birthDate)" ;
	
	private static final String [] tests_09a = {"dbo:team ?x", 
			"dbo:league res:Premier_League", "dbo:birthDate ?y", "ORDER BY DESC(?y)"} ;	
	private static final String [] tests_09b = {"dbo:team ?x", 
			"dbo:birthDate ?y", "ORDER BY DESC(?y)"} ;


	// 10 SAQ longest river

	// 10 Resolved

	private static String q10_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX dbp: <http://dbpedia.org/property/> "
	       + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri rdf:type dbo:River . "
	       + "      { ?uri dbo:length ?l . } "
	       + "      UNION "
	       + "      { ?uri dbp:length ?l . } "
	       + "} "
	       + "ORDER BY DESC(?l) "
	       + "OFFSET 0 LIMIT 1 " ;

	private static String schema_10 = "river(length)" ;
	
	private static final String [] tests_10 = {"df:type dbo:River", "dbo:length ?l", "ORDER BY DESC(?l)"} ;

	// 11 SAQ cars that are produced in Germany

	// 11 Resolved

	private static String q11_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX dbp: <http://dbpedia.org/property/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri rdf:type dbo:Automobile . "
	       + "      { ?uri dbo:assembly res:Germany . } "
	       + "      UNION "
	       + "      { ?uri dbp:assembly res:Germany . } "
	       + "      UNION "
	       + "      { { ?uri dbo:manufacturer ?x . } "
	       + "        UNION "
	       + "        { ?uri dbp:manufacturer ?x . } "
	       + "        { ?x dbo:locationCountry res:Germany . } "
	       + "        UNION "
	       + "        { ?x dbo:location res:Germany . } "
	       + "      } "
	       + "} " ;

//	private static String schema_11a = "automobile(assembly)" ;
//	private static String schema_11b = "automobile(assembly, manufacturer)" ;
// The above schemas create queries that don't parse.
	
	private static String schema_11c = "automobile(assembly, manufacturer, locationCountry, location)" ;
	
//	private static final String [] tests_11a = {"rdf:type dbo:Automobile", "dbo:assembly res:Germany"} ;
//	private static final String [] tests_11b = {"rdf:type dbo:Automobile", "dbo:assembly res:Germany",
//			"dbo:manufacturer ?x"} ;
	private static final String [] tests_11c = {"rdf:type dbo:Automobile", "dbo:assembly res:Germany",
			"dbo:manufacturer ?x", "dbo:locationCountry res:Germany", "dbo:location res:Germany"} ;
	


	// 12 SAQ People that were born in Vienna and died in Berlin

	// 12 Resolved

	private static String q12_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri dbo:birthPlace res:Vienna . "
	       + "        ?uri dbo:deathPlace res:Berlin . "
	       + "} " ;

	private static String schema_12 = "entity(birthPlace, deathPlace)" ;
	
	private static final String [] tests_12 = {"dbo:birthPlace res:Vienna", "dbo:deathPlace"};
	

	// 13 SAQ Mother and father of Prince Harry and Prince William

	// 13 Resolved

	private static String q13_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "SELECT ?x"
	       + "WHERE { "
	       + "        <http://dbpedia.org/resource/Prince_William,_Duke_of_Cambridge> dbo:parent ?x . "
	       + "        <http://dbpedia.org/resource/Prince_Harry> dbo:parent ?x ."
	       + "} " ;

	private static String schema_13="entity(parent)" ;
	
	private static final String [] tests_13 = {"dbo:parent ?x"} ;

	// 14 SAQ  latest U.S. state admitted

	// 14 Resolved

	private static String q14_res = "PREFIX dbp: <http://dbpedia.org/property/> "
	       + "PREFIX yago: <http://dbpedia.org/class/yago/> "
	       + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri rdf:type yago:StatesOfTheUnitedStates . "
	       + "        ?uri dbp:admittancedate ?x . "
	       + "} ORDER BY DESC(?x) "
	       + "OFFSET 0 LIMIT 1 " ;


	private static String schema_14 = "StatesOfTheUnitedStates_State(admittancedate)" ;
	private static final String [] tests_14 = {"rdf:type yago:StatesOfTheUnitedStates",
			"dbp:admittancedate ?x", "OFFSET  0"} ;
	

	// 15 SAQ number of languages spoken in Turkmenistan

	// 15 Resolved 

	// Example corrected for JENA parsing - COUNT(DISTINCT ?x) replaced by 
	// (COUNT(DISTINCT ?x) AS ?count)
	private static String q15_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "SELECT (COUNT(DISTINCT ?x) AS ?count) "
	       + "WHERE { "
	       + "        res:Turkmenistan dbo:language ?x . "
	       + "} " ;

	private static String schema_15 = "entity(language)" ;
	
	private static final String [] tests_15 = {"dbo:language ?x"} ;

	// 16 SAQ movies directed by Francis Ford Coppola

	// 16 Resolved

	private static String q16_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri rdf:type dbo:Film . "
	       + "        ?uri dbo:director res:Francis_Ford_Coppola . "
	       + "} " ;

	private static String schema_16 = "film(director)" ;
	
	private static final String [] tests_16 = {"rdf:type dbo:Film","dbo:director"} ;

	// 17 SAQ  maiden name of Angela Merkel

	// 17 Resolved

	private static String q17_res = "PREFIX dbp: <http://dbpedia.org/property/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "SELECT DISTINCT ?string "
	       + "WHERE { "
	       + "        res:Angela_Merkel dbp:birthName ?string . "
	       + "} " ;

	private static String schema_17 = "entity(birthName)" ;
	
	private static final String [] tests_17 = {"dbp:birthName ?string"} ;

	// 18 SAQ Methodist politicians

	// 18 Resolved

	private static String q18_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri rdf:type dbo:Politician . "
	       + "        ?uri dbo:religion res:Methodism . "
	       + "} " ;

	private static String schema_18= "politician(religion)" ;
	
	private static final String [] tests_18 = {"rdf:type dbo:Politician","dbo:religion res:Methodism"};

	// 19 SAQ number of times that Jane Fonda married

	// 19 Resolved
	// Note - syntax correction Added AS ?count
	private static String q19_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "SELECT (COUNT(DISTINCT ?uri) AS ?count)"
	       + "WHERE { "
	       + "        res:Jane_Fonda dbo:spouse ?uri . "
	       + "} " ;

	private static String schema_19 = "entity(spouse)" ;
	
	private static final String [] tests_19 = {"dbo:spouse ?uri"} ;
	

	// 20 SAQ Australian nonprofit organizations

	// 20 Resolved

	private static String q20_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri dbo:type res:Nonprofit_organization . "
	       + "      { ?uri dbo:locationCountry res:Australia . } "
	       + "      UNION "
	       + "      { ?uri dbo:location ?x . "
	       + "        ?x dbo:country res:Australia . } "
	       + "} " ;

	// private static String schema_20 = "Nonprofit_Organization(locationCountry, location, country)" ;
	private static String schema_20 = "entity(locationCountry, location, country)" ;
	
	private static final String [] tests_20 = {
			"dbo:locationCountry res:Australia","dbo:location ?x", "dbo:country res:Australia"} ;

	// 21 SAQ Military conflicts in which Lawrence of Arabia participated

	// 21 Resolved

	private static String q21_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        res:T._E._Lawrence dbo:battle ?uri . "
	       + "} " ;
	       
	private static String schema_21 = "entity(battle)" ;
	
	private static final String [] tests_21 = {"dbo:battle ?uri"} ;

	// 22 SAQ  number of inhabitants in Maribor


	// 22 Resolved

	private static String q22_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "SELECT DISTINCT ?num "
	       + "WHERE { "
	       + "        res:Maribor dbo:populationTotal ?num . "
	       + "} " ;

	private static String schema_22 = "entity(populationTotal)" ;
	
	private static final String [] tests_22 = {"dbo:populationTotal ?num"} ;
	
	
	// 23 SAQ  companies in Munich

	// 23 Resolved

	private static String q23_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri rdf:type dbo:Company . "
	       + "      { ?uri dbo:location res:Munich . } "
	       + "      UNION "
	       + "      { ?uri dbo:headquarter res:Munich . } "
	       + "      UNION "
	       + "      { ?uri dbo:locationCity res:Munich . } "
	       + "} " ;

	private static String schema_23 = "Company(location,headquarter,locationCity)" ;
	private static final String [] tests_23 = {"rdf:type dbo:Company",
			"dbo:location res:Munich", "dbo:headquarter res:Munich", "dbo:locationCity res:Munich"
	};

	// 24 SAQ  games developed by GMT

	// 24 Resolved

	private static String q24_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri dbo:publisher res:GMT_Games . "
	       + "} " ;

	private static String schema_24 = "entity(publisher)" ;
	
	private static final String [] tests_24 = {"dbo:publisher res:GMT_Games"} ;

	// 25 SAQ  husband of Amanda Palmer

	// 25 Resolved

	private static String q25_res = "PREFIX dbp: <http://dbpedia.org/property/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        res:Amanda_Palmer dbp:spouse ?uri . "
	       + "} " ;

	private static String schema_25 = "entity(spouse)" ;
	
	private static final String [] tests_25 = {"dbp:spouse ?uri"} ;

	// 26 SAQ  countries connected by the Rhine

	// 26 Resolved

	private static String q26_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
			+ "PREFIX dbp: <http://dbpedia.org/property/> "
			+ "PREFIX res: <http://dbpedia.org/resource/> "
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
			+ "SELECT DISTINCT ?uri " 
			+ "WHERE { "
			+ "        res:Rhine dbp:country ?string .  "
			+ "        ?uri rdf:type dbo:Country .  "
			+ "        ?uri rdfs:label ?string . "
			+ "} " ;

	private static String schema_26 =  "Country(label,country)" ;
	private static final String [] tests_26 = {"rdf:type dbo:Country",
			"rdfs:label ?string","dbp:country ?string"};
	
	
	// 27 SAQ   professional surfers born in Australia

	// 27 Resolved

	private static String q27_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
			  + "PREFIX res: <http://dbpedia.org/resource/> "
		       + "SELECT DISTINCT ?uri "
		       + "WHERE { "
		       + "        ?uri dbo:occupation res:Surfing .  "	       
		       + "       { ?uri dbo:birthPlace res:Australia . }"
		       + "       UNION "
		       + "       { ?uri dbo:birthPlace ?x . "
		       + "         ?x dbo:country res:Australia . }"
		       + "} " ;
	
	private static String schema_27 = "entity(occupation,birthplace)" ;
	private static final String [] tests_27 = {"dbo:occupation res:Surfing",
			"dbo:birthPlace res:Australia","dbo:birthPlace ?x","dbo:country res:Australia"};
	
	// 28 SAQ  islands that belong to Japan

	// 28 Resolved

	private static String q28_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri rdf:type dbo:Island .  "
	       + "        ?uri dbo:country res:Japan . "
	       + "} " ;

	private static String schema_28 = "island(country)" ;
	private static final String [] tests_28 = {"rdf:type dbo:Island", "dbo:country res:Japan"} ;


	// 29 SAQ  ruling party in Lisbon

	// 29 resolved

	private static String q29_res = "PREFIX dbp: <http://dbpedia.org/property/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        res:Lisbon dbp:leaderParty ?uri . "
	       + "} " ;

	private static String schema_29 = "entity(leaderParty)" ;
	private static final String [] tests_29 = {"dbp:leaderParty ?uri"};

	// 30 SAQ  Apollo 14 astronauts

	// 30 resolved
	private static String q30_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri dbo:mission res:Apollo_14 . "
	       + "} " ;

	private static String schema_30 = "entity(mission)" ;
	private static final String [] tests_30 = {"dbo:mission res:Apollo_14"} ;

	private CallSPSM spsmCall;
	private RepairSchema getRepairedSchema;
	private CreateQuery createQuery;
	
	private ArrayList<MatchStruc> finalRes;
	private String schema;
	
	//for writing results
	private static File testRes;
	private PrintWriter fOut;
	private static boolean alreadyWritten;
	
	
	@BeforeClass
	public static void beforeAll(){
		System.out.println("Testing RepairQuery.java with resolved SAQ queries") ;
		System.out.println("\nThe results from these tests can be found in outputs/testing/SAQ_Repair_Queries_Test.txt\n");

		alreadyWritten = false;
		try{
			testRes = new File("outputs/testing/SAQ_Repair_Queries_Tests.txt");
			testRes.createNewFile();
			
			new PrintWriter("outputs/testing/SAQ_Repair_Queries_Tests.txt").close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	
	@Before
	public void setup(){
		spsmCall = new CallSPSM();
		getRepairedSchema = new RepairSchema();
		createQuery = new CreateQuery();
		
		try{
			fOut = new PrintWriter(new FileWriter(testRes,true));
			
			if(alreadyWritten==false){
				fOut.write("Testing Results for SAQ_Repair_Queries_Tests.java\n\n");
				alreadyWritten = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_01(){
	  testQueryRepair("SAQ 01", q01_res, schema_01, tests_01) ;	  
	}
	
	@Test
	public void test_02a(){
	  testQueryRepair("SAQ 02a", q02_res, schema_02a, tests_02) ;	  
	}
	
	@Test
	public void test_02b(){
	  testQueryRepair("SAQ 02b", q02_res, schema_02b, tests_02) ;	  
	}
	
	@Test
	public void test_03(){
	  testQueryRepair("SAQ 03", q03_res, schema_03, tests_03) ;  
	}
	
	@Test
	public void test_04(){
	  testQueryRepair("SAQ 04", q04_res, schema_04, tests_04) ;	  
	}
	
	@Test
	public void test_05a(){
	  testQueryRepair("SAQ 05", q05_res, schema_05a, tests_05a) ;	  
	}
	
	@Test
	public void test_05b(){
	  System.out.println("\nRunning test SAQ_05b"); 
	  testQueryRepair("SAQ 05", q05_res, schema_05b, tests_05b) ;	  
	}
	
	@Test
	public void test_06(){
	  testQueryRepair("SAQ 06", q06_res, schema_06, tests_06) ;
	}
	
	@Test
	public void test_07a(){
	  testQueryRepair("SAQ 07a", q07_res, schema_07a, tests_07a) ;	  
	}
	
	@Test
	public void test_07b(){
	  testQueryRepair("SAQ 07b", q07_res, schema_07b, tests_07b) ;	  
	}
	
	@Test
	public void test_08(){
	  testQueryRepair("SAQ 08", q08_res, schema_08, tests_08) ;	  
	}
	
	@Test
	public void test_09a(){
	  testQueryRepair("SAQ 09a", q09_res, schema_09a, tests_09a) ;	  
	}
	
	@Test
	public void test_09b(){
	  testQueryRepair("SAQ 09b", q09_res, schema_09b, tests_09b) ;	  
	}
	
	@Test
	public void test_10(){
	  testQueryRepair("SAQ 10", q10_res, schema_10, tests_10) ;	  
	}
	
//	@Test
//	public void test_11a(){
//	  System.out.println("\nRunning test SAQ_11a"); 
//	  testQueryRepair("SAQ 11a", q11_res, schema_11a, tests_11a) ;	  
//	}
//	
//	@Test
//	public void test_11b(){
//	  System.out.println("\nRunning test SAQ_11b"); 
//	  testQueryRepair("SAQ 11b", q11_res, schema_11b, tests_11b) ;	  
//	}
	
	@Test
	public void test_11c(){
	  testQueryRepair("SAQ 11c", q11_res, schema_11c, tests_11c) ;	  
	}
	
	@Test
	public void test_12(){
	  testQueryRepair("SAQ 12", q12_res, schema_12, tests_12) ;	  
	}
	
	@Test
	public void test_13(){
	  testQueryRepair("SAQ 13", q13_res, schema_13, tests_13) ;	  
	}
	
	@Test
	public void test_14(){
	  testQueryRepair("SAQ 14", q14_res, schema_14, tests_14) ;	  
	}
	
	@Test
	public void test_15(){
	  testQueryRepair("SAQ 15", q15_res, schema_15, tests_15) ;	  
	}
	
	@Test
	public void test_16(){
	  testQueryRepair("SAQ 16", q16_res, schema_16, tests_16) ;	  
	}
	
	@Test
	public void test_17(){
	  testQueryRepair("SAQ 17", q17_res, schema_17, tests_17) ;	  
	}
	
	@Test
	public void test_18(){
	  testQueryRepair("SAQ 18", q18_res, schema_18, tests_18) ;	  
	}
	
	@Test
	public void test_19(){
	  testQueryRepair("SAQ 19", q19_res, schema_19, tests_19) ;	  
	}
	
	@Test
	public void test_20(){
	  testQueryRepair("SAQ 20", q20_res, schema_20, tests_20) ;	  
	}
	
	@Test
	public void test_21(){
	  testQueryRepair("SAQ 21", q21_res, schema_21, tests_21) ;	  
	}
	
	@Test
	public void test_22(){
	  testQueryRepair("SAQ 22", q22_res, schema_22, tests_22) ;	  
	}
	
	@Test
	public void test_23(){
	  testQueryRepair("SAQ 23", q23_res, schema_23, tests_23) ;	  
	}
	
	@Test
	public void test_24(){
	  testQueryRepair("SAQ 24", q24_res, schema_24, tests_24) ;	  
	}


	// Tests do not work

//	@Test
//	public void test_25(){
//	  testQueryRepair("SAQ 25", q25_res, schema_25, tests_25) ;
//	}
//
//	@Test
//	public void test_26(){
//	  testQueryRepair("SAQ 26", q26_res, schema_26, tests_26) ;
//	}
//
//	@Test
//	public void test_27(){
//	  testQueryRepair("SAQ 27", q27_res, schema_27, tests_27) ;
//	}
//
	@Test
	public void test_28(){
	  testQueryRepair("SAQ 28", q28_res, schema_28, tests_28) ;	  
	}
	
	@Test
	public void test_29(){
	  testQueryRepair("SAQ 29", q29_res, schema_29, tests_29) ;	  
	}
	
	@Test
	public void test_30(){
	  testQueryRepair("SAQ 30", q30_res, schema_30, tests_30) ;	  
	}
	private void testQueryRepair(String testID, String query, String schema, String[] tests) {
		System.out.println("\nRunning test " + testID);
		
		// TODO Auto-generated method stub
		fOut.write("Test " + testID + "\n\n");
		fOut.write("Query " +  query + "\n\n");
		fOut.write("Schema: " + schema +"\n\n") ;
		
		System.out.println("Query " +  query + "\n");
		System.out.println("Schema: " + schema +"\n") ;
		
		try {
			Query q = QueryFactory.create(query);
			query = q.toString();
			System.out.println("Original Query:\n" + query);
		
		} catch (Exception e) {
			
			System.out.println("SAQ_Create_Query_Test.java: The original query string doesn't parse as correct Sparql in Jena.");
			System.out.println(query) ;
			fail() ;
		}
		
		// Call SPSM to create a match structure
		finalRes = new ArrayList<MatchStruc>();

		finalRes=spsmCall.callSPSM(finalRes, schema, schema);

		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.repairSchemas(finalRes);
		} else {
			fOut.write("Empty results from SPSM. \n\n");
			fail() ;
		}
		
		QueryData queryData = new QueryData(query) ;
		fOut.write(queryData.toString()+"\n\n") ;		
		System.out.println(queryData) ;
		
		finalRes = RepairQuery.repairQueries(finalRes, queryData, "dbpedia", null, "queryData/dbpedia/dbpedia_ontology.json",20) ;

		if(finalRes!=null){
			if(finalRes.size() == 0){
				//then we have no results so end test
				fOut.write("Empty results returned. \n\n");
				fail() ; // We expect all tests to return more than zero matches
			}else{
				MatchStruc current = finalRes.get(0);
				fOut.write("Result: \n\n" + current.getQuery() + "\n\n");
				for(String testStr: tests) {
					assertTrue(current.getQuery().contains(testStr)) ;
				}
			}
		}else{
			fOut.write("Null Results! \n\n");
			fail() ; // We expect all tests to return something
		}
	}

	
	@After
	public void cleanUp(){
		fOut.close();
	}

}
