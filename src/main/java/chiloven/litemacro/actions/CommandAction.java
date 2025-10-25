package chiloven.litemacro.actions;

import chiloven.litemacro.runtime.InvocationContext;
import com.velocitypowered.api.command.CommandSource;

/**
 * Action that executes a Velocity command either as console or the invoking player.
 * Placeholders in the raw command are expanded using the {@code InvocationContext}.
 */
public class CommandAction implements Action {
    private final String rawCommand;
    private final String runAs; // console / player

    /**
     * Constructs a command action.
     *
     * @param rawCommand the command to execute (without leading slash)
     * @param runAs      either "console" or "player"
     */
    public CommandAction(String rawCommand, String runAs) {
        this.rawCommand = rawCommand;
        this.runAs = runAs == null ? "console" : runAs.toLowerCase();
    }

    /**
     * Executes the configured command asynchronously using Velocity's command manager.
     *
     * @param ctx the invocation context (used for placeholder expansion and source)
     */
    @Override
    public void execute(InvocationContext ctx) {
        String cmd = ctx.replacePlaceholders(rawCommand);
        CommandSource source = "player".equals(runAs) && ctx.player().isPresent() ? ctx.player().get() : ctx.server().getConsoleCommandSource();
        ctx.server().getCommandManager().executeAsync(source, cmd);
    }
}