package coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions.SuggestionsFactory.getSuggestions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SuggestionsFactoryTest {
    @Test
    fun getBlackTeaSuggestions() {
        val suggestions = getSuggestions(0, Application())
        assertThat(suggestions).isInstanceOf(BlackTeaSuggestions::class.java)
    }

    @Test
    fun getGreenTeaSuggestions() {
        val suggestions = getSuggestions(1, Application())
        assertThat(suggestions).isInstanceOf(GreenTeaSuggestions::class.java)
    }

    @Test
    fun getYellowTeaSuggestions() {
        val suggestions = getSuggestions(2, Application())
        assertThat(suggestions).isInstanceOf(YellowTeaSuggestions::class.java)
    }

    @Test
    fun getWhiteTeaSuggestions() {
        val suggestions = getSuggestions(3, Application())
        assertThat(suggestions).isInstanceOf(WhiteTeaSuggestions::class.java)
    }

    @Test
    fun getOolongTeaSuggestions() {
        val suggestions = getSuggestions(4, Application())
        assertThat(suggestions).isInstanceOf(OolongTeaSuggestions::class.java)
    }

    @Test
    fun getPuerhTeaSuggestions() {
        val suggestions = getSuggestions(5, Application())
        assertThat(suggestions).isInstanceOf(PuerhTeaSuggestions::class.java)
    }

    @Test
    fun getHerbalTeaSuggestions() {
        val suggestions = getSuggestions(6, Application())
        assertThat(suggestions).isInstanceOf(HerbalTeaSuggestions::class.java)
    }

    @Test
    fun getFruitTeaSuggestions() {
        val suggestions = getSuggestions(7, Application())
        assertThat(suggestions).isInstanceOf(FruitTeaSuggestions::class.java)
    }

    @Test
    fun getRooibusTeaSuggestions() {
        val suggestions = getSuggestions(8, Application())
        assertThat(suggestions).isInstanceOf(RooibusTeaSuggestions::class.java)
    }

    @Test
    fun getNoSuggestions() {
        val suggestions = getSuggestions(9, Application())
        assertThat(suggestions).isInstanceOf(NoSuggestions::class.java)
    }
}