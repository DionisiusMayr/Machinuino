package Machinuino.model;

import org.junit.Assert;
import org.junit.Test;

public class BoolPinTest {

    @Test(expected = NullPointerException.class)
    public void createBoolPinNullPinShouldThrowException() {
        BoolPin boolPin = BoolPin.ofValue(null, true);
    }

    @Test
    public void createBoolPinRetainValue() {
        Pin pin = Pin.ofValue("lmao", 322);
        BoolPin boolPin = BoolPin.ofValue(pin, true);
        Assert.assertEquals(pin, boolPin.getPin());
        Assert.assertTrue(boolPin.isHigh());
    }

    @Test
    public void createEqualBoolPins() {
        Assert.assertEquals(BoolPin.ofValue(Pin.ofValue("lmao", 322), false),
                BoolPin.ofValue(Pin.ofValue("lmao", 322), false));
    }

    @Test
    public void createNotEqualBoolPins() {
        Assert.assertNotEquals(BoolPin.ofValue(Pin.ofValue("lmao", 322), false),
                BoolPin.ofValue(Pin.ofValue("haha", 0), true));
    }

    @Test
    public void equalBoolPinsEqualHashcode() {
        BoolPin boolPin1 = BoolPin.ofValue(Pin.ofValue("lmao", 322), true);
        BoolPin boolPin2 = BoolPin.ofValue(Pin.ofValue("lmao", 322), true);
        Assert.assertEquals(boolPin1.hashCode(), boolPin2.hashCode());
    }

    @Test
    public void boolPinToString() {
        Pin pin = Pin.ofValue("lmao", 322);
        Assert.assertEquals("BoolPin {pin=" + pin + ", high=" + true + "}",
                BoolPin.ofValue(pin, true).toString());
    }
}
