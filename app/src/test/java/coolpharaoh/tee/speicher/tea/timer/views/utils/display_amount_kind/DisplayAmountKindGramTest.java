package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind;

import org.junit.Test;

import coolpharaoh.tee.speicher.tea.timer.R;

import static org.assertj.core.api.Assertions.assertThat;

public class DisplayAmountKindGramTest {

    @Test
    public void getTextId() {
        final DisplayAmountKindGram displayAmountKindGram = new DisplayAmountKindGram();
        assertThat(displayAmountKindGram.getTextIdShowTea()).isEqualTo(R.string.show_tea_display_gr);
    }

    @Test
    public void getResourceId() {
        final DisplayAmountKindGram displayAmountKindGram = new DisplayAmountKindGram();
        assertThat(displayAmountKindGram.getImageResourceIdShowTea()).isEqualTo(R.drawable.gram_black);
    }

    @Test
    public void getTextIdCalculator() {
        final DisplayAmountKindGram displayAmountKindGram = new DisplayAmountKindGram();
        assertThat(displayAmountKindGram.getTextIdCalculatorShowTea()).isEqualTo(R.string.show_tea_dialog_amount_per_amount_gr);
    }

    @Test
    public void getTextIdNewTea() {
        final DisplayAmountKindGram displayAmountKindGram = new DisplayAmountKindGram();
        assertThat(displayAmountKindGram.getTextIdNewTea()).isEqualTo(R.string.new_tea_edit_text_amount_text_gr);
    }
}
