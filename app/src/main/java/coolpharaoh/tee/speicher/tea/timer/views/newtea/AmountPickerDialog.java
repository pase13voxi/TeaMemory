package coolpharaoh.tee.speicher.tea.timer.views.newtea;

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
import coolpharaoh.tee.speicher.tea.timer.views.newtea.suggestions.Suggestions;

public class AmountPickerDialog extends DialogFragment {
    public static final String TAG = "AmountPickerDialog";

    private final Suggestions suggestions;
    private final NewTeaViewModel newTeaViewModel;
    private View dialogView;

    public AmountPickerDialog(final Suggestions suggestions, final NewTeaViewModel newTeaViewModel) {
        this.suggestions = suggestions;
        this.newTeaViewModel = newTeaViewModel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstancesState) {
        Activity activity = requireActivity();
        ViewGroup parent = activity.findViewById(R.id.new_tea_parent);
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_amount_picker, parent, false);

        setAmountPicker();
        setSuggestions();

        return new AlertDialog.Builder(activity)
                .setView(dialogView)
                .setTitle(R.string.newtea_dialog_amount_header)
                .setNegativeButton(R.string.newtea_dialog_picker_negative, null)
                .setPositiveButton(R.string.newtea_dialog_picker_positive, (dialog, which) -> persistAmount())
                .create();
    }

    private void setAmountPicker() {
        final NumberPicker amountPicker = dialogView.findViewById(R.id.number_picker_dialog_amount);
        amountPicker.setMinValue(0);
        amountPicker.setMaxValue(100);

        final NumberPicker amountPickerDecimal = dialogView.findViewById(R.id.number_picker_dialog_amount_decimal);
        amountPickerDecimal.setMinValue(0);
        amountPickerDecimal.setMaxValue(9);

        final NumberPicker amountPickerKind = dialogView.findViewById(R.id.number_picker_dialog_amount_kind);
        amountPickerKind.setMinValue(0);
        amountPickerKind.setMaxValue(1);
        amountPickerKind.setDisplayedValues(getResources().getStringArray(R.array.newtea_dialog_amount_kind));
        amountPickerKind.setOnValueChangedListener((numberPicker, oldValue, newValue) -> setSuggestions());
    }

    private void setSuggestions() {
        final List<Button> buttons = new ArrayList<>();
        buttons.add(dialogView.findViewById(R.id.button_picker_suggestion_1));
        buttons.add(dialogView.findViewById(R.id.button_picker_suggestion_2));
        buttons.add(dialogView.findViewById(R.id.button_picker_suggestion_3));

        if (getSuggestions() != null && getSuggestions().length > 0) {
            enableSuggestions();
            fillSuggestions(buttons);
            setClickListener(buttons);
        } else {
            disableSuggestions();
        }
    }

    private int[] getSuggestions() {
        final NumberPicker amountPickerKind = dialogView.findViewById(R.id.number_picker_dialog_amount_kind);
        if (amountPickerKind.getValue() == 1) {
            return suggestions.getAmountGrSuggestions();
        } else {
            return suggestions.getAmountTsSuggestions();
        }
    }

    private void fillSuggestions(final List<Button> buttons) {
        final int[] amountSuggestions = getSuggestions();

        for (int i = 0; i < 3; i++) {
            if (i < amountSuggestions.length) {
                buttons.get(i).setVisibility(View.VISIBLE);
                buttons.get(i).setText(String.valueOf(amountSuggestions[i]));
            } else {
                buttons.get(i).setVisibility(View.GONE);
            }
        }
    }

    private void setClickListener(final List<Button> buttons) {
        final NumberPicker amountPicker = dialogView.findViewById(R.id.number_picker_dialog_amount);
        final int[] amountSuggestions = getSuggestions();

        for (int i = 0; i < amountSuggestions.length; i++) {
            final int amount = amountSuggestions[i];
            buttons.get(i).setOnClickListener(view -> amountPicker.setValue(amount));
        }
    }

    private void enableSuggestions() {
        final LinearLayout layoutSuggestions = dialogView.findViewById(R.id.layout_picker_suggestions);
        layoutSuggestions.setVisibility(View.VISIBLE);
    }

    private void disableSuggestions() {
        final LinearLayout layoutSuggestions = dialogView.findViewById(R.id.layout_picker_suggestions);
        layoutSuggestions.setVisibility(View.GONE);
    }

    private void persistAmount() {
        final NumberPicker amountPicker = dialogView.findViewById(R.id.number_picker_dialog_amount);
        final int amount = amountPicker.getValue();

        final NumberPicker amountKindPicker = dialogView.findViewById(R.id.number_picker_dialog_amount_kind);
        final int amountKindPosition = amountKindPicker.getValue();
        final String amountKind;
        if (amountKindPosition == 1) {
            amountKind = "Gr";
        } else {
            amountKind = "Ts";
        }

        newTeaViewModel.setAmount(amount, amountKind);
    }

}
