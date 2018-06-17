package examples;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    private FileUtils() {

    }

    public static Path toResourcePath(String fileName, Class context) {
        try {
            final URI fileUri = context.getClassLoader().getResource(fileName).toURI();
            return Paths.get(fileUri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeToFile(String content, String fileName) {
        final Path destinationPath = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(destinationPath)) {

            writer.write(content);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
