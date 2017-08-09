import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({ 
	Create_Query_Test_Cases.class, 
	Repair_Schema_Test_Cases.class,
		Run_Query_Test_Cases.class 
})

public class Task2_Test_Suite {
}
