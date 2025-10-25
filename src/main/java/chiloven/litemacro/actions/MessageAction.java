package chiloven.litemacro.actions;

import chiloven.litemacro.runtime.InvocationContext;
import net.kyori.adventure.text.Component;

/**
 * Action that sends a chat message to the invoking {@code CommandSource}.
 * Supports placeholder expansion.
 */
public class MessageAction implements Action {
    private final String text;

    /**
     * Constructs a message action.
     *
     * @param text the message template to send
     */
    public MessageAction(String text) {
        this.text = text;
    }

    /**
     * Sends the message to the source after expanding placeholders.
     *
     * @param ctx the invocation context
     */
    @Override
    public void execute(InvocationContext ctx) {
        ctx.source().sendMessage(Component.text(ctx.replacePlaceholders(text)));
    }
}