package api;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadFile {

    private ReadFile(){}

    private static Path getFilePath( String fileName ) throws IOException {

        return Path.of("build","resources", "main", fileName).toRealPath();
    }

    public static InputStream getDataStream( String fileName ) throws IOException {

        return Files.newInputStream( getFilePath(fileName) );
    }
}
