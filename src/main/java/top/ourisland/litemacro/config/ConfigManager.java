package top.ourisland.litemacro.config;

import top.ourisland.litemacro.config.model.MacroSpec;
import top.ourisland.litemacro.config.model.RootConfig;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Map;

/**
 * Loads and manages the plugin configuration (command.yml). Responsible for creating a default file when missing and
 * providing a typed view of macro specifications.
 */
public class ConfigManager {
    private final Logger logger;
    private final ProxyServer server;
    private final Path dataDir;
    private RootConfig root;

    /**
     * Constructs a ConfigManager.
     *
     * @param logger  the logger to use
     * @param server  the proxy server instance
     * @param dataDir the data directory path
     */
    public ConfigManager(Logger logger, ProxyServer server, Path dataDir) {
        this.logger = logger;
        this.server = server;
        this.dataDir = dataDir;
    }

    /**
     * Loads the configuration from disk, creating a default file when absent.
     * Performs light validation (e.g., warning on empty action lists).
     *
     * @throws java.io.IOException if disk access fails
     */
    public void loadOrCreate() throws IOException {
        Path file = dataDir.resolve("command.yml");
        if (!Files.exists(file)) {
            writeDefault(file);
        }
        try (InputStream in = Files.newInputStream(file)) {
            Yaml yaml = new Yaml(new Constructor(RootConfig.class));
            RootConfig loaded = yaml.load(in);
            if (loaded == null || loaded.getMacros() == null) {
                root = new RootConfig();
                root.setMacros(Collections.emptyMap());
            } else {
                root = loaded;
            }
            for (Map.Entry<String, MacroSpec> e : root.getMacros().entrySet()) {
                if (e.getValue().getActions() == null || e.getValue().getActions().isEmpty()) {
                    logger.warn("Macro '{}' has no actions.", e.getKey());
                }
            }
        }
    }

    /**
     * Writes a minimal default configuration to the given path.
     * Intended for first-run initialization.
     *
     * @param file the target path for the default YAML
     * @throws java.io.IOException if writing fails
     */
    private void writeDefault(Path file) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("command.yml")) {
            if (inputStream == null) {
                throw new IOException("Default resource file 'command.yml' not found in resources.");
            }
            Files.copy(inputStream, file, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Returns the specified language to use. Default is en_US.
     *
     * @return the language code
     */
    public String getLang() {
        if (root == null || root.getLang() == null || root.getLang().isBlank()) {
            return "en_US";
        }
        return root.getLang();
    }

    /**
     * Returns the current map of macro specifications keyed by macro name.
     *
     * @return immutable or read-only view of macros; never null
     */
    public Map<String, MacroSpec> getMacros() {
        return root == null || root.getMacros() == null ? Collections.emptyMap() : root.getMacros();
    }
}
