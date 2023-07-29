package coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NoSuggestionsTest {

    private Suggestions noSuggestions;

    @BeforeEach
    void setUp() {
        noSuggestions = new NoSuggestions();
    }

    @Test
    void getAmountTsSuggestion() {
        assertThat(noSuggestions.getAmountTsSuggestions()).isEmpty();
    }

    @Test
    void getAmountGrSuggestion() {
        assertThat(noSuggestions.getAmountGrSuggestions()).isEmpty();
    }

    @Test
    void getAmountTbSuggestion() {
        assertThat(noSuggestions.getAmountTbSuggestions()).isEmpty();
    }

    @Test
    void getTemperatureCelsiusSuggestion() {
        assertThat(noSuggestions.getTemperatureCelsiusSuggestions()).isEmpty();
    }

    @Test
    void getTemperatureFahrenheitSuggestion() {
        assertThat(noSuggestions.getTemperatureFahrenheitSuggestions()).isEmpty();
    }

    @Test
    void getSteepingTimeSuggestion() {
        assertThat(noSuggestions.getTimeSuggestions()).isEmpty();
    }
}
