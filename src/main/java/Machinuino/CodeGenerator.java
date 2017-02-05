package Machinuino;

import Machinuino.model.*;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CodeGenerator {

    private static CodeGenerator instance;
    private static String PIN_START_SYMBOL = "_";
    private static String STATE_START_SYMBOL = "__";

    private CodeGenerator() {
    }

    public static CodeGenerator getInstance() {
        if (instance == null) instance = new CodeGenerator();
        return instance;
    }

    public String generateCode(MooreMachine machine) {
        return definePinsAndState(machine) +
                System.lineSeparator() +
                defineIsHighFunction() +
                System.lineSeparator() +
                defineTransitionFunction(machine) +
                System.lineSeparator() +
                defineOutputFunction(machine) +
                System.lineSeparator() +
                defineLogicVariablesAndFunction(machine);
    }

    private String definePinsAndState(MooreMachine machine) {
        return defineInputPins(machine) +
                System.lineSeparator() +
                defineOutputPins(machine) +
                System.lineSeparator() +
                defineStates(machine);
    }

    private String defineInputPins(MooreMachine machine) {
        StringBuilder builder = new StringBuilder();
        builder.append("/* Input */")
                .append(System.lineSeparator());
        Map<Boolean, Set<Pin>> clockPinOrNot = machine.getInputPins()
                .collect(Collectors.partitioningBy(pin -> pin.getName().equals("clock"),
                        Collectors.toSet()));

        Optional<Integer> optional = clockPinOrNot.get(Boolean.TRUE).stream()
                .map(Pin::getNumber)
                .findFirst();
        // Impossible to get -1 in clockNumber, by construction, MooreMachine always has clock
        int clockNumber = optional.isPresent() ? optional.get() : -1;
        builder.append("const int clock = ")
                .append(clockNumber)
                .append(";")
                .append(System.lineSeparator());

        builder.append(definePins(clockPinOrNot.get(Boolean.FALSE).stream()));

        return builder.toString();
    }

    private String defineOutputPins(MooreMachine machine) {
        StringBuilder builder = new StringBuilder();
        builder.append("/* Output */")
                .append(System.lineSeparator());
        builder.append(definePins(machine.getOutputPins()));
        return builder.toString();
    }

    private String definePins(Stream<Pin> pinStream) {
        StringBuilder builder = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        for (Pin pin : pinStream.collect(Collectors.toSet())) {
            builder.append("const int ")
                    .append(PIN_START_SYMBOL)
                    .append(pin.getName())
                    .append(" = ")
                    .append(pin.getNumber())
                    .append(";")
                    .append(lineSeparator);
        }
        return builder.toString();
    }

    private String defineStates(MooreMachine machine) {
        StringBuilder builder = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        builder.append("/* States */")
                .append(lineSeparator);
        int i = 0;
        for (String state : machine.getStates().collect(Collectors.toSet())) {
            builder.append("const int ")
                    .append(STATE_START_SYMBOL)
                    .append(state)
                    .append(" = ")
                    .append(i)
                    .append(";")
                    .append(lineSeparator);
            i++;
        }
        return builder.toString();
    }

    private String defineIsHighFunction() {
        return "bool isHigh(int pin) {" + System.lineSeparator() +
                "    return digitalRead(pin) == HIGH ? true : false;" + System.lineSeparator() +
                "}" + System.lineSeparator();
    }

    private String defineTransitionFunction(MooreMachine machine) {
        StringBuilder builder = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        builder.append("int transition(int current) {").append(lineSeparator)
                .append("    switch(current) {").append(lineSeparator)
                .append(defineTransitionsOfEachState(machine))
                .append("        default:").append(lineSeparator)
                .append("            // Not reachable").append(lineSeparator)
                .append("            exit(1);").append(lineSeparator)
                .append("            break;").append(lineSeparator)
                .append("    }").append(lineSeparator)
                .append("}").append(lineSeparator);

        return builder.toString();
    }

    private String defineTransitionsOfEachState(MooreMachine machine) {
        StringBuilder builder = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        for (String state : machine.getStates().sorted(String::compareToIgnoreCase)
                .collect(Collectors.toSet())) {
            builder.append("        case ")
                    .append(STATE_START_SYMBOL)
                    .append(state)
                    .append(":").append(lineSeparator)
                    .append(defineEachTransitionOfAState(state, machine))
                    .append("            break;").append(lineSeparator);
        }

        return builder.toString();
    }

    private String defineEachTransitionOfAState(String state, MooreMachine machine) {
        StringBuilder builder = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        Set<Transition> transitions = machine.getTransitions()
                .filter(transition -> transition.getPreviousState().equals(state))
                .collect(Collectors.toSet());
        for (Transition transition : transitions) {
            builder.append("            if (");
            String logicalExpression = transition.getInput().stream()
                    .map(this::mapBoolPinToIsHigh)
                    .collect(Collectors.joining(" && "));
            builder.append(logicalExpression)
                    .append(") return ")
                    .append(STATE_START_SYMBOL)
                    .append(transition.getNextState())
                    .append(";").append(lineSeparator);
        }
        return builder.toString();
    }

    private String mapBoolPinToIsHigh(BoolPin boolPin) {
        return (boolPin.isHigh() ? "" : "!") + "isHigh(" + PIN_START_SYMBOL +
                boolPin.getPin().getName() + ")";
    }

    private String defineOutputFunction(MooreMachine machine) {
        StringBuilder builder = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        builder.append("void output(int current) {").append(lineSeparator)
                .append("    switch(current) {").append(lineSeparator)
                .append(defineOutputOfEachState(machine))
                .append("        default:").append(lineSeparator)
                .append("            // Not reachable").append(lineSeparator)
                .append("            exit(1);").append(lineSeparator)
                .append("            break;").append(lineSeparator)
                .append("    }").append(lineSeparator)
                .append("}").append(lineSeparator);

        return builder.toString();
    }

    private String defineOutputOfEachState(MooreMachine machine) {
        StringBuilder builder = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        for (String state : machine.getStates().sorted(String::compareToIgnoreCase)
                .collect(Collectors.toSet())) {
            builder.append("        case ")
                    .append(STATE_START_SYMBOL)
                    .append(state)
                    .append(":").append(lineSeparator)
                    .append(defineOutputOfAState(state, machine))
                    .append("            break;").append(lineSeparator);
        }

        return builder.toString();
    }

    private String defineOutputOfAState(String state, MooreMachine machine) {
        StringBuilder builder = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        Set<Output> outputs = machine.getOutputs()
                .filter(output -> output.getState().equals(state))
                .collect(Collectors.toSet());
        for (Output output : outputs) {
            builder.append("            ");
            Set<Pin> outputPinsOnThisOutput = output.getBoolPins().stream().map(BoolPin::getPin)
                    .collect(Collectors.toSet());
            String setOutputPins = Stream.concat(
                    output.getBoolPins().stream(),
                    machine.getOutputPins().filter(pin -> !outputPinsOnThisOutput.contains(pin))
                            .map(pin -> pinToFalseBoolPin(pin, machine))
            ).map(this::mapBoolPinToOutput)
                    .collect(Collectors.joining(lineSeparator + "            "));
            builder.append(setOutputPins);
        }
        builder.append(lineSeparator);
        return builder.toString();
    }

    private String mapBoolPinToOutput(BoolPin boolPin) {
        return "digitalWrite(" + PIN_START_SYMBOL + boolPin.getPin().getName() + ", " +
                (boolPin.isHigh() ? "HIGH" : "LOW") + ");";
    }

    private BoolPin pinToFalseBoolPin(Pin pin, MooreMachine machine) {
        Optional<BoolPin> optional = machine.getAllPinsValues()
                .filter(boolPin -> boolPin.getPin().equals(pin))
                .filter(boolPin -> !boolPin.isHigh())
                .findFirst();
        // Impossible to this optional not have a value
        return optional.isPresent() ? optional.get() : null;
    }

    private String defineLogicVariablesAndFunction(MooreMachine machine) {
        StringBuilder builder = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        builder.append("int currentState;").append(lineSeparator)
                .append("bool previousClock;").append(lineSeparator)
                .append(lineSeparator)
                .append(defineSetupFunction(machine))
                .append(lineSeparator)
                .append(defineLoopFunction());

        return builder.toString();
    }

    private String defineSetupFunction(MooreMachine machine) {
        StringBuilder builder = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        builder.append("void setup() {").append(lineSeparator)
                .append(setupInputPins(machine)).append(lineSeparator)
                .append(setupOutputPins(machine)).append(lineSeparator)
                .append(setupInitialConditions(machine))
                .append("}").append(lineSeparator);

        return builder.toString();
    }

    private String setupInputPins(MooreMachine machine) {
        StringBuilder builder = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        builder.append("    /* Input */").append(lineSeparator)
                .append("    pinMode(clock, INPUT);").append(lineSeparator)
                .append("    ")
                .append(machine.getInputPins().filter(pin -> !pin.getName().equals("clock"))
                        .map(this::setupAInputPin)
                        .collect(Collectors.joining(lineSeparator + "    ")))
                .append(lineSeparator);

        return builder.toString();
    }

    private String setupAInputPin(Pin pin) {
        return "pinMode(" + PIN_START_SYMBOL + pin.getName() + ", INPUT);";
    }

    private String setupOutputPins(MooreMachine machine) {
        StringBuilder builder = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        builder.append("    /* Output */").append(lineSeparator)
                .append("    ")
                .append(machine.getOutputPins().map(this::setupAOutputPin)
                        .collect(Collectors.joining(lineSeparator + "    ")))
                .append(lineSeparator);

        return builder.toString();
    }

    private String setupAOutputPin(Pin pin) {
        return "pinMode(" + PIN_START_SYMBOL + pin.getName() + ", OUTPUT);";
    }

    private String setupInitialConditions(MooreMachine machine) {
        String lineSeparator = System.lineSeparator();
        return "    /* Initial state */" + lineSeparator +
                "    currentState = " + STATE_START_SYMBOL + machine.getInitialState() + ";" +
                lineSeparator +
                "    previousClock = false;" + lineSeparator +
                "    output(" + STATE_START_SYMBOL + machine.getInitialState() + ");" +
                lineSeparator;
    }

    private String defineLoopFunction() {
        StringBuilder builder = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        builder.append("void loop() {").append(lineSeparator)
                .append("    if (!previousClock && digitalRead(clock) == HIGH) {")
                .append(lineSeparator)
                .append("        currentState = transition(currentState);").append(lineSeparator)
                .append("        output(currentState);").append(lineSeparator)
                .append("    }").append(lineSeparator)
                .append(lineSeparator)
                .append("    previousClock = digitalRead(clock) == HIGH;").append(lineSeparator)
                .append("}").append(lineSeparator);

        return builder.toString();
    }
}
