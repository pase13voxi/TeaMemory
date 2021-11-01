package coolpharaoh.tee.speicher.tea.timer.core.infusion;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class TemperatureConversationTest {

    @Test
    public void convertMinus500CelsiusToFahrenheitReturnMinus500() {
        final int celsius = -500;

        final int fahrenheit = TemperatureConversation.celsiusToFahrenheit(celsius);

        assertThat(fahrenheit).isEqualTo(-500);
    }

    @Test
    public void convert0CelsiusToFahrenheitReturnMinus32() {
        final int celsius = 0;

        final int fahrenheit = TemperatureConversation.celsiusToFahrenheit(celsius);

        assertThat(fahrenheit).isEqualTo(32);
    }

    @Test
    public void convert100CelsiusToFahrenheitReturnMinus212() {
        final int celsius = 100;

        final int fahrenheit = TemperatureConversation.celsiusToFahrenheit(celsius);

        assertThat(fahrenheit).isEqualTo(212);
    }

    @Test
    public void convertMinus500FahrenheitToCelsiusReturnMinus500() {
        final int fahrenheit = -500;

        final int celsius = TemperatureConversation.fahrenheitToCelsius(fahrenheit);

        assertThat(celsius).isEqualTo(-500);
    }

    @Test
    public void convert0FahrenheitToCelsiusReturnMinus32() {
        final int fahrenheit = 32;

        final int celsius = TemperatureConversation.fahrenheitToCelsius(fahrenheit);

        assertThat(celsius).isZero();
    }

    @Test
    public void convert100FahrenheitToCelsiusReturnMinus212() {
        final int fahrenheit = 212;

        final int celsius = TemperatureConversation.fahrenheitToCelsius(fahrenheit);

        assertThat(celsius).isEqualTo(100);
    }

    @Test
    public void convertMinus500CelsiusToCoolDownTimeReturnNull() {
        final int celsius = -500;

        final String coolDownTime = TemperatureConversation.celsiusToCoolDownTime(celsius);

        assertThat(coolDownTime).isNull();
    }

    @Test
    public void convert100CelsiusToCoolDownTimeReturnNull() {
        final int celsius = 100;

        final String coolDownTime = TemperatureConversation.celsiusToCoolDownTime(celsius);

        assertThat(coolDownTime).isNull();
    }

    @Test
    public void convert80CelsiusToCoolDownTimeReturn10Min() {
        final int celsius = 80;

        final String coolDownTime = TemperatureConversation.celsiusToCoolDownTime(celsius);

        assertThat(coolDownTime).isEqualTo("10:00");
    }

    @Test
    public void convert60CelsiusToCoolDownTimeReturn20Min() {
        final int celsius = 60;

        final String coolDownTime = TemperatureConversation.celsiusToCoolDownTime(celsius);

        assertThat(coolDownTime).isEqualTo("20:00");
    }
}
