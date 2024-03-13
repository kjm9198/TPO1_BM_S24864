package zad1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Futil {
    public static void processDir(String inputStr, String outputStr) {
        Path outputPath = Paths.get(outputStr);
        Path startingOne = Paths.get(inputStr);

        try {
            OwnFile fileVisitor = new OwnFile(outputPath);
            Files.walkFileTree(startingOne, fileVisitor);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
