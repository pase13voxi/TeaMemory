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
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind
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
class AmountPickerDialogTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @RelaxedMockK
    lateinit var newTeaViewModel: NewTeaViewModel
    @MockK
    lateinit var suggestions: Suggestions
    @InjectMockKs
    lateinit var dialogFragment: AmountPickerDialog

    private var fragmentManager: FragmentManager? = null

    @Before
    fun setUp() {
        val activity = Robolectric.buildActivity(FragmentActivity::class.java).create().start().resume().get()
        fragmentManager = activity.supportFragmentManager
        // always default
        every { newTeaViewModel.amountKind } returns AmountKind.TEA_SPOON
    }

    @Test
    fun showDialogAndExpectTitle() {
        every { suggestions.amountTsSuggestions } returns intArrayOf()

        dialogFragment.show(fragmentManager!!, AmountPickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val shadowDialog = Shadows.shadowOf(dialog)
        assertThat(shadowDialog.title).isEqualTo(dialogFragment.getString(R.string.new_tea_dialog_amount_header))
    }

    @Test
    fun showDialogAndExpectTwoTsSuggestions() {
        every { suggestions.amountTsSuggestions } returns intArrayOf(4, 5)

        dialogFragment.show(fragmentManager!!, AmountPickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val buttonSuggestion1 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_1)
        assertThat(buttonSuggestion1)
            .extracting(Function<Button, Any> { obj: Button -> obj.visibility }, Function<Button, Any> { tv: Button -> tv.text.toString() })
            .containsExactly(View.VISIBLE, "4")

        val buttonSuggestion2 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_2)
        assertThat(buttonSuggestion2)
            .extracting(Function<Button, Any> { obj: Button -> obj.visibility }, Function<Button, Any> { tv: Button -> tv.text.toString() })
            .containsExactly(View.VISIBLE, "5")

        val buttonSuggestion3 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_3)
        assertThat(buttonSuggestion3.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun showDialogAndExpectNoSuggestions() {
        every { suggestions.amountTsSuggestions } returns intArrayOf()

        dialogFragment.show(fragmentManager!!, AmountPickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val layoutSuggestions = dialog.findViewById<LinearLayout>(R.id.layout_new_tea_custom_variety)
        assertThat(layoutSuggestions.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun setAmountKindGrAndExpectGrSuggestions() {
        every { suggestions.amountTsSuggestions } returns intArrayOf()
        every { suggestions.amountGrSuggestions } returns intArrayOf(10, 11, 12)

        dialogFragment.show(fragmentManager!!, AmountPickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val amountKindPicker = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount_kind)
        amountKindPicker.value = 1
        Shadows.shadowOf(amountKindPicker).onValueChangeListener.onValueChange(amountKindPicker, 0, 1)

        val layoutSuggestions = dialog.findViewById<LinearLayout>(R.id.layout_new_tea_custom_variety)
        assertThat(layoutSuggestions.visibility).isEqualTo(View.VISIBLE)

        val buttonSuggestion1 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_1)
        assertThat(buttonSuggestion1)
            .extracting(Function<Button, Any> { obj: Button -> obj.visibility }, Function<Button, Any> { btn: Button -> btn.text.toString() })
            .containsExactly(View.VISIBLE, "10")

        val buttonSuggestion2 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_2)
        assertThat(buttonSuggestion2)
            .extracting(Function<Button, Any> { obj: Button -> obj.visibility }, Function<Button, Any> { btn: Button -> btn.text.toString() })
            .containsExactly(View.VISIBLE, "11")

        val buttonSuggestion3 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_3)
        assertThat(buttonSuggestion3)
            .extracting(Function<Button, Any> { obj: Button -> obj.visibility }, Function<Button, Any> { btn: Button -> btn.text.toString() })
            .containsExactly(View.VISIBLE, "12")
    }

    @Test
    fun setAmountKindTbAndExpectTbSuggestions() {
         every { suggestions.amountTsSuggestions } returns intArrayOf()
         every { suggestions.amountTbSuggestions } returns intArrayOf(10, 11, 12)

        dialogFragment.show(fragmentManager!!, AmountPickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val amountKindPicker = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount_kind)
        amountKindPicker.value = 2
        Shadows.shadowOf(amountKindPicker).onValueChangeListener.onValueChange(amountKindPicker, 0, 1)

        val layoutSuggestions = dialog.findViewById<LinearLayout>(R.id.layout_new_tea_custom_variety)
        assertThat(layoutSuggestions.visibility).isEqualTo(View.VISIBLE)

        val buttonSuggestion1 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_1)
        assertThat(buttonSuggestion1)
            .extracting(Function<Button, Any> { obj: Button -> obj.visibility }, Function<Button, Any> { btn: Button -> btn.text.toString() })
            .containsExactly(View.VISIBLE, "10")

        val buttonSuggestion2 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_2)
        assertThat(buttonSuggestion2)
            .extracting(Function<Button, Any> { obj: Button -> obj.visibility }, Function<Button, Any> { btn: Button -> btn.text.toString() })
            .containsExactly(View.VISIBLE, "11")

        val buttonSuggestion3 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_3)
        assertThat(buttonSuggestion3)
            .extracting(Function<Button, Any> { obj: Button -> obj.visibility }, Function<Button, Any> { btn: Button -> btn.text.toString() })
            .containsExactly(View.VISIBLE, "12")
    }

    @Test
    fun clickSuggestionAndExpectShownSuggestion() {
         every { suggestions.amountTsSuggestions } returns intArrayOf(4, 5)

        dialogFragment.show(fragmentManager!!, AmountPickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val buttonSuggestion1 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_1)
        buttonSuggestion1.performClick()

        val amountPicker = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount)
        assertThat(amountPicker.value).isEqualTo(4)
    }

    @Test
    fun acceptTsInputExceptSavedInput() {
         every { suggestions.amountTsSuggestions } returns intArrayOf()
        dialogFragment.show(fragmentManager!!, AmountPickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val amountPicker = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount)
        amountPicker.value = 7

        val amountKindPicker = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount_kind)
        amountKindPicker.value = 0

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        verify { newTeaViewModel.setAmount(7.0, AmountKind.TEA_SPOON) }
    }

    @Test
    fun acceptGrInputExceptSavedInput() {
         every { suggestions.amountTsSuggestions } returns intArrayOf()
        dialogFragment.show(fragmentManager!!, AmountPickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val amountPicker = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount)
        amountPicker.value = 7

        val amountKindPicker = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount_kind)
        amountKindPicker.value = 1

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        verify { newTeaViewModel.setAmount(7.0, AmountKind.GRAM) }
    }

    @Test
    fun acceptTbInputExceptSavedInput() {
         every { suggestions.amountTsSuggestions } returns intArrayOf()
        dialogFragment.show(fragmentManager!!, AmountPickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val amountPicker = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount)
        amountPicker.value = 7

        val amountKindPicker = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount_kind)
        amountKindPicker.value = 2

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        verify { newTeaViewModel.setAmount(7.0, AmountKind.TEA_BAG) }
    }

    @Test
    fun showExistingAmountConfiguration() {
         every { suggestions.amountGrSuggestions } returns intArrayOf()
         every { newTeaViewModel.amount } returns 7.0
         every { newTeaViewModel.amountKind } returns AmountKind.GRAM

        dialogFragment.show(fragmentManager!!, AmountPickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val amountPicker = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount)
        assertThat(amountPicker.value).isEqualTo(7)

        val amountKindPicker = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount_kind)
        assertThat(amountKindPicker.value).isEqualTo(1)
    }

    @Test
    fun showExistingAmountConfigurationDecimal() {
         every { suggestions.amountTsSuggestions } returns intArrayOf()
         every { newTeaViewModel.amount } returns 7.5

        dialogFragment.show(fragmentManager!!, AmountPickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val amountPicker = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount)
        assertThat(amountPicker.value).isEqualTo(7)

        val amountPickerDecimal = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount_decimal)
        assertThat(amountPickerDecimal.value).isEqualTo(5)

        val amountKindPicker = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount_kind)
        assertThat(amountKindPicker.value).isZero
    }

    @Test
    fun clickSuggestionAndExpectOverwrittenExistingConfiguration() {
         every { suggestions.amountTsSuggestions } returns intArrayOf(4, 5)
         every { newTeaViewModel.amount } returns 7.5

        dialogFragment.show(fragmentManager!!, AmountPickerDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        val buttonSuggestion1 = dialog.findViewById<Button>(R.id.button_new_tea_picker_suggestion_1)
        buttonSuggestion1.performClick()

        val amountPicker = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount)
        assertThat(amountPicker.value).isEqualTo(4)

        val amountPickerDecimal = dialog.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount_decimal)
        assertThat(amountPickerDecimal.value).isZero
    }
}