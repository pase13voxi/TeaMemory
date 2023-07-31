package coolpharaoh.tee.speicher.tea.timer.views.new_tea

import android.content.DialogInterface
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowAlertDialog
import java.util.function.Function

@RunWith(RobolectricTestRunner::class)
class CoolDownTimePickerDialogTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @RelaxedMockK
    lateinit var newTeaViewModel: NewTeaViewModel
    @InjectMockKs
    lateinit var dialogFragment: CoolDownTimePickerDialog

    private var fragmentManager: FragmentManager? = null

    @Before
    fun setUp() {
        val activity = Robolectric.buildActivity(FragmentActivity::class.java).create().start().resume().get()
        fragmentManager = activity.supportFragmentManager
    }

    @Test
    fun showDialog() {
        every { newTeaViewModel.getInfusionCoolDownTime() } returns null

        dialogFragment.show(fragmentManager!!, CoolDownTimePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val shadowDialog = Shadows.shadowOf(dialog)
        assertThat(shadowDialog.title).isEqualTo(dialogFragment.getString(R.string.new_tea_dialog_cool_down_time_header))
    }

    @Test
    fun showDialogWithNoConfiguredTemperatureAndExpectNoCalculatedCoolDownTime() {
        every { newTeaViewModel.getInfusionCoolDownTime() } returns null
        every { newTeaViewModel.getInfusionTemperature() } returns -500
        every { newTeaViewModel.getTemperatureUnit() } returns TemperatureUnit.CELSIUS

        dialogFragment.show(fragmentManager!!, CoolDownTimePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val shadowDialog = Shadows.shadowOf(dialog)
        assertThat(shadowDialog.title).isEqualTo(dialogFragment.getString(R.string.new_tea_dialog_cool_down_time_header))

        val linearLayout = dialog.findViewById<LinearLayout>(R.id.layout_new_tea_custom_variety)
        assertThat(linearLayout.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun showDialogWith100CelsiusAndExpectNoCalculatedCoolDownTime() {
        every { newTeaViewModel.getInfusionCoolDownTime() } returns null
        every { newTeaViewModel.getInfusionTemperature() } returns 212
        every { newTeaViewModel.getTemperatureUnit() } returns TemperatureUnit.FAHRENHEIT

        dialogFragment.show(fragmentManager!!, CoolDownTimePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val shadowDialog = Shadows.shadowOf(dialog)
        assertThat(shadowDialog.title).isEqualTo(dialogFragment.getString(R.string.new_tea_dialog_cool_down_time_header))

        val linearLayout = dialog.findViewById<LinearLayout>(R.id.layout_new_tea_custom_variety)
        assertThat(linearLayout.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun showDialogAndExpectCalculatedCoolDownTime() {
        every { newTeaViewModel.getInfusionCoolDownTime() } returns null
        every { newTeaViewModel.getInfusionTemperature() } returns 90
        every { newTeaViewModel.getTemperatureUnit() } returns TemperatureUnit.CELSIUS

        dialogFragment.show(fragmentManager!!, CoolDownTimePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val textViewSuggestions = dialog.findViewById<TextView>(R.id.text_view_new_tea_suggestions_description)
        assertThat(textViewSuggestions.text).hasToString(dialogFragment.getString(R.string.new_tea_dialog_cool_down_time_calculated_suggestion))

        val buttonSuggestion1 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_1)
        assertThat(buttonSuggestion1)
            .extracting(Function<Button, Any> { obj: Button -> obj.visibility }, Function<Button, Any> { tv: Button -> tv.text.toString() })
            .containsExactly(View.VISIBLE, "5:00")

        val buttonSuggestion2 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_2)
        assertThat(buttonSuggestion2.visibility).isEqualTo(View.GONE)

        val buttonSuggestion3 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_3)
        assertThat(buttonSuggestion3.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun clickCalculatedCoolDownTimeAndExpectShownCalculatedCoolDownTime() {
        every { newTeaViewModel.getInfusionCoolDownTime() } returns null
        every { newTeaViewModel.getInfusionTemperature() } returns 95
        every { newTeaViewModel.getTemperatureUnit() } returns TemperatureUnit.CELSIUS

        dialogFragment.show(fragmentManager!!, CoolDownTimePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val buttonSuggestion = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_1)
        buttonSuggestion.performClick()

        val numberPickerTimeMinutes = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_minutes)
        assertThat(numberPickerTimeMinutes.value).isEqualTo(2)

        val numberPickerTimeSeconds = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_seconds)
        assertThat(numberPickerTimeSeconds.value).isEqualTo(30)
    }

    @Test
    fun acceptInputAndExpectSavedCoolDownTime() {
        every { newTeaViewModel.getInfusionCoolDownTime() } returns null

        dialogFragment.show(fragmentManager!!, CoolDownTimePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val numberPickerTimeMinutes = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_minutes)
        numberPickerTimeMinutes.value = 5

        val numberPickerTimeSeconds = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_seconds)
        numberPickerTimeSeconds.value = 45

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        verify { newTeaViewModel.setInfusionCoolDownTime("05:45") }
    }

    @Test
    fun inputZeroTimeAndExpectSavedNull() {
        every { newTeaViewModel.getInfusionCoolDownTime() } returns null

        dialogFragment.show(fragmentManager!!, CoolDownTimePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val numberPickerTimeMinutes = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_minutes)
        numberPickerTimeMinutes.value = 0

        val numberPickerTimeSeconds = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_seconds)
        numberPickerTimeSeconds.value = 0

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        verify { newTeaViewModel.setInfusionCoolDownTime(null) }
    }

    @Test
    fun showExistingCoolDownTimeConfiguration() {
         every { newTeaViewModel.getInfusionCoolDownTime() } returns "05:15"

        dialogFragment.show(fragmentManager!!, CoolDownTimePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val numberPickerTimeMinutes = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_minutes)
        assertThat(numberPickerTimeMinutes.value).isEqualTo(5)

        val numberPickerTimeSeconds = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_seconds)
        assertThat(numberPickerTimeSeconds.value).isEqualTo(15)
    }
}