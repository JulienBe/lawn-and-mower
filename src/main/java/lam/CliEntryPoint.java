package lam;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import lam.logic.OverallCommander;
import lam.logic.SessionCreatorAutomata;
import lam.records.MowingSession;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.FileWriter;
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
    private static final String DEFAULT_FILE_FOLDER = "/opt/lawn_and_mower/";
    private static final String DEFAULT_FILE_FOLDER_INPUTS = DEFAULT_FILE_FOLDER + "/inputs";
    private static final String DEFAULT_FILE_FOLDER_OUTPUTS = DEFAULT_FILE_FOLDER + "/outputs";
    private static final String DEFAULT_FILE = "given_test";

    @Override
    public int run(String... args) throws Exception {
        LOG.info("\n\tStarting LAWN AND MOWER!");
        LOG.info("CLI MODE");
        LOG.info("Args : " + Arrays.toString(args));
        List<FileNameContent> files = new ArrayList<>();

        var outputFolder = false;
        if (args == null || args.length == 0) {
            addFilesFromDefaultFolder(files);
            files.add(readFileFromResources(DEFAULT_FILE, ""));
            if (files.size() != 1)
                outputFolder = true;
        } else {
            readFilePassedAsArgs(files, args);
        }
        boolean finalOutputFolder = outputFolder;
        files.forEach(f -> runCommander(f, finalOutputFolder));
        return 0;
    }

    private void readFilePassedAsArgs(List<FileNameContent> files, String[] args) {
        Arrays.stream(args)
                .parallel()
                .forEach(arg ->
                        arg.lines()
                                .map(line -> new FileNameContent(line, readFile(line)))
                                .forEach(files::add));
    }

    private void addFilesFromDefaultFolder(List<FileNameContent> files) {
        try {
            File f = new File(DEFAULT_FILE_FOLDER_INPUTS);
            if (f != null) {
                String[] filesInFolder = f.list();
                for (String s : filesInFolder) {
                    files.add(new FileNameContent(s ,readFile(DEFAULT_FILE_FOLDER_INPUTS + "/" + s)));
                }
            }
        } catch (Exception e) {
            LOG.error("Failed to reading file from " + DEFAULT_FILE_FOLDER_INPUTS, e);
        }
    }

    private void runCommander(FileNameContent fileNameContent, boolean finalOutputFolder) {
        try {
            LOG.debug("Commander starting on " + fileNameContent.name());
            MowingSession session = new SessionCreatorAutomata().processInput(fileNameContent.content());
            OverallCommander commander = new OverallCommander(session);
            String result = processExecResult(commander);
            LOG.info("\n" + result);
            if (finalOutputFolder)
                writeOutput(fileNameContent, result);
        } catch (Exception e) {
            LOG.error("Unexpected issue during processing of " + fileNameContent.name());
        }
    }

    private void writeOutput(FileNameContent s, String result) {
        try (FileWriter writer = new FileWriter(DEFAULT_FILE_FOLDER_OUTPUTS + "/" + s.name())) {
            writer.write(result);
        } catch (Exception e) {
            LOG.error("\nFailed to write output for " + s.name(), e);
        }
    }

    private String processExecResult(OverallCommander commander) {
        return commander.exec()
                .stream()
                .map(e -> e + "\n")
                .reduce("", String::concat);
    }

    private FileNameContent readFileFromResources(String fileName, String path) {
        LOG.debug("Loading file from resources: " + path + fileName);
        var classLoader = CliEntryPoint.class.getClassLoader();
        try (var inputStream = classLoader.getResourceAsStream(path + fileName))  {
            if (inputStream != null)
                return new FileNameContent(fileName, new String(inputStream.readAllBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Failed to reading file " + fileName, e);
        }
        throw new IllegalArgumentException(fileName + " was obviously an incorrect file name ! :) ");
    }

    private String readFile(String file) {
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

// Just so I am able to write with the same name for the output
record FileNameContent(String name, String content) {}