package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind;

import android.app.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import coolpharaoh.tee.speicher.tea.timer.R;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DisplayAmountKindTeaSpoonTest {

    private DisplayAmountKindTeaSpoon displayAmountKindTeaSpoon;
    @Mock
    Application application;

    @Before
    public void setUp() {
        displayAmountKindTeaSpoon = new DisplayAmountKindTeaSpoon(application);
    }

    @Test
    public void getTextShowTea() {
        when(application.getString(eq(R.string.show_tea_display_ts), anyString())).thenReturn("1.5 ts/l");

        assertThat(displayAmountKindTeaSpoon.getTextShowTea(1.5)).isEqualTo("1.5 ts/l");
    }

    @Test
    public void getEmptyTextShowTea() {
        when(application.getString(R.string.show_tea_display_ts, "-")).thenReturn("- ts/l");

        assertThat(displayAmountKindTeaSpoon.getTextShowTea(-500)).isEqualTo("- ts/l");
    }

    @Test
    public void getResourceIdShowTea() {
        assertThat(displayAmountKindTeaSpoon.getImageResourceIdShowTea()).isEqualTo(R.drawable.spoon_black);
    }

    @Test
    public void getTextCalculatorShowTea() {
        final float amountPerLiter = 1.5f;
        final float liter = 0.5f;
        when(application.getString(R.string.show_tea_dialog_amount_per_amount_ts, amountPerLiter, liter)).thenReturn("1.5 ts / 0.5 l");

        assertThat(displayAmountKindTeaSpoon.getTextCalculatorShowTea(amountPerLiter, liter)).isEqualTo("1.5 ts / 0.5 l");
    }

    @Test
    public void getTextNewTea() {
        when(application.getString(eq(R.string.new_tea_edit_text_amount_text_ts), anyString())).thenReturn("1.5 ts/l (teaspoon/liter)");

        assertThat(displayAmountKindTeaSpoon.getTextNewTea(1.5)).isEqualTo("1.5 ts/l (teaspoon/liter)");
    }

    @Test
    public void getEmptyTextNewTea() {
        when(application.getString(R.string.new_tea_edit_text_amount_text_ts, "-")).thenReturn("- ts/l (teaspoon/liter)");

        assertThat(displayAmountKindTeaSpoon.getTextNewTea(-500)).isEqualTo("- ts/l (teaspoon/liter)");
    }
}
