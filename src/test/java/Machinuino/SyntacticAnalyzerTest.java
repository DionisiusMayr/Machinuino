package Machinuino;

import junit.framework.TestCase;

public class SyntacticAnalyzerTest extends TestCase {
    private String pathNoErrors = "src/test/testCases/noErrors/";
    private String pathSyntacticErrors = "src/test/testCases/syntacticErrors/";

    /* No Errors */
    public void testNoErrors1() throws Exception {
        assertEquals("", SyntacticAnalyzer.analyzeFile(pathNoErrors + "template.moore"));
    }

    /* Syntactic Errors */
    public void testMooreNameWrong() throws Exception {
        assertEquals("1: Syntactic error next to 'muure'\n",
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "wrongMooreName.moore"));
    }

    public void testWrongStatesName() throws Exception {
        assertEquals("3: Syntactic error next to 'staites'\n",
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "wrongStatesName.moore"));
    }
}
