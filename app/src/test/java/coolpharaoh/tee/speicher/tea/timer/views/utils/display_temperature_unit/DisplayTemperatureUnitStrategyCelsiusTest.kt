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
internal class DisplayTemperatureUnitStrategyCelsiusTest {

    private var displayTemperatureUnitStrategyCelsius: DisplayTemperatureUnitStrategyCelsius? = null

    @Mock
    var application: Application? = null

    @BeforeEach
    fun setUp() {
        displayTemperatureUnitStrategyCelsius = DisplayTemperatureUnitStrategyCelsius(application!!)
    }

    @Test
    fun getTextShowTea() {
        `when`(application!!.getString(R.string.show_tea_display_celsius, "100")).thenReturn("100 °C")
        assertThat(displayTemperatureUnitStrategyCelsius!!.getTextIdShowTea(100)).isEqualTo("100 °C")
    }

    @Test
    fun getTextShowTeaEmptyTemperature() {
        `when`(application!!.getString(R.string.show_tea_display_celsius, "-")).thenReturn("- °C")
        assertThat(displayTemperatureUnitStrategyCelsius!!.getTextIdShowTea(-500)).isEqualTo("- °C")
    }

    @Test
    fun getTextNewTea() {
        `when`(application!!.getString(R.string.new_tea_edit_text_temperature_text_celsius, "100")).thenReturn("100 °C (Celsius)")
        assertThat(displayTemperatureUnitStrategyCelsius!!.getTextNewTea(100)).isEqualTo("100 °C (Celsius)")
    }

    @Test
    fun getTextNewTeaEmptyTemperature() {
        `when`(application!!.getString(R.string.new_tea_edit_text_temperature_text_celsius, "-")).thenReturn("- °C (Celsius)")
        assertThat(displayTemperatureUnitStrategyCelsius!!.getTextNewTea(-500)).isEqualTo("- °C (Celsius)")
    }
}