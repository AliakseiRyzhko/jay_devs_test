public class MinimalDistance {
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

    public static String[] insertIntoArray(final String[] originalArray, final int insertionIndex, final String newItem) {
        int originalLength = originalArray.length;
        String[] newArray = new String[originalLength + 1];
        System.arraycopy(originalArray, 0, newArray, 0, insertionIndex);
        newArray[insertionIndex] = newItem;
        System.arraycopy(originalArray, insertionIndex, newArray, insertionIndex + 1, originalLength - insertionIndex);

        return newArray;
    }

    public static void minimalDistance(final String source, final String target) {
        int sourceLength = source.length();
        int targetLength = target.length();
        int[][] editDistanceMatrix = new int[sourceLength + 1][targetLength + 1];

        for (int i = 0; i <= sourceLength; i++) {
            editDistanceMatrix[i][0] = i;
        }
        for (int j = 0; j <= targetLength; j++) {
            editDistanceMatrix[0][j] = j;
        }

        for (int i = 1; i <= sourceLength; i++) {
            for (int j = 1; j <= targetLength; j++) {
                int deletion = editDistanceMatrix[i][j - 1] + 1;
                int insertion = editDistanceMatrix[i - 1][j] + 1;
                int substitution = editDistanceMatrix[i - 1][j - 1] + (source.charAt(i - 1) == target.charAt(j - 1) ? 0 : 1);
                editDistanceMatrix[i][j] = Math.min(Math.min(deletion, insertion), substitution);
            }
        }

        int distance = editDistanceMatrix[sourceLength][targetLength];
        System.out.println("Minimum Edit Distance: " + distance);

        int currentSourceIndex = sourceLength;
        int currentTargetIndex = targetLength;
        String[] transformedWord = target.split("");

        System.out.println(String.join("", transformedWord));
        while (distance > 0) {
            int deletion = editDistanceMatrix[currentSourceIndex][currentTargetIndex - 1];
            int insertion = editDistanceMatrix[currentSourceIndex - 1][currentTargetIndex];
            int substitution = editDistanceMatrix[currentSourceIndex - 1][currentTargetIndex - 1];

            if (substitution < distance) {
                transformedWord[currentTargetIndex - 1] = Character.toString(source.charAt(currentSourceIndex - 1));
                currentSourceIndex--;
                currentTargetIndex--;
                distance = substitution;
                System.out.println(String.join("", transformedWord));
            } else if (deletion < distance) {
                transformedWord[currentTargetIndex - 1] = "";
                currentTargetIndex--;
                distance = deletion;
                System.out.println(String.join("", transformedWord));
            } else if (insertion < distance) {
                transformedWord = insertIntoArray(transformedWord, currentTargetIndex, Character.toString(source.charAt(currentSourceIndex - 1)));
                currentSourceIndex--;
                distance = insertion;
                System.out.println(String.join("", transformedWord));
            } else {
                currentSourceIndex--;
                currentTargetIndex--;
            }
        }
    }
}
