package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;

public class VarietyPickerDialog extends DialogFragment {
    public static final String TAG = "VarietyPickerDialog";
    public static final int VARIETY_OTHER = 9;

    private final NewTeaViewModel newTeaViewModel;
    private View dialogView;

    public VarietyPickerDialog(final NewTeaViewModel newTeaViewModel) {
        this.newTeaViewModel = newTeaViewModel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstancesState) {
        Activity activity = requireActivity();
        ViewGroup parent = activity.findViewById(R.id.new_tea_parent);
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_variety_picker, parent, false);

        defineVarietyRadioGroup();

        return new AlertDialog.Builder(activity)
                .setView(dialogView)
                .setTitle(R.string.new_tea_dialog_variety_header)
                .setNegativeButton(R.string.new_tea_dialog_picker_negative, null)
                .setPositiveButton(R.string.new_tea_dialog_picker_positive, (dialog, which) -> persistVariety())
                .create();
    }

    private void defineVarietyRadioGroup() {
        final String[] varietyList = getResources().getStringArray(R.array.new_tea_variety_teas);
        final RadioGroup varietyRadioGroup = dialogView.findViewById(R.id.new_tea_radio_group_variety_input);

        for (final String variety : varietyList) {
            final RadioButton varietyRadioButton = createRadioButton(variety);
            varietyRadioGroup.addView(varietyRadioButton);
        }

        varietyRadioGroup.setOnCheckedChangeListener(this::showCustomVariety);

        setConfiguredValues(varietyRadioGroup, varietyList);
    }

    private void setConfiguredValues(final RadioGroup varietyRadioGroup, final String[] varietyList) {
        final String variety = newTeaViewModel.getVariety();
        final int varietyIndex = Arrays.asList(varietyList).indexOf(variety);
        final List<RadioButton> radioButtons = getRadioButtons(varietyRadioGroup);

        if (varietyIndex == -1) {
            radioButtons.get(VARIETY_OTHER).setChecked(true);
            final EditText editTextCustomVariety = dialogView.findViewById(R.id.new_tea_edit_text_custom_variety);
            editTextCustomVariety.setText(variety);
        } else {
            radioButtons.get(varietyIndex).setChecked(true);
        }
    }

    private List<RadioButton> getRadioButtons(final RadioGroup radioGroup) {
        final ArrayList<RadioButton> listRadioButtons = new ArrayList<>();
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View o = radioGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                listRadioButtons.add((RadioButton) o);
            }
        }
        return listRadioButtons;
    }

    private RadioButton createRadioButton(final String variety) {
        final RadioButton varietyRadioButton = new RadioButton(getActivity());
        varietyRadioButton.setText(variety);
        final ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked}, // unchecked
                        new int[]{android.R.attr.state_checked}  // checked
                },
                new int[]{
                        getResources().getColor(R.color.lightgrey),
                        getResources().getColor(R.color.colorPrimary)
                }
        );
        varietyRadioButton.setButtonTintList(colorStateList);
        varietyRadioButton.setPadding(40, 35, 0, 0);
        varietyRadioButton.setTextSize(17);
        return varietyRadioButton;
    }

    private void showCustomVariety(final RadioGroup radioGroup, final int checkedId) {
        final String[] varietyList = getResources().getStringArray(R.array.new_tea_variety_teas);
        final RadioButton radioButton = radioGroup.findViewById(checkedId);
        final EditText editTextCustomVariety = dialogView.findViewById(R.id.new_tea_edit_text_custom_variety);

        if (varietyList[VARIETY_OTHER].equals(radioButton.getText().toString())) {
            editTextCustomVariety.setVisibility(View.VISIBLE);
        } else {
            editTextCustomVariety.setVisibility(View.GONE);
        }
    }

    private void persistVariety() {
        final EditText editTextCustomVariety = dialogView.findViewById(R.id.new_tea_edit_text_custom_variety);

        if (editTextCustomVariety.getVisibility() == View.VISIBLE &&
                !editTextCustomVariety.getText().toString().isEmpty()) {
            newTeaViewModel.setVariety(editTextCustomVariety.getText().toString());
        } else {
            final RadioGroup varietyRadioGroup = dialogView.findViewById(R.id.new_tea_radio_group_variety_input);
            final RadioButton radioButton = varietyRadioGroup.findViewById(varietyRadioGroup.getCheckedRadioButtonId());

            newTeaViewModel.setVariety(radioButton.getText().toString());
        }
    }

}
