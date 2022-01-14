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
class DisplayAmountKindStrategyTeaSpoonTest {

    private DisplayAmountKindStrategyTeaSpoon displayAmountKindStrategyTeaSpoon;
    @Mock
    Application application;

    @BeforeEach
    void setUp() {
        displayAmountKindStrategyTeaSpoon = new DisplayAmountKindStrategyTeaSpoon(application);
    }

    @Test
    void getTextShowTea() {
        when(application.getString(eq(R.string.show_tea_display_ts), anyString())).thenReturn("1.5 ts/l");

        assertThat(displayAmountKindStrategyTeaSpoon.getTextShowTea(1.5)).isEqualTo("1.5 ts/l");
    }

    @Test
    void getEmptyTextShowTea() {
        when(application.getString(R.string.show_tea_display_ts, "-")).thenReturn("- ts/l");

        assertThat(displayAmountKindStrategyTeaSpoon.getTextShowTea(-500)).isEqualTo("- ts/l");
    }

    @Test
    void getResourceIdShowTea() {
        assertThat(displayAmountKindStrategyTeaSpoon.getImageResourceIdShowTea()).isEqualTo(R.drawable.spoon_black);
    }

    @Test
    void getTextCalculatorShowTea() {
        final float amountPerLiter = 1.5f;
        final float liter = 0.5f;
        when(application.getString(R.string.show_tea_dialog_amount_per_amount_ts, amountPerLiter, liter)).thenReturn("1.5 ts / 0.5 l");

        assertThat(displayAmountKindStrategyTeaSpoon.getTextCalculatorShowTea(amountPerLiter, liter)).isEqualTo("1.5 ts / 0.5 l");
    }

    @Test
    void getTextNewTea() {
        when(application.getString(eq(R.string.new_tea_edit_text_amount_text_ts), anyString())).thenReturn("1.5 ts/l (teaspoon/liter)");

        assertThat(displayAmountKindStrategyTeaSpoon.getTextNewTea(1.5)).isEqualTo("1.5 ts/l (teaspoon/liter)");
    }

    @Test
    void getEmptyTextNewTea() {
        when(application.getString(R.string.new_tea_edit_text_amount_text_ts, "-")).thenReturn("- ts/l (teaspoon/liter)");

        assertThat(displayAmountKindStrategyTeaSpoon.getTextNewTea(-500)).isEqualTo("- ts/l (teaspoon/liter)");
    }
}
