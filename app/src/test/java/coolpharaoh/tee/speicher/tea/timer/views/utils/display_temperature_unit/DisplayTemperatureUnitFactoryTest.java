package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit;

import static org.assertj.core.api.Assertions.assertThat;

import android.app.Application;

import org.junit.jupiter.api.Test;

import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit;

class DisplayTemperatureUnitFactoryTest {

    @Test
    void getDisplayTemperatureUnitFahrenheit() {
        final DisplayTemperatureUnitStrategy displayTemperatureUnitStrategy = DisplayTemperatureUnitFactory.get(TemperatureUnit.FAHRENHEIT, new Application());
        assertThat(displayTemperatureUnitStrategy).isInstanceOf(DisplayTemperatureUnitStrategyFahrenheit.class);
    }

    @Test
    void getDisplayTemperatureUnitCelsius() {
        final DisplayTemperatureUnitStrategy displayTemperatureUnitStrategy = DisplayTemperatureUnitFactory.get(TemperatureUnit.CELSIUS, new Application());
        assertThat(displayTemperatureUnitStrategy).isInstanceOf(DisplayTemperatureUnitStrategyCelsius.class);
    }
}
