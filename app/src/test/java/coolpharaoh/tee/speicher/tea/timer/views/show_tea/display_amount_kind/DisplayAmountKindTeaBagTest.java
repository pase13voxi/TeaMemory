package coolpharaoh.tee.speicher.tea.timer.views.show_tea.display_amount_kind;

import org.junit.Test;

import coolpharaoh.tee.speicher.tea.timer.R;

import static org.assertj.core.api.Assertions.assertThat;

public class DisplayAmountKindTeaBagTest {

    @Test
    public void getTextId() {
        final DisplayAmountKindTeaBag displayAmountKindTeaBag = new DisplayAmountKindTeaBag();
        assertThat(displayAmountKindTeaBag.getTextId()).isEqualTo(R.string.show_tea_display_tb);
    }

    @Test
    public void getResourceId() {
        final DisplayAmountKindTeaBag displayAmountKindTeaBag = new DisplayAmountKindTeaBag();
        assertThat(displayAmountKindTeaBag.getImageResourceId()).isEqualTo(R.drawable.tea_bag_black);
    }

    @Test
    public void getTextIdCalculator() {
        final DisplayAmountKindTeaBag displayAmountKindTeaBag = new DisplayAmountKindTeaBag();
        assertThat(displayAmountKindTeaBag.getTextIdCalculator()).isEqualTo(R.string.show_tea_dialog_amount_per_amount_tb);
    }
}
