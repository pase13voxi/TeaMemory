package coolpharaoh.tee.speicher.tea.timer.core.settings

enum class SortMode(val text: String, val choice: Int) {
    LAST_USED("last_used", 0),
    ALPHABETICAL("alphabetical", 1),
    BY_VARIETY("by_variety", 2),
    RATING("rating", 3);

    companion object {
        @JvmStatic
        fun fromText(text: String?): SortMode {
            for (sortModeSetting in values()) {
                if (sortModeSetting.text.equals(text, ignoreCase = true)) {
                    return sortModeSetting
                }
            }
            return LAST_USED
        }

        @JvmStatic
        fun fromChoice(choice: Int): SortMode {
            for (sortModeSetting in values()) {
                if (sortModeSetting.choice == choice) {
                    return sortModeSetting
                }
            }
            return LAST_USED
        }
    }
}