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
internal class DisplayAmountKindStrategyTeaBagTest {
    @MockK
    lateinit var application: Application
    @InjectMockKs
    lateinit var displayAmountKindStrategyTeaBag: DisplayAmountKindStrategyTeaBag

    @Test
    fun getTextShowTea() {
        every { application.getString(eq(R.string.show_tea_display_tb), any()) } returns "1.5 tb/l"

        assertThat(displayAmountKindStrategyTeaBag.getTextShowTea(1.5)).isEqualTo("1.5 tb/l")
    }

    @Test
    fun getEmptyTextShowTea() {
        every { application.getString(R.string.show_tea_display_tb, "-") } returns "- tb/l"

        assertThat(displayAmountKindStrategyTeaBag.getTextShowTea(-500.0)).isEqualTo("- tb/l")
    }

    @Test
    fun getResourceIdShowTea() {
        assertThat(displayAmountKindStrategyTeaBag.getImageResourceIdShowTea()).isEqualTo(R.drawable.tea_bag_black)
    }

    @Test
    fun getTextCalculatorShowTea() {
        val amountPerLiter = 1.5f
        val liter = 0.5f
        every { application.getString(R.string.show_tea_dialog_amount_per_amount_tb, amountPerLiter, liter) } returns "1.5 tb / 0.5 l"

        assertThat(displayAmountKindStrategyTeaBag.getTextCalculatorShowTea(amountPerLiter, liter)).isEqualTo("1.5 tb / 0.5 l")
    }

    @Test
    fun getTextNewTea() {
        every { application.getString(eq(R.string.new_tea_edit_text_amount_text_tb), any()) } returns "1.5 tb/l (teabag/liter)"

        assertThat(displayAmountKindStrategyTeaBag.getTextNewTea(1.5)).isEqualTo("1.5 tb/l (teabag/liter)")
    }

    @Test
    fun getEmptyTextNewTea() {
        every { application.getString(R.string.new_tea_edit_text_amount_text_tb, "-") } returns "- tb/l (teabag/liter)"

        assertThat(displayAmountKindStrategyTeaBag.getTextNewTea(-500.0)).isEqualTo("- tb/l (teabag/liter)")
    }
}