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
		MatchResultsTestCases.class,
		SPSMFilterResultsTestCases.class,
		SPSMTestCases.class,
		SchemaFromQueryTestCases.class,
		JWICallerTestCases.class,
//		Create_Query_Test_Cases.class,
		RepairSchemaTestCases.class,
		RunQueryTestCases.class,
		RunCHAInTestCases.class,
		SAQCreateQueryTests.class
})

public class CHAINTests {

	}

