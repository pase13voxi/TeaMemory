package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind;

class DisplayAmountKindStrategyFactoryTest {

    @Test
    void getDisplayAmountKindGram() {
        final DisplayAmountKindStrategy displayAmountKindStrategy = DisplayAmountKindFactory.get(AmountKind.GRAM, null);
        assertThat(displayAmountKindStrategy).isInstanceOf(DisplayAmountKindStrategyGram.class);
    }

    @Test
    void getDisplayAmountKindTeaBag() {
        final DisplayAmountKindStrategy displayAmountKindStrategy = DisplayAmountKindFactory.get(AmountKind.TEA_BAG, null);
        assertThat(displayAmountKindStrategy).isInstanceOf(DisplayAmountKindStrategyTeaBag.class);
    }

    @Test
    void getDisplayAmountKindTeaSpoon() {
        final DisplayAmountKindStrategy displayAmountKindStrategy = DisplayAmountKindFactory.get(AmountKind.TEA_SPOON, null);
        assertThat(displayAmountKindStrategy).isInstanceOf(DisplayAmountKindStrategyTeaSpoon.class);
    }
}
