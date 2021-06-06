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
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind;
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions.Suggestions;

import static coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind.GRAM;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind.TEA_BAG;

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

        return new AlertDialog.Builder(activity, R.style.dialog_theme)
                .setView(dialogView)
                .setTitle(R.string.new_tea_dialog_amount_header)
                .setNegativeButton(R.string.new_tea_dialog_picker_negative, null)
                .setPositiveButton(R.string.new_tea_dialog_picker_positive, (dialog, which) -> persistAmount())
                .create();
    }

    private void setAmountPicker() {
        final NumberPicker amountPicker = dialogView.findViewById(R.id.number_picker_new_tea_dialog_amount);
        amountPicker.setMinValue(0);
        amountPicker.setMaxValue(100);

        final NumberPicker amountPickerKind = dialogView.findViewById(R.id.number_picker_new_tea_dialog_amount_kind);
        amountPickerKind.setMinValue(0);
        amountPickerKind.setMaxValue(2);
        amountPickerKind.setDisplayedValues(getResources().getStringArray(R.array.new_tea_dialog_amount_kind));
        amountPickerKind.setOnValueChangedListener((numberPicker, oldValue, newValue) -> setSuggestions());

        setConfiguredValues(amountPicker, amountPickerKind);
    }

    private void setConfiguredValues(final NumberPicker amountPicker, final NumberPicker amountKindPicker) {
        final int amount = newTeaViewModel.getAmount();
        final AmountKind amountKind = newTeaViewModel.getAmountKind();

        if (amount != -500) {
            amountPicker.setValue(amount);
        }

        amountKindPicker.setValue(amountKind.getChoice());
    }

    private void setSuggestions() {
        final List<Button> buttons = new ArrayList<>();
        buttons.add(dialogView.findViewById(R.id.button_new_tea_picker_suggestion_1));
        buttons.add(dialogView.findViewById(R.id.button_new_tea_picker_suggestion_2));
        buttons.add(dialogView.findViewById(R.id.button_new_tea_picker_suggestion_3));

        if (getSuggestions() != null && getSuggestions().length > 0) {
            enableSuggestions();
            fillSuggestions(buttons);
            setClickListener(buttons);
        } else {
            disableSuggestions();
        }
    }

    private int[] getSuggestions() {
        final NumberPicker amountPickerKind = dialogView.findViewById(R.id.number_picker_new_tea_dialog_amount_kind);
        if (GRAM.getChoice() == amountPickerKind.getValue()) {
            return suggestions.getAmountGrSuggestions();
        } else if (TEA_BAG.getChoice() == amountPickerKind.getValue()) {
            return suggestions.getAmountTbSuggestions();
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
        final NumberPicker amountPicker = dialogView.findViewById(R.id.number_picker_new_tea_dialog_amount);
        final int[] amountSuggestions = getSuggestions();

        for (int i = 0; i < amountSuggestions.length; i++) {
            final int amount = amountSuggestions[i];
            buttons.get(i).setOnClickListener(view -> amountPicker.setValue(amount));
        }
    }

    private void enableSuggestions() {
        final LinearLayout layoutSuggestions = dialogView.findViewById(R.id.layout_new_tea_custom_variety);
        layoutSuggestions.setVisibility(View.VISIBLE);
    }

    private void disableSuggestions() {
        final LinearLayout layoutSuggestions = dialogView.findViewById(R.id.layout_new_tea_custom_variety);
        layoutSuggestions.setVisibility(View.GONE);
    }

    private void persistAmount() {
        final NumberPicker amountPicker = dialogView.findViewById(R.id.number_picker_new_tea_dialog_amount);
        final int amount = amountPicker.getValue();

        final NumberPicker amountKindPicker = dialogView.findViewById(R.id.number_picker_new_tea_dialog_amount_kind);
        final int amountKindChoice = amountKindPicker.getValue();

        newTeaViewModel.setAmount(amount, AmountKind.fromChoice(amountKindChoice));
    }

}
