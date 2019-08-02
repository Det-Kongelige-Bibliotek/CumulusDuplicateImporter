package dk.kb.cumulus.cdi;

import dk.kb.cumulus.utils.ArgumentCheck;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;

/**
 * Main class for handling the arguments, perform the operation, etc.
 */
public class CumulusDuplicateImporter {
    /**
     * Main method for instantiating this commandline client.
     * @param args The arguments.
     */
    public static void main(String ... args) {
        CumulusDuplicateImporter cdi = null;
        try {
            cdi = new CumulusDuplicateImporter(args);
        } catch (Exception e) {
            System.err.println("Failed to import duplicates due to argument failure.");
            e.printStackTrace(System.err);
            System.exit(Constants.EXIT_ARGUMENT_FAILURE);
        }

        OutputHandler outputHandler = OutputHandler.instantiate(cdi.isVerbose());
        outputHandler.info("Importing the duplicates");

        try {
            cdi.performDuplicationImport();
        } catch (Exception e) {
            outputHandler.warning("Error occurred while importing the file.", e);
            System.exit(Constants.EXIT_OPERATION_FAILURE);
        }

        outputHandler.debug("Successfully imported the duplicates!!");
        System.exit(Constants.EXIT_SUCCESS);
    }


    /** The handler for the command line arguments.*/
    protected final CommandLineArgumentHandler cmdHandler;
    /** Cumulus Client.*/
    protected final CumulusClient cumulusClient;
    /** The duplicate file handler.*/
    protected final DuplicateFileHandler duplicateFileHandler;
    /** The file with the duplicates.*/
    protected final File duplicateFile;

    /**
     * Constructor.
     * @param args The arguments from the commandline.
     * @throws ParseException If it fails to parse the arguments.
     */
    protected CumulusDuplicateImporter(String ... args) throws ParseException {
        cmdHandler = new CommandLineArgumentHandler();
        createCmdOptions();
        cmdHandler.parseArguments(args);

        duplicateFile = new File(cmdHandler.getOptionValue(Constants.ARG_FILE));
        ArgumentCheck.checkExistsNormalFile(duplicateFile, "File inputFile");

        cumulusClient = new CumulusClient(cmdHandler.getOptionValue(Constants.ARG_SERVER),
                cmdHandler.getOptionValue(Constants.ARG_USERNAME),
                cmdHandler.getOptionValue(Constants.ARG_PASSWORD),
                cmdHandler.getOptionValue(Constants.ARG_CATALOG));

        duplicateFileHandler = new DuplicateFileHandler(cumulusClient);
    }

    /**
     * Perform the operation of importing the duplicate and setting the relations for the grouped records.
     * @throws IOException If it fails to read/write data.
     */
    protected void performDuplicationImport() throws IOException {
        duplicateFileHandler.handleFile(duplicateFile);
    }

    /**
     * Creates the options for the commandline argument handler.
     */
    protected void createCmdOptions() {
        cmdHandler.createDefaultOptions();

        cmdHandler.addOption(Constants.ARG_SERVER, Constants.HAS_ARGUMENT, Constants.ARGUMENT_IS_REQUIRED,
                "The server url for the Cumulus server.");
        cmdHandler.addOption(Constants.ARG_USERNAME, Constants.HAS_ARGUMENT, Constants.ARGUMENT_IS_REQUIRED,
                "The username for accessing the Cumulus server.");
        cmdHandler.addOption(Constants.ARG_PASSWORD, Constants.HAS_ARGUMENT, Constants.ARGUMENT_IS_REQUIRED,
                "The user password for accessing the Cumulus server.");
        cmdHandler.addOption(Constants.ARG_CATALOG, Constants.HAS_ARGUMENT, Constants.ARGUMENT_IS_REQUIRED,
                "The name of the Cumulus catalog.");
        cmdHandler.addOption(Constants.ARG_FILE, Constants.HAS_ARGUMENT, Constants.ARGUMENT_IS_REQUIRED,
                "The path to the file to import.");
    }

    /**
     * @return Whether or not it has been set to verbose.
     */
    public boolean isVerbose() {
        return cmdHandler.hasOption(Constants.VERBOSITY_ARG);
    }
}
