package coolpharaoh.tee.speicher.tea.timer.views.overview;

import static android.os.Looper.getMainLooper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SortMode.ALPHABETICAL;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SortMode.BY_VARIETY;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SortMode.LAST_USED;
import static coolpharaoh.tee.speicher.tea.timer.views.overview.RecyclerViewConfigurationDialog.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class RecyclerViewConfigurationDialogTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    OverviewViewModel overviewViewModel;

    private RecyclerViewConfigurationDialog dialogFragment;
    private FragmentManager fragmentManager;

    @Before
    public void setUp() {
        final FragmentActivity activity = Robolectric.buildActivity(FragmentActivity.class).create().start().resume().get();
        fragmentManager = activity.getSupportFragmentManager();
        dialogFragment = new RecyclerViewConfigurationDialog(overviewViewModel);
    }

    @Test
    public void showDialogAndExpectTitle() {
        when(overviewViewModel.getSort()).thenReturn(LAST_USED);

        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();
        final TextView textViewSortModeTitle = dialog.findViewById(R.id.text_view_overview_sort_mode_configuration_title);
        final TextView textViewFilterTitle = dialog.findViewById(R.id.text_view_overview_filter_configuration_title);

        assertThat(textViewSortModeTitle.getText()).isEqualTo(dialogFragment.getString(R.string.overview_dialog_sort_title));
        assertThat(textViewFilterTitle.getText()).isEqualTo(dialogFragment.getString(R.string.overview_dialog_filter_title));
    }

    @Test
    public void showDialogAndExpectSortModeByVariety() {
        when(overviewViewModel.getSort()).thenReturn(BY_VARIETY);

        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();
        final List<RadioButton> radioButtons = getRadioButtons(dialog);

        assertThat(radioButtons.get(BY_VARIETY.getChoice()).isChecked()).isTrue();
    }

    @Test
    public void selectAlphabeticallyAndExpectSavedSortMode() {
        when(overviewViewModel.getSort()).thenReturn(LAST_USED);

        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();
        final List<RadioButton> radioButtons = getRadioButtons(dialog);

        radioButtons.get(ALPHABETICAL.getChoice()).performClick();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        shadowOf(getMainLooper()).idle();

        verify(overviewViewModel).setSort(ALPHABETICAL);
    }

    @Test
    public void showDialogAndExpectFilterInStock() {
        when(overviewViewModel.getSort()).thenReturn(LAST_USED);
        when(overviewViewModel.isOverViewInStock()).thenReturn(true);

        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();
        final CheckBox checkBoxInStock = dialog.findViewById(R.id.checkbox_overview_in_stock);

        assertThat(checkBoxInStock.isChecked()).isTrue();
    }

    @Test
    public void selectFilterInStockAndExpectSavedFilter() {
        when(overviewViewModel.getSort()).thenReturn(LAST_USED);

        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();
        final CheckBox checkBoxInStock = dialog.findViewById(R.id.checkbox_overview_in_stock);

        checkBoxInStock.setChecked(true);

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        shadowOf(getMainLooper()).idle();

        verify(overviewViewModel).setOverviewInStock(true);
    }

    private List<RadioButton> getRadioButtons(final AlertDialog dialog) {
        final RadioGroup radioGroup = dialog.findViewById(R.id.radio_group_overview_sort_mode);
        final ArrayList<RadioButton> listRadioButtons = new ArrayList<>();
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            final View o = radioGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                listRadioButtons.add((RadioButton) o);
            }
        }
        return listRadioButtons;
    }
}
