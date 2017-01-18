package Machinuino;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.io.IOException;

class SyntacticAnalyzer {

    private SyntacticAnalyzer() {
    }

    /**
     * Analyze a file on {@code fileLocation} and return the syntactic error it contains, if any.
     *
     * @param fileLocation path to the file to be analyzed
     * @return a String containing the syntactic errors, if there is any,
     * otherwise returns an empty String
     */
    static String analyzeFile(String fileLocation) throws IOException {
        ANTLRInputStream input = new ANTLRFileStream(fileLocation);
        MachinuinoLexer lexer = new MachinuinoLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MachinuinoParser parser = new MachinuinoParser(tokens);

        StringBuilder errors = new StringBuilder();

        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                                    int col, String msg, RecognitionException e) {
                Token t = (Token) offendingSymbol;

                String error = String.format("%d: Syntactic error next to '%s'", line,
                        t.getText());

                throw new ParseCancellationException(error + System.lineSeparator());
            }
        });

        try {
            parser.moore();
        } catch (ParseCancellationException pce) {
            errors.append(pce.getMessage());
        }

        if (errors.length() == 0)
            return "";
        else
            return errors.toString();
    }
}
