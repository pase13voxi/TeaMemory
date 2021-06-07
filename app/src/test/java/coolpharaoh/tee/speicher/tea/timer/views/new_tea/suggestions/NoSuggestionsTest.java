package coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NoSuggestionsTest {

    private Suggestions noSuggestions;

    @Before
    public void setUp() {
        noSuggestions = new NoSuggestions();
    }

    @Test
    public void getAmountTsSuggestion() {
        assertThat(noSuggestions.getAmountTsSuggestions()).isEmpty();
    }

    @Test
    public void getAmountGrSuggestion() {
        assertThat(noSuggestions.getAmountGrSuggestions()).isEmpty();
    }

    @Test
    public void getAmountTbSuggestion() {
        assertThat(noSuggestions.getAmountTbSuggestions()).isEmpty();
    }

    @Test
    public void getTemperatureCelsiusSuggestion() {
        assertThat(noSuggestions.getTemperatureCelsiusSuggestions()).isEmpty();
    }

    @Test
    public void getTemperatureFahrenheitSuggestion() {
        assertThat(noSuggestions.getTemperatureFahrenheitSuggestions()).isEmpty();
    }

    @Test
    public void getSteepingTimeSuggestion() {
        assertThat(noSuggestions.getTimeSuggestions()).isEmpty();
    }
}
