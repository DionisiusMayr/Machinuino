package Machinuino.model;

import Machinuino.Utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MooreMachine {

    private String name;
    private String initialState;
    private Set<String> states;
    private Set<Pin> inputPins;
    private Set<Pin> outputPins;
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
        private Set<Output> outputs;
        private Set<BoolPin> allPinsValues;

        public Builder(String name) {
            Utils.verifyNullity(NAME_TAG + "#Builder", "name", name);
            this.name = name;
            initialState = "";
            states = new HashSet<>();
            inputPins = new HashSet<>();
            outputPins = new HashSet<>();
            outputs = new HashSet<>();
            allPinsValues = new HashSet<>();
        }

        public Builder(Builder builder) {
            this.name = builder.name;
            this.initialState = builder.initialState;
            this.states = builder.states;
            this.inputPins = builder.inputPins;
            this.outputPins = builder.outputPins;
            this.outputs = builder.outputs;
            this.allPinsValues = builder.allPinsValues;
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
            if (!hasState(initialState)) {
                throw new IllegalArgumentException(NAME_TAG + "#initialState: " +
                        initialState + "was not on this builder " + this);
            }
            this.initialState = initialState;
            return this;
        }

        /**
         * Sets the states of the machine, preferably the states should be one of the first things
         * to be set as they enable other attributes to be set. This method will verify if the
         * states passed do not harm the integrity of the machine
         *
         * @param states
         * @return this builder
         * @throws IllegalArgumentException if there is output for a state of this machine not
         * on the parameter passed
         */
        public Builder states(Set<String> states) {
            Utils.verifyCollectionNullity(NAME_TAG + "#states", "states", states);
            for (Output output : outputs) {
                if (!states.contains(output.getState())) {
                    throw new IllegalArgumentException(NAME_TAG + "#states: " +
                            states + " did not contain a state which this machine has " +
                            "an output on " + this);
                }
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
            if (hasState(state)) {
                throw new IllegalArgumentException(NAME_TAG + "#addState: " +
                        state + " already on this builder " + this);
            }
            states.add(state);
            return this;
        }

        /**
         * Removes a state from the machine
         *
         * @param state a state already on this machine, can not be null
         * @return this builder
         * @throws IllegalArgumentException if the state passed is not on this machine or if this
         * machine has a output on the state passed
         * @see #hasState
         * @see #hasOutput
         */
        public Builder removeState(String state) {
            Utils.verifyNullity(NAME_TAG + "#removeState", "state", state);
            if (!hasState(state)) {
                throw new IllegalArgumentException(NAME_TAG + "#removeState: " +
                        state + " was not on this builder " + this);
            }
            if (hasOutput(state)) {
                throw new IllegalArgumentException(NAME_TAG + "#removeState: " +
                        state + " is a state which this machine has an output on " + this);
            }
            states.remove(state);
            return this;
        }

        public Builder inputPins(Set<Pin> inputPins) {
            Utils.verifyCollectionNullity(NAME_TAG + "#inputPins", "inputPins", inputPins);
            for (Pin pin : inputPins) {
                if (hasOutputPin(pin)) {
                    throw new IllegalArgumentException(NAME_TAG + "#inputPins: " +
                            inputPins + " contain an element already on this Builder " + this);
                }
            }
            for (Pin pin : this.inputPins) {
                allPinsValues.remove(getPinOfValue(pin, true));
                allPinsValues.remove(getPinOfValue(pin, false));
            }
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
            for (Pin pin : inputPins) {
                if (pin.getName().equals(name)) return pin;
            }
            return null;
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
            if (hasInputPin(inputPin)) {
                throw new IllegalArgumentException(NAME_TAG + "#addInputPin: " +
                        inputPin + " already on this builder " + this);
            }
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
         * @throws IllegalArgumentException if the pin passed is not on this machine
         * @see #hasInputPin
         */
        public Builder removeInputPin(Pin inputPin) {
            Utils.verifyNullity(NAME_TAG + "#removeInputPin", "inputPin", inputPin);
            if (!hasInputPin(inputPin)) {
                throw new IllegalArgumentException(NAME_TAG + "#removeInputPin: " +
                        inputPin + " was not on this builder " + this);
            }
            allPinsValues.remove(getPinOfValue(inputPin, true));
            allPinsValues.remove(getPinOfValue(inputPin, false));
            inputPins.remove(inputPin);
            return this;
        }

        public Builder outputPins(Set<Pin> outputPins) {
            Utils.verifyCollectionNullity(NAME_TAG + "#outputPins", "outputPins", outputPins);
            for (Pin pin : this.outputPins) {
                allPinsValues.remove(getPinOfValue(pin, true));
                allPinsValues.remove(getPinOfValue(pin, false));
            }
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
            for (Pin pin : outputPins) {
                if (pin.getName().equals(name)) return pin;
            }
            return null;
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
            if (hasOutputPin(outputPin)) {
                throw new IllegalArgumentException(NAME_TAG + "#addOutputPin: " +
                        outputPin + " already on the set!");
            }
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
         * @throws IllegalArgumentException if the pin passed is not on this machine
         * @see #hasOutputPin
         */
        public Builder removeOutputPin(Pin outputPin) {
            if (!hasOutputPin(outputPin)) {
                throw new IllegalArgumentException(NAME_TAG + "#removeOutputPin: " +
                        outputPin + "was not on the set!");
            }
            allPinsValues.remove(getPinOfValue(outputPin, true));
            allPinsValues.remove(getPinOfValue(outputPin, false));
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
        public BoolPin getPinOfValue(Pin pin, boolean high) {
            if (!hasInputPin(pin) && !hasOutputPin(pin)) {
                throw new IllegalArgumentException(NAME_TAG + "#getPinOfValue: " +
                        pin + " was on neither sets of this builder " + this);
            }
            for (BoolPin boolPin : allPinsValues) {
                if (boolPin.getPin().equals(pin) && boolPin.isHigh() == high) return boolPin;
            }
            return null;
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
                if (!hasState(output.getState())) {
                    throw new IllegalArgumentException(NAME_TAG + "#outputs: " +
                            "an element of outputs " + outputs +
                            " contained a state not on this builder " + this);
                }
                if (!allPinsValues.containsAll(output.getPins())) {
                    throw new IllegalArgumentException(NAME_TAG + "#outputs: " +
                            "an element of outputs " + outputs +
                            " contained at least a Pin not on this builder " + this);
                }
            }
            this.outputs = new HashSet<>(outputs);
            return this;
        }

        public boolean hasOutput(String state) {
            for (Output output : outputs) {
                if (output.getState().equals(state)) return true;
            }
            return false;
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
            if (hasOutput(state)) {
                throw new IllegalArgumentException(NAME_TAG + "#addOutput: " +
                        state + "was already on this builder " + this);
            }
            Utils.verifyNullity(NAME_TAG + "#addOutput", "state", state);
            Utils.verifyCollectionNullity(NAME_TAG + "#addOutput", "boolPins", boolPins);
            if (!hasState(state)) {
                throw new IllegalArgumentException(NAME_TAG + "#addOutput: " +
                        state + "was not on this builder " + this);
            }
            if (!allPinsValues.containsAll(boolPins)) {
                throw new IllegalArgumentException(NAME_TAG + "#addOutput: " +
                        boolPins + " contained at least a Pin not on this builder " + this);
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
            if (!hasOutput(state)) {
                throw new IllegalArgumentException(NAME_TAG + "#removeOutput: " +
                        state + "was not on this builder " + this);
            }
            for (Iterator<Output> iterator = outputs.iterator(); iterator.hasNext(); ) {
                Output output = iterator.next();
                if (output.getState().equals(state)) {
                    iterator.remove();
                    break;
                }
            }
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
                    ", outputs=" + outputs +
                    ", allPinsValues=" + allPinsValues +
                    '}';
        }
    }

    private MooreMachine(Builder builder) {
        this.name = builder.name;
        this.initialState = builder.initialState;
        this.states = builder.states;
        this.inputPins = builder.inputPins;
        this.outputPins = builder.outputPins;
        this.outputs = builder.outputs;
        this.allPinsValues = builder.allPinsValues;
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

    public Set<Output> getOutputs() {
        return new HashSet<>(outputs);
    }

    public Set<BoolPin> getAllPinsValues() {
        return new HashSet<>(allPinsValues);
    }
}
