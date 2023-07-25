package coolpharaoh.tee.speicher.tea.timer.core.settings

import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit.Companion.fromChoice
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit.Companion.fromText
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TemperatureUnitTest {
    @Test
    fun getTextFahrenheit() {
        val temperatureUnit = TemperatureUnit.FAHRENHEIT
        assertThat(temperatureUnit.text).isEqualTo("fahrenheit")
    }

    @Test
    fun getChoiceFahrenheit() {
        val temperatureUnit = TemperatureUnit.FAHRENHEIT
        assertThat(temperatureUnit.choice).isOne
    }

    @Test
    fun temperatureUnitFromTextFahrenheit() {
        val temperatureUnit = fromText("fahrenheit")
        assertThat(temperatureUnit).isEqualTo(TemperatureUnit.FAHRENHEIT)
    }

    @Test
    fun temperatureUnitFromTextNotDefined() {
        val temperatureUnit = fromText("not defined")
        assertThat(temperatureUnit).isEqualTo(TemperatureUnit.CELSIUS)
    }

    @Test
    fun temperatureUnitFromTextNull() {
        val temperatureUnit = fromText(null)
        assertThat(temperatureUnit).isEqualTo(TemperatureUnit.CELSIUS)
    }

    @Test
    fun temperatureUnitFromChoiceZero() {
        val temperatureUnit = fromChoice(1)
        assertThat(temperatureUnit).isEqualTo(TemperatureUnit.FAHRENHEIT)
    }

    @Test
    fun temperatureUnitFromChoiceMinusOne() {
        val temperatureUnit = fromChoice(-1)
        assertThat(temperatureUnit).isEqualTo(TemperatureUnit.CELSIUS)
    }
}