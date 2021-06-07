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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions.Suggestions;

import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit.FAHRENHEIT;

public class TemperaturePickerDialog extends DialogFragment {
    public static final String TAG = "TemperaturePickerDialog";

    private final Suggestions suggestions;
    private final NewTeaViewModel newTeaViewModel;
    private View dialogView;

    public TemperaturePickerDialog(final Suggestions suggestions, final NewTeaViewModel newTeaViewModel) {
        this.suggestions = suggestions;
        this.newTeaViewModel = newTeaViewModel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstancesState) {
        Activity activity = requireActivity();
        ViewGroup parent = activity.findViewById(R.id.new_tea_parent);
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_temperature_picker, parent, false);

        setTemperaturePicker();
        setTemperatureUnit();
        setSuggestions();

        return new AlertDialog.Builder(activity, R.style.dialog_theme)
                .setView(dialogView)
                .setTitle(R.string.new_tea_dialog_temperature_header)
                .setNegativeButton(R.string.new_tea_dialog_picker_negative, null)
                .setPositiveButton(R.string.new_tea_dialog_picker_positive, (dialog, which) -> persistTemperature())
                .create();
    }

    private void setTemperaturePicker() {
        final NumberPicker temperaturePicker = dialogView.findViewById(R.id.number_picker_new_tea_dialog_temperature);
        if (isFahrenheit()) {
            temperaturePicker.setMinValue(122);
            temperaturePicker.setMaxValue(212);
        } else {
            temperaturePicker.setMinValue(50);
            temperaturePicker.setMaxValue(100);
        }

        setConfiguredValues(temperaturePicker);
    }

    private void setConfiguredValues(final NumberPicker temperaturePicker) {
        final int temperature = newTeaViewModel.getInfusionTemperature();

        if (temperature != -500) {
            temperaturePicker.setValue(temperature);
        }
    }

    private boolean isFahrenheit() {
        return FAHRENHEIT.equals(newTeaViewModel.getTemperatureUnit());
    }

    private void setTemperatureUnit() {
        if (isFahrenheit()) {
            final TextView textViewTemperatureUnit = dialogView.findViewById(R.id.text_view_new_tea_temperature_picker_unit);
            textViewTemperatureUnit.setText(R.string.new_tea_dialog_temperature_fahrenheit);
        }
    }

    private void setSuggestions() {
        final List<Button> buttons = new ArrayList<>();
        buttons.add(dialogView.findViewById(R.id.button_new_tea_picker_suggestion_1));
        buttons.add(dialogView.findViewById(R.id.button_new_tea_picker_suggestion_2));
        buttons.add(dialogView.findViewById(R.id.button_new_tea_picker_suggestion_3));

        if (getSuggestions() != null && getSuggestions().length > 0) {
            fillSuggestions(buttons);
            setClickListener(buttons);
        } else {
            disableSuggestions();
        }
    }

    private int[] getSuggestions() {
        if (isFahrenheit()) {
            return suggestions.getTemperatureFahrenheitSuggestions();
        } else {
            return suggestions.getTemperatureCelsiusSuggestions();
        }
    }

    private void fillSuggestions(final List<Button> buttons) {
        final int[] temperatureSuggestions = getSuggestions();

        for (int i = 0; i < 3; i++) {
            if (i < temperatureSuggestions.length) {
                buttons.get(i).setText(String.valueOf(temperatureSuggestions[i]));
            } else {
                buttons.get(i).setVisibility(View.GONE);
            }
        }
    }

    private void setClickListener(final List<Button> buttons) {
        final NumberPicker temperaturePicker = dialogView.findViewById(R.id.number_picker_new_tea_dialog_temperature);
        final int[] temperatureSuggestions = getSuggestions();

        for (int i = 0; i < temperatureSuggestions.length; i++) {
            final int suggestion = temperatureSuggestions[i];
            buttons.get(i).setOnClickListener(view -> temperaturePicker.setValue(suggestion));
        }
    }

    private void disableSuggestions() {
        final LinearLayout layoutSuggestions = dialogView.findViewById(R.id.layout_new_tea_custom_variety);
        layoutSuggestions.setVisibility(View.GONE);
    }

    private void persistTemperature() {
        final NumberPicker temperaturePicker = dialogView.findViewById(R.id.number_picker_new_tea_dialog_temperature);
        final int temperature = temperaturePicker.getValue();
        newTeaViewModel.setInfusionTemperature(temperature);
    }
}
