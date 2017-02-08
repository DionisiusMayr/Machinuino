package Machinuino;

import Machinuino.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CodeGeneratorTest {

    private MooreMachine machine;

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
                "    output(__q0);" + lineSeparator +
                "}" + lineSeparator +
                lineSeparator +
                "void loop() {" + lineSeparator +
                "    while (digitalRead(clock) == LOW);" + lineSeparator +
                lineSeparator +
                "    currentState = transition(currentState);" + lineSeparator +
                "    output(currentState);" + lineSeparator +
                lineSeparator +
                "    while (digitalRead(clock) == HIGH);" + lineSeparator +
                "}" + lineSeparator, codeGenerator.generateCode(machine));
    }

    @Test
    public void generateCodeCountTo3() {
        MooreMachine.Builder builder = new MooreMachine.Builder("LMAO");
        builder.states(Stream.of("q0", "q1", "q2", "q3").collect(Collectors.toSet()));
        builder.initialState("q0");
        builder.inputPins(Stream.of(Pin.ofValue("clock", 4), Pin.ofValue("input", 5))
                .collect(Collectors.toSet()));
        builder.outputPins(Stream.of(Pin.ofValue("led1", 6), Pin.ofValue("led2", 7))
                .collect(Collectors.toSet()));

        BoolPin bpo0 = builder.getBoolPinOfValue(builder.getOutputPinOfName("led1"), false);
        BoolPin bpo1 = builder.getBoolPinOfValue(builder.getOutputPinOfName("led1"), true);
        BoolPin bpo2 = builder.getBoolPinOfValue(builder.getOutputPinOfName("led2"), false);
        BoolPin bpo3 = builder.getBoolPinOfValue(builder.getOutputPinOfName("led2"), true);

        Set<BoolPin> boolPins0 = Stream.of(bpo0, bpo2).collect(Collectors.toSet());
        Set<BoolPin> boolPins1 = Stream.of(bpo0, bpo3).collect(Collectors.toSet());
        Set<BoolPin> boolPins2 = Stream.of(bpo1, bpo2).collect(Collectors.toSet());
        Set<BoolPin> boolPins3 = Stream.of(bpo1, bpo3).collect(Collectors.toSet());

        Output output0 = Output.ofValue("q0", boolPins0);
        Output output1 = Output.ofValue("q1", boolPins1);
        Output output2 = Output.ofValue("q2", boolPins2);
        Output output3 = Output.ofValue("q3", boolPins3);

        builder.outputs(Stream.of(output0, output1, output2, output3).collect(Collectors.toSet()));

        Set<BoolPin> bpi0 = Stream.of(builder.getBoolPinOfValue(builder.getInputPinOfName("input"),
                false)).collect(Collectors.toSet());
        Set<BoolPin> bpi1 = Stream.of(builder.getBoolPinOfValue(builder.getInputPinOfName("input"),
                true)).collect(Collectors.toSet());

        Transition transition01 = Transition.ofValue("q0", "q1", bpi1);
        Transition transition12 = Transition.ofValue("q1", "q2", bpi1);
        Transition transition23 = Transition.ofValue("q2", "q3", bpi1);
        Transition transition30 = Transition.ofValue("q3", "q0", bpi1);
        Transition transition03 = Transition.ofValue("q0", "q3", bpi0);
        Transition transition32 = Transition.ofValue("q3", "q2", bpi0);
        Transition transition21 = Transition.ofValue("q2", "q1", bpi0);
        Transition transition10 = Transition.ofValue("q1", "q0", bpi0);

        builder.transitions(Stream.of(transition01, transition12, transition23, transition30,
                transition03, transition32, transition21, transition10)
                .collect(Collectors.toSet()));

        MooreMachine machine2 = builder.build();
        CodeGenerator codeGenerator = CodeGenerator.getInstance();
        String lineSeparator = System.lineSeparator();
        Assert.assertEquals("/* Input */" + lineSeparator +
                "const int clock = 4;" + lineSeparator +
                "const int _input = 5;" + lineSeparator +
                "" + lineSeparator +
                "/* Output */" + lineSeparator +
                "const int _led1 = 6;" + lineSeparator +
                "const int _led2 = 7;" + lineSeparator +
                "" + lineSeparator +
                "/* States */" + lineSeparator +
                "const int __q1 = 0;" + lineSeparator +
                "const int __q2 = 1;" + lineSeparator +
                "const int __q3 = 2;" + lineSeparator +
                "const int __q0 = 3;" + lineSeparator +
                "" + lineSeparator +
                "bool isHigh(int pin) {" + lineSeparator +
                "    return digitalRead(pin) == HIGH ? true : false;" + lineSeparator +
                "}" + lineSeparator +
                lineSeparator +
                "int transition(int current) {" + lineSeparator +
                "    switch(current) {" + lineSeparator +
                "        case __q1:" + lineSeparator +
                "            if (!isHigh(_input)) return __q0;" + lineSeparator +
                "            if (isHigh(_input)) return __q2;" + lineSeparator +
                "            break;" + lineSeparator +
                "        case __q2:" + lineSeparator +
                "            if (!isHigh(_input)) return __q1;" + lineSeparator +
                "            if (isHigh(_input)) return __q3;" + lineSeparator +
                "            break;" + lineSeparator +
                "        case __q3:" + lineSeparator +
                "            if (isHigh(_input)) return __q0;" + lineSeparator +
                "            if (!isHigh(_input)) return __q2;" + lineSeparator +
                "            break;" + lineSeparator +
                "        case __q0:" + lineSeparator +
                "            if (!isHigh(_input)) return __q3;" + lineSeparator +
                "            if (isHigh(_input)) return __q1;" + lineSeparator +
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
                "            digitalWrite(_led1, LOW);" + lineSeparator +
                "            digitalWrite(_led2, HIGH);" + lineSeparator +
                "            break;" + lineSeparator +
                "        case __q2:" + lineSeparator +
                "            digitalWrite(_led2, LOW);" + lineSeparator +
                "            digitalWrite(_led1, HIGH);" + lineSeparator +
                "            break;" + lineSeparator +
                "        case __q3:" + lineSeparator +
                "            digitalWrite(_led1, HIGH);" + lineSeparator +
                "            digitalWrite(_led2, HIGH);" + lineSeparator +
                "            break;" + lineSeparator +
                "        case __q0:" + lineSeparator +
                "            digitalWrite(_led1, LOW);" + lineSeparator +
                "            digitalWrite(_led2, LOW);" + lineSeparator +
                "            break;" + lineSeparator +
                "        default:" + lineSeparator +
                "            // Not reachable" + lineSeparator +
                "            exit(1);" + lineSeparator +
                "            break;" + lineSeparator +
                "    }" + lineSeparator +
                "}" + lineSeparator +
                lineSeparator +
                "int currentState;" + lineSeparator +
                lineSeparator +
                "void setup() {" + lineSeparator +
                "    /* Input */" + lineSeparator +
                "    pinMode(clock, INPUT);" + lineSeparator +
                "    pinMode(_input, INPUT);" + lineSeparator +
                "" + lineSeparator +
                "    /* Output */" + lineSeparator +
                "    pinMode(_led1, OUTPUT);" + lineSeparator +
                "    pinMode(_led2, OUTPUT);" + lineSeparator +
                lineSeparator +
                "    /* Initial state */" + lineSeparator +
                "    currentState = __q0;" + lineSeparator +
                "    output(__q0);" + lineSeparator +
                "}" + lineSeparator +
                lineSeparator +
                "void loop() {" + lineSeparator +
                "    while (digitalRead(clock) == LOW);" + lineSeparator +
                lineSeparator +
                "    currentState = transition(currentState);" + lineSeparator +
                "    output(currentState);" + lineSeparator +
                lineSeparator +
                "    while (digitalRead(clock) == HIGH);" + lineSeparator +
                "}" + lineSeparator, codeGenerator.generateCode(machine2));
    }
}
