package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit;

import org.junit.Test;

import coolpharaoh.tee.speicher.tea.timer.R;

import static org.assertj.core.api.Assertions.assertThat;

public class DisplayTemperatureUnitCelsiusTest {

    @Test
    public void getTextIdShowTea() {
        final DisplayTemperatureUnitCelsius displayTemperatureUnitCelsius = new DisplayTemperatureUnitCelsius();
        assertThat(displayTemperatureUnitCelsius.getTextIdShowTea()).isEqualTo(R.string.show_tea_display_celsius);
    }

    @Test
    public void getTextIdEmptyTemperatureNewTea() {
        final DisplayTemperatureUnitCelsius displayTemperatureUnitCelsius = new DisplayTemperatureUnitCelsius();
        assertThat(displayTemperatureUnitCelsius.getTextIdEmptyTemperatureNewTea()).isEqualTo(R.string.new_tea_edit_text_temperature_empty_text_celsius);
    }

    @Test
    public void getTextIdCalculator() {
        final DisplayTemperatureUnitCelsius displayTemperatureUnitCelsius = new DisplayTemperatureUnitCelsius();
        assertThat(displayTemperatureUnitCelsius.getTextIdNewTea()).isEqualTo(R.string.new_tea_edit_text_temperature_text_celsius);
    }
}
