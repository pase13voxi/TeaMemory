package coolpharaoh.tee.speicher.tea.timer.views.new_tea

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind.Companion.fromChoice
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions.Suggestions

class AmountPickerDialog(private val suggestions: Suggestions, private val newTeaViewModel: NewTeaViewModel) : DialogFragment() {

    private var dialogView: View? = null

    override fun onCreateDialog(savedInstancesState: Bundle?): Dialog {
        val activity: Activity = requireActivity()
        val parent = activity.findViewById<ViewGroup>(R.id.new_tea_parent)
        val inflater = activity.layoutInflater
        dialogView = inflater.inflate(R.layout.dialog_amount_picker, parent, false)

        setAmountPicker()
        setSuggestions()

        return AlertDialog.Builder(activity, R.style.dialog_theme)
            .setView(dialogView)
            .setTitle(R.string.new_tea_dialog_amount_header)
            .setNegativeButton(R.string.new_tea_dialog_picker_negative, null)
            .setPositiveButton(R.string.new_tea_dialog_picker_positive) { dialog, which -> persistAmount() }
            .create()
    }

    private fun setAmountPicker() {
        val amountPicker = dialogView!!.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount)
        amountPicker.minValue = 0
        amountPicker.maxValue = 100

        val amountPickerDecimal = dialogView!!.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount_decimal)
        amountPickerDecimal.minValue = 0
        amountPickerDecimal.maxValue = 9

        val amountPickerKind = dialogView!!.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount_kind)
        amountPickerKind.minValue = 0
        amountPickerKind.maxValue = 2
        amountPickerKind.displayedValues = resources.getStringArray(R.array.new_tea_dialog_amount_kind)
        amountPickerKind.setOnValueChangedListener { numberPicker, oldValue, newValue -> setSuggestions() }

        setConfiguredValues(amountPicker, amountPickerDecimal, amountPickerKind)
    }

    private fun setConfiguredValues(amountPicker: NumberPicker, amountPickerDecimal: NumberPicker,
        amountKindPicker: NumberPicker) {

        val amount = newTeaViewModel.amount
        val amountPreDecimal = amount.toInt()
        val amountDecimal = ((amount - amountPreDecimal) * 10).toInt()

        if (amount != -500.0) {
            amountPicker.value = amountPreDecimal
            amountPickerDecimal.value = amountDecimal
        }

        val amountKind = newTeaViewModel.amountKind
        amountKindPicker.value = amountKind.choice
    }

    private fun setSuggestions() {
        val buttons: MutableList<Button> = ArrayList()
        buttons.add(dialogView!!.findViewById(R.id.button_new_tea_picker_suggestion_1))
        buttons.add(dialogView!!.findViewById(R.id.button_new_tea_picker_suggestion_2))
        buttons.add(dialogView!!.findViewById(R.id.button_new_tea_picker_suggestion_3))

        if (getSuggestions().isNotEmpty()) {
            enableSuggestions()
            fillSuggestions(buttons)
            setClickListener(buttons)
        } else {
            disableSuggestions()
        }
    }

    private fun getSuggestions(): IntArray {
        val amountPickerKind = dialogView!!.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount_kind)
        return if (AmountKind.GRAM.choice == amountPickerKind.value) {
            suggestions.amountGrSuggestions
        } else if (AmountKind.TEA_BAG.choice == amountPickerKind.value) {
            suggestions.amountTbSuggestions
        } else {
            suggestions.amountTsSuggestions
        }
    }

    private fun fillSuggestions(buttons: List<Button>) {
        val amountSuggestions = getSuggestions()

        for (i in 0..2) {
            if (i < amountSuggestions.size) {
                buttons[i].visibility = View.VISIBLE
                buttons[i].text = amountSuggestions[i].toString()
            } else {
                buttons[i].visibility = View.GONE
            }
        }
    }

    private fun setClickListener(buttons: List<Button>) {
        val amountPicker = dialogView!!.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount)
        val amountPickerDecimal = dialogView!!.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount_decimal)
        val amountSuggestions = getSuggestions()

        for (i in amountSuggestions.indices) {
            val amount = amountSuggestions[i]
            buttons[i].setOnClickListener {
                amountPicker.value = amount
                amountPickerDecimal.value = 0
            }
        }
    }

    private fun enableSuggestions() {
        val layoutSuggestions = dialogView!!.findViewById<LinearLayout>(R.id.layout_new_tea_custom_variety)
        layoutSuggestions.visibility = View.VISIBLE
    }

    private fun disableSuggestions() {
        val layoutSuggestions = dialogView!!.findViewById<LinearLayout>(R.id.layout_new_tea_custom_variety)
        layoutSuggestions.visibility = View.GONE
    }

    private fun persistAmount() {
        val amountPicker = dialogView!!.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount)
        val amountPreDecimal = amountPicker.value.toDouble()

        val amountPickerDecimal = dialogView!!.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount_decimal)
        val amountDecimal = amountPickerDecimal.value.toDouble()

        val amount = amountPreDecimal + amountDecimal / 10

        val amountKindPicker = dialogView!!.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_amount_kind)
        val amountKindChoice = amountKindPicker.value

        newTeaViewModel.setAmount(amount, fromChoice(amountKindChoice))
    }

    companion object {
        const val TAG = "AmountPickerDialog"
    }
}