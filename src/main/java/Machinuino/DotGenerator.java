package Machinuino;

import Machinuino.model.BoolPin;
import Machinuino.model.MooreMachine;
import Machinuino.model.Output;
import Machinuino.model.Transition;

import java.util.stream.Collectors;

public class DotGenerator {

    private static DotGenerator instance;
    private static final String LS = System.lineSeparator();
    private static final int INDENTATION_SPACE = 4;

    public static DotGenerator getInstance() {
        if (instance == null) instance = new DotGenerator();
        return instance;
    }

    private void DotGenerator() {
    }

    private String indent(int numberOfTabs) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < INDENTATION_SPACE * numberOfTabs; i++) builder.append(" ");

        return builder.toString();
    }

    private String outputToGv(Output output) {
        return "\"" + output.getState() + " \\\\ \\n" + output.getBoolPins().stream()
                .map(s -> boolPinToGv(s)).collect(Collectors.joining(", \\n")) + "\"";
    }

    private String boolPinToGv(BoolPin boolPin) {
        return (boolPin.isHigh() ? "" : "!") + boolPin.getPin().getName();
    }

    private String transitionToGv(Transition transition) {
        return "\"" + transition.getInput().stream().map(s -> boolPinToGv(s))
                .collect(Collectors.joining(", ")) + "\"";
    }

    private String generateTransitionsImage(MooreMachine machine) {
        StringBuilder sb = new StringBuilder();

        for (Transition trans : machine.getTransitions().collect(Collectors.toSet())) {
            sb.append(indent(1))
                    .append(outputToGv(getOutputOfState(machine, trans.getPreviousState())))
                    .append(" -> ")
                    .append(outputToGv(getOutputOfState(machine, trans.getNextState())))
                    .append("[ label = ").append(transitionToGv(trans)).append("]")
                    .append(LS);
        }

        return sb.toString();
    }

    private Output getOutputOfState(MooreMachine machine, String state) {
        return machine.getOutputs()
                .filter(output -> output.getState().equals(state))
                .collect(Collectors.toList()).get(0);
    }

    String generateImage(MooreMachine machine) {
        StringBuilder sb = new StringBuilder();

        sb.append("digraph ").append(machine.getName()).append(" {").append(LS)
                .append(indent(1)).append("node [shape = circle];").append(LS)
                .append(indent(1)).append("rankdir = \"LR\";").append(LS)
                .append(LS)
                .append(indent(1)).append("x [style = invis]").append(LS)
                .append(indent(1)).append("x -> ")
                .append(outputToGv(getOutputOfState(machine, machine.getInitialState())))
                .append(";").append(LS)
                .append(LS)
                .append(generateTransitionsImage(machine))
                .append("}").append(LS);

        return sb.toString();
    }
}
