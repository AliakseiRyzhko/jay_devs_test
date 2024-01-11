package org.example;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.OutputStreamAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MinimalDistanceTest {
    private ByteArrayOutputStream logOutputStream;

    @Test
    public void targetMoreThanSourceTest() {
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

    @Test
    public void targetLessThanSourceTest() {
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append(MinimalDistance.MINIMUM_EDIT_DISTANCE).append("2").append(System.lineSeparator());
        expectedOutput.append("wod").append(System.lineSeparator());
        expectedOutput.append("wod1").append(System.lineSeparator());
        expectedOutput.append("word1").append(System.lineSeparator());
        expectedOutput.append(System.lineSeparator());

        String[] args = {"word1", "wod"};
        String output = captureSystemOut(() -> MinimalDistance.main(args));

        assertEquals(expectedOutput.toString(), output);
    }

    @Test
    public void targetEqualsSourceTest() {
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append(MinimalDistance.MINIMUM_EDIT_DISTANCE).append("1").append(System.lineSeparator());
        expectedOutput.append("word2").append(System.lineSeparator());
        expectedOutput.append("word1").append(System.lineSeparator());
        expectedOutput.append(System.lineSeparator());

        String[] args = {"word1", "word2"};
        String output = captureSystemOut(() -> MinimalDistance.main(args));

        assertEquals(expectedOutput.toString(), output);
    }

    @Test
    public void oneWordErrorTest() {
        setupLogger();
        String[] args = {"word1"};
        MinimalDistance.main(args);
        String logOutput = logOutputStream.toString();

        assertTrue(logOutput.contains(MinimalDistance.ILLEGAL_ARGUMENT));
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

    public void setupLogger() {
        logOutputStream = new ByteArrayOutputStream();

        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration configuration = context.getConfiguration();
        Appender appender = OutputStreamAppender.newBuilder()
                .setName("TestAppender")
                .setLayout(PatternLayout.newBuilder().withPattern("%msg%n").build())
                .setTarget(logOutputStream)
                .build();
        appender.start();
        configuration.addAppender(appender);
        Appender ref = configuration.getAppender(appender.getName());
        LoggerConfig loggerConfig = configuration.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.addAppender(ref, null, null);
        context.updateLoggers();
    }
}