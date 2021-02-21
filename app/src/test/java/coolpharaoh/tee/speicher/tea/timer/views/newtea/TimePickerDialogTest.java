package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.widget.Button;
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
import coolpharaoh.tee.speicher.tea.timer.views.newtea.suggestions.Suggestions;

import static coolpharaoh.tee.speicher.tea.timer.views.newtea.TimePickerDialog.TAG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class TimePickerDialogTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    NewTeaViewModel newTeaViewModel;
    @Mock
    Suggestions suggestions;

    private TimePickerDialog dialogFragment;
    private FragmentManager fragmentManager;

    @Before
    public void setUp() {
        final FragmentActivity activity = Robolectric.buildActivity(FragmentActivity.class).create().start().resume().get();
        fragmentManager = activity.getSupportFragmentManager();
        dialogFragment = new TimePickerDialog(suggestions, newTeaViewModel);
    }

    @Test
    public void showDialogAndExpectTitle() {
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();
        final ShadowAlertDialog shadowDialog = Shadows.shadowOf(dialog);
        assertThat(shadowDialog.getTitle()).isEqualTo(dialogFragment.getString(R.string.new_tea_dialog_time_header));
    }

    @Test
    public void showDialogAndExpectTwoSuggestions() {
        when(suggestions.getTimeSuggestions()).thenReturn(new String[]{"2:00", "3:00"});
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final Button buttonSuggestion1 = dialog.findViewById(R.id.new_tea_button_picker_suggestion_1);
        assertThat(buttonSuggestion1)
                .extracting(View::getVisibility, tv -> tv.getText().toString())
                .containsExactly(View.VISIBLE, "2:00");

        final Button buttonSuggestion2 = dialog.findViewById(R.id.new_tea_button_picker_suggestion_2);
        assertThat(buttonSuggestion2)
                .extracting(View::getVisibility, tv -> tv.getText().toString())
                .containsExactly(View.VISIBLE, "3:00");

        final Button buttonSuggestion3 = dialog.findViewById(R.id.new_tea_button_picker_suggestion_3);
        assertThat(buttonSuggestion3.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void clickSuggestionAndExpectShownSuggestion() {
        when(suggestions.getTimeSuggestions()).thenReturn(new String[]{"2:30", "3:00"});
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final Button buttonSuggestion1 = dialog.findViewById(R.id.new_tea_button_picker_suggestion_1);
        buttonSuggestion1.performClick();

        final NumberPicker numberPickerMinutes = dialog.findViewById(R.id.new_tea_number_picker_dialog_time_minutes);
        assertThat(numberPickerMinutes.getValue()).isEqualTo(2);

        final NumberPicker numberPickerSeconds = dialog.findViewById(R.id.new_tea_number_picker_dialog_time_seconds);
        assertThat(numberPickerSeconds.getValue()).isEqualTo(30);
    }

    @Test
    public void showDialogAndExpectNoSuggestions() {
        when(suggestions.getTimeSuggestions()).thenReturn(new String[]{});
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();
        final LinearLayout layoutSuggestions = dialog.findViewById(R.id.new_tea_layout_custom_variety);
        assertThat(layoutSuggestions.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void acceptInputAndExpectSavedTime() {
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final NumberPicker numberPickerTimeMinutes = dialog.findViewById(R.id.new_tea_number_picker_dialog_time_minutes);
        numberPickerTimeMinutes.setValue(5);

        final NumberPicker numberPickerTimeSeconds = dialog.findViewById(R.id.new_tea_number_picker_dialog_time_seconds);
        numberPickerTimeSeconds.setValue(45);

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        verify(newTeaViewModel).setInfusionTime("05:45");
    }

    @Test
    public void showExistingTimeConfiguration() {
        when(newTeaViewModel.getInfusionTime()).thenReturn("05:15");

        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final NumberPicker numberPickerTimeMinutes = dialog.findViewById(R.id.new_tea_number_picker_dialog_time_minutes);
        assertThat(numberPickerTimeMinutes.getValue()).isEqualTo(5);

        final NumberPicker numberPickerTimeSeconds = dialog.findViewById(R.id.new_tea_number_picker_dialog_time_seconds);
        assertThat(numberPickerTimeSeconds.getValue()).isEqualTo(15);
    }
}
