package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.R
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*

@ExtendWith(MockKExtension::class)
internal class DisplayTemperatureUnitStrategyCelsiusTest {
    @MockK
    lateinit var application: Application
    @InjectMockKs
    lateinit var displayTemperatureUnitStrategyCelsius: DisplayTemperatureUnitStrategyCelsius

    @Test
    fun getTextShowTea() {
        every { application.getString(R.string.show_tea_display_celsius, "100") } returns "100 °C"
        assertThat(displayTemperatureUnitStrategyCelsius.getTextIdShowTea(100)).isEqualTo("100 °C")
    }

    @Test
    fun getTextShowTeaEmptyTemperature() {
        every { application.getString(R.string.show_tea_display_celsius, "-") } returns "- °C"
        assertThat(displayTemperatureUnitStrategyCelsius.getTextIdShowTea(-500)).isEqualTo("- °C")
    }

    @Test
    fun getTextNewTea() {
        every { application.getString(R.string.new_tea_edit_text_temperature_text_celsius, "100") } returns "100 °C (Celsius)"
        assertThat(displayTemperatureUnitStrategyCelsius.getTextNewTea(100)).isEqualTo("100 °C (Celsius)")
    }

    @Test
    fun getTextNewTeaEmptyTemperature() {
        every { application.getString(R.string.new_tea_edit_text_temperature_text_celsius, "-") } returns "- °C (Celsius)"
        assertThat(displayTemperatureUnitStrategyCelsius.getTextNewTea(-500)).isEqualTo("- °C (Celsius)")
    }
}