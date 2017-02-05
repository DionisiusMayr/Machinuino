package Machinuino;

import Machinuino.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CodeGeneratorTest {

    MooreMachine machine;

    @Before
    public void init() {
        MooreMachine.Builder builder = new MooreMachine.Builder("M1");
        builder.states(Stream.of("q0", "q1", "q2").collect(Collectors.toSet()))
                .initialState("q0")
                .inputPins(Stream.of(Pin.ofValue("clock", 10), Pin.ofValue("button", 11),
                        Pin.ofValue("switch", 12)).collect(Collectors.toSet()))
                .outputPins(Stream.of(Pin.ofValue("motor", 5), Pin.ofValue("led", 6))
                        .collect(Collectors.toSet()));
        BoolPin notButton = builder.getBoolPinOfValue(builder.getInputPinOfName("button"), false);
        BoolPin button = builder.getBoolPinOfValue(builder.getInputPinOfName("button"), true);
        BoolPin notSwitch = builder.getBoolPinOfValue(builder.getInputPinOfName("switch"), false);
        BoolPin _switch = builder.getBoolPinOfValue(builder.getInputPinOfName("switch"), true);
        Set<BoolPin> notButtonAndNotSwitch = Stream.of(notButton, notSwitch)
                .collect(Collectors.toSet());
        Set<BoolPin> notButtonAndSwitch = Stream.of(notButton, _switch).collect(Collectors.toSet());
        Set<BoolPin> buttonAndNotSwitch = Stream.of(button, notSwitch).collect(Collectors.toSet());
        Set<BoolPin> buttonAndSwitch = Stream.of(button, _switch).collect(Collectors.toSet());
        Transition q0buttonSwitch = Transition.ofValue("q0", "q1", buttonAndSwitch);
        Transition q0ButtonNotSwitch = Transition.ofValue("q0", "q0", buttonAndNotSwitch);
        Transition q0NotButtonSwitch = Transition.ofValue("q0", "q2", notButtonAndSwitch);
        Transition q0NotButtonNotSwitch = Transition.ofValue("q0", "q1", notButtonAndNotSwitch);

        Transition q1buttonSwitch = Transition.ofValue("q1", "q0", buttonAndSwitch);
        Transition q1ButtonNotSwitch = Transition.ofValue("q1", "q0", buttonAndNotSwitch);
        Transition q1NotButtonSwitch = Transition.ofValue("q1", "q0", notButtonAndSwitch);
        Transition q1NotButtonNotSwitch = Transition.ofValue("q1", "q2", notButtonAndNotSwitch);

        Transition q2buttonSwitch = Transition.ofValue("q2", "q1", buttonAndSwitch);
        Transition q2ButtonNotSwitch = Transition.ofValue("q2", "q1", buttonAndNotSwitch);
        Transition q2NotButtonSwitch = Transition.ofValue("q2", "q2", notButtonAndSwitch);
        Transition q2NotButtonNotSwitch = Transition.ofValue("q2", "q2", notButtonAndNotSwitch);

        Set<Transition> transitions = Stream.of(q0buttonSwitch, q0ButtonNotSwitch,
                q0NotButtonSwitch, q0NotButtonNotSwitch, q1buttonSwitch, q1ButtonNotSwitch,
                q1NotButtonSwitch, q1NotButtonNotSwitch, q2buttonSwitch, q2ButtonNotSwitch,
                q2NotButtonNotSwitch, q2NotButtonSwitch).collect(Collectors.toSet());
        builder.transitions(transitions);

        BoolPin notMotor = builder.getBoolPinOfValue(builder.getOutputPinOfName("motor"), false);
        BoolPin motor = builder.getBoolPinOfValue(builder.getOutputPinOfName("motor"), true);
        BoolPin notLed = builder.getBoolPinOfValue(builder.getOutputPinOfName("led"), false);
        BoolPin led = builder.getBoolPinOfValue(builder.getOutputPinOfName("led"), true);

        Output q0 = Output.ofValue("q0", Stream.of(motor, notLed).collect(Collectors.toSet()));
        Output q1 = Output.ofValue("q1", Stream.of(notMotor, notLed).collect(Collectors.toSet()));
        Output q2 = Output.ofValue("q2", Stream.of(motor, led).collect(Collectors.toSet()));

        builder.outputs(Stream.of(q0, q1, q2).collect(Collectors.toSet()));

        machine = builder.build();
    }

    @Test(expected = NullPointerException.class)
    public void generateCodeNullMooreMachineShouldThrowException() {
        CodeGenerator codeGenerator = CodeGenerator.getInstance();
        codeGenerator.generateCode(null);
    }

    @Test
    public void generateCode() {
        CodeGenerator codeGenerator = CodeGenerator.getInstance();
        String lineSeparator = System.lineSeparator();
        Assert.assertEquals("/* Input */" + lineSeparator +
                "const int clock = 10;" + lineSeparator +
                "const int _switch = 12;" + lineSeparator +
                "const int _button = 11;" + lineSeparator +
                "" + lineSeparator +
                "/* Output */" + lineSeparator +
                "const int _motor = 5;" + lineSeparator +
                "const int _led = 6;" + lineSeparator +
                "" + lineSeparator +
                "/* States */" + lineSeparator +
                "const int __q1 = 0;" + lineSeparator +
                "const int __q2 = 1;" + lineSeparator +
                "const int __q0 = 2;" + lineSeparator +
                "" + lineSeparator +
                "bool isHigh(int pin) {" + lineSeparator +
                "    return digitalRead(pin) == HIGH ? true : false;" + lineSeparator +
                "}" + lineSeparator +
                lineSeparator +
                "int transition(int current) {" + lineSeparator +
                "    switch(current) {" + lineSeparator +
                "        case __q1:" + lineSeparator +
                "            if (!isHigh(_switch) && isHigh(_button)) return __q0;" +
                lineSeparator +
                "            if (!isHigh(_button) && isHigh(_switch)) return __q0;" +
                lineSeparator +
                "            if (isHigh(_switch) && isHigh(_button)) return __q0;" +
                lineSeparator +
                "            if (!isHigh(_button) && !isHigh(_switch)) return __q2;" +
                lineSeparator +
                "            break;" + lineSeparator +
                "        case __q2:" + lineSeparator +
                "            if (!isHigh(_switch) && isHigh(_button)) return __q1;" +
                lineSeparator +
                "            if (!isHigh(_button) && isHigh(_switch)) return __q2;" +
                lineSeparator +
                "            if (isHigh(_switch) && isHigh(_button)) return __q1;" +
                lineSeparator +
                "            if (!isHigh(_button) && !isHigh(_switch)) return __q2;" +
                lineSeparator +
                "            break;" + lineSeparator +
                "        case __q0:" + lineSeparator +
                "            if (isHigh(_switch) && isHigh(_button)) return __q1;" +
                lineSeparator +
                "            if (!isHigh(_switch) && isHigh(_button)) return __q0;" +
                lineSeparator +
                "            if (!isHigh(_button) && isHigh(_switch)) return __q2;" +
                lineSeparator +
                "            if (!isHigh(_button) && !isHigh(_switch)) return __q1;" +
                lineSeparator +
                "            break;" + lineSeparator +
                "        default:" + lineSeparator +
                "            // Not reachable" + lineSeparator +
                "            exit(1);" + lineSeparator +
                "            break;" + lineSeparator +
                "    }" + lineSeparator +
                "}" + lineSeparator +
                lineSeparator +
                "void output(int current) {" + lineSeparator +
                "    switch(current) {" + lineSeparator +
                "        case __q1:" + lineSeparator +
                "            digitalWrite(_led, LOW);" + lineSeparator +
                "            digitalWrite(_motor, LOW);" + lineSeparator +
                "            break;" + lineSeparator +
                "        case __q2:" + lineSeparator +
                "            digitalWrite(_motor, HIGH);" + lineSeparator +
                "            digitalWrite(_led, HIGH);" + lineSeparator +
                "            break;" + lineSeparator +
                "        case __q0:" + lineSeparator +
                "            digitalWrite(_led, LOW);" + lineSeparator +
                "            digitalWrite(_motor, HIGH);" + lineSeparator +
                "            break;" + lineSeparator +
                "        default:" + lineSeparator +
                "            // Not reachable" + lineSeparator +
                "            exit(1);" + lineSeparator +
                "            break;" + lineSeparator +
                "    }" + lineSeparator +
                "}" + lineSeparator +
                lineSeparator +
                "int currentState;" + lineSeparator +
                "bool previousClock;" + lineSeparator +
                lineSeparator +
                "void setup() {" + lineSeparator +
                "    /* Input */" + lineSeparator +
                "    pinMode(clock, INPUT);" + lineSeparator +
                "    pinMode(_switch, INPUT);" + lineSeparator +
                "    pinMode(_button, INPUT);" + lineSeparator +
                "" + lineSeparator +
                "    /* Output */" + lineSeparator +
                "    pinMode(_motor, OUTPUT);" + lineSeparator +
                "    pinMode(_led, OUTPUT);" + lineSeparator +
                lineSeparator +
                "    /* Initial state */" + lineSeparator +
                "    currentState = __q0;" + lineSeparator +
                "    previousClock = false;" + lineSeparator +
                "    output(__q0);" + lineSeparator +
                "}" + lineSeparator +
                lineSeparator +
                "void loop() {" + lineSeparator +
                "    if (!previousClock && digitalRead(clock) == HIGH) {" + lineSeparator +
                "        currentState = transition(currentState);" + lineSeparator +
                "        output(currentState);" + lineSeparator +
                "    }" + lineSeparator +
                lineSeparator +
                "    previousClock = digitalRead(clock) == HIGH;" + lineSeparator +
                "}" + lineSeparator, codeGenerator.generateCode(machine));
    }
}
