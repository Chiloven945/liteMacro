package chiloven.litemacro.config.model;

import java.util.Map;

/**
 * Root configuration model holding all macros.
 */
public class RootConfig {
    private Map<String, MacroSpec> macros;

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
}