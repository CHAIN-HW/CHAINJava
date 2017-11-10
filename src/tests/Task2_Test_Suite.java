package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/* Author Tanya Howden
 * Date September 2017
 * Modified
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
	Create_Query_Test_Cases.class, 
	Repair_Schema_Test_Cases.class,
		Run_Query_Test_Cases.class 
})

public class Task2_Test_Suite {
}
