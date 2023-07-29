package coolpharaoh.tee.speicher.tea.timer.views.new_tea

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions.Suggestions

class TemperaturePickerDialog(private val suggestions: Suggestions, private val newTeaViewModel: NewTeaViewModel) : DialogFragment() {

    private var dialogView: View? = null

    override fun onCreateDialog(savedInstancesState: Bundle?): Dialog {
        val activity: Activity = requireActivity()
        val parent = activity.findViewById<ViewGroup>(R.id.new_tea_parent)
        val inflater = activity.layoutInflater
        dialogView = inflater.inflate(R.layout.dialog_temperature_picker, parent, false)

        setTemperaturePicker()
        setTemperatureUnit()
        setSuggestions()

        return AlertDialog.Builder(activity, R.style.dialog_theme)
            .setView(dialogView)
            .setTitle(R.string.new_tea_dialog_temperature_header)
            .setNegativeButton(R.string.new_tea_dialog_picker_negative, null)
            .setPositiveButton(R.string.new_tea_dialog_picker_positive) { dialog: DialogInterface?, which: Int -> persistTemperature() }
            .create()
    }

    private fun setTemperaturePicker() {
        val temperaturePicker = dialogView!!.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_temperature)
        if (isFahrenheit) {
            temperaturePicker.minValue = 122
            temperaturePicker.maxValue = 212
        } else {
            temperaturePicker.minValue = 50
            temperaturePicker.maxValue = 100
        }

        setConfiguredValues(temperaturePicker)
    }

    private fun setConfiguredValues(temperaturePicker: NumberPicker) {
        val temperature = newTeaViewModel.getInfusionTemperature()

        if (temperature != -500) {
            temperaturePicker.value = temperature
        }
    }

    private val isFahrenheit: Boolean
        get() = TemperatureUnit.FAHRENHEIT == newTeaViewModel.getTemperatureUnit()

    private fun setTemperatureUnit() {
        if (isFahrenheit) {
            val textViewTemperatureUnit = dialogView!!.findViewById<TextView>(R.id.text_view_new_tea_temperature_picker_unit)
            textViewTemperatureUnit.setText(R.string.new_tea_dialog_temperature_fahrenheit)
        }
    }

    private fun setSuggestions() {
        val buttons: MutableList<Button> = ArrayList()
        buttons.add(dialogView!!.findViewById(R.id.button_new_tea_picker_suggestion_1))
        buttons.add(dialogView!!.findViewById(R.id.button_new_tea_picker_suggestion_2))
        buttons.add(dialogView!!.findViewById(R.id.button_new_tea_picker_suggestion_3))

        if (getSuggestions().isNotEmpty()) {
            fillSuggestions(buttons)
            setClickListener(buttons)
        } else {
            disableSuggestions()
        }
    }

    private fun getSuggestions(): IntArray {
        return if (isFahrenheit) {
            suggestions.temperatureFahrenheitSuggestions
        } else {
            suggestions.temperatureCelsiusSuggestions
        }
    }

    private fun fillSuggestions(buttons: List<Button>) {
        val temperatureSuggestions = getSuggestions()

        for (i in 0..2) {
            if (i < temperatureSuggestions.size) {
                buttons[i].text = temperatureSuggestions[i].toString()
            } else {
                buttons[i].visibility = View.GONE
            }
        }
    }

    private fun setClickListener(buttons: List<Button>) {
        val temperaturePicker = dialogView!!.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_temperature)
        val temperatureSuggestions = getSuggestions()

        for (i in temperatureSuggestions.indices) {
            val suggestion = temperatureSuggestions[i]
            buttons[i].setOnClickListener { temperaturePicker.value = suggestion }
        }
    }

    private fun disableSuggestions() {
        val layoutSuggestions = dialogView!!.findViewById<LinearLayout>(R.id.layout_new_tea_custom_variety)
        layoutSuggestions.visibility = View.GONE
    }

    private fun persistTemperature() {
        val temperaturePicker = dialogView!!.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_temperature)
        val temperature = temperaturePicker.value
        newTeaViewModel.setInfusionTemperature(temperature)
    }

    companion object {
        const val TAG = "TemperaturePickerDialog"
    }
}