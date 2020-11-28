package util;

import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class FilePath {

    private FilePath() {}

    public static Path getFilePath( String fileName ) throws IOException {

        return Path.of("build","resources", "main", fileName).toRealPath();
    }

    public static Path getInFilePath() {

        return Path.of("build", "resources", "main").toAbsolutePath();
    }

    public static String caseFormatter(String inputString) {

        String expression = "\\b(.)(.*?)\\b";

        if( inputString.contains("_"))
            inputString = inputString.split("_")[0] + " " + inputString.split("_")[1];

        return Pattern.compile(expression).matcher(inputString.toLowerCase()).replaceAll(
                matchResult -> matchResult.group(1).toUpperCase() + matchResult.group(2));
    }
}
