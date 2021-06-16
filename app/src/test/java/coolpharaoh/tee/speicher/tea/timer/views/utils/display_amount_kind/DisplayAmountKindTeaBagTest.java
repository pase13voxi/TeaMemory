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
public class DisplayAmountKindTeaBagTest {

    private DisplayAmountKindTeaBag displayAmountKindTeaBag;
    @Mock
    Application application;

    @Before
    public void setUp() {
        displayAmountKindTeaBag = new DisplayAmountKindTeaBag(application);
    }

    @Test
    public void getTextShowTea() {
        when(application.getString(eq(R.string.show_tea_display_tb), anyString())).thenReturn("1.5 tb/l");

        assertThat(displayAmountKindTeaBag.getTextShowTea(1.5)).isEqualTo("1.5 tb/l");
    }

    @Test
    public void getEmptyTextShowTea() {
        when(application.getString(R.string.show_tea_display_tb, "-")).thenReturn("- tb/l");

        assertThat(displayAmountKindTeaBag.getTextShowTea(-500)).isEqualTo("- tb/l");
    }

    @Test
    public void getResourceIdShowTea() {
        assertThat(displayAmountKindTeaBag.getImageResourceIdShowTea()).isEqualTo(R.drawable.tea_bag_black);
    }

    @Test
    public void getTextCalculatorShowTea() {
        final float amountPerLiter = 1.5f;
        final float liter = 0.5f;
        when(application.getString(R.string.show_tea_dialog_amount_per_amount_tb, amountPerLiter, liter)).thenReturn("1.5 tb / 0.5 l");

        assertThat(displayAmountKindTeaBag.getTextCalculatorShowTea(amountPerLiter, liter)).isEqualTo("1.5 tb / 0.5 l");
    }

    @Test
    public void getTextNewTea() {
        when(application.getString(eq(R.string.new_tea_edit_text_amount_text_tb), anyString())).thenReturn("1.5 tb/l (teabag/liter)");

        assertThat(displayAmountKindTeaBag.getTextNewTea(1.5)).isEqualTo("1.5 tb/l (teabag/liter)");
    }

    @Test
    public void getEmptyTextNewTea() {
        when(application.getString(R.string.new_tea_edit_text_amount_text_tb, "-")).thenReturn("- tb/l (teabag/liter)");

        assertThat(displayAmountKindTeaBag.getTextNewTea(-500)).isEqualTo("- tb/l (teabag/liter)");
    }
}
