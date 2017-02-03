package Machinuino;

import Machinuino.model.Fault;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class SemanticAnalyzerTest {
    private SemanticAnalyzer semanticAnalyzer;
    private static final String LS = System.lineSeparator();

    private String pathSemanticErrors = "src/test/testCases/semanticErrors/";

    @Before
    public void init() {
        semanticAnalyzer = SemanticAnalyzer.getInstance();
    }

    private void compareExpectedFaults(String file, String errorsExpected,
                                       String warningsExpected) {
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
    public void emptyStates() {
        String file = pathSemanticErrors + "emptyStates.moore";
        compareExpectedFaults(file, "", "2: Empty \"state\" section" + LS +
                "9: Empty \"Transition\" section" + LS +
                "13: Empty \"Output pins\" section" + LS);
    }

    @Test
    public void duplicateState() {
        String file = pathSemanticErrors + "duplicateState.moore";
        compareExpectedFaults(file, "", "3: Symbol q0 already used. Will be ignored." + LS);
    }

    @Test
    public void duplicateInputPin() {
        String file = pathSemanticErrors + "duplicateInputPin.moore";
        compareExpectedFaults(file, "9: Duplicate pin switch." + LS, "");
    }

    @Test
    public void emptyTransition() {
        String file = pathSemanticErrors + "emptyTransition.moore";
        compareExpectedFaults(file, "", "11: Empty \"Transition\" section" + LS +
                "15: Empty \"Output pins\" section" + LS);
    }

    @Test
    public void undeclaredActualState() {
        String file = pathSemanticErrors + "undeclaredActualState.moore";
        compareExpectedFaults(file, "16: State q3 undeclared." + LS, "");
    }

    @Test
    public void undeclaredTargetState() {
        String file = pathSemanticErrors + "undeclaredTargetState.moore";
        compareExpectedFaults(file, "20: State q3 undeclared." + LS, "");
    }

    @Test
    public void undeclaredInputPit() {
        String file = pathSemanticErrors + "undeclaredInputPin.moore";
        compareExpectedFaults(file, "20: Input Pin errrrr undeclared." + LS, "");
    }

    @Test
    public void emptyOutputPins() {
        String file = pathSemanticErrors + "emptyOutputPins.moore";
        compareExpectedFaults(file, "", "");
    }

}


