import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/*
 * 
 * Testing suite for task 2 that
 * runs Match_Results_Test_Cases,
 * SPSM_Filter_Results_Test_Cases
 * and SPSM_Test_Cases
 * 
 */

@RunWith(Suite.class)

@SuiteClasses({ 
		Match_Results_Test_Cases.class,
		SPSM_Filter_Results_Test_Cases.class, 
		SPSM_Test_Cases.class 
})

public class Task1_Test_Suite {
}
