package com.tigerit.exam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Faisal Ahmed
 * Sandboxing applicant code execution
 */
public class SandBox {

    private String inputFileName;

    private String outputFileName;

    private static final Logger logger = LoggerFactory.getLogger(SandBox.class);

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
        try {
            runnable.run();
            System.setIn(stdIn);
            System.setOut(stdOut);
        } catch (Exception e) {
            System.setIn(stdIn);
            System.setOut(stdOut);
            logger.debug("Exception occurred inside applicant solution");
            throw new RuntimeException(e);
        }
    }
}
