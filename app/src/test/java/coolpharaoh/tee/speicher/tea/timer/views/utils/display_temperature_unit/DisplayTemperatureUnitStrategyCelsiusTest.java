package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit;

import android.app.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import coolpharaoh.tee.speicher.tea.timer.R;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DisplayTemperatureUnitStrategyCelsiusTest {

    private DisplayTemperatureUnitStrategyCelsius displayTemperatureUnitStrategyCelsius;
    @Mock
    Application application;

    @Before
    public void setUp() {
        displayTemperatureUnitStrategyCelsius = new DisplayTemperatureUnitStrategyCelsius(application);
    }

    @Test
    public void getTextShowTea() {
        when(application.getString(R.string.show_tea_display_celsius, "100")).thenReturn("100 °C");

        assertThat(displayTemperatureUnitStrategyCelsius.getTextIdShowTea(100)).isEqualTo("100 °C");
    }

    @Test
    public void getTextShowTeaEmptyTemperature() {
        when(application.getString(R.string.show_tea_display_celsius, "-")).thenReturn("- °C");

        assertThat(displayTemperatureUnitStrategyCelsius.getTextIdShowTea(-500)).isEqualTo("- °C");
    }

    @Test
    public void getTextNewTea() {
        when(application.getString(R.string.new_tea_edit_text_temperature_text_celsius, "100")).thenReturn("100 °C (Celsius)");

        assertThat(displayTemperatureUnitStrategyCelsius.getTextNewTea(100)).isEqualTo("100 °C (Celsius)");
    }

    @Test
    public void getTextNewTeaEmptyTemperature() {
        when(application.getString(R.string.new_tea_edit_text_temperature_text_celsius, "-")).thenReturn("- °C (Celsius)");

        assertThat(displayTemperatureUnitStrategyCelsius.getTextNewTea(-500)).isEqualTo("- °C (Celsius)");
    }
}
