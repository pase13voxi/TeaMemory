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
import coolpharaoh.tee.speicher.tea.timer.views.newtea.suggestions.Suggestions;

import static coolpharaoh.tee.speicher.tea.timer.views.newtea.TemperaturePickerDialog.TAG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class TemperaturePickerDialogTest {
    private static final String FAHRENHEIT = "Fahrenheit";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    NewTeaViewModel newTeaViewModel;
    @Mock
    Suggestions suggestions;

    private TemperaturePickerDialog dialogFragment;
    private FragmentManager fragmentManager;

    @Before
    public void setUp() {
        final FragmentActivity activity = Robolectric.buildActivity(FragmentActivity.class).create().start().resume().get();
        fragmentManager = activity.getSupportFragmentManager();
        dialogFragment = new TemperaturePickerDialog(suggestions, newTeaViewModel);
    }

    @Test
    public void showDialogAndExpectTitle() {
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();
        final ShadowAlertDialog shadowDialog = Shadows.shadowOf(dialog);
        assertThat(shadowDialog.getTitle()).isEqualTo(dialogFragment.getString(R.string.new_tea_dialog_temperature_header));
    }

    @Test
    public void showDialogAndExpectCelsiusUnit() {
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();
        final TextView textViewUnit = dialog.findViewById(R.id.textView_temperature_picker_unit);

        assertThat(textViewUnit.getText()).hasToString(dialogFragment.getString(R.string.new_tea_dialog_temperature_celsius));
    }

    @Test
    public void showDialogAndExpectFahrenheitUnit() {
        when(newTeaViewModel.getTemperatureUnit()).thenReturn(FAHRENHEIT);
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();
        final TextView textViewUnit = dialog.findViewById(R.id.textView_temperature_picker_unit);

        assertThat(textViewUnit.getText()).hasToString(dialogFragment.getString(R.string.new_tea_dialog_temperature_fahrenheit));
    }

    @Test
    public void showDialogAndExpectTwoCelsiusSuggestions() {
        when(suggestions.getTemperatureCelsiusSuggestions()).thenReturn(new int[]{100, 90});
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final Button buttonSuggestion1 = dialog.findViewById(R.id.new_tea_button_picker_suggestion_1);
        assertThat(buttonSuggestion1)
                .extracting(View::getVisibility, tv -> tv.getText().toString())
                .containsExactly(View.VISIBLE, "100");

        final Button buttonSuggestion2 = dialog.findViewById(R.id.new_tea_button_picker_suggestion_2);
        assertThat(buttonSuggestion2)
                .extracting(View::getVisibility, tv -> tv.getText().toString())
                .containsExactly(View.VISIBLE, "90");

        final Button buttonSuggestion3 = dialog.findViewById(R.id.new_tea_button_picker_suggestion_3);
        assertThat(buttonSuggestion3.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void showDialogAndExpectTwoFahrenheitSuggestions() {
        when(suggestions.getTemperatureFahrenheitSuggestions()).thenReturn(new int[]{212, 194});
        when(newTeaViewModel.getTemperatureUnit()).thenReturn(FAHRENHEIT);
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final Button buttonSuggestion1 = dialog.findViewById(R.id.new_tea_button_picker_suggestion_1);
        assertThat(buttonSuggestion1)
                .extracting(View::getVisibility, tv -> tv.getText().toString())
                .containsExactly(View.VISIBLE, "212");

        final Button buttonSuggestion2 = dialog.findViewById(R.id.new_tea_button_picker_suggestion_2);
        assertThat(buttonSuggestion2)
                .extracting(View::getVisibility, tv -> tv.getText().toString())
                .containsExactly(View.VISIBLE, "194");

        final Button buttonSuggestion3 = dialog.findViewById(R.id.new_tea_button_picker_suggestion_3);
        assertThat(buttonSuggestion3.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void showDialogAndExpectNoSuggestions() {
        when(suggestions.getTemperatureCelsiusSuggestions()).thenReturn(new int[]{});
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();
        final LinearLayout layoutSuggestions = dialog.findViewById(R.id.new_tea_layout_custom_variety);
        assertThat(layoutSuggestions.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void showDialogAndClickSuggestions() {
        when(suggestions.getTemperatureCelsiusSuggestions()).thenReturn(new int[]{100, 90, 80});
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final Button buttonSuggestion2 = dialog.findViewById(R.id.new_tea_button_picker_suggestion_2);
        buttonSuggestion2.performClick();

        final NumberPicker numberPickerTemperature = dialog.findViewById(R.id.new_tea_number_picker_dialog_temperature);
        assertThat(numberPickerTemperature.getValue()).isEqualTo(90);
    }

    @Test
    public void acceptInputAndExpectSavedTemperature() {
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final NumberPicker numberPickerTemperature = dialog.findViewById(R.id.new_tea_number_picker_dialog_temperature);
        numberPickerTemperature.setValue(80);

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        verify(newTeaViewModel).setInfusionTemperature(80);
    }
}
