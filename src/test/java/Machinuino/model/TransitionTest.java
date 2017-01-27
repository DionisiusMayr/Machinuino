package Machinuino.model;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class TransitionTest {

    @Test(expected = NullPointerException.class)
    public void previousStateNull() {

        String nextState = "q1";

        Pin pin = Pin.ofValue("a", 2);
        BoolPin boolPin = BoolPin.ofValue(pin, true);

        Set<BoolPin> input = new HashSet<>();
        input.add(boolPin);
        Transition transition = Transition.ofValue(null, nextState, input);

    }

    @Test(expected = NullPointerException.class)
    public void nextStateNull() {

        String previousState = "q0";

        Pin pin = Pin.ofValue("a", 2);
        BoolPin boolPin = BoolPin.ofValue(pin, true);

        Set<BoolPin> input = new HashSet<>();
        input.add(boolPin);
        Transition transition = Transition.ofValue(previousState, null, input);

    }

    @Test(expected = NullPointerException.class)
    public void inputNull() {

        String previousState = "q0";
        String nextState = "q1";
        Transition transition = Transition.ofValue(previousState, nextState, null);

    }

    @Test
    public void createTransitionRetainData() {

        String prevState = "q0", nextState = "q1";

        Pin pin = Pin.ofValue("a", 2);
        BoolPin boolPin = BoolPin.ofValue(pin, true);

        Set<BoolPin> input = new HashSet<>();
        input.add(boolPin);

        Transition transition = Transition.ofValue(prevState, nextState, input);
        Assert.assertEquals(prevState, transition.getPreviousState());
        Assert.assertEquals(nextState, transition.getNextState());
        Assert.assertEquals(input, transition.getInput());

    }

    @Test
    public void createEqualTransitions() {

        Set<BoolPin> input1 = new HashSet<>();
        input1.add(BoolPin.ofValue(Pin.ofValue("a", 2), true));

        Set<BoolPin> input2 = new HashSet<>();
        input2.add(BoolPin.ofValue(Pin.ofValue("a", 2), true));

        Assert.assertEquals(Transition.ofValue("q0", "q1", input1),
                Transition.ofValue("q0", "q1", input2));

    }

    @Test
    public void createNotEqualTransitions() {
        Set<BoolPin> input1 = new HashSet<>();
        input1.add(BoolPin.ofValue(Pin.ofValue("a", 2), true));

        Set<BoolPin> input2 = new HashSet<>();
        input2.add(BoolPin.ofValue(Pin.ofValue("b", 1), true));

        Assert.assertNotEquals(Transition.ofValue("q0", "q1", input1),
                Transition.ofValue("q0", "q1", input2));

        Assert.assertNotEquals(Transition.ofValue("q0", "q1", input1),
                Transition.ofValue("q1", "q0", input1));

        Assert.assertNotEquals(Transition.ofValue("q0", "q1", input1),
                Transition.ofValue("q0", "q0", input1));

        Assert.assertNotEquals(Transition.ofValue("q0", "q1", input1),
                Transition.ofValue("q1", "q1", input1));

    }

    @Test
    public void equalTransitionsEqualHashcode() {

        Set<BoolPin> input1 = new HashSet<>();
        input1.add(BoolPin.ofValue(Pin.ofValue("a", 2), true));

        Set<BoolPin> input2 = new HashSet<>();
        input2.add(BoolPin.ofValue(Pin.ofValue("a", 2), true));

        Transition t1 = Transition.ofValue("q0", "q1", input1);
        Transition t2 = Transition.ofValue("q0", "q1", input2);

        Assert.assertEquals(t1.hashCode(), t2.hashCode());

    }

    @Test
    public void pinToString() {
        String previousState = "q0", nextState = "q1";
        Set<BoolPin> input = new HashSet<>();
        input.add(BoolPin.ofValue(Pin.ofValue("a", 2), true));

        Assert.assertEquals("Transition {previous_state='" + previousState + "\'" +
                ", input='" + input.toString() + "\', next_state='" + nextState + "}",
                Transition.ofValue(previousState, nextState, input).toString());
    }

}
