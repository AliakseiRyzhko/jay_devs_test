package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinimalDistanceTest {

    @Test
    public void minimalDistanceTest() {
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append(MinimalDistance.MINIMUM_EDIT_DISTANCE).append("2").append(System.lineSeparator());
        expectedOutput.append("word1").append(System.lineSeparator());
        expectedOutput.append("word").append(System.lineSeparator());
        expectedOutput.append("wod").append(System.lineSeparator());
        expectedOutput.append(System.lineSeparator());

        String[] args = {"wod", "word1"};
        String output = captureSystemOut(() -> MinimalDistance.main(args));

        assertEquals(expectedOutput.toString(), output);
    }

    private String captureSystemOut(Runnable runnable) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;
        System.setOut(printStream);

        try {
            runnable.run();
            return outputStream.toString();
        } finally {
            System.setOut(originalOut);
        }
    }
}