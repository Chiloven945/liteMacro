package top.ourisland.litemacro.actions;

import top.ourisland.litemacro.runtime.InvocationContext;

/**
 * Executable unit within a macro. Implementations must avoid blocking the
 * main thread; any waiting should be modeled via {@link #delayMillis()} and
 * external scheduling.
 */
public interface Action {
    /**
     * Performs the action using the provided invocation context.
     * Implementations should be exception-safe; errors should not stop the sequence.
     *
     * @param ctx the invocation context containing server, source, and variables
     */
    void execute(InvocationContext ctx);

    /**
     * Returns a requested delay (in milliseconds) that should occur before
     * the next action is executed. Non-delay actions return 0.
     *
     * @return delay in milliseconds, or 0 for no delay
     */
    default long delayMillis() {
        return 0L;
    }
}