package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.R
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class DisplayAmountKindStrategyTeaSpoonTest {

    private var displayAmountKindStrategyTeaSpoon: DisplayAmountKindStrategyTeaSpoon? = null

    @Mock
    var application: Application? = null

    @BeforeEach
    fun setUp() {
        displayAmountKindStrategyTeaSpoon = DisplayAmountKindStrategyTeaSpoon(application!!)
    }

    @Test
    fun getTextShowTea() {
        `when`(application!!.getString(eq(R.string.show_tea_display_ts), anyString())).thenReturn("1.5 ts/l")

        assertThat(displayAmountKindStrategyTeaSpoon!!.getTextShowTea(1.5)).isEqualTo("1.5 ts/l")
    }

    @Test
    fun getEmptyTextShowTea() {
        `when`(application!!.getString(R.string.show_tea_display_ts, "-")).thenReturn("- ts/l")

        assertThat(displayAmountKindStrategyTeaSpoon!!.getTextShowTea(-500.0)).isEqualTo("- ts/l")
    }

    @Test
    fun getResourceIdShowTea() {
        assertThat(displayAmountKindStrategyTeaSpoon!!.getImageResourceIdShowTea()).isEqualTo(R.drawable.spoon_black)
    }

    @Test
    fun getTextCalculatorShowTea() {
        val amountPerLiter = 1.5f
        val liter = 0.5f
        `when`(application!!.getString(R.string.show_tea_dialog_amount_per_amount_ts, amountPerLiter, liter)).thenReturn("1.5 ts / 0.5 l")

        assertThat(displayAmountKindStrategyTeaSpoon!!.getTextCalculatorShowTea(amountPerLiter, liter)).isEqualTo("1.5 ts / 0.5 l")
    }

    @Test
    fun getTextNewTea() {
        `when`(application!!.getString(eq(R.string.new_tea_edit_text_amount_text_ts), anyString())).thenReturn("1.5 ts/l (teaspoon/liter)")

        assertThat(displayAmountKindStrategyTeaSpoon!!.getTextNewTea(1.5)).isEqualTo("1.5 ts/l (teaspoon/liter)")
    }

    @Test
    fun getEmptyTextNewTea() {
        `when`(application!!.getString(R.string.new_tea_edit_text_amount_text_ts, "-")).thenReturn("- ts/l (teaspoon/liter)")

        assertThat(displayAmountKindStrategyTeaSpoon!!.getTextNewTea(-500.0)).isEqualTo("- ts/l (teaspoon/liter)")
    }
}