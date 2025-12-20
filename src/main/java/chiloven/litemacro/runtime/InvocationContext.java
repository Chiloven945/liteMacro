package chiloven.litemacro.runtime;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Per-invocation context passed to actions, providing access to the server, the command source, and a bag of variables
 * used for placeholder expansion.
 *
 * @param server the proxy server
 * @param source the command source invoking the macro
 * @param vars   a map of variable names to values for placeholder replacement
 * @param plugin the plugin instance
 */
public record InvocationContext(ProxyServer server,
                                CommandSource source,
                                Map<String, String> vars,
                                Object plugin) {
    /**
     * Expands known placeholders (e.g., {player}, {uuid}, and arbitrary {argN}) within the supplied string using the
     * current context variables.
     *
     * @param s the input string with placeholders
     * @return the string with placeholders replaced
     */
    public String replacePlaceholders(String s) {
        if (s == null) return "";
        String out = s;
        String name = player().map(Player::getUsername).orElse("CONSOLE");
        String uuid = player().map(Player::getUniqueId).map(UUID::toString).orElse("-");
        out = out.replace("{player}", name).replace("{uuid}", uuid);
        if (vars != null) {
            for (Map.Entry<String, String> e : vars.entrySet()) {
                out = out.replace("{" + e.getKey() + "}", e.getValue());
            }
        }
        return out;
    }

    /**
     * Returns the player if the source is a player.
     *
     * @return an Optional containing the player or empty for non-player sources
     */
    public Optional<Player> player() {
        return source instanceof Player p ? Optional.of(p) : Optional.empty();
    }
}