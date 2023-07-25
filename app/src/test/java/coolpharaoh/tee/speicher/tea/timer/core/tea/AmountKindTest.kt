package coolpharaoh.tee.speicher.tea.timer.core.tea

import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind.Companion.fromChoice
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind.Companion.fromText
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AmountKindTest {
    @Test
    fun getTextGram() {
        val amountKind = AmountKind.GRAM
        assertThat(amountKind.text).isEqualTo("Gr")
    }

    @Test
    fun choiceGram() {
        val amountKind = AmountKind.TEA_SPOON
        assertThat(amountKind.choice).isZero
    }

    @Test
    fun amountKindFromTextGram() {
        val amountKind = fromText("Gr")
        assertThat(amountKind).isEqualTo(AmountKind.GRAM)
    }

    @Test
    fun amountKindFromTextNotDefined() {
        val amountKind = fromText("not defined")
        assertThat(amountKind).isEqualTo(AmountKind.TEA_SPOON)
    }

    @Test
    fun amountKindFromTextNull() {
        val amountKind = fromText(null)
        assertThat(amountKind).isEqualTo(AmountKind.TEA_SPOON)
    }

    @Test
    fun amountKindFromChoiceZero() {
        val amountKind = fromChoice(1)
        assertThat(amountKind).isEqualTo(AmountKind.GRAM)
    }

    @Test
    fun amountKindFromChoiceMinusOne() {
        val amountKind = fromChoice(-1)
        assertThat(amountKind).isEqualTo(AmountKind.TEA_SPOON)
    }
}