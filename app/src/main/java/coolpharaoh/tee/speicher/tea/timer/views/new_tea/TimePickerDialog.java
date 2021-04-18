package coolpharaoh.tee.speicher.tea.timer.views.new_tea;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TimeConverter;
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions.Suggestions;

public class TimePickerDialog extends DialogFragment {
    public static final String TAG = "TimePickerDialog";

    private final Suggestions suggestions;
    private final NewTeaViewModel newTeaViewModel;
    private View dialogView;

    public TimePickerDialog(final Suggestions suggestions, final NewTeaViewModel newTeaViewModel) {
        this.suggestions = suggestions;
        this.newTeaViewModel = newTeaViewModel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstancesState) {
        Activity activity = requireActivity();
        ViewGroup parent = activity.findViewById(R.id.new_tea_parent);
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_time_picker, parent, false);

        setTimePicker();
        setSuggestions();

        return new AlertDialog.Builder(activity, R.style.dialog_theme)
                .setView(dialogView)
                .setTitle(R.string.new_tea_dialog_time_header)
                .setNegativeButton(R.string.new_tea_dialog_picker_negative, null)
                .setPositiveButton(R.string.new_tea_dialog_picker_positive, (dialog, which) -> persistTime())
                .create();
    }

    private void setTimePicker() {
        final NumberPicker timePickerMinutes = dialogView.findViewById(R.id.number_picker_new_tea_dialog_time_minutes);
        timePickerMinutes.setMinValue(0);
        timePickerMinutes.setMaxValue(59);
        final NumberPicker timePickerSeconds = dialogView.findViewById(R.id.number_picker_new_tea_dialog_time_seconds);
        timePickerSeconds.setMinValue(0);
        timePickerSeconds.setMaxValue(59);
        timePickerSeconds.setFormatter(value -> String.format("%02d", value));

        setConfiguredValues(timePickerMinutes, timePickerSeconds);
    }

    private void setConfiguredValues(final NumberPicker timePickerMinutes, final NumberPicker timePickerSeconds) {
        final String time = newTeaViewModel.getInfusionTime();

        if (time != null) {
            final TimeConverter timeConverter = new TimeConverter(time);

            timePickerMinutes.setValue(timeConverter.getMinutes());
            timePickerSeconds.setValue(timeConverter.getSeconds());
        }
    }

    private void setSuggestions() {
        final List<Button> buttons = new ArrayList<>();
        buttons.add(dialogView.findViewById(R.id.button_new_tea_picker_suggestion_1));
        buttons.add(dialogView.findViewById(R.id.button_new_tea_picker_suggestion_2));
        buttons.add(dialogView.findViewById(R.id.button_new_tea_picker_suggestion_3));

        if (suggestions.getTimeSuggestions() != null && suggestions.getTimeSuggestions().length > 0) {
            fillSuggestions(buttons);
            setClickListener(buttons);
        } else {
            disableSuggestions();
        }
    }

    private void fillSuggestions(final List<Button> buttons) {
        final String[] timeSuggestions = suggestions.getTimeSuggestions();

        for (int i = 0; i < 3; i++) {
            if (i < timeSuggestions.length) {
                buttons.get(i).setText(timeSuggestions[i]);
            } else {
                buttons.get(i).setVisibility(View.GONE);
            }
        }
    }

    private void setClickListener(final List<Button> buttons) {
        final NumberPicker timePickerMinutes = dialogView.findViewById(R.id.number_picker_new_tea_dialog_time_minutes);
        final NumberPicker timePickerSeconds = dialogView.findViewById(R.id.number_picker_new_tea_dialog_time_seconds);
        final String[] timeSuggestions = suggestions.getTimeSuggestions();

        for (int i = 0; i < timeSuggestions.length; i++) {
            final TimeConverter timeConverter = new TimeConverter(timeSuggestions[i]);

            buttons.get(i).setOnClickListener(view -> {
                timePickerMinutes.setValue(timeConverter.getMinutes());
                timePickerSeconds.setValue(timeConverter.getSeconds());
            });
        }
    }

    private void disableSuggestions() {
        final LinearLayout layoutSuggestions = dialogView.findViewById(R.id.layout_new_tea_custom_variety);
        layoutSuggestions.setVisibility(View.GONE);
    }

    private void persistTime() {
        final NumberPicker timePickerMinutes = dialogView.findViewById(R.id.number_picker_new_tea_dialog_time_minutes);
        final NumberPicker timePickerSeconds = dialogView.findViewById(R.id.number_picker_new_tea_dialog_time_seconds);
        final int minutes = timePickerMinutes.getValue();
        final int seconds = timePickerSeconds.getValue();

        if (minutes == 0 && seconds == 0) {
            newTeaViewModel.setInfusionTime(null);
        } else {
            final TimeConverter timeConverter = new TimeConverter(minutes, seconds);
            newTeaViewModel.setInfusionTime(timeConverter.getTime());
        }
    }

}
