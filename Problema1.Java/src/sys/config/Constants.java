package sys.config;

public interface Constants {

    interface File {

        String CONFIG_FILE_NAME = "config.properties";

        String OUTPUT_FILENAME_SUFIX = "-decrypted";

    }

    interface ErrorCodes {

        String ERROR_CODE_1 = "[Error 1: No runtime path.]";

        String ERROR_CODE_2 = "[Error 2: Couldn't create files in PC. Check write permissions.]";

        String ERROR_CODE_3 = "[Error 3: Couldn't read file. Restart the program.]";

        String ERROR_CODE_4 = "[Error 4: Chosen file not accomplish the input format.]";

        String ERROR_CODE_5 = "[Error 4: Result file couldn't be created.]";

    }

}
