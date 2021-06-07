package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind;

import org.junit.Test;

import coolpharaoh.tee.speicher.tea.timer.R;

import static org.assertj.core.api.Assertions.assertThat;

public class DisplayAmountKindTeaBagTest {

    @Test
    public void getTextIdShowTea() {
        final DisplayAmountKindTeaBag displayAmountKindTeaBag = new DisplayAmountKindTeaBag();
        assertThat(displayAmountKindTeaBag.getTextIdShowTea()).isEqualTo(R.string.show_tea_display_tb);
    }

    @Test
    public void getResourceIdShowTea() {
        final DisplayAmountKindTeaBag displayAmountKindTeaBag = new DisplayAmountKindTeaBag();
        assertThat(displayAmountKindTeaBag.getImageResourceIdShowTea()).isEqualTo(R.drawable.tea_bag_black);
    }

    @Test
    public void getTextIdCalculatorShowTea() {
        final DisplayAmountKindTeaBag displayAmountKindTeaBag = new DisplayAmountKindTeaBag();
        assertThat(displayAmountKindTeaBag.getTextIdCalculatorShowTea()).isEqualTo(R.string.show_tea_dialog_amount_per_amount_tb);
    }

    @Test
    public void getTextIdNewTea() {
        final DisplayAmountKindTeaBag displayAmountKindTeaBag = new DisplayAmountKindTeaBag();
        assertThat(displayAmountKindTeaBag.getTextIdNewTea()).isEqualTo(R.string.new_tea_edit_text_amount_text_tb);
    }
}
