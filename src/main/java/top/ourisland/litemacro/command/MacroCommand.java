package top.ourisland.litemacro.command;

import top.ourisland.litemacro.actions.Action;
import top.ourisland.litemacro.actions.ActionFactory;
import top.ourisland.litemacro.config.model.ActionSpec;
import top.ourisland.litemacro.config.model.MacroSpec;
import top.ourisland.litemacro.runtime.I18n;
import top.ourisland.litemacro.runtime.InvocationContext;
import top.ourisland.litemacro.runtime.MacroRunner;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Velocity {@code SimpleCommand} implementation that binds a macro specification
 * to an executable command. Handles argument capture, placeholder variables,
 * and permission checks.
 */
public class MacroCommand implements SimpleCommand {
    private final ProxyServer server;
    private final Logger logger;
    private final MacroSpec spec;
    private final List<Action> actions;
    private final Object plugin;

    /**
     * Constructs a macro command from the given specification.
     *
     * @param server the Velocity proxy server
     * @param logger the logger
     * @param spec   the macro specification
     * @param plugin the plugin instance
     */
    public MacroCommand(ProxyServer server, Logger logger, Object plugin, MacroSpec spec) {
        this.server = server;
        this.logger = logger;
        this.plugin = plugin;
        this.spec = spec;
        this.actions = buildActions(spec.getActions());
    }

    /**
     * Builds the ordered list of concrete actions from the action specs.
     *
     * @param specs action specifications (may be null)
     * @return ordered, non-null list of actions
     */
    private List<Action> buildActions(List<ActionSpec> specs) {
        if (specs == null) return Collections.emptyList();
        return specs.stream().map(ActionFactory::fromSpec).collect(Collectors.toList());
    }

    /**
     * Executes the macro: captures arguments into variables (arg0, arg1, ...),
     * creates an {@code InvocationContext}, and starts the {@code MacroRunner}.
     *
     * @param invocation the command invocation
     */
    @Override
    public void execute(Invocation invocation) {
        Map<String, String> vars = new HashMap<>();
        String[] args = invocation.arguments();
        for (int i = 0; i < args.length; i++) vars.put("arg" + i, args[i]);

        InvocationContext ctx = new InvocationContext(server, invocation.source(), vars, plugin);
        if (actions.isEmpty()) {
            invocation.source().sendMessage(I18n.lang("litemacro.command.macro.no_actions"));
            return;
        }
        new MacroRunner(ctx, actions).start();
    }

    /**
     * Checks whether the source has the required permission from the macro spec.
     * A blank or null permission means the macro is public.
     *
     * @param invocation the command invocation
     * @return true if permitted; false otherwise
     */
    @Override
    public boolean hasPermission(Invocation invocation) {
        String perm = spec.getPermission();
        return perm == null || perm.isBlank() || invocation.source().hasPermission(perm);
    }
}