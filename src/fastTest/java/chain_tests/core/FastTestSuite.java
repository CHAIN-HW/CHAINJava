package chain_tests.core;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import static junit.framework.TestCase.assertEquals;

public class FastTestSuite {

    private static String getErrors(Result result) {
        StringBuilder sb = new StringBuilder();

        for(Failure failure : result.getFailures()) {
            sb.append("\n-------\nFailure\n-------\n");
            sb.append(failure.getTestHeader());
            sb.append("\n");
            sb.append(failure.getException().getClass().getName());
            sb.append("\n");
            sb.append(failure.getMessage());
            sb.append("\n");
            sb.append(failure.getTrace());
            sb.append("\n");
        }

        return sb.toString();
    }

    protected void testMethod(Class cls, String testName) {
        JUnitCore junitRunner = new JUnitCore();
        Request request = Request.method(cls, testName);
        Result result = junitRunner.run(request);

        assertEquals("Test failures found: " + getErrors(result), 0, result.getFailureCount());
    }

    protected void testClass(Class cls) {
        JUnitCore junitRunner = new JUnitCore();
        Request request = Request.aClass(cls);
        Result result = junitRunner.run(request);

        assertEquals("Test failures found: " + getErrors(result), 0, result.getFailureCount());
    }
}
