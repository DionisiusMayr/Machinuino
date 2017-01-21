package Machinuino.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class OutputTest {

    @Test(expected = NullPointerException.class)
    public void createOutputNullStateShouldThrowException() {
        Output.ofValue(null, new HashSet<BoolPin>());
    }

    @Test(expected = NullPointerException.class)
    public void createOutputNullPinsShouldThrowException() {
        Output.ofValue("lmao", null);
    }

    @Test(expected = NullPointerException.class)
    public void createOutputNullPinsElementShouldThrowException() {
        Set<BoolPin> pins = new HashSet<>();
        pins.add(null);
        Output.ofValue("lmao", pins);
    }

    @Test
    public void createOutputRetainValue() {
        Set<BoolPin> pins = new HashSet<>();
        pins.add(BoolPin.ofValue(Pin.ofValue("haha", 0), true));
        Output output = Output.ofValue("lmao", pins);
        Assert.assertEquals("lmao", output.getState());
        Assert.assertEquals(pins, output.getPins());
    }

    @Test
    public void outputCheckImmutabily() {
        Set<BoolPin> pins = new HashSet<>();
        pins.add(BoolPin.ofValue(Pin.ofValue("haha", 0), true));
        Output output = Output.ofValue("lmao", pins);
        pins.add(BoolPin.ofValue(Pin.ofValue("hehe", 1), false));
        Assert.assertNotEquals(pins, output.getPins());

        Set<BoolPin> boolPins = output.getPins();
        boolPins.add(BoolPin.ofValue(Pin.ofValue("hehe", 1), false));
        Assert.assertNotEquals(boolPins, output.getPins());
    }

    @Test
    public void createEqualOutput() {
        Set<BoolPin> pins1 = new HashSet<>();
        pins1.add(BoolPin.ofValue(Pin.ofValue("lmao", 0),true));
        Set<BoolPin> pins2 = new HashSet<>();
        pins2.add(BoolPin.ofValue(Pin.ofValue("lmao", 0),true));
        Assert.assertEquals(Output.ofValue("haha", pins1), Output.ofValue("haha", pins2));
    }

    @Test
    public void createNotEqualOutput() {
        Set<BoolPin> pins1 = new HashSet<>();
        pins1.add(BoolPin.ofValue(Pin.ofValue("lmao", 322),true));
        Set<BoolPin> pins2 = new HashSet<>();
        pins2.add(BoolPin.ofValue(Pin.ofValue("haha", 1),false));
        Assert.assertNotEquals(Output.ofValue("haha", pins1), Output.ofValue("hehe", pins2));

        pins1.clear();
        pins1.add(BoolPin.ofValue(Pin.ofValue("lmao", 322),true));
        pins2.clear();
        pins2.add(BoolPin.ofValue(Pin.ofValue("lmao", 1),true));
        Assert.assertNotEquals(Output.ofValue("haha", pins1), Output.ofValue("hehe", pins2));

        pins1.clear();
        pins1.add(BoolPin.ofValue(Pin.ofValue("lmao", 322),true));
        pins2.clear();
        pins2.add(BoolPin.ofValue(Pin.ofValue("lmao", 322),true));
        Assert.assertNotEquals(Output.ofValue("haha", pins1), Output.ofValue("hehe", pins2));

        pins1.clear();
        pins1.add(BoolPin.ofValue(Pin.ofValue("lmao", 322),true));
        pins2.clear();
        pins2.add(BoolPin.ofValue(Pin.ofValue("haha", 1),false));
        Assert.assertNotEquals(Output.ofValue("haha", pins1), Output.ofValue("haha", pins2));
    }

    @Test
    public void equalOutputEqualHashcode() {
        Set<BoolPin> pins1 = new HashSet<>();
        pins1.add(BoolPin.ofValue(Pin.ofValue("lmao", 0),true));
        Set<BoolPin> pins2 = new HashSet<>();
        pins2.add(BoolPin.ofValue(Pin.ofValue("lmao", 0),true));
        Output output1 = Output.ofValue("haha", pins1);
        Output output2 = Output.ofValue("haha", pins2);
        if (output1.equals(output2)) Assert.assertEquals(output1.hashCode(), output2.hashCode());
    }

    @Test
    public void outputToString() {
        Set<BoolPin> pins = new HashSet<>();
        pins.add(BoolPin.ofValue(Pin.ofValue("lmao", 322), true));
        Assert.assertEquals("Output {state='haha', pins=[BoolPin {pin=Pin {name='lmao', number=" +
                322 + '}' + ", high=" + true + "}]}", Output.ofValue("haha", pins).toString());
    }
}
