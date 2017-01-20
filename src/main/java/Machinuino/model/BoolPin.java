package Machinuino.model;

public class BoolPin {
    private Pin pin;
    private boolean bool;

    private BoolPin(Pin pin, boolean bool) {
        this.pin = pin;
        this.bool = bool;
    }

    public static BoolPin ofValue(Pin pin, boolean bool) {
        return new BoolPin(pin, bool);
    }

    public Pin getPin() {
        return pin;
    }

    public boolean getBool() {
        return bool;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != getClass()) return false;

        BoolPin boolPin = (BoolPin) o;
        return pin.equals(boolPin.pin) && bool == boolPin.bool;
    }

    @Override
    public int hashCode() {
        int result = pin.hashCode();
        return 31*(result + (bool ? 1 : 0));
    }

    @Override
    public String toString() {
        return "BoolPin {" +
                "pin='" + pin + '\'' +
                ", boolean=" + bool +
                '}';
    }
}
