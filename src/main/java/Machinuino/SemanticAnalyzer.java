package Machinuino;

import Machinuino.model.Fault;
import Machinuino.model.MooreMachine;
import Machinuino.model.Pin;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

public class SemanticAnalyzer extends MachinuinoBaseVisitor {
    MooreMachine.Builder mooreBuilder;
    Fault fault;
    static SemanticAnalyzer semanticAnalyzerInstance;

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
                fault.addDuplicatePin(pinName, ctx.getStart().getLine());
            }
            else mooreBuilder.addInputPin(pin);
            ++i;
        }

        return super.visitPinsInput(ctx);
    }

    @Override
    public Object visitTransBlock(MachinuinoParser.TransBlockContext ctx) {


        return super.visitTransBlock(ctx);
    }
}
