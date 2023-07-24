package coolpharaoh.tee.speicher.tea.timer.core.settings

enum class DarkMode(val text: String, val choice: Int) {
    SYSTEM("system", 0),
    ENABLED("enabled", 1),
    DISABLED("disabled", 2);

    companion object {
        @JvmStatic
        fun fromText(text: String?): DarkMode {
            for (darkModeSetting in values()) {
                if (darkModeSetting.text.equals(text, ignoreCase = true)) {
                    return darkModeSetting
                }
            }
            return SYSTEM
        }

        @JvmStatic
        fun fromChoice(choice: Int): DarkMode {
            for (darkModeSetting in values()) {
                if (darkModeSetting.choice == choice) {
                    return darkModeSetting
                }
            }
            return SYSTEM
        }
    }
}