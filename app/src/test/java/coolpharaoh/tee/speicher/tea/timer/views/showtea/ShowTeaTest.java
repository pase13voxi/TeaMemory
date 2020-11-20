package coolpharaoh.tee.speicher.tea.timer.views.showtea;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.views.main.Main;
import coolpharaoh.tee.speicher.tea.timer.views.newtea.NewTea;
import coolpharaoh.tee.speicher.tea.timer.views.showtea.countdowntimer.TimerController;
import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class ShowTeaTest {
    private static final String TEA_ID_EXTRA = "teaId";
    private static final long TEA_ID = 1L;
    private static final String VARIETY = "variety";
    private static final String CELSIUS = "Celsius";
    private static final String FAHRENHEIT = "Fahrenheit";
    private static final String TEA_SPOON = "Ts";
    private static final String GRAM = "Gr";
    private static final String INSERTED_NOTE = "Any note.";
    private static final String BROADCAST_EXTRA_READY = "ready";
    private static final String BROADCAST_EXTRA_COUNTDOWN = "countdown";

    Tea tea;
    List<Infusion> infusions;
    Counter counter;
    Note note;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    TeaDao teaDao;
    @Mock
    InfusionDao infusionDao;
    @Mock
    NoteDao noteDao;
    @Mock
    CounterDao counterDao;
    @Mock
    ActualSettingsDao actualSettingsDao;

    @Test
    public void launchActivityWithNoTeaIdAndExpectFailingDialog() {
        ActivityScenario<ShowTea> newTeaActivityScenario = ActivityScenario.launch(ShowTea.class);
        newTeaActivityScenario.onActivity(showTea -> {
            AlertDialog dialogFail = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogFail, R.string.showtea_dialog_tea_missing_header, R.string.showtea_dialog_tea_missing_description);
            dialogFail.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            Intent expected = new Intent(showTea, Main.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void launchActivityWithNotExistingTeaIdExpectFailingDialog() {
        mockDB();
        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, 5L);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            AlertDialog dialogFail = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogFail, R.string.showtea_dialog_tea_missing_header, R.string.showtea_dialog_tea_missing_description);
            dialogFail.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            Intent expected = new Intent(showTea, Main.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void launchActivityAndExpectDescriptionDialog() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON, 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList(null),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS, true, false);
        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            AlertDialog dialogDescription = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogDescription, R.string.showtea_dialog_description_header);

            CheckBox checkBox = dialogDescription.findViewById(R.id.checkboxDialogShowTeaDescription);
            checkBox.setChecked(true);

            dialogDescription.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            verify(actualSettingsDao).update(any(ActualSettings.class));
        });
    }

    @Test
    public void launchActivityAndExpectDisplayNextInfusionDialog() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON, 2);
        mockInfusions(
                Arrays.asList(new String[]{"1:00", "2:00", "3:00"}), Arrays.asList(new String[]{null, null, null}),
                Arrays.asList(new Integer[]{100, 100, 90}), Arrays.asList(new Integer[]{212, 212, 176}));
        mockActualSettings(CELSIUS, false, false);
        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            AlertDialog dialogNextInfusion = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogNextInfusion, R.string.showtea_dialog_following_infusion_header, showTea.getString(R.string.showtea_dialog_following_infusion_description, 2, 3));

            dialogNextInfusion.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            TextView textViewInfusionIndex = showTea.findViewById(R.id.toolbar_text_infusionindex);
            TextView textViewTemperature = showTea.findViewById(R.id.textViewTemperature);
            Spinner spinnerMinutes = showTea.findViewById(R.id.spinnerMinutes);
            Spinner spinnerSeconds = showTea.findViewById(R.id.spinnerSeconds);

            assertThat(textViewInfusionIndex.getText()).hasToString("3.");
            assertThat(textViewTemperature.getText()).isEqualTo(infusions.get(2).getTemperatureCelsius() + " °C");
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("03");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");
        });
    }

    @Test
    public void launchActivityWithCelsiusAndTeaSpoonStandardValuesAndExpectFilledActivity() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON, 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList(null),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS, false, false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button buttonInfusionIndex = showTea.findViewById(R.id.toolbar_infusionindex);
            TextView textViewInfusionIndex = showTea.findViewById(R.id.toolbar_text_infusionindex);
            Button buttonNextInfusion = showTea.findViewById(R.id.toolbar_nextinfusion);
            TextView textViewName = showTea.findViewById(R.id.textViewName);
            TextView textViewVariety = showTea.findViewById(R.id.textViewVariety);
            Button buttonNote = showTea.findViewById(R.id.buttonNote);
            Button buttonExchange = showTea.findViewById(R.id.buttonExchange);
            TextView textViewTemperature = showTea.findViewById(R.id.textViewTemperature);
            TextView textViewAmount = showTea.findViewById(R.id.textViewAmount);
            Spinner spinnerMinutes = showTea.findViewById(R.id.spinnerMinutes);
            Spinner spinnerSeconds = showTea.findViewById(R.id.spinnerSeconds);

            assertThat(buttonInfusionIndex.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(textViewInfusionIndex.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(buttonNextInfusion.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(textViewName.getText()).isEqualTo(tea.getName());
            assertThat(textViewVariety.getText()).isEqualTo(tea.getVariety());
            assertThat(buttonNote.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(buttonExchange.isEnabled()).isFalse();
            assertThat(textViewTemperature.getText()).isEqualTo(infusions.get(0).getTemperatureCelsius() + " °C");
            assertThat(textViewAmount.getText()).isEqualTo(tea.getAmount() + " ts/L");
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("01");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");
        });
    }

    @Test
    public void launchActivityWithFahrenheitAndGramValuesAndExpectFilledActivity() {
        mockDB();
        mockTea(VARIETY, 1, GRAM, 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList(null),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(FAHRENHEIT, false, false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            TextView textViewTemperature = showTea.findViewById(R.id.textViewTemperature);
            TextView textViewAmount = showTea.findViewById(R.id.textViewAmount);

            assertThat(textViewTemperature.getText()).isEqualTo(infusions.get(0).getTemperatureFahrenheit() + " °F");
            assertThat(textViewAmount.getText()).isEqualTo(tea.getAmount() + " g/L");
        });
    }

    @Test
    public void launchActivityWithEmptyValuesCelsiusAndTeaSpoonAndExpectFilledActivity() {
        mockDB();
        mockTea(null, -500, TEA_SPOON, 0);
        mockInfusions(Collections.singletonList(null), Collections.singletonList(null),
                Collections.singletonList(-500), Collections.singletonList(-500));
        mockActualSettings(CELSIUS, false, false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            TextView textViewVariety = showTea.findViewById(R.id.textViewVariety);
            TextView textViewTemperature = showTea.findViewById(R.id.textViewTemperature);
            TextView textViewAmount = showTea.findViewById(R.id.textViewAmount);
            Spinner spinnerMinutes = showTea.findViewById(R.id.spinnerMinutes);
            Spinner spinnerSeconds = showTea.findViewById(R.id.spinnerSeconds);

            assertThat(textViewVariety.getText()).isEmpty();
            assertThat(textViewTemperature.getText()).isEqualTo("- °C");
            assertThat(textViewAmount.getText()).isEqualTo("- ts/L");
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("00");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");
        });
    }

    @Test
    public void launchActivityWithEmptyValuesFahrenheitAndGramAndExpectFilledActivity() {
        mockDB();
        mockTea(null, -500, GRAM, 0);
        mockInfusions(Collections.singletonList(null), Collections.singletonList(null),
                Collections.singletonList(-500), Collections.singletonList(-500));
        mockActualSettings(FAHRENHEIT, false, false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            TextView textViewTemperature = showTea.findViewById(R.id.textViewTemperature);
            TextView textViewAmount = showTea.findViewById(R.id.textViewAmount);

            assertThat(textViewTemperature.getText()).isEqualTo("- °F");
            assertThat(textViewAmount.getText()).isEqualTo("- g/L");
        });
    }

    @Test
    public void switchBetweenTimerAndCoolDownTimer() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON, 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList("4:00"),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS, false, false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button buttonExchange = showTea.findViewById(R.id.buttonExchange);
            Button buttonInfo = showTea.findViewById(R.id.buttonInfo);
            Spinner spinnerMinutes = showTea.findViewById(R.id.spinnerMinutes);
            Spinner spinnerSeconds = showTea.findViewById(R.id.spinnerSeconds);

            assertThat(buttonExchange.isEnabled()).isTrue();
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("01");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");

            buttonExchange.performClick();
            assertThat(buttonInfo.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("04");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");

            buttonInfo.performClick();
            AlertDialog dialogInfo = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogInfo, R.string.showtea_cooldown_header, R.string.showtea_cooldown_description);
            dialogInfo.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            buttonExchange.performClick();
            assertThat(buttonInfo.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("01");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");
        });
    }

    @Test
    public void showDialogAmountAndCalcuateAmountTeaSpoon() {
        mockDB();
        mockTea(VARIETY, 4, TEA_SPOON, 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList("4:00"),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS, false, false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button buttonCalculateAmount = showTea.findViewById(R.id.buttonCalculateAmount);

            buttonCalculateAmount.performClick();
            AlertDialog dialogCalculateAmount = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogCalculateAmount, R.string.showtea_dialog_amount);

            final SeekBar seekBarAmountPerAmount = dialogCalculateAmount.findViewById(R.id.seekBarAmountPerAmount);
            final TextView textViewAmountPerAmount = dialogCalculateAmount.findViewById(R.id.textViewShowAmountPerAmount);

            assertThat(textViewAmountPerAmount.getText()).hasToString("4.0 ts / 1.0 L");

            seekBarAmountPerAmount.setProgress(5);

            assertThat(textViewAmountPerAmount.getText()).hasToString("2.0 ts / 0.5 L");
        });
    }

    @Test
    public void showDialogAmountAndCalcuateAmountGram() {
        mockDB();
        mockTea(VARIETY, 9, GRAM, 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList("4:00"),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS, false, false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button buttonCalculateAmount = showTea.findViewById(R.id.buttonCalculateAmount);

            buttonCalculateAmount.performClick();
            AlertDialog dialogCalculateAmount = getLatestAlertDialog();

            final SeekBar seekBarAmountPerAmount = dialogCalculateAmount.findViewById(R.id.seekBarAmountPerAmount);
            final TextView textViewAmountPerAmount = dialogCalculateAmount.findViewById(R.id.textViewShowAmountPerAmount);

            assertThat(textViewAmountPerAmount.getText()).hasToString("9.0 g / 1.0 L");

            seekBarAmountPerAmount.setProgress(15);

            assertThat(textViewAmountPerAmount.getText()).hasToString("13.5 g / 1.5 L");
        });
    }

    @Test
    public void openAndChangeNotesViaButton() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON, 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList(null),
                Collections.singletonList(100), Collections.singletonList(212));
        mockNote(INSERTED_NOTE);
        mockActualSettings(CELSIUS, false, false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button buttonNote = showTea.findViewById(R.id.buttonNote);

            assertThat(buttonNote.getVisibility()).isEqualTo(View.VISIBLE);

            buttonNote.performClick();
            AlertDialog dialogNote = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogNote, R.string.showtea_action_note);

            EditText editTextNote = dialogNote.findViewById(R.id.editTextNote);
            assertThat(editTextNote.getText()).hasToString(INSERTED_NOTE);
            editTextNote.setText("");
            dialogNote.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            assertThat(buttonNote.getVisibility()).isEqualTo(View.INVISIBLE);
        });
    }

    @Test
    public void openAndChangeNotesViaMenu() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON, 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList(null),
                Collections.singletonList(100), Collections.singletonList(212));
        mockNote("");
        mockActualSettings(CELSIUS, false, false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button buttonNote = showTea.findViewById(R.id.buttonNote);

            assertThat(buttonNote.getVisibility()).isEqualTo(View.INVISIBLE);

            showTea.onOptionsItemSelected(new RoboMenuItem(R.id.action_note));
            AlertDialog dialogNote = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogNote, R.string.showtea_action_note);

            EditText editTextNote = dialogNote.findViewById(R.id.editTextNote);
            assertThat(editTextNote.getText()).hasToString("");
            editTextNote.setText(INSERTED_NOTE);
            dialogNote.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            assertThat(buttonNote.getVisibility()).isEqualTo(View.VISIBLE);
        });
    }

    @Test
    public void openTeaCounter() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON, 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList(null),
                Collections.singletonList(100), Collections.singletonList(212));
        mockCounter();
        mockActualSettings(CELSIUS, false, false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            showTea.onOptionsItemSelected(new RoboMenuItem(R.id.action_counter));

            checkTitleAndMessageOfLatestDialog(showTea, getLatestAlertDialog(), R.string.showtea_action_counter);

            // instead of a listview use a normal layout to make it more testable
        });
    }

    @Test
    public void editTea() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON, 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList(null),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS, false, false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            showTea.onOptionsItemSelected(new RoboMenuItem(R.id.action_edit));

            Intent expected = new Intent(showTea, NewTea.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void switchBetweenInfusionsCelsius() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON, 0);
        mockInfusions(
                Arrays.asList(new String[]{"1:00", "2:00", "3:00"}), Arrays.asList(new String[]{null, "5:00", null}),
                Arrays.asList(new Integer[]{100, -500, 100}), Arrays.asList(new Integer[]{212, -500, 212}));
        mockActualSettings(CELSIUS, false, false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button buttonInfusionIndex = showTea.findViewById(R.id.toolbar_infusionindex);
            TextView textViewInfusionIndex = showTea.findViewById(R.id.toolbar_text_infusionindex);
            Button buttonNextInfusion = showTea.findViewById(R.id.toolbar_nextinfusion);
            Spinner spinnerMinutes = showTea.findViewById(R.id.spinnerMinutes);
            Spinner spinnerSeconds = showTea.findViewById(R.id.spinnerSeconds);
            Button buttonExchange = showTea.findViewById(R.id.buttonExchange);
            TextView textViewTemperature = showTea.findViewById(R.id.textViewTemperature);

            assertThat(buttonInfusionIndex.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(textViewInfusionIndex.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(buttonNextInfusion.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(buttonExchange.isEnabled()).isFalse();
            assertThat(textViewTemperature.getText()).hasToString("100 °C");
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("01");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");

            buttonNextInfusion.performClick();
            assertThat(buttonExchange.isEnabled()).isTrue();
            assertThat(textViewTemperature.getText()).hasToString("- °C");
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("02");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");

            buttonInfusionIndex.performClick();
            AlertDialog dialogInfusionIndex = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogInfusionIndex, R.string.showtea_dialog_infusion_count_title);

            ShadowAlertDialog shadowDialog = Shadows.shadowOf(dialogInfusionIndex);
            shadowDialog.clickOnItem(2);
            assertThat(buttonExchange.isEnabled()).isFalse();
            assertThat(textViewTemperature.getText()).hasToString("100 °C");
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("03");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");
        });
    }

    @Test
    public void switchBetweenInfusionsFahrenheit() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON, 0);
        mockInfusions(
                Arrays.asList(new String[]{"1:00", "2:00"}), Arrays.asList(new String[]{null, "5:00"}),
                Arrays.asList(new Integer[]{100, -500}), Arrays.asList(new Integer[]{212, -500}));
        mockActualSettings(FAHRENHEIT, false, false);

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button buttonNextInfusion = showTea.findViewById(R.id.toolbar_nextinfusion);
            Button buttonExchange = showTea.findViewById(R.id.buttonExchange);
            TextView textViewTemperature = showTea.findViewById(R.id.textViewTemperature);

            assertThat(textViewTemperature.getText()).hasToString("212 °F");

            buttonNextInfusion.performClick();
            assertThat(buttonExchange.isEnabled()).isTrue();
            assertThat(textViewTemperature.getText()).hasToString("- °F");
        });
    }

    @Test
    public void startTimer() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON, 0);
        mockInfusions(
                Arrays.asList(new String[]{"1:00", "2:00"}), Arrays.asList(new String[]{"1:00", "1:00"}),
                Arrays.asList(new Integer[]{95, 95}), Arrays.asList(new Integer[]{203, 203}));
        mockActualSettings(CELSIUS, false, true);
        mockCounter();

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button startButton = showTea.findViewById(R.id.buttonStartTimer);
            Button buttonExchange = showTea.findViewById(R.id.buttonExchange);
            Button buttonInfusionIndex = showTea.findViewById(R.id.toolbar_infusionindex);
            Button buttonNextInfusion = showTea.findViewById(R.id.toolbar_nextinfusion);
            Spinner spinnerMinutes = showTea.findViewById(R.id.spinnerMinutes);
            Spinner spinnerSeconds = showTea.findViewById(R.id.spinnerSeconds);
            TextView textViewMinutes = showTea.findViewById(R.id.textViewMinutes);
            TextView textViewSeconds = showTea.findViewById(R.id.textViewSeconds);
            TextView textViewDoublePoint = showTea.findViewById(R.id.textViewDoublePoint);
            TextView textViewTimer = showTea.findViewById(R.id.textViewTimer);
            ImageView imageViewCup = showTea.findViewById(R.id.imageViewCup);
            ImageView imageViewFill = showTea.findViewById(R.id.imageViewFill);


            startButton.performClick();

            assertThat(startButton.getText()).hasToString(showTea.getString(R.string.showtea_timer_reset));
            // collectDrinkingBehaviorInformation
            verify(counterDao).update(any(Counter.class));
            verify(teaDao, times(2)).update(any(Tea.class));
            // disableInfusionBarAndCooldownSwitch
            assertThat(buttonExchange.isEnabled()).isFalse();
            assertThat(buttonInfusionIndex.isEnabled()).isFalse();
            assertThat(buttonNextInfusion.isEnabled()).isFalse();
            // hideTimeInputAndVisualizeTimerDisplay
            assertThat(spinnerMinutes.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(spinnerSeconds.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(textViewMinutes.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(textViewSeconds.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(textViewDoublePoint.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(textViewTimer.getVisibility()).isEqualTo(View.VISIBLE);
            // visualizeTeaCup
            assertThat(imageViewCup.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(imageViewFill.getVisibility()).isEqualTo(View.VISIBLE);
        });
    }

    @Test
    public void timerUpdate() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON, 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList(null),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS, false, true);
        mockCounter();

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button startButton = showTea.findViewById(R.id.buttonStartTimer);
            TextView textViewTimer = showTea.findViewById(R.id.textViewTimer);
            ImageView imageViewFill = showTea.findViewById(R.id.imageViewFill);

            startButton.performClick();

            Intent broadcastIntent = new Intent(TimerController.COUNTDOWN_BR);
            broadcastIntent.putExtra(BROADCAST_EXTRA_COUNTDOWN, 30000L);
            broadcastIntent.putExtra(BROADCAST_EXTRA_READY, false);
            showTea.sendBroadcast(broadcastIntent);

            assertThat(textViewTimer.getText()).hasToString("00 : 30");
            int imageId = showTea.getResources().getIdentifier("fill50pr", "drawable", showTea.getPackageName());
            assertThat(imageViewFill.getTag()).isEqualTo(imageId);
        });
    }

    @Test
    public void timerFinish() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON, 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList(null),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS, false, true);
        mockCounter();

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button startButton = showTea.findViewById(R.id.buttonStartTimer);
            TextView textViewTimer = showTea.findViewById(R.id.textViewTimer);
            ImageView imageViewSteam = showTea.findViewById(R.id.imageViewSteam);

            startButton.performClick();

            Intent broadcastIntent = new Intent(TimerController.COUNTDOWN_BR);
            broadcastIntent.putExtra(BROADCAST_EXTRA_READY, true);
            showTea.sendBroadcast(broadcastIntent);

            assertThat(textViewTimer.getText()).hasToString(showTea.getString(R.string.showtea_tea_ready));
            assertThat(imageViewSteam.getVisibility()).isEqualTo(View.VISIBLE);
        });
    }

    @Test
    public void startCoolDownTimerAndExpectNoAnimation() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON, 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList("1:00"),
                Collections.singletonList(95), Collections.singletonList(203));
        mockActualSettings(CELSIUS, false, true);
        mockCounter();

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button buttonExchange = showTea.findViewById(R.id.buttonExchange);
            Button startButton = showTea.findViewById(R.id.buttonStartTimer);
            ImageView imageViewCup = showTea.findViewById(R.id.imageViewCup);
            ImageView imageViewFill = showTea.findViewById(R.id.imageViewFill);
            ImageView imageViewSteam = showTea.findViewById(R.id.imageViewSteam);

            buttonExchange.performClick();
            startButton.performClick();

            Intent broadcastUpdate = new Intent(TimerController.COUNTDOWN_BR);
            broadcastUpdate.putExtra(BROADCAST_EXTRA_COUNTDOWN, 30000L);
            broadcastUpdate.putExtra(BROADCAST_EXTRA_READY, false);
            showTea.sendBroadcast(broadcastUpdate);

            Intent broadcastFinish = new Intent(TimerController.COUNTDOWN_BR);
            broadcastFinish.putExtra(BROADCAST_EXTRA_READY, true);
            showTea.sendBroadcast(broadcastFinish);

            verify(counterDao, times(0)).update(any(Counter.class));
            assertThat(imageViewCup.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(imageViewFill.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(imageViewSteam.getVisibility()).isEqualTo(View.INVISIBLE);
        });
    }

    @Test
    public void startTimerWithSettingAnimationFalseAndExpectNoAnimation() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON, 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList(null),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS, false, false);
        mockCounter();

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button startButton = showTea.findViewById(R.id.buttonStartTimer);
            ImageView imageViewCup = showTea.findViewById(R.id.imageViewCup);
            ImageView imageViewFill = showTea.findViewById(R.id.imageViewFill);
            ImageView imageViewSteam = showTea.findViewById(R.id.imageViewSteam);

            startButton.performClick();

            Intent broadcastUpdate = new Intent(TimerController.COUNTDOWN_BR);
            broadcastUpdate.putExtra(BROADCAST_EXTRA_COUNTDOWN, 30000L);
            broadcastUpdate.putExtra(BROADCAST_EXTRA_READY, false);
            showTea.sendBroadcast(broadcastUpdate);

            Intent broadcastFinish = new Intent(TimerController.COUNTDOWN_BR);
            broadcastFinish.putExtra(BROADCAST_EXTRA_READY, true);
            showTea.sendBroadcast(broadcastFinish);

            assertThat(imageViewCup.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(imageViewFill.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(imageViewSteam.getVisibility()).isEqualTo(View.INVISIBLE);
        });
    }

    @Test
    public void resetTimer() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON, 0);
        mockInfusions(
                Arrays.asList(new String[]{"1:00", "2:00"}), Arrays.asList(new String[]{"1:00", "1:00"}),
                Arrays.asList(new Integer[]{95, 95}), Arrays.asList(new Integer[]{203, 203}));
        mockActualSettings(CELSIUS, false, true);
        mockCounter();

        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            Button startButton = showTea.findViewById(R.id.buttonStartTimer);
            Button buttonExchange = showTea.findViewById(R.id.buttonExchange);
            Button buttonInfusionIndex = showTea.findViewById(R.id.toolbar_infusionindex);
            Button buttonNextInfusion = showTea.findViewById(R.id.toolbar_nextinfusion);
            Spinner spinnerMinutes = showTea.findViewById(R.id.spinnerMinutes);
            Spinner spinnerSeconds = showTea.findViewById(R.id.spinnerSeconds);
            TextView textViewMinutes = showTea.findViewById(R.id.textViewMinutes);
            TextView textViewSeconds = showTea.findViewById(R.id.textViewSeconds);
            TextView textViewDoublePoint = showTea.findViewById(R.id.textViewDoublePoint);
            TextView textViewTimer = showTea.findViewById(R.id.textViewTimer);
            ImageView imageViewCup = showTea.findViewById(R.id.imageViewCup);
            ImageView imageViewFill = showTea.findViewById(R.id.imageViewFill);


            startButton.performClick();
            // reset
            startButton.performClick();

            assertThat(startButton.getText()).hasToString(showTea.getString(R.string.showtea_timer_start));
            // disableInfusionBarAndCooldownSwitch
            assertThat(buttonExchange.isEnabled()).isTrue();
            assertThat(buttonInfusionIndex.isEnabled()).isTrue();
            assertThat(buttonNextInfusion.isEnabled()).isTrue();
            // hideTimeInputAndVisualizeTimerDisplay
            assertThat(spinnerMinutes.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(spinnerSeconds.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(textViewMinutes.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(textViewSeconds.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(textViewDoublePoint.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(textViewTimer.getVisibility()).isEqualTo(View.INVISIBLE);
            // visualizeTeaCup
            assertThat(imageViewCup.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(imageViewFill.getVisibility()).isEqualTo(View.INVISIBLE);
        });
    }

    private void mockDB() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDao);
        when(teaMemoryDatabase.getInfusionDao()).thenReturn(infusionDao);
        when(teaMemoryDatabase.getNoteDao()).thenReturn(noteDao);
        when(teaMemoryDatabase.getCounterDao()).thenReturn(counterDao);
        when(teaMemoryDatabase.getActualSettingsDao()).thenReturn(actualSettingsDao);
    }

    private void mockTea(String variety, int amount, String amountKind, int nextInfusion) {
        tea = new Tea("name", variety, amount, amountKind, 1, nextInfusion, CurrentDate.getDate());
        tea.setId(TEA_ID);
        when(teaDao.getTeaById(TEA_ID)).thenReturn(tea);
    }

    private void mockInfusions(List<String> time, List<String> coolDownTime, List<Integer> temperatureCelsius, List<Integer> temperatureFahrenheit) {
        infusions = new ArrayList<>();
        for (int i = 0; i < time.size(); i++) {
            Infusion infusion = new Infusion(TEA_ID, i, time.get(i), coolDownTime.get(i), temperatureCelsius.get(i), temperatureFahrenheit.get(i));
            infusion.setId((long) (i + 1));
            infusions.add(infusion);
        }
        when(infusionDao.getInfusionsByTeaId(TEA_ID)).thenReturn(infusions);
    }

    private void mockCounter() {
        counter = new Counter(TEA_ID, 1, 2, 3, 4, CurrentDate.getDate(), CurrentDate.getDate(), CurrentDate.getDate());
        counter.setId(1L);
        when(counterDao.getCounterByTeaId(TEA_ID)).thenReturn(counter);
    }

    private void mockNote(String description) {
        note = new Note(TEA_ID, 1, "header", description);
        note.setId(1L);
        when(noteDao.getNoteByTeaId(TEA_ID)).thenReturn(note);
    }

    private void mockActualSettings(String temperatureUnit, boolean dialog, boolean animation) {
        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setTemperatureUnit(temperatureUnit);
        actualSettings.setShowTeaAlert(dialog);
        actualSettings.setAnimation(animation);
        when(actualSettingsDao.getSettings()).thenReturn(actualSettings);
    }

    private void checkTitleAndMessageOfLatestDialog(ShowTea showTea, AlertDialog dialog, int title) {
        checkTitleAndMessageOfLatestDialog(showTea, dialog, title, null);
    }

    private void checkTitleAndMessageOfLatestDialog(ShowTea showTea, AlertDialog dialog, int title, int message) {
        checkTitleAndMessageOfLatestDialog(showTea, dialog, title, showTea.getString(message));
    }

    private void checkTitleAndMessageOfLatestDialog(ShowTea showTea, AlertDialog dialog, int title, String message) {
        ShadowAlertDialog shadowDialog = Shadows.shadowOf(dialog);
        assertThat(shadowDialog).isNotNull();
        assertThat(shadowDialog.getTitle()).isEqualTo(showTea.getString(title));
        if (message != null) {
            assertThat(shadowDialog.getMessage()).isEqualTo(message);
        }
    }

}
