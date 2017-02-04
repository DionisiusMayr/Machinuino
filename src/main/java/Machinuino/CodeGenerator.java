package Machinuino;

import Machinuino.model.MooreMachine;
import Machinuino.model.Pin;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CodeGenerator {

    private static CodeGenerator instance;

    private CodeGenerator() {
    }

    public static CodeGenerator getInstance() {
        if (instance == null) instance = new CodeGenerator();
        return instance;
    }

    public String generateCode(MooreMachine machine) {
        StringBuilder builder = new StringBuilder();
        builder.append(definePinsAndState(machine));

        return builder.toString();
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

        builder.append(clockPinOrNot.get(Boolean.FALSE).stream());

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
        for (Pin pin : pinStream.collect(Collectors.toSet())) {
            builder.append("const int _")
                    .append(pin.getName())
                    .append(" = ")
                    .append(pin.getNumber())
                    .append(";")
                    .append(System.lineSeparator());
        }
        return builder.toString();
    }

    private String defineStates(MooreMachine machine) {
        StringBuilder builder = new StringBuilder();
        final String initialState = machine.getInitialState();
        builder.append("/* States */")
                .append(System.lineSeparator())
                .append("const int __")
                .append(initialState)
                .append(" = 0;")
                .append(System.lineSeparator());
        Set<String> statesWithoutTheInitial = machine.getStates()
                .filter(state -> !state.equals(initialState))
                .collect(Collectors.toSet());
        int i = 1;
        for (String state : statesWithoutTheInitial) {
            builder.append("/* States */")
                    .append("const int __")
                    .append(state)
                    .append(" = ")
                    .append(i)
                    .append(";")
                    .append(System.lineSeparator());
        }
        return builder.toString();
    }
}
