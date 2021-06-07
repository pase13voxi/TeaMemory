package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit;

import org.junit.Test;

import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit;

import static org.assertj.core.api.Assertions.assertThat;

;

public class DisplayTemperatureUnitFactoryTest {
    @Test
    public void getDisplayTemperatureUnitFahrenheit() {
        final DisplayTemperatureUnit displayTemperatureUnit = DisplayTemperatureUnitFactory.get(TemperatureUnit.FAHRENHEIT);
        assertThat(displayTemperatureUnit).isInstanceOf(DisplayTemperatureUnitFahrenheit.class);
    }

    @Test
    public void getDisplayTemperatureUnitCelsius() {
        final DisplayTemperatureUnit displayTemperatureUnit = DisplayTemperatureUnitFactory.get(TemperatureUnit.CELSIUS);
        assertThat(displayTemperatureUnit).isInstanceOf(DisplayTemperatureUnitCelsius.class);
    }
}
