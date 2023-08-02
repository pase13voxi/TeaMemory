package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.R
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class DisplayTemperatureUnitStrategyFahrenheitTest {

    @MockK
    lateinit var application: Application
    @InjectMockKs
    lateinit var displayTemperatureUnitStrategyFahrenheit: DisplayTemperatureUnitStrategyFahrenheit

    @BeforeEach
    fun setUp() {
        displayTemperatureUnitStrategyFahrenheit = DisplayTemperatureUnitStrategyFahrenheit(application)
    }

    @Test
    fun getTextShowTea() {
        every { application.getString(R.string.show_tea_display_fahrenheit, "212") } returns "212 °F"
        assertThat(displayTemperatureUnitStrategyFahrenheit.getTextIdShowTea(212)).isEqualTo("212 °F")
    }

    @Test
    fun getTextShowTeaEmptyTemperature() {
        every { application.getString(R.string.show_tea_display_fahrenheit, "-") } returns "- °F"
        assertThat(displayTemperatureUnitStrategyFahrenheit.getTextIdShowTea(-500)).isEqualTo("- °F")
    }

    @Test
    fun getTextNewTea() {
        every { application.getString(R.string.new_tea_edit_text_temperature_text_fahrenheit, "212") } returns "212 °F (Fahrenheit)"
        assertThat(displayTemperatureUnitStrategyFahrenheit.getTextNewTea(212)).isEqualTo("212 °F (Fahrenheit)")
    }

    @Test
    fun getTextNewTeaEmptyTemperature() {
        every { application.getString(R.string.new_tea_edit_text_temperature_text_fahrenheit, "-") } returns "- °F (Fahrenheit)"
        assertThat(displayTemperatureUnitStrategyFahrenheit.getTextNewTea(-500)).isEqualTo("- °F (Fahrenheit)")
    }
}