package org.example;

public class MinimalDistance {
    public static final String MINIMUM_EDIT_DISTANCE = "Minimum Edit Distance: ";

    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                throw new IllegalArgumentException("Usage: java MinimalDistance <word1> <word2>");
            }
            minimalDistance(args[0], args[1]);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void minimalDistance(final String source, final String target) {
        final int sourceLength = source.length();
        final int targetLength = target.length();
        int[][] editDistanceMatrix = new int[sourceLength + 1][targetLength + 1];

        for (int i = 0; i <= sourceLength; i++) {
            editDistanceMatrix[i][0] = i;
        }
        for (int j = 0; j <= targetLength; j++) {
            editDistanceMatrix[0][j] = j;
        }

        for (int i = 1; i <= sourceLength; i++) {
            for (int j = 1; j <= targetLength; j++) {
                final int deletion = editDistanceMatrix[i][j - 1] + 1;
                final int insertion = editDistanceMatrix[i - 1][j] + 1;
                final int substitution = editDistanceMatrix[i - 1][j - 1] + (source.charAt(i - 1) == target.charAt(j - 1) ? 0 : 1);
                editDistanceMatrix[i][j] = Math.min(Math.min(deletion, insertion), substitution);
            }
        }

        int distance = editDistanceMatrix[sourceLength][targetLength];
        int currentSourceIndex = sourceLength;
        int currentTargetIndex = targetLength;
        StringBuilder transformedWord = new StringBuilder(target);
        StringBuilder output = new StringBuilder();
        output.append(MINIMUM_EDIT_DISTANCE).append(editDistanceMatrix[sourceLength][targetLength]).append(System.lineSeparator());
        output.append(transformedWord.toString()).append(System.lineSeparator());
        while (distance > 0) {
            int deletion = editDistanceMatrix[currentSourceIndex][currentTargetIndex - 1];
            int insertion = editDistanceMatrix[currentSourceIndex - 1][currentTargetIndex];
            int substitution = editDistanceMatrix[currentSourceIndex - 1][currentTargetIndex - 1];

            if (substitution < distance) {
                transformedWord.setCharAt(currentTargetIndex - 1, source.charAt(currentSourceIndex - 1));
                currentSourceIndex--;
                currentTargetIndex--;
                distance = substitution;
                output.append(transformedWord.toString()).append(System.lineSeparator());
            } else if (deletion < distance) {
                transformedWord.deleteCharAt(currentTargetIndex - 1);
                currentTargetIndex--;
                distance = deletion;
                output.append(transformedWord.toString()).append(System.lineSeparator());
            } else if (insertion < distance) {
                transformedWord.insert(currentTargetIndex, source.charAt(currentSourceIndex - 1));
                currentSourceIndex--;
                distance = insertion;
                output.append(transformedWord.toString()).append(System.lineSeparator());
            } else {
                currentSourceIndex--;
                currentTargetIndex--;
            }
        }
        System.out.println(output.toString());
    }

}
