package coolpharaoh.tee.speicher.tea.timer.core.actual_settings;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TemperatureUnitTest {

    @Test
    public void getTextFahrenheit() {
        final TemperatureUnit temperatureUnit = TemperatureUnit.FAHRENHEIT;
        assertThat(temperatureUnit.getText()).isEqualTo("Fahrenheit");
    }

    @Test
    public void getChoiceFahrenheit() {
        final TemperatureUnit temperatureUnit = TemperatureUnit.FAHRENHEIT;
        assertThat(temperatureUnit.getChoice()).isOne();
    }

    @Test
    public void temperatureUnitFromTextFahrenheit() {
        final TemperatureUnit temperatureUnit = TemperatureUnit.fromText("Fahrenheit");
        assertThat(temperatureUnit).isEqualTo(TemperatureUnit.FAHRENHEIT);
    }

    @Test
    public void temperatureUnitFromTextNotDefined() {
        final TemperatureUnit temperatureUnit = TemperatureUnit.fromText("not defined");
        assertThat(temperatureUnit).isEqualTo(TemperatureUnit.CELSIUS);
    }

    @Test
    public void temperatureUnitFromTextNull() {
        final TemperatureUnit temperatureUnit = TemperatureUnit.fromText(null);
        assertThat(temperatureUnit).isEqualTo(TemperatureUnit.CELSIUS);
    }

    @Test
    public void temperatureUnitFromChoiceZero() {
        final TemperatureUnit temperatureUnit = TemperatureUnit.fromChoice(1);
        assertThat(temperatureUnit).isEqualTo(TemperatureUnit.FAHRENHEIT);
    }

    @Test
    public void temperatureUnitFromChoiceMinusOne() {
        final TemperatureUnit temperatureUnit = TemperatureUnit.fromChoice(-1);
        assertThat(temperatureUnit).isEqualTo(TemperatureUnit.CELSIUS);
    }
}
