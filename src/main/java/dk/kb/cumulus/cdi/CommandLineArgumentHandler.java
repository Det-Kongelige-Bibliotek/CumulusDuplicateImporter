package dk.kb.cumulus.cdi;

import dk.kb.cumulus.utils.ArgumentCheck;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Collection;

/**
 * Commandline argument handler.
 */
public class CommandLineArgumentHandler {
    /** The parser of the command line arguments.*/
    protected final CommandLineParser parser;
    /** The options for the command line arguments*/
    protected final Options options;
    /** The command line. */
    protected CommandLine cmd = null;

    /**
     * Constructor.
     */
    public CommandLineArgumentHandler() {
        parser = new DefaultParser();
        options = new Options();
    }

    /**
     * Parses the commandline arguments.
     * @param args The command line arguments to pass.
     * @throws ParseException If the arguments does not parse. E.g. arguments missing or too many arguments.
     */
    public void parseArguments(String ... args) throws ParseException {
        cmd = parser.parse(options, args, Constants.NOT_ALLOWING_UNDEFINED_ARGUMENTS);
    }

    /**
     * For validating that the command line has been instantiated.
     */
    protected void ensureThatCmdHasBeenInitialised() {
        ArgumentCheck.checkNotNull(cmd, "No argument has been parsed from the command line.");
    }

    /**
     * Creates the default options for the command line arguments for the clients.
     */
    public void createDefaultOptions() {
        Option verbosity = new Option(Constants.VERBOSITY_ARG, false, "Makes the client more verbose");
        verbosity.setRequired(Constants.ARGUMENT_IS_NOT_REQUIRED);
        options.addOption(verbosity);
    }

    /**
     * @param optionName The name of the option to extract the value for.
     * @return The value corresponding to the given option name.
     */
    public String getOptionValue(String optionName) {
        ensureThatCmdHasBeenInitialised();
        return cmd.getOptionValue(optionName);
    }

    /**
     * @param optionName The name of the option to validate whether exists.
     * @return Whether any arguments for the options have been given.
     */
    public boolean hasOption(String optionName) {
        ensureThatCmdHasBeenInitialised();
        return cmd.hasOption(optionName);
    }

    /**
     * @return Lists the possible arguments in a human readable format.
     */
    public String listArguments() {
        StringBuilder res = new StringBuilder();
        res.append("Takes the following arguments:\n");
        for(Option option : (Collection<Option>) options.getOptions()) {
            res.append("-" + option.getOpt() + " " + option.getDescription() + "\n");
        }
        return res.toString();
    }

    /**
     * Creates and adds a new option.
     * @param optionParameter The option parameter.
     * @param hasArgument Whether or not the option takes an argument.
     * @param optRequired Whether or not the option is required.
     * @param description The description of the option.
     */
    public void addOption(String optionParameter, boolean hasArgument, boolean optRequired, String description) {
        Option option = new Option(optionParameter, hasArgument, description);
        option.setRequired(optRequired);
        options.addOption(option);
    }
}
