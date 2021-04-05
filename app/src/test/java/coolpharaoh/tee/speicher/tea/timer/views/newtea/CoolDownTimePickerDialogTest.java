package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
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
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;

import coolpharaoh.tee.speicher.tea.timer.R;

import static coolpharaoh.tee.speicher.tea.timer.views.newtea.CoolDownTimePickerDialog.TAG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class CoolDownTimePickerDialogTest {
    private static final String CELSIUS = "Celsius";
    public static final String FAHRENHEIT = "Fahrenheit";

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
        assertThat(shadowDialog.getTitle()).isEqualTo(dialogFragment.getString(R.string.new_tea_dialog_cool_down_time_header));
    }

    @Test
    public void showDialogWithNoConfiguredTemperatureAndExpectNoCalculatedCoolDownTime() {
        when(newTeaViewModel.getInfusionTemperature()).thenReturn(-500);
        when(newTeaViewModel.getTemperatureUnit()).thenReturn(CELSIUS);

        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();
        final ShadowAlertDialog shadowDialog = Shadows.shadowOf(dialog);
        assertThat(shadowDialog.getTitle()).isEqualTo(dialogFragment.getString(R.string.new_tea_dialog_cool_down_time_header));

        final LinearLayout linearLayout = dialog.findViewById(R.id.new_tea_layout_custom_variety);
        assertThat(linearLayout.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void showDialogWith100CelsiusAndExpectNoCalculatedCoolDownTime() {
        when(newTeaViewModel.getInfusionTemperature()).thenReturn(212);
        when(newTeaViewModel.getTemperatureUnit()).thenReturn(FAHRENHEIT);

        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();
        final ShadowAlertDialog shadowDialog = Shadows.shadowOf(dialog);
        assertThat(shadowDialog.getTitle()).isEqualTo(dialogFragment.getString(R.string.new_tea_dialog_cool_down_time_header));

        final LinearLayout linearLayout = dialog.findViewById(R.id.new_tea_layout_custom_variety);
        assertThat(linearLayout.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void showDialogAndExpectCalculatedCoolDownTime() {
        when(newTeaViewModel.getInfusionTemperature()).thenReturn(90);
        when(newTeaViewModel.getTemperatureUnit()).thenReturn(CELSIUS);
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final TextView textViewSuggestions = dialog.findViewById(R.id.textView_suggestions_description);
        assertThat(textViewSuggestions.getText()).hasToString(dialogFragment.getString(R.string.new_tea_dialog_cool_down_time_calculated_suggestion));

        final Button buttonSuggestion1 = dialog.findViewById(R.id.new_tea_button_picker_suggestion_1);
        assertThat(buttonSuggestion1)
                .extracting(View::getVisibility, tv -> tv.getText().toString())
                .containsExactly(View.VISIBLE, "5:00");

        final Button buttonSuggestion2 = dialog.findViewById(R.id.new_tea_button_picker_suggestion_2);
        assertThat(buttonSuggestion2.getVisibility()).isEqualTo(View.GONE);

        final Button buttonSuggestion3 = dialog.findViewById(R.id.new_tea_button_picker_suggestion_3);
        assertThat(buttonSuggestion3.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void clickCalculatedCoolDownTimeAndExpectShownCalculatedCoolDownTime() {
        when(newTeaViewModel.getInfusionTemperature()).thenReturn(95);
        when(newTeaViewModel.getTemperatureUnit()).thenReturn(CELSIUS);
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final Button buttonSuggestion = dialog.findViewById(R.id.new_tea_button_picker_suggestion_1);
        buttonSuggestion.performClick();

        final NumberPicker numberPickerTimeMinutes = dialog.findViewById(R.id.new_tea_number_picker_dialog_time_minutes);
        assertThat(numberPickerTimeMinutes.getValue()).isEqualTo(2);

        final NumberPicker numberPickerTimeSeconds = dialog.findViewById(R.id.new_tea_number_picker_dialog_time_seconds);
        assertThat(numberPickerTimeSeconds.getValue()).isEqualTo(30);
    }

    @Test
    public void acceptInputAndExpectSavedCoolDownTime() {
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final NumberPicker numberPickerTimeMinutes = dialog.findViewById(R.id.new_tea_number_picker_dialog_time_minutes);
        numberPickerTimeMinutes.setValue(5);

        final NumberPicker numberPickerTimeSeconds = dialog.findViewById(R.id.new_tea_number_picker_dialog_time_seconds);
        numberPickerTimeSeconds.setValue(45);

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        verify(newTeaViewModel).setInfusionCoolDownTime("05:45");
    }

    @Test
    public void inputZeroTimeAndExpectSavedNull() {
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final NumberPicker numberPickerTimeMinutes = dialog.findViewById(R.id.new_tea_number_picker_dialog_time_minutes);
        numberPickerTimeMinutes.setValue(0);

        final NumberPicker numberPickerTimeSeconds = dialog.findViewById(R.id.new_tea_number_picker_dialog_time_seconds);
        numberPickerTimeSeconds.setValue(0);

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        verify(newTeaViewModel).setInfusionCoolDownTime(null);
    }

    @Test
    public void showExistingCoolDownTimeConfiguration() {
        when(newTeaViewModel.getInfusionCoolDownTime()).thenReturn("05:15");

        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final NumberPicker numberPickerTimeMinutes = dialog.findViewById(R.id.new_tea_number_picker_dialog_time_minutes);
        assertThat(numberPickerTimeMinutes.getValue()).isEqualTo(5);

        final NumberPicker numberPickerTimeSeconds = dialog.findViewById(R.id.new_tea_number_picker_dialog_time_seconds);
        assertThat(numberPickerTimeSeconds.getValue()).isEqualTo(15);
    }
}
