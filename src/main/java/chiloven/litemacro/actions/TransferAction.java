package chiloven.litemacro.actions;

import chiloven.litemacro.runtime.InvocationContext;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;

import java.util.Optional;

public class TransferAction implements Action {
    private final String targetName;
    private final String message;

    public TransferAction(String targetName, String message) {
        this.targetName = targetName;
        this.message = message;
    }

    @Override
    public void execute(InvocationContext ctx) {
        Optional<Player> pOpt = ctx.player();
        if (pOpt.isEmpty()) {
            ctx.source().sendMessage(Component.text("This action requires a player."));
            return;
        }
        String resolved = ctx.replacePlaceholders(targetName);
        Optional<RegisteredServer> srvOpt = ctx.server().getServer(resolved);
        if (srvOpt.isEmpty()) {
            ctx.source().sendMessage(Component.text("Server not found: " + resolved));
            return;
        }

        Player player = pOpt.get();
        if (message != null && !message.isBlank()) {
            player.sendMessage(Component.text(ctx.replacePlaceholders(message)));
        }

        RegisteredServer server = srvOpt.get();
        player.createConnectionRequest(server)
                .connect()
                .whenComplete((result, err) -> {
                    if (err != null) {
                        ctx.source().sendMessage(Component.text("Failed to connect: " + err.getMessage()));
                        return;
                    }
                    // Only report on non-success statuses (SUCCESS means connected)
                    try {
                        // Most Velocity versions expose a SUCCESS status
                        if (!"SUCCESS".equals(String.valueOf(result.getStatus()))) {
                            ctx.source().sendMessage(Component.text("Transfer failed: " + result.getStatus()));
                        }
                    } catch (Throwable t) {
                        // Fallback: always print status just in case
                        ctx.source().sendMessage(Component.text("Transfer result: " + result));
                    }
                });
    }
}