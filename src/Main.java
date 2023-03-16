import java.beans.PropertyEditorSupport;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {

    private static final RabinKarp rabinKarp = new RabinKarp();
    private static ArrayList<String> patternsArray;
    private static ArrayList<String> unmodifiedTextArray;
    public static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";


    public static void main(String[] args) {

        String file = "data.txt";
        System.out.println(fileReader(file));

        String pattern = "e";
        patternsArray = getPatternArray(pattern);
        System.out.println(patternsArray);

        ArrayList<String> array = getArray(file);
        unmodifiedTextArray = getUnmodifiedArrayText(file);
        rabinKarp.hashing("lack tea");
        findMatchAndPrint(array, patternsArray.get(0), patternsArray);

    }

    private static void findMatchAndPrint(ArrayList<String> text, String pattern, ArrayList<String> arrayOfPatterns) {

        int hashedPattern = rabinKarp.hashing(pattern);
        rabinKarp.setHashFirst(0);
        int startIteration = 0;
        for (int iterator = 0; iterator < unmodifiedTextArray.size(); iterator++) {
            String str = unmodifiedTextArray.get(iterator);
            if (str.length() > pattern.length()) {

                String subStr = str.substring(0, pattern.length());
                int hashedSubStr = rabinKarp.hashing(subStr);
                char lastChar;
                for (int i = 0; i <= str.length() - pattern.length(); i++) {

                    subStr = str.substring(i, pattern.length() + i);
                    if (i > 0) {
                        lastChar = str.charAt(pattern.length() - 1 + i);
                        hashedSubStr = rabinKarp.rollingHash(subStr, lastChar);
                    }

                    if (hashedSubStr == hashedPattern && subStr.equals(pattern)) {
                        boolean contains = checkNextStr(iterator, text, arrayOfPatterns);
                        if (contains) {
                            startIteration = printMarkedText(startIteration, iterator, arrayOfPatterns);
                            //iterator = startIteration;
                            break;
                        }
                    }
                }
            } else if (str.length() == pattern.length()) {
                int hashedStr = rabinKarp.hashing(str);
                if (hashedStr == hashedPattern && str.equals(pattern)) {

                    boolean contains = checkNextStr(iterator, text, arrayOfPatterns);
                    if (contains) {
                        startIteration = printMarkedText(startIteration, iterator, arrayOfPatterns);
                        //iterator = startIteration ;
                    }
                }
            }
            if (iterator == unmodifiedTextArray.size() - 1 && startIteration <= unmodifiedTextArray.size() - arrayOfPatterns.size() - 2) {
                System.out.print(listToString(unmodifiedTextArray.subList(startIteration, unmodifiedTextArray.size())));

            }
            // returns -1 ...
            /*if (iterator < 0) {
                return;
            }*/

        }

    }

    private static ArrayList<String> getUnmodifiedArrayText(String file) {

        String str = fileReader(file);
        str = str.replace("\n", " \n");
        String[] arrayOfText = str.split(" ");

        return new ArrayList<>(List.of(arrayOfText));
    }

    ////!!!!!!!!!!! change arrayList to unmodified text
    // mb somenthing is wrong with numOFPatterns
    private static int printMarkedText(int startIteration, int startMarkedIndex, ArrayList<String> patternsArray) {
        //int iterator = startIteration;
        int numOfPatterns = patternsArray.size();
        for (int iterator = startIteration; iterator < unmodifiedTextArray.size(); iterator++) {

            String str = unmodifiedTextArray.get(iterator);
            //str = str.replace("\n", "");
            if (iterator < startMarkedIndex) {
                System.out.print(str + " ");
            } else if (iterator <= startMarkedIndex + numOfPatterns - 1) {
                if (iterator == startMarkedIndex) {
                    System.out.print(getMarkedWord(str, patternsArray.get(0)) + " ");
                    if (numOfPatterns == 1) {
                        return ++iterator;
                    }
                } else if (iterator == startMarkedIndex + numOfPatterns - 1) {
                    System.out.print(getMarkedWord(str, patternsArray.get(patternsArray.size() - 1)) + " ");
                    return ++iterator;
                } else {
                    System.out.print(ANSI_BLUE + str + ANSI_RESET + " ");
                }
            }
        }

        return 0;
    }

    private static String getMarkedWord(String word, String pattern) {

        StringBuilder markedWord = new StringBuilder();
        int hashedPattern = rabinKarp.hashing(pattern);
        rabinKarp.setHashFirst(0);
        boolean isSameWord = false;

        if (word.length() >= pattern.length()) {

            String subWord = word.substring(0, pattern.length());
            int hashedSubWord = rabinKarp.hashing(subWord);
            char lastChar;
            for (int i = 0; i <= word.length() - pattern.length(); i++) {
                subWord = word.substring(i, pattern.length() + i);

                if (i > 0) {
                    lastChar = word.charAt(pattern.length() - 1 + i);
                    hashedSubWord = rabinKarp.rollingHash(subWord, lastChar);
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
        } else {
            markedWord = new StringBuilder(ANSI_BLUE + word + ANSI_RESET);
        }

        return markedWord.toString();
    }

    private static boolean checkNextStr(int index, ArrayList<String> text, ArrayList<String> arrayOfPatterns) {

        int numOfPatterns = arrayOfPatterns.size();
        int i = 1;
        while (i <= numOfPatterns - 1) {
            if (index + i <= unmodifiedTextArray.size() - 1 && !unmodifiedTextArray.get(index + i).contains(arrayOfPatterns.get(i))) {
                return false;
            }
            i++;
        }

        return true;
    }


    private static ArrayList<String> getPatternArray(String pattern) {

        return new ArrayList<>(List.of(pattern.split(" ")));
    }

    private static ArrayList<String> getArray(String file) {

        String str = fileReader(file);
        str = str.replaceAll("\\s+", " ").replaceAll("\n", " ").replaceAll("\\p{Punct}", "");
        String[] arrayOfText = str.split(" ");

        return new ArrayList<>(List.of(arrayOfText));
    }

    private static void fileWriter(String file, String data) {

        try (FileWriter fw = new FileWriter(file)) {
            fw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String fileReader(String file) {

        String dataText = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            dataText = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataText;
    }

    private static String listToString(List<String> list) {

        StringBuilder result = new StringBuilder();
        for (String s : list) {
            result.append(s).append(" ");
        }
        return result.toString();
    }
}
