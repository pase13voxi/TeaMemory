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
internal class DisplayAmountKindStrategyTeaSpoonTest {
    @MockK
    lateinit var application: Application
    @InjectMockKs
    lateinit var displayAmountKindStrategyTeaSpoon: DisplayAmountKindStrategyTeaSpoon

    @Test
    fun getTextShowTea() {
        every { application.getString(eq(R.string.show_tea_display_ts), any()) } returns "1.5 ts/l"

        assertThat(displayAmountKindStrategyTeaSpoon.getTextShowTea(1.5)).isEqualTo("1.5 ts/l")
    }

    @Test
    fun getEmptyTextShowTea() {
        every { application.getString(R.string.show_tea_display_ts, "-") } returns "- ts/l"

        assertThat(displayAmountKindStrategyTeaSpoon.getTextShowTea(-500.0)).isEqualTo("- ts/l")
    }

    @Test
    fun getResourceIdShowTea() {
        assertThat(displayAmountKindStrategyTeaSpoon.getImageResourceIdShowTea()).isEqualTo(R.drawable.spoon_black)
    }

    @Test
    fun getTextCalculatorShowTea() {
        val amountPerLiter = 1.5f
        val liter = 0.5f
        every { application.getString(R.string.show_tea_dialog_amount_per_amount_ts, amountPerLiter, liter) } returns "1.5 ts / 0.5 l"

        assertThat(displayAmountKindStrategyTeaSpoon.getTextCalculatorShowTea(amountPerLiter, liter)).isEqualTo("1.5 ts / 0.5 l")
    }

    @Test
    fun getTextNewTea() {
        every { application.getString(eq(R.string.new_tea_edit_text_amount_text_ts), any()) } returns "1.5 ts/l (teaspoon/liter)"

        assertThat(displayAmountKindStrategyTeaSpoon.getTextNewTea(1.5)).isEqualTo("1.5 ts/l (teaspoon/liter)")
    }

    @Test
    fun getEmptyTextNewTea() {
        every { application.getString(R.string.new_tea_edit_text_amount_text_ts, "-") } returns "- ts/l (teaspoon/liter)"

        assertThat(displayAmountKindStrategyTeaSpoon.getTextNewTea(-500.0)).isEqualTo("- ts/l (teaspoon/liter)")
    }
}