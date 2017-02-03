package Machinuino;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class SyntacticAnalyzerTest {
    private String pathNoErrors = "src/test/testCases/noErrors/";
    private String pathSyntacticErrors = "src/test/testCases/syntacticErrors/";
    private String pathLexicalErrors = "src/test/testCases/lexicalErrors/";

    private void compareExpectedToResult(String expected, String fileName) {
        String result;
        try {
            result = SyntacticAnalyzer.analyzeFile(fileName);
        } catch (IOException e) {
            Assert.fail("Failed to open file " + fileName + "!");
            return;
        }
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IOException.class)
    public void inexistentFileShouldThrowException() throws IOException {
        SyntacticAnalyzer.analyzeFile("rofl.lmao");
    }

    /* No Errors */
    @Test
    public void noErrors() {
        compareExpectedToResult("", pathNoErrors + "template.moore");
    }

    @Test
    public void commentAfterMoore() {
        compareExpectedToResult("", pathNoErrors + "commentAfterMoore.moore");
    }

    /* Syntactic Errors */
    @Test
    public void OnlyComment() {
        compareExpectedToResult("66: Syntactic error next to '<EOF>'" + System.lineSeparator(),
                pathSyntacticErrors + "onlyComment.moore");
    }

    @Test
    public void testMissingOpenComment() {
        compareExpectedToResult("2: Syntactic error next to 'The'" + System.lineSeparator(),
                pathSyntacticErrors + "missingOpenComment.moore");
    }

    @Test
    public void missingOpenBrackets() {
        compareExpectedToResult("3: Syntactic error next to 'states'" + System.lineSeparator(),
                pathSyntacticErrors + "missingOpenBrackets.moore");
    }

    @Test
    public void missingCloseBrackets() {
        compareExpectedToResult("3: Syntactic error next to 'states'" + System.lineSeparator(),
                pathSyntacticErrors + "missingCloseBrackets.moore");
    }

    @Test
    public void missingPinNumber() {
        compareExpectedToResult("11: Syntactic error next to ','" + System.lineSeparator(),
                pathSyntacticErrors + "missingPinNumber.moore");
    }

    @Test
    public void missingStateName() {
        compareExpectedToResult("18: Syntactic error next to '{'" + System.lineSeparator(),
                pathSyntacticErrors + "missingStateName.moore");
    }

    @Test
    public void missingAmpersand() {
        compareExpectedToResult("19: Syntactic error next to 'switch'" + System.lineSeparator(),
                pathSyntacticErrors + "missingAmpersand.moore");
    }

    @Test
    public void missingArrow() {
        compareExpectedToResult("19: Syntactic error next to 'q1'" + System.lineSeparator(),
                pathSyntacticErrors + "missingArrow.moore");
    }

    @Test
    public void missingPinAtTransition() {
        compareExpectedToResult("19: Syntactic error next to '->'" + System.lineSeparator(),
                pathSyntacticErrors + "missingPinAtTransition.moore");
    }

    @Test
    public void missingPinName() {
        compareExpectedToResult("43: Syntactic error next to ':'" + System.lineSeparator(),
                pathSyntacticErrors + "missingPinName.moore");
    }

    @Test
    public void missingColon() {
        compareExpectedToResult("43: Syntactic error next to '5'" + System.lineSeparator(),
                pathSyntacticErrors + "missingColon.moore");
    }

    @Test
    public void missingComma() {
        compareExpectedToResult("44: Syntactic error next to 'led'" + System.lineSeparator(),
                pathSyntacticErrors + "missingComma.moore");
    }

    @Test
    public void missingCloseComment() {
        compareExpectedToResult("48: Syntactic error next to 'function'" + System.lineSeparator(),
                pathSyntacticErrors + "missingCloseComment.moore");
    }

    @Test
    public void mooreNameWrong() {
        compareExpectedToResult("1: Syntactic error next to 'muure'" + System.lineSeparator(),
                pathSyntacticErrors + "wrongMooreName.moore");
    }

    @Test
    public void wrongStatesName() {
        compareExpectedToResult("3: Syntactic error next to 'staites'" + System.lineSeparator(),
                pathSyntacticErrors + "wrongStatesName.moore");
    }

    @Test
    public void wrongInputName() {
        compareExpectedToResult("7: Syntactic error next to 'innput'" + System.lineSeparator(),
                pathSyntacticErrors + "wrongInputName.moore");
    }

    @Test
    public void wrongPinsName() {
        compareExpectedToResult("9: Syntactic error next to 'pis'" + System.lineSeparator(),
                pathSyntacticErrors + "wrongPinsName.moore");
    }

    @Test
    public void wrongClockName() {
        compareExpectedToResult("10: Syntactic error next to 'click'" + System.lineSeparator(),
                pathSyntacticErrors + "wrongClockName.moore");
    }

    @Test
    public void wrongTransitionName() {
        compareExpectedToResult("17: Syntactic error next to 'tranion'" + System.lineSeparator(),
                pathSyntacticErrors + "wrongTransitionName.moore");
    }

    @Test
    public void wrongLogicalExpression() {
        compareExpectedToResult("19: Syntactic error next to '&'" + System.lineSeparator(),
                pathSyntacticErrors + "wrongLogicalExpression.moore");
    }

    @Test
    public void wrongLogicalExpression2() {
        compareExpectedToResult("20: Syntactic error next to '->'" + System.lineSeparator(),
                pathSyntacticErrors + "wrongLogicalExpression2.moore");
    }

    @Test
    public void wrongOutputName() {
        compareExpectedToResult("41: Syntactic error next to 'autput'" + System.lineSeparator(),
                pathSyntacticErrors + "wrongOutputName.moore");
    }

    @Test
    public void wrongPinsName2() {
        compareExpectedToResult("42: Syntactic error next to 'piiins'" + System.lineSeparator(),
                pathSyntacticErrors + "wrongPinsName2.moore");
    }

    @Test
    public void wrongFunctionName() {
        compareExpectedToResult("48: Syntactic error next to 'fanction'" + System.lineSeparator(),
                pathSyntacticErrors + "wrongFunctionName.moore");
    }

    @Test
    public void notEofAfterMoore() {
        compareExpectedToResult("66: Syntactic error next to 'aaaaa'" + System.lineSeparator(),
                pathSyntacticErrors + "notEofAfterMoore.moore");
    }

    /* Lexical Errors */
    @Test
    public void invalidStateName() {
        compareExpectedToResult("4: Lexical Error: q - invalid symbol" + System.lineSeparator(),
                pathLexicalErrors + "invalidStateName.moore");
    }

    @Test
    public void invalidPinName() {
        compareExpectedToResult("11: Lexical Error: _ - invalid symbol" + System.lineSeparator(),
                pathLexicalErrors + "invalidPinName.moore");
    }

    @Test
    public void invalidArrow() {
        compareExpectedToResult("19: Lexical Error: - - invalid symbol" + System.lineSeparator(),
                pathLexicalErrors + "invalidArrow.moore");
    }
}
