package coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class NoSuggestionsTest {
    private var noSuggestions: NoSuggestions = NoSuggestions()

    @Test
    fun getAmountTsSuggestion() {
        assertThat(noSuggestions.amountTsSuggestions).isEmpty()
    }

    @Test
    fun getAmountGrSuggestion() {
        assertThat(noSuggestions.amountGrSuggestions).isEmpty()
    }

    @Test
    fun getAmountTbSuggestion() {
        assertThat(noSuggestions.amountTbSuggestions).isEmpty()
    }

    @Test
    fun getTemperatureCelsiusSuggestion() {
        assertThat(noSuggestions.temperatureCelsiusSuggestions).isEmpty()
    }

    @Test
    fun getTemperatureFahrenheitSuggestion() {
        assertThat(noSuggestions.temperatureFahrenheitSuggestions).isEmpty()
    }

    @Test
    fun getSteepingTimeSuggestion() {
        assertThat(noSuggestions.timeSuggestions).isEmpty()
    }
}