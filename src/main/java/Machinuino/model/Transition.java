package Machinuino.model;

import java.util.HashSet;
import java.util.Set;

public class Transition {

    private String previousState, nextState;
    private Set<BoolPin> input;

    private Transition(String previousState, String nextState, Set<BoolPin> input) {
        this.previousState = previousState;
        this.nextState = nextState;
        this.input = new HashSet<>(input);
    }

    /**
     * Creates a transition with attributes specified by the parameters
     * @param previousState current state before the transition
     * @param nextState current state after the transition
     * @param input values of the pins that cause the transition
     * @return Transition with attributes specified by the parameters
     */
    public static Transition ofValue(String previousState, String nextState, Set<BoolPin> input)
            throws NullPointerException {

        if(previousState == null) throw new NullPointerException("Transition#ofValue: " +
                "previousState was null!");
        if(nextState == null) throw new NullPointerException("Transition#ofValue: " +
                "nextState was null!");
        if(input == null) throw new NullPointerException("Transition#ofValue: " +
                "input was null!");


        return new Transition(previousState, nextState, input);
    }

    public String getPreviousState() {
        return previousState;
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
        return "Transition {" +
                "previous_state='" + previousState + "\'" +
                ", input='" + input.toString() + "\'" +
                ", next_state='" + nextState +
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
