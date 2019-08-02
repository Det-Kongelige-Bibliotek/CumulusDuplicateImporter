package dk.kb.cumulus.cdi;

import dk.kb.cumulus.utils.ArgumentCheck;

/**
 * The output handler.
 * Simple logging mechanism for standard out/err.
 */
public class OutputHandler {

    /** The singleton of this output handler.*/
    private static OutputHandler singleton;

    /**
     * Instantiates the singleton for this class.
     * @param isVerbose Whether or not the output-handler is verbose.
     * @return The newly instantiated output-handler.
     */
    public static OutputHandler instantiate(boolean isVerbose) {
        if(singleton == null) {
            throw new ArgumentCheck("Cannot re-instantiate singleton");
        }
        singleton = new OutputHandler(isVerbose);
        return singleton;
    }

    /**
     * Retrieves the instantiated singleton of this output handler.
     * The output-handler must have been instantiated before this retrieval.
     * @return The output-handler singleton.
     */
    public static OutputHandler getSingleton() {
        ArgumentCheck.checkNotNull(singleton, "OutputHandler singleton");
        return singleton;
    }


    /** Whether or not this output-handler is verbose (thus writing debug messages).*/
    protected final boolean isVerbose;

    /**
     * Constructor.
     * @param isVerbose Whether or not to write debug messages.
     */
    public OutputHandler(boolean isVerbose) {
        this.isVerbose = isVerbose;
    }

    /**
     * Write debug message (only if it is verbose).
     * @param output The output message to write.
     */
    public void debug(String output) {
        debug(output, null);
    }

    /**
     * Write debug message and throwable (only if it is verbose).
     * @param output The output message to write.
     * @param throwable The exception or error attached to the message.
     */
    public void debug(String output, Throwable throwable) {
        if(isVerbose) {
            System.out.println(output);
            if(throwable != null) {
                throwable.printStackTrace(System.out);
            }
        }
    }

    /**
     * Write info message.
     * @param output The output message to write.
     */
    public void info(String output) {
        info(output, null);
    }

    /**
     * Write info message and throwable.
     * @param output The output message to write.
     * @param throwable The exception or error attached to the message.
     */
    public void info(String output, Throwable throwable) {
        System.out.println(output);
        if(throwable != null) {
            throwable.printStackTrace(System.out);
        }
    }

    /**
     * Write warning message.
     * @param output The output message to write.
     */
    public void warning(String output) {
        warning(output, null);
    }

    /**
     * Write warning message and throwable.
     * @param output The output message to write.
     * @param throwable The exception or error attached to the message.
     */
    public void warning(String output, Throwable throwable) {
        System.err.println(output);
        if(throwable != null) {
            throwable.printStackTrace(System.err);
        }
    }
}
