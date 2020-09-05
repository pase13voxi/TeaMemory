package coolpharaoh.tee.speicher.tea.timer.core.infusion;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TemperatureConversationTest {

    @Test
    public void convertMinus500CelsiusToFahrenheitReturnMinus500() {
        int celsius = -500;

        int fahrenheit = TemperatureConversation.celsiusToFahrenheit(celsius);

        assertThat(fahrenheit).isEqualTo(-500);
    }

    @Test
    public void convert0CelsiusToFahrenheitReturnMinus32() {
        int celsius = 0;

        int fahrenheit = TemperatureConversation.celsiusToFahrenheit(celsius);

        assertThat(fahrenheit).isEqualTo(32);
    }

    @Test
    public void convert100CelsiusToFahrenheitReturnMinus212() {
        int celsius = 100;

        int fahrenheit = TemperatureConversation.celsiusToFahrenheit(celsius);

        assertThat(fahrenheit).isEqualTo(212);
    }

    @Test
    public void convertMinus500FahrenheitToCelsiusReturnMinus500() {
        int fahrenheit = -500;

        int celsius = TemperatureConversation.fahrenheitToCelsius(fahrenheit);

        assertThat(celsius).isEqualTo(-500);
    }

    @Test
    public void convert0FahrenheitToCelsiusReturnMinus32() {
        int fahrenheit = 32;

        int celsius = TemperatureConversation.fahrenheitToCelsius(fahrenheit);

        assertThat(celsius).isEqualTo(0);
    }

    @Test
    public void convert100FahrenheitToCelsiusReturnMinus212() {
        int fahrenheit = 212;

        int celsius = TemperatureConversation.fahrenheitToCelsius(fahrenheit);

        assertThat(celsius).isEqualTo(100);
    }

    @Test
    public void convertMinus500CelsiusToCoolDownTimeReturnNull() {
        int celsius = -500;

        String coolDownTime = TemperatureConversation.celsiusToCoolDownTime(celsius);

        assertThat(coolDownTime).isNull();
    }

    @Test
    public void convert100CelsiusToCoolDownTimeReturnNull() {
        int celsius = 100;

        String coolDownTime = TemperatureConversation.celsiusToCoolDownTime(celsius);

        assertThat(coolDownTime).isNull();
    }

    @Test
    public void convert80CelsiusToCoolDownTimeReturn10Min() {
        int celsius = 80;

        String coolDownTime = TemperatureConversation.celsiusToCoolDownTime(celsius);

        assertThat(coolDownTime).isEqualTo("10:00");
    }

    @Test
    public void convert60CelsiusToCoolDownTimeReturn20Min() {
        int celsius = 60;

        String coolDownTime = TemperatureConversation.celsiusToCoolDownTime(celsius);

        assertThat(coolDownTime).isEqualTo("20:00");
    }
}
