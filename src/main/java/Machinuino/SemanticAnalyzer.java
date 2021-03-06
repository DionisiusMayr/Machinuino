package Machinuino;

import Machinuino.model.*;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SemanticAnalyzer extends MachinuinoBaseVisitor {
    private MooreMachine.Builder mooreBuilder;
    private Fault fault;
    private static SemanticAnalyzer semanticAnalyzerInstance;
    private boolean finishedAnalysis;
    private Set<Integer> pinNumbers;
    private String previousState;

    public static SemanticAnalyzer getInstance() {
        if (semanticAnalyzerInstance == null) {
            semanticAnalyzerInstance = new SemanticAnalyzer();
            semanticAnalyzerInstance.finishedAnalysis = false;
        }

        return semanticAnalyzerInstance;
    }

    private SemanticAnalyzer() {
        fault = Fault.getInstance();
        pinNumbers = new HashSet<>();
    }

    Fault analyzeFile(String fileLocation) throws IOException {
        ANTLRInputStream input = new ANTLRFileStream(fileLocation);
        MachinuinoLexer lexer = new MachinuinoLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MachinuinoParser parser = new MachinuinoParser(tokens);

        visit(parser.moore());

        Fault faultCopy = fault;
        fault = Fault.getInstance();

        finishedAnalysis = true;
        return faultCopy;
    }

    /**
     * This method must be called after {@link #analyzeFile}.
     *
     * @return the {@link MooreMachine} created during the analysis.
     * @throws IllegalStateException if this method is called before {@link #analyzeFile}.
     *                               TODO: test this
     */
    public MooreMachine buildMachine() {
        if (finishedAnalysis) return mooreBuilder.build();
        else {
            throw new IllegalStateException("Must analyze file before calling " +
                    "SemanticAnalyzer#buildMachine!");
        }
    }

    @Override
    public Object visitMoore(MachinuinoParser.MooreContext ctx) {
        String name = ctx.NAME().getText();
        mooreBuilder = new MooreMachine.Builder(name);

        return super.visitMoore(ctx);
    }

    @Override
    public Object visitStates(MachinuinoParser.StatesContext ctx) {
        int line = ctx.getStart().getLine();

        if (ctx.NAME(0) != null) {
            String initialState = ctx.NAME(0).getText();

            mooreBuilder.addState(initialState);
            mooreBuilder.initialState(initialState);

            int i = 1;
            while (ctx.NAME(i) != null) {
                String state = ctx.NAME(i).getText();
                if (mooreBuilder.hasState(state)) {
                    fault.addWarningDuplicateSymbol(state, line);
                } else mooreBuilder.addState(state);

                ++i;
            }
        } else fault.addWarningEmptySection("state", line);

        return super.visitStates(ctx);
    }

    @Override
    public Object visitPinsInput(MachinuinoParser.PinsInputContext ctx) {
        int clockPinNumber = Integer.parseInt(ctx.NUMBER(0).getText());
        mooreBuilder.addInputPin(Pin.ofValue("clock", clockPinNumber));

        pinNumbers.add(clockPinNumber);

        // TODO: Document what an empty "input pins" is (it ignore the clock pin)
        if (ctx.NAME(0) == null) {
            fault.addWarningEmptySection("Input Pins", ctx.getStart().getLine());
        } else {
            int i = 0;

            while (ctx.NAME(i) != null) {
                String pinName = ctx.NAME(i).getText();
                int pinNumber = Integer.parseInt(ctx.NUMBER(i + 1).getText());
                if (pinNumbers.contains(pinNumber)) {
                    fault.addErrorPinNumberAlreadyUsed(pinNumber, ctx.getStart().getLine());
                }
                Pin pin = Pin.ofValue(pinName, pinNumber);
                if (mooreBuilder.getInputPinOfName(pin.getName()) != null) {
                    // TODO it is reporting a line that is far from the token
                    fault.addErrorDuplicatePin(pinName, ctx.getStart().getLine());
                } else mooreBuilder.addInputPin(pin);
                ++i;
            }
        }

        return super.visitPinsInput(ctx);
    }

    @Override
    public Object visitTransition(MachinuinoParser.TransitionContext ctx) {
        if (ctx.NAME(0) == null) {
            fault.addWarningEmptySection("Transition", ctx.getStart().getLine());
        } else {
            int i = 0;
            while (ctx.NAME(i) != null) {
                previousState = ctx.NAME(i).getText();

                if (!mooreBuilder.hasState(previousState)) {
                    fault.addErrorUndeclaredState(previousState, ctx.getStart().getLine());
                }

                // Note: it won't inform two empty transition blocks, thus the name of the trans block
                if (ctx.transBlock(i) == null || ctx.transBlock(i).getText().isEmpty()) {
                    fault.addWarningEmptySection("Transition block of " + ctx.NAME(i).getText(),
                            ctx.getStart().getLine());
                }

                visitTransBlock(ctx.transBlock(i));

                ++i;
            }
        }

        return "";  // Note: already visited the children rules.
    }


    @Override
    public Object visitPartialTrans(MachinuinoParser.PartialTransContext ctx) {
        if (ctx.NAME() != null) {
            String targetState = ctx.NAME().getText();
            if (!mooreBuilder.hasState(targetState)) {
                fault.addErrorUndeclaredState(targetState, ctx.getStart().getLine());
            } else if (mooreBuilder.hasState(previousState)) {
                Set<BoolPin> boolPins = visitLogicExp(ctx.logicExp());
                Transition trans;

                if (boolPins != null) trans = Transition.ofValue(previousState, targetState, boolPins);
                else return "";

                if (mooreBuilder.transitionCausesNonDeterminism(trans)) {
                    fault.addErrorDuplicateTransitionFromState(previousState, ctx.getStart().getLine());
                } else {
                    mooreBuilder.addTransition(trans);
                }
            }
        }

        return "";  // Note: already visited the children rules.
    }

    // Returns a BoolPin Set representing the entire logic expression.
    @Override
    public Set<BoolPin> visitLogicExp(MachinuinoParser.LogicExpContext ctx) {
        Set<String> inputsUsed = new HashSet<>();
        Set<BoolPin> boolPins = new HashSet<>();

        int i = 0;
        while (ctx.extName(i) != null) {
            String pinName = ctx.extName(i).NAME().getText();
            boolean pinValue = !ctx.extName(i).getText().startsWith("!");
            int line = ctx.getStart().getLine();

            Pin pin = mooreBuilder.getInputPinOfName(pinName);

            // TODO test both conditions
            if (pin == null || !mooreBuilder.hasInputPin(pin)) {
                fault.addErrorUndeclaredInputPin(pinName, line);
                boolPins = null;
                break;
            } else {
                if (inputsUsed.contains(pinName)) {
                    fault.addErrorInputAlreadyInExp(pinName, ctx.getStart().getLine());
                } else {
                    inputsUsed.add(pinName);
                    boolPins.add(BoolPin.ofValue(mooreBuilder.getInputPinOfName(pinName), pinValue));
                }
            }

            ++i;
        }

        super.visitLogicExp(ctx);
        return boolPins;
    }

    @Override
    public Object visitPinsOutput(MachinuinoParser.PinsOutputContext ctx) {
        if (ctx.NAME(0) == null) fault.addWarningEmptySection("Output pins", ctx.getStart().getLine());

        int i = 0;
        while (ctx.NAME(i) != null) {
            int line = ctx.getStart().getLine();
            String outputPinName = ctx.NAME(i).getText();
            int outputPinNumber = Integer.parseInt(ctx.NUMBER(i).getText());

            if (pinNumbers.contains(outputPinNumber)) {
                fault.addErrorPinNumberAlreadyUsed(outputPinNumber, ctx.getStart().getLine());
            }

            if (mooreBuilder.getInputPinOfName(outputPinName) != null) fault.addErrorDuplicatePin(outputPinName, line);
            else if (mooreBuilder.getOutputPinOfName(outputPinName) != null) {
                if (mooreBuilder.getOutputPinOfName(outputPinName).getName().equals(outputPinName)) {
                    fault.addErrorDuplicatePin(outputPinName, ctx.getStart().getLine());
                } else fault.addErrorUndeclaredOutputPin(outputPinName, line);
            } else mooreBuilder.addOutputPin(Pin.ofValue(outputPinName, outputPinNumber));

            ++i;
        }

        return super.visitPinsOutput(ctx);
    }

    @Override
    public Object visitFunction(MachinuinoParser.FunctionContext ctx) {
        int i = 0;
        while (ctx.NAME(i) != null) {
            String state = ctx.NAME(i).getText();
            int line = ctx.getStart().getLine();

            if (!mooreBuilder.hasState(state)) {
                fault.addErrorUndeclaredState(state, line);
            } else {
                if (mooreBuilder.hasOutput(state)) {
                    fault.addErrorOutputAlreadyDefined(state, line);
                } else {
                    String stateOfOutput = state;
                    Set<BoolPin> sbp = visitFuncBlock(ctx.funcBlock(i));

                    mooreBuilder.addOutput(stateOfOutput, sbp);
                }
            }
            ++i;
        }
        return super.visitFunction(ctx);
    }

    // Returns a BoolPin Set representing every output of a certain state.
    @Override
    public Set<BoolPin> visitFuncBlock(MachinuinoParser.FuncBlockContext ctx) {
        int line = ctx.getStart().getLine();
        Set<String> pinsWithDefinedOutput = new HashSet<>();
        Set<BoolPin> boolPins = new HashSet<>();

        int i = 0;
        while (ctx.extName(i) != null) {
            String outputPinName = ctx.extName(i).NAME().getText();
            boolean pinValue = !ctx.extName(i).getText().startsWith("!");

            Pin outputPin = mooreBuilder.getOutputPinOfName(outputPinName);
            if (outputPin == null) {
                fault.addErrorUndeclaredOutputPin(outputPinName, line);
            } else {
                if (pinsWithDefinedOutput.contains(outputPinName)) {
                    fault.addErrorOutputAlreadyDefined(outputPinName, line);
                } else {
                    pinsWithDefinedOutput.add(outputPinName);
                    boolPins.add(mooreBuilder.getBoolPinOfValue(outputPin, pinValue));
                }
            }

            ++i;
        }

        super.visitFuncBlock(ctx);
        return boolPins;
    }
}
