package domain;

import sys.config.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MainScreenUseCases implements MainScreenUseCasesContract {

    /**
     * Get stored current output path if no exist stored file create new one
     * with JAR runtime executor dir
     *
     * @param callbacks {@link Callbacks} result callback
     */
    @Override
    public void loadOutputPath(Callbacks callbacks) {
        try {
            File configFile = getConfigFile();

            String content = readFile(configFile);

            // if file is empty fill with the default storage directory
            if (!content.isEmpty()) {
                callbacks.onLoadOutputPathSuccess(content);
                return;
            }

            writeDefaultOutput(configFile);
            callbacks.onLoadOutputPathSuccess(readFile(configFile));

        } catch (URISyntaxException e) {
            callbacks.onLoadOutputPathFailure(Constants.ErrorCodes.ERROR_CODE_1);
            e.printStackTrace();

        } catch (IOException e) {
            callbacks.onLoadOutputPathFailure(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Change stored output directory given a path from a directory picker
     *
     * @param newPath   Chosen new path
     * @param callbacks {@link Callbacks} result callback
     */
    @Override
    public void changeOutputPath(String newPath, Callbacks callbacks) {
        try {
            writeInFile(getConfigFile(), newPath);
            callbacks.onChangeOutputPathSuccess(newPath);
        } catch (URISyntaxException e) {
            callbacks.onChangeOutputPathFailure(Constants.ErrorCodes.ERROR_CODE_1);
            e.printStackTrace();

        } catch (IOException e) {
            callbacks.onChangeOutputPathFailure(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void readSelectedFile(String path, Callbacks callbacks) {
        try {
            File file = new File(path);
            String content = readFile(file);
            String[] contentSplit = content.split("\n");

            String formattedContent;
            if ((formattedContent = validateContent(contentSplit)) != null) {
                callbacks.onReadSelectedFileSuccess(
                        path,
                        formattedContent,
                        generateResult(file.getName(), analyzeDocument(formattedContent))
                );
            } else {
                callbacks.onReadSelectedFileFailure(Constants.ErrorCodes.ERROR_CODE_4);
            }
        } catch (IOException e) {
            e.printStackTrace();
            callbacks.onReadSelectedFileFailure(e.getMessage());

        } catch (URISyntaxException e) {
            e.printStackTrace();
            callbacks.onReadSelectedFileFailure(Constants.ErrorCodes.ERROR_CODE_1);
        }
    }

    /**
     * Get the raw config file and if it doesn't exist create a new one
     *
     * @return Config file in {@link File}
     * @throws URISyntaxException delegate from File API
     * @throws IOException        When no permissions allowed
     */
    private File getConfigFile() throws URISyntaxException, IOException {
        // fetch current runtime dir with config file name
        String configFilePath = String.format(
                "%s%s",
                MainScreenUseCases.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(),
                Constants.File.CONFIG_FILE_NAME
        );

        File configFile = new File(configFilePath);

        // check if file exists, if it isn't check if could be created; throw exception
        try {
            if (!configFile.exists() && !configFile.createNewFile())
                throw new IOException(Constants.ErrorCodes.ERROR_CODE_2);
        } catch (SecurityException e) {
            throw new IOException(Constants.ErrorCodes.ERROR_CODE_2);
        }

        return configFile;
    }

    /**
     * Read incoming file line by line joined in a String variable
     *
     * @param file input fie
     * @return String content of input file
     * @throws IOException When file can not be read
     */
    private String readFile(File file) throws IOException {
        try (Stream<String> stream = Files.lines(file.toPath())) {
            // consume all lines in file
            return stream.collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new IOException(Constants.ErrorCodes.ERROR_CODE_3, e.getCause());
        }
    }

    private void writeInFile(File file, String content) throws IOException {
        if (file.isFile()) {
            // fetch default path
            try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
                writer.write(content);
            }
        } else {
            throw new IOException(Constants.ErrorCodes.ERROR_CODE_3);
        }
    }

    private void writeDefaultOutput(File configFile) throws IOException {
        writeInFile(configFile, new File("/").getAbsolutePath());
    }

    private String validateContent(String[] content) {
        List<String> listContent;

        try {
            // remove the possible blank spaces
            listContent = Arrays.stream(content).filter(s -> !s.trim().isEmpty()).collect(Collectors.toList());

            // If there is less than 3 lines
            if (listContent.size() < 4) return null;

            // If there is less than 3 numbers in first position
            if (listContent.get(0).split(" ").length != 3) return null;

            int[] numbers = Arrays.stream(listContent.get(0).split(" ")).mapToInt(Integer::valueOf).toArray();

            if (numbers[0] < 2 || numbers[0] > 50) return null;

            if (numbers[1] < 2 || numbers[1] > 50) return null;

            if (numbers[2] < 3 || numbers[2] > 5000) return null;

            if (numbers[0] != listContent.get(1).length() ||
                    numbers[1] != listContent.get(2).length() ||
                    numbers[2] != listContent.get(3).length())
                return null;

            if (!listContent.get(3).matches("[a-zA-Z0-9]*")) return null;

        } catch (IndexOutOfBoundsException | ClassCastException e) {
            return null;
        }

        return String.join("\n", listContent);
    }

    private String decryptText(String text) {
        char[] chars = text.toCharArray();
        char findingChar = chars[0];

        StringBuilder out = new StringBuilder(String.valueOf(findingChar));

        for (int i = 1; i < chars.length; i++)
            if (findingChar != chars[i]) out.append(findingChar = chars[i]);

        return out.toString();
    }

    private String analyzeDocument(String text) {
        String[] lines = text.split("\n");
        String msgDecrypted = decryptText(lines[3]);

        StringBuilder out = new StringBuilder();

        for (int i = 1; i <= 2; i++)
            out.append("Instruction ").append(lines[i]).append(" ==> ")
                    .append(msgDecrypted.contains(lines[i]) ? "SI" : "NO").append("\n");

        return out.toString();
    }

    private String generateResult(String fileName, String output) throws IOException, URISyntaxException {
        File configFile = getConfigFile();
        String content = readFile(configFile);

        fileName = fileName.substring(0, fileName.indexOf('.'));

        File outPutFile = new File(content + "/" + fileName.concat(Constants.File.OUTPUT_FILENAME_SUFIX) + ".txt");

        if (outPutFile.exists() && outPutFile.delete() && outPutFile.createNewFile())
            writeInFile(outPutFile, output);
        else if (outPutFile.createNewFile())
            writeInFile(outPutFile, output);
        else
            throw new IOException(Constants.ErrorCodes.ERROR_CODE_5);

        return outPutFile.getAbsolutePath();
    }

}
