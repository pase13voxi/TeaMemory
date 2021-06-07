package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit;

import org.junit.Test;

import coolpharaoh.tee.speicher.tea.timer.R;

import static org.assertj.core.api.Assertions.assertThat;

public class DisplayTemperatureUnitFahrenheitTest {

    @Test
    public void getTextIdShowTea() {
        final DisplayTemperatureUnitFahrenheit displayTemperatureUnitFahrenheit = new DisplayTemperatureUnitFahrenheit();
        assertThat(displayTemperatureUnitFahrenheit.getTextIdShowTea()).isEqualTo(R.string.show_tea_display_fahrenheit);
    }

    @Test
    public void getTextIdEmptyTemperatureNewTea() {
        final DisplayTemperatureUnitFahrenheit displayTemperatureUnitFahrenheit = new DisplayTemperatureUnitFahrenheit();
        assertThat(displayTemperatureUnitFahrenheit.getTextIdEmptyTemperatureNewTea()).isEqualTo(R.string.new_tea_edit_text_temperature_empty_text_fahrenheit);
    }

    @Test
    public void getTextIdCalculator() {
        final DisplayTemperatureUnitFahrenheit displayTemperatureUnitFahrenheit = new DisplayTemperatureUnitFahrenheit();
        assertThat(displayTemperatureUnitFahrenheit.getTextIdNewTea()).isEqualTo(R.string.new_tea_edit_text_temperature_text_fahrenheit);
    }
}
