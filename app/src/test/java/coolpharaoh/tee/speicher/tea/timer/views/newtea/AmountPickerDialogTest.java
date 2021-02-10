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

import static coolpharaoh.tee.speicher.tea.timer.views.newtea.AmountPickerDialog.TAG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class AmountPickerDialogTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    NewTeaViewModel newTeaViewModel;
    @Mock
    Suggestions suggestions;

    private AmountPickerDialog dialogFragment;
    private FragmentManager fragmentManager;

    @Before
    public void setUp() {
        final FragmentActivity activity = Robolectric.buildActivity(FragmentActivity.class).create().start().resume().get();
        fragmentManager = activity.getSupportFragmentManager();
        dialogFragment = new AmountPickerDialog(suggestions, newTeaViewModel);
    }

    @Test
    public void showDialogAndExpectTitle() {
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();
        final ShadowAlertDialog shadowDialog = Shadows.shadowOf(dialog);
        assertThat(shadowDialog.getTitle()).isEqualTo(dialogFragment.getString(R.string.newtea_dialog_amount_header));
    }

    @Test
    public void showDialogAndExpectTwoTsSuggestions() {
        when(suggestions.getAmountTsSuggestions()).thenReturn(new int[]{4, 5});
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final Button buttonSuggestion1 = dialog.findViewById(R.id.button_picker_suggestion_1);
        assertThat(buttonSuggestion1)
                .extracting(View::getVisibility, tv -> tv.getText().toString())
                .containsExactly(View.VISIBLE, "4");

        final Button buttonSuggestion2 = dialog.findViewById(R.id.button_picker_suggestion_2);
        assertThat(buttonSuggestion2)
                .extracting(View::getVisibility, tv -> tv.getText().toString())
                .containsExactly(View.VISIBLE, "5");

        final Button buttonSuggestion3 = dialog.findViewById(R.id.button_picker_suggestion_3);
        assertThat(buttonSuggestion3.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void showDialogAndExpectNoSuggestions() {
        when(suggestions.getAmountTsSuggestions()).thenReturn(new int[]{});
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();
        final LinearLayout layoutSuggestions = dialog.findViewById(R.id.layout_picker_suggestions);
        assertThat(layoutSuggestions.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void setAmountKindGrAndExpectGrSuggestions() {
        when(suggestions.getAmountTsSuggestions()).thenReturn(null);
        when(suggestions.getAmountGrSuggestions()).thenReturn(new int[]{10, 11, 12});
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final NumberPicker amountKindPicker = dialog.findViewById(R.id.number_picker_dialog_amount_kind);
        amountKindPicker.setValue(1);
        Shadows.shadowOf(amountKindPicker).getOnValueChangeListener().onValueChange(amountKindPicker, 0, 1);

        final LinearLayout layoutSuggestions = dialog.findViewById(R.id.layout_picker_suggestions);
        assertThat(layoutSuggestions.getVisibility()).isEqualTo(View.VISIBLE);

        final Button buttonSuggestion1 = dialog.findViewById(R.id.button_picker_suggestion_1);
        assertThat(buttonSuggestion1)
                .extracting(View::getVisibility, btn -> btn.getText().toString())
                .containsExactly(View.VISIBLE, "10");

        final Button buttonSuggestion2 = dialog.findViewById(R.id.button_picker_suggestion_2);
        assertThat(buttonSuggestion2)
                .extracting(View::getVisibility, btn -> btn.getText().toString())
                .containsExactly(View.VISIBLE, "11");

        final Button buttonSuggestion3 = dialog.findViewById(R.id.button_picker_suggestion_3);
        assertThat(buttonSuggestion3)
                .extracting(View::getVisibility, btn -> btn.getText().toString())
                .containsExactly(View.VISIBLE, "12");
    }

    @Test
    public void clickSuggestionAndExpectShownSuggestion() {
        when(suggestions.getAmountTsSuggestions()).thenReturn(new int[]{4, 5});
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final Button buttonSuggestion1 = dialog.findViewById(R.id.button_picker_suggestion_1);
        buttonSuggestion1.performClick();

        final NumberPicker amountPicker = dialog.findViewById(R.id.number_picker_dialog_amount);
        assertThat(amountPicker.getValue()).isEqualTo(4);
    }

    @Test
    public void acceptTsInputExceptSavedInput() {
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final NumberPicker amountPicker = dialog.findViewById(R.id.number_picker_dialog_amount);
        amountPicker.setValue(7);

        final NumberPicker amountKindPicker = dialog.findViewById(R.id.number_picker_dialog_amount_kind);
        amountKindPicker.setValue(0);

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

        verify(newTeaViewModel).setAmount(7, "Ts");
    }

    @Test
    public void acceptGrInputExceptSavedInput() {
        dialogFragment.show(fragmentManager, TAG);

        final AlertDialog dialog = getLatestAlertDialog();

        final NumberPicker amountPicker = dialog.findViewById(R.id.number_picker_dialog_amount);
        amountPicker.setValue(7);

        final NumberPicker amountKindPicker = dialog.findViewById(R.id.number_picker_dialog_amount_kind);
        amountKindPicker.setValue(1);

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

        verify(newTeaViewModel).setAmount(7, "Gr");
    }
}
