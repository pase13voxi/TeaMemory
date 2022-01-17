package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit;

public class DisplayTemperatureUnitFactory {

    private DisplayTemperatureUnitFactory() {
    }

    public static DisplayTemperatureUnitStrategy get(final TemperatureUnit temperatureUnit, final Application application) {
        if (temperatureUnit == TemperatureUnit.FAHRENHEIT) {
            return new DisplayTemperatureUnitStrategyFahrenheit(application);
        }
        return new DisplayTemperatureUnitStrategyCelsius(application);
    }
}
