package Machinuino;

import Machinuino.model.Fault;
import Machinuino.model.MooreMachine;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    private static final String mooreExtension = ".moore";

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Incorrect Usage. Instructions:" + System.lineSeparator() +
                    "java -jar package.jar mooreMachine.moore");
        }

        String file = args[0];

        if (!file.endsWith(mooreExtension)) {
            System.out.println("Pass a .moore file as argument.");
        } else {
            System.out.println("Compiling file " + file);
            String syntacticErrors = "";

            try {
                syntacticErrors = SyntacticAnalyzer.analyzeFile(file);
            } catch (IOException e) {
                System.out.println("File " + file + " not found. Exitting.");
                System.exit(1);
            }

            if (syntacticErrors.isEmpty()) {
                System.out.println(" No Syntactic Errors.");

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
                    System.out.println(" No Semantic Errors.");
                    if (fault.getWarnings().isEmpty()) {
                        System.out.println(" No Semantic Warnings.\n");
                    } else {
                        System.out.println("Semantic Warnings:");
                        System.out.println(fault.getWarnings());
                    }

                    MooreMachine machine = semantic.buildMachine();

                    System.out.println("Generating code.");

                    CodeGenerator codeGen = CodeGenerator.getInstance();
                    String inoFile = file.substring(0,
                            file.length() - mooreExtension.length()) + ".ino";
                    try {
                        PrintWriter pwIno = new PrintWriter(new FileWriter(inoFile));
                        pwIno.print(codeGen.generateCode(machine));
                        pwIno.flush();
                        pwIno.close();
                    } catch (IOException e) {
                        System.out.println("Error creating file " + inoFile + "\nExitting.");
                        System.exit(1);
                    }

                    System.out.println("Code generated successfully!\n");
                    System.out.println("Generating graphviz file.");

                    DotGenerator graphvizGenerator = DotGenerator.getInstance();
                    String gvFile = file.substring(0,
                            file.length() - mooreExtension.length()) + ".gv";
                    try {
                        PrintWriter pwGv = new PrintWriter(new FileWriter(gvFile));
                        pwGv.print(graphvizGenerator.generateImage(machine));
                        pwGv.flush();
                        pwGv.close();
                    } catch (IOException e) {
                        System.out.println("Error creating file " + gvFile + "\nExitting.");
                        System.exit(1);
                    }

                    System.out.println("Graphviz file generated successfully!");
                } else {
                    System.out.println("ERRORS:");
                    System.out.println(fault.getErrors());
                    System.out.println("Warnings:");
                    System.out.println(fault.getWarnings());
                    System.exit(1);
                }
            }
        }
    }
}

