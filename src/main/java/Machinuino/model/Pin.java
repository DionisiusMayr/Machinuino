package Machinuino.model;

/**
 * A real pin used as interface with the machine, can be either input, output or the clock
 * Immutable
 */

public class Pin {

    private String name;
    private int number;

    private Pin(String name, int number) {
        this.name = name;
        this.number = number;
    }

    /**
     * Creates a pin with attributes specified by the parameters
     *
     * @param name name of the pin, can not be null
     * @param number number of the pin
     * @return Pin with attributes specified by the parameters
     * @throws NullPointerException if the name passed is null
     */
    public static Pin ofValue(String name, int number) throws NullPointerException {
        if (name == null) throw new NullPointerException("Pin#ofValue: name was null!");
        return new Pin(name, number);
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != getClass()) return false;

        Pin pin = (Pin) o;
        return name.equals(pin.name) && number == pin.number;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        return 31 * result + number;
    }

    @Override
    public String toString() {
        return "Pin {" +
                "name='" + name + '\'' +
                ", number=" + number +
                '}';
    }
}
