package Machinuino.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class MooreMachineTest {

    private MooreMachine.Builder defaultBuilder;

    @Before
    public void setupBuilder() {
        defaultBuilder = new MooreMachine.Builder("M1");
        defaultBuilder.addState("q0");
        defaultBuilder.addState("q1");
        defaultBuilder.initialState("q0");
        defaultBuilder.addInputPin(Pin.ofValue("clock", 1));
        defaultBuilder.addInputPin(Pin.ofValue("button", 2));
        defaultBuilder.addOutputPin(Pin.ofValue("led", 3));
        defaultBuilder.addOutputPin(Pin.ofValue("lmao", 4));
        Set<BoolPin> set = new HashSet<>();
        set.add(defaultBuilder.getPinOfValue(defaultBuilder.getOutputPinOfName("led"), true));
        set.add(defaultBuilder.getPinOfValue(defaultBuilder.getOutputPinOfName("lmao"), false));
        defaultBuilder.addOutput("q1", set);
        Set<BoolPin> set1 = new HashSet<>();
        set1.add(defaultBuilder.getPinOfValue(defaultBuilder.getOutputPinOfName("led"), false));
        set1.add(defaultBuilder.getPinOfValue(defaultBuilder.getOutputPinOfName("lmao"), true));
        defaultBuilder.addOutput("q0", set1);
    }

    @Test(expected = NullPointerException.class)
    public void createBuilderNullNameShouldThrowException() {
        String lmao = null;
        MooreMachine.Builder builder = new MooreMachine.Builder(lmao);
    }

    @Test(expected = NullPointerException.class)
    public void builderAddNullInitialStateShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.initialState(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderSetInitialStateNotOnBuilderShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.initialState("haha");
    }

    @Test
    public void builderSetInitialState() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addState("haha");
        builder.initialState("haha");
        MooreMachine mooreMachine = builder.build();
        Assert.assertEquals("haha", mooreMachine.getInitialState());
    }

    @Test(expected = NullPointerException.class)
    public void builderSetNullStatesShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.states(null);
    }

    @Test(expected = NullPointerException.class)
    public void builderSetStatesWithNullStateShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        Set<String> strings = new HashSet<>();
        strings.add(null);
        builder.states(strings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderSetStatesNotOnOutputShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        Set<String> strings = new HashSet<>();
        strings.add("q0");
        builder.states(strings);
        builder.addOutputPin(Pin.ofValue("haha", 1));
        Set<BoolPin> boolPins = new HashSet<>();
        boolPins.add(builder.getPinOfValue(builder.getOutputPinOfName("haha"), true));
        builder.addOutput("q0", boolPins);
        builder.states(new HashSet<String>());
    }

    @Test
    public void builderSetStates() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        Set<String> strings = new HashSet<>();
        strings.add("q0");
        strings.add("q1");
        builder.states(strings);
        builder.addOutputPin(Pin.ofValue("led", 3));
        Set<BoolPin> set = new HashSet<>();
        set.add(builder.getPinOfValue(builder.getOutputPinOfName("led"), true));
        builder.addOutput("q1", set);
        builder.states(strings);
        MooreMachine machine = builder.build();
        Assert.assertEquals(strings, machine.getStates());
    }

    @Test(expected = NullPointerException.class)
    public void builderAddNullStateShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addState(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderAddStateAlreadyOnTheBuilderShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addState("haha");
        builder.addState("haha");
    }

    @Test
    public void builderAddState() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addState("q0");
        MooreMachine machine = builder.build();
        Assert.assertTrue(machine.getStates().contains("q0"));
    }

    @Test(expected = NullPointerException.class)
    public void builderRemoveNullStateShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.removeState(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderRemoveStateNotOnTheBuilderShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.removeState("haha");
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderRemoveStateWhichBuilderHasOutputOnShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addState("q0");
        builder.addOutputPin(Pin.ofValue("haha", 1));
        Set<BoolPin> boolPins = new HashSet<>();
        boolPins.add(builder.getPinOfValue(builder.getOutputPinOfName("haha"), true));
        builder.addOutput("q0", boolPins);
        builder.removeState("q0");
    }

    @Test
    public void builderRemoveState() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addState("q0");
        builder.removeState("q0");
        MooreMachine machine = builder.build();
        Assert.assertFalse(machine.getStates().contains("q0"));
    }

    @Test(expected = NullPointerException.class)
    public void builderSetNullInputPinsShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.inputPins(null);
    }

    @Test(expected = NullPointerException.class)
    public void builderSetInputPinsWithNullInputPinShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        Set<Pin> pins = new HashSet<>();
        pins.add(null);
        builder.inputPins(pins);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderSetInputPinsAlreadyOnOutputPinsShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        Set<Pin> pins = new HashSet<>();
        pins.add(Pin.ofValue("haha", 1));
        builder.outputPins(pins);
        builder.inputPins(pins);
    }

    @Test
    public void builderSetInputPins() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        Set<Pin> pins = new HashSet<>();
        pins.add(Pin.ofValue("haha", 1));
        builder.inputPins(pins);
        MooreMachine machine = builder.build();
        Assert.assertEquals(pins, machine.getInputPins());
        Set<BoolPin> boolPins = new HashSet<>();
        boolPins.add(builder.getPinOfValue(builder.getInputPinOfName("haha"), true));
        boolPins.add(builder.getPinOfValue(builder.getInputPinOfName("haha"), false));
        Assert.assertTrue(machine.getAllPinsValues().containsAll(boolPins));
    }

    @Test
    public void builderSetInputPinsWithInputPinsAlraedyOnTheBuilder() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addInputPin(Pin.ofValue("rofl", 1));
        Set<BoolPin> boolPins = new HashSet<>();
        boolPins.add(builder.getPinOfValue(builder.getInputPinOfName("rofl"), true));
        boolPins.add(builder.getPinOfValue(builder.getInputPinOfName("rofl"), false));
        Set<Pin> pins = new HashSet<>();
        pins.add(Pin.ofValue("haha", 1));
        builder.inputPins(pins);
        MooreMachine machine = builder.build();
        Assert.assertFalse(machine.getInputPins().contains(Pin.ofValue("rofl", 1)));
        Assert.assertFalse(machine.getAllPinsValues().containsAll(boolPins));
    }

    @Test
    public void builderGetInputPinOfNameNotOnTheBuilder() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addInputPin(Pin.ofValue("rofl", 1));
        Assert.assertNull(builder.getInputPinOfName("haha"));
    }

    @Test
    public void builderGetInputPinOfName() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        Set<Pin> pins = new HashSet<>();
        pins.add(Pin.ofValue("haha", 1));
        builder.inputPins(pins);
        Assert.assertEquals(Pin.ofValue("haha", 1), builder.getInputPinOfName("haha"));
    }

    @Test(expected = NullPointerException.class)
    public void builderAddNullInputPinShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addInputPin(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderAddInputPinAlreadyOnTheBuilderShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addInputPin(Pin.ofValue("haha", 1));
        builder.addInputPin(Pin.ofValue("haha", 1));
    }

    @Test
    public void builderAddInputPin() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addInputPin(Pin.ofValue("haha", 1));
        MooreMachine machine = builder.build();
        Assert.assertTrue(machine.getInputPins().contains(Pin.ofValue("haha", 1)));
        Set<BoolPin> boolPins = new HashSet<>();
        boolPins.add(builder.getPinOfValue(builder.getInputPinOfName("haha"), true));
        boolPins.add(builder.getPinOfValue(builder.getInputPinOfName("haha"), false));
        Assert.assertTrue(machine.getAllPinsValues().containsAll(boolPins));
    }

    @Test(expected = NullPointerException.class)
    public void builderRemoveNullInputPinShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.removeInputPin(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderRemoveInputPinNotOnTheBuilderShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.removeInputPin(Pin.ofValue("haha", 1));
    }

    @Test
    public void builderRemoveInputPin() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addInputPin(Pin.ofValue("haha", 1));
        Set<BoolPin> boolPins = new HashSet<>();
        boolPins.add(builder.getPinOfValue(builder.getInputPinOfName("haha"), true));
        boolPins.add(builder.getPinOfValue(builder.getInputPinOfName("haha"), false));
        builder.removeInputPin(Pin.ofValue("haha", 1));
        MooreMachine machine = builder.build();
        Assert.assertFalse(machine.getInputPins().contains(Pin.ofValue("haha", 1)));
        Assert.assertFalse(machine.getAllPinsValues().containsAll(boolPins));
    }
}
