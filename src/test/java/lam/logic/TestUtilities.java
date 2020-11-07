package lam.logic;

import org.jboss.logging.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtilities {

    private static final Logger LOG = Logger.getLogger(InputManipulatorTest.class);
    private static final String invalidFilesPath = "invalids/";
    private static final String validFilesPath = "valids/";
    private final ClassLoader classLoader = getClass().getClassLoader();

    String getValid(String name) {
        return readFile(name, validFilesPath);
    }
    List<String> getValids() {
        return getInFolder(validFilesPath);
    }
    List<String> getInvalids() {
        return getInFolder(invalidFilesPath);
    }

    @NotNull
    private List<String> getInFolder(String path) {
        return Arrays.stream(new File(classLoader.getResource(path).getFile()).list())
                .map(f -> readFile(f, path))
                .collect(Collectors.toList());
    }
    private String readFile(String fileName, String path) {
        try (var inputStream = classLoader.getResourceAsStream(path + fileName))  {
            if (inputStream != null)
                return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Failed to read file " + fileName, e);
        }
        throw new IllegalArgumentException(fileName + " was obviously an incorrect file name ! :) ");
    }
}
