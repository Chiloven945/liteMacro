package top.ourisland.litemacro.runtime;

import top.ourisland.litemacro.config.ConfigManager;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * The I18n class is responsible for handling internationalization (i18n) of the plugin. It loads the appropriate
 * language resource bundle based on the configuration and provides methods to fetch localized strings with or without
 * placeholders.
 */
public class I18n {

    /**
     * The current resource bundle that contains localized strings.
     */
    private static ResourceBundle resourceBundle;

    /**
     * The logger used for logging warnings and errors related to localization.
     */
    private static Logger logger;

    /**
     * The ConfigManager instance used to load configuration settings, including the language setting.
     */
    private static ConfigManager configManager;

    /**
     * Initializes the I18n class by setting up the ConfigManager and Logger instances. It loads the appropriate
     * resource bundle based on the language specified in the configuration.
     *
     * @param configManager the ConfigManager instance used to load the language setting
     * @param logger        the Logger instance used for logging
     */
    public static void init(ConfigManager configManager, Logger logger) {
        I18n.configManager = configManager;
        I18n.logger = logger;
        loadResourceBundle(configManager.getLang());
    }

    /**
     * Loads the resource bundle for the specified language. If the language cannot be loaded, it falls back to the
     * default language (en_US).
     *
     * @param lang the language code (e.g., "en_US", "zh_CN") used to load the corresponding resource bundle
     */
    public static void loadResourceBundle(String lang) {
        try {
            // Load the resource bundle for the specified language
            resourceBundle = ResourceBundle.getBundle("lang/" + lang);
        } catch (Exception e) {
            // Fall back to the default language (en_US) if loading fails
            resourceBundle = ResourceBundle.getBundle("lang/en_US");
            logger.warn("Failed to load language '{}'. Falling back to default language 'en_US'.", lang);
        }
    }

    /**
     * Retrieves the localized string for the given key, formatted with the specified arguments. The string is prefixed
     * with the "litemacro.prefix" value from the resource bundle.
     *
     * @param key  the key for the localized string
     * @param args the arguments to replace placeholders in the string
     * @return the formatted Component containing the localized string with prefix
     */
    public static Component lang(String key, Object... args) {
        String formattedMessage = getString("litemacro.prefix") + MessageFormat.format(getString(key), args);
        return Component.text(formattedMessage);
    }

    /**
     * Retrieves the localized string for the given key. The string is prefixed with the "litemacro.prefix" value from
     * the resource bundle.
     *
     * @param key the key for the localized string
     * @return the localized string with prefix
     */
    public static String getString(String key) {
        return resourceBundle.getString(key);
    }

    /**
     * Retrieves the localized string for the given key, formatted with the specified arguments. This method does not
     * add any prefix to the string.
     *
     * @param key  the key for the localized string
     * @param args the arguments to replace placeholders in the string
     * @return the formatted Component containing the localized string
     */
    public static Component langPlain(String key, Object... args) {
        String formattedMessage = MessageFormat.format(getString(key), args);
        return Component.text(formattedMessage);
    }

    /**
     * Retrieves the localized string for the given key. This method does not add any prefix to the string.
     *
     * @param key the key for the localized string
     * @return the localized string
     */
    public static Component langPlain(String key) {
        return Component.text(getString(key));
    }

    /**
     * Retrieves a localized string for the prefix defined in the resource bundle, then concatenates
     * it with the provided text.
     *
     * @param text the text to concatenate with the prefix
     * @return a Component containing the localized prefix followed by the provided text
     */
    public static Component prefix(String text) {
        return Component.text(getString("litemacro.prefix") + text);
    }
}
