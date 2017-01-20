package Machinuino.model;

public class Pin {
    private String name;
    private int number;

    private Pin(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public static Pin ofValue(String name, int number) {
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
        return 31*(result + number);
    }

    @Override
    public String toString() {
        return "Pin {" +
                "name='" + name + '\'' +
                ", number=" + number +
                '}';
    }
}
