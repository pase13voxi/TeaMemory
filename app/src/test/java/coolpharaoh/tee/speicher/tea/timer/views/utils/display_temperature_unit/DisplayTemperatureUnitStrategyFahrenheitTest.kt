package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.R
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class DisplayTemperatureUnitStrategyFahrenheitTest {

    private var displayTemperatureUnitStrategyFahrenheit: DisplayTemperatureUnitStrategyFahrenheit? = null

    @Mock
    var application: Application? = null

    @BeforeEach
    fun setUp() {
        displayTemperatureUnitStrategyFahrenheit = DisplayTemperatureUnitStrategyFahrenheit(application!!)
    }

    @Test
    fun getTextShowTea() {
        `when`(application!!.getString(R.string.show_tea_display_fahrenheit, "212")).thenReturn("212 °F")
        assertThat(displayTemperatureUnitStrategyFahrenheit!!.getTextIdShowTea(212)).isEqualTo("212 °F")
    }

    @Test
    fun getTextShowTeaEmptyTemperature() {
        `when`(application!!.getString(R.string.show_tea_display_fahrenheit, "-")).thenReturn("- °F")
        assertThat(displayTemperatureUnitStrategyFahrenheit!!.getTextIdShowTea(-500)).isEqualTo("- °F")
    }

    @Test
    fun getTextNewTea() {
        `when`(application!!.getString(R.string.new_tea_edit_text_temperature_text_fahrenheit, "212")).thenReturn("212 °F (Fahrenheit)")
        assertThat(displayTemperatureUnitStrategyFahrenheit!!.getTextNewTea(212)).isEqualTo("212 °F (Fahrenheit)")
    }

    @Test
    fun getTextNewTeaEmptyTemperature() {
        `when`(application!!.getString(R.string.new_tea_edit_text_temperature_text_fahrenheit, "-")).thenReturn("- °F (Fahrenheit)")
        assertThat(displayTemperatureUnitStrategyFahrenheit!!.getTextNewTea(-500)).isEqualTo("- °F (Fahrenheit)")
    }
}