package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import android.app.Application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import coolpharaoh.tee.speicher.tea.timer.R;

@ExtendWith(MockitoExtension.class)
class DisplayAmountKindStrategyGramTest {

    private DisplayAmountKindStrategyGram displayAmountKindStrategyGram;
    @Mock
    Application application;

    @BeforeEach
    void setUp() {
        displayAmountKindStrategyGram = new DisplayAmountKindStrategyGram(application);
    }

    @Test
    void getTextShowTea() {
        when(application.getString(eq(R.string.show_tea_display_gr), anyString())).thenReturn("1.5 g/l");

        assertThat(displayAmountKindStrategyGram.getTextShowTea(1.5)).isEqualTo("1.5 g/l");
    }

    @Test
    void getEmptyTextShowTea() {
        when(application.getString(R.string.show_tea_display_gr, "-")).thenReturn("- g/l");

        assertThat(displayAmountKindStrategyGram.getTextShowTea(-500)).isEqualTo("- g/l");
    }

    @Test
    void getImageResourceIdShowTea() {
        assertThat(displayAmountKindStrategyGram.getImageResourceIdShowTea()).isEqualTo(R.drawable.gram_black);
    }

    @Test
    void getTextCalculatorShowTea() {
        final float amountPerLiter = 1.5f;
        final float liter = 0.5f;
        when(application.getString(R.string.show_tea_dialog_amount_per_amount_gr, amountPerLiter, liter)).thenReturn("1.5 g / 0.5 l");

        assertThat(displayAmountKindStrategyGram.getTextCalculatorShowTea(amountPerLiter, liter)).isEqualTo("1.5 g / 0.5 l");
    }

    @Test
    void getTextNewTea() {
        when(application.getString(eq(R.string.new_tea_edit_text_amount_text_gr), anyString())).thenReturn("1.5 g/l (gram/liter)");

        assertThat(displayAmountKindStrategyGram.getTextNewTea(1.5)).isEqualTo("1.5 g/l (gram/liter)");
    }

    @Test
    void getEmptyTextNewTea() {
        when(application.getString(R.string.new_tea_edit_text_amount_text_gr, "-")).thenReturn("- g/l (gram/liter)");

        assertThat(displayAmountKindStrategyGram.getTextNewTea(-500)).isEqualTo("- g/l (gram/liter)");
    }
}
