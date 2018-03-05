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
 * Testing suite for task 2 that
 * runs MatchResultsTestCases,
 * SPSMFilterResultsTestCases
 * and SPSMTestCases
 * 
 */

@RunWith(Suite.class)

@SuiteClasses({ 
		MatchResultsTestCases.class,
		SPSMFilterResultsTestCases.class,
		SPSMTestCases.class
})

public class Task1TestSuite extends tests.BaseTest {
}
