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
internal class DisplayAmountKindStrategyGramTest {

    private var displayAmountKindStrategyGram: DisplayAmountKindStrategyGram? = null

    @Mock
    var application: Application? = null

    @BeforeEach
    fun setUp() {
        displayAmountKindStrategyGram = DisplayAmountKindStrategyGram(application!!)
    }

    @Test
    fun getTextShowTea() {
        `when`(application!!.getString(eq(R.string.show_tea_display_gr), anyString())).thenReturn("1.5 g/l")

        assertThat(displayAmountKindStrategyGram!!.getTextShowTea(1.5)).isEqualTo("1.5 g/l")
    }

    @Test
    fun getEmptyTextShowTea() {
        `when`(application!!.getString(R.string.show_tea_display_gr, "-")).thenReturn("- g/l")

        assertThat(displayAmountKindStrategyGram!!.getTextShowTea(-500.0)).isEqualTo("- g/l")
    }

    @Test
    fun getImageResourceIdShowTea() {
        assertThat(displayAmountKindStrategyGram!!.getImageResourceIdShowTea()).isEqualTo(R.drawable.gram_black)
    }

    @Test
    fun getTextCalculatorShowTea() {
        val amountPerLiter = 1.5f
        val liter = 0.5f
        `when`(application!!.getString(R.string.show_tea_dialog_amount_per_amount_gr, amountPerLiter, liter)).thenReturn("1.5 g / 0.5 l")

        assertThat(displayAmountKindStrategyGram!!.getTextCalculatorShowTea(amountPerLiter, liter)).isEqualTo("1.5 g / 0.5 l")
    }

    @Test
    fun getTextNewTea() {
        `when`(application!!.getString(eq(R.string.new_tea_edit_text_amount_text_gr), anyString())).thenReturn("1.5 g/l (gram/liter)")

        assertThat(displayAmountKindStrategyGram!!.getTextNewTea(1.5)).isEqualTo("1.5 g/l (gram/liter)")
    }

    @Test
    fun getEmptyTextNewTea() {
        `when`(application!!.getString(R.string.new_tea_edit_text_amount_text_gr, "-")).thenReturn("- g/l (gram/liter)")

        assertThat(displayAmountKindStrategyGram!!.getTextNewTea(-500.0)).isEqualTo("- g/l (gram/liter)")
    }
}