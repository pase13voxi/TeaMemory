package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind;

import org.junit.Test;

import coolpharaoh.tee.speicher.tea.timer.R;

import static org.assertj.core.api.Assertions.assertThat;

public class DisplayAmountKindTeaSpoonTest {

    @Test
    public void getTextIdShowTea() {
        final DisplayAmountKindTeaSpoon displayAmountKindTeaSpoon = new DisplayAmountKindTeaSpoon();
        assertThat(displayAmountKindTeaSpoon.getTextIdShowTea()).isEqualTo(R.string.show_tea_display_ts);
    }

    @Test
    public void getResourceIdShowTea() {
        final DisplayAmountKindTeaSpoon displayAmountKindTeaSpoon = new DisplayAmountKindTeaSpoon();
        assertThat(displayAmountKindTeaSpoon.getImageResourceIdShowTea()).isEqualTo(R.drawable.spoon_black);
    }

    @Test
    public void getTextIdCalculatorShowTea() {
        final DisplayAmountKindTeaSpoon displayAmountKindTeaSpoon = new DisplayAmountKindTeaSpoon();
        assertThat(displayAmountKindTeaSpoon.getTextIdCalculatorShowTea()).isEqualTo(R.string.show_tea_dialog_amount_per_amount_ts);
    }

    @Test
    public void getTextIdNewTea() {
        final DisplayAmountKindTeaSpoon displayAmountKindTeaSpoon = new DisplayAmountKindTeaSpoon();
        assertThat(displayAmountKindTeaSpoon.getTextIdNewTea()).isEqualTo(R.string.new_tea_edit_text_amount_text_ts);
    }
}
