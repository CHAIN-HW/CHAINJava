package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/* Author Tanya Howden
 * Author Diana Bental
 * Date September 2017
 * Modified December 2017
 * - added subsequent tasks
 */

/*
 * 
 * Testing suite for task 1 that runs
 * Create_Query_Test_Cases,
 * RepairSchemaTestCases and
 * RunQueryTestCases
 * 
 */
@RunWith(Suite.class)

@SuiteClasses({ 
	SchemaFromQueryTestCases.class,
	JWICallerTestCases.class,
	CreateQueryTestCases.class,
	RepairSchemaTestCases.class,
		RunQueryTestCases.class
})

public class Task2TestSuite extends tests.BaseTest {
}
