package Machinuino.model;

import Machinuino.Utils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MooreMachine {

    private String name;
    private String initialState;
    private Set<String> states;
    private Set<Pin> inputPins;
    private Set<Pin> outputPins;
    private Set<Transition> transitions;
    private Set<Output> outputs;
    private Set<BoolPin> allPinsValues;

    /**
     * Builder for the MooreMachine, every method of this class will throw
     * {@link NullPointerException} if null is passed as parameter
     */
    public static class Builder {

        private static final String NAME_TAG = "MooreMachine.Builder";
        private final String name;
        private String initialState;
        private Set<String> states;
        private Set<Pin> inputPins;
        private Set<Pin> outputPins;
        private Set<Transition> transitions;
        private Set<Output> outputs;
        private Set<BoolPin> allPinsValues;

        public Builder(String name) {
            Utils.verifyNullity(NAME_TAG + "#Builder", "name", name);
            this.name = name;
            this.initialState = "";
            this.states = new HashSet<>();
            this.inputPins = new HashSet<>();
            this.outputPins = new HashSet<>();
            this.transitions = new HashSet<>();
            this.outputs = new HashSet<>();
            this.allPinsValues = new HashSet<>();
        }

        public Builder(Builder builder) {
            this.name = builder.name;
            this.initialState = builder.initialState;
            this.states = new HashSet<>(builder.states);
            this.inputPins = new HashSet<>(builder.inputPins);
            this.outputPins = new HashSet<>(builder.outputPins);
            this.transitions = new HashSet<>(builder.transitions);
            this.outputs = new HashSet<>(builder.outputs);
            this.allPinsValues = new HashSet<>(builder.allPinsValues);
        }

        /**
         * Sets the initial state of the machine, preferably use this method after setting all the
         * states of the machine, as this method will check the set of states before setting the
         * initial one
         *
         * @param initialState the initial state of the machine, can not be null and has to be on
         *                     the set of states
         * @return this builder
         * @throws IllegalArgumentException if the stated passed is not on this builder
         * @see #hasState
         */
        public Builder initialState(String initialState) {
            Utils.verifyNullity(NAME_TAG + "#initialState", "initialState", initialState);
            Utils.verifyCollectionIntegrity(NAME_TAG + "#initialState", "initialState",
                    initialState, "states of this builder", states, true);
            this.initialState = initialState;
            return this;
        }

        /**
         * Sets the states of the machine, preferably the states should be one of the first things
         * to be set as they enable other attributes to be set. This method will verify if the
         * states passed do not harm the integrity of the machine
         *
         * @param states states of the machine
         * @return this builder
         * @throws IllegalArgumentException if there is output or a transition envolving a state
         * of this machine not on the parameter passed
         */
        public Builder states(Set<String> states) {
            Utils.verifyCollectionNullity(NAME_TAG + "#states", "states", states);
            for (String state :
                    outputs.stream().map(Output::getState).collect(Collectors.toSet())) {
                Utils.verifyCollectionIntegrity(NAME_TAG + "#states", "state of an output of this" +
                        "builder", state, "states", states, true);
            }
            Set<String> transitionStates = Stream.concat(
                    transitions.stream().map(Transition::getPreviousState),
                    transitions.stream().map(Transition::getNextState))
                    .collect(Collectors.toSet());
            for (String state : transitionStates) {
                Utils.verifyCollectionIntegrity(NAME_TAG + "#states", "state involving a transition"
                        + "on this builder", state, "states", states, true);
            }
            this.states = new HashSet<>(states);
            return this;
        }

        public boolean hasState(String state) {
            return states.contains(state);
        }

        /**
         * Adds a state to the machine
         *
         * @param state a state not already on this machine, can not be null
         * @return this builder
         * @throws IllegalArgumentException if the state passed is already on this machine
         */
        public Builder addState(String state) {
            Utils.verifyNullity(NAME_TAG + "#addState", "state", state);
            Utils.verifyCollectionIntegrity(NAME_TAG + "#addState", "state",
                    state, "states of this builder", states, false);
            states.add(state);
            return this;
        }

        /**
         * Removes a state from the machine
         *
         * @param state a state already on this machine, can not be null
         * @return this builder
         * @throws IllegalArgumentException if the state passed is not on this machine or if this
         * machine has a output or a transition involving the state passed
         * @see #hasState
         * @see #hasOutput
         */
        public Builder removeState(String state) {
            Utils.verifyNullity(NAME_TAG + "#removeState", "state", state);
            Utils.verifyCollectionIntegrity(NAME_TAG + "#removeState", "state", state,
                    "states of this builder", states, true);
            Utils.verifyCollectionIntegrity(NAME_TAG + "#removeState", "state", state,
                    "states which have output",
                    outputs.stream().map(Output::getState).collect(Collectors.toSet()), false);
            Set<String> transitionStates = Stream.concat(
                    transitions.stream().map(Transition::getPreviousState),
                    transitions.stream().map(Transition::getNextState))
                    .collect(Collectors.toSet());
            Utils.verifyCollectionIntegrity(NAME_TAG + "#removeState", "state", state,
                    "states involving a transition", transitionStates, false);
            states.remove(state);
            return this;
        }

        /**
         * Sets the input pins of the machine, preferably this method setting the pins should be
         * the one of the first things to be done as setting the input pins enable setting the
         * transitions
         *
         * @param inputPins the input pins of the machine
         * @return this builder
         * @throws IllegalArgumentException if any of the pins are already in outputPins or if
         * there is at least a pin not on the set passed which is involved in a transition
         * @see #hasOutputPin
         */
        public Builder inputPins(Set<Pin> inputPins) {
            Utils.verifyCollectionNullity(NAME_TAG + "#inputPins", "inputPins", inputPins);
            for (Pin pin : inputPins) {
                Utils.verifyCollectionIntegrity(NAME_TAG + "#inputPins", "inputPin", pin,
                        "outputPins of this builder", outputPins, false);
            }
            Set<Pin> transitionPins = transitions.stream()
                    .flatMap(transition -> transition.getInput().stream())
                    .map(BoolPin::getPin)
                    .collect(Collectors.toSet());
            for (Pin pin : transitionPins) {
                Utils.verifyCollectionIntegrity(NAME_TAG + "#inputPins",
                        "inputPin involving a transition", pin,
                        "inputPins of this builder", inputPins, true);
            }
            allPinsValues.removeIf(boolPin -> this.inputPins.contains(boolPin.getPin()));
            this.inputPins = new HashSet<>(inputPins);
            for (Pin pin : inputPins) {
                allPinsValues.add(BoolPin.ofValue(pin, true));
                allPinsValues.add(BoolPin.ofValue(pin, false));
            }
            return this;
        }

        /**
         * Searches the set of inputPins for the {@link Pin} whose name is
         * specified by the parameter
         *
         * @return a pin with the specified name or null if not found
         */
        public Pin getInputPinOfName(String name) {
            Optional<Pin> optional = inputPins.stream()
                    .filter(pin -> pin.getName().equals(name))
                    .findFirst();
            return optional.isPresent() ? optional.get() : null;
        }

        /**
         * Checks if the pin passed is on this machine, {@link #getInputPinOfName} returns a pin of
         * this machine so there is no need to check
         */
        public boolean hasInputPin(Pin inputPin) {
            return inputPins.contains(inputPin);
        }

        /**
         * Adds a input {@link Pin} to the machine
         *
         * @param inputPin a pin not already on this machine, can not be null
         * @return this builder
         * @throws IllegalArgumentException if the pin passed is already on this machine
         */
        public Builder addInputPin(Pin inputPin) {
            Utils.verifyNullity(NAME_TAG + "#addInputPin", "inputPin", inputPin);
            Utils.verifyCollectionIntegrity(NAME_TAG + "#addInputPin", "inputPin", inputPin,
                    "pins of this builder",
                    Stream.concat(inputPins.stream(), outputPins.stream())
                            .collect(Collectors.toSet()), false);
            allPinsValues.add(BoolPin.ofValue(inputPin, true));
            allPinsValues.add(BoolPin.ofValue(inputPin, false));
            inputPins.add(inputPin);
            return this;
        }

        /**
         * Removes a input {@link Pin} from the machine
         *
         * @param inputPin a pin already on this machine
         * @return this builder
         * @throws IllegalArgumentException if the pin passed is not on this machine or if the pin
         * is involved in a transition
         * @see #hasInputPin
         */
        public Builder removeInputPin(Pin inputPin) {
            Utils.verifyNullity(NAME_TAG + "#removeInputPin", "inputPin", inputPin);
            Utils.verifyCollectionIntegrity(NAME_TAG + "#removeInputPin", "inputPin", inputPin,
                    "inputPins of this builder", inputPins, true);
            Set<Pin> transitionPins = transitions.stream()
                    .flatMap(transition -> transition.getInput().stream())
                    .map(BoolPin::getPin)
                    .collect(Collectors.toSet());
            Utils.verifyCollectionIntegrity(NAME_TAG + "#inputPins", "inputPin", inputPin,
                    "inputPin involving a transition", transitionPins, false);
            allPinsValues.removeIf(boolPin -> boolPin.getPin().equals(inputPin));
            inputPins.remove(inputPin);
            return this;
        }

        /**
         * Sets the output pins of the machine, preferably this method setting the pins should be
         * the one of the first things to be done as setting the output pins enable setting the
         * outputs
         *
         * @param outputPins the output pins of the machine
         * @return this builder
         * @throws IllegalArgumentException if any of the pins are already in inputPins or if
         * there is at least a pin not on the set passed which is involved in a output
         * @see #hasInputPin
         */
        public Builder outputPins(Set<Pin> outputPins) {
            Utils.verifyCollectionNullity(NAME_TAG + "#outputPins", "outputPins", outputPins);
            for (Pin pin : outputPins) {
                Utils.verifyCollectionIntegrity(NAME_TAG + "#outputPins", "outputPin", pin,
                        "inputPins of this builder", inputPins, false);
            }
            Set<Pin> outputs = this.outputs.stream()
                    .flatMap(output -> output.getBoolPins().stream())
                    .map(BoolPin::getPin)
                    .collect(Collectors.toSet());
            for (Pin pin : outputs) {
                Utils.verifyCollectionIntegrity(NAME_TAG + "#outputPins",
                        "outputPin involved in a Output", pin,
                        "outputPins", outputPins, true);
            }
            allPinsValues.removeIf(boolPin -> this.outputPins.contains(boolPin.getPin()));
            this.outputPins = new HashSet<>(outputPins);
            for (Pin pin : outputPins) {
                allPinsValues.add(BoolPin.ofValue(pin, true));
                allPinsValues.add(BoolPin.ofValue(pin, false));
            }
            return this;
        }

        /**
         * Searches the set of outputPins for the {@link Pin} whose name is
         * specified by the parameter
         *
         * @return a pin with the specified name or null if not found
         */
        public Pin getOutputPinOfName(String name) {
            Optional<Pin> optional = outputPins.stream()
                    .filter(pin -> pin.getName().equals(name))
                    .findFirst();
            return optional.isPresent() ? optional.get() : null;
        }

        /**
         * Checks if the pin passed is on this machine, {@link #getOutputPinOfName} returns a pin of
         * this machine so there is no need to check
         */
        public boolean hasOutputPin(Pin outputPin) {
            return outputPins.contains(outputPin);
        }

        /**
         * Adds a output {@link Pin} to the machine
         *
         * @param outputPin a pin not already on this machine, can not be null
         * @return this builder
         * @throws IllegalArgumentException if the pin passed is already on this machine
         */
        public Builder addOutputPin(Pin outputPin) {
            Utils.verifyNullity(NAME_TAG + "#addOutputPin", "outputPin", outputPin);
            Utils.verifyCollectionIntegrity(NAME_TAG + "#addOutputPin", "outputPin", outputPin,
                    "pins of this builder",
                    Stream.concat(inputPins.stream(), outputPins.stream())
                            .collect(Collectors.toSet()), false);
            allPinsValues.add(BoolPin.ofValue(outputPin, true));
            allPinsValues.add(BoolPin.ofValue(outputPin, false));
            outputPins.add(outputPin);
            return this;
        }

        /**
         * Removes a output {@link Pin} from the machine
         *
         * @param outputPin a pin already on this machine
         * @return this builder
         * @throws IllegalArgumentException if the pin passed is not on this machine or if the pin
         * if involved in a Output
         * @see #hasOutputPin
         */
        public Builder removeOutputPin(Pin outputPin) {
            Utils.verifyNullity(NAME_TAG + "#removeOutputPin", "outputPin", outputPin);
            Utils.verifyCollectionIntegrity(NAME_TAG + "#removeOutputPin", "outputPin", outputPin,
                    "outputPins of this builder", outputPins, true);
            Set<Pin> outputs = this.outputs.stream()
                    .flatMap(output -> output.getBoolPins().stream())
                    .map(BoolPin::getPin)
                    .collect(Collectors.toSet());
            Utils.verifyCollectionIntegrity(NAME_TAG + "#outputPins", "outputPin", outputPin,
                    "outputPins involved in a Output", outputs, false);
            allPinsValues.removeIf(boolPin -> boolPin.getPin().equals(outputPin));
            outputPins.remove(outputPin);
            return this;
        }

        /**
         * Returns a pin associated with a value
         *
         * @param pin Pin associated with a value, preferable use {@link #getInputPinOfName} or
         *            {@link #getOutputPinOfName} to get the Pin as Pins returned by these methods
         *            are garantied to be on this builder
         * @return a {@link BoolPin} specified by the parameter
         * @throws IllegalArgumentException if the pin passed is not on any of this builder's sets
         * @see #hasOutputPin
         * @see #hasInputPin
         */
        public BoolPin getBoolPinOfValue(Pin pin, boolean high) {
            Utils.verifyNullity(NAME_TAG + "#getBoolPinOfValue", "pin", pin);
            Utils.verifyCollectionIntegrity(NAME_TAG + "#getBoolPinOfValue", "pin", pin,
                    "pins of this builder",
                    Stream.concat(inputPins.stream(), outputPins.stream())
                            .collect(Collectors.toSet()), true);
            Optional<BoolPin> optional = allPinsValues.stream()
                    .filter(boolPin -> boolPin.getPin().equals(pin) && boolPin.isHigh() == high)
                    .findFirst();
            return optional.isPresent() ? optional.get() : null;
        }

        public Builder transitions(Set<Transition> transitions) {
            Utils.verifyCollectionNullity(NAME_TAG + "#transitions", "transitions", transitions);
            for (Transition transition : transitions) {
                Utils.verifyCollectionIntegrity(NAME_TAG + "#transitions",
                        "previous state of a transition", transition.getPreviousState(),
                        "states of this builder", states, true);
                Utils.verifyCollectionIntegrity(NAME_TAG + "#transitions",
                        "next state of a transition", transition.getNextState(),
                        "states of this builder", states, true);
                Set<Pin> pins = transition.getInput().stream()
                        .map(BoolPin::getPin)
                        .collect(Collectors.toSet());
                if (transition.getInput().size() != pins.size()) {
                    throw new IllegalArgumentException(NAME_TAG + "#transitions: " +
                            "there is a pin with two values on " + transition);
                }
                for (Pin pin : pins) {
                    Utils.verifyCollectionIntegrity(NAME_TAG + "#transitions",
                            "pin of a transition", pin,
                            "inputPins of this builder", inputPins, true);
                }
            }
            this.transitions = new HashSet<>(transitions);
            return this;
        }

        /**
         * Sets the outputs of this machine, preferably use this method after setting all the
         * states and pins of the machine, as this method will check the set of states and pins
         * before setting the outputs
         *
         * @param outputs the outputs of the machine, a element can not contain a state or
         *                pins not contained on this machine, can not be null, neither contain a
         *                null element
         * @return this builder
         * @throws IllegalArgumentException if the state or the pins of a output is not on this
         * machine
         * @see #hasState
         * @see #hasOutputPin
         * @see #hasInputPin
         */
        public Builder outputs(Set<Output> outputs) {
            Utils.verifyCollectionNullity(NAME_TAG + "#outputs", "outputs", outputs);
            for (Output output : outputs) {
                Utils.verifyCollectionIntegrity(NAME_TAG + "#outputs", "state of an output",
                        output.getState(), "states of this builder", states, true);
                Set<Pin> pins = output.getBoolPins().stream()
                        .map(BoolPin::getPin)
                        .collect(Collectors.toSet());
                if (output.getBoolPins().size() != pins.size()) {
                    throw new IllegalArgumentException(NAME_TAG + "#outputs: " +
                            "there is a pin with two values on " + outputs);
                }
                for (Pin pin : pins) {
                    Utils.verifyCollectionIntegrity(NAME_TAG + "#outputs", "pin of a output", pin,
                            "outputPins of this builder", outputPins, true);
                }
            }
            this.outputs = new HashSet<>(outputs);
            return this;
        }

        public boolean hasOutput(String state) {
            return outputs.stream()
                    .map(Output::getState)
                    .anyMatch(outputState -> outputState.equals(state));
        }

        /**
         * Adds a output to this machine, preferably use this method after setting all the
         * states and pins of the machine, as this method will check the set of states and pins
         * before adding the output
         *
         * @param state a state of this machine, can not be null
         * @param boolPins pins contained on this machine, can not be null, neither contain a
         *                 null element
         * @return this builder
         * @throws IllegalArgumentException if the state or the pins are not on this machine
         * @see #hasState
         * @see #hasOutputPin
         * @see #hasInputPin
         */
        public Builder addOutput(String state, Set<BoolPin> boolPins) {
            Utils.verifyNullity(NAME_TAG + "#addOutput", "state", state);
            Utils.verifyCollectionNullity(NAME_TAG + "#addOutput", "boolPins", boolPins);
            Utils.verifyCollectionIntegrity(NAME_TAG + "#outputs", "state", state,
                    "states of this builder", states, true);
            Utils.verifyCollectionIntegrity(NAME_TAG + "#addOutput", "state", state,
                    "states which have output",
                    outputs.stream().map(Output::getState).collect(Collectors.toSet()), false);
            Set<Pin> pins = boolPins.stream()
                    .map(BoolPin::getPin)
                    .collect(Collectors.toSet());
            if (boolPins.size() != pins.size()) {
                throw new IllegalArgumentException(NAME_TAG + "#addOutput: " +
                        "there is a pin with two values on " + boolPins);
            }
            for (Pin pin : pins) {
                Utils.verifyCollectionIntegrity(NAME_TAG + "#addOutput", "pin", pin,
                        "outputPins of this builder", outputPins, true);
            }
            outputs.add(Output.ofValue(state, boolPins));
            return this;
        }

        /**
         * Removes a output from this machine
         *
         * @param state a state whose output has been set on this machine already
         * @return this builder
         * @throws IllegalArgumentException if the state passed does not have a output attached to
         * it
         * @see #hasOutput
         */
        public Builder removeOutput(String state) {
            Utils.verifyNullity(NAME_TAG + "#removeOutput", "state", state);
            Utils.verifyCollectionIntegrity(NAME_TAG + "#removeOutput", "state", state,
                    "states which have output",
                    outputs.stream().map(Output::getState).collect(Collectors.toSet()), true);
            outputs.removeIf(output -> output.getState().equals(state));
            return this;
        }

        /**
         * Builds the moore machine with characteristics specified by this builder
         *
         * @return A moore machine
         */
        public MooreMachine build() {
            return new MooreMachine(this);
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "name='" + name + '\'' +
                    ", initialState='" + initialState + '\'' +
                    ", states=" + states +
                    ", inputPins=" + inputPins +
                    ", outputPins=" + outputPins +
                    ", transitions=" + transitions +
                    ", outputs=" + outputs +
                    ", allPinsValues=" + allPinsValues +
                    '}';
        }
    }

    private MooreMachine(Builder builder) {
        this.name = builder.name;
        this.initialState = builder.initialState;
        this.states = new HashSet<>(builder.states);
        this.inputPins = new HashSet<>(builder.inputPins);
        this.outputPins = new HashSet<>(builder.outputPins);
        this.transitions = new HashSet<>(builder.transitions);
        this.outputs = new HashSet<>(builder.outputs);
        this.allPinsValues = new HashSet<>(builder.allPinsValues);
    }

    public String getName() {
        return name;
    }

    public String getInitialState() {
        return initialState;
    }

    public Set<String> getStates() {
        return new HashSet<>(states);
    }

    public Set<Pin> getInputPins() {
        return new HashSet<>(inputPins);
    }

    public Set<Pin> getOutputPins() {
        return new HashSet<>(outputPins);
    }

    public Set<Transition> getTransitions() {
        return new HashSet<>(transitions);
    }

    public Set<Output> getOutputs() {
        return new HashSet<>(outputs);
    }

    public Set<BoolPin> getAllPinsValues() {
        return new HashSet<>(allPinsValues);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != getClass()) return false;

        MooreMachine machine = (MooreMachine) o;
        return name.equals(machine.name) && initialState.equals(machine.initialState) &&
                states.equals(machine.states) && inputPins.equals(machine.inputPins) &&
                transitions.equals(machine.transitions) && outputPins.equals(machine.outputPins) &&
                outputs.equals(machine.outputs);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + initialState.hashCode();
        result = 31 * result + states.hashCode();
        result = 31 * result + inputPins.hashCode();
        result = 31 * result + outputPins.hashCode();
        result = 31 * result + transitions.hashCode();
        result = 31 * result + outputs.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MooreMachine{" +
                "name='" + name + '\'' +
                ", initialState='" + initialState + '\'' +
                ", states=" + states +
                ", inputPins=" + inputPins +
                ", outputPins=" + outputPins +
                ", transitions=" + transitions +
                ", outputs=" + outputs +
                '}';
    }
}
