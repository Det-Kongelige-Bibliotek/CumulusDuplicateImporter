package dk.kb.cumulus.cdi;

/**
 * Class for containing all the constants, which could be used across classes.
 */
public class Constants {

    /** For not allowing undefined arguments when parsing of arguments.*/
    public static final Boolean NOT_ALLOWING_UNDEFINED_ARGUMENTS = false;
    /** If a given option has an argument.*/
    public static final Boolean HAS_ARGUMENT = true;
    /** If a given option dies not have an argument.*/
    public static final Boolean NO_ARGUMENT = false;
    /** If an argument is required.*/
    public static final Boolean ARGUMENT_IS_REQUIRED = true;
    /** If an argument is not required.*/
    public static final Boolean ARGUMENT_IS_NOT_REQUIRED = false;


    /** For making the client verbose.*/
    public static final String VERBOSITY_ARG = "v";

    /** Argument for the Cumulus server URL.*/
    public static final String ARG_SERVER = "s";
    /** Argument for the Cumulus user name.*/
    public static final String ARG_USERNAME = "u";
    /** Argument for the Cumulus user password.*/
    public static final String ARG_PASSWORD = "p";
    /** Argument for the Cumulus catalog.*/
    public static final String ARG_CATALOG = "c";
    /** Argument for the input file.*/
    public static final String ARG_FILE = "f";


    /** The exit code when success.*/
    public static final int EXIT_SUCCESS = 0;
    /** The exit code, when the client fails due to argument failure.*/
    public static final int EXIT_ARGUMENT_FAILURE = 1;
    /** The exit code, when the client fails due to operation failure.*/
    public static final int EXIT_OPERATION_FAILURE = -1;
}
