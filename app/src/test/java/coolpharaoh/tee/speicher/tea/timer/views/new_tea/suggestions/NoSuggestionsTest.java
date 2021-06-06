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
    public void getAmountTsHint() {
        assertThat(noSuggestions.getAmountTsSuggestions()).isEmpty();
    }

    @Test
    public void getAmountGrHint() {
        assertThat(noSuggestions.getAmountGrSuggestions()).isEmpty();
    }

    @Test
    public void getAmountTbHint() {
        assertThat(noSuggestions.getAmountTbSuggestions()).isEmpty();
    }

    @Test
    public void getTemperatureCelsiusHint() {
        assertThat(noSuggestions.getTemperatureCelsiusSuggestions()).isEmpty();
    }

    @Test
    public void getTemperatureFahrenheitHint() {
        assertThat(noSuggestions.getTemperatureFahrenheitSuggestions()).isEmpty();
    }

    @Test
    public void getSteepingTimeHint() {
        assertThat(noSuggestions.getTimeSuggestions()).isEmpty();
    }
}
