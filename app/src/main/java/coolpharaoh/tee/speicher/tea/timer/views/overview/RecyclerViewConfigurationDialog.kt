package coolpharaoh.tee.speicher.tea.timer.views.overview

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode
import coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode.Companion.fromChoice

class RecyclerViewConfigurationDialog(private val overviewViewModel: OverviewViewModel) : DialogFragment() {

    private lateinit var dialogView: View

    override fun onCreateDialog(savedInstancesState: Bundle?): Dialog {
        val activity: Activity = requireActivity()
        val parent = activity.findViewById<ViewGroup>(R.id.overview_parent)
        val inflater = activity.layoutInflater
        dialogView = inflater.inflate(R.layout.dialog_overview_list_configuration, parent, false)

        defineVarietyRadioGroup()
        setShowInStock()

        return AlertDialog.Builder(activity, R.style.dialog_theme)
            .setView(dialogView)
            .setNegativeButton(R.string.overview_dialog_sort_negative, null)
            .setPositiveButton(R.string.overview_dialog_sort_positive) { dialog: DialogInterface?, which: Int ->
                persistSortMode()
                persistShowInStock()
            }
            .create()
    }

    private fun defineVarietyRadioGroup() {
        val sortOptions = resources.getStringArray(R.array.overview_sort_options)
        val sortModeRadioGroup = dialogView.findViewById<RadioGroup>(R.id.radio_group_overview_sort_mode)

        for (variety in sortOptions) {
            val varietyRadioButton = createRadioButton(variety)
            sortModeRadioGroup.addView(varietyRadioButton)
        }

        setConfiguredValues(sortModeRadioGroup)
    }

    private fun createRadioButton(variety: String): RadioButton {
        val varietyRadioButton = RadioButton(activity)
        varietyRadioButton.text = variety

        val colorStateList = ColorStateList(
            arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)), intArrayOf(
                ContextCompat.getColor(requireActivity().application, R.color.background_grey),
                ContextCompat.getColor(requireActivity().application, R.color.background_green)
            )
        )
        varietyRadioButton.buttonTintList = colorStateList
        val params = RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(dpToPixel(20), dpToPixel(10), 0, 0)
        varietyRadioButton.layoutParams = params
        varietyRadioButton.setPadding(dpToPixel(15), 0, 0, 0)
        varietyRadioButton.textSize = 16f
        return varietyRadioButton
    }

    private fun dpToPixel(dpValue: Int): Int {
        val density = requireActivity().application.resources.displayMetrics.density
        return (dpValue * density).toInt() // margin in pixels
    }

    private fun setConfiguredValues(varietyRadioGroup: RadioGroup) {
        val sortMode = overviewViewModel.sort
        val radioButtons = getRadioButtons(varietyRadioGroup)

        radioButtons[sortMode.choice].isChecked = true
    }

    private fun getRadioButtons(radioGroup: RadioGroup): List<RadioButton> {
        val listRadioButtons = ArrayList<RadioButton>()
        for (i in 0 until radioGroup.childCount) {
            val o = radioGroup.getChildAt(i)
            if (o is RadioButton) {
                listRadioButtons.add(o)
            }
        }
        return listRadioButtons
    }

    private fun setShowInStock() {
        val checkBoxInStock = dialogView.findViewById<CheckBox>(R.id.checkbox_overview_in_stock)
        checkBoxInStock.isChecked = overviewViewModel.isOverviewInStock
    }

    private fun persistSortMode() {
        val sortMode = extractSortMode()

        overviewViewModel.sort = sortMode
    }

    private fun extractSortMode(): SortMode {
        val sortModeRadioGroup = dialogView.findViewById<RadioGroup>(R.id.radio_group_overview_sort_mode)
        val radioButtons = getRadioButtons(sortModeRadioGroup)

        var sortMode = SortMode.LAST_USED
        for (i in radioButtons.indices) {
            if (radioButtons[i].isChecked) {
                sortMode = fromChoice(i)
            }
        }
        return sortMode
    }

    private fun persistShowInStock() {
        val checkBoxInStock = dialogView.findViewById<CheckBox>(R.id.checkbox_overview_in_stock)

        overviewViewModel.isOverviewInStock = checkBoxInStock.isChecked
    }

    companion object {
        const val TAG = "RecyclerViewConfigurationDialog"
    }
}