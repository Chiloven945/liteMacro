package top.ourisland.litemacro.actions;

import top.ourisland.litemacro.runtime.I18n;
import top.ourisland.litemacro.runtime.InvocationContext;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.Optional;

/**
 * Represents an action to transfer a player to another server. This action attempts to transfer the player to a
 * specified target server and optionally sends a message to the player before or after the transfer.
 */
public class TransferAction implements Action {

    /**
     * The name of the target server to transfer the player to.
     */
    private final String targetName;

    /**
     * The message to send to the player before transfer. If null or empty, no message is sent.
     */
    private final String message;

    /**
     * Constructs a new {@code TransferAction} with the given target server name and message.
     *
     * @param targetName the name of the target server to transfer the player to
     * @param message    the message to send to the player before transfer (can be null or empty)
     */
    public TransferAction(String targetName, String message) {
        this.targetName = targetName;
        this.message = message;
    }

    /**
     * Executes the transfer action. This method attempts to transfer the player to the specified target server. If the
     * player is not found or the server cannot be resolved, an appropriate message is sent to the source. If the
     * transfer is successful, the player is connected to the target server.
     *
     * @param ctx the invocation context that provides access to the player, source, and other resources
     */
    @Override
    public void execute(InvocationContext ctx) {
        Optional<Player> pOpt = ctx.player();

        // If no player is found in the context, send a message to the source and return
        if (pOpt.isEmpty()) {
            ctx.source().sendMessage(I18n.lang("litemacro.action.transfer.need_player"));
            return;
        }

        // Resolve the target server name and check if it exists
        String resolved = ctx.replacePlaceholders(targetName);
        Optional<RegisteredServer> srvOpt = ctx.server().getServer(resolved);

        // If the server is not found, send a message to the source and return
        if (srvOpt.isEmpty()) {
            ctx.source().sendMessage(I18n.lang("litemacro.action.transfer.server_not_found", resolved));
            return;
        }

        Player player = pOpt.get();

        // If a message is provided, send it to the player
        if (message != null && !message.isBlank()) {
            player.sendMessage(I18n.prefix(ctx.replacePlaceholders(message)));
        }

        // Resolve the target server and initiate the transfer
        RegisteredServer server = srvOpt.get();
        player.createConnectionRequest(server)
                .connect()
                .whenComplete((result, err) -> {
                    // If an error occurs during connection, send an error message to the source
                    if (err != null) {
                        ctx.source().sendMessage(I18n.lang("litemacro.action.transfer.failed_to_connect", err.getMessage()));
                        return;
                    }
                    // If the transfer result is not "SUCCESS", notify the source
                    try {
                        if (!"SUCCESS".equals(String.valueOf(result.getStatus()))) {
                            ctx.source().sendMessage(I18n.lang("litemacro.action.transfer.failed", result.getStatus()));
                        }
                    } catch (Throwable t) {
                        // If there is an unexpected error, show the result status
                        ctx.source().sendMessage(I18n.lang("litemacro.action.transfer.result", result));
                    }
                });
    }
}
