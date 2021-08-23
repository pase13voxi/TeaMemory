package coolpharaoh.tee.speicher.tea.timer.core.actual_settings;

public enum SortMode {
    LAST_USED(0),
    ALPHABETICAL(1),
    BY_VARIETY(2),
    RATING(3);

    private final int index;

    SortMode(final int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static SortMode fromIndex(final int index) {
        for (final SortMode sortMode : SortMode.values()) {
            if (sortMode.index == index) {
                return sortMode;
            }
        }
        return LAST_USED;
    }
}
