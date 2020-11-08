package lam;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import lam.logic.OverallCommander;
import lam.logic.SessionCreatorAutomata;
import lam.records.MowingSession;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@QuarkusMain
public class CliEntryPoint implements QuarkusApplication {

    private static final Logger LOG = Logger.getLogger(CliEntryPoint.class);
    private final String defaultFile = "given_test";

    @Override
    public int run(String... args) throws Exception {
        LOG.info("\n\tStarting LAWN AND MOWER!");
        LOG.info("CLI MODE");
        LOG.info("Args : " + Arrays.toString(args));
        List<String> files = new ArrayList<>();
        if (args == null || args.length == 0) {
            files.add(readFileFromResources(defaultFile, ""));
        } else {
            Arrays.stream(args)
                    .parallel()
                    .forEach(arg ->
                            arg.lines().map(this::runFile).forEach(files::add));
        }
        files.forEach(this::runCommander);
        return 0;
    }

    private void runCommander(String s) {
        try {
            LOG.debug("Commander starting on " + s);
            MowingSession session = new SessionCreatorAutomata().processInput(s);
            OverallCommander commander = new OverallCommander(session);
            LOG.info("\n" + processExecResult(commander));
        } catch (Exception e) {
            LOG.error("Unexpected issue during processing of " + s);
        }
    }

    private String processExecResult(OverallCommander commander) {
        return commander.exec()
                .stream()
                .map(e -> e + "\n")
                .reduce("", String::concat);
    }

    private String readFileFromResources(String fileName, String path) {
        LOG.debug("Loading file from resources: " + path + fileName);
        var classLoader = CliEntryPoint.class.getClassLoader();
        try (var inputStream = classLoader.getResourceAsStream(path + fileName))  {
            if (inputStream != null)
                return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Failed to read file " + fileName, e);
        }
        throw new IllegalArgumentException(fileName + " was obviously an incorrect file name ! :) ");
    }

    private String runFile(String file) {
        file = file.trim();
        LOG.info("Running file " + file);
        if (file != null) {
            try {
                Path path = Paths.get(file);
                if (path != null) {
                    return new String(Files.readAllBytes(path));
                } else {
                    throw new Throwable("Path was null");
                }
            } catch (Throwable e) {
                LOG.error("There was an issue read file", e);
            }
        }
        LOG.error("Return invalid and will be discarded");
        return "invalid and will be discarded";
    }
}
