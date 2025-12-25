package top.ourisland.litemacro.actions;

import top.ourisland.litemacro.runtime.InvocationContext;

/**
 * Action representing a non-blocking delay between actions.
 * Execution itself is a no-op; the delay is communicated via {@link #delayMillis()}.
 */
public class DelayAction implements Action {
    private final long millis;

    /**
     * Constructs a delay action.
     *
     * @param millis non-negative delay duration in milliseconds
     */
    public DelayAction(long millis) {
        this.millis = Math.max(0L, millis);
    }

    /**
     * No-op; the scheduler uses {@link #delayMillis()} to pause the sequence.
     *
     * @param ctx the invocation context (unused)
     */
    @Override
    public void execute(InvocationContext ctx) { /* no-op */ }

    /**
     * @return the configured delay in milliseconds
     */
    @Override
    public long delayMillis() {
        return millis;
    }
}