package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import chain_source.Run_CHAIn;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

/* Author Diana Bental
 * Date 3 April 2018
 * Modified
 */

/*
 * 
 * Runs CHAIN with SAQ training data - schemas and queries
 * 
 */

public class SAQ_Training_Cases {
	
	// 1 SAQ  cosmonauts
	
	private static String target01_saq="Cosmonaut";
	
	private static String q01_saq = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX saq: <http://saq/ontology/> "
			+ "SELECT DISTINCT ?uri "
		    + "WHERE { "
		    + " ?uri rdf:type saq:Cosmonaut . "
		    + "} " ;
	
	private static String target01_res="Astronaut(nationality)";
	
	private static String q01_res = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
		    + "PREFIX dbo: <http://dbpedia.org/ontology/> "
		    + "PREFIX res: <http://dbpedia.org/resource/> "
//		    + "PREFIX saq: <http://saq/ontology/> "
		    + "SELECT DISTINCT ?uri "
		    + "WHERE { "
		    + "?uri rdf:type dbo:Astronaut . "
		    + "{ ?uri dbo:nationality res:Russia . } "
		    + "UNION "
		    + "{ ?uri dbo:nationality res:Soviet_Union . }"
		    + "}" ;
	

	// 2 SAQ German cities with more than 250000 inhabitants

	private static String q02_saq = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX saq: <http://saq/ontology/> "
			+ "SELECT DISTINCT ?uri "
			+ "WHERE { "
			+ "?uri rdf:type saq:GermanCities .  "
			+ "?uri saq:inhabitants ?inhabitants . "
			+ "FILTER ( ?inhabitants > 250000 ) } " ;

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

	private static String target02a_res = "City(country, populationTotal)" ;
	private static String target02b_res = "Town(country, populationTotal)" ;


	// 3 SAQ mayor of Berlin

	private static String q03_saq = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX saq: <http://saq/ontology/> "
			+ "SELECT DISTINCT ?uri "
			+ "WHERE { "
			+ "?uri saq:mayorOf saq:Berlin . "
	        + "} " ;

	// 3 Resolved

	String q03_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        res:Berlin dbo:leader ?uri . "
	       + "} " ;
	
	private static String target03_res = "entity(leader)";
	
	// 4 SAQ second highest mountain on Earth

	private static String q04_saq = 
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX saq: <http://saq/ontology/> "
			+ "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "?uri saq:locatedOn saq:Earth ."
	       + "?uri rdf:type saq:Mountain . "
	       + "?uri saq:height ?height . "
	       + "} "
	       + "ORDER BY DESC(?height) "
	       + "OFFSET 1 LIMIT 1 " ;

	private static String q04_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
		       + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
		       + "SELECT DISTINCT ?uri "
		       + "WHERE { "
		       + "        ?uri rdf:type dbo:Mountain . "
		       + "        ?uri dbo:elevation ?elevation . "
		       + "} "
		       + "ORDER BY DESC(?elevation) "
		       + "OFFSET 1 LIMIT 1 " ;
	
	private static String target04_res = "Mountain(elevation)" ;
	
	// 5 SAQ professional skateboarders from Sweden

	private static String q05_saq = 
		       "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
		       + "PREFIX saq: <http://saq/ontology/> "
		       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "       ?uri rdf:type saq:ProfessionalSkateboarders . "
	       + "       ?uri saq:nationality saq:Sweden ."
	       + "} " ;

	// 5 Resolved

	String q05_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
	       + "PREFIX res: <http://dbpedia.org/resource/> "
	       + "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri dbo:occupation res:Skateboarder . "
	       + "      { ?uri dbo:birthPlace res:Sweden . } "
	       + "      UNION "
	       + "      { ?uri dbo:birthPlace ?place . "
	       + "        ?place dbo:country res:Sweden . } "
	       + "} " ;

	String target05_res = "entity(occupation, birthPlace)" ;

	// 6 SAQ band leaders that play trumpet
	private static String q06_saq = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX saq: <http://saq/ontology/> "
			+ "SELECT DISTINCT ?uri "
			+ "WHERE { "
			+ "        ?uri type Bandleader . "
			+ "        ?uri play Trumpet . "
			+ "} " ;


// 6 Resolved

	private static String q06_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
       + "PREFIX res: <http://dbpedia.org/resource/> "
       + "SELECT DISTINCT ?uri "
       + "WHERE { "
       + "        ?uri dbo:occupation res:Bandleader . "
       + "        ?uri dbo:instrument res:Trumpet . "
       + "} " ;

	private static String target06_res = "entity(occupation, instrument)" ;

	// 7 SAQ countries have more than ten caves

	private static String q07_saq = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX saq: <http://saq/ontology/> "
			+ "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri rdf:type saq:Country ."
	       + "	      ?uri saq:has ?cave ."
	       + "        ?cave rdf:type saq:Cave . "
	       + "} "
	       + "GROUP BY ?uri "
	       + "HAVING(COUNT(?cave) > 10) " ;
	
	String q07_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
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


	private static String target07a_res = "Country(location)" ;
	private static String target07b_res = "Cave(location)";


	// 8 SAQ Formula 1 driver with the most races

	private static String q08_saq = 
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX saq: <http://saq/ontology/> "
			+ "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "        ?uri rdf:type saq:Formula1Driver . "
	       + "        ?uri saq:numberOfRaces ?x . "
	       + "} "
	       + "ORDER BY DESC(?x) "
	       + "OFFSET 0 LIMIT 1 " ;


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

	String target08_res = "FormulaOneRacer(races)" ;
	
	
	private static String q09_saq = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX saq: <http://saq/ontology/> "
			+ "SELECT DISTINCT ?uri "
		       + "WHERE { "
		       + "        ?uri rdf:type saq:Player . "
		       + "        ?uri saq:playsAt saq:PremierLeague . "
		       + "        ?uri saq:birthDate ?y . "
		       + "} "
		       + "ORDER BY DESC(?y) "
		       + "OFFSET 0 LIMIT 1 " ;

	
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

	private static String target09_res = "entity(team, league, birthDate)" ;
	
	// 10 SAQ longest river

	String q10_saq = 
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
					+ "PREFIX saq: <http://saq/ontology/> "
					+ "SELECT DISTINCT ?uri "
	       + "WHERE { "
	       + "       ?uri rdf:type saq:River . "
	       + "       ?uri saq:length ?l . "
	       + "} "
	       + "ORDER BY DESC(?l) "
	       + "OFFSET 0 LIMIT 1 " ;

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

		private static String target10_res = "River(length)" ;
		
		// 11 SAQ cars that are produced in Germany
		
		String q11_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						+ "SELECT DISTINCT ?uri "
					       + "WHERE { "
					       + "        ?uri rdf:type saq:Car . "
					       + "        ?uri saq:producedIn saq:Germany .  "
					       + "} " ;


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

		private static String target11_res = "Automobile(assembly, manufacturer, location, locationCountry)" ;
		
		// 12 SAQ People that were born in Vienna and died in Berlin
		
		private static String q12_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						+ "SELECT DISTINCT ?uri "
			       + "WHERE { "
			       + "		?uri rdf:type saq:Person ."
			       + "        ?uri saq:bornIn saq:Vienna . "
			       + "        ?uri saq:diedIn saq:Berlin . "
			       + "} " ;

		// 12 Resolved

		private static String q12_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
		       + "PREFIX res: <http://dbpedia.org/resource/> "
		       + "SELECT DISTINCT ?uri "
		       + "WHERE { "
		       + "        ?uri dbo:birthPlace res:Vienna . "
		       + "        ?uri dbo:deathPlace res:Berlin . "
		       + "} " ;

		private static String target12_res = "entity(birthPlace, deathPlace)" ;

		// 13 SAQ Mother and father of Prince Harry and Prince William
		private static String q13_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						+ "SELECT ?father ?mother "
			       + "WHERE { "
			       + "        saq:Prince_William saq:hasFather ?father . "
			       + "        saq:Prince_Harry saq:hasFather ?father ."
			       + "		saq:Prince_William saq:hasMother ?mother . "
			       + "        saq:Prince_Harry saq:hasMother ?mother ."
			       + "} " ;


		// 13 Resolved

		private static String q13_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
		       + "SELECT ?x"
		       + "WHERE { "
		       + "        <http://dbpedia.org/resource/Prince_William,_Duke_of_Cambridge> dbo:parent ?x . "
		       + "        <http://dbpedia.org/resource/Prince_Harry> dbo:parent ?x ."
		       + "} " ;

		private static String target13_res="entity(hasParent)" ;
		
		// 14 SAQ  latest U.S. state admitted
		private static String q14_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						+ "SELECT DISTINCT ?uri "
					       + "WHERE { "
					       + "        ?uri rdf:type saq:US_State . "
					       + "        ?uri saq:admittedAt ?x . "
					       + "} ORDER BY DESC(?x) "
					       + "OFFSET 0 LIMIT 1 " ;


		// 14 Resolved
		// Query modified so it gives results on dbpedia
		// 	Alternatively use      "        ?uri dbo:country res:United_States . "
		// Could also add a type: "        ?uri rdf:type dbo:PopulatedPlace . "

		private static String q14_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
				+ "PREFIX dbp: <http://dbpedia.org/property/> "
				+ "PREFIX res: <http://dbpedia.org/resource/> "
		       + "PREFIX yago: <http://dbpedia.org/class/yago/> "
		       + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
		       + "SELECT DISTINCT ?uri "
		       + "WHERE { "
		       + "        ?uri rdf:type yago:WikicatStatesOfTheUnitedStates . "
		       + "        ?uri dbp:admittancedate ?x . "
		       + "} ORDER BY DESC(?x) "
		       + "OFFSET 0 LIMIT 1 " ;


		private static String target14_res = "WikicatStatesOfTheUnitedStates(admittancedate)" ;

		// 15 SAQ number of languages spoken in Turkmenistan
		// Modify (remove COUNT) so Jena can parse 
		private static String q15_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						// + "SELECT COUNT(DISTINCT ?x) "
						+ "SELECT DISTINCT ?x "
					       + "WHERE { "
					       + "		?x rdf:type saq:Language ."
					       + "		?x saq:spokenIn saq:Turkmenistan ."
					       + "}" ;

		// 15 Resolved 

		//Query modified because JENA doesn't accept this syntax for counting
		private static String q15_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
		       + "PREFIX res: <http://dbpedia.org/resource/> "
//		       + "SELECT COUNT(DISTINCT ?x) "
				+ "SELECT DISTINCT ?x "
		       + "WHERE { "
		       + "        res:Turkmenistan dbo:language ?x . "
		       + "} " ;

		private static String target15_res = "entity(language)" ;

		// 16 SAQ movies directed by Francis Ford Coppola
		String q16_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						+ "SELECT DISTINCT ?uri "
			       + "WHERE { "
			       + "        ?uri rdf:type saq:Movie . "
			       + "        ?uri saq:directedBy saq:Francis_Ford_Coppola . "
			       + "} " ;


		// 16 Resolved

		private static String q16_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
		       + "PREFIX res: <http://dbpedia.org/resource/> "
		       + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
		       + "SELECT DISTINCT ?uri "
		       + "WHERE { "
		       + "        ?uri rdf:type dbo:Film . "
		       + "        ?uri dbo:director res:Francis_Ford_Coppola . "
		       + "} " ;

		private static String target16_res = "Film(director(Francis_Ford_Coppola))" ;

		// 17 SAQ  maiden name of Angela Merkel
		
		private static String q17_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						+ "SELECT DISTINCT ?string "
			       + "WHERE { "
			       + "        saq:Angela_Merkel saq:maidenName ?string . "
			       + "} " ;

		

		// 17 Resolved

		private static String q17_res = "PREFIX dbp: <http://dbpedia.org/property/> "
		       + "PREFIX res: <http://dbpedia.org/resource/> "
		       + "SELECT DISTINCT ?string "
		       + "WHERE { "
		       + "        res:Angela_Merkel dbp:birthName ?string . "
		       + "} " ;

		private static String target17_res = "entity(birthName)" ;

		// 18 SAQ Methodist politicians
		private static String q18_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX saq: <http://saq/ontology/> "
				+ "SELECT DISTINCT ?uri "
			       + "WHERE { "
			       + "        ?uri rdf:type saq:MethodistPolitician . "
			       + "}" ;



		// 18 Resolved

		private static String q18_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
		       + "PREFIX res: <http://dbpedia.org/resource/> "
		       + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
		       + "SELECT DISTINCT ?uri "
		       + "WHERE { "
		       + "        ?uri rdf:type dbo:Politician . "
		       + "        ?uri dbo:religion res:Methodism . "
		       + "} " ;

		private static String target18_res= "Politician(religion)" ;

		// 19 SAQ number of times that Jane Fonda married
		
		// Edited to allow JENA to parse
		private static String q19_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						+ "SELECT DISTINCT ?uri "
				// "SELECT COUNT(DISTINCT ?uri) "
			       + "WHERE { "
			       + "        saq:Jane_Fonda saq:marriedTo ?uri . "
			       + "} " ;


		// 19 Resolved
		// Edited to allow JENA to parse
		private static String q19_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
		       + "PREFIX res: <http://dbpedia.org/resource/> "
		       // + "SELECT COUNT(DISTINCT ?uri) "
		       + "SELECT DISTINCT ?uri "
		       + "WHERE { "
		       + "        res:Jane_Fonda dbo:spouse ?uri . "
		       + "} " ;

		private static String target19_res = "entity(spouse)" ;

		// 20 SAQ Australian nonprofit organizations
		private static String q20_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						+ "SELECT DISTINCT ?uri "
					       + "WHERE { "
					       + "        ?uri rdf:type saq:NonprofitOrganization . "
					       + "        ?uri saq:country saq:Australia . "
					       + "} " ;

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

		private static String target20_res = "Nonprofit_organization(locationCountry)" ;
		// private static String target20_res = "Nonprofit_Organization(location)" ;

		// 21 SAQ Military conflicts in which Lawrence of Arabia participated

		private static String q21_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						+ "SELECT DISTINCT ?uri "
					       + "WHERE { "
					       + "        saq:Lawrence_of_Arabia saq:militaryConflicts ?uri . "
					       + "} " ;
		// 21 Resolved

		private static String q21_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
		       + "PREFIX res: <http://dbpedia.org/resource/> "
		       + "SELECT DISTINCT ?uri "
		       + "WHERE { "
		       + "        res:T._E._Lawrence dbo:battle ?uri . "
		       + "} " ;
		       
		private static String target21_res = "entity(battle)" ;

		// 22 SAQ  number of inhabitants in Maribor
		private static String q22_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						+ "SELECT DISTINCT ?num "
					       + "WHERE { "
					       + "        saq:Maribor saq:numberOfInhabitants ?num . "
					       + "} " ;

		// 22 Resolved

		private static String q22_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
		       + "PREFIX res: <http://dbpedia.org/resource/> "
		       + "SELECT DISTINCT ?num "
		       + "WHERE { "
		       + "        res:Maribor dbo:populationTotal ?num . "
		       + "} " ;

		private static String target22_res = "entity(populationTotal)" ;

		// 23 SAQ  companies in Munich
		private static String q23_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						+ "SELECT DISTINCT ?uri "
					       + "WHERE { "
					       + "        ?uri rdf:type saq:MunichCompany . "
					       + "} " ;

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

		private static String target23_res = "Company(location, headquarter, locationCity)" ;
		// private static String target23_res = "Company(headquarter)" ;
		// private static String target23_res = "Company(locationCity)" ;

		// 24 SAQ  games developed by GMT
		private static String q24_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						+ "SELECT DISTINCT ?uri "
					       + "WHERE { "
					       + "        ?uri rdf:type saq:Games . "
					       + "        ?uri saq:developedBy saq:GMT . "
					       + "} " ;

		// 24 Resolved

		private static String q24_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
		       + "PREFIX res: <http://dbpedia.org/resource/> "
		       + "SELECT DISTINCT ?uri "
		       + "WHERE { "
		       + "        ?uri dbo:publisher res:GMT_Games . "
		       + "} " ;

		private static String target24_res = "entity(publisher)" ;
		
		// 25 SAQ  husband of Amanda Palmer
		private static String q25_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						+ "SELECT DISTINCT ?uri "
					       + "WHERE { "
					       + "        ?uri saq:husbandOf saq:Amanda_Palmer . "
					       + "} " ;


		// 25 Resolved
		// This query does not return results

		private static String q25_res = "PREFIX dbp: <http://dbpedia.org/property/> "
		       + "PREFIX res: <http://dbpedia.org/resource/> "
		       + "SELECT DISTINCT ?uri "
		       + "WHERE { "
		       + "        res:Amanda_Palmer dbp:spouse ?uri . "
		       + "} " ;

		private static String target25_res = "entity(spouse)" ;


		// 26 SAQ  countries connected by the Rhine
		private static String q26_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						+ "SELECT DISTINCT ?uri "
					       + "WHERE { "
					       + "      ?uri rdf:type saq:Country ."
					       + "		?uri saq:hasRiver saq:Rhine .		"
					       + "} " ;

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

		private static String target26_res =  "Country(label)" ;
		
		// 27 SAQ   professional surfers born in Australia
		// Changed to Italy, to give result for resolved query
		
		private static String q27_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						+ "SELECT DISTINCT ?uri "
					       + "WHERE { "
					       + "        ?uri type ProfessionalSurfers .  "
					       + "        ?uri bornIn Italy .  "
					       + "} " ;
		
		// 27 Resolved

		private static String q27_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
		       + "PREFIX res: <http://dbpedia.org/resource/> "
		       + "SELECT DISTINCT ?uri "
		       + "WHERE { "
		       + "        ?uri dbo:occupation res:Surfing .  "
		       + "       { ?uri dbo:birthPlace res:Italy . }"
		       + "       UNION "
		       + "       { ?uri dbo:birthPlace ?x . "
		       + "         ?x dbo:country res:Italy . }"
		       + "} " ;

		private static String target27_res = "entity(occupation, birthPlace, country)" ;
		
		// 28 SAQ  islands that belong to Japan
		private static String q28_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						+ "SELECT DISTINCT ?uri "
					       + "WHERE { "
					       + "        ?uri rdf:type saq:Island .  "
					       + "        ?uri saq:belongTo saq:Japan . "
					       + "} " ;


		// 28 Resolved

		private static String q28_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
		       + "PREFIX res: <http://dbpedia.org/resource/> "
		       + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
		       + "SELECT DISTINCT ?uri "
		       + "WHERE { "
		       + "        ?uri rdf:type dbo:Island .  "
		       + "        ?uri dbo:country res:Japan . "
		       + "} " ;

		private static String target28_res = "Island(country)" ;


		// 29 SAQ  ruling party in Lisbon
		private static String q29_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						+ "SELECT DISTINCT ?uri "
					       + "WHERE { "
					       + "        Lisbon rulingParty ?uri . "
					       + "} " ;

		// 29 resolved

		private static String q29_res = "PREFIX dbp: <http://dbpedia.org/property/> "
		       + "PREFIX res: <http://dbpedia.org/resource/> "
		       + "SELECT DISTINCT ?uri "
		       + "WHERE { "
		       + "        res:Lisbon dbp:leaderParty ?uri . "
		       + "} " ;

		private static String target29_res = "entity(leaderParty))" ;

		// 30 SAQ  Apollo 14 astronauts
		private static String q30_saq = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX saq: <http://saq/ontology/> "
						+ "SELECT DISTINCT ?uri "
					       + "WHERE { "
					       + "        ?uri type Apollo14Astronauts . "
					       + "} " ;


		// 30 resolved
		private static String q30_res = "PREFIX dbo: <http://dbpedia.org/ontology/> "
		       + "PREFIX res: <http://dbpedia.org/resource/> "
		       + "SELECT DISTINCT ?uri "
		       + "WHERE { "
		       + "        ?uri dbo:mission res:Apollo_14 . "
		       + "} " ;

		private static String target30_res ="entity(mission)" ;



		


		

	

	private static final int UNKNOWNSTATUS = 0 ;
	private static final int INITIALQUERYSUCCESS = 5 ;
	private static final int INVALIDQUERY = 6 ;
	private static final int SPSMFAILURE = 7 ;
	private static final int NOMATCHESFROMSPSM = 8 ;
	private static final int REPAIREDQUERYRUNERROR = 9 ;
	private static final int REPAIREDQUERYRESULTS = 10 ;
	private static final int REPAIREDQUERYNORESULTS = 11 ;
	private static final int DATAREPAIREDWITHRESULTS = 12 ;

	private String queryType;
	private Run_CHAIn chain = new Run_CHAIn();
	
	//for writing results
	private static File testRes;
	private PrintWriter fOut;
	private static boolean alreadyWritten;
	
	
	
	@BeforeClass
	public static void beforeAll(){
		System.out.println("These tests use Run_CHAIn.java on the  SAQ training set");
		System.out.println("\nThe results from these tests can be found in outputs/testing/SAQ_Training_Cases.txt\n");

		alreadyWritten = false;
		try{
			testRes = new File("outputs/testing/SAQ_Training_Cases.txt");
			testRes.createNewFile();
			
			new PrintWriter("outputs/testing/SAQ_Training_Cases.txt").close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Before
	public void setup(){
		queryType="";
		
		try{
			fOut = new PrintWriter(new FileWriter(testRes,true));
			
			if(alreadyWritten==false){
				fOut.write("Results for SAQ_Training_Cases.java\n\n");
				alreadyWritten = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	


	@Test
	public void test_01_a_res() {
		runSAQ("Test 01_a_res", q01_res,  target01_res, INITIALQUERYSUCCESS);
	}
	
	@Test
	public void test_01_b_saq() {
		runSAQ("Test 01_c_saq", q01_saq,  target01_res, REPAIREDQUERYRESULTS);
	}
	
	
	@Test
	public void test_02_a_res() {
		runSAQ("Test 02_a_res", q02_res,  target02a_res, INITIALQUERYSUCCESS);
	}
	
	@Test
	public void test_02_b_res() {
		runSAQ("Test 02_b_res", q02_res,  target02b_res, INITIALQUERYSUCCESS);
	}
	
	@Test
	public void test_02_c_saq() {
		runSAQ("Test 02_c_saq", q02_saq,  target02a_res, REPAIREDQUERYRESULTS);
	}
	
	@Test
	public void test_02_d_saq() {
		runSAQ("Test 02_d_saq", q02_saq,  target02b_res, NOMATCHESFROMSPSM);
	}
	
	// Test 3 - The resolved query does not return any results (it should)
	// Neither resolved nor SAQ can be parsed into a schema.
	
	@Test
	public void test_03_a_res() {
		runSAQ("Test 03_a_res", q03_res,  target03_res, INVALIDQUERY);
	}
	
	
	@Test
	public void test_03_b_saq() {
		runSAQ("Test 03_b_saq", q03_saq,  target03_res, INVALIDQUERY);
		}
	
	@Test
	public void test_04_a_res() {
		runSAQ("Test 04_a_res", q04_res,  target04_res, INITIALQUERYSUCCESS);
	}
	
	
	@Test
	public void test_04_b_saq() {
		runSAQ("Test 04_b_saq", q04_saq,  target04_res, DATAREPAIREDWITHRESULTS);
		}
	
	@Test
	public void test_05_a_res() {
		runSAQ("Test 05_a_res", q05_res,  target05_res, INITIALQUERYSUCCESS);
	}
	
	
	@Test
	public void test_05_b_saq() {
		runSAQ("Test 05_b_saq", q05_saq,  target05_res, NOMATCHESFROMSPSM);
		}
	
	
	@Test
	public void test_06_a_res() {
		runSAQ("Test 06_a_res", q06_res,  target06_res, INITIALQUERYSUCCESS);
	}
	
	
	@Test
	public void test_06_b_saq() {
		runSAQ("Test 06_b_saq", q06_saq,  target06_res, INVALIDQUERY);
		}
	
	
	
	@Test
	public void test_07_a_res() {
		runSAQ("Test 07_a_res", q07_res,  target07a_res, INITIALQUERYSUCCESS);
	}
	
	@Test
	public void test_07_b_res() {
		runSAQ("Test 07_b_res", q07_res,  target07b_res, INITIALQUERYSUCCESS);
	}
	
	@Test
	public void test_07_c_saq() {
		runSAQ("Test 07_c_saq", q07_saq,  target07a_res, REPAIREDQUERYRESULTS);
	}
	
	@Test
	public void test_07_d_saq() {
		runSAQ("Test 07_d_saq", q07_saq,  target07b_res, NOMATCHESFROMSPSM);
	}
	
	
	@Test
	public void test_08_a_res() {
		runSAQ("Test 08_a_res", q08_res,  target08_res, INITIALQUERYSUCCESS);
	}
	
	
	@Test
	public void test_08_b_saq() {
		runSAQ("Test 08_b_saq", q08_saq,  target08_res, REPAIREDQUERYRESULTS);
		}
	
	@Test
	public void test_09_a_res() {
		runSAQ("Test 09_a_res", q09_res,  target09_res, INVALIDQUERY);
	}
	
	
	@Test
	public void test_09_b_saq() {
		runSAQ("Test 09_b_saq", q09_saq,  target09_res, NOMATCHESFROMSPSM);
		}
	
	@Test
	public void test_10_a_res() {
		runSAQ("Test 10_a_res", q10_res,  target10_res, INITIALQUERYSUCCESS);
	}
	
	
	@Test
	public void test_10_b_saq() {
		runSAQ("Test 10_b_saq", q10_saq,  target10_res, REPAIREDQUERYRESULTS);
		}
	
	@Test
	public void test_11_a_res() {
		runSAQ("Test 11_a_res", q11_res,  target11_res, INITIALQUERYSUCCESS);
	}
	
	@Test
	public void test_11_b_saq() {
		runSAQ("Test 11_b_saq", q11_saq,  target11_res, REPAIREDQUERYRESULTS);
	}
	
	@Test
	public void test_12_a_res() {
		runSAQ("Test 12_a_res", q12_res,  target12_res, INITIALQUERYSUCCESS);
	}
	
	@Test
	public void test_12_b_saq() {
		runSAQ("Test 12_b_saq", q12_saq,  target12_res, NOMATCHESFROMSPSM);
	}

@Test
	public void test_13_a_res() {
		runSAQ("Test 13_a_res", q13_res,  target13_res, INITIALQUERYSUCCESS);
	}

@Test
public void test_13_b_saq() {
	runSAQ("Test 13_b_saq", q13_saq,  target13_res, INVALIDQUERY);
}

@Test
	public void test_14_a_res() {
		runSAQ("Test 14_a_res", q14_res,  target14_res, INITIALQUERYSUCCESS);
	}

@Test
public void test_14_b_saq() {
	runSAQ("Test 14_b_saq", q14_saq,  target14_res, NOMATCHESFROMSPSM);
}

@Test
	public void test_15_a_res() {
		runSAQ("Test 15_a_res", q15_res,  target15_res, INITIALQUERYSUCCESS);
	}

@Test
public void test_15_b_saq() {
	runSAQ("Test 15_b_saq", q15_saq,  target15_res, NOMATCHESFROMSPSM);
}


@Test
public void test_16_a_res() {
	runSAQ("Test 16_a_res", q16_res,  target16_res, INITIALQUERYSUCCESS);
}

@Test
public void test_16_b_saq() {
runSAQ("Test 16_b_saq", q16_saq,  target16_res, REPAIREDQUERYRESULTS);
}

@Test
public void test_17_a_res() {
	runSAQ("Test 17_a_res", q17_res,  target17_res, INITIALQUERYSUCCESS);
}

@Test
public void test_17_b_saq() {
runSAQ("Test 17_b_saq", q17_saq,  target17_res, INVALIDQUERY);
}

@Test
public void test_18_a_res() {
	runSAQ("Test 18_a_res", q18_res,  target18_res, INITIALQUERYSUCCESS);
}

@Test
public void test_18_b_saq() {
	runSAQ("Test 18_b_saq", q18_saq,  target18_res, REPAIREDQUERYRESULTS);
}

@Test
public void test_19_a_res() {
	runSAQ("Test 19_a_res", q19_res,  target19_res, INITIALQUERYSUCCESS);
}

@Test
public void test_19_b_saq() {
	runSAQ("Test 19_b_saq", q19_saq,  target19_res, INVALIDQUERY);
}

@Test
public void test_20_a_res() {
	runSAQ("Test 20_a_res", q20_res,  target20_res, INITIALQUERYSUCCESS);
}

@Test
public void test_20_b_saq() {
	runSAQ("Test 20_b_saq", q20_saq,  target20_res, REPAIREDQUERYNORESULTS);
}

@Test
public void test_21_a_res() {
	runSAQ("Test 21_a_res", q21_res,  target21_res, INITIALQUERYSUCCESS);
}

@Test
public void test_21_b_saq() {
	runSAQ("Test 21_b_saq", q21_saq,  target21_res, INVALIDQUERY);
}

@Test
public void test_22_a_res() {
	runSAQ("Test 22_a_res", q22_res,  target22_res, INITIALQUERYSUCCESS);
}

@Test
public void test_22_b_saq() {
	runSAQ("Test 22_b_saq", q22_saq,  target22_res, INVALIDQUERY);
}

@Test
public void test_23_a_res() {
	runSAQ("Test 23_a_res", q23_res,  target23_res, INITIALQUERYSUCCESS);
}

@Test
public void test_23_b_saq() {
	runSAQ("Test 23_b_saq", q23_saq,  target23_res, REPAIREDQUERYRESULTS);
}

@Test
public void test_24_a_res() {
	runSAQ("Test 24_a_res", q24_res,  target24_res, INITIALQUERYSUCCESS);
}

@Test
public void test_24_b_saq() {
	runSAQ("Test 24_b_saq", q24_saq,  target24_res, NOMATCHESFROMSPSM);
}

@Test
public void test_25_a_res() {
	runSAQ("Test 25_a_res", q25_res,  target25_res, INVALIDQUERY);
}

@Test
public void test_25_b_saq() {
	runSAQ("Test 25_b_saq", q25_saq,  target25_res, INVALIDQUERY);
}

@Test
public void test_26_a_res() {
	runSAQ("Test 26_a_res", q26_res,  target26_res, REPAIREDQUERYRESULTS);
}

@Test
public void test_26_b_saq() {
	runSAQ("Test 26_b_saq", q26_saq,  target26_res, REPAIREDQUERYRESULTS);
}

@Test
public void test_27_a_res() {
	runSAQ("Test 27_a_res", q27_res,  target27_res, INITIALQUERYSUCCESS);
}

@Test
public void test_27_b_saq() {
	runSAQ("Test 27_b_saq", q27_saq,  target27_res, INVALIDQUERY);
}

@Test
public void test_28_a_res() {
	runSAQ("Test 28_a_res", q28_res,  target28_res, INITIALQUERYSUCCESS);
}

@Test
public void test_28_b_saq() {
	runSAQ("Test 28_b_saq", q28_saq,  target28_res, REPAIREDQUERYRESULTS);
}

@Test
public void test_29_a_res() {
	runSAQ("Test 29_a_res", q29_res,  target29_res, INITIALQUERYSUCCESS);
}

@Test
public void test_29_b_saq() {
	runSAQ("Test 29_b_saq", q29_saq,  target29_res, INVALIDQUERY);
}

@Test
public void test_30_a_res() {
	runSAQ("Test 30_a_res", q30_res,  target30_res, INITIALQUERYSUCCESS);
}

@Test
public void test_30_b_saq() {
	runSAQ("Test 30_b_saq", q30_saq,  target30_res, INVALIDQUERY);
}


	public void runSAQ(String testID, String query, String targetSchemas, int expectedOutcome){
		
		System.out.println("\nRunning test " + testID);
		
		fOut.write("Test " + testID + "\n\n");
		fOut.write("Query " +  query + "\n\n");
		fOut.write("Schema: " + targetSchemas +"\n\n") ;
		
		System.out.println("Query " +  query + "\n");
		System.out.println("Schema: " + targetSchemas +"\n") ;
		
		
		queryType="dbpedia";
		
		String dataDir = null;
		String ontologyPath = "queryData/dbpedia/dbpedia_ontology.json";
		
		
//		int status = chain.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, 0.5, 5, fOut);
		int status = chain.runCHAIn(query, queryType, targetSchemas, dataDir, ontologyPath, 10, 0, 5, fOut);
		
		System.out.println("Status: " +  status + "\n");
		assertTrue(status == expectedOutcome) ;
	}
	
	@After
	public void cleanUp(){
		fOut.close();
	}

}
