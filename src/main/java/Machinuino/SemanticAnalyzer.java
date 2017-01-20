package Machinuino;

import Machinuino.model.MooreMachine;

public class SemanticAnalyzer extends MachinuinoBaseVisitor {
    MooreMachine moore;
    StringBuilder warnings;

    SemanticAnalyzer() {
        moore = new MooreMachine();
        warnings = new StringBuilder();
    }

    @Override
    public Object visitMoore(MachinuinoParser.MooreContext ctx) {
        String name = ctx.NAME().getText();
        moore.setName(name);

        return super.visitMoore(ctx);
    }

    @Override
    public Object visitStates(MachinuinoParser.StatesContext ctx) {
        if(ctx.NAME(0) != null) {
            String initialState = ctx.NAME(0).getText();
            moore.setInitialState(initialState);

            int i = 0;
            while(ctx.NAME(i) != null) {
                String state = ctx.NAME(i).getText();
                if(moore.hasState(state))
                    warnings.append("Ignoring duplicate state: " + state + System.lineSeparator());
                else
                    moore.insertState(state);
            }
        }
        else
            warnings.append("Empty state list." + System.lineSeparator());

        return super.visitStates(ctx);
    }

    @Override
    public Object visitPinsInput(MachinuinoParser.PinsInputContext ctx) {
        int clockPin = Integer.parseInt(ctx.NUMBER(0).getText());

        return super.visitPinsInput(ctx);
    }
}
