package coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions;

import android.app.Application;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import coolpharaoh.tee.speicher.tea.timer.R;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class YellowTeaSuggestionsTest {

    private Suggestions yellowTeaSuggestions;

    @Mock
    Application application;
    @Mock
    Resources resources;

    @Before
    public void setUp() {
        when(application.getResources()).thenReturn(resources);
        yellowTeaSuggestions = new YellowTeaSuggestions(application);
    }

    @Test
    public void getAmountTsSuggestion() {
        final int[] arrayTs = new int[]{1, 2};
        when(resources.getIntArray(R.array.new_tea_suggestions_yellow_tea_amount_ts)).thenReturn(arrayTs);

        assertThat(yellowTeaSuggestions.getAmountTsSuggestions()).isEqualTo(arrayTs);
    }

    @Test
    public void getAmountGrSuggestion() {
        final int[] arrayGr = new int[]{1, 2};
        when(resources.getIntArray(R.array.new_tea_suggestions_yellow_tea_amount_gr)).thenReturn(arrayGr);

        assertThat(yellowTeaSuggestions.getAmountGrSuggestions()).isEqualTo(arrayGr);
    }

    @Test
    public void getAmountTbSuggestion() {
        final int[] arrayTb = new int[]{1, 2};
        when(resources.getIntArray(R.array.new_tea_suggestions_yellow_tea_amount_tb)).thenReturn(arrayTb);

        assertThat(yellowTeaSuggestions.getAmountTbSuggestions()).isEqualTo(arrayTb);
    }

    @Test
    public void getTemperatureCelsiusSuggestion() {
        final int[] arrayCelsius = new int[]{1, 2};
        when(resources.getIntArray(R.array.new_tea_suggestions_yellow_tea_temperature_celsius)).thenReturn(arrayCelsius);

        assertThat(yellowTeaSuggestions.getTemperatureCelsiusSuggestions()).isEqualTo(arrayCelsius);
    }

    @Test
    public void getTemperatureFahrenheitSuggestion() {
        final int[] arrayFahrenheit = new int[]{1, 2};
        when(resources.getIntArray(R.array.new_tea_suggestions_yellow_tea_temperature_fahrenheit)).thenReturn(arrayFahrenheit);

        assertThat(yellowTeaSuggestions.getTemperatureFahrenheitSuggestions()).isEqualTo(arrayFahrenheit);
    }

    @Test
    public void getSteepingTimeSuggestion() {
        final String[] arrayTime = new String[]{"1:00", "2:30"};
        when(resources.getStringArray(R.array.new_tea_suggestions_yellow_tea_time)).thenReturn(arrayTime);

        assertThat(yellowTeaSuggestions.getTimeSuggestions()).isEqualTo(arrayTime);
    }
}
