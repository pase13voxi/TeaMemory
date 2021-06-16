package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind;

import org.junit.Test;

import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind;

import static org.assertj.core.api.Assertions.assertThat;

public class DisplayAmountKindFactoryTest {

    @Test
    public void getDisplayAmountKindGram() {
        final DisplayAmountKind displayAmountKind = DisplayAmountKindFactory.get(AmountKind.GRAM, null);
        assertThat(displayAmountKind).isInstanceOf(DisplayAmountKindGram.class);
    }

    @Test
    public void getDisplayAmountKindTeaBag() {
        final DisplayAmountKind displayAmountKind = DisplayAmountKindFactory.get(AmountKind.TEA_BAG, null);
        assertThat(displayAmountKind).isInstanceOf(DisplayAmountKindTeaBag.class);
    }

    @Test
    public void getDisplayAmountKindTeaSpoon() {
        final DisplayAmountKind displayAmountKind = DisplayAmountKindFactory.get(AmountKind.TEA_SPOON, null);
        assertThat(displayAmountKind).isInstanceOf(DisplayAmountKindTeaSpoon.class);
    }
}
