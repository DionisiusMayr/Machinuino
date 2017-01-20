package Machinuino.model;

import org.junit.Assert;
import org.junit.Test;

public class PinTest {

    @Test(expected = NullPointerException.class)
    public void createPinNullNameShouldThrowException() {
        Pin pin = Pin.ofValue(null, 0);
    }

    @Test
    public void createPinRetainData() {
        String name = "huhu";
        int number = 57;
        Pin pin = Pin.ofValue(name, number);
        Assert.assertEquals(name, pin.getName());
        Assert.assertEquals(number, pin.getNumber());
    }

    @Test
    public void createEqualPins() {
        Assert.assertEquals(Pin.ofValue("haha", 5), Pin.ofValue("haha", 5));
    }

    @Test
    public void createNotEqualPins() {
        Assert.assertNotEquals(Pin.ofValue("lmao", 234), Pin.ofValue("rofl", 322));
    }

    @Test
    public void equalPinsEqualHashcode() {
        Pin pin1 = Pin.ofValue("hehe", 9);
        Pin pin2 = Pin.ofValue("hehe", 9);
        if (pin1.equals(pin2)) Assert.assertEquals(pin1.hashCode(), pin2.hashCode());
    }

    @Test
    public void pinToString() {
        String name = "hoho";
        int number = 4;
        Assert.assertEquals("Pin {name='" + name + "\', number=" + number + "}",
                Pin.ofValue(name, number).toString());
    }
}
