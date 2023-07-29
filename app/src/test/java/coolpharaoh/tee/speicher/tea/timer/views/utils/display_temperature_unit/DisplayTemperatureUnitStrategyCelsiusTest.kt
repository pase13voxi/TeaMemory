package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import android.app.Application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import coolpharaoh.tee.speicher.tea.timer.R;

@ExtendWith(MockitoExtension.class)
class DisplayTemperatureUnitStrategyCelsiusTest {

    private DisplayTemperatureUnitStrategyCelsius displayTemperatureUnitStrategyCelsius;
    @Mock
    Application application;

    @BeforeEach
    void setUp() {
        displayTemperatureUnitStrategyCelsius = new DisplayTemperatureUnitStrategyCelsius(application);
    }

    @Test
    void getTextShowTea() {
        when(application.getString(R.string.show_tea_display_celsius, "100")).thenReturn("100 °C");

        assertThat(displayTemperatureUnitStrategyCelsius.getTextIdShowTea(100)).isEqualTo("100 °C");
    }

    @Test
    void getTextShowTeaEmptyTemperature() {
        when(application.getString(R.string.show_tea_display_celsius, "-")).thenReturn("- °C");

        assertThat(displayTemperatureUnitStrategyCelsius.getTextIdShowTea(-500)).isEqualTo("- °C");
    }

    @Test
    void getTextNewTea() {
        when(application.getString(R.string.new_tea_edit_text_temperature_text_celsius, "100")).thenReturn("100 °C (Celsius)");

        assertThat(displayTemperatureUnitStrategyCelsius.getTextNewTea(100)).isEqualTo("100 °C (Celsius)");
    }

    @Test
    void getTextNewTeaEmptyTemperature() {
        when(application.getString(R.string.new_tea_edit_text_temperature_text_celsius, "-")).thenReturn("- °C (Celsius)");

        assertThat(displayTemperatureUnitStrategyCelsius.getTextNewTea(-500)).isEqualTo("- °C (Celsius)");
    }
}
