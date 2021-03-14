package coolpharaoh.tee.speicher.tea.timer.views.newtea.suggestions;

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
public class BlackTeaSuggestionsTest {

    private Suggestions blackTeaHints;

    @Mock
    Application application;
    @Mock
    Resources resources;

    @Before
    public void setUp() {
        when(application.getResources()).thenReturn(resources);
        blackTeaHints = new BlackTeaSuggestions(application);
    }

    @Test
    public void getAmountTsHint() {
        final int[] arrayTs = new int[]{1, 2};
        when(resources.getIntArray(R.array.new_tea_suggestions_black_tea_amount_ts)).thenReturn(arrayTs);

        assertThat(blackTeaHints.getAmountTsSuggestions()).isEqualTo(arrayTs);
    }

    @Test
    public void getAmountGrHint() {
        final int[] arrayGr = new int[]{1, 2};
        when(resources.getIntArray(R.array.new_tea_suggestions_black_tea_amount_gr)).thenReturn(arrayGr);

        assertThat(blackTeaHints.getAmountGrSuggestions()).isEqualTo(arrayGr);
    }

    @Test
    public void getTemperatureCelsiusHint() {
        final int[] arrayCelsius = new int[]{1, 2};
        when(resources.getIntArray(R.array.new_tea_suggestions_black_tea_temperature_celsius)).thenReturn(arrayCelsius);

        assertThat(blackTeaHints.getTemperatureCelsiusSuggestions()).isEqualTo(arrayCelsius);
    }

    @Test
    public void getTemperatureFahrenheitHint() {
        final int[] arrayFahrenheit = new int[]{1, 2};
        when(resources.getIntArray(R.array.new_tea_suggestions_black_tea_temperature_fahrenheit)).thenReturn(arrayFahrenheit);

        assertThat(blackTeaHints.getTemperatureFahrenheitSuggestions()).isEqualTo(arrayFahrenheit);
    }

    @Test
    public void getSteepingTimeHint() {
        final String[] arrayTime = new String[]{"1:00", "2:30"};
        when(resources.getStringArray(R.array.new_tea_suggestions_black_tea_time)).thenReturn(arrayTime);

        assertThat(blackTeaHints.getTimeSuggestions()).isEqualTo(arrayTime);
    }
}