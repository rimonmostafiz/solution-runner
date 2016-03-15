package com.tigerit.exam;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Faisal Ahmed
 */
public class SandBox {

    private String inputFileName;

    private String outputFileName;

    public SandBox(String inputFileName, String outputFileName) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }

    public void run(Runnable runnable) {
        InputStream stdIn = System.in;
        PrintStream stdOut = System.out;
        try {
            System.setIn(new FileInputStream(new File(this.inputFileName)));
            System.setOut(new PrintStream(new File(this.outputFileName)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        runnable.run();
        System.setIn(stdIn);
        System.setOut(stdOut);
    }
}
