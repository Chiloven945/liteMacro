package top.ourisland.litemacro;

import top.ourisland.litemacro.command.MacroCommand;
import top.ourisland.litemacro.config.ConfigManager;
import top.ourisland.litemacro.config.model.MacroSpec;
import top.ourisland.litemacro.runtime.I18n;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Core plugin entry for liteMarco.
 * <p>
 * Handles plugin initialization, configuration loading, macro command registration/unregistration, and the admin reload
 * command.
 */
@Plugin(
        id = "litemacro",
        name = "liteMacro",
        version = BuildConstants.VERSION,
        description = "A lite-weight macro command plugin for velocity.",
        url = "https://github.com/Our-Island/liteMacro",
        authors = {"OurIsland", "Chiloven945"}
)
public class LiteMacro {

    private final Map<String, List<String>> registeredAliases = new HashMap<>(); // macroName -> aliases
    @Inject
    private Logger logger;
    @Inject
    private ProxyServer server;
    @Inject
    private CommandManager commandManager;
    @Inject
    @DataDirectory
    private Path dataDirectory;
    private ConfigManager configManager;

    /**
     * Velocity lifecycle hook invoked when the proxy is initializing. Loads or creates the configuration, then
     * registers all macro commands and the admin command.
     *
     * @param event the initialize event fired by Velocity
     */
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        try {
            logger.info("Initializing liteMarco v{}...\n{}", BuildConstants.VERSION, """
                    ##\\ ##\\   ##\\               ##\\      ##\\                                        \s
                    ## |\\__|  ## |              ###\\    ### |                                       \s
                    ## |##\\ ######\\    ######\\  ####\\  #### | ######\\   #######\\  ######\\   ######\\ \s
                    ## |## |\\_##  _|  ##  __##\\ ##\\##\\## ## | \\____##\\ ##  _____|##  __##\\ ##  __##\\\s
                    ## |## |  ## |    ######## |## \\###  ## | ####### |## /      ## |  \\__|## /  ## |
                    ## |## |  ## |##\\ ##   ____|## |\\#  /## |##  __## |## |      ## |      ## |  ## |
                    ## |## |  \\####  |\\#######\\ ## | \\_/ ## |\\####### |\\#######\\ ## |      \\######  |
                    \\__|\\__|   \\____/  \\_______|\\__|     \\__| \\_______| \\_______|\\__|       \\______/\s"""
            );
            if (!Files.exists(dataDirectory)) {
                Files.createDirectories(dataDirectory);
            }
            this.configManager = new ConfigManager(logger, server, dataDirectory);
            this.configManager.loadOrCreate();

            I18n.init(configManager, logger);

            registerAllMacros();
            registerAdminCommand();
            logger.info("liteMarco initialized. Macros registered: {}", registeredAliases.keySet());
        } catch (Exception e) {
            logger.error("Failed to initialize liteMarco", e);
        }
    }

    /**
     * (Re)registers all macros from the current configuration. First unregisters any previously registered macro
     * aliases to ensure a clean state, then registers each macro command.
     */
    private void registerAllMacros() {
        unregisterAllMacros();
        Map<String, MacroSpec> macros = configManager.getMacros();
        if (macros.isEmpty()) {
            logger.warn("No macros found in command.yml");
            return;
        }
        macros.forEach(this::registerOneMacro);
    }

    /**
     * Registers the admin command "/litemacro" which currently supports the subcommand "reload" to hot-reload
     * configuration and macros.
     */
    private void registerAdminCommand() {
        CommandMeta meta = commandManager.metaBuilder("litemacro")
                .plugin(this)
                .build();

        commandManager.register(meta, (SimpleCommand) invocation -> {
            if (!invocation.source().hasPermission("litemacro.admin")) {
                invocation.source().sendMessage(I18n.lang("litemacro.main.no_perms", "litemacro.admin"));
                return;
            }
            String[] args = invocation.arguments();
            if (args.length == 0 || !"reload".equalsIgnoreCase(args[0])) {
                invocation.source().sendMessage(I18n.lang("litemacro.main.usage", "/litemacro reload"));
                return;
            }
            try {
                unregisterAllMacros();

                configManager.loadOrCreate();

                String lang = configManager.getLang();
                I18n.loadResourceBundle(lang);

                registerAllMacros();

                invocation.source().sendMessage(I18n.lang("litemacro.main.reload"));
            } catch (Exception e) {
                invocation.source().sendMessage(I18n.lang("litemacro.main.reload.failed", e.getMessage()));
                logger.error("Reload failed", e);
            }
        });
    }

    /**
     * Unregisters all previously registered macro aliases. Safe to call repeatedly; no-op if nothing is registered.
     */
    private void unregisterAllMacros() {
        if (registeredAliases.isEmpty()) return;
        for (List<String> aliases : registeredAliases.values()) {
            for (String a : aliases) {
                try {
                    commandManager.unregister(a);
                } catch (Throwable ignored) {
                }
            }
        }
        registeredAliases.clear();
    }

    /**
     * Registers a single macro command and all of its aliases.
     *
     * @param name the primary macro name
     * @param spec the macro specification loaded from configuration
     */
    private void registerOneMacro(String name, MacroSpec spec) {
        String primary = name.toLowerCase(Locale.ROOT);
        List<String> aliases = new ArrayList<>();
        aliases.add(primary);
        if (spec.getAliases() != null) {
            spec.getAliases().forEach(a -> aliases.add(a.toLowerCase(Locale.ROOT)));
        }

        MacroCommand command = new MacroCommand(server, logger, this, spec);
        for (String alias : aliases) {
            CommandMeta meta = commandManager.metaBuilder(alias)
                    .plugin(this)
                    .build();
            commandManager.register(meta, command);
        }
        registeredAliases.put(primary, aliases);
        logger.info("Registered macro '{}' with aliases {} and perm '{}'", primary, aliases, spec.getPermission());
    }
}