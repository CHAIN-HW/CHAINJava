package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import chain_source.Create_Query;
import chain_source.Match_Struc;
import chain_source.Query_Data;
import chain_source.Repair_Query;
import chain_source.Repair_Schema;

/* Author Tanya Howden
 * Author Diana Bental
 * Date September 2017
 * Modified January 2017
 */

/*
 * Responsible for testing Create_Query.java to ensure
 * that after calling SPSM and repairing the target schema
 * we can create the correct sepa or dbpedia query
 * 
 * The input is a query and a schema; information about data is extracted from the query
 * 
 * For most of these tests the original query and the result query should be the same
 * though terms may be in a different order.
 * Q5.3.2. tests the effect where the schema contains a property that is not in the original
 * query; in this case the extra property is included as an unbound variable.
 * 
 * These tests cover three different ways to create a repaired query:
 * 
 * testQueryCreation - creates a new simple query directly from the repaired schema plus data
 * 						matching taken from the original query
 * testOpenQueryCreation - creates a new simple query directly from the repaired schema.
 * 						There are no matching constraints on the data (just variables)
 * testQueryRepair - repairs the original query using the match data from SPSM
 * 
 * 
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Create_Query_Test_Cases {
	
	private static String Q5_1_1 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
			+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
			+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
			+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
			+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
			+ "SELECT *  \n"
			+ "FROM <queryData/sepa/sepa_datafiles/waterBodyPressures.n3>\n"
			+ "WHERE {\n ?id sepaw:dataSource ?dataSource;\n"
			+ "sepaw:identifiedDate  ?identifiedDate  ;\n"
			+ "sepaw:affectsGroundwater ?affectsGroundwater ;\n"
			+ "sepaw:waterBodyId ?waterBodyId .}" ;
	
	private static String T5_1_1 = "sepaw:affectsGroundwater ?affectsGroundwater" ;
	
	private static String Q5_1_2 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
			+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
			+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
			+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
			+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
			+ "SELECT *  \n"
			+ "FROM <queryData/sepa/sepa_datafiles/water.n3>\n"
			+ "WHERE { ?id sepaw:timePeriod ?timePeriod;\n"
			+ "geo:geo ?geo  ;\n"
			+ "sepaw:measure ?measure ;\n"
			+ "sepaw:resource ?resource .}" ;
	
	private static String T5_1_2 = "geo:geo ?geo" ;
	
	private static String Q5_1_3 = 	 "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
     + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
     + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
     + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
     + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
     + "SELECT *  \n"
     + "FROM <queryData/sepa/sepa_datafiles/waterBodyMeasures.n3>\n"
     + "WHERE { ?id sepaw:timePeriod ?timePeriod;\n"
     + "geo:geo ?geo  ;\n"
     + "sepaw:measure ?measure ;\n"
     + "sepaw:resource ?resource .}" ;
	
	private static String T5_1_3 = "sepaw:measure ?measure" ;

	private static String Q5_1_4 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
          	+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
          	+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
          	+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
          	+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
          	+ "SELECT *  \n"
          	+ "FROM <queryData/sepa/sepa_datafiles/waterBodyPressures.n3>\n"
          	+ "WHERE { ?id sepaw:identifiedDate ?identifiedDate;\n"
          	+ "sepaw:waterBodyId ?waterBodyId  ;\n"
          	+ "sepaw:assessmentCategory ?assessmentCategory ;\n"
          	+ "sepaw:source ?source .}" ;
	
	private static String T5_1_4 = "sepaw:identifiedDate ?identifiedDate" ;
	
	private static String Q5_1_5 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          + "SELECT *  \n"
	          + "FROM <queryData/sepa/sepa_datafiles/waterBodyMeasures.n3>\n"
	          + "WHERE { ?id sepaw:waterBodyId ?waterBodyId;\n"
	          + "sepaw:secondaryMeasure ?secondaryMeasure  ;\n"
	          + "sepaw:dataSource ?dataSource .}" ;
	
	private static String T5_1_5 = "sepaw:dataSource ?dataSource" ;
	
	private static String Q5_1_6 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          + "SELECT *  \n"
	          + "FROM <queryData/sepa/sepa_datafiles/surfaceWaterBodies.n3>\n"
	          + "WHERE { ?id sepaw:riverName ?riverName;\n"
	          + "sepaw:associatedGroundwaterId ?associatedGroundwaterId .}" ;
	
	private static String T5_1_6 = "FROM <queryData/sepa/sepa_datafiles/surfaceWaterBodies.n3>" ;
	
	private static String Q5_1_7 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          + "SELECT *  \n"
	          + "FROM <queryData/sepa/sepa_datafiles/bathingWaters.n3>\n"
	          + "WHERE { ?id sepaloc:catchment ?catchment;\n"
	          + "sepaloc:localAuthority ?localAuthority  ;\n"
	          + "geo:lat ?lat ;\n"
	          + "geo:long ?long .}" ;
	
	private static String T5_1_7 = "?id" ;
	
	
	private static String Q5_1_8 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          + "SELECT *  \n"
	          + "FROM <queryData/sepa/sepa_datafiles/surfaceWaterBodies.n3>\n"
	          + "WHERE { ?id sepaw:altitudeTypology ?altitudeTypology;\n"
	          + "sepaw:associatedGrounwaterId ?associatedGroundwaterId  ;\n"
	          + "sepaw:riverName ?riverName ;\n"
	          + "sepaw:subBasinDistrict ?subBasinDistrict .}" ;
	
	private static String T5_1_8 = "sepaw:riverName ?riverName" ;
	
	private static String Q5_1_9 = 	"PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
	          + "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
	          + "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
	          + "SELECT *  \n"
	          + "FROM <queryData/sepa/sepa_datafiles/bathingWaters.n3>\n"
	          + "WHERE { ?id sepaw:bathingWaterId ?bathingWaterId;\n"
	          + ".}" ;
	
	private static String T5_1_9 = "sepaw:bathingWaterId ?bathingWaterId" ;
	
	private static String Q5_2_1 =	"PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
			+ "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
			+ "PREFIX  res: <http://dbpedia.org/resource/> \n"
			+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
			+ "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
			+ "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
			+ "SELECT DISTINCT *  \n"
			+ "WHERE { ?id rdf:type dbo:City ;\n"
			+ "dbo:country ?country ;\n"
			+ "dbo:populationTotal ?populationTotal .}\n"
			+ "LIMIT 20" ;
	
	private static String T5_2_1 = "dbo:populationTotal ?populationTotal" ;
	
	private static String Q5_2_2 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
			+ "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
			+ "PREFIX  res: <http://dbpedia.org/resource/> \n"
			+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
			+ "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
			+ "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
			+ "SELECT DISTINCT *  \n"
			+ "WHERE { ?id rdf:type dbo:City .}\n"
			+ "LIMIT 20" ;
	
	private static String T5_2_2 = "?id rdf:type dbo:City" ;
	
	private static String Q5_2_3 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type Astronaut ;\n"
	          + "dbo:nationality ?nationality ;\n"
	          + ".}\n"
	          + "LIMIT 20";
	private static String T5_2_3 = "dbo:nationality ?nationality" ;
	
	private static String Q5_2_4 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:Mountain ;\n"
	          + "dbo:elevation ?elevation ;\n"
	          + ".}\n"
	          + "LIMIT 20" ;
	private static String T5_2_4 = "SELECT DISTINCT *" ;
	
	private static String Q5_2_5 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:Person ;\n"
	          + "dbo:occupation ?occupation ;\n"
	          + "dbo:birthPlace ?birthPlace .}\n"
	          + "LIMIT 20" ;
	
	private static String T5_2_5 = "dbp: <http://dbpedia.org/property/>" ;
	
	private static String Q5_2_6 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:Person ;\n"
	          + "dbo:occupation ?occupation ;\n"
	          + "dbo:instrument ?instrument .}\n"
	          + "LIMIT 20" ;
	private static String T5_2_6 = "dbo:instrument ?instrument" ;
	
	
	private static String Q5_2_7 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:Cave ;\n"
	          + "dbo:location ?location ;\n"
	          + ".}\n"
	          + "LIMIT 20" ;
	private static String T5_2_7 = "rdf:type dbo:Cave" ;
	
	private static String Q5_2_8 =  "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:FormulaOneRacer ;\n"
	          + "dbo:races ?races ;\n"
	          + ".}\n"
	          + "LIMIT 20" ;
	private static String T5_2_8 = "dbo:races ?races" ;
	
	private static String Q5_2_9 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:River ;\n"
	          + "dbo:length ?length ;\n"
	          + ".}\n"
	          + "LIMIT 20" ;
	private static String T5_2_9 = "dbo:length ?length" ;
	
	private static String Q5_2_10 =  "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:Royalty ;\n"
	          + "dbo:parent ?parent ;\n"
	          + ".}\n"
	          + "LIMIT 20" ;
	private static String T5_2_10 = "dbo:parent ?parent" ;
	
	private static String Q5_2_11 = "" ;
	private static String T5_2_11 = "" ;
			
	private static String Q5_2_12 = "" ;
	private static String T5_2_12 = "" ;
	
	private static String Q5_2_13 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:River;\n"
	          + "dbo:Mountain ?Mountain ;\n"
	          + ".}\n"
	          + "LIMIT 20" ;
	private static String T5_2_13 = "dbo:Mountain ?Mountain" ;
	

	private static String Q5_3_1 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
          	+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
          	+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
          	+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
          	+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
          	+ "SELECT *  \n"
          	+ "FROM <queryData/sepa/sepa_datafiles/waterBodyPressures.n3>\n"
          	+ "WHERE { ?id sepaw:identifiedDate \"2009-09-03\";\n"
          	+ "sepaw:waterBodyId sepaidw:100053  ;\n"
          	+ "sepaw:assessmentCategory ?assessmentCategory ;\n"
          	+ "sepaw:source \"SEPA\" .}" ;
	private static String T5_3_1 = "sepaw:identifiedDate \"2009-09-03\"" ;
	
	private static String T5_3_2 = "sepaw:affectsGroundwater ?affectsGroundwater" ;
	
	
	private static String Q5_3_3 =  "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
	          + "PREFIX  dbp: <http://dbpedia.org/property/>   \n"
	          + "PREFIX  res: <http://dbpedia.org/resource/> \n"
	          + "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
	          + "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
	          + "PREFIX yago: <hhtp://dbpedia.org/class/yaho/> \n\n"
	          + "SELECT DISTINCT *  \n"
	          + "WHERE { ?id rdf:type dbo:FormulaOneRacer ;\n"
	          + "dbo:races \"202\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger> ;\n"
	          + ".}\n"
	          + "LIMIT 20" ;
	private static String T5_3_3 = "dbo:races \"202\"^^<http://www.w3.org/2001/XMLSchema#nonNegativeInteger>" ;
	
	private static String T5_3_4 = "sepaw:identifiedDate ?identifiedDate" ;
	
	private static String T5_3_5 = "dbo:races ?races" ;
	
	// Sepa query with data in it
	private static String Q5_3_6 = "PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
          	+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
          	+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
          	+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
          	+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
          	+ "SELECT *  \n"
          	+ "FROM <queryData/sepa/sepa_datafiles/waterBodyPressures.n3>\n"
          	+ "WHERE { ?id sepaw:identifiedDate \"2008-04-01\";\n"
          	+ "sepaw:waterBodyId sepaidw:20304  ;\n"
          	+ "sepaw:assessmentCategory ?assessmentCategory ;\n"
          	+ "sepaw:source \"Lake\" .}" ;
	
	private static String T5_3_6 = "sepaw:identifiedDate \"2008-04-01\"" ;
	
	private static String T5_3_7 = "sepaw:identifiedDate ?identifiedDate" ;
	
	// Sepa query with non-matching data in it (can create a query but will require data repair to run)
	private static String Q5_3_8 ="PREFIX  geo:  <http://www.w3.org/2003/01/geo/wgs84_pos#> \n"
          	+ "PREFIX  sepaidw: <http://data.sepa.org.uk/id/Water/>   \n"
          	+ "PREFIX  sepaidloc: <http://data.sepa.org.uk/id/Location/> \n"
          	+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
          	+ "PREFIX  sepaw: <http://data.sepa.org.uk/ont/Water#> \n"
          	+ "SELECT *  \n"
          	+ "FROM <queryData/sepa/sepa_datafiles/waterBodyPressures.n3>\n"
          	+ "WHERE { ?id sepaw:identifiedDate \"2008-04-01\";\n"
          	+ "sepaw:waterBodyId sepaidw:20308xxx  ;\n" // wont match
          	+ "sepaw:assessmentCategory ?assessmentCategory ;\n"
          	+ "sepaw:source \"Lake\" .}" ;
	
	private static String T5_3_8 = "sepaw:waterBodyId <http://data.sepa.org.uk/id/Water/20308xxx>" ;
	
	private static String T5_3_9 = "sepaw:waterBodyId ?waterBodyId" ;
	
	private static String Q5_4_2 = "PREFIX  dbo:  <http://dbpedia.org/ontology/> \n"
			+ "PREFIX dbp: <http://dbpedia.org/property/>   \n"
			+ "PREFIX res: <http://dbpedia.org/resource/> \n"
			+ "PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
			+ "PREFIX  foaf: <http://xlmns.com/foaf/0.1/> \n"
			+ "PREFIX yago: <hhtp://dbpedia.org/class/yago/> \n\n"
			+ "SELECT DISTINCT *  \n"
			+ "WHERE { ?id rdf:type dbo:River;\n"
			+ "dbp:Mountain ?Mountain ;\n"
			+ "dbp:location dbo:USA ; \n"
			+ "dbp:explorer dbo:Livingston; \n"
			+ "dbp:elevation 1000 ;\n"
			+ ".}\n"
			+ "LIMIT 20" ;
	private static String T5_4_2 = "dbo:location dbo:USA" ;
	
	private static String Q5_4_3 = "PREFIX dbot: <http://dbpedia.org/ontology/>  \n"
			+ "PREFIX res: <http://dbpedia.org/resource/>   \n"
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>   \n"
			+ "SELECT DISTINCT ?uri   \n"
			+ "WHERE {   \n"
			+ "    ?uri rdf:type dbot:City .   \n"
			+ "    ?uri dbot:country res:Germany .   \n"
			+ "    ?uri dbot:populationTotal ?population .   \n"
			+ "    FILTER ( ?population > 250000 ) }   \n" ;
	
	private static String T5_4_3 = " FILTER ( ?population > 250000 )";
	
	private static String T5_4_4 = "?uri dbo:country res:Germany";
	
	private static String Q5_4_5 = "PREFIX dbo: <http://dbpedia.org/ontology/>  \n"
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  \n"
			+ "SELECT DISTINCT ?uri  \n"
			+ "WHERE {  \n"
			+ "   ?uri rdf:type dbo:Mountain .  \n"
			+ "   ?uri dbo:elevation ?elevation .  \n"
			+ "}  \n"
			+ "ORDER BY DESC(?elevation)  \n"
			+ "OFFSET 1 LIMIT 1  \n" ;
	
	
	private static String T5_4_5 = "DESC(?elevation)" ;
	
	private Call_SPSM spsmCall;
	private Repair_Schema getRepairedSchema;
	private Create_Query createQuery;
	
	private ArrayList<Match_Struc> finalRes;
	private String schema;
	
	//for writing results
	private static File testRes;
	private PrintWriter fOut;
	private static boolean alreadyWritten;
	
	@BeforeClass
	public static void beforeAll(){
		System.out.println("These tests are responsible for testing Create_Query.java and Repair_Query.java to ensure that\n"
				+"after calling SPSM and repairing the target schema, we can create the correct sepa or dbpedia query.");
		System.out.println("\nThe results from these tests can be found in outputs/testing/Create_Queries_Test.txt\n");

		alreadyWritten = false;
		try{
			testRes = new File("outputs/testing/Create_Queries_Tests.txt");
			testRes.createNewFile();
			
			new PrintWriter("outputs/testing/Create_Queries_Tests.txt").close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	
	@Before
	public void setup(){
		spsmCall = new Call_SPSM();
		getRepairedSchema = new Repair_Schema();
		createQuery = new Create_Query();
		
		try{
			fOut = new PrintWriter(new FileWriter(testRes,true));
			
			if(alreadyWritten==false){
				fOut.write("Testing Results for Create_Query_Test_Cases.java\n\n");
				alreadyWritten = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	@Test //Sepa
	public void test11(){
		schema="waterBodyPressures(dataSource,identifiedDate,affectsGroundwater,waterBodyId)";
		testQueryCreation("5.1.1", schema, Q5_1_1, T5_1_1, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0) ;
	}
	
	@Test //Sepa
	public void test12(){
		schema="water(timePeriod, geo, measure, resource)";
		testQueryCreation("5.1.2", schema, Q5_1_2, T5_1_2, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0) ;
	}

	@Test //Sepa
	public void test13(){
		
	  schema="waterBodyMeasures(timePeriod, geo, measure, resource)";
	  // target="waterBodyMeasures(timePeriod, geo, measure, resource)";
	  testQueryCreation("5.1.3", schema, Q5_1_3, T5_1_3, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0) ;
	}
	
	@Test //Sepa
	public void test14(){		
		schema="waterBodyPressures(identifiedDate,waterBodyId,assessmentCategory,source)";
		testQueryCreation("5.1.4", schema, Q5_1_4, T5_1_4, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0) ;
	}
	
	@Test //Sepa
	public void test15(){
	  schema="waterBodyMeasures(waterBodyId,secondaryMeasure,dataSource)";
	  testQueryCreation("5.1.5", schema, Q5_1_5, T5_1_5, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0) ;
	}
	
	@Test //Sepa
	public void test16(){
	  schema="surfaceWaterBodies(riverName,associatedGroundwaterId)";
	  testQueryCreation("5.1.6", schema,  Q5_1_6, T5_1_6, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0) ;
	}
	
	@Test //Sepa
	public void test17(){	
	  schema="bathingWaters(catchment, localAuthority, lat, long)";
	  testQueryCreation("5.1.7", schema,  Q5_1_7, T5_1_7, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0) ;
	}
	
	@Test //Sepa
	public void test18(){
	  schema="surfaceWaterBodies(subBasinDistrict,riverName,altitudeTypology,associatedGroundwaterId)";
	  testQueryCreation("5.1.8", schema, Q5_1_8, T5_1_8, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0) ;
	}
	
	@Test //Sepa
	public void test19(){		
	  schema="bathingWaters(bathingWaterId)";
	  testQueryCreation("5.1.9", schema, Q5_1_9, T5_1_9, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0) ;
	}
	
	@Test //Dbpedia
	public void test21(){	
		schema="City(country,populationTotal)";
		testQueryCreation("5.2.1", schema,  Q5_2_1, T5_2_1, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	}
	
	@Test //Dbpedia
	public void test22(){	
		schema="City";
		testQueryCreation("5.2.2", schema, Q5_2_2, T5_2_2, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	}
	
	@Test //Dbpedia
	public void test23(){	
	  schema="Astronaut(nationality)";
	  testQueryCreation("5.2.3", schema, Q5_2_3, T5_2_3, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	}
	
	@Test //Dbpedia
	public void test24(){	
	  schema="Mountain(elevation)";
	  testQueryCreation("5.2.4", schema, Q5_2_4, T5_2_4, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	  
	}
	
	@Test //Dbpedia
	public void test25(){	
	  schema="Person(occupation, birthPlace)";
	  testQueryCreation("5.2.5", schema,  Q5_2_5, T5_2_5, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	}
	
	@Test //Dbpedia
	public void test26(){
	  schema="Person(occupation, instrument)";
	  testQueryCreation("5.2.6", schema, Q5_2_6, T5_2_6, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	  }
	
	@Test //Dbpedia
	public void test27(){	
	  schema="Cave(location)";
	  testQueryCreation("5.2.7", schema, Q5_2_7, T5_2_7, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	  }
	
	@Test //Dbpedia
	public void test28(){
	  schema="FormulaOneRacer(races)";
	  testQueryCreation("5.2.8", schema, Q5_2_8, T5_2_8, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	}
	
	@Test //Dbpedia
	public void test29(){		
	  schema="River(length)";
	  testQueryCreation("5.2.9", schema, Q5_2_9, T5_2_9, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	}
	
	@Test //Dbpedia
	public void test210(){
	  schema="Royalty(parent)";
	  testQueryCreation("5.2.10", schema, Q5_2_10, T5_2_10, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	}
	
	@Test //Dbpedia
	public void test211(){
	  schema="river(length)";
	  testQueryCreation("5.2.11", schema,  Q5_2_11, T5_2_11, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	  }
	
	@Test //Dbpedia
	public void test212(){
	  schema="Stream(length)";
	  testQueryCreation("5.2.12", schema, Q5_2_12, T5_2_12, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	}
	
	@Test //Dbpedia
	public void test213(){
	  schema="River(Mountain(elevation))";
	  testQueryCreation("5.2.13", schema,  Q5_2_13, T5_2_13, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	}
	
	@Test //Sepa
	public void test301(){
		schema="waterBodyPressures(source,identifiedDate,assessmentCategory,waterBodyId)";
		testQueryCreation("5.3.1", schema, Q5_3_1, T5_3_1, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0) ;
	}
	
	@Test //Sepa
	public void test302(){
		System.out.println("\nRunning test 5.3.2 - dbpedia query with additional property");
		// There is an additional property (affectsGroundwater) in the schema that was not in the original query
		schema="waterBodyPressures(source,identifiedDate,assessmentCategory,affectsGroundwater,waterBodyId)";
		testQueryCreation("5.3.2", schema,Q5_3_1, T5_3_2, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0) ;
	}
	
	@Test //Dbpedia
	public void test303(){
	  schema="FormulaOneRacer(races)";  
	  testQueryCreation("5.3.3", schema, Q5_3_3, T5_3_3, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	  
	}
	
	@Test //Sepa
	public void test304(){
		System.out.println("\nRunning test 5.3.4 - An open SEPA query");
		schema="waterBodyPressures(source,identifiedDate,assessmentCategory,waterBodyId)";
		testOpenQueryCreation("5.3.4", schema,Q5_3_1, T5_3_4, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0) ;
	}
	
	@Test //Dbpedia
	public void test305(){
	  System.out.println("\nRunning test 5.3.5 - An open dbpedia query");
	  schema="FormulaOneRacer(races)";  
	  testOpenQueryCreation("5.3.5", schema, Q5_3_3, T5_3_5, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	  
	}
	
	@Test //Sepa
	public void test306(){
		System.out.println("\nRunning test 5.3.6 - A SEPA query with data");
		schema="waterBodyPressures(source,identifiedDate,assessmentCategory,waterBodyId)";
		testQueryCreation("5.3.6", schema,Q5_3_6, T5_3_6, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0) ;
	}
	
	@Test //Sepa
	public void test307(){
		System.out.println("\nRunning test 5.3.7 - An open SEPA query, original query has data");
		schema="waterBodyPressures(source,identifiedDate,assessmentCategory,waterBodyId)";
		testOpenQueryCreation("5.3.7", schema, Q5_3_6, T5_3_7, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0) ;
	}
	
	
	@Test //Sepa
	public void test308(){
		System.out.println("\nRunning test 5.3.8 - A SEPA query withdata");
		schema="waterBodyPressures(source,identifiedDate,assessmentCategory,waterBodyId)";
		testQueryCreation("5.3.8", schema, Q5_3_8, T5_3_8, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0) ;
	}

	@Test //Sepa
	public void test309(){
		System.out.println("\nRunning test 5.3.9 - An open SEPA query, original query has data");
		schema="waterBodyPressures(source,identifiedDate,assessmentCategory,waterBodyId)";
		testOpenQueryCreation("5.3.9", schema, Q5_3_8, T5_3_9, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0) ;
	}
	
	@Test //Sepa
	public void test401(){ // Repaired query
		System.out.println("\nRunning test 5.4.1 - Repair a SEPA query with data");
		schema="waterBodyPressures(source,identifiedDate,assessmentCategory,waterBodyId)";
		testQueryRepair("5.4.1", schema,Q5_3_6, T5_3_6, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0) ;
	}
	
	@Test //Dbpedia
	public void test402(){
	  System.out.println("\nRunning test 5.4.2 - Repair a dbpedia query with data");
	  schema="River(elevation,location,Mountain)";  
	  testQueryRepair("5.4.2", schema, Q5_4_2, T5_4_2, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	  
	}
	
	@Test //Dbpedia
	public void test403(){
	  System.out.println("\nRunning test 5.4.3 - Repair a dbpedia query with filter");
	  schema="City(populationTotal,country)";  
	  testQueryRepair("5.4.3", schema, Q5_4_3, T5_4_3, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	  
	}
	
	@Test //Dbpedia
	public void test404(){
	  System.out.println("\nRunning test 5.4.4 - Repair a dbpedia query with data");
	  schema="City(country)";  
	  testQueryRepair("5.4.4", schema, Q5_4_3, T5_4_4, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	  
	}
	
	@Test //Dbpedia
	public void test405(){
	  System.out.println("\nRunning test 5.4.5 - Repair a dbpedia query");
	  schema="Mountain(elevation)";  
	  testQueryRepair("5.4.5", schema, Q5_4_5, T5_4_5, "dbpedia",null,"queryData/dbpedia/dbpedia_ontology.json",20) ;
	  
	}
	
	@Test //Sepa
	public void test406(){
		System.out.println("\nRunning test 5.4.6 - Repair a SEPA query");
		schema="surfaceWaterBodies(subBasinDistrict,riverName,altitudeTypology,associatedGroundwaterId)";
		testQueryCreation("5.4.6", schema, Q5_1_8, T5_1_8, "sepa","queryData/sepa/sepa_datafiles/","queryData/sepa/sepa_ontology.json", 0) ;
	}
	
	
	
	

	private void testQueryCreation(String testID, String schema, String query, String testStr, String type, String dataLoc, String ontLoc, int numResults) {
		  
		fOut.write("Test " + testID + " - " + type + " query\n\n");
		fOut.write("Query " +  query + "\n\n");
		fOut.write("Schema: " + schema +"\n\n") ;
		
		// Call SPSM to create a match structure
		finalRes = new ArrayList<Match_Struc>();

		finalRes=spsmCall.callSPSM(finalRes, schema, schema);

		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.repairSchemas(finalRes);
		} else {
			fOut.write("Empty results from SPSM. \n\n");
			fail() ;
		}

		Query_Data queryData = new Query_Data(query) ;		  
		fOut.write(queryData.toString()+"\n\n") ;		  
		finalRes = createQuery.createQueries(finalRes, queryData, type,dataLoc,ontLoc,numResults);	  

		if(finalRes!=null){
			if(finalRes.size() == 0){
				//then we have no results so end test
				fOut.write("Empty results returned. \n\n");
				fail() ; // We expect all tests to return more than zero matches
			}else{
				Match_Struc current = finalRes.get(0);	      
				fOut.write("Result: \n\n" + current.getQuery() + "\n\n");
				assertTrue(current.getQuery().contains(testStr)) ;
			}
		}else{
			fOut.write("Null Results! \n\n");
			fail() ; // We expect all tests to return something
		}
	}
	
	private void testOpenQueryCreation(String testID, String schema, String query, String testStr, String type, String dataLoc, String ontLoc, int numResults) {
		  
		fOut.write("Test Open Query Creation " + testID + " - " + type + " query\n\n");
		fOut.write("Query " +  query + "\n\n");
		fOut.write("Schema: " + schema +"\n\n") ;
		
		finalRes = new ArrayList<Match_Struc>();
		  
		  finalRes=spsmCall.callSPSM(finalRes, schema, schema);
		  
		  if(finalRes!=null && finalRes.size()!=0){
		    finalRes = getRepairedSchema.repairSchemas(finalRes);
		  } else {
			fOut.write("Empty results from SPSM. \n\n");
			fail() ;
		  }
		  
		  Query_Data queryData = new Query_Data(query) ;		  
		  fOut.write(queryData.toString()+"\n\n") ;		  
		  finalRes = createQuery.createOpenQueries(finalRes, queryData, type,dataLoc,ontLoc,numResults);	  
		  
		  if(finalRes!=null){
		    if(finalRes.size() == 0){
		      //then we have no results so end test
		      fOut.write("Empty results returned. \n\n");
		      fail() ; // We expect all tests to return more than zero matches
		    }else{
		      Match_Struc current = finalRes.get(0);	      
		      fOut.write("Result: \n\n" + current.getQuery() + "\n\n");
		      assertTrue(current.getQuery().contains(testStr)) ;
		    }
		  }else{
		    fOut.write("Null Results! \n\n");
		    fail() ; // We expect all tests to return something
		  }
	}
	
	private void testQueryRepair(String testID, String schema, String query, String testStr, String type, String dataLoc, String ontLoc, int numResults) {

		fOut.write("Test Query Repair " + testID + " - " + type + " query\n\n");
		fOut.write("Query " +  query + "\n\n");
		fOut.write("Schema: " + schema +"\n\n") ;

		finalRes = new ArrayList<Match_Struc>();

		finalRes=spsmCall.callSPSM(finalRes, schema, schema);

		if(finalRes!=null && finalRes.size()!=0){
			finalRes = getRepairedSchema.repairSchemas(finalRes);
		} else {
			fOut.write("Empty results from SPSM. \n\n");
			fail() ;
		}

		Query_Data queryData = new Query_Data(query) ;		  
		fOut.write(queryData.toString()+"\n\n") ;	
		finalRes = Repair_Query.repairQueries(finalRes, queryData, type, dataLoc, ontLoc,numResults);	  

		if(finalRes!=null){
			if(finalRes.size() == 0){
				//then we have no results so end test
				fOut.write("Empty results returned. \n\n");
				fail() ; // We expect all tests to return more than zero matches
			}else{
				Match_Struc current = finalRes.get(0);	      
				fOut.write("Result: \n\n" + current.getQuery() + "\n\n");
				assertTrue(current.getQuery().contains(testStr)) ;
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
