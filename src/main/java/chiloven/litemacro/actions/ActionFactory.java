package chiloven.litemacro.actions;

import chiloven.litemacro.config.model.ActionSpec;

import java.util.Locale;
import java.util.Map;

/**
 * Factory for constructing {@code Action} instances from declarative
 * {@code ActionSpec} definitions.
 */
public class ActionFactory {
    /**
     * Creates an {@code Action} from the given specification.
     * Supports "command", "message", and "delay" types.
     *
     * @param spec the action specification
     * @return a concrete {@code Action}
     * @throws IllegalArgumentException if the type is unknown or invalid
     */
    public static Action fromSpec(ActionSpec spec) throws IllegalArgumentException {
        String t = spec.getType();
        if (t == null) throw new IllegalArgumentException("Action type is null");
        String type = t.toLowerCase(Locale.ROOT);
        Map<String, Object> opt = spec.getOptions();
        switch (type) {
            case "command" -> {
                String cmd = str(opt, "cmd", "");
                String runAs = str(opt, "run_as", "console");
                return new CommandAction(cmd, runAs);
            }
            case "message" -> {
                String text = str(opt, "text", "");
                return new MessageAction(text);
            }
            case "delay" -> {
                long ms = num(opt, "millis", 0L);
                return new DelayAction(ms);
            }
            case "transfer" -> {
                String target = str(opt, "target", "");
                String msg = str(opt, "message", "");
                return new TransferAction(target, msg);
            }
            default -> throw new IllegalArgumentException("Unknown action type: " + type);
        }
    }

    /**
     * Helper to extract a string value from the options map with a default.
     *
     * @param m   the options map
     * @param k   the key
     * @param def the default value if key is missing or null
     * @return the string value
     */
    private static String str(Map<String, Object> m, String k, String def) {
        Object v = m == null ? null : m.get(k);
        return v == null ? def : String.valueOf(v);
    }

    /**
     * Helper to extract a numeric (long) value from the options map with a default.
     *
     * @param m   the options map
     * @param k   the key
     * @param def the default value if key is missing, null, or unparsable
     * @return the long value
     */
    private static long num(Map<String, Object> m, String k, long def) {
        Object v = m == null ? null : m.get(k);
        if (v == null) return def;
        if (v instanceof Number) return ((Number) v).longValue();
        try {
            return Long.parseLong(String.valueOf(v));
        } catch (Exception e) {
            return def;
        }
    }
}