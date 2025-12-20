package chiloven.litemacro.config.model;

import java.util.Map;

/**
 * Root configuration model holding all macros.
 */
public class RootConfig {
    private Map<String, MacroSpec> macros;
    private Map<String, String> placeholders;
    private String lang;

    /**
     * Gets the macro map.
     *
     * @return macros keyed by name, or null if unset
     */
    public Map<String, MacroSpec> getMacros() {
        return macros;
    }

    /**
     * Sets the macro map.
     *
     * @param macros macros keyed by name
     */
    public void setMacros(Map<String, MacroSpec> macros) {
        this.macros = macros;
    }

    /**
     * Gets the placeholders map.
     *
     * @return placeholders keyed by name, or null if unset
     */
    public Map<String, String> getPlaceholders() {
        return placeholders;
    }

    /**
     * Sets the placeholders map.
     *
     * @param placeholders placeholders keyed by name
     */
    public void setPlaceholders(Map<String, String> placeholders) {
        this.placeholders = placeholders;
    }

    /**
     * Get the specified language.
     *
     * @return the language code
     */
    public String getLang() {
        return lang;
    }

    /**
     * Set the specified language
     *
     * @param lang the language code to be specified
     */
    public void setLang(String lang) {
        this.lang = lang;
    }
}