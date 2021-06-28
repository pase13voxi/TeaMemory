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
public class DisplayAmountKindStrategyGramTest {

    private DisplayAmountKindStrategyGram displayAmountKindStrategyGram;
    @Mock
    Application application;

    @Before
    public void setUp() {
        displayAmountKindStrategyGram = new DisplayAmountKindStrategyGram(application);
    }

    @Test
    public void getTextShowTea() {
        when(application.getString(eq(R.string.show_tea_display_gr), anyString())).thenReturn("1.5 g/l");

        assertThat(displayAmountKindStrategyGram.getTextShowTea(1.5)).isEqualTo("1.5 g/l");
    }

    @Test
    public void getEmptyTextShowTea() {
        when(application.getString(R.string.show_tea_display_gr, "-")).thenReturn("- g/l");

        assertThat(displayAmountKindStrategyGram.getTextShowTea(-500)).isEqualTo("- g/l");
    }

    @Test
    public void getImageResourceIdShowTea() {
        assertThat(displayAmountKindStrategyGram.getImageResourceIdShowTea()).isEqualTo(R.drawable.gram_black);
    }

    @Test
    public void getTextCalculatorShowTea() {
        final float amountPerLiter = 1.5f;
        final float liter = 0.5f;
        when(application.getString(R.string.show_tea_dialog_amount_per_amount_gr, amountPerLiter, liter)).thenReturn("1.5 g / 0.5 l");

        assertThat(displayAmountKindStrategyGram.getTextCalculatorShowTea(amountPerLiter, liter)).isEqualTo("1.5 g / 0.5 l");
    }

    @Test
    public void getTextNewTea() {
        when(application.getString(eq(R.string.new_tea_edit_text_amount_text_gr), anyString())).thenReturn("1.5 g/l (gram/liter)");

        assertThat(displayAmountKindStrategyGram.getTextNewTea(1.5)).isEqualTo("1.5 g/l (gram/liter)");
    }

    @Test
    public void getEmptyTextNewTea() {
        when(application.getString(R.string.new_tea_edit_text_amount_text_gr, "-")).thenReturn("- g/l (gram/liter)");

        assertThat(displayAmountKindStrategyGram.getTextNewTea(-500)).isEqualTo("- g/l (gram/liter)");
    }
}
