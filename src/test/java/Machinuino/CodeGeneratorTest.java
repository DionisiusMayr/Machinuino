package Machinuino;

import Machinuino.model.*;
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
}
