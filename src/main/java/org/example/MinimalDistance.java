package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinimalDistance {
    public static final String ILLEGAL_ARGUMENT = "Usage: java MinimalDistance <word1> <word2>";
    public static final Logger logger = LoggerFactory.getLogger(MinimalDistance.class);
    public static final String MINIMUM_EDIT_DISTANCE = "Minimum Edit Distance: ";
    public static final int OPERATION_COST = 1;
    public static final int NO_COST = 0;

    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                throw new IllegalArgumentException(ILLEGAL_ARGUMENT);
            }
            final int[][] distanceMatrix = getDistanceMatrix(args[0], args[1]);
            outputIntermediateWords(args[0], args[1], distanceMatrix);
        } catch (IllegalArgumentException e) {
            logger.error("Error: {}", e.getMessage());
        }
    }

    private static void outputIntermediateWords(String source, String target, int[][] distanceMatrix) {
        int sourceIndex = source.length();
        int targetIndex = target.length();
        int distance = distanceMatrix[sourceIndex][targetIndex];
        final StringBuilder transformedWord = new StringBuilder(target);
        final StringBuilder output = new StringBuilder();
        output.append(MINIMUM_EDIT_DISTANCE).append(distanceMatrix[sourceIndex][targetIndex]).append(System.lineSeparator());
        output.append(transformedWord.toString()).append(System.lineSeparator());
        while (distance > 0) {
            final int deletion = targetIndex > 0 ? distanceMatrix[sourceIndex][targetIndex - 1] : distance;
            final int insertion = sourceIndex > 0 ? distanceMatrix[sourceIndex - 1][targetIndex] : distance;
            final int substitution = (sourceIndex > 0 && targetIndex > 0) ? distanceMatrix[sourceIndex - 1][targetIndex - 1] : distance;

            if (substitution < distance) {
                transformedWord.setCharAt(targetIndex - 1, source.charAt(sourceIndex - 1));
                sourceIndex--;
                targetIndex--;
                distance = substitution;
                output.append(transformedWord.toString()).append(System.lineSeparator());
            } else if (deletion < distance) {
                transformedWord.deleteCharAt(targetIndex - 1);
                targetIndex--;
                distance = deletion;
                output.append(transformedWord.toString()).append(System.lineSeparator());
            } else if (insertion < distance) {
                transformedWord.insert(targetIndex, source.charAt(sourceIndex - 1));
                sourceIndex--;
                distance = insertion;
                output.append(transformedWord.toString()).append(System.lineSeparator());
            } else {
                sourceIndex--;
                targetIndex--;
            }
        }
        System.out.println(output.toString());
    }

    private static int[][] getDistanceMatrix(final String source, final String target) {
        final int sourceLength = source.length();
        final int targetLength = target.length();
        final int[][] distanceMatrix = new int[sourceLength + 1][targetLength + 1];

        for (int i = 0; i <= sourceLength; i++) {
            for (int j = 0; j <= targetLength; j++) {
                if (i == 0) {
                    distanceMatrix[i][j] = j;
                } else if (j == 0) {
                    distanceMatrix[i][j] = i;
                } else {
                    final int deletion = distanceMatrix[i][j - 1] + 1;
                    final int insertion = distanceMatrix[i - 1][j] + 1;
                    final int substitution = distanceMatrix[i - 1][j - 1] + (source.charAt(i - 1) == target.charAt(j - 1) ? NO_COST : OPERATION_COST);
                    distanceMatrix[i][j] = Math.min(Math.min(deletion, insertion), substitution);
                }
            }
        }
        return distanceMatrix;
    }
}
