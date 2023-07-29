package coolpharaoh.tee.speicher.tea.timer.views.new_tea

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety
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

@RunWith(RobolectricTestRunner::class)
class VarietyPickerDialogTest {
    @JvmField
    @Rule
    var rule = MockitoJUnit.rule()

    @Mock
    lateinit var newTeaViewModel: NewTeaViewModel

    @InjectMocks
    lateinit var dialogFragment: VarietyPickerDialog

    private var fragmentManager: FragmentManager? = null

    @Before
    fun setUp() {
        val activity = Robolectric.buildActivity(FragmentActivity::class.java).create().start().resume().get()
        fragmentManager = activity.supportFragmentManager
    }

    @Test
    fun showDialogAndExpectTitle() {
        dialogFragment.show(fragmentManager!!, VarietyPickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val shadowDialog = Shadows.shadowOf(dialog)
        assertThat(shadowDialog.title).isEqualTo(dialogFragment.getString(R.string.new_tea_dialog_variety_header))
    }

    @Test
    fun selectYellowTeaAndExpectSavedYellowTea() {
        dialogFragment.show(fragmentManager!!, VarietyPickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val radioButtons = getRadioButtons(dialog)

        radioButtons[Variety.YELLOW_TEA.choice].performClick()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        verify(newTeaViewModel).setVariety(YELLOW_TEA_TEXT)
    }

    @Test
    fun selectYellowTeaAndExpectSavedColorForYellowTea() {
        dialogFragment.show(fragmentManager!!, VarietyPickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val radioButtons = getRadioButtons(dialog)

        radioButtons[Variety.YELLOW_TEA.choice].performClick()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        verify(newTeaViewModel).color = -15797
    }

    @Test
    fun showAndHideCustomVarityInputField() {
        `when`(newTeaViewModel.varietyAsText).thenReturn(YELLOW_TEA_TEXT)
        `when`(newTeaViewModel.variety).thenReturn(Variety.YELLOW_TEA)

        dialogFragment.show(fragmentManager!!, VarietyPickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val editTextCustomVariety = dialog.findViewById<EditText>(R.id.edit_text_new_tea_custom_variety)
        assertThat(editTextCustomVariety.visibility).isEqualTo(View.GONE)

        val radioButtons = getRadioButtons(dialog)
        radioButtons[Variety.OTHER.choice].performClick()
        assertThat(editTextCustomVariety.visibility).isEqualTo(View.VISIBLE)

        radioButtons[Variety.YELLOW_TEA.choice].performClick()
        assertThat(editTextCustomVariety.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun inputCustomVarietyAndExpectSavedCustomVariety() {
        dialogFragment.show(fragmentManager!!, VarietyPickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val radioButtons = getRadioButtons(dialog)

        radioButtons[Variety.OTHER.choice].performClick()

        val editTextCustomVariety = dialog.findViewById<EditText>(R.id.edit_text_new_tea_custom_variety)
        editTextCustomVariety.setText(CUSTOM_VARIETY)

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        verify(newTeaViewModel).setVariety(CUSTOM_VARIETY)
    }

    @Test
    fun showExistingVarietyConfiguration() {
        `when`(newTeaViewModel.varietyAsText).thenReturn(YELLOW_TEA_TEXT)
        `when`(newTeaViewModel.variety).thenReturn(Variety.YELLOW_TEA)

        dialogFragment.show(fragmentManager!!, TemperaturePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val radioButtons = getRadioButtons(dialog)
        assertThat(radioButtons[Variety.YELLOW_TEA.choice].isChecked).isTrue
    }

    @Test
    fun showExistingCustomVarietyConfiguration() {
        `when`(newTeaViewModel.variety).thenReturn(Variety.OTHER)
        `when`(newTeaViewModel.varietyAsText).thenReturn(CUSTOM_VARIETY)

        dialogFragment.show(fragmentManager!!, TemperaturePickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val radioButtons = getRadioButtons(dialog)
        assertThat(radioButtons[Variety.OTHER.choice].isChecked).isTrue

        val editTextCustomVariety = dialog.findViewById<EditText>(R.id.edit_text_new_tea_custom_variety)
        assertThat(editTextCustomVariety.text).hasToString(CUSTOM_VARIETY)
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
        private const val YELLOW_TEA_TEXT = "Yellow tea"
        private const val CUSTOM_VARIETY = "Custom Variety"
    }
}