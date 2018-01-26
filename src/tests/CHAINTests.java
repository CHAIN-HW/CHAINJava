package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/* Author Diana Bental 
 * Date January 2018
 * Modified
 */

/*
 * 
 * Testing suite for CHAIn
 * 
 */

@RunWith(Suite.class)

@SuiteClasses({ 
		Match_Results_Test_Cases.class,
		SPSM_Filter_Results_Test_Cases.class, 
		SPSM_Test_Cases.class,
		Schema_From_Query_Test_Cases.class,
		JWI_Caller_Test_Cases.class,
		Create_Query_Test_Cases.class, 
		Repair_Schema_Test_Cases.class,
		Run_Query_Test_Cases.class,
		Run_CHAIn_Test_Cases.class,
		SAQ_Create_Query_Tests.class	
})

public class CHAINTests {

	}

