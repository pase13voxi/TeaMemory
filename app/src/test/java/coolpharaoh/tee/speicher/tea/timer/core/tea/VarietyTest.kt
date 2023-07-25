package coolpharaoh.tee.speicher.tea.timer.core.tea

import android.app.Application
import android.content.res.Resources
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.convertStoredVarietyToText
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.convertTextToStoredVariety
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.fromChoice
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.fromStoredText
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class VarietyTest {
    @Mock
    private val application: Application? = null

    @Mock
    private val resources: Resources? = null

    private lateinit var varieties: Array<String>

    @Test
    fun getStoredTextFromGreenTea() {
        assertThat(Variety.GREEN_TEA.code).isEqualTo("02_green")
    }

    @Test
    fun getColorFromGreenTea() {
        assertThat(Variety.GREEN_TEA.color).isEqualTo(R.color.greentea)
    }

    @Test
    fun choiceFromGreenTea() {
        assertThat(Variety.GREEN_TEA.choice).isEqualTo(1)
    }

    @Test
    fun varietyFromStoredTextGreenTea() {
        val variety = fromStoredText(Variety.GREEN_TEA.code)
        assertThat(variety).isEqualTo(Variety.GREEN_TEA)
    }

    @Test
    fun varietyFromStoredTextNotDefined() {
        val notDefinedText = "not defined"
        val variety = fromStoredText(notDefinedText)
        assertThat(variety).isEqualTo(Variety.OTHER)
    }

    @Test
    fun varietyFromStoredTextNull() {
        val variety = fromStoredText(null)
        assertThat(variety).isEqualTo(Variety.OTHER)
    }

    @Test
    fun varietyFromChoiceGreenTea() {
        val variety = fromChoice(Variety.GREEN_TEA.choice)
        assertThat(variety).isEqualTo(Variety.GREEN_TEA)
    }

    @Test
    fun varietyFromChoiceMinusValue() {
        val variety = fromChoice(-1)
        assertThat(variety).isEqualTo(Variety.OTHER)
    }

    @Test
    fun convertBlackTeaVarietyToCode() {
        mockVarietyStrings()
        val code = convertTextToStoredVariety(varieties[0], application!!)
        assertThat(code).isEqualTo(Variety.BLACK_TEA.code)
    }

    @Test
    fun convertOolongTeaVarietyToCode() {
        mockVarietyStrings()
        val code = convertTextToStoredVariety(varieties[4], application!!)
        assertThat(code).isEqualTo(Variety.OOLONG_TEA.code)
    }

    @Test
    fun convertVarietyToCodeReturnInputBecauseVarietyNotExist() {
        mockVarietyStrings()
        val otherVariety = "Other Variety"
        val code = convertTextToStoredVariety(otherVariety, application!!)
        assertThat(code).isEqualTo(otherVariety)
    }

    @Test
    fun convertBlackTeaCodeToVariety() {
        mockVarietyStrings()
        val variety = convertStoredVarietyToText(Variety.BLACK_TEA.code, application!!)
        assertThat(variety).isEqualTo(varieties[0])
    }

    @Test
    fun convertOolongTeaCodeToVariety() {
        mockVarietyStrings()
        val variety = convertStoredVarietyToText(Variety.OOLONG_TEA.code, application!!)
        assertThat(variety).isEqualTo(varieties[4])
    }

    @Test
    fun convertCodeToVarietyReturnInputBecauseCodeNotExist() {
        mockVarietyStrings()
        val otherCode = "Other Code"
        val variety = convertStoredVarietyToText(otherCode, application!!)
        assertThat(variety).isEqualTo(otherCode)
    }

    private fun mockVarietyStrings() {
        Mockito.`when`(application!!.resources).thenReturn(resources)
        varieties = arrayOf(
            "Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
            "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"
        )
        Mockito.`when`(resources!!.getStringArray(R.array.new_tea_variety_teas))
            .thenReturn(varieties)
    }
}