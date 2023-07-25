package coolpharaoh.tee.speicher.tea.timer.core.infusion

import coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation.celsiusToCoolDownTime
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation.celsiusToFahrenheit
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation.fahrenheitToCelsius
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TemperatureConversationTest {
    @Test
    fun convertMinus500CelsiusToFahrenheitReturnMinus500() {
        val celsius = -500

        val fahrenheit = celsiusToFahrenheit(celsius)

        assertThat(fahrenheit).isEqualTo(-500)
    }

    @Test
    fun convert0CelsiusToFahrenheitReturnMinus32() {
        val celsius = 0

        val fahrenheit = celsiusToFahrenheit(celsius)

        assertThat(fahrenheit).isEqualTo(32)
    }

    @Test
    fun convert100CelsiusToFahrenheitReturnMinus212() {
        val celsius = 100

        val fahrenheit = celsiusToFahrenheit(celsius)

        assertThat(fahrenheit).isEqualTo(212)
    }

    @Test
    fun convertMinus500FahrenheitToCelsiusReturnMinus500() {
        val fahrenheit = -500

        val celsius = fahrenheitToCelsius(fahrenheit)

        assertThat(celsius).isEqualTo(-500)
    }

    @Test
    fun convert0FahrenheitToCelsiusReturnMinus32() {
        val fahrenheit = 32

        val celsius = fahrenheitToCelsius(fahrenheit)

        assertThat(celsius).isZero
    }

    @Test
    fun convert100FahrenheitToCelsiusReturnMinus212() {
        val fahrenheit = 212

        val celsius = fahrenheitToCelsius(fahrenheit)

        assertThat(celsius).isEqualTo(100)
    }

    @Test
    fun convertMinus500CelsiusToCoolDownTimeReturnNull() {
        val celsius = -500

        val coolDownTime = celsiusToCoolDownTime(celsius)

        assertThat(coolDownTime).isNull()
    }

    @Test
    fun convert100CelsiusToCoolDownTimeReturnNull() {
        val celsius = 100

        val coolDownTime = celsiusToCoolDownTime(celsius)

        assertThat(coolDownTime).isNull()
    }

    @Test
    fun convert80CelsiusToCoolDownTimeReturn10Min() {
        val celsius = 80

        val coolDownTime = celsiusToCoolDownTime(celsius)

        assertThat(coolDownTime).isEqualTo("10:00")
    }

    @Test
    fun convert60CelsiusToCoolDownTimeReturn20Min() {
        val celsius = 60

        val coolDownTime = celsiusToCoolDownTime(celsius)

        assertThat(coolDownTime).isEqualTo("20:00")
    }
}