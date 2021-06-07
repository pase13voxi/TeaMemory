package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit;

import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit;

public class DisplayTemperatureUnitFactory {

    private DisplayTemperatureUnitFactory() {
    }

    public static DisplayTemperatureUnit get(final TemperatureUnit temperatureUnit) {
        if (temperatureUnit == TemperatureUnit.FAHRENHEIT) {
            return new DisplayTemperatureUnitFahrenheit();
        }
        return new DisplayTemperatureUnitCelsius();
    }
}
