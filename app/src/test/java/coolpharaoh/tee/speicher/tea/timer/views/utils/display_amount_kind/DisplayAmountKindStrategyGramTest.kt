package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.R
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class DisplayAmountKindStrategyGramTest {
    @MockK
    lateinit var application: Application
    @InjectMockKs
    lateinit var displayAmountKindStrategyGram: DisplayAmountKindStrategyGram

    @Test
    fun getTextShowTea() {
        every { application.getString(eq(R.string.show_tea_display_gr), any()) } returns "1.5 g/l"

        assertThat(displayAmountKindStrategyGram.getTextShowTea(1.5)).isEqualTo("1.5 g/l")
    }

    @Test
    fun getEmptyTextShowTea() {
        every { application.getString(R.string.show_tea_display_gr, "-") } returns "- g/l"

        assertThat(displayAmountKindStrategyGram.getTextShowTea(-500.0)).isEqualTo("- g/l")
    }

    @Test
    fun getImageResourceIdShowTea() {
        assertThat(displayAmountKindStrategyGram.getImageResourceIdShowTea()).isEqualTo(R.drawable.gram_black)
    }

    @Test
    fun getTextCalculatorShowTea() {
        val amountPerLiter = 1.5f
        val liter = 0.5f
        every { application.getString(R.string.show_tea_dialog_amount_per_amount_gr, amountPerLiter, liter) } returns "1.5 g / 0.5 l"

        assertThat(displayAmountKindStrategyGram.getTextCalculatorShowTea(amountPerLiter, liter)).isEqualTo("1.5 g / 0.5 l")
    }

    @Test
    fun getTextNewTea() {
        every { application.getString(eq(R.string.new_tea_edit_text_amount_text_gr), any()) } returns "1.5 g/l (gram/liter)"

        assertThat(displayAmountKindStrategyGram.getTextNewTea(1.5)).isEqualTo("1.5 g/l (gram/liter)")
    }

    @Test
    fun getEmptyTextNewTea() {
        every { application.getString(R.string.new_tea_edit_text_amount_text_gr, "-") } returns "- g/l (gram/liter)"

        assertThat(displayAmountKindStrategyGram.getTextNewTea(-500.0)).isEqualTo("- g/l (gram/liter)")
    }
}