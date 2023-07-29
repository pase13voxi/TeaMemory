package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DisplayAmountKindStrategyFactoryTest {

    @Test
    fun getDisplayAmountKindGram() {
        val displayAmountKindStrategy = DisplayAmountKindFactory[AmountKind.GRAM, Application()]
        assertThat(displayAmountKindStrategy).isInstanceOf(DisplayAmountKindStrategyGram::class.java)
    }

    @Test
    fun getDisplayAmountKindTeaBag() {
        val displayAmountKindStrategy = DisplayAmountKindFactory[AmountKind.TEA_BAG, Application()]
        assertThat(displayAmountKindStrategy).isInstanceOf(DisplayAmountKindStrategyTeaBag::class.java)
    }

    @Test
    fun getDisplayAmountKindTeaSpoon() {
        val displayAmountKindStrategy = DisplayAmountKindFactory[AmountKind.TEA_SPOON, Application()]
        assertThat(displayAmountKindStrategy).isInstanceOf(DisplayAmountKindStrategyTeaSpoon::class.java)
    }
}