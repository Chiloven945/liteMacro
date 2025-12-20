package chiloven.litemacro.runtime;

import chiloven.litemacro.config.ConfigManager;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {

    private static ResourceBundle resourceBundle;
    private static Logger logger;
    private static ConfigManager configManager;

    public static void init(ConfigManager configManager, Logger logger) {
        I18n.configManager = configManager;
        I18n.logger = logger;
        loadResourceBundle(configManager.getLang());
    }

    public static void loadResourceBundle(String lang) {
        try {
            Locale locale = Locale.forLanguageTag(lang);
            resourceBundle = ResourceBundle.getBundle("lang/" + locale.toString());
        } catch (Exception e) {
            resourceBundle = ResourceBundle.getBundle("lang/en_US");
            logger.warn("Failed to load language '{}'. Falling back to default language 'en_US'.", lang);
        }
    }

    public static Component lang(String key, Object... args) {
        String formattedMessage = getString("litemacro.prefix") + MessageFormat.format(getString(key), args);
        return Component.text(formattedMessage);
    }

    public static String getString(String key) {
        return resourceBundle.getString(key);
    }

    public static Component lang(String key) {
        return Component.text(getString("litemacro.prefix") + getString(key));
    }

    public static Component langPlain(String key, Object... args) {
        String formattedMessage = MessageFormat.format(getString(key), args);
        return Component.text(formattedMessage);
    }

    public static Component langPlain(String key) {
        return Component.text(getString(key));
    }

    public static Component prefix(String text) {
        return Component.text(getString("litemacro.prefix") + text);
    }
}
