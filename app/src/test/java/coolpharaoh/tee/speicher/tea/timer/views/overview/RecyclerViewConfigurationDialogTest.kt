package coolpharaoh.tee.speicher.tea.timer.views.overview

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Looper
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode
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

@RunWith(RobolectricTestRunner::class)
class RecyclerViewConfigurationDialogTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @RelaxedMockK
    lateinit var overviewViewModel: OverviewViewModel
    @InjectMockKs
    lateinit var dialogFragment: RecyclerViewConfigurationDialog

    private var fragmentManager: FragmentManager? = null

    @Before
    fun setUp() {
        val activity = Robolectric.buildActivity(FragmentActivity::class.java).create().start().resume().get()
        fragmentManager = activity.supportFragmentManager
        dialogFragment = RecyclerViewConfigurationDialog(overviewViewModel)
    }

    @Test
    fun showDialogAndExpectTitle() {
        every { overviewViewModel.sort } returns SortMode.LAST_USED

        dialogFragment.show(fragmentManager!!, RecyclerViewConfigurationDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val textViewSortModeTitle = dialog.findViewById<TextView>(R.id.text_view_overview_sort_mode_configuration_title)
        val textViewFilterTitle = dialog.findViewById<TextView>(R.id.text_view_overview_filter_configuration_title)

        assertThat(textViewSortModeTitle.text).isEqualTo(dialogFragment.getString(R.string.overview_dialog_sort_title))
        assertThat(textViewFilterTitle.text).isEqualTo(dialogFragment.getString(R.string.overview_dialog_filter_title))
    }

    @Test
    fun showDialogAndExpectSortModeByVariety() {
        every { overviewViewModel.sort } returns SortMode.BY_VARIETY

        dialogFragment.show(fragmentManager!!, RecyclerViewConfigurationDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val radioButtons = getRadioButtons(dialog)

        assertThat(radioButtons[SortMode.BY_VARIETY.choice].isChecked).isTrue
    }

    @Test
    fun selectAlphabeticallyAndExpectSavedSortMode() {
        every { overviewViewModel.sort} returns SortMode.LAST_USED

        dialogFragment.show(fragmentManager!!, RecyclerViewConfigurationDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val radioButtons = getRadioButtons(dialog)

        radioButtons[SortMode.ALPHABETICAL.choice].performClick()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        verify { overviewViewModel.sort = SortMode.ALPHABETICAL }
    }

    @Test
    fun showDialogAndExpectFilterInStock() {
        every { overviewViewModel.sort } returns SortMode.LAST_USED
        every { overviewViewModel.isOverviewInStock } returns true

        dialogFragment.show(fragmentManager!!, RecyclerViewConfigurationDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val checkBoxInStock = dialog.findViewById<CheckBox>(R.id.checkbox_overview_in_stock)

        assertThat(checkBoxInStock.isChecked).isTrue
    }

    @Test
    fun selectFilterInStockAndExpectSavedFilter() {
        every { overviewViewModel.sort } returns SortMode.LAST_USED

        dialogFragment.show(fragmentManager!!, RecyclerViewConfigurationDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val checkBoxInStock = dialog.findViewById<CheckBox>(R.id.checkbox_overview_in_stock)

        checkBoxInStock.isChecked = true

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        verify { overviewViewModel.isOverviewInStock = true }
    }

    private fun getRadioButtons(dialog: AlertDialog): List<RadioButton> {
        val radioGroup = dialog.findViewById<RadioGroup>(R.id.radio_group_overview_sort_mode)
        val listRadioButtons = ArrayList<RadioButton>()
        for (i in 0 until radioGroup.childCount) {
            val o = radioGroup.getChildAt(i)
            if (o is RadioButton) {
                listRadioButtons.add(o)
            }
        }
        return listRadioButtons
    }
}