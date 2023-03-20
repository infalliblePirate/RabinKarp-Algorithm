import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("enter the path to your text");
        String filePath = scanner.nextLine();
        System.out.println("enter the pattern");
        String pattern = scanner.nextLine();

        String text = FileUtil.reader(filePath);
        ArrayList<String> patternsArray = getPatternArray(pattern);
        ArrayList<String> textArray = textArray(text);

        OperateOnText operate = new OperateOnText(textArray);
        operate.printMarkedText(patternsArray.get(0), patternsArray);
    }

    private static ArrayList<String> getPatternArray(String p) {

        return new ArrayList<>(List.of(p.split(" ")));
    }

    private static ArrayList<String> textArray(String text) {

        text = text.replace("\n", " \n");
        String[] arrayOfText = text.split(" ");

        return new ArrayList<>(List.of(arrayOfText));
    }

}
