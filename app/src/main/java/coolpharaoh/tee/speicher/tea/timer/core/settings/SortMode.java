package coolpharaoh.tee.speicher.tea.timer.core.settings;

public enum SortMode {
    LAST_USED("last_used", 0),
    ALPHABETICAL("alphabetical", 1),
    BY_VARIETY("by_variety", 2),
    RATING("rating", 3);

    private final String text;
    private final int choice;

    SortMode(final String text, final int choice) {
        this.text = text;
        this.choice = choice;
    }

    public String getText() {
        return text;
    }

    public int getChoice() {
        return choice;
    }

    public static SortMode fromText(final String text) {
        for (final SortMode sortModeSetting : SortMode.values()) {
            if (sortModeSetting.text.equalsIgnoreCase(text)) {
                return sortModeSetting;
            }
        }
        return LAST_USED;
    }

    public static SortMode fromChoice(final int choice) {
        for (final SortMode sortModeSetting : SortMode.values()) {
            if (sortModeSetting.choice == choice) {
                return sortModeSetting;
            }
        }
        return LAST_USED;
    }
}
