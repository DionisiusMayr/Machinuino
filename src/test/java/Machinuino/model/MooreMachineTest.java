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
        set.add(defaultBuilder.getBoolPinOfValue(defaultBuilder.getOutputPinOfName("led"), true));
        set.add(defaultBuilder.getBoolPinOfValue(defaultBuilder.getOutputPinOfName("lmao"), false));
        defaultBuilder.addOutput("q1", set);
        Set<BoolPin> set1 = new HashSet<>();
        set1.add(defaultBuilder.getBoolPinOfValue(defaultBuilder.getOutputPinOfName("led"), false));
        set1.add(defaultBuilder.getBoolPinOfValue(defaultBuilder.getOutputPinOfName("lmao"), true));
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
        boolPins.add(builder.getBoolPinOfValue(builder.getOutputPinOfName("haha"), true));
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
        set.add(builder.getBoolPinOfValue(builder.getOutputPinOfName("led"), true));
        builder.addOutput("q1", set);
        builder.states(strings);
        MooreMachine machine = builder.build();
        Assert.assertEquals(strings, machine.getStates());
    }

    @Test
    public void builderHasStateNotOnBuilder() {
        MooreMachine.Builder builder = new MooreMachine.Builder(defaultBuilder);
        Assert.assertFalse(builder.hasState("limao"));
    }

    @Test
    public void builderHasStateOnBuilder() {
        MooreMachine.Builder builder = new MooreMachine.Builder(defaultBuilder);
        Assert.assertTrue(builder.hasState("q0"));
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
        boolPins.add(builder.getBoolPinOfValue(builder.getOutputPinOfName("haha"), true));
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
        boolPins.add(builder.getBoolPinOfValue(builder.getInputPinOfName("haha"), true));
        boolPins.add(builder.getBoolPinOfValue(builder.getInputPinOfName("haha"), false));
        Assert.assertTrue(machine.getAllPinsValues().containsAll(boolPins));
    }

    @Test
    public void builderSetInputPinsWithInputPinsAlreadyOnTheBuilder() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addInputPin(Pin.ofValue("rofl", 1));
        Set<BoolPin> boolPins = new HashSet<>();
        boolPins.add(builder.getBoolPinOfValue(builder.getInputPinOfName("rofl"), true));
        boolPins.add(builder.getBoolPinOfValue(builder.getInputPinOfName("rofl"), false));
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

    @Test
    public void builderHasInputPinNotOnTheBuilder() {
        MooreMachine.Builder builder = new MooreMachine.Builder(defaultBuilder);
        Assert.assertFalse(builder.hasInputPin(builder.getInputPinOfName("limao")));
    }

    @Test
    public void builderHasInputPinOnTheBuilder() {
        MooreMachine.Builder builder = new MooreMachine.Builder(defaultBuilder);
        Assert.assertTrue(builder.hasInputPin(builder.getInputPinOfName("clock")));
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

    @Test(expected = IllegalArgumentException.class)
    public void builderAddInputPinAlreadyOnOutputPinsShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addOutputPin(Pin.ofValue("haha", 1));
        builder.addInputPin(Pin.ofValue("haha", 1));
    }

    @Test
    public void builderAddInputPin() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addInputPin(Pin.ofValue("haha", 1));
        MooreMachine machine = builder.build();
        Assert.assertTrue(machine.getInputPins().contains(Pin.ofValue("haha", 1)));
        Set<BoolPin> boolPins = new HashSet<>();
        boolPins.add(builder.getBoolPinOfValue(builder.getInputPinOfName("haha"), true));
        boolPins.add(builder.getBoolPinOfValue(builder.getInputPinOfName("haha"), false));
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
        boolPins.add(builder.getBoolPinOfValue(builder.getInputPinOfName("haha"), true));
        boolPins.add(builder.getBoolPinOfValue(builder.getInputPinOfName("haha"), false));
        builder.removeInputPin(Pin.ofValue("haha", 1));
        MooreMachine machine = builder.build();
        Assert.assertFalse(machine.getInputPins().contains(Pin.ofValue("haha", 1)));
        Assert.assertFalse(machine.getAllPinsValues().containsAll(boolPins));
    }

    @Test(expected = NullPointerException.class)
    public void builderSetNullOutputPinsShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.outputPins(null);
    }

    @Test(expected = NullPointerException.class)
    public void builderSetOutputPinsWithNullInputPinShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        Set<Pin> pins = new HashSet<>();
        pins.add(null);
        builder.outputPins(pins);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderSetOutputPinsAlreadyOnInputPinsShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        Set<Pin> pins = new HashSet<>();
        pins.add(Pin.ofValue("haha", 1));
        builder.inputPins(pins);
        builder.outputPins(pins);
    }

    @Test
    public void builderSetOutputPins() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        Set<Pin> pins = new HashSet<>();
        pins.add(Pin.ofValue("haha", 1));
        builder.outputPins(pins);
        MooreMachine machine = builder.build();
        Assert.assertEquals(pins, machine.getOutputPins());
        Set<BoolPin> boolPins = new HashSet<>();
        boolPins.add(builder.getBoolPinOfValue(builder.getOutputPinOfName("haha"), true));
        boolPins.add(builder.getBoolPinOfValue(builder.getOutputPinOfName("haha"), false));
        Assert.assertTrue(machine.getAllPinsValues().containsAll(boolPins));
    }

    @Test
    public void builderSetOutputPinsWithOutputPinsAlreadyOnTheBuilder() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addOutputPin(Pin.ofValue("rofl", 1));
        Set<BoolPin> boolPins = new HashSet<>();
        boolPins.add(builder.getBoolPinOfValue(builder.getOutputPinOfName("rofl"), true));
        boolPins.add(builder.getBoolPinOfValue(builder.getOutputPinOfName("rofl"), false));
        Set<Pin> pins = new HashSet<>();
        pins.add(Pin.ofValue("haha", 1));
        builder.outputPins(pins);
        MooreMachine machine = builder.build();
        Assert.assertFalse(machine.getOutputPins().contains(Pin.ofValue("rofl", 1)));
        Assert.assertFalse(machine.getAllPinsValues().containsAll(boolPins));
    }

    @Test
    public void builderGetOutputPinOfNameNotOnTheBuilder() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addOutputPin(Pin.ofValue("rofl", 1));
        Assert.assertNull(builder.getOutputPinOfName("haha"));
    }

    @Test
    public void builderGetOutputPinOfName() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        Set<Pin> pins = new HashSet<>();
        pins.add(Pin.ofValue("haha", 1));
        builder.outputPins(pins);
        Assert.assertEquals(Pin.ofValue("haha", 1), builder.getOutputPinOfName("haha"));
    }

    @Test
    public void builderHasOutputPinNotOnTheBuilder() {
        MooreMachine.Builder builder = new MooreMachine.Builder(defaultBuilder);
        Assert.assertFalse(builder.hasOutputPin(builder.getOutputPinOfName("limao")));
    }

    @Test
    public void builderHasOutputPinOnTheBuilder() {
        MooreMachine.Builder builder = new MooreMachine.Builder(defaultBuilder);
        Assert.assertTrue(builder.hasOutputPin(builder.getOutputPinOfName("lmao")));
    }

    @Test(expected = NullPointerException.class)
    public void builderAddNullOutputPinShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addOutputPin(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderAddOutputPinAlreadyOnTheBuilderShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addOutputPin(Pin.ofValue("haha", 1));
        builder.addOutputPin(Pin.ofValue("haha", 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderAddOutputPinAlreadyOnInputPinsShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addInputPin(Pin.ofValue("haha", 1));
        builder.addOutputPin(Pin.ofValue("haha", 1));
    }

    @Test
    public void builderAddOutputPin() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addOutputPin(Pin.ofValue("haha", 1));
        MooreMachine machine = builder.build();
        Assert.assertTrue(machine.getOutputPins().contains(Pin.ofValue("haha", 1)));
        Set<BoolPin> boolPins = new HashSet<>();
        boolPins.add(builder.getBoolPinOfValue(builder.getOutputPinOfName("haha"), true));
        boolPins.add(builder.getBoolPinOfValue(builder.getOutputPinOfName("haha"), false));
        Assert.assertTrue(machine.getAllPinsValues().containsAll(boolPins));
    }

    @Test(expected = NullPointerException.class)
    public void builderRemoveNullOutputPinShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.removeOutputPin(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderRemoveOutputPinNotOnTheBuilderShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.removeOutputPin(Pin.ofValue("haha", 1));
    }

    @Test
    public void builderRemoveOutputPin() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addOutputPin(Pin.ofValue("haha", 1));
        Set<BoolPin> boolPins = new HashSet<>();
        boolPins.add(builder.getBoolPinOfValue(builder.getOutputPinOfName("haha"), true));
        boolPins.add(builder.getBoolPinOfValue(builder.getOutputPinOfName("haha"), false));
        builder.removeOutputPin(Pin.ofValue("haha", 1));
        MooreMachine machine = builder.build();
        Assert.assertFalse(machine.getOutputPins().contains(Pin.ofValue("haha", 1)));
        Assert.assertFalse(machine.getAllPinsValues().containsAll(boolPins));
    }

    @Test(expected = NullPointerException.class)
    public void builderGetNullBoolPinOfValueShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addInputPin(Pin.ofValue("haha", 1));
        BoolPin boolPin = builder.getBoolPinOfValue(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderGetBoolPinOfValueNotOnTheBuilderShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addInputPin(Pin.ofValue("haha", 1));
        BoolPin boolPin = builder.getBoolPinOfValue(Pin.ofValue("rofl", 1), true);
    }

    @Test
    public void builderGetBoolPinOfValue() {
        MooreMachine.Builder builder = new MooreMachine.Builder("lmao");
        builder.addInputPin(Pin.ofValue("haha", 1));
        Assert.assertEquals(builder.getBoolPinOfValue(Pin.ofValue("haha", 1), true),
                BoolPin.ofValue(Pin.ofValue("haha", 1), true));
        Assert.assertEquals(builder.getBoolPinOfValue(Pin.ofValue("haha", 1), false),
                BoolPin.ofValue(Pin.ofValue("haha", 1), false));
    }

    /* Output tests */
    @Test(expected = NullPointerException.class)
    public void builderSetNullOutputShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");
        builder.outputs(null);
    }

    @Test(expected = NullPointerException.class)
    public void builderSetOutputsWithNullOutputShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");
        Set<Output> outputs = new HashSet<>();
        outputs.add(null);
        builder.outputs(outputs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderSetOutputsWithStateNotOnBuilderShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");
        Set<Output> outputs = new HashSet<>();
        Set<BoolPin> boolPins = new HashSet<>();

        Pin pin = Pin.ofValue("pin", 2);
        builder.addOutputPin(pin);
        boolPins.add(BoolPin.ofValue(pin, true));

        outputs.add(Output.ofValue("q0", boolPins));
        builder.outputs(outputs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderSetOutputsWithPinNotOnBuilderShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");
        Set<Output> outputs = new HashSet<>();
        Set<BoolPin> boolPins = new HashSet<>();

        builder.addState("q0");
        Pin pin = Pin.ofValue("pin", 2);
        boolPins.add(BoolPin.ofValue(pin, true));

        outputs.add(Output.ofValue("q0", boolPins));
        builder.outputs(outputs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderSetOutputsWithInputPinShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");
        Set<Output> outputs = new HashSet<>();
        Set<BoolPin> boolPins = new HashSet<>();

        builder.addState("q0");
        Pin pin = Pin.ofValue("pin", 2);
        builder.addInputPin(pin);
        boolPins.add(BoolPin.ofValue(pin, true));

        outputs.add(Output.ofValue("q0", boolPins));
        builder.outputs(outputs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderSetOutputsWithPinWithTwoValuesShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");
        Set<Output> outputs = new HashSet<>();
        Set<BoolPin> boolPins = new HashSet<>();

        builder.addState("q0");
        Pin pin = Pin.ofValue("pin", 2);
        builder.addOutputPin(pin);
        boolPins.add(BoolPin.ofValue(pin, true));
        boolPins.add(BoolPin.ofValue(pin, false));

        outputs.add(Output.ofValue("q0", boolPins));
        builder.outputs(outputs);
    }

    @Test
    public void builderSetOutputs() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");
        Set<Output> outputs = new HashSet<>();
        Set<BoolPin> boolPins = new HashSet<>();

        builder.addState("q0");
        Pin pin = Pin.ofValue("pin", 2);
        builder.addOutputPin(pin);
        boolPins.add(BoolPin.ofValue(pin, true));

        outputs.add(Output.ofValue("q0", boolPins));
        builder.outputs(outputs);

        MooreMachine mooreMachine = builder.build();
        Assert.assertEquals(outputs, mooreMachine.getOutputs());
    }

    @Test
    public void builderHasOutputNotOnTheBuilder() {
        MooreMachine.Builder builder = new MooreMachine.Builder(defaultBuilder);
        Assert.assertFalse(builder.hasOutput("limao"));
    }

    @Test
    public void builderHasOutputOnTheBuilder() {
        MooreMachine.Builder builder = new MooreMachine.Builder(defaultBuilder);
        Assert.assertTrue(builder.hasOutput("q0"));
    }

    @Test(expected = NullPointerException.class)
    public void builderAddOutputWithNullStateShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");
        Set<BoolPin> boolPins = new HashSet<>();
        Pin pin = Pin.ofValue("pin", 2);
        builder.addOutputPin(pin);
        boolPins.add(BoolPin.ofValue(pin, true));

        builder.addOutput(null, boolPins);
    }

    @Test(expected = NullPointerException.class)
    public void builderAddOutputWithNullBoolPinsShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");
        builder.addState("q0");

        builder.addOutput("q0", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderAddOutputWithStateNotOnBuilderShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");
        Set<BoolPin> boolPins = new HashSet<>();

        Pin pin = Pin.ofValue("pin", 2);
        builder.addOutputPin(pin);
        boolPins.add(BoolPin.ofValue(pin, true));

        builder.addOutput("q0", boolPins);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderAddOutputWithPinNotOnBuilderShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");
        Set<BoolPin> boolPins = new HashSet<>();

        builder.addState("q0");
        Pin pin = Pin.ofValue("pin", 2);
        boolPins.add(BoolPin.ofValue(pin, true));

        builder.addOutput("q0", boolPins);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderAddOutputWithInputPinShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");
        Set<BoolPin> boolPins = new HashSet<>();

        builder.addState("q0");
        Pin pin = Pin.ofValue("pin", 2);
        builder.addInputPin(pin);
        boolPins.add(BoolPin.ofValue(pin, true));

        builder.addOutput("q0", boolPins);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderAddOutputWithPinWithTwoValuesShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");
        Set<BoolPin> boolPins = new HashSet<>();

        builder.addState("q0");
        Pin pin = Pin.ofValue("pin", 2);
        builder.addOutputPin(pin);
        boolPins.add(BoolPin.ofValue(pin, true));
        boolPins.add(BoolPin.ofValue(pin, false));

        builder.addOutput("q0", boolPins);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderAddOutputWithStateWithAlreadyAnOutputShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");
        Set<BoolPin> boolPins = new HashSet<>();

        builder.addState("q0");
        Pin pin = Pin.ofValue("pin", 2);
        builder.addOutputPin(pin);
        boolPins.add(BoolPin.ofValue(pin, true));

        builder.addOutput("q0", boolPins);
        builder.addOutput("q0", boolPins);
    }

    @Test
    public void builderAddOutput() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");
        Set<BoolPin> boolPins = new HashSet<>();
        Set<Output> outputs = new HashSet<>();

        builder.addState("q0");
        Pin pin = Pin.ofValue("pin", 2);
        builder.addOutputPin(pin);

        boolPins.add(BoolPin.ofValue(pin, true));
        builder.addOutput("q0", boolPins);
        outputs.add(Output.ofValue("q0", boolPins));

        MooreMachine mooreMachine = builder.build();
        Assert.assertEquals(outputs, mooreMachine.getOutputs());
    }

    @Test
    public void builderAddOutputWithAlreadyOutputsOnBuilder() {
        MooreMachine.Builder builder = new MooreMachine.Builder(defaultBuilder);
        Pin pin = Pin.ofValue("pin", 5);
    }

    @Test(expected = NullPointerException.class)
    public void builderRemoveOutputWithNullStateShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");

        builder.removeOutput(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderRemoveOutputWithStateNotOnBuilderShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");

        builder.removeOutput("q0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderRemoveOutputWithStateWithoutOutputShouldThrowException() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");
        builder.addState("q0");

        builder.removeOutput("q0");
    }

    @Test
    public void builderRemoveOutput() {
        MooreMachine.Builder builder = new MooreMachine.Builder("m1");
        Set<BoolPin> boolPins = new HashSet<>();
        Set<Output> outputs = new HashSet<>();

        builder.addState("q0");
        builder.addState("q1");
        Pin pin = Pin.ofValue("pin", 2);
        builder.addOutputPin(pin);

        boolPins.add(BoolPin.ofValue(pin, true));
        builder.addOutput("q0", boolPins);
        builder.addOutput("q1", boolPins);
        outputs.add(Output.ofValue("q0", boolPins));
        builder.removeOutput("q1");

        MooreMachine mooreMachine = builder.build();
        Assert.assertEquals(outputs, mooreMachine.getOutputs());
    }

    @Test
    public void builderReturnFromGetEqualsInitialValues() {
        MooreMachine.Builder builder = new MooreMachine.Builder("M1");

        Set<String> states = new HashSet<>();
        states.add("q0");
        states.add("q1");

        Set<Pin> inputPins = new HashSet<>();
        inputPins.add(Pin.ofValue("input", 1));

        Set<Pin> outputPins = new HashSet<>();
        outputPins.add(Pin.ofValue("output", 2));

        Set<BoolPin> boolPins = new HashSet<>();
        boolPins.add(BoolPin.ofValue(Pin.ofValue("output", 2), true));

        Set<Output> outputs = new HashSet<>();
        outputs.add(Output.ofValue("q0", boolPins));

        builder.states(states);
        builder.initialState("q0");
        builder.inputPins(inputPins);
        builder.outputPins(outputPins);
        builder.outputs(outputs);

        MooreMachine mooreMachine = builder.build();

        Assert.assertEquals(mooreMachine.getName(), "M1");
        Assert.assertEquals(mooreMachine.getInitialState(), "q0");
        Assert.assertEquals(mooreMachine.getStates(), states);
        Assert.assertEquals(mooreMachine.getInputPins(), inputPins);
        Assert.assertEquals(mooreMachine.getOutputPins(), outputPins);
        Assert.assertEquals(mooreMachine.getOutputs(), outputs);
    }

    @Test
    public void builderToString() {
        MooreMachine.Builder builder = new MooreMachine.Builder("M1");

        Set<String> states = new HashSet<>();
        states.add("q0");
        states.add("q1");

        Set<Pin> inputPins = new HashSet<>();
        inputPins.add(Pin.ofValue("input", 1));

        Set<Pin> outputPins = new HashSet<>();
        outputPins.add(Pin.ofValue("output", 2));

        Set<BoolPin> boolPins = new HashSet<>();
        boolPins.add(BoolPin.ofValue(Pin.ofValue("output", 2), true));

        Set<Output> outputs = new HashSet<>();
        outputs.add(Output.ofValue("q0", boolPins));

        builder.states(states);
        builder.initialState("q0");
        builder.inputPins(inputPins);
        builder.outputPins(outputPins);
        builder.outputs(outputs);

        Set<BoolPin> boolPins1 = new HashSet<>();
        boolPins1.add(BoolPin.ofValue(Pin.ofValue("input", 1), false));
        boolPins1.add(BoolPin.ofValue(Pin.ofValue("input", 1), true));
        boolPins1.add(BoolPin.ofValue(Pin.ofValue("output", 2), false));
        boolPins1.add(BoolPin.ofValue(Pin.ofValue("output", 2), true));

        Assert.assertEquals(builder.toString(), "Builder{" +
                "name='" + "M1" + "\', initialState='" + "q0" +
                "\', states=" + states + ", inputPins=" + inputPins + ", outputPins=" + outputPins +
                ", outputs=" + outputs + ", allPinsValues=" + boolPins1 + '}');
    }

    @Test
    public void mooreMachineNotEqualMooreMachine() {
        MooreMachine.Builder builder = new MooreMachine.Builder(defaultBuilder);
        MooreMachine machine1 = builder.build();
        builder.addInputPin(Pin.ofValue("rofl", 5));
        MooreMachine machine2 = builder.build();
        Assert.assertNotEquals(machine1, machine2);
    }

    @Test
    public void mooreMachineEqualMooreMachine() {
        MooreMachine.Builder builder = new MooreMachine.Builder(defaultBuilder);
        MooreMachine machine1 = builder.build();
        MooreMachine machine2 = builder.build();
        Assert.assertEquals(machine1, machine2);
    }

    @Test
    public void mooreMachineEqualMooreMachineEqualHashcode() {
        MooreMachine.Builder builder = new MooreMachine.Builder(defaultBuilder);
        MooreMachine machine1 = builder.build();
        MooreMachine machine2 = builder.build();
        Assert.assertEquals(machine1.hashCode(), machine2.hashCode());
    }

    @Test
    public void mooreMachineToString() {
        MooreMachine machine = new MooreMachine.Builder(defaultBuilder).build();
        Set<String> states = machine.getStates();
        Set<Pin> inputPins = machine.getInputPins();
        Set<Pin> outputPins = machine.getOutputPins();
        Set<Output> outputs = machine.getOutputs();
        Assert.assertEquals(machine.toString(), "MooreMachine{" +
                "name='" + machine.getName() + "\', initialState='" + machine.getInitialState() +
                "\', states=" + states + ", inputPins=" + inputPins + ", outputPins=" + outputPins +
                ", outputs=" + outputs + '}');
    }
}
