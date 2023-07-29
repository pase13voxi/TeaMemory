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
class DisplayAmountKindStrategyTeaBagTest {

    private DisplayAmountKindStrategyTeaBag displayAmountKindStrategyTeaBag;
    @Mock
    Application application;

    @BeforeEach
    void setUp() {
        displayAmountKindStrategyTeaBag = new DisplayAmountKindStrategyTeaBag(application);
    }

    @Test
    void getTextShowTea() {
        when(application.getString(eq(R.string.show_tea_display_tb), anyString())).thenReturn("1.5 tb/l");

        assertThat(displayAmountKindStrategyTeaBag.getTextShowTea(1.5)).isEqualTo("1.5 tb/l");
    }

    @Test
    void getEmptyTextShowTea() {
        when(application.getString(R.string.show_tea_display_tb, "-")).thenReturn("- tb/l");

        assertThat(displayAmountKindStrategyTeaBag.getTextShowTea(-500)).isEqualTo("- tb/l");
    }

    @Test
    void getResourceIdShowTea() {
        assertThat(displayAmountKindStrategyTeaBag.getImageResourceIdShowTea()).isEqualTo(R.drawable.tea_bag_black);
    }

    @Test
    void getTextCalculatorShowTea() {
        final float amountPerLiter = 1.5f;
        final float liter = 0.5f;
        when(application.getString(R.string.show_tea_dialog_amount_per_amount_tb, amountPerLiter, liter)).thenReturn("1.5 tb / 0.5 l");

        assertThat(displayAmountKindStrategyTeaBag.getTextCalculatorShowTea(amountPerLiter, liter)).isEqualTo("1.5 tb / 0.5 l");
    }

    @Test
    void getTextNewTea() {
        when(application.getString(eq(R.string.new_tea_edit_text_amount_text_tb), anyString())).thenReturn("1.5 tb/l (teabag/liter)");

        assertThat(displayAmountKindStrategyTeaBag.getTextNewTea(1.5)).isEqualTo("1.5 tb/l (teabag/liter)");
    }

    @Test
    void getEmptyTextNewTea() {
        when(application.getString(R.string.new_tea_edit_text_amount_text_tb, "-")).thenReturn("- tb/l (teabag/liter)");

        assertThat(displayAmountKindStrategyTeaBag.getTextNewTea(-500)).isEqualTo("- tb/l (teabag/liter)");
    }
}
