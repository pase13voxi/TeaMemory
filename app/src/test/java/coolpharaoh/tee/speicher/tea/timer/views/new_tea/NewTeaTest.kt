package coolpharaoh.tee.speicher.tea.timer.views.new_tea

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.convertStoredVarietyToText
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase.Companion.setMockedDatabase
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.fakes.RoboMenuItem
import org.robolectric.shadows.ShadowAlertDialog
import org.robolectric.shadows.ShadowInstrumentation
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.Date

@RunWith(RobolectricTestRunner::class)
class NewTeaTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    lateinit var teaMemoryDatabase: TeaMemoryDatabase
    @RelaxedMockK
    lateinit var teaDao: TeaDao
    @RelaxedMockK
    lateinit var infusionDao: InfusionDao

    @Before
    fun setUp() {
        mockDB()
    }

    private fun mockDB() {
        setMockedDatabase(teaMemoryDatabase)
        every { teaMemoryDatabase.teaDao } returns teaDao
        every { teaMemoryDatabase.infusionDao } returns infusionDao
    }

    @Test
    fun showActivityAddModeAndExpectFilledDefaultValues() {
        val newTeaActivityScenario = ActivityScenario.launch(NewTea::class.java)
        newTeaActivityScenario.onActivity { newTea: NewTea ->
            val editTextVariety = newTea.findViewById<EditText>(R.id.edit_text_new_tea_variety)
            val editTextName = newTea.findViewById<EditText>(R.id.edit_text_new_tea_name)
            val editTextAmount = newTea.findViewById<EditText>(R.id.edit_text_new_tea_amount)
            val editTextTemperature = newTea.findViewById<EditText>(R.id.edit_text_new_tea_temperature)
            val editTextCoolDownTime = newTea.findViewById<EditText>(R.id.edit_text_new_tea_cool_down_time)
            val editTextTime = newTea.findViewById<EditText>(R.id.edit_text_new_tea_time)

            val varieties = newTea.resources.getStringArray(R.array.new_tea_variety_teas)
            assertThat(editTextVariety.text).hasToString(varieties[0])
            assertThat(editTextName.text.toString()).isBlank
            assertThat(editTextAmount.text).hasToString(newTea.getString(R.string.new_tea_edit_text_amount_empty_text_ts))
            assertThat(editTextTemperature.text).hasToString(newTea.getString(R.string.new_tea_edit_text_temperature_text_celsius, "-"))
            assertThat(editTextCoolDownTime.text).hasToString(newTea.getString(R.string.new_tea_edit_text_cool_down_time_empty_text))
            assertThat(editTextTime.text).hasToString(newTea.getString(R.string.new_tea_edit_text_time_empty_text))
        }
    }

    @Test
    fun addNewTeaAndExpectSavedDefaultValues() {
        val newTeaActivityScenario = ActivityScenario.launch(NewTea::class.java)
        newTeaActivityScenario.onActivity { newTea: NewTea ->
            val editTextName = newTea.findViewById<EditText>(R.id.edit_text_new_tea_name)
            editTextName.setText("Name")
            newTea.onOptionsItemSelected(RoboMenuItem(R.id.action_new_tea_done))

            val slotTea = slot<Tea>()
            verify { teaDao.insert(capture(slotTea)) }
            assertThat(slotTea.captured).extracting(
                Tea::name,
                Tea::variety,
                Tea::color,
                Tea::amount,
                Tea::amountKind,
                Tea::rating,
                Tea::inStock
            ).containsExactly("Name", Variety.BLACK_TEA.code, -15461296, -500.0, AmountKind.TEA_SPOON.text, 0, true)

            val slotInfusion = slot<Infusion>()
            verify { infusionDao.insert(capture(slotInfusion)) }
            assertThat(slotInfusion.captured).extracting(
                Infusion::temperatureCelsius,
                Infusion::temperatureFahrenheit,
                Infusion::coolDownTime,
                Infusion::time
            ).containsExactly(-500, -500, null, null)
        }
    }

    @Test
    fun changeVarietyAndExpectUpdatedVariety() {
        val newTeaActivityScenario = ActivityScenario.launch(NewTea::class.java)
        newTeaActivityScenario.onActivity { newTea: NewTea ->
            val editTextVariety = newTea.findViewById<EditText>(R.id.edit_text_new_tea_variety)
            editTextVariety.performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val dialog = ShadowAlertDialog.getLatestAlertDialog()

            val radioButtons = getRadioButtons(dialog)

            radioButtons[3].performClick()

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val varieties = newTea.resources.getStringArray(R.array.new_tea_variety_teas)
            assertThat(editTextVariety.text).hasToString(varieties[3])
        }
    }

    @Test
    fun changeColorAndExpectUpdatedColor() {
        val newTeaActivityScenario = ActivityScenario.launch(NewTea::class.java)
        newTeaActivityScenario.onActivity { newTea: NewTea ->
            val buttonColor = newTea.findViewById<Button>(R.id.button_new_tea_color)
            buttonColor.performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            assertThat(ShadowAlertDialog.getLatestDialog()).isNotNull()
        }
    }

    @Test
    fun changeAmountAndExpectUpdatedAmount() {
        val newTeaActivityScenario = ActivityScenario.launch(NewTea::class.java)
        newTeaActivityScenario.onActivity { newTea: NewTea ->
            val editTextAmount = newTea.findViewById<EditText>(R.id.edit_text_new_tea_amount)
            editTextAmount.performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val dialog = ShadowAlertDialog.getLatestAlertDialog()

            val amountPicker = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount)
            amountPicker.value = 7

            val amountKindPicker = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount_kind)
            amountKindPicker.value = 0

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            assertThat(editTextAmount.text).hasToString(newTea.getString(R.string.new_tea_edit_text_amount_text_ts, 7))
        }
    }

    @Test
    fun changeTemperatureAndExpectUpdatedTemperature() {
        val newTeaActivityScenario = ActivityScenario.launch(NewTea::class.java)
        newTeaActivityScenario.onActivity { newTea: NewTea ->
            val editTextTemperature = newTea.findViewById<EditText>(R.id.edit_text_new_tea_temperature)
            editTextTemperature.performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val dialog = ShadowAlertDialog.getLatestAlertDialog()

            val numberPickerTemperature = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_temperature)
            numberPickerTemperature.value = 80

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            assertThat(editTextTemperature.text).hasToString(newTea.getString(R.string.new_tea_edit_text_temperature_text_celsius, 80))
        }
    }

    @Test
    fun showCoolDownTimeWhenTemperatureIsLessThan100Celsius() {
        val newTeaActivityScenario = ActivityScenario.launch(NewTea::class.java)
        newTeaActivityScenario.onActivity { newTea: NewTea ->
            val editTextTemperature = newTea.findViewById<EditText>(R.id.edit_text_new_tea_temperature)
            editTextTemperature.performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val dialog = ShadowAlertDialog.getLatestAlertDialog()

            val numberPickerTemperature = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_temperature)
            numberPickerTemperature.value = 80

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            val editTextCoolDownTime = newTea.findViewById<EditText>(R.id.edit_text_new_tea_cool_down_time)

            assertThat(editTextCoolDownTime.visibility).isEqualTo(View.VISIBLE)
        }
    }

    @Test
    fun hideCoolDownTimeWhenTemperatureIs100Celsius() {
        mockTemperatureUnitFahrenheit()
        val newTeaActivityScenario = ActivityScenario.launch(NewTea::class.java)
        newTeaActivityScenario.onActivity { newTea: NewTea ->
            val editTextTemperature = newTea.findViewById<EditText>(R.id.edit_text_new_tea_temperature)
            editTextTemperature.performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val dialog = ShadowAlertDialog.getLatestAlertDialog()

            val numberPickerTemperature = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_temperature)
            numberPickerTemperature.value = 212

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()

            val layoutCoolDownTime = newTea.findViewById<LinearLayout>(R.id.layout_new_tea_cool_down_time)

            assertThat(layoutCoolDownTime.visibility).isEqualTo(View.GONE)
        }
    }

    @Test
    fun changeCoolDownTimeAndExpectUpdatedCoolDownTime() {
        val newTeaActivityScenario = ActivityScenario.launch(NewTea::class.java)
        newTeaActivityScenario.onActivity { newTea: NewTea ->
            val editTextCoolDownTime = newTea.findViewById<EditText>(R.id.edit_text_new_tea_cool_down_time)
            editTextCoolDownTime.performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val dialog = ShadowAlertDialog.getLatestAlertDialog()

            val numberPickerTimeMinutes = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_minutes)
            numberPickerTimeMinutes.value = 5

            val numberPickerTimeSeconds = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_seconds)
            numberPickerTimeSeconds.value = 45

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            assertThat(editTextCoolDownTime.text).hasToString(newTea.getString(R.string.new_tea_edit_text_cool_down_time_text, "05:45"))
        }
    }

    @Test
    fun changeTimeAndExpectUpdatedTime() {
        val newTeaActivityScenario = ActivityScenario.launch(NewTea::class.java)
        newTeaActivityScenario.onActivity { newTea: NewTea ->
            val editTextTime = newTea.findViewById<EditText>(R.id.edit_text_new_tea_time)
            editTextTime.performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val dialog = ShadowAlertDialog.getLatestAlertDialog()

            val numberPickerTimeMinutes = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_minutes)
            numberPickerTimeMinutes.value = 5

            val numberPickerTimeSeconds = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_seconds)
            numberPickerTimeSeconds.value = 45

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            assertThat(editTextTime.text).hasToString(newTea.getString(R.string.new_tea_edit_text_time_text, "05:45"))
        }
    }

    @Test
    fun showActivityEditModeAndExpectFilledFields() {
        mockTemperatureUnitFahrenheit()
        val tea = Tea("Tea", Variety.GREEN_TEA.code, 1.0, AmountKind.TEA_SPOON.text, 234, 0, Date.from(fixedDate))
        tea.id = 1L
        every { teaDao.getTeaById(1) } returns tea

        val infusions: MutableList<Infusion> = ArrayList()
        infusions.add(Infusion(1, 0, "2:00", "5:00", 90, 194))
        every { infusionDao.getInfusionsByTeaId(1) } returns infusions

        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, NewTea::class.java)
        intent.putExtra(TEA_ID, 1L)

        val newTeaActivityScenario = ActivityScenario.launch<NewTea>(intent)
        newTeaActivityScenario.onActivity { newTea: NewTea ->
            val editTextVariety = newTea.findViewById<EditText>(R.id.edit_text_new_tea_variety)
            val editTextName = newTea.findViewById<EditText>(R.id.edit_text_new_tea_name)
            val editTextAmount = newTea.findViewById<EditText>(R.id.edit_text_new_tea_amount)
            val editTextTemperature = newTea.findViewById<EditText>(R.id.edit_text_new_tea_temperature)
            val editTextTime = newTea.findViewById<EditText>(R.id.edit_text_new_tea_time)
            val editTextCoolDownTime = newTea.findViewById<EditText>(R.id.edit_text_new_tea_cool_down_time)

            assertThat(editTextVariety.text).hasToString(convertStoredVarietyToText(tea.variety, newTea.application))
            assertThat(editTextName.text).hasToString(tea.name)
            assertThat(editTextAmount.text).hasToString(newTea.getString(R.string.new_tea_edit_text_amount_text_ts, tea.amount.toInt()))
            assertThat(editTextTemperature.text).hasToString(newTea.getString(R.string.new_tea_edit_text_temperature_text_fahrenheit, infusions[0].temperatureFahrenheit))
            assertThat(editTextTime.text).hasToString(newTea.getString(R.string.new_tea_edit_text_time_text, infusions[0].time))
            assertThat(editTextCoolDownTime.text).hasToString(newTea.getString(R.string.new_tea_edit_text_cool_down_time_text, infusions[0].coolDownTime))
        }
    }

    @Test
    fun editTeaAndExpectEditedTea() {
        mockTemperatureUnitFahrenheit()
        val tea = Tea("Tea", Variety.GREEN_TEA.code, 1.0, AmountKind.TEA_SPOON.text, 234, 0, Date.from(fixedDate))
        tea.id = 1L
        every { teaDao.getTeaById(1) } returns tea

        val infusions: MutableList<Infusion> = ArrayList()
        infusions.add(Infusion(1, 0, "2:00", "5:00", 100, 212))
        every { infusionDao.getInfusionsByTeaId(1) } returns infusions

        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, NewTea::class.java)
        intent.putExtra(TEA_ID, 1L)

        val newTeaActivityScenario = ActivityScenario.launch<NewTea>(intent)
        newTeaActivityScenario.onActivity { newTea: NewTea ->
            newTea.onOptionsItemSelected(RoboMenuItem(R.id.action_new_tea_done))

            val slotTea = slot<Tea>()
            verify { teaDao.update(capture(slotTea)) }
            assertThat(slotTea.captured.name).isEqualTo(tea.name)

            val slotInfusion = slot<Infusion>()
            verify { infusionDao.insert(capture(slotInfusion)) }
            assertThat(slotInfusion.captured.time).isEqualTo(infusions[0].time)
        }
    }

    @Test
    fun addInfusion() {
        val newTeaActivityScenario = ActivityScenario.launch(NewTea::class.java)
        newTeaActivityScenario.onActivity { newTea: NewTea ->
            val buttonLeft = newTea.findViewById<ImageButton>(R.id.button_new_tea_previous_infusion)
            val buttonRight = newTea.findViewById<ImageButton>(R.id.button_new_tea_next_infusion)
            val buttonDelete = newTea.findViewById<ImageButton>(R.id.button_new_tea_delete_infusion)
            val buttonAddInfusion = newTea.findViewById<ImageButton>(R.id.button_new_tea_add_infusion)
            val textViewInfusionBar = newTea.findViewById<TextView>(R.id.text_view_new_tea_count_infusion)

            assertThat(textViewInfusionBar.text).isEqualTo(FIRST_INFUSION)
            assertThat(buttonLeft.isEnabled).isFalse
            assertThat(buttonRight.isEnabled).isFalse
            assertThat(buttonDelete.visibility).isEqualTo(View.GONE)
            assertThat(buttonAddInfusion.isEnabled).isTrue

            buttonAddInfusion.performClick()
            assertThat(textViewInfusionBar.text).isEqualTo(SECOND_INFUSION)
            assertThat(buttonLeft.isEnabled).isTrue
            assertThat(buttonDelete.visibility).isEqualTo(View.VISIBLE)
        }
    }

    @Test
    fun performNextAndPreviousInfusion() {
        mockTemperatureUnitFahrenheit()
        val tea = Tea("Tea", Variety.GREEN_TEA.code, 1.0, AmountKind.TEA_SPOON.text, 234, 0, Date.from(fixedDate))
        tea.id = 1L
        every { teaDao.getTeaById(1) } returns tea

        val infusions: MutableList<Infusion> = ArrayList()
        val infusion1 = Infusion(1, 0, "2:00", "5:00", 100, 212)
        infusions.add(infusion1)
        val infusion2 = Infusion(1, 1, "4:00", "", 70, 158)
        infusions.add(infusion2)
        every { infusionDao.getInfusionsByTeaId(1) } returns infusions

        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, NewTea::class.java)
        intent.putExtra(TEA_ID, 1L)
        intent.putExtra(SHOW_TEA_FLAG, true)

        val newTeaActivityScenario = ActivityScenario.launch<NewTea>(intent)
        newTeaActivityScenario.onActivity { newTea: NewTea ->
            val buttonLeft = newTea.findViewById<ImageButton>(R.id.button_new_tea_previous_infusion)
            val buttonRight = newTea.findViewById<ImageButton>(R.id.button_new_tea_next_infusion)
            val buttonDeleteInfusion = newTea.findViewById<ImageButton>(R.id.button_new_tea_delete_infusion)
            val buttonAddInfusion = newTea.findViewById<ImageButton>(R.id.button_new_tea_add_infusion)
            val textViewInfusionBar = newTea.findViewById<TextView>(R.id.text_view_new_tea_count_infusion)

            assertThat(buttonLeft.isEnabled).isFalse
            assertThat(buttonRight.isEnabled).isTrue
            assertThat(buttonAddInfusion.visibility).isEqualTo(View.GONE)
            assertThat(buttonDeleteInfusion.visibility).isEqualTo(View.VISIBLE)
            assertThat(textViewInfusionBar.text).hasToString(FIRST_INFUSION)

            buttonRight.performClick()

            assertThat(buttonLeft.isEnabled).isTrue
            assertThat(buttonRight.isEnabled).isFalse
            assertThat(buttonAddInfusion.visibility).isEqualTo(View.VISIBLE)
            assertThat(textViewInfusionBar.text).hasToString(SECOND_INFUSION)

            buttonLeft.performClick()

            assertThat(buttonLeft.isEnabled).isFalse
            assertThat(buttonRight.isEnabled).isTrue
            assertThat(buttonAddInfusion.visibility).isEqualTo(View.GONE)
            assertThat(textViewInfusionBar.text).hasToString(FIRST_INFUSION)
        }
    }

    @Test
    fun performDeleteInfusion() {
        mockTemperatureUnitFahrenheit()
        val tea = Tea("Tea", Variety.GREEN_TEA.code, 1.0, AmountKind.TEA_SPOON.text, 234, 0, Date.from(fixedDate))
        tea.id = 1L
        every { teaDao.getTeaById(1) } returns tea

        val infusions: MutableList<Infusion> = ArrayList()
        val infusion1 = Infusion(1, 0, "2:00", "5:00", 100, 212)
        infusions.add(infusion1)
        val infusion2 = Infusion(1, 1, "4:00", "", 70, 158)
        infusions.add(infusion2)
        every { infusionDao.getInfusionsByTeaId(1) } returns infusions

        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, NewTea::class.java)
        intent.putExtra(TEA_ID, 1L)
        intent.putExtra(SHOW_TEA_FLAG, true)

        val newTeaActivityScenario = ActivityScenario.launch<NewTea>(intent)
        newTeaActivityScenario.onActivity { newTea: NewTea ->
            val buttonNextInfusion = newTea.findViewById<ImageButton>(R.id.button_new_tea_next_infusion)
            val buttonDeleteInfusion = newTea.findViewById<ImageButton>(R.id.button_new_tea_delete_infusion)
            val textViewInfusionBar = newTea.findViewById<TextView>(R.id.text_view_new_tea_count_infusion)

            buttonNextInfusion.performClick()

            assertThat(textViewInfusionBar.text).hasToString(SECOND_INFUSION)

            buttonDeleteInfusion.performClick()

            assertThat(textViewInfusionBar.text).hasToString(FIRST_INFUSION)
        }
    }

    @Test
    fun exitEditModeAndExpectShowTeaActivity() {
        val tea = Tea("Tea", Variety.BLACK_TEA.code, 1.0, AmountKind.GRAM.text, 1, 0, Date.from(fixedDate))
        tea.id = 1L
        every { teaDao.getTeaById(1) } returns tea

        val infusions: MutableList<Infusion> = ArrayList()
        val infusion = Infusion(1, 0, "", "", 100, 212)
        infusions.add(infusion)
        every { infusionDao.getInfusionsByTeaId(1) } returns infusions

        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, NewTea::class.java)
        intent.putExtra(TEA_ID, 1L)
        intent.putExtra(SHOW_TEA_FLAG, true)

        val newTeaActivityScenario = ActivityScenario.launch<NewTea>(intent)
        newTeaActivityScenario.onActivity { newTea: NewTea ->
            newTea.onOptionsItemSelected(RoboMenuItem(android.R.id.home))

            val expected = Intent(newTea, ShowTea::class.java)
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.component).isEqualTo(expected.component)
        }
    }

    @Test
    fun editTeaAndExpectShowTeaActivity() {
        mockTemperatureUnitFahrenheit()
        val tea = Tea("Tea", Variety.GREEN_TEA.code, 1.0, AmountKind.TEA_SPOON.text, 234, 0, Date.from(fixedDate))
        tea.id = 1L
        every { teaDao.getTeaById(1) } returns tea

        val infusions: MutableList<Infusion> = ArrayList()
        val infusion1 = Infusion(1, 0, "2:00", "5:00", 100, 212)
        infusions.add(infusion1)
        every { infusionDao.getInfusionsByTeaId(1) } returns infusions

        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, NewTea::class.java)
        intent.putExtra(TEA_ID, 1L)
        intent.putExtra(SHOW_TEA_FLAG, true)

        val newTeaActivityScenario = ActivityScenario.launch<NewTea>(intent)
        newTeaActivityScenario.onActivity { newTea: NewTea ->
            newTea.onOptionsItemSelected(RoboMenuItem(R.id.action_new_tea_done))

            val expected = Intent(newTea, ShowTea::class.java)
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.component).isEqualTo(expected.component)
        }
    }

    private fun mockTemperatureUnitFahrenheit() {
        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())
        sharedSettings.temperatureUnit = TemperatureUnit.FAHRENHEIT
    }

    private val fixedDate: Instant
        get() {
            val clock = Clock.fixed(Instant.parse(CURRENT_DATE), ZoneId.of("UTC"))
            return Instant.now(clock)
        }

    private fun getRadioButtons(dialog: AlertDialog): List<RadioButton> {
        val radioGroup = dialog.findViewById<RadioGroup>(R.id.radio_group_new_tea_variety_input)
        val listRadioButtons = ArrayList<RadioButton>()
        for (i in 0 until radioGroup.childCount) {
            val o = radioGroup.getChildAt(i)
            if (o is RadioButton) {
                listRadioButtons.add(o)
            }
        }
        return listRadioButtons
    }

    companion object {
        private const val CURRENT_DATE = "2020-08-19T10:15:30Z"
        private const val FIRST_INFUSION = "1. Infusion"
        private const val SECOND_INFUSION = "2. Infusion"
        private const val TEA_ID = "teaId"
        private const val SHOW_TEA_FLAG = "showTea"
    }
}