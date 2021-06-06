package coolpharaoh.tee.speicher.tea.timer.views.show_tea.display_amount_kind;

import org.junit.Test;

import coolpharaoh.tee.speicher.tea.timer.R;

import static org.assertj.core.api.Assertions.assertThat;

public class DisplayAmountKindTeaSpoonTest {

    @Test
    public void getTextId() {
        final DisplayAmountKindTeaSpoon displayAmountKindTeaSpoon = new DisplayAmountKindTeaSpoon();
        assertThat(displayAmountKindTeaSpoon.getTextId()).isEqualTo(R.string.show_tea_display_ts);
    }

    @Test
    public void getResourceId() {
        final DisplayAmountKindTeaSpoon displayAmountKindTeaSpoon = new DisplayAmountKindTeaSpoon();
        assertThat(displayAmountKindTeaSpoon.getImageResourceId()).isEqualTo(R.drawable.spoon_black);
    }

    @Test
    public void getTextIdCalculator() {
        final DisplayAmountKindTeaSpoon displayAmountKindTeaSpoon = new DisplayAmountKindTeaSpoon();
        assertThat(displayAmountKindTeaSpoon.getTextIdCalculator()).isEqualTo(R.string.show_tea_dialog_amount_per_amount_ts);
    }
}
