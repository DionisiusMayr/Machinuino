package Machinuino.model;

/**
 * Um pino utilizado na interface com a máquina, pode ser tanto de entrada, saída ou o clock
 * Imutável
 */

public class Pin {

    private String name;
    private int number;

    private Pin(String name, int number) {
        this.name = name;
        this.number = number;
    }

    /**
     * Retorna um pino com os valores passados no parâmetro
     *
     * @param name nome do pino, não pode ser nulo
     * @param number número do pino da placa
     * @return Pin com os valores passados no parâmetro
     * @throws NullPointerException se o nome passado for nulo
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
