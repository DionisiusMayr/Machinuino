package Machinuino.model;

import java.util.*;

public class Transition {

    private String previousState, nextState;
    private Set<BoolPin> input;

    private Transition(String previousState, String nextState, Set<BoolPin> input) {
        setPreviousState(previousState);
        setNextState(nextState);
        this.input = new HashSet<BoolPin>(input);
    }

    public static Transition ofValue(String previousState, String nextState, Set<BoolPin> input) {
        return new Transition(previousState, nextState, input);
    }

    private void setPreviousState(String previousState) {
        this.previousState = previousState;
    }

    public String getPreviousState() {
        return previousState;
    }

    private void setNextState(String nextState) {
        this.nextState = nextState;
    }

    public String getNextState() {
        return nextState;
    }

    public Set<BoolPin> getInput() {
        return new HashSet<>(input);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;

        Transition transition = (Transition) obj;
        return previousState.equals(transition.getPreviousState()) &&
                nextState.equals(transition.getNextState()) && input.equals(transition.getInput());

    }

    @Override
    public String toString() {
        return "Transition {" + System.lineSeparator() +
                "previous_state = " + previousState + System.lineSeparator() +
                "input = " + input.toString() + System.lineSeparator() +
                "next_state = " + nextState + System.lineSeparator() +
                "}";
    }

    @Override
    public int hashCode() {
        int hashPrev = previousState.hashCode();
        int hashNext = nextState.hashCode();
        int hashInput = input.hashCode();
        return 47 * (hashInput + hashNext + hashPrev);
    }
}
