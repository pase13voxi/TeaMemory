package coolpharaoh.tee.speicher.tea.timer.core.actual_settings;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TemperatureUnitTest {

    @Test
    void getTextFahrenheit() {
        final TemperatureUnit temperatureUnit = TemperatureUnit.FAHRENHEIT;
        assertThat(temperatureUnit.getText()).isEqualTo("fahrenheit");
    }

    @Test
    void getChoiceFahrenheit() {
        final TemperatureUnit temperatureUnit = TemperatureUnit.FAHRENHEIT;
        assertThat(temperatureUnit.getChoice()).isOne();
    }

    @Test
    void temperatureUnitFromTextFahrenheit() {
        final TemperatureUnit temperatureUnit = TemperatureUnit.fromText("fahrenheit");
        assertThat(temperatureUnit).isEqualTo(TemperatureUnit.FAHRENHEIT);
    }

    @Test
    void temperatureUnitFromTextNotDefined() {
        final TemperatureUnit temperatureUnit = TemperatureUnit.fromText("not defined");
        assertThat(temperatureUnit).isEqualTo(TemperatureUnit.CELSIUS);
    }

    @Test
    void temperatureUnitFromTextNull() {
        final TemperatureUnit temperatureUnit = TemperatureUnit.fromText(null);
        assertThat(temperatureUnit).isEqualTo(TemperatureUnit.CELSIUS);
    }

    @Test
    void temperatureUnitFromChoiceZero() {
        final TemperatureUnit temperatureUnit = TemperatureUnit.fromChoice(1);
        assertThat(temperatureUnit).isEqualTo(TemperatureUnit.FAHRENHEIT);
    }

    @Test
    void temperatureUnitFromChoiceMinusOne() {
        final TemperatureUnit temperatureUnit = TemperatureUnit.fromChoice(-1);
        assertThat(temperatureUnit).isEqualTo(TemperatureUnit.CELSIUS);
    }
}
