package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit

object DisplayTemperatureUnitFactory {

    @JvmStatic
    operator fun get(temperatureUnit: TemperatureUnit, application: Application): DisplayTemperatureUnitStrategy {
        return if (temperatureUnit === TemperatureUnit.FAHRENHEIT) {
            DisplayTemperatureUnitStrategyFahrenheit(application)
        } else DisplayTemperatureUnitStrategyCelsius(application)
    }
}