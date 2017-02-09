package Machinuino;

import Machinuino.model.Fault;
import Machinuino.model.MooreMachine;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        System.out.println("Begin");
        // TODO check the number of arguments
        //String file = args[0];
        String file = "/home/dionisius/Dropbox/UFSCar/2016_2/Laboratório de Microcontroladores e Aplicações/Machinuino/doc/template.moore";

        if(!file.endsWith(".moore")) {
            System.out.println("Pass a .moore file as argument.");
        }
        else {
            String syntacticErrors;
            try {
                syntacticErrors = SyntacticAnalyzer.analyzeFile(file);
            } catch (IOException e) {
                System.out.println("File " + file + " not found. Exitting.");
                System.exit(1);
                return; // Shouldn't be reached.
            }

            if (syntacticErrors.isEmpty()) {
                System.out.println("No Syntactic Errors.");

                SemanticAnalyzer semantic = SemanticAnalyzer.getInstance();
                Fault fault;

                try {
                    fault = semantic.analyzeFile(file);
                } catch (IOException e) {
                    System.out.println("File " + file + " not found. Exitting.");
                    System.exit(1);
                    return; // Shouldn't be reached.
                }

                if (fault.getErrors().isEmpty()) {
                    System.out.println("No Semantic Errors.");
                    if (fault.getWarnings().isEmpty()) {
                        System.out.println("No Semantic Warnings.");
                    }
                    else {
                        System.out.println("Semantic Warnings:");
                        System.out.println(fault.getWarnings());
                    }

                    MooreMachine mm = semantic.buildMachine();

                    System.out.println(mm);

                    System.out.println("Generating code.");
                    // TODO insert code generation here
                } else {
                    System.out.println("ERRORS:");
                    System.out.println(fault.getErrors());
                    System.out.println(fault.getWarnings());
                    System.exit(1);
                    return; // Shouldn't be reached.
                }
            }
        }
    }
}

