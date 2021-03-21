package coolpharaoh.tee.speicher.tea.timer.core.actualsettings;

public enum DarkMode {
    SYSTEM("system", 0),
    ENABLED("enabled", 1),
    DISABLED("disabled", 2);

    private final String text;
    private final int choice;

    DarkMode(final String text, final int choice) {
        this.text = text;
        this.choice = choice;
    }

    public String getText() {
        return text;
    }

    public int getChoice() {
        return choice;
    }

    public static DarkMode fromText(final String text) {
        for (final DarkMode darkModeSetting : DarkMode.values()) {
            if (darkModeSetting.text.equalsIgnoreCase(text)) {
                return darkModeSetting;
            }
        }
        return SYSTEM;
    }

    public static DarkMode fromChoice(final int choice) {
        for (final DarkMode darkModeSetting : DarkMode.values()) {
            if (darkModeSetting.choice == choice) {
                return darkModeSetting;
            }
        }
        return SYSTEM;
    }
}
