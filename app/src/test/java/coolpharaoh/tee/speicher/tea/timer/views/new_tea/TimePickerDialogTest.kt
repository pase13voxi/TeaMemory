package coolpharaoh.tee.speicher.tea.timer.views.new_tea

import android.content.DialogInterface
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions.Suggestions
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowAlertDialog
import java.util.function.Function

@RunWith(RobolectricTestRunner::class)
class TimePickerDialogTest {
    @JvmField
    @Rule
    var rule = MockitoJUnit.rule()

    @Mock
    lateinit var newTeaViewModel: NewTeaViewModel
    @Mock
    lateinit var suggestions: Suggestions
    @InjectMocks
    lateinit var dialogFragment: TimePickerDialog

    private var fragmentManager: FragmentManager? = null

    @Before
    fun setUp() {
        val activity = Robolectric.buildActivity(FragmentActivity::class.java).create().start().resume().get()
        fragmentManager = activity.supportFragmentManager
    }

    @Test
    fun showDialogAndExpectTitle() {
        `when`(suggestions.timeSuggestions).thenReturn(arrayOf())

        dialogFragment.show(fragmentManager!!, TimePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val shadowDialog = Shadows.shadowOf(dialog)
        assertThat(shadowDialog.title).isEqualTo(dialogFragment.getString(R.string.new_tea_dialog_time_header))
    }

    @Test
    fun showDialogAndExpectTwoSuggestions() {
        `when`(suggestions.timeSuggestions).thenReturn(arrayOf("2:00", "3:00"))

        dialogFragment.show(fragmentManager!!, TimePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val buttonSuggestion1 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_1)
        assertThat(buttonSuggestion1)
            .extracting(Function<Button, Any> { obj: Button -> obj.visibility }, Function<Button, Any> { tv: Button -> tv.text.toString() })
            .containsExactly(View.VISIBLE, "2:00")

        val buttonSuggestion2 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_2)
        assertThat(buttonSuggestion2)
            .extracting(Function<Button, Any> { obj: Button -> obj.visibility }, Function<Button, Any> { tv: Button -> tv.text.toString() })
            .containsExactly(View.VISIBLE, "3:00")

        val buttonSuggestion3 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_3)
        assertThat(buttonSuggestion3.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun clickSuggestionAndExpectShownSuggestion() {
        `when`(suggestions.timeSuggestions).thenReturn(arrayOf("2:30", "3:00"))

        dialogFragment.show(fragmentManager!!, TimePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val buttonSuggestion1 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_1)
        buttonSuggestion1.performClick()

        val numberPickerMinutes = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_minutes)
        assertThat(numberPickerMinutes.value).isEqualTo(2)

        val numberPickerSeconds = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_seconds)
        assertThat(numberPickerSeconds.value).isEqualTo(30)
    }

    @Test
    fun showDialogAndExpectNoSuggestions() {
        `when`(suggestions.timeSuggestions).thenReturn(arrayOf())

        dialogFragment.show(fragmentManager!!, TimePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val layoutSuggestions = dialog.findViewById<LinearLayout>(R.id.layout_new_tea_custom_variety)
        assertThat(layoutSuggestions.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun acceptInputAndExpectSavedTime() {
        `when`(suggestions.timeSuggestions).thenReturn(arrayOf())

        dialogFragment.show(fragmentManager!!, TimePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val numberPickerTimeMinutes = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_minutes)
        numberPickerTimeMinutes.value = 5

        val numberPickerTimeSeconds = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_seconds)
        numberPickerTimeSeconds.value = 45

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        verify(newTeaViewModel).setInfusionTime("05:45")
    }

    @Test
    fun inputZeroTimeAndExpectSavedNull() {
        `when`(suggestions.timeSuggestions).thenReturn(arrayOf())

        dialogFragment.show(fragmentManager!!, TimePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val numberPickerTimeMinutes = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_minutes)
        numberPickerTimeMinutes.value = 0

        val numberPickerTimeSeconds = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_seconds)
        numberPickerTimeSeconds.value = 0

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        verify(newTeaViewModel).setInfusionTime(null)
    }

    @Test
    fun showExistingTimeConfiguration() {
        `when`(suggestions.timeSuggestions).thenReturn(arrayOf())
        `when`(newTeaViewModel.getInfusionTime()).thenReturn("05:15")

        dialogFragment.show(fragmentManager!!, TimePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val numberPickerTimeMinutes = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_minutes)
        assertThat(numberPickerTimeMinutes.value).isEqualTo(5)

        val numberPickerTimeSeconds = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_seconds)
        assertThat(numberPickerTimeSeconds.value).isEqualTo(15)
    }
}