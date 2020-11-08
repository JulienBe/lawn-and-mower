package lam;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import lam.logic.InputManipulator;
import lam.logic.InputParser;
import lam.logic.OverallCommander;
import lam.records.MowingSession;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@QuarkusMain
public class CliEntryPoint implements QuarkusApplication {

    private static final Logger LOG = Logger.getLogger(CliEntryPoint.class);
    private final String defaultFile = "given_test";

    @Override
    public int run(String... args) throws Exception {
        LOG.info("Starting LAWN AND MOWER!");
        LOG.info("CLI MODE");
        LOG.info("Args : " + Arrays.toString(args));
        List<String> files = new ArrayList<>();
        if (args == null || args.length == 0) {
            files.add(readFileFromResources(defaultFile, ""));
        } else {
            Arrays.stream(args)
                    .parallel()
                    .forEach(this::runFile);
        }
        files
                .stream()
                .filter(this::validateInput)
                .forEach(this::runCommander);
        return 0;
    }

    private boolean validateInput(String s) {
        try {
            InputManipulator.validateInput(s);
        } catch (IllegalArgumentException e) {
            LOG.error("Input between those ''' was invalid \n'''" + s + "'''\n ", e);
        }
        return true;
    }

    private void runCommander(String s) {
        LOG.debug("Commander starting on " + s);
        MowingSession session = InputParser.init(s);
        OverallCommander commander = new OverallCommander(session);
        LOG.info("FOR INPUT \n" + s + "\n\n" + processExecResult(commander));
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

    private void runFile(String file) {
        LOG.info("Running file " + file);
    }
}
