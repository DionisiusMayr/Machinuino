package Machinuino.model;

import org.junit.Assert;
import org.junit.Test;

//TODO: Use System.lineSeparator()
public class FaultTest {

    @Test
    public void startWithoutErrors() {
        Fault fault = Fault.getInstance();
        Assert.assertTrue(fault.getErrors().isEmpty() && fault.getWarnings().isEmpty());
    }

    @Test
    public void printEmptyError() {
        Fault fault = Fault.getInstance();
        Assert.assertEquals("Fault {errors=[], warnings=[]}", fault.toString());
    }

    @Test
    public void oneError() {
        Fault fault = Fault.getInstance();
        fault.addError("Simple error message\n");
        Assert.assertEquals("Fault {errors=[Simple error message\n], warnings=[]}", fault.toString());
    }

    @Test
    public void manyErrors() {
        Fault fault = Fault.getInstance();
        fault.addError("Simple error message\n");
        fault.addError("Second line\n");
        fault.addError("Final\n");
        Assert.assertEquals("Fault {errors=[Simple error message\n, Second line\n, Final\n], warnings=[]}",
                fault.toString());
    }

    @Test
    public void oneWarning() {
        Fault fault = Fault.getInstance();
        fault.addWarning("This is a warning\n");
        Assert.assertEquals("Fault {errors=[], warnings=[This is a warning\n]}", fault.toString());
    }

    @Test
    public void manyWarnings() {
        Fault fault = Fault.getInstance();
        fault.addWarning("This is a warning\n");
        fault.addWarning("Another one\n");
        fault.addWarning("Lets go\n");
        Assert.assertEquals("Fault {errors=[], warnings=[This is a warning\n, Another one\n, Lets go\n]}",
                fault.toString());
    }

    @Test
    public void addOneErrorOneWarning() {
        Fault fault = Fault.getInstance();
        fault.addWarning("This is a warning\n");
        fault.addError("Simple error message\n");
        Assert.assertEquals("Fault {errors=[Simple error message\n], warnings=[This is a warning\n]}",
                fault.toString());
    }

    @Test
    public void manyErrosAndWarnings() {
        Fault fault = Fault.getInstance();
        fault.addWarning("This is a warning\n");
        fault.addError("Simple error message\n");
        fault.addWarning("Lets go\n");
        fault.addWarning("Another one\n");
        fault.addError("Second line\n");
        fault.addError("Final\n");

        Assert.assertEquals("Fault {errors=[Simple error message\n, Second line\n, Final\n], " +
                "warnings=[This is a warning\n, Lets go\n, Another one\n]}", fault.toString());
    }

    @Test
    public void emptySections() {
        Fault fault = Fault.getInstance();
        fault.addWarningEmptySection("States", 11);
        Assert.assertEquals("Fault {errors=[], warnings=[11: Empty \"States\" section\n]}", fault.toString());
    }
}
