package Machinuino;

import Machinuino.model.Fault;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class SemanticAnalyzerTest {
    SemanticAnalyzer semanticAnalyzer;

    private String pathSemanticErrors = "src/test/testCases/semanticErrors/";

    @Before
    public void init() {
        semanticAnalyzer = SemanticAnalyzer.getInstance();
    }

    private void compareExpectedFaults(String file, String errorsExpected, String warningsExpected) {
        Fault error;
        try {
            error = semanticAnalyzer.analyzeFile(file);
        } catch (IOException e) {
            // The file exists because it is hardcoded to test.
            Assert.fail(e.toString());
            return;
        }

        Assert.assertEquals(errorsExpected, error.getErrors());
        Assert.assertEquals(warningsExpected, error.getWarnings());
    }

    /* Warnings */
    @Test
    public void noStates() {
        String file = pathSemanticErrors + "noStates.moore";
        compareExpectedFaults(file, "", "2: Empty \"state\" section" + System.lineSeparator());
    }

    @Test
    public void duplicateState() {
        String file = pathSemanticErrors + "duplicateState.moore";
        compareExpectedFaults(file, "", "2: Symbol a1 already used. Will be ignored." + System.lineSeparator());
    }

    @Test
    public void duplicateInputPin() {
        String file = pathSemanticErrors + "duplicateInputPin.moore";
        compareExpectedFaults(file, "5: Duplicate pin p1." + System.lineSeparator(), "");
    }
}


