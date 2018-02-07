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
 * Repair_Schema_Test_Cases and
 * Run_Query_Test_Cases
 * 
 */
@RunWith(Suite.class)

@SuiteClasses({ 
	Schema_From_Query_Test_Cases.class,
	JWI_Caller_Test_Cases.class,
	Create_Query_Test_Cases.class, 
	Repair_Schema_Test_Cases.class,
		Run_Query_Test_Cases.class 
})

public class Task2_Test_Suite {
}
