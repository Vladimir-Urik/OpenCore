package lol.gggedr.opencore.api.modules.economy.cons;

public class Economy {

    private final String name;
    private final String format;
    private final long defaultValue;
    private final long min;
    private final long max;

    public Economy(String name, String format, long defaultValue, long min, long max) {
        this.name = name;
        this.format = format;
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public long getDefaultValue() {
        return defaultValue;
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }
}
