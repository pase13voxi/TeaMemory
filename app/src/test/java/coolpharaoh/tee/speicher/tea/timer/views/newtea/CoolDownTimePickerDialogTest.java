package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

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

import coolpharaoh.tee.speicher.tea.timer.R;

import static coolpharaoh.tee.speicher.tea.timer.views.newtea.CoolDownTimePickerDialog.TAG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class CoolDownTimePickerDialogTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    NewTeaViewModel newTeaViewModel;

    private CoolDownTimePickerDialog dialogFragment;
    private FragmentManager fragmentManager;

    @Before
    public void setUp() {
        final FragmentActivity activity = Robolectric.buildActivity(FragmentActivity.class).create().start().resume().get();
        fragmentManager = activity.getSupportFragmentManager();
        dialogFragment = new CoolDownTimePickerDialog(newTeaViewModel);
    }

    @Test
    public void showDialog() {
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();
        final ShadowAlertDialog shadowDialog = Shadows.shadowOf(dialog);
        assertThat(shadowDialog.getTitle()).isEqualTo(dialogFragment.getString(R.string.newtea_dialog_cool_down_time_header));

        final LinearLayout linearLayout = dialog.findViewById(R.id.layout_picker_suggestions);
        assertThat(linearLayout.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void acceptInputAndExpectSavedCoolDownTime() {
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final NumberPicker numberPickerTimeMinutes = dialog.findViewById(R.id.number_picker_dialog_time_minutes);
        numberPickerTimeMinutes.setValue(5);

        final NumberPicker numberPickerTimeSeconds = dialog.findViewById(R.id.number_picker_dialog_time_seconds);
        numberPickerTimeSeconds.setValue(45);

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        verify(newTeaViewModel).setInfusionCoolDownTime("05:45");
    }
}
