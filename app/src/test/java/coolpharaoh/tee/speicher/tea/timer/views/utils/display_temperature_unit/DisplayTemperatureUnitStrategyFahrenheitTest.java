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
public class DisplayTemperatureUnitStrategyFahrenheitTest {

    private DisplayTemperatureUnitStrategyFahrenheit displayTemperatureUnitStrategyFahrenheit;
    @Mock
    Application application;

    @Before
    public void setUp() {
        displayTemperatureUnitStrategyFahrenheit = new DisplayTemperatureUnitStrategyFahrenheit(application);
    }

    @Test
    public void getTextShowTea() {
        when(application.getString(R.string.show_tea_display_fahrenheit, "212")).thenReturn("212 °F");

        assertThat(displayTemperatureUnitStrategyFahrenheit.getTextIdShowTea(212)).isEqualTo("212 °F");
    }

    @Test
    public void getTextShowTeaEmptyTemperature() {
        when(application.getString(R.string.show_tea_display_fahrenheit, "-")).thenReturn("- °F");

        assertThat(displayTemperatureUnitStrategyFahrenheit.getTextIdShowTea(-500)).isEqualTo("- °F");
    }

    @Test
    public void getTextNewTea() {
        when(application.getString(R.string.new_tea_edit_text_temperature_text_fahrenheit, "212")).thenReturn("212 °F (Fahrenheit)");

        assertThat(displayTemperatureUnitStrategyFahrenheit.getTextNewTea(212)).isEqualTo("212 °F (Fahrenheit)");
    }

    @Test
    public void getTextNewTeaEmptyTemperature() {
        when(application.getString(R.string.new_tea_edit_text_temperature_text_fahrenheit, "-")).thenReturn("- °F (Fahrenheit)");

        assertThat(displayTemperatureUnitStrategyFahrenheit.getTextNewTea(-500)).isEqualTo("- °F (Fahrenheit)");
    }
}
