package dk.kb.cumulus.cdi;

import dk.kb.cumulus.utils.ArgumentCheck;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.jaccept.structure.ExtendedTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CommandLineArgumentHandlerTest extends ExtendedTestCase {

    @Test
    public void testInstantiation() {
        CommandLineArgumentHandler c = new CommandLineArgumentHandler();
        Assert.assertTrue(c instanceof CommandLineArgumentHandler);
        Assert.assertNotNull(c.options);
        Assert.assertNotNull(c.parser);
    }

    @Test
    public void testParseArgumentsSuccessWithNoArguments() throws Exception {
        CommandLineArgumentHandler c = new CommandLineArgumentHandler();
        c.parseArguments(new String[0]);
    }

    @Test(expectedExceptions = MissingOptionException.class)
    public void testParseArgumentsFailureWithNoArguments() throws Exception {
        CommandLineArgumentHandler c = new CommandLineArgumentHandler();
        c.addOption("a", true, true, "TEST");
        c.parseArguments(new String[0]);
    }

    @Test(expectedExceptions = UnrecognizedOptionException.class)
    public void testParseArgumentsFailureBadArgument() throws Exception {
        CommandLineArgumentHandler c = new CommandLineArgumentHandler();
        c.parseArguments("-aTEST");
    }

    @Test
    public void testEnsureThatCmdHasBeenInitialisedSuccess() throws Exception {
        CommandLineArgumentHandler c = new CommandLineArgumentHandler();
        c.parseArguments(new String[0]);
        c.ensureThatCmdHasBeenInitialised();
    }

    @Test(expectedExceptions = ArgumentCheck.class)
    public void testEnsureThatCmdHasBeenInitialisedFailure() throws Exception {
        CommandLineArgumentHandler c = new CommandLineArgumentHandler();
        c.ensureThatCmdHasBeenInitialised();
    }

    @Test
    public void testDefaultOptions() throws Exception {
        CommandLineArgumentHandler c = new CommandLineArgumentHandler();
        c.createDefaultOptions();

        c.parseArguments(new String[0]);
        Assert.assertNull(c.getOptionValue(Constants.VERBOSITY_ARG));

//        c.parseArguments("-" + Constants.VERBOSITY_ARG, "true");
//        Assert.assertNotNull(c.getOptionValue(Constants.VERBOSITY_ARG));
    }
}
