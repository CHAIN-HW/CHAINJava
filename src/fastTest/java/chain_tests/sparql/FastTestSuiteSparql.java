package chain_tests.sparql;

import chain_tests.core.FastTestSuite;
import org.junit.Test;

public class FastTestSuiteSparql extends FastTestSuite{

    @Test
    public void runCreateQueryTestCases() {
        testMethod(CreateQueryTestCases.class, "test15");
    }

    @Test
    public void runRunQueryTestCases() {
        testMethod(RunQueryTestCases.class, "test25");
    }

    @Test
    public void runSAQCreateQueryTests() {
        testMethod(SAQCreateQueryTests.class, "test_22");
    }

    @Test
    public void runSchemaFromQueryTestCases() {
        testMethod(SchemaFromQueryTestCases.class, "test23");
    }
}
