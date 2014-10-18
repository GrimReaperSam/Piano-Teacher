package midi.common.service;

public enum Difficulty {
    HIGHEST(5), HIGH(4), NORMAL(3), LOW(2), LOWEST(1);

    private Integer value;

    private Difficulty(Integer value) {
        this.value = value;
    }

    public static Difficulty fromInt(Integer value) {
        for (Difficulty difficulty : Difficulty.values()) {
            if (difficulty.value.equals(value)) {
                return difficulty;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }
}
