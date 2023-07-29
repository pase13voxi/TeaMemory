package coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions

import android.app.Application
import android.content.res.Resources
import coolpharaoh.tee.speicher.tea.timer.R
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class PuerhTeaSuggestionsTest {
    @Mock
    lateinit var application: Application

    @Mock
    lateinit var resources: Resources

    @InjectMocks
    lateinit var puerhTeaSuggestions: PuerhTeaSuggestions

    @BeforeEach
    fun setUp() {
        `when`(application.resources).thenReturn(resources)
    }

    @Test
    fun getAmountTsSuggestion() {
            val arrayTs = intArrayOf(1, 2)
            `when`(resources.getIntArray(R.array.new_tea_suggestions_puerh_tea_amount_ts)).thenReturn(arrayTs)

            assertThat(puerhTeaSuggestions.amountTsSuggestions).isEqualTo(arrayTs)
        }

    @Test
    fun getAmountGrSuggestion() {
            val arrayGr = intArrayOf(1, 2)
            `when`(resources.getIntArray(R.array.new_tea_suggestions_puerh_tea_amount_gr)).thenReturn(arrayGr)

            assertThat(puerhTeaSuggestions.amountGrSuggestions).isEqualTo(arrayGr)
        }

    @Test
    fun getAmountTbSuggestion() {
            val arrayTb = intArrayOf(1, 2)
            `when`(resources.getIntArray(R.array.new_tea_suggestions_puerh_tea_amount_tb)).thenReturn(arrayTb)

            assertThat(puerhTeaSuggestions.amountTbSuggestions).isEqualTo(arrayTb)
        }

    @Test
    fun getTemperatureCelsiusSuggestion() {
            val arrayCelsius = intArrayOf(1, 2)
            `when`(resources.getIntArray(R.array.new_tea_suggestions_puerh_tea_temperature_celsius)).thenReturn(arrayCelsius)

            assertThat(puerhTeaSuggestions.temperatureCelsiusSuggestions).isEqualTo(arrayCelsius)
        }

    @Test
    fun getTemperatureFahrenheitSuggestion() {
            val arrayFahrenheit = intArrayOf(1, 2)
            `when`(resources.getIntArray(R.array.suggestions_puerh_tea_temperature_fahrenheit)).thenReturn(arrayFahrenheit)

            assertThat(puerhTeaSuggestions.temperatureFahrenheitSuggestions).isEqualTo(arrayFahrenheit)
        }

    @Test
    fun getSteepingTimeSuggestion() {
            val arrayTime = arrayOf("1:00", "2:30")
            `when`(resources.getStringArray(R.array.new_tea_suggestions_puerh_tea_time)).thenReturn(arrayTime)

            assertThat(puerhTeaSuggestions.timeSuggestions).isEqualTo(arrayTime)
        }
}