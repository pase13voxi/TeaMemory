package coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SuggestionsFactoryTest {

    @Test
    public void getBlackTeaSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(0, null);
        assertThat(suggestions).isInstanceOf(BlackTeaSuggestions.class);
    }

    @Test
    public void getGreenTeaSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(1, null);
        assertThat(suggestions).isInstanceOf(GreenTeaSuggestions.class);
    }

    @Test
    public void getYellowTeaSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(2, null);
        assertThat(suggestions).isInstanceOf(YellowTeaSuggestions.class);
    }

    @Test
    public void getWhiteTeaSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(3, null);
        assertThat(suggestions).isInstanceOf(WhiteTeaSuggestions.class);
    }

    @Test
    public void getOolongTeaSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(4, null);
        assertThat(suggestions).isInstanceOf(OolongTeaSuggestions.class);
    }

    @Test
    public void getPuerhTeaSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(5, null);
        assertThat(suggestions).isInstanceOf(PuerhTeaSuggestions.class);
    }

    @Test
    public void getHerbalTeaSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(6, null);
        assertThat(suggestions).isInstanceOf(HerbalTeaSuggestions.class);
    }

    @Test
    public void getFruitTeaSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(7, null);
        assertThat(suggestions).isInstanceOf(FruitTeaSuggestions.class);
    }

    @Test
    public void getRooibusTeaSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(8, null);
        assertThat(suggestions).isInstanceOf(RooibusTeaSuggestions.class);
    }

    @Test
    public void getNoSuggestions() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(9, null);
        assertThat(suggestions).isInstanceOf(NoSuggestions.class);
    }
}
