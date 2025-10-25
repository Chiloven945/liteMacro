package chiloven.litemacro.config.model;

import java.util.List;

/**
 * Macro specification: description, permission, optional aliases,
 * and a list of action specifications.
 */
public class MacroSpec {
    private String description;
    private String permission;
    private List<String> aliases;
    private List<ActionSpec> actions;

    /**
     * @return human-readable description of the macro
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description human-readable description of the macro
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return permission node required to execute this macro (nullable/blank means everyone)
     */
    public String getPermission() {
        return permission;
    }

    /**
     * @param permission permission node required to execute this macro
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }

    /**
     * @return list of additional aliases for the macro
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * @param aliases list of additional macro aliases
     */
    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    /**
     * @return ordered action specifications for this macro
     */
    public List<ActionSpec> getActions() {
        return actions;
    }

    /**
     * @param actions ordered action specifications for this macro
     */
    public void setActions(List<ActionSpec> actions) {
        this.actions = actions;
    }
}