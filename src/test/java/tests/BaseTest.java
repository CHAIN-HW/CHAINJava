package tests;

import java.io.File;
import org.junit.BeforeClass;




public class BaseTest {
    @BeforeClass
    public static void setUpBaseClass() {
        File outputsFile = new File("outputs");
        File testingFile = new File("outputs/testing");
        if (!outputsFile.exists()) {
            try{
                outputsFile.mkdir();
                testingFile.mkdir();

            }
            catch(SecurityException se){
                System.out.println(se.getMessage());
            }
        }
        if(!testingFile.exists())
        {
            try{
                testingFile.mkdir();

            }
            catch(SecurityException se){
                System.out.println(se.getMessage());
            }
        }
    }
}