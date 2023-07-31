package coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions

import android.app.Application
import android.content.res.Resources
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
internal class WhiteTeaSuggestionsTest {
    @MockK
    lateinit var application: Application

    @MockK
    lateinit var resources: Resources

    @InjectMockKs
    lateinit var whiteTeaSuggestions: WhiteTeaSuggestions

    @BeforeEach
    fun setUp() {
        every { application.resources } returns resources
    }

    @Test
    fun getAmountTsSuggestion() {
        val arrayTs = intArrayOf(1, 2)
        every { resources.getIntArray(R.array.new_tea_suggestions_white_tea_amount_ts) } returns arrayTs

        assertThat(whiteTeaSuggestions.amountTsSuggestions).isEqualTo(arrayTs)
    }

    @Test
    fun getAmountGrSuggestion() {
        val arrayGr = intArrayOf(1, 2)
        every { resources.getIntArray(R.array.new_tea_suggestions_white_tea_amount_gr) } returns arrayGr

        assertThat(whiteTeaSuggestions.amountGrSuggestions).isEqualTo(arrayGr)
    }

    @Test
    fun getAmountTbSuggestion() {
        val arrayTb = intArrayOf(1, 2)
        every { resources.getIntArray(R.array.new_tea_suggestions_white_tea_amount_tb) } returns arrayTb

        assertThat(whiteTeaSuggestions.amountTbSuggestions).isEqualTo(arrayTb)
    }

    @Test
    fun getTemperatureCelsiusSuggestion() {
        val arrayCelsius = intArrayOf(1, 2)
        every { resources.getIntArray(R.array.new_tea_suggestions_white_tea_temperature_celsius) } returns arrayCelsius

        assertThat(whiteTeaSuggestions.temperatureCelsiusSuggestions).isEqualTo(arrayCelsius)
    }

    @Test
    fun getTemperatureFahrenheitSuggestion() {
        val arrayFahrenheit = intArrayOf(1, 2)
        every { resources.getIntArray(R.array.new_tea_suggestions_white_tea_temperature_fahrenheit) } returns arrayFahrenheit

        assertThat(whiteTeaSuggestions.temperatureFahrenheitSuggestions).isEqualTo(arrayFahrenheit)
    }

    @Test
    fun getSteepingTimeSuggestion() {
        val arrayTime = arrayOf("1:00", "2:30")
        every { resources.getStringArray(R.array.new_tea_suggestions_white_tea_time) } returns arrayTime

        assertThat(whiteTeaSuggestions.timeSuggestions).isEqualTo(arrayTime)
    }
}