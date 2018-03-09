package chain_tests.core;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import static junit.framework.TestCase.*;

/**
 * As the main test suite takes a long time to run, this selects a single
 * tests to run (they were picked mostly at random)
 *
 * Some tests are ignored as their tests take too long.  This is mostly to quickly check that
 * everything hasn't suddenly been broken
 */
public class FastTestSuiteCore extends FastTestSuite {



    @Test
    public void runJWICallerTestCases() {
        testMethod(JWICallerTestCases.class, "test_jwi_01");
        testMethod(JWICallerTestCases.class, "test_jwi_04");

    }

    @Test
    public void runMatchResultsTestCases() {
        testClass(MatchResultsTestCases.class);
    }

    @Test
    public void repairSchemaTestCases() {
        // Long but has a lot of tests - only run 1
        testMethod(RepairSchemaTestCases.class, "test4124");
    }

    @Test
    public void runRunCHAInTestCases() {
        testMethod(RunCHAInTestCases.class, "test8_0_1");
        testMethod(RunCHAInTestCases.class, "test8_0_6");
    }

    @Test()
    public void runSPSMFilteResultsTestCases() {
        // All tests in this class seem to take long - ignore
    }

    @Test
    public void runSPSMTestCases() {
        testMethod(SPSMTestCases.class, "test1_1_1");
        testMethod(SPSMTestCases.class, "test1_4_5");
        testMethod(SPSMTestCases.class, "test1_6_5");
    }
}
