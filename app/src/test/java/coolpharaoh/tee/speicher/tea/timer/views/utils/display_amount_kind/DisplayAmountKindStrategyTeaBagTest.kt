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
internal class DisplayAmountKindStrategyTeaBagTest {

    private var displayAmountKindStrategyTeaBag: DisplayAmountKindStrategyTeaBag? = null

    @Mock
    var application: Application? = null

    @BeforeEach
    fun setUp() {
        displayAmountKindStrategyTeaBag = DisplayAmountKindStrategyTeaBag(application!!)
    }

    @Test
    fun getTextShowTea() {
        `when`(application!!.getString(eq(R.string.show_tea_display_tb), anyString())).thenReturn("1.5 tb/l")

        assertThat(displayAmountKindStrategyTeaBag!!.getTextShowTea(1.5)).isEqualTo("1.5 tb/l")
    }

    @Test
    fun getEmptyTextShowTea() {
        `when`(application!!.getString(R.string.show_tea_display_tb, "-")).thenReturn("- tb/l")

        assertThat(displayAmountKindStrategyTeaBag!!.getTextShowTea(-500.0)).isEqualTo("- tb/l")
    }

    @Test
    fun getResourceIdShowTea() {
        assertThat(displayAmountKindStrategyTeaBag!!.getImageResourceIdShowTea()).isEqualTo(R.drawable.tea_bag_black)
    }

    @Test
    fun getTextCalculatorShowTea() {
        val amountPerLiter = 1.5f
        val liter = 0.5f
        `when`(application!!.getString(R.string.show_tea_dialog_amount_per_amount_tb, amountPerLiter, liter)).thenReturn("1.5 tb / 0.5 l")

        assertThat(displayAmountKindStrategyTeaBag!!.getTextCalculatorShowTea(amountPerLiter, liter)).isEqualTo("1.5 tb / 0.5 l")
    }

    @Test
    fun getTextNewTea() {
        `when`(application!!.getString(eq(R.string.new_tea_edit_text_amount_text_tb), anyString())).thenReturn("1.5 tb/l (teabag/liter)")

        assertThat(displayAmountKindStrategyTeaBag!!.getTextNewTea(1.5)).isEqualTo("1.5 tb/l (teabag/liter)")
    }

    @Test
    fun getEmptyTextNewTea() {
        `when`(application!!.getString(R.string.new_tea_edit_text_amount_text_tb, "-")).thenReturn("- tb/l (teabag/liter)")

        assertThat(displayAmountKindStrategyTeaBag!!.getTextNewTea(-500.0)).isEqualTo("- tb/l (teabag/liter)")
    }
}