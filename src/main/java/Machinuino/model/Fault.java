package Machinuino.model;

import Machinuino.Utils;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Fault {

    private static final String NAME_TAG = "Fault";
    private List<String> errors;
    private List<String> warnings;


    //TODO remove
    /**
     * getLine:    return the line of the context
     */
    private <T extends ParserRuleContext> int getLine(T ctx) {
        return ctx.getStart().getLine();
    }

    private Fault(List<String> errors, List<String> warnings) {
        if (errors == null) throw new NullPointerException(NAME_TAG +
                "#Fault: Null errors array list.");
        if (warnings == null) throw new NullPointerException(NAME_TAG +
                "#Fault: Null warnings array list.");

        this.errors = errors;
        this.warnings = warnings;
    }

    public static Fault getInstance() {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        return new Fault(errors, warnings);
    }

    public void addError(String error) {
        if (error == null) throw new NullPointerException(NAME_TAG +
                "#addError: Null error message.");

        if (!errors.contains(error))
            errors.add(error);
    }

    public void addDuplicatePin(String pinName, int line) {
        Utils.verifyNullity(NAME_TAG + "#addDuplicatePin", "pinName", pinName);

        if (line < 0) {
            throw new IllegalArgumentException(NAME_TAG +
                    "#addDuplicatePin: Negative line number!");
        }

        this.addError(line + ": Duplicate pin " + pinName + "." + System.lineSeparator());
    }

    public void addWarning(String warning) {
        if (warning == null) throw new NullPointerException(NAME_TAG +
                "#addWarning: Null warning message.");

        if (!warnings.contains(warning))
            warnings.add(warning);
    }

    public void addWarningDuplicateSymbol(String symbol, int line) {
        Utils.verifyNullity(NAME_TAG + "#addWarningDuplicateSymbol", "symbol", symbol);
        if (line < 0) throw new IllegalArgumentException(NAME_TAG +
                "#addWarningDuplicateSymbol: Negative line!");

        addWarning(line + ": Symbol " + symbol + " already used. Will be ignored." + System.lineSeparator());
    }

    public void addWarningEmptySection(String section, int line) {
        if (section == null) {
            throw new NullPointerException(NAME_TAG +
                    "#addWarningEmptySection: Null section string.");
        }
        if (line < 0) {
            throw new IllegalArgumentException(NAME_TAG +
                    "#addWarningEmptySection: Negative line number!");
        }

        addWarning(line + ": Empty \"" + section + "\" section" + System.lineSeparator());
    }

    public String getErrors() {
        return errors.stream().collect(Collectors.joining(""));
    }

    public String getWarnings() {
        return warnings.stream().collect(Collectors.joining(""));
    }

    @Override
    public String toString() {
        return "Fault {" +
                "errors=" + errors +
                ", warnings=" + warnings +
                '}';
    }
}
