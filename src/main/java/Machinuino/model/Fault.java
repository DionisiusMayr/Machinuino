package Machinuino.model;

import Machinuino.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Fault {

    private static final String NAME_TAG = "Fault";
    private List<String> errors;
    private List<String> warnings;

    private Fault(List<String> errors, List<String> warnings) {
        if (errors == null) throw new NullPointerException(NAME_TAG + "#Fault: Null errors array list.");
        if (warnings == null) throw new NullPointerException(NAME_TAG + "#Fault: Null warnings array list.");

        this.errors = errors;
        this.warnings = warnings;
    }

    public static Fault getInstance() {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        return new Fault(errors, warnings);
    }

    public void addError(String error) {
        if (error == null) throw new NullPointerException(NAME_TAG + "#addError: Null error message.");
        if (!errors.contains(error)) errors.add(error);
    }

    public void addErrorDuplicatePin(String pinName, int line) {
        Utils.verifyNullity(NAME_TAG + "#addErrorDuplicatePin", "pinName", pinName);
        Utils.verifyPositive(NAME_TAG + "#addErrorDuplicatePin", "line", line);

        this.addError(line + ": Duplicate pin " + pinName + "." + System.lineSeparator());
    }

    public void addErrorUndeclaredState(String state, int line) {
        Utils.verifyNullity(NAME_TAG + "#addErrorStateUndeclared", "state", state);
        Utils.verifyPositive(NAME_TAG + "#addErrorStateUndeclared", "line", line);

        this.addError(line + ": State " + state + " undeclared." + System.lineSeparator());
    }

    public void addErrorUndeclaredInputPin(String pinName, int line) {
        Utils.verifyNullity(NAME_TAG + "#addErrorUndeclaredPin", "Pin name", pinName);
        Utils.verifyPositive(NAME_TAG + "#addErrorUndeclaredPin", "line", line);

        this.addError(line + ": Input Pin " + pinName + " undeclared." + System.lineSeparator());
    }

    public void addErrorUndeclaredOutputPin(String pinName, int line) {
        Utils.verifyNullity(NAME_TAG + "#addErrorUndeclaredOutputPin", "Pin name", pinName);
        Utils.verifyPositive(NAME_TAG + "#addErrorUndeclaredOutputPin", "line", line);

        this.addError(line + ": Output Pin " + pinName + " undeclared." + System.lineSeparator());
    }

    public void addErrorPinNumberAlreadyUsed(int pinNumber, int line) {
        Utils.verifyPositive(NAME_TAG + "#addErrorPinNumberAlreadyUsed", "pin number", pinNumber);
        Utils.verifyPositive(NAME_TAG + "#addErrorPinNumberAlreadyUsed", "line", line);

        this.addError(line + ": Pin Number " + pinNumber + " already used." + System.lineSeparator());
    }

    public void addErrorOutputAlreadyDefined(String outputPinName, int line) {
        Utils.verifyNullity(NAME_TAG + "#addErrorOutputAlreadyDefined", "Pin name", outputPinName);
        Utils.verifyPositive(NAME_TAG + "#addErrorOutputAlreadyDefined", "line", line);

        this.addError(line + ": Output \"" + outputPinName + "\" already defined." + System.lineSeparator());
    }

    public void addErrorInputAlreadyInExp(String inputPinName, int line) {
        Utils.verifyNullity(NAME_TAG + "#addErrorInputAlreadyInExp", "Pin name", inputPinName);
        Utils.verifyPositive(NAME_TAG + "#addErrorInputAlreadyInExp", "line", line);

        this.addError(line + ": Input Pin \"" + inputPinName +
                "\" already used in expression." + System.lineSeparator());
    }

    public void addErrorDuplicateTransitionFromState(String state, int line) {
        Utils.verifyNullity(NAME_TAG + "#addErrorDuplicateTransitionFromState", "State", state);
        Utils.verifyPositive(NAME_TAG + "#addErrorDuplicateTransitionFromState", "line", line);

        this.addError(line + ": Transition coming from state \"" + state + "\" already defined." +
                System.lineSeparator());
    }

    public String getErrors() {
        return errors.stream().collect(Collectors.joining(""));
    }

    public void addWarning(String warning) {
        Utils.verifyNullity(NAME_TAG + "#addWarning", "warning message", warning);

        if (!warnings.contains(warning))
            warnings.add(warning);
    }

    public void addWarningDuplicateSymbol(String symbol, int line) {
        Utils.verifyNullity(NAME_TAG + "#addWarningDuplicateSymbol", "symbol", symbol);
        Utils.verifyPositive(NAME_TAG + "#addWarningDuplicateSymbol", "line", line);

        this.addWarning(line + ": Symbol " + symbol + " already used. Will be ignored." + System.lineSeparator());
    }

    public void addWarningEmptySection(String section, int line) {
        Utils.verifyNullity(NAME_TAG + "#addWarningEmptySection", "section string", section);
        Utils.verifyPositive(NAME_TAG + "#addWarningEmptySection", "line", line);

        this.addWarning(line + ": Empty \"" + section + "\" section" + System.lineSeparator());
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
