package chiloven.litemacro.runtime;

import chiloven.litemacro.actions.Action;

import java.time.Duration;
import java.util.List;

/**
 * Executes a list of actions sequentially, honoring per-action delays via Velocity's scheduler to avoid blocking the
 * main thread.
 */
public class MacroRunner {
    private final InvocationContext ctx;
    private final List<Action> actions;

    public MacroRunner(InvocationContext ctx, List<Action> actions) {
        this.ctx = ctx;
        this.actions = actions;
    }

    /**
     * Starts executing the configured action sequence.
     */
    public void start() {
        runStep(0);
    }

    /**
     * Executes the action at the given index and schedules the next step, applying any delay requested by the current
     * action.
     *
     * @param idx zero-based index of the action to run
     */
    private void runStep(int idx) {
        if (idx >= actions.size()) return;
        Action a = actions.get(idx);
        long delay = a.delayMillis();
        Runnable step = () -> {
            try {
                a.execute(ctx);
            } catch (Throwable ignored) {
            }
            runStep(idx + 1);
        };
        if (delay > 0) {
            ctx.server().getScheduler()
                    .buildTask(ctx.plugin(), scheduled -> step.run())
                    .delay(Duration.ofMillis(delay))
                    .schedule();

        } else {
            step.run();
        }
    }
}