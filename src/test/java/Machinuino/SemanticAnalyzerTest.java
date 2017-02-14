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
                "5: Empty \"Input Pins\" section" + LS +
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
    public void undeclaredInputPin() {
        String file = pathSemanticErrors + "undeclaredInputPin.moore";
        compareExpectedFaults(file, "20: Input Pin errrrr undeclared." + LS, "");
    }

    @Test
    public void emptyOutputPins() {
        String file = pathSemanticErrors + "emptyOutputPins.moore";
        compareExpectedFaults(file, "", "41: Empty \"Output pins\" section" + LS);
    }

    @Test
    public void undeclaredOutputPin() {
        String file = pathSemanticErrors + "undeclaredOutputPin.moore";
        compareExpectedFaults(file, "49: Output Pin wrongPin undeclared." + LS, "");
    }

    @Test
    public void undeclaredFunctionState() {
        String file = pathSemanticErrors + "undeclaredFunctionState.moore";
        compareExpectedFaults(file, "47: State q3 undeclared." + LS, "");
    }

    @Test
    public void emptyTransBlock() {
        String file = pathSemanticErrors + "emptyTransBlock.moore";
        compareExpectedFaults(file, "", "11: Empty \"Transition block of a1\" section" + LS +
                "11: Empty \"Transition block of a2\" section" + LS);
    }

    @Test
    public void emptyInputPins() {
        String file = pathSemanticErrors + "emptyInputPins.moore";
        compareExpectedFaults(file, "", "9: Empty \"Input Pins\" section" + LS +
                "14: Empty \"Transition\" section" + LS);
    }

    @Test
    public void duplicateOutputPin() {
        String file = pathSemanticErrors + "duplicateOutputPin.moore";
        compareExpectedFaults(file, "41: Duplicate pin motor." + LS, "");
    }

    @Test
    public void duplicatePinNumber() {
        String file = pathSemanticErrors + "duplicatePinNumber.moore";
        compareExpectedFaults(file, "9: Pin Number 10 already used." + LS, "");
    }

    @Test
    public void duplicatePinNumber2() {
        String file = pathSemanticErrors + "duplicatePinNumber2.moore";
        compareExpectedFaults(file, "41: Pin Number 10 already used." + LS, "");
    }

    @Test
    public void duplicateOutput() {
        String file = pathSemanticErrors + "duplicateOutput.moore";
        compareExpectedFaults(file, "54: Output \"motor\" already defined." + LS, "");
    }

    @Test
    public void duplicateInputInExp() {
        String file = pathSemanticErrors + "duplicateInputInExp.moore";
        compareExpectedFaults(file, "18: Input Pin \"button\" already used in expression." + LS, "");
    }

    @Test
    public void duplicateTransition() {
        String file = pathSemanticErrors + "duplicateTransition.moore";
        compareExpectedFaults(file, "19: Transition coming from state \"q0\" already defined." + LS,  "");
    }
}
