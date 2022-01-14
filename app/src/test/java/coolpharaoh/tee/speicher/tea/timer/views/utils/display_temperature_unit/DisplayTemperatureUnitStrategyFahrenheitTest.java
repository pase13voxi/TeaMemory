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
class DisplayTemperatureUnitStrategyFahrenheitTest {

    private DisplayTemperatureUnitStrategyFahrenheit displayTemperatureUnitStrategyFahrenheit;
    @Mock
    Application application;

    @BeforeEach
    void setUp() {
        displayTemperatureUnitStrategyFahrenheit = new DisplayTemperatureUnitStrategyFahrenheit(application);
    }

    @Test
    void getTextShowTea() {
        when(application.getString(R.string.show_tea_display_fahrenheit, "212")).thenReturn("212 °F");

        assertThat(displayTemperatureUnitStrategyFahrenheit.getTextIdShowTea(212)).isEqualTo("212 °F");
    }

    @Test
    void getTextShowTeaEmptyTemperature() {
        when(application.getString(R.string.show_tea_display_fahrenheit, "-")).thenReturn("- °F");

        assertThat(displayTemperatureUnitStrategyFahrenheit.getTextIdShowTea(-500)).isEqualTo("- °F");
    }

    @Test
    void getTextNewTea() {
        when(application.getString(R.string.new_tea_edit_text_temperature_text_fahrenheit, "212")).thenReturn("212 °F (Fahrenheit)");

        assertThat(displayTemperatureUnitStrategyFahrenheit.getTextNewTea(212)).isEqualTo("212 °F (Fahrenheit)");
    }

    @Test
    void getTextNewTeaEmptyTemperature() {
        when(application.getString(R.string.new_tea_edit_text_temperature_text_fahrenheit, "-")).thenReturn("- °F (Fahrenheit)");

        assertThat(displayTemperatureUnitStrategyFahrenheit.getTextNewTea(-500)).isEqualTo("- °F (Fahrenheit)");
    }
}
