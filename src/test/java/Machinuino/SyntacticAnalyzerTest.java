package Machinuino;

import junit.framework.TestCase;

public class SyntacticAnalyzerTest extends TestCase {
    private String pathNoErrors = "src/test/testCases/noErrors/";
    private String pathSyntacticErrors = "src/test/testCases/syntacticErrors/";
    private String pathLexicalErrors = "src/test/testCases/lexicalErrors/";

    /* No Errors */
    public void testNoErrors() throws Exception {
        assertEquals("", SyntacticAnalyzer.analyzeFile(pathNoErrors + "template.moore"));
    }

    public void testCommentAfterMoore() throws Exception {
        assertEquals("", SyntacticAnalyzer.analyzeFile(pathNoErrors + "commentAfterMoore.moore"));
    }

    /* Syntactic Errors */
    public void testOnlyComment() throws Exception {
        assertEquals("66: Syntactic error next to '<EOF>'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "onlyComment.moore"));
    }

    public void testMissingOpenComment() throws Exception {
        assertEquals("2: Syntactic error next to 'The'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "missingOpenComment.moore"));
    }

    public void testMissingOpenBrackets() throws Exception {
        assertEquals("3: Syntactic error next to 'states'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "missingOpenBrackets.moore"));
    }

    public void testMissingCloseBrackets() throws Exception {
        assertEquals("3: Syntactic error next to 'states'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "missingCloseBrackets.moore"));
    }

    public void testMissingPinNumber() throws Exception {
        assertEquals("11: Syntactic error next to ','" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "missingPinNumber.moore"));
    }

    public void testMissingStateName() throws Exception {
        assertEquals("18: Syntactic error next to '{'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "missingStateName.moore"));
    }

    public void testMissingAmpersand() throws Exception {
        assertEquals("19: Syntactic error next to 'switch'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "missingAmpersand.moore"));
    }

    public void testMissingArrow() throws Exception {
        assertEquals("19: Syntactic error next to 'q1'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "missingArrow.moore"));
    }

    public void testMissingPinAtTransition() throws Exception {
        assertEquals("19: Syntactic error next to '->'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "missingPinAtTransition.moore"));
    }

    public void testMissingPinName() throws Exception {
        assertEquals("43: Syntactic error next to ':'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "missingPinName.moore"));
    }

    public void testMissingColon() throws Exception {
        assertEquals("43: Syntactic error next to '5'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "missingColon.moore"));
    }

    public void testMissingComma() throws Exception {
        assertEquals("44: Syntactic error next to 'led'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "missingComma.moore"));
    }

    public void testMissingCloseComment() throws Exception {
        assertEquals("48: Syntactic error next to 'function'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "missingCloseComment.moore"));
    }

    public void testMooreNameWrong() throws Exception {
        assertEquals("1: Syntactic error next to 'muure'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "wrongMooreName.moore"));
    }

    public void testWrongStatesName() throws Exception {
        assertEquals("3: Syntactic error next to 'staites'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "wrongStatesName.moore"));
    }

    public void testWrongInputName() throws Exception {
        assertEquals("7: Syntactic error next to 'innput'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "wrongInputName.moore"));
    }

    public void testWrongPinsName() throws Exception {
        assertEquals("9: Syntactic error next to 'pis'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "wrongPinsName.moore"));
    }

    public void testWrongClockName() throws Exception {
        assertEquals("10: Syntactic error next to 'click'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "wrongClockName.moore"));
    }

    public void testWrongTransitionName() throws Exception {
        assertEquals("17: Syntactic error next to 'tranion'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "wrongTransitionName.moore"));
    }

    public void testWrongLogicalExpression() throws Exception {
        assertEquals("19: Syntactic error next to '&'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "wrongLogicalExpression.moore"));
    }

    public void testWrongLogicalExpression2() throws Exception {
        assertEquals("20: Syntactic error next to '->'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "wrongLogicalExpression2.moore"));
    }

    public void testWrongOutputName() throws Exception {
        assertEquals("41: Syntactic error next to 'autput'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "wrongOutputName.moore"));
    }

    public void testWrongPinsName2() throws Exception {
        assertEquals("42: Syntactic error next to 'piiins'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "wrongPinsName2.moore"));
    }

    public void testWrongFunctionName() throws Exception {
        assertEquals("48: Syntactic error next to 'fanction'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "wrongFunctionName.moore"));
    }

    public void testNotEofAfterMoore() throws Exception {
        assertEquals("66: Syntactic error next to 'aaaaa'" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathSyntacticErrors + "notEofAfterMoore.moore"));
    }

    /* Lexical Errors */
    public void testInvalidStateName() throws Exception {
        assertEquals("4: Lexical Error: q - invalid symbol" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathLexicalErrors + "invalidStateName.moore"));
    }

    public void testInvalidPinName() throws Exception {
        assertEquals("11: Lexical Error: _ - invalid symbol" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathLexicalErrors + "invalidPinName.moore"));
    }

    public void testInvalidArrow() throws Exception {
        assertEquals("19: Lexical Error: - - invalid symbol" + System.lineSeparator(),
                SyntacticAnalyzer.analyzeFile(pathLexicalErrors + "invalidArrow.moore"));
    }
}
