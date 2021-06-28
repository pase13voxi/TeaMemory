package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind;

import org.junit.Test;

import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind;

import static org.assertj.core.api.Assertions.assertThat;

public class DisplayAmountKindStrategyFactoryTest {

    @Test
    public void getDisplayAmountKindGram() {
        final DisplayAmountKindStrategy displayAmountKindStrategy = DisplayAmountKindFactory.get(AmountKind.GRAM, null);
        assertThat(displayAmountKindStrategy).isInstanceOf(DisplayAmountKindStrategyGram.class);
    }

    @Test
    public void getDisplayAmountKindTeaBag() {
        final DisplayAmountKindStrategy displayAmountKindStrategy = DisplayAmountKindFactory.get(AmountKind.TEA_BAG, null);
        assertThat(displayAmountKindStrategy).isInstanceOf(DisplayAmountKindStrategyTeaBag.class);
    }

    @Test
    public void getDisplayAmountKindTeaSpoon() {
        final DisplayAmountKindStrategy displayAmountKindStrategy = DisplayAmountKindFactory.get(AmountKind.TEA_SPOON, null);
        assertThat(displayAmountKindStrategy).isInstanceOf(DisplayAmountKindStrategyTeaSpoon.class);
    }
}
