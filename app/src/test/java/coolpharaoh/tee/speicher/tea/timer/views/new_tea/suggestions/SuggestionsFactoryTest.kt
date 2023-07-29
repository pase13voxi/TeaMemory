package coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions;

import static org.assertj.core.api.Assertions.assertThat;

import android.app.Application;

import org.junit.jupiter.api.Test;

class SuggestionsFactoryTest {

    @Test
    void getBlackTeaSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(0, new Application());
        assertThat(suggestions).isInstanceOf(BlackTeaSuggestions.class);
    }

    @Test
    void getGreenTeaSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(1, new Application());
        assertThat(suggestions).isInstanceOf(GreenTeaSuggestions.class);
    }

    @Test
    void getYellowTeaSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(2, new Application());
        assertThat(suggestions).isInstanceOf(YellowTeaSuggestions.class);
    }

    @Test
    void getWhiteTeaSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(3, new Application());
        assertThat(suggestions).isInstanceOf(WhiteTeaSuggestions.class);
    }

    @Test
    void getOolongTeaSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(4, new Application());
        assertThat(suggestions).isInstanceOf(OolongTeaSuggestions.class);
    }

    @Test
    void getPuerhTeaSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(5, new Application());
        assertThat(suggestions).isInstanceOf(PuerhTeaSuggestions.class);
    }

    @Test
    void getHerbalTeaSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(6, new Application());
        assertThat(suggestions).isInstanceOf(HerbalTeaSuggestions.class);
    }

    @Test
    void getFruitTeaSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(7, new Application());
        assertThat(suggestions).isInstanceOf(FruitTeaSuggestions.class);
    }

    @Test
    void getRooibusTeaSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(8, new Application());
        assertThat(suggestions).isInstanceOf(RooibusTeaSuggestions.class);
    }

    @Test
    void getNoSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(9, new Application());
        assertThat(suggestions).isInstanceOf(NoSuggestions.class);
    }
}
