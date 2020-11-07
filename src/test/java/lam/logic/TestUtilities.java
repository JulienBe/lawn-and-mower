package lam.logic;

import lam.Mower;
import lam.enums.Direction;
import lam.records.Coordinate;
import lam.records.Lawn;
import lam.records.MowingSession;
import org.jboss.logging.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static lam.enums.Instruction.*;

public class TestUtilities {

    private static final Logger LOG = Logger.getLogger(InputManipulatorTest.class);
    private static final String invalidFilesPath = "invalids/";
    private static final String validFilesPath = "valids/";
    private final ClassLoader classLoader = getClass().getClassLoader();

    public static MowingSession getExampleSession() {
        Lawn lawn = new Lawn(6, 6);
        Mower mower1 = new Mower(new Coordinate(1, 2), Direction.N, Arrays.asList(L,F,L,F,L,F,L,F,F), lawn);
        Mower mower2 = new Mower(new Coordinate(3, 3), Direction.E, Arrays.asList(F,F,R,F,F,R,F,R,R,F), lawn);
        return new MowingSession(lawn, Arrays.asList(mower1, mower2));
    }

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
