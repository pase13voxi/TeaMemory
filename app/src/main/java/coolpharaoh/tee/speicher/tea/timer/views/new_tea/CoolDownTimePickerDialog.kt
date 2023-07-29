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
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation.celsiusToCoolDownTime
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation.fahrenheitToCelsius
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TimeConverter
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit

class CoolDownTimePickerDialog(private val newTeaViewModel: NewTeaViewModel) : DialogFragment() {

    private var dialogView: View? = null

    override fun onCreateDialog(savedInstancesState: Bundle?): Dialog {
        val activity: Activity = requireActivity()
        val parent = activity.findViewById<ViewGroup>(R.id.new_tea_parent)
        val inflater = activity.layoutInflater
        dialogView = inflater.inflate(R.layout.dialog_time_picker, parent, false)

        setTimePicker()
        setCalculatedCoolDownTime()

        return AlertDialog.Builder(activity, R.style.dialog_theme)
            .setView(dialogView)
            .setTitle(R.string.new_tea_dialog_cool_down_time_header)
            .setNegativeButton(R.string.new_tea_dialog_picker_negative, null)
            .setPositiveButton(R.string.new_tea_dialog_picker_positive) { dialog, which -> persistCoolDownTime() }
            .create()
    }

    private fun setTimePicker() {
        val timePickerMinutes = dialogView!!.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_minutes)
        timePickerMinutes.minValue = 0
        timePickerMinutes.maxValue = 59
        val timePickerSeconds = dialogView!!.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_seconds)
        timePickerSeconds.minValue = 0
        timePickerSeconds.maxValue = 59
        timePickerSeconds.setFormatter { value: Int -> String.format("%02d", value) }

        setConfiguredValues(timePickerMinutes, timePickerSeconds)
    }

    private fun setConfiguredValues(timePickerMinutes: NumberPicker, timePickerSeconds: NumberPicker) {
        val coolDownTime = newTeaViewModel.getInfusionCoolDownTime()

        if (coolDownTime != null) {
            val timeConverter = TimeConverter(coolDownTime)

            timePickerMinutes.value = timeConverter.minutes
            timePickerSeconds.value = timeConverter.seconds
        }
    }

    private fun setCalculatedCoolDownTime() {
        val buttons: MutableList<Button> = ArrayList()
        buttons.add(dialogView!!.findViewById(R.id.button_new_tea_picker_suggestion_1))
        buttons.add(dialogView!!.findViewById(R.id.button_new_tea_picker_suggestion_2))
        buttons.add(dialogView!!.findViewById(R.id.button_new_tea_picker_suggestion_3))

        val temperatureUnit = newTeaViewModel.getTemperatureUnit()
        var temperature = newTeaViewModel.getInfusionTemperature()

        //Falls nötig in Celsius umwandeln
        if (TemperatureUnit.FAHRENHEIT == temperatureUnit) {
            temperature = fahrenheitToCelsius(temperature)
        }
        if (temperature != -500 && temperature != 100) {
            val coolDownTime = celsiusToCoolDownTime(temperature)
            fillSuggestions(buttons, coolDownTime)
            setClickListener(buttons, coolDownTime)
        } else {
            disableSuggestions()
        }
    }

    private fun setClickListener(buttons: List<Button>, coolDownTime: String?) {
        val timePickerMinutes = dialogView!!.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_minutes)
        val timePickerSeconds = dialogView!!.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_seconds)

        val timeConverter = TimeConverter(coolDownTime)
        buttons[0].setOnClickListener { view: View? ->
            timePickerMinutes.value = timeConverter.minutes
            timePickerSeconds.value = timeConverter.seconds
        }
    }

    private fun fillSuggestions(buttons: List<Button>, coolDownTime: String?) {
        val textViewSuggestions = dialogView!!.findViewById<TextView>(R.id.text_view_new_tea_suggestions_description)
        textViewSuggestions.setText(R.string.new_tea_dialog_cool_down_time_calculated_suggestion)

        buttons[0].text = coolDownTime
        buttons[1].visibility = View.GONE
        buttons[2].visibility = View.GONE
    }

    private fun disableSuggestions() {
        val layoutSuggestions = dialogView!!.findViewById<LinearLayout>(R.id.layout_new_tea_custom_variety)
        layoutSuggestions.visibility = View.GONE
    }

    private fun persistCoolDownTime() {
        val timePickerMinutes = dialogView!!.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_minutes)
        val timePickerSeconds = dialogView!!.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_seconds)
        val minutes = timePickerMinutes.value
        val seconds = timePickerSeconds.value

        if (minutes == 0 && seconds == 0) {
            newTeaViewModel.setInfusionCoolDownTime(null)
        } else {
            val timeConverter = TimeConverter(minutes, seconds)
            newTeaViewModel.setInfusionCoolDownTime(timeConverter.time)
        }
    }

    companion object {
        const val TAG = "CoolDownTimePickerDialog"
    }
}