package coolpharaoh.tee.speicher.tea.timer.views.overview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;

public class SortPickerDialog extends DialogFragment {
    public static final String TAG = "SortPickerDialog";

    private final OverviewViewModel overviewViewModel;
    private View dialogView;

    public SortPickerDialog(final OverviewViewModel overviewViewModel) {
        this.overviewViewModel = overviewViewModel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstancesState) {
        final Activity activity = requireActivity();
        final ViewGroup parent = activity.findViewById(R.id.new_tea_parent);
        final LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_overview_sort_picker, parent, false);

        fillShowHeader();
        defineSortRadioGroup();

        return new AlertDialog.Builder(activity, R.style.dialog_theme)
                .setView(dialogView)
                .setTitle(R.string.overview_dialog_sort_title)
                .setNegativeButton(R.string.overview_dialog_sort_negative, null)
                .setPositiveButton(R.string.overview_dialog_sort_positive, (dialog, which) -> {
                    persistShowHeader();
                    persistSortMode();
                })
                .create();
    }

    private void fillShowHeader() {
        final boolean overviewHeader = overviewViewModel.isOverviewHeader();
        final SwitchCompat switchShowHeader = dialogView.findViewById(R.id.switch_overview_show_header);
        switchShowHeader.setChecked(overviewHeader);
    }

    private void defineSortRadioGroup() {
        final String[] sortOptions = getResources().getStringArray(R.array.overview_sort_options);
        final RadioGroup varietyRadioGroup = dialogView.findViewById(R.id.radio_group_overview_sort_options_input);

        for (final String sortOption : sortOptions) {
            final RadioButton varietyRadioButton = createRadioButton(sortOption);
            varietyRadioGroup.addView(varietyRadioButton);
        }

        setConfiguredValues(varietyRadioGroup);
    }

    private void setConfiguredValues(final RadioGroup varietyRadioGroup) {
        final int sortOption = overviewViewModel.getSort();
        final List<RadioButton> radioButtons = getRadioButtons(varietyRadioGroup);
        radioButtons.get(sortOption).setChecked(true);
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
                        ContextCompat.getColor(getActivity().getApplication(), R.color.background_grey),
                        ContextCompat.getColor(getActivity().getApplication(), R.color.background_green)
                }
        );
        varietyRadioButton.setButtonTintList(colorStateList);
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(dpToPixel(20), dpToPixel(10), 0, 0);
        varietyRadioButton.setLayoutParams(params);
        varietyRadioButton.setPadding(dpToPixel(15), 0, 0, 0);
        varietyRadioButton.setTextSize(16);
        return varietyRadioButton;
    }

    private int dpToPixel(int dpValue) {
        final float density = getActivity().getApplication().getResources().getDisplayMetrics().density;
        return (int) (dpValue * density); // margin in pixels
    }

    private void persistShowHeader() {
        final SwitchCompat switchShowHeader = dialogView.findViewById(R.id.switch_overview_show_header);
        overviewViewModel.setOverviewHeader(switchShowHeader.isChecked());
    }

    private void persistSortMode() {
        final RadioGroup varietyRadioGroup = dialogView.findViewById(R.id.radio_group_overview_sort_options_input);
        final RadioButton radioButton = varietyRadioGroup.findViewById(varietyRadioGroup.getCheckedRadioButtonId());
        final CharSequence text = radioButton.getText();
        final String[] sortOptions = getResources().getStringArray(R.array.overview_sort_options);

        int checkedSortMode = 0;
        for (int i = 0; i < sortOptions.length; i++) {
            if (sortOptions[i].contentEquals(text)) {
                checkedSortMode = i;
            }
        }
        overviewViewModel.setSort(checkedSortMode);
    }
}
