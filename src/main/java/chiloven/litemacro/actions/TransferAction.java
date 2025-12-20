package chiloven.litemacro.actions;

import chiloven.litemacro.runtime.I18n;
import chiloven.litemacro.runtime.InvocationContext;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

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
            ctx.source().sendMessage(I18n.lang("litemacro.action.transfer.need_player"));
            return;
        }
        String resolved = ctx.replacePlaceholders(targetName);
        Optional<RegisteredServer> srvOpt = ctx.server().getServer(resolved);
        if (srvOpt.isEmpty()) {
            ctx.source().sendMessage(I18n.lang("litemacro.action.transfer.server_not_found", resolved));
            return;
        }

        Player player = pOpt.get();
        if (message != null && !message.isBlank()) {
            player.sendMessage(I18n.prefix(ctx.replacePlaceholders(message)));
        }

        RegisteredServer server = srvOpt.get();
        player.createConnectionRequest(server)
                .connect()
                .whenComplete((result, err) -> {
                    if (err != null) {
                        ctx.source().sendMessage(I18n.lang("litemacro.action.transfer.failed_to_connect", err.getMessage()));
                        return;
                    }
                    try {
                        if (!"SUCCESS".equals(String.valueOf(result.getStatus()))) {
                            ctx.source().sendMessage(I18n.lang("litemacro.action.transfer.failed", result.getStatus()));
                        }
                    } catch (Throwable t) {
                        ctx.source().sendMessage(I18n.lang("litemacro.action.transfer.result", result));
                    }
                });
    }
}