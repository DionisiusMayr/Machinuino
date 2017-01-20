package Machinuino.model;

/**
 * Represents a pin with a specified value, either true or false
 * Immutable
 */

public class BoolPin {

    private Pin pin;
    private boolean high;

    private BoolPin(Pin pin, boolean high) {
        this.pin = pin;
        this.high = high;
    }

    /**
     * Creates a boolPin with attributes specified by the parameters
     *
     * @param pin pin which will be associated with a value, can not be null
     * @param high value of the pin
     * @return BoolPin with attributes specified by the parameters
     * @throws NullPointerException if the pin passed is null
     */
    public static BoolPin ofValue(Pin pin, boolean high) throws NullPointerException {
        if (pin == null) throw new NullPointerException("BoolPin#ofValue: pin was null!");
        return new BoolPin(pin, high);
    }

    public Pin getPin() {
        return pin;
    }

    public boolean isHigh() {
        return high;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != getClass()) return false;

        BoolPin boolPin = (BoolPin) o;
        return pin.equals(boolPin.pin) && high == boolPin.high;
    }

    @Override
    public int hashCode() {
        int result = pin.hashCode();
        return 31*(result + (high ? 1 : 0));
    }

    @Override
    public String toString() {
        return "BoolPin {" +
                "pin=" + pin +
                ", high=" + high +
                '}';
    }
}
