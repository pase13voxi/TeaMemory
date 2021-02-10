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
public class PuerhTeaSuggestionsTest {

    private Suggestions puerhTeaSuggestions;

    @Mock
    Application application;
    @Mock
    Resources resources;

    @Before
    public void setUp() {
        when(application.getResources()).thenReturn(resources);
        puerhTeaSuggestions = new PuerhTeaSuggestions(application);
    }

    @Test
    public void getAmountTsHint() {
        final int[] arrayTs = new int[]{1, 2};
        when(resources.getIntArray(R.array.suggestions_puerh_tea_amount_ts)).thenReturn(arrayTs);

        assertThat(puerhTeaSuggestions.getAmountTsSuggestions()).isEqualTo(arrayTs);
    }

    @Test
    public void getAmountGrHint() {
        final int[] arrayGr = new int[]{1, 2};
        when(resources.getIntArray(R.array.suggestions_puerh_tea_amount_gr)).thenReturn(arrayGr);

        assertThat(puerhTeaSuggestions.getAmountGrSuggestions()).isEqualTo(arrayGr);
    }

    @Test
    public void getTemperatureCelsiusHint() {
        final int[] arrayCelsius = new int[]{1, 2};
        when(resources.getIntArray(R.array.suggestions_puerh_tea_temperature_celsius)).thenReturn(arrayCelsius);

        assertThat(puerhTeaSuggestions.getTemperatureCelsiusSuggestions()).isEqualTo(arrayCelsius);
    }

    @Test
    public void getTemperatureFahrenheitHint() {
        final int[] arrayFahrenheit = new int[]{1, 2};
        when(resources.getIntArray(R.array.suggestions_puerh_tea_temperature_fahrenheit)).thenReturn(arrayFahrenheit);

        assertThat(puerhTeaSuggestions.getTemperatureFahrenheitSuggestions()).isEqualTo(arrayFahrenheit);
    }

    @Test
    public void getSteepingTimeHint() {
        final String[] arrayTime = new String[]{"1:00", "2:30"};
        when(resources.getStringArray(R.array.suggestions_puerh_tea_time)).thenReturn(arrayTime);

        assertThat(puerhTeaSuggestions.getTimeSuggestions()).isEqualTo(arrayTime);
    }
}
