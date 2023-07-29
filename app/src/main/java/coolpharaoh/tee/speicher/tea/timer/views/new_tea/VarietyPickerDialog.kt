package coolpharaoh.tee.speicher.tea.timer.views.new_tea

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.fromChoice

class VarietyPickerDialog(private val newTeaViewModel: NewTeaViewModel) : DialogFragment() {

    private var dialogView: View? = null

    override fun onCreateDialog(savedInstancesState: Bundle?): Dialog {
        val activity: Activity = requireActivity()
        val parent = activity.findViewById<ViewGroup>(R.id.new_tea_parent)
        val inflater = activity.layoutInflater
        dialogView = inflater.inflate(R.layout.dialog_variety_picker, parent, false)

        defineVarietyRadioGroup()

        return AlertDialog.Builder(activity, R.style.dialog_theme)
            .setView(dialogView)
            .setTitle(R.string.new_tea_dialog_variety_header)
            .setNegativeButton(R.string.new_tea_dialog_picker_negative, null)
            .setPositiveButton(R.string.new_tea_dialog_picker_positive) { _, _ ->
                persistVariety()
                persistColor()
            }
            .create()
    }

    private fun defineVarietyRadioGroup() {
        val varietyList = resources.getStringArray(R.array.new_tea_variety_teas)
        val varietyRadioGroup = dialogView!!.findViewById<RadioGroup>(R.id.radio_group_new_tea_variety_input)

        for (variety in varietyList) {
            val varietyRadioButton = createRadioButton(variety)
            varietyRadioGroup.addView(varietyRadioButton)
        }

        varietyRadioGroup.setOnCheckedChangeListener { radioGroup: RadioGroup, checkedId: Int -> showCustomVariety(radioGroup, checkedId) }

        setConfiguredValues(varietyRadioGroup, varietyList)
    }

    private fun setConfiguredValues(varietyRadioGroup: RadioGroup, varietyList: Array<String>) {
        val varietyAsText = newTeaViewModel.varietyAsText
        val varietyIndex = listOf(*varietyList).indexOf(varietyAsText)
        val radioButtons = getRadioButtons(varietyRadioGroup)

        if (varietyIndex == -1) {
            radioButtons[Variety.OTHER.choice].isChecked = true
            val editTextCustomVariety = dialogView!!.findViewById<EditText>(R.id.edit_text_new_tea_custom_variety)
            editTextCustomVariety.setText(varietyAsText)
        } else {
            val variety = newTeaViewModel.variety
            radioButtons[variety.choice].isChecked = true
        }
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

    private fun createRadioButton(variety: String): RadioButton {
        val varietyRadioButton = RadioButton(activity)
        varietyRadioButton.text = variety

        val colorStateList = ColorStateList(
            arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
            intArrayOf(
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

    private fun showCustomVariety(radioGroup: RadioGroup, checkedId: Int) {
        val varietyList = resources.getStringArray(R.array.new_tea_variety_teas)
        val radioButton = radioGroup.findViewById<RadioButton>(checkedId)
        val editTextCustomVariety = dialogView!!.findViewById<EditText>(R.id.edit_text_new_tea_custom_variety)

        if (varietyList[Variety.OTHER.choice] == radioButton.text.toString()) {
            editTextCustomVariety.visibility = View.VISIBLE
        } else {
            editTextCustomVariety.visibility = View.GONE
        }
    }

    private fun persistVariety() {
        val editTextCustomVariety = dialogView!!.findViewById<EditText>(R.id.edit_text_new_tea_custom_variety)

        if (editTextCustomVariety.visibility == View.VISIBLE && editTextCustomVariety.text.toString().isNotEmpty()) {
            newTeaViewModel.setVariety(editTextCustomVariety.text.toString())
        } else {
            val varietyRadioGroup = dialogView!!.findViewById<RadioGroup>(R.id.radio_group_new_tea_variety_input)
            val radioButton = varietyRadioGroup.findViewById<RadioButton>(varietyRadioGroup.checkedRadioButtonId)

            newTeaViewModel.setVariety(radioButton.text.toString())
        }
    }

    private fun persistColor() {
        val varietyList = resources.getStringArray(R.array.new_tea_variety_teas)
        val varietyRadioGroup = dialogView!!.findViewById<RadioGroup>(R.id.radio_group_new_tea_variety_input)
        val radioButton = varietyRadioGroup.findViewById<RadioButton>(varietyRadioGroup.checkedRadioButtonId)

        val varietyIndex = listOf(*varietyList).indexOf(radioButton.text.toString())
        val varietyColor = ContextCompat.getColor(requireActivity().application, fromChoice(varietyIndex).color)

        newTeaViewModel.color = varietyColor
    }

    companion object {
        const val TAG = "VarietyPickerDialog"
    }
}