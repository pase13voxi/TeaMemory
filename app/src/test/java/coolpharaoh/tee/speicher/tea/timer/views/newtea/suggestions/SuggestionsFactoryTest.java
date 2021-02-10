package coolpharaoh.tee.speicher.tea.timer.views.newtea.suggestions;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SuggestionsFactoryTest {

    @Test
    public void getBlackTeaHints() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(0, null);
        assertThat(suggestions).isInstanceOf(BlackTeaSuggestions.class);
    }

    @Test
    public void getGreenTeaHints() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(1, null);
        assertThat(suggestions).isInstanceOf(GreenTeaSuggestions.class);
    }

    @Test
    public void getYellowTeaHints() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(2, null);
        assertThat(suggestions).isInstanceOf(YellowTeaSuggestions.class);
    }

    @Test
    public void getWhiteTeaHints() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(3, null);
        assertThat(suggestions).isInstanceOf(WhiteTeaSuggestions.class);
    }

    @Test
    public void getOolongTeaHints() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(4, null);
        assertThat(suggestions).isInstanceOf(OolongTeaSuggestions.class);
    }

    @Test
    public void getPuerhTeaHints() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(5, null);
        assertThat(suggestions).isInstanceOf(PuerhTeaSuggestions.class);
    }

    @Test
    public void getHerbalTeaHints() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(6, null);
        assertThat(suggestions).isInstanceOf(HerbalTeaSuggestions.class);
    }

    @Test
    public void getFruitTeaHints() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(7, null);
        assertThat(suggestions).isInstanceOf(FruitTeaSuggestions.class);
    }

    @Test
    public void getRooibusTeaHints() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(8, null);
        assertThat(suggestions).isInstanceOf(RooibusTeaSuggestions.class);
    }

    @Test
    public void getNoHints() {
        final Suggestions suggestions = SuggestionsFactory.getSuggestions(9, null);
        assertThat(suggestions).isInstanceOf(NoSuggestions.class);
    }
}
