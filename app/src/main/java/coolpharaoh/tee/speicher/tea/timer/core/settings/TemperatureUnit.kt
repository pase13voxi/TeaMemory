package coolpharaoh.tee.speicher.tea.timer.core.settings;

public enum TemperatureUnit {
    CELSIUS("celsius", 0),
    FAHRENHEIT("fahrenheit", 1);

    private final String text;
    private final int choice;

    TemperatureUnit(final String text, final int choice) {
        this.text = text;
        this.choice = choice;
    }

    public String getText() {
        return text;
    }

    public int getChoice() {
        return choice;
    }

    public static TemperatureUnit fromText(final String text) {
        for (final TemperatureUnit temperatureUnitSetting : TemperatureUnit.values()) {
            if (temperatureUnitSetting.text.equalsIgnoreCase(text)) {
                return temperatureUnitSetting;
            }
        }
        return CELSIUS;
    }

    public static TemperatureUnit fromChoice(final int choice) {
        for (final TemperatureUnit temperatureUnitSetting : TemperatureUnit.values()) {
            if (temperatureUnitSetting.choice == choice) {
                return temperatureUnitSetting;
            }
        }
        return CELSIUS;
    }
}
