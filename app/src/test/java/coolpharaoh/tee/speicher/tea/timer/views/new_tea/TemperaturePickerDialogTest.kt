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
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions.Suggestions
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
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
class TemperaturePickerDialogTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @RelaxedMockK
    lateinit var newTeaViewModel: NewTeaViewModel
    @MockK
    lateinit var suggestions: Suggestions
    @InjectMockKs
    lateinit var dialogFragment: TemperaturePickerDialog

    private var fragmentManager: FragmentManager? = null

    @Before
    fun setUp() {
        val activity = Robolectric.buildActivity(FragmentActivity::class.java).create().start().resume().get()
        fragmentManager = activity.supportFragmentManager
    }

    @Test
    fun showDialogAndExpectTitle() {
        every { suggestions.temperatureCelsiusSuggestions } returns intArrayOf()

        dialogFragment.show(fragmentManager!!, TemperaturePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val shadowDialog = Shadows.shadowOf(dialog)
        assertThat(shadowDialog.title).isEqualTo(dialogFragment.getString(R.string.new_tea_dialog_temperature_header))
    }

    @Test
    fun showDialogAndExpectCelsiusUnit() {
        every { suggestions.temperatureCelsiusSuggestions } returns intArrayOf()

        dialogFragment.show(fragmentManager!!, TemperaturePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val textViewUnit = dialog.findViewById<TextView>(R.id.text_view_new_tea_temperature_picker_unit)
        assertThat(textViewUnit.text).hasToString(dialogFragment.getString(R.string.new_tea_dialog_temperature_celsius))
    }

    @Test
    fun showDialogAndExpectFahrenheitUnit() {
        every { suggestions.temperatureFahrenheitSuggestions } returns intArrayOf()
        every { newTeaViewModel.getTemperatureUnit() } returns TemperatureUnit.FAHRENHEIT

        dialogFragment.show(fragmentManager!!, TemperaturePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val textViewUnit = dialog.findViewById<TextView>(R.id.text_view_new_tea_temperature_picker_unit)

        assertThat(textViewUnit.text).hasToString(dialogFragment.getString(R.string.new_tea_dialog_temperature_fahrenheit))
    }

    @Test
    fun showDialogAndExpectTwoCelsiusSuggestions() {
        every { suggestions.temperatureCelsiusSuggestions } returns intArrayOf(100, 90)

        dialogFragment.show(fragmentManager!!, TemperaturePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val buttonSuggestion1 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_1)
        assertThat(buttonSuggestion1)
            .extracting(Function<Button, Any> { obj: Button -> obj.visibility }, Function<Button, Any> { tv: Button -> tv.text.toString() })
            .containsExactly(View.VISIBLE, "100")

        val buttonSuggestion2 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_2)
        assertThat(buttonSuggestion2)
            .extracting(Function<Button, Any> { obj: Button -> obj.visibility }, Function<Button, Any> { tv: Button -> tv.text.toString() })
            .containsExactly(View.VISIBLE, "90")

        val buttonSuggestion3 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_3)
        assertThat(buttonSuggestion3.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun showDialogAndExpectTwoFahrenheitSuggestions() {
        every { suggestions.temperatureFahrenheitSuggestions } returns intArrayOf(212, 194)
        every { newTeaViewModel.getTemperatureUnit() } returns TemperatureUnit.FAHRENHEIT

        dialogFragment.show(fragmentManager!!, TemperaturePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val buttonSuggestion1 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_1)
        assertThat(buttonSuggestion1)
            .extracting(Function<Button, Any> { obj: Button -> obj.visibility }, Function<Button, Any> { tv: Button -> tv.text.toString() })
            .containsExactly(View.VISIBLE, "212")

        val buttonSuggestion2 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_2)
        assertThat(buttonSuggestion2)
            .extracting(Function<Button, Any> { obj: Button -> obj.visibility }, Function<Button, Any> { tv: Button -> tv.text.toString() })
            .containsExactly(View.VISIBLE, "194")

        val buttonSuggestion3 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_3)
        assertThat(buttonSuggestion3.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun showDialogAndExpectNoSuggestions() {
        every { suggestions.temperatureCelsiusSuggestions } returns intArrayOf()

        dialogFragment.show(fragmentManager!!, TemperaturePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val layoutSuggestions = dialog.findViewById<LinearLayout>(R.id.layout_new_tea_custom_variety)
        assertThat(layoutSuggestions.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun showDialogAndClickSuggestions() {
        every { suggestions.temperatureCelsiusSuggestions } returns intArrayOf(100, 90, 80)

        dialogFragment.show(fragmentManager!!, TemperaturePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val buttonSuggestion2 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_2)
        buttonSuggestion2.performClick()

        val numberPickerTemperature = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_temperature)
        assertThat(numberPickerTemperature.value).isEqualTo(90)
    }

    @Test
    fun acceptInputAndExpectSavedTemperature() {
        every { suggestions.temperatureCelsiusSuggestions } returns intArrayOf()

        dialogFragment.show(fragmentManager!!, TemperaturePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val numberPickerTemperature = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_temperature)
        numberPickerTemperature.value = 80

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        verify  { newTeaViewModel.setInfusionTemperature(80) }
    }

    @Test
    fun showExistingTemperatureConfiguration() {
        every { suggestions.temperatureCelsiusSuggestions } returns intArrayOf()
        every { newTeaViewModel.getInfusionTemperature() } returns 85

        dialogFragment.show(fragmentManager!!, TemperaturePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val numberPickerTemperature = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_temperature)
        assertThat(numberPickerTemperature.value).isEqualTo(85)
    }
}