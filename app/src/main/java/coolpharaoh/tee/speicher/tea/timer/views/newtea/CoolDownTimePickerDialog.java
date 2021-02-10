package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TimeConverter;

public class CoolDownTimePickerDialog extends DialogFragment {
    public static final String TAG = "CoolDownTimePickerDialog";

    private final NewTeaViewModel newTeaViewModel;
    private View dialogView;

    public CoolDownTimePickerDialog(final NewTeaViewModel newTeaViewModel) {
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
        disableSuggestions();

        return new AlertDialog.Builder(activity)
                .setView(dialogView)
                .setTitle(R.string.newtea_dialog_cool_down_time_header)
                .setNegativeButton(R.string.newtea_dialog_picker_negative, null)
                .setPositiveButton(R.string.newtea_dialog_picker_positive, (dialog, which) -> persistCoolDownTime())
                .create();
    }

    private void setTimePicker() {
        final NumberPicker timePickerMinutes = dialogView.findViewById(R.id.number_picker_dialog_time_minutes);
        final NumberPicker timePickerSeconds = dialogView.findViewById(R.id.number_picker_dialog_time_seconds);
        timePickerMinutes.setMinValue(0);
        timePickerMinutes.setMaxValue(59);
        timePickerSeconds.setMinValue(0);
        timePickerSeconds.setMaxValue(59);
    }

    private void disableSuggestions() {
        final LinearLayout layoutSuggestions = dialogView.findViewById(R.id.layout_picker_suggestions);
        layoutSuggestions.setVisibility(View.GONE);
    }

    private void persistCoolDownTime() {
        final NumberPicker timePickerMinutes = dialogView.findViewById(R.id.number_picker_dialog_time_minutes);
        final NumberPicker timePickerSeconds = dialogView.findViewById(R.id.number_picker_dialog_time_seconds);
        final int minutes = timePickerMinutes.getValue();
        final int seconds = timePickerSeconds.getValue();

        final TimeConverter timeConverter = new TimeConverter(minutes, seconds);
        newTeaViewModel.setInfusionCoolDownTime(timeConverter.getTime());
    }

}
