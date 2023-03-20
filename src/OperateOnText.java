import java.util.ArrayList;
import java.util.List;

public class OperateOnText extends RabinKarp {

    ArrayList<String> textArray;
    public static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    StringBuilder markedText = new StringBuilder();


    public OperateOnText(ArrayList<String> textArray) {
        this.textArray = textArray;
    }

    void printMarkedText(String pattern, ArrayList<String> arrayOfPatterns) {

        int hashedPattern = hashing(pattern);
        setHashFirst(0);
        int startIteration = 0;
        int hashedSubStr;
        for (int iterator = 0; iterator < textArray.size(); iterator++) {
            String str = textArray.get(iterator);
            if (str.length() > pattern.length()) {

                String subStr = str.substring(0, pattern.length());
                hashedSubStr = hashing(subStr);
                char lastChar;
                for (int i = 0; i <= str.length() - pattern.length(); i++) {

                    subStr = str.substring(i, pattern.length() + i);
                    if (i > 0) {
                        lastChar = str.charAt(pattern.length() - 1 + i);
                        hashedSubStr = rollingHash(subStr, lastChar);
                    }

                    if (hashedSubStr == hashedPattern && subStr.equals(pattern)) {
                        boolean contains = checkNextStr(iterator, arrayOfPatterns);
                        if (contains) {
                            createMarkedText(startIteration, iterator, arrayOfPatterns);
                            startIteration = ++iterator;
                            break;
                        }
                    }
                }
            } else if (str.length() == pattern.length()) {
                hashedSubStr = hashing(str);
                if (hashedSubStr == hashedPattern && str.equals(pattern)) {

                    boolean contains = checkNextStr(iterator, arrayOfPatterns);
                    if (contains) {
                        createMarkedText(startIteration, iterator, arrayOfPatterns);
                        startIteration = ++iterator;
                    }
                }
            }
            if (iterator == textArray.size() - 1 && startIteration <= textArray.size() - arrayOfPatterns.size() - 2) {
                markedText.append(listToString(textArray.subList(startIteration, textArray.size())));
            }
        }

        System.out.println(markedText);
    }

    void createMarkedText(int startIteration, int startMarkedIndex, ArrayList<String> patternsArray) {

        int numOfPatterns = patternsArray.size();
        for (int iterator = startIteration; iterator < textArray.size(); iterator++) {

            String str = textArray.get(iterator);
            if (iterator < startMarkedIndex) {
                markedText.append(str).append(" ");
            } else if (iterator <= startMarkedIndex + numOfPatterns - 1) {
                if (iterator == startMarkedIndex) {
                    markedText.append(getMarkedWord(str, patternsArray.get(0))).append(" ");
                } else if (iterator == startMarkedIndex + numOfPatterns - 1) {
                    markedText.append(getMarkedWord(str, patternsArray.get(patternsArray.size() - 1))).append(" ");
                } else {
                    markedText.append(ANSI_BLUE).append(str).append(ANSI_RESET).append(" ");
                }
            }
        }
    }

    String getMarkedWord(String word, String pattern) {

        StringBuilder markedWord = new StringBuilder();
        int hashedPattern = hashing(pattern);
        setHashFirst(0);
        boolean isSameWord = false;

        String subWord = word.substring(0, pattern.length());
        int hashedSubWord = hashing(subWord);
        char lastChar;

        for (int i = 0; i <= word.length() - pattern.length(); i++) {
            subWord = word.substring(i, pattern.length() + i);

            if (i > 0) {
                lastChar = word.charAt(pattern.length() - 1 + i);
                hashedSubWord = rollingHash(subWord, lastChar);
            }

            if (hashedSubWord == hashedPattern && subWord.equals(pattern)) {
                isSameWord = true;
                markedWord.append(ANSI_BLUE).append(subWord).append(ANSI_RESET);
                if (!word.substring(i + 1).contains(pattern)) {
                    markedWord.append(word.substring(i + pattern.length()));
                } else {
                    int nextMatch = word.substring(i + 1).indexOf(pattern);
                    markedWord.append(word, i + pattern.length(), i + 1 + nextMatch);
                }
            } else if (i == word.length() - pattern.length() && !isSameWord) {
                markedWord.append(word.substring(i));
            } else if (!isSameWord) {
                markedWord.append(subWord.charAt(0));
            }

        }

        return markedWord.toString();
    }

    private boolean checkNextStr(int index, ArrayList<String> arrayOfPatterns) {

        int numOfPatterns = arrayOfPatterns.size();
        int i = 1;
        while (i <= numOfPatterns - 1) {
            if (index + i <= textArray.size() - 1 && !textArray.get(index + i).contains(arrayOfPatterns.get(i))) {
                return false;
            }
            i++;
        }

        return true;
    }

    private static String listToString(List<String> list) {

        StringBuilder result = new StringBuilder();
        for (String s : list) {
            result.append(s).append(" ");
        }

        return result.toString();
    }

}
