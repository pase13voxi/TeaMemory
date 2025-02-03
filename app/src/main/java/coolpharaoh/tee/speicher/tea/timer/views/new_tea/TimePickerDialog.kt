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
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TimeConverter
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions.Suggestions

class TimePickerDialog(private val suggestions: Suggestions, private val newTeaViewModel: NewTeaViewModel) : DialogFragment() {

    private lateinit var dialogView: View

    override fun onCreateDialog(savedInstancesState: Bundle?): Dialog {
        val activity: Activity = requireActivity()
        val parent = activity.findViewById<ViewGroup>(R.id.new_tea_parent)
        val inflater = activity.layoutInflater
        dialogView = inflater.inflate(R.layout.dialog_time_picker, parent, false)

        setTimePicker()
        setSuggestions()

        return AlertDialog.Builder(activity, R.style.dialog_theme)
            .setView(dialogView)
            .setTitle(R.string.new_tea_dialog_time_header)
            .setNegativeButton(R.string.new_tea_dialog_picker_negative, null)
            .setPositiveButton(R.string.new_tea_dialog_picker_positive) { _, _ -> persistTime() }
            .create()
    }

    private fun setTimePicker() {
        val timePickerMinutes = dialogView.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_minutes)
        timePickerMinutes.minValue = 0
        timePickerMinutes.maxValue = 59
        val timePickerSeconds = dialogView.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_seconds)
        timePickerSeconds.minValue = 0
        timePickerSeconds.maxValue = 59
        timePickerSeconds.setFormatter { value: Int -> String.format("%02d", value) }

        setConfiguredValues(timePickerMinutes, timePickerSeconds)
    }

    private fun setConfiguredValues(timePickerMinutes: NumberPicker, timePickerSeconds: NumberPicker) {
        val time = newTeaViewModel.getInfusionTime()

        if (time != null) {
            val timeConverter = TimeConverter(time)

            timePickerMinutes.value = timeConverter.minutes
            timePickerSeconds.value = timeConverter.seconds
        }
    }

    private fun setSuggestions() {
        val buttons: MutableList<Button> = ArrayList()
        buttons.add(dialogView.findViewById(R.id.button_new_tea_picker_suggestion_1))
        buttons.add(dialogView.findViewById(R.id.button_new_tea_picker_suggestion_2))
        buttons.add(dialogView.findViewById(R.id.button_new_tea_picker_suggestion_3))

        if (suggestions.timeSuggestions.isNotEmpty()) {
            fillSuggestions(buttons)
            setClickListener(buttons)
        } else {
            disableSuggestions()
        }
    }

    private fun fillSuggestions(buttons: List<Button>) {
        val timeSuggestions = suggestions.timeSuggestions

        for (i in 0..2) {
            if (i < timeSuggestions.size) {
                buttons[i].text = timeSuggestions[i]
            } else {
                buttons[i].visibility = View.GONE
            }
        }
    }

    private fun setClickListener(buttons: List<Button>) {
        val timePickerMinutes = dialogView.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_minutes)
        val timePickerSeconds = dialogView.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_seconds)
        val timeSuggestions = suggestions.timeSuggestions

        for (i in timeSuggestions.indices) {
            val timeConverter = TimeConverter(timeSuggestions[i])

            buttons[i].setOnClickListener {
                timePickerMinutes.value = timeConverter.minutes
                timePickerSeconds.value = timeConverter.seconds
            }
        }
    }

    private fun disableSuggestions() {
        val layoutSuggestions = dialogView.findViewById<LinearLayout>(R.id.layout_new_tea_custom_variety)
        layoutSuggestions.visibility = View.GONE
    }

    private fun persistTime() {
        val timePickerMinutes = dialogView.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_minutes)
        val timePickerSeconds = dialogView.findViewById<NumberPicker>(R.id.number_picker_new_tea_dialog_time_seconds)
        val minutes = timePickerMinutes.value
        val seconds = timePickerSeconds.value

        if (minutes == 0 && seconds == 0) {
            newTeaViewModel.setInfusionTime(null)
        } else {
            val timeConverter = TimeConverter(minutes, seconds)
            newTeaViewModel.setInfusionTime(timeConverter.time)
        }
    }

    companion object {
        const val TAG = "TimePickerDialog"
    }
}