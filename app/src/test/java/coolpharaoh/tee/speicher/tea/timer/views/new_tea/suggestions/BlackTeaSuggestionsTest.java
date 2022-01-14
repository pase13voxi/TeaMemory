package coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.content.res.Resources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import coolpharaoh.tee.speicher.tea.timer.R;

@ExtendWith(MockitoExtension.class)
class BlackTeaSuggestionsTest {

    private Suggestions blackTeaSuggestions;

    @Mock
    Application application;
    @Mock
    Resources resources;

    @BeforeEach
    void setUp() {
        when(application.getResources()).thenReturn(resources);
        blackTeaSuggestions = new BlackTeaSuggestions(application);
    }

    @Test
    void getAmountTsSuggestion() {
        final int[] arrayTs = new int[]{1, 2};
        when(resources.getIntArray(R.array.new_tea_suggestions_black_tea_amount_ts)).thenReturn(arrayTs);

        assertThat(blackTeaSuggestions.getAmountTsSuggestions()).isEqualTo(arrayTs);
    }

    @Test
    void getAmountGrSuggestion() {
        final int[] arrayGr = new int[]{1, 2};
        when(resources.getIntArray(R.array.new_tea_suggestions_black_tea_amount_gr)).thenReturn(arrayGr);

        assertThat(blackTeaSuggestions.getAmountGrSuggestions()).isEqualTo(arrayGr);
    }

    @Test
    void getAmountTbSuggestion() {
        final int[] arrayTb = new int[]{1, 2};
        when(resources.getIntArray(R.array.new_tea_suggestions_black_tea_amount_tb)).thenReturn(arrayTb);

        assertThat(blackTeaSuggestions.getAmountTbSuggestions()).isEqualTo(arrayTb);
    }

    @Test
    void getTemperatureCelsiusSuggestion() {
        final int[] arrayCelsius = new int[]{1, 2};
        when(resources.getIntArray(R.array.new_tea_suggestions_black_tea_temperature_celsius)).thenReturn(arrayCelsius);

        assertThat(blackTeaSuggestions.getTemperatureCelsiusSuggestions()).isEqualTo(arrayCelsius);
    }

    @Test
    void getTemperatureFahrenheitSuggestion() {
        final int[] arrayFahrenheit = new int[]{1, 2};
        when(resources.getIntArray(R.array.new_tea_suggestions_black_tea_temperature_fahrenheit)).thenReturn(arrayFahrenheit);

        assertThat(blackTeaSuggestions.getTemperatureFahrenheitSuggestions()).isEqualTo(arrayFahrenheit);
    }

    @Test
    void getSteepingTimeSuggestion() {
        final String[] arrayTime = new String[]{"1:00", "2:30"};
        when(resources.getStringArray(R.array.new_tea_suggestions_black_tea_time)).thenReturn(arrayTime);

        assertThat(blackTeaSuggestions.getTimeSuggestions()).isEqualTo(arrayTime);
    }
}
