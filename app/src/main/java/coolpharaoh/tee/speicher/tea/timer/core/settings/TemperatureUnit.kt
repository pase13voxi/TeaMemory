package coolpharaoh.tee.speicher.tea.timer.core.settings

enum class TemperatureUnit(val text: String, val choice: Int) {
    CELSIUS("celsius", 0),
    FAHRENHEIT("fahrenheit", 1);

    companion object {
        @JvmStatic
        fun fromText(text: String?): TemperatureUnit {
            for (temperatureUnitSetting in values()) {
                if (temperatureUnitSetting.text.equals(text, ignoreCase = true)) {
                    return temperatureUnitSetting
                }
            }
            return CELSIUS
        }

        @JvmStatic
        fun fromChoice(choice: Int): TemperatureUnit {
            for (temperatureUnitSetting in values()) {
                if (temperatureUnitSetting.choice == choice) {
                    return temperatureUnitSetting
                }
            }
            return CELSIUS
        }
    }
}