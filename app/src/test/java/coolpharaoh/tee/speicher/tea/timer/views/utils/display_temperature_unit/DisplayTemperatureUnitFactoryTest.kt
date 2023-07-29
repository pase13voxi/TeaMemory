package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DisplayTemperatureUnitFactoryTest {

    @Test
    fun getDisplayTemperatureUnitFahrenheit() {
        val displayTemperatureUnitStrategy = DisplayTemperatureUnitFactory[TemperatureUnit.FAHRENHEIT, Application()]
        assertThat(displayTemperatureUnitStrategy).isInstanceOf(DisplayTemperatureUnitStrategyFahrenheit::class.java)
    }

    @Test
    fun getDisplayTemperatureUnitCelsius() {
        val displayTemperatureUnitStrategy = DisplayTemperatureUnitFactory[TemperatureUnit.CELSIUS, Application()]
        assertThat(displayTemperatureUnitStrategy).isInstanceOf(DisplayTemperatureUnitStrategyCelsius::class.java)
    }
}