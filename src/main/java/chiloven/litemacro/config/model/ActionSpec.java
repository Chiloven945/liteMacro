package chiloven.litemacro.config.model;

import java.util.Map;

/**
 * Action specification: type discriminator and an open-ended options map
 * containing the type-specific fields (e.g., cmd, run_as, text, millis).
 */
public class ActionSpec {
    private String type; // command / message / delay
    private Map<String, Object> options; // 其余键值（cmd, run_as, text, millis 等）

    /**
     * @return action type (e.g., "command", "message", "delay")
     */
    public String getType() {
        return type;
    }

    /**
     * @param type action type discriminator
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return options map with type-specific keys and values
     */
    public Map<String, Object> getOptions() {
        return options;
    }

    /**
     * @param options options map with type-specific keys and values
     */
    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }
}