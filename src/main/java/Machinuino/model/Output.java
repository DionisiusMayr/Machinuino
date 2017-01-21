package Machinuino.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Specifies the output of each state, namely it determines the value of the output pins, either
 * true or false
 * Immutable
 */

public class Output {

    private String state;
    private Set<BoolPin> pins;

    private Output(String state, Set<BoolPin> pins) {
        this.state = state;
        this.pins = new HashSet<>(pins);
    }

    /**
     * Creates an Output with attributes specified by the parameters
     *
     * @param state state which triggers this output, can not be null
     * @param pins the output itself, a collection of the pins and its values, can not be null
     *             neither any of it's elements can
     * @return Output with attributes specified by the parameters
     * @throws NullPointerException if any of the parameters passed are null
     */
    public static Output ofValue(String state, Set<BoolPin> pins) throws NullPointerException {
        if (state == null) throw new NullPointerException("Output#ofValue: state was null!");
        if (pins == null) throw new NullPointerException("Output#ofValue: pins was null!");
        for (BoolPin boolPin : pins) {
            if (boolPin == null) {
                throw new NullPointerException("Output#ofValue: an element of pins was null!");
            }
        }
        return new Output(state, pins);
    }

    public String getState() {
        return state;
    }

    public Set<BoolPin> getPins() {
        return new HashSet<>(pins);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != getClass()) return false;

        Output output = (Output) o;
        return state.equals(output.state) && pins.equals(output.pins);
    }

    @Override
    public int hashCode() {
        int result = state.hashCode();
        return 31 * result + pins.hashCode();
    }

    @Override
    public String toString() {
        return "Output {" +
                "state='" + state + '\'' +
                ", pins=" + pins +
                '}';
    }
}
