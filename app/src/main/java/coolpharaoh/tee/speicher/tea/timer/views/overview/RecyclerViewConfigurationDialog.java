package coolpharaoh.tee.speicher.tea.timer.views.overview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SortMode;

public class RecyclerViewConfigurationDialog extends DialogFragment {
    public static final String TAG = "RecyclerViewConfigurationDialog";

    private final OverviewViewModel overviewViewModel;
    private View dialogView;

    public RecyclerViewConfigurationDialog(final OverviewViewModel overviewViewModel) {
        this.overviewViewModel = overviewViewModel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstancesState) {
        final Activity activity = requireActivity();
        final ViewGroup parent = activity.findViewById(R.id.overview_parent);
        final LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_overview_list_configuration, parent, false);

        defineVarietyRadioGroup();
        setShowInStock();

        return new AlertDialog.Builder(activity, R.style.dialog_theme)
                .setView(dialogView)
                .setNegativeButton(R.string.overview_dialog_sort_negative, null)
                .setPositiveButton(R.string.overview_dialog_sort_positive, (dialog, which) -> {
                    persistSortMode();
                    persistShowInStock();
                })
                .create();
    }

    private void defineVarietyRadioGroup() {
        final String[] sortOptions = getResources().getStringArray(R.array.overview_sort_options);
        final RadioGroup sortModeRadioGroup = dialogView.findViewById(R.id.radio_group_overview_sort_mode);

        for (final String variety : sortOptions) {
            final RadioButton varietyRadioButton = createRadioButton(variety);
            sortModeRadioGroup.addView(varietyRadioButton);
        }

        setConfiguredValues(sortModeRadioGroup);
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
        final RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(dpToPixel(20), dpToPixel(10), 0, 0);
        varietyRadioButton.setLayoutParams(params);
        varietyRadioButton.setPadding(dpToPixel(15), 0, 0, 0);
        varietyRadioButton.setTextSize(16);
        return varietyRadioButton;
    }

    private int dpToPixel(final int dpValue) {
        final float density = getActivity().getApplication().getResources().getDisplayMetrics().density;
        return (int) (dpValue * density); // margin in pixels
    }

    private void setConfiguredValues(final RadioGroup varietyRadioGroup) {
        final SortMode sortMode = overviewViewModel.getSort();
        final List<RadioButton> radioButtons = getRadioButtons(varietyRadioGroup);

        radioButtons.get(sortMode.getChoice()).setChecked(true);
    }

    private List<RadioButton> getRadioButtons(final RadioGroup radioGroup) {
        final ArrayList<RadioButton> listRadioButtons = new ArrayList<>();
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            final View o = radioGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                listRadioButtons.add((RadioButton) o);
            }
        }
        return listRadioButtons;
    }

    private void setShowInStock() {
        final CheckBox checkBoxInStock = dialogView.findViewById(R.id.checkbox_overview_in_stock);
        checkBoxInStock.setChecked(overviewViewModel.isOverViewInStock());
    }

    private void persistSortMode() {
        final SortMode sortMode = extractSortMode();

        overviewViewModel.setSort(sortMode);
    }

    private SortMode extractSortMode() {
        final RadioGroup sortModeRadioGroup = dialogView.findViewById(R.id.radio_group_overview_sort_mode);
        final List<RadioButton> radioButtons = getRadioButtons(sortModeRadioGroup);

        SortMode sortMode = SortMode.LAST_USED;
        for (int i = 0; i < radioButtons.size(); i++) {
            if (radioButtons.get(i).isChecked()) {
                sortMode = SortMode.fromChoice(i);
            }
        }
        return sortMode;
    }

    private void persistShowInStock() {
        final CheckBox checkBoxInStock = dialogView.findViewById(R.id.checkbox_overview_in_stock);

        overviewViewModel.setOverviewInStock(checkBoxInStock.isChecked());
    }
}
