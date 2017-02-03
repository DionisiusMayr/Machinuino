package Machinuino;

import Machinuino.model.Fault;
import Machinuino.model.MooreMachine;
import Machinuino.model.Pin;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

public class SemanticAnalyzer extends MachinuinoBaseVisitor {
    private MooreMachine.Builder mooreBuilder;
    private Fault fault;
    private static SemanticAnalyzer semanticAnalyzerInstance;

    public static SemanticAnalyzer getInstance() {
        if (semanticAnalyzerInstance == null) semanticAnalyzerInstance =  new SemanticAnalyzer();

        return semanticAnalyzerInstance;
    }

    private SemanticAnalyzer() {
        fault = Fault.getInstance();
    }

    Fault analyzeFile(String fileLocation) throws IOException {
        ANTLRInputStream input = new ANTLRFileStream(fileLocation);
        MachinuinoLexer lexer = new MachinuinoLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MachinuinoParser parser = new MachinuinoParser(tokens);

        visit(parser.moore());

        Fault faultCopy = fault;
        fault = Fault.getInstance();

        return faultCopy;
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
        } else fault.addWarningEmptySection("state",line);

        return super.visitStates(ctx);
    }

    @Override
    public Object visitPinsInput(MachinuinoParser.PinsInputContext ctx) {
        int clockPinNumber = Integer.parseInt(ctx.NUMBER(0).getText());
        mooreBuilder.addInputPin(Pin.ofValue("clock", clockPinNumber));

        int i = 0;
        while (ctx.NAME(i) != null) {
            String pinName = ctx.NAME(i).getText();
            int pinNumber = Integer.parseInt(ctx.NUMBER(i + 1).getText());
            Pin pin = Pin.ofValue(pinName, pinNumber);
            if (mooreBuilder.getInputPinOfName(pin.getName()) != null) {
                // TODO reporting a line that is far from the token
                fault.addErrorDuplicatePin(pinName, ctx.getStart().getLine());
            }
            else mooreBuilder.addInputPin(pin);
            ++i;
        }

        return super.visitPinsInput(ctx);
    }

    @Override
    public Object visitTransition(MachinuinoParser.TransitionContext ctx) {
        if (ctx.NAME(0) == null) {
            fault.addWarningEmptySection("Transition", ctx.getStart().getLine());
        }
        else {
            int i = 0;
            while (ctx.NAME(i) != null) {
                String actualState = ctx.NAME(i).getText();
                if (!mooreBuilder.hasState(actualState)) {
                    fault.addErrorUndeclaredState(actualState, ctx.getStart().getLine());
                }

                // TODO test this
                if (ctx.transBlock(i) == null) {
                    fault.addWarningEmptySection("Transition block", ctx.getStart().getLine());
                }
                ++i;
            }
        }

        return super.visitTransition(ctx);
    }

    @Override
    public Object visitTransBlock(MachinuinoParser.TransBlockContext ctx) {
        return super.visitTransBlock(ctx);
    }

    @Override
    public Object visitPartialTrans(MachinuinoParser.PartialTransContext ctx) {
        if (ctx.NAME() != null) {
            String targetState = ctx.NAME().getText();
            if (!mooreBuilder.hasState(targetState)) {
                fault.addErrorUndeclaredState(targetState, ctx.getStart().getLine());
            }
        }

        return super.visitPartialTrans(ctx);
    }

    @Override
    public Object visitLogicExp(MachinuinoParser.LogicExpContext ctx) {
        int i = 0;
        while (ctx.extName(i) != null) {
            String pinName = ctx.extName(i).NAME().getText();
            int line = ctx.getStart().getLine();

            Pin pin = mooreBuilder.getInputPinOfName(pinName);

            // TODO test both conditions
            if (pin == null || !mooreBuilder.hasInputPin(pin))
                fault.addErrorUndeclaredInputPin(pinName, line);

            ++i;
        }

        return super.visitLogicExp(ctx);
    }

    // TODO: Verify the pin number
    @Override
    public Object visitPinsOutput(MachinuinoParser.PinsOutputContext ctx) {
        int i = 0;
        if (ctx.NAME(i) == null)
            fault.addWarningEmptySection("Output pins", ctx.getStart().getLine());

        while (ctx.NAME(i) != null) {
            int line = ctx.getStart().getLine();
            String pinName = ctx.NAME(i).getText();
            if (mooreBuilder.getInputPinOfName(pinName) != null) {
                fault.addErrorDuplicatePin(pinName, line);
            }
            else if (mooreBuilder.getOutputPinOfName(pinName) != null) {
                fault.addErrorUndeclaredOutputPin(pinName, line);
            }

            ++i;
        }

        return super.visitPinsOutput(ctx);
    }
}
