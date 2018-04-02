package chain.sql;

import it.unitn.disi.smatch.SMatchException;
import org.junit.Before;
import org.junit.Test;


import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;


/**
 * 
 * SPSMMatcherTest
 * 
 * This class is responsible for testing the SPSMMatcher class.
 * 
 * It will create a new instance of SPSMMatcher and use this
 * matcher to check that we can get the synonym “lastname”
 * for the given word “surname” from an example list of targets.
 *
 */
public class WordNetMatcherTest {

    private WordNetMatcher matcher;

    @Before
    public void setUp()
    {
        Set<String> targetExample = new HashSet<>();
        targetExample.add("name");
        targetExample.add("lastname");
        targetExample.add("middlename");
        targetExample.add("username");
        targetExample.add("kettle");

        matcher = new WordNetMatcher(targetExample);
    }

    @Test
    public void match() throws SMatchException, WordNetMatchingException {
        String sourceExample = "surname";

        assertEquals("lastname", matcher.match(sourceExample));
    }

    @Test(expected = WordNetMatchingException.class)
    public void matchThrowSPSMMatchingExceptionTooMany() throws SMatchException, WordNetMatchingException {
        matcher.match("name");
    }

    @Test(expected = WordNetMatchingException.class)
    public void matchThrowSPSMMatchingExceptionNone() throws SMatchException, WordNetMatchingException {
        matcher.match("france");
    }

}