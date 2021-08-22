package coolpharaoh.tee.speicher.tea.timer.views.overview;

import static android.os.Looper.getMainLooper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;
import static coolpharaoh.tee.speicher.tea.timer.views.new_tea.AmountPickerDialog.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.widget.SwitchCompat;
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
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class SortPickerDialogTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    OverviewViewModel overviewViewModel;

    private SortPickerDialog dialogFragment;
    private FragmentManager fragmentManager;

    @Before
    public void setUp() {
        final FragmentActivity activity = Robolectric.buildActivity(FragmentActivity.class).create().start().resume().get();
        fragmentManager = activity.getSupportFragmentManager();
        dialogFragment = new SortPickerDialog(overviewViewModel);
    }

    @Test
    public void showDialogAndExpectTitle() {
        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();
        final ShadowAlertDialog shadowDialog = Shadows.shadowOf(dialog);
        assertThat(shadowDialog.getTitle()).isEqualTo(dialogFragment.getString(R.string.overview_dialog_sort_title));
    }

    @Test
    public void showDialogAndSortModeAlphabeticalAndCheckedShowHeader() {
        final int sortMode = 1;
        when(overviewViewModel.isOverviewHeader()).thenReturn(true);
        when(overviewViewModel.getSort()).thenReturn(sortMode);

        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();

        final SwitchCompat switchShowHeader = dialog.findViewById(R.id.switch_overview_show_header);
        assertThat(switchShowHeader.isChecked()).isTrue();

        final List<RadioButton> radioButtons = getRadioButtons(dialog);
        assertThat(radioButtons.get(sortMode).isChecked()).isTrue();
    }

    @Test
    public void selectSortModeAlphabeticalAndExpectPersistedAlphabeticalSortMode() {
        final int sortModeAlphabetical = 1;

        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();
        final List<RadioButton> radioButtons = getRadioButtons(dialog);

        radioButtons.get(sortModeAlphabetical).performClick();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        shadowOf(getMainLooper()).idle();

        verify(overviewViewModel).setSort(sortModeAlphabetical);
    }

    @Test
    public void enableShowHeaderAndExpectPersistedShowHeaderTrue() {
        dialogFragment.show(fragmentManager, TAG);
        shadowOf(getMainLooper()).idle();

        final AlertDialog dialog = getLatestAlertDialog();

        final SwitchCompat switchShowHeader = dialog.findViewById(R.id.switch_overview_show_header);
        switchShowHeader.setChecked(true);

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        shadowOf(getMainLooper()).idle();

        verify(overviewViewModel).setOverviewHeader(true);
    }

    private List<RadioButton> getRadioButtons(AlertDialog dialog) {
        final RadioGroup radioGroup = dialog.findViewById(R.id.radio_group_overview_sort_options_input);
        final ArrayList<RadioButton> listRadioButtons = new ArrayList<>();
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View o = radioGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                listRadioButtons.add((RadioButton) o);
            }
        }
        return listRadioButtons;
    }
}
