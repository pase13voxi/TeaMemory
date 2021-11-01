package coolpharaoh.tee.speicher.tea.timer.views.show_tea;

import static android.os.Looper.getMainLooper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit.CELSIUS;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind.TEA_SPOON;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.views.description.ShowTeaDescription;
import coolpharaoh.tee.speicher.tea.timer.views.information.Information;
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.NewTea;
import coolpharaoh.tee.speicher.tea.timer.views.overview.Overview;
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer.TimerController;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class
ShowTeaTest {
    private static final String TEA_ID_EXTRA = "teaId";
    private static final long TEA_ID = 1L;
    private static final String VARIETY = "variety";
    private static final String BROADCAST_EXTRA_READY = "ready";
    private static final String BROADCAST_EXTRA_COUNTDOWN = "countdown";

    Tea tea;
    List<Infusion> infusions;
    Counter counter;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    TeaDao teaDao;
    @Mock
    InfusionDao infusionDao;
    @Mock
    CounterDao counterDao;
    @Mock
    ActualSettingsDao actualSettingsDao;

    @Test
    public void launchActivityWithNoTeaIdAndExpectFailingDialog() {
        final ActivityScenario<ShowTea> newTeaActivityScenario = ActivityScenario.launch(ShowTea.class);
        newTeaActivityScenario.onActivity(showTea -> {
            final AlertDialog dialogFail = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogFail, R.string.show_tea_dialog_tea_missing_header, R.string.show_tea_dialog_tea_missing_description);
            dialogFail.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            final Intent expected = new Intent(showTea, Overview.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void launchActivityWithNotExistingTeaIdExpectFailingDialog() {
        mockDB();
        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, 5L);

        final ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            final AlertDialog dialogFail = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogFail, R.string.show_tea_dialog_tea_missing_header, R.string.show_tea_dialog_tea_missing_description);
            dialogFail.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            final Intent expected = new Intent(showTea, Overview.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void launchActivityAndExpectDescriptionDialog() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON.getText(), 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList(null),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS.getText(), true, false);
        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            final AlertDialog dialogDescription = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogDescription, R.string.show_tea_dialog_description_header);

            final CheckBox checkBox = dialogDescription.findViewById(R.id.check_box_show_tea_dialog_description);
            checkBox.setChecked(true);

            dialogDescription.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            verify(actualSettingsDao).update(any(ActualSettings.class));

            final Intent expected = new Intent(showTea, ShowTeaDescription.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getData()).isEqualTo(expected.getData());
        });
    }

    @Test
    public void launchActivityAndExpectDisplayNextInfusionDialog() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON.getText(), 2);
        mockInfusions(
                Arrays.asList("1:00", "2:00", "3:00"), Arrays.asList(new String[]{null, null, null}),
                Arrays.asList(100, 100, 90), Arrays.asList(212, 212, 176));
        mockActualSettings(CELSIUS.getText(), false, false);
        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            final AlertDialog dialogNextInfusion = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogNextInfusion, R.string.show_tea_dialog_following_infusion_header, showTea.getString(R.string.show_tea_dialog_following_infusion_description, 2, 3));

            dialogNextInfusion.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            final TextView textViewInfusionIndex = showTea.findViewById(R.id.show_tea_tool_bar_text_infusion_index);
            final TextView textViewTemperature = showTea.findViewById(R.id.text_view_show_tea_temperature);
            final Spinner spinnerMinutes = showTea.findViewById(R.id.spinner_show_tea_minutes);
            final Spinner spinnerSeconds = showTea.findViewById(R.id.spinner_show_tea_seconds);

            assertThat(textViewInfusionIndex.getText()).hasToString("3.");
            assertThat(textViewTemperature.getText()).isEqualTo(infusions.get(2).getTemperatureCelsius() + " °C");
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("03");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");
        });
    }

    @Test
    public void displayNextInfusionDialogClickCancelAndExpectNextInfusionZero() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON.getText(), 2);
        mockInfusions(
                Arrays.asList("1:00", "2:00", "3:00"), Arrays.asList(new String[]{null, null, null}),
                Arrays.asList(100, 100, 90), Arrays.asList(212, 212, 176));
        mockActualSettings(CELSIUS.getText(), false, false);
        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            final AlertDialog dialogNextInfusion = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogNextInfusion, R.string.show_tea_dialog_following_infusion_header, showTea.getString(R.string.show_tea_dialog_following_infusion_description, 2, 3));

            dialogNextInfusion.getButton(DialogInterface.BUTTON_NEGATIVE).performClick();
            shadowOf(getMainLooper()).idle();

            final ArgumentCaptor<Tea> teaCaptor = ArgumentCaptor.forClass(Tea.class);
            verify(teaDao).update(teaCaptor.capture());

            assertThat(teaCaptor.getValue().getNextInfusion()).isZero();
        });
    }

    @Test
    public void launchActivityWithCelsiusAndTeaSpoonStandardValuesAndExpectFilledActivity() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON.getText(), 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList(null),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS.getText(), false, false);

        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            final ImageButton buttonInfusionIndex = showTea.findViewById(R.id.show_tea_tool_bar_infusion_index);
            final TextView textViewInfusionIndex = showTea.findViewById(R.id.show_tea_tool_bar_text_infusion_index);
            final ImageButton buttonNextInfusion = showTea.findViewById(R.id.show_tea_tool_bar_next_infusion);
            final TextView textViewName = showTea.findViewById(R.id.text_view_show_tea_name);
            final TextView textViewVariety = showTea.findViewById(R.id.text_view_show_tea_variety);
            final TextView textViewTemperature = showTea.findViewById(R.id.text_view_show_tea_temperature);
            final TextView textViewAmount = showTea.findViewById(R.id.text_view_show_tea_amount);
            final Spinner spinnerMinutes = showTea.findViewById(R.id.spinner_show_tea_minutes);
            final Spinner spinnerSeconds = showTea.findViewById(R.id.spinner_show_tea_seconds);

            assertThat(buttonInfusionIndex.getVisibility()).isEqualTo(View.GONE);
            assertThat(textViewInfusionIndex.getVisibility()).isEqualTo(View.GONE);
            assertThat(buttonNextInfusion.getVisibility()).isEqualTo(View.GONE);
            assertThat(textViewName.getText()).isEqualTo(tea.getName());
            assertThat(textViewVariety.getText()).isEqualTo(tea.getVariety());
            assertThat(textViewTemperature.getText()).isEqualTo(infusions.get(0).getTemperatureCelsius() + " °C");
            assertThat(textViewAmount.getText()).contains(((int) tea.getAmount()) + " ts/l");
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("01");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");
        });
    }

    @Test
    public void launchActivityWithEmptyValuesCelsiusAndTeaSpoonAndExpectFilledActivity() {
        mockDB();
        mockTea(null, -500, TEA_SPOON.getText(), 0);
        mockInfusions(Collections.singletonList(null), Collections.singletonList(null),
                Collections.singletonList(-500), Collections.singletonList(-500));
        mockActualSettings(CELSIUS.getText(), false, false);

        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            final TextView textViewVariety = showTea.findViewById(R.id.text_view_show_tea_variety);
            final TextView textViewTemperature = showTea.findViewById(R.id.text_view_show_tea_temperature);
            final TextView textViewAmount = showTea.findViewById(R.id.text_view_show_tea_amount);
            final Spinner spinnerMinutes = showTea.findViewById(R.id.spinner_show_tea_minutes);
            final Spinner spinnerSeconds = showTea.findViewById(R.id.spinner_show_tea_seconds);

            assertThat(textViewVariety.getText()).isEqualTo("-");
            assertThat(textViewTemperature.getText()).isEqualTo("- °C");
            assertThat(textViewAmount.getText()).contains("- ts/l");
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("00");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");
        });
    }

    @Test
    public void switchBetweenTimerAndCoolDownTimer() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON.getText(), 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList("4:00"),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS.getText(), false, false);

        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            final ImageButton buttonTemperature = showTea.findViewById(R.id.button_show_tea_temperature);
            final ImageButton buttonInfo = showTea.findViewById(R.id.button_show_tea_info);
            final Spinner spinnerMinutes = showTea.findViewById(R.id.spinner_show_tea_minutes);
            final Spinner spinnerSeconds = showTea.findViewById(R.id.spinner_show_tea_seconds);

            assertThat(buttonTemperature.isEnabled()).isTrue();
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("01");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");

            buttonTemperature.performClick();
            assertThat(buttonInfo.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("04");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");

            buttonInfo.performClick();
            final AlertDialog dialogInfo = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogInfo, R.string.show_tea_cool_down_time_header, R.string.show_tea_cool_down_time_description);
            dialogInfo.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            buttonTemperature.performClick();
            assertThat(buttonInfo.getVisibility()).isEqualTo(View.INVISIBLE);
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("01");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");
        });
    }

    @Test
    public void showDialogAmountAndCalculateAmountTeaSpoon() {
        mockDB();
        mockTea(VARIETY, 4, TEA_SPOON.getText(), 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList("4:00"),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS.getText(), false, false);

        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            final ImageButton buttonCalculateAmount = showTea.findViewById(R.id.button_show_tea_calculate_amount);

            buttonCalculateAmount.performClick();
            final AlertDialog dialogCalculateAmount = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogCalculateAmount, R.string.show_tea_dialog_amount);

            final SeekBar seekBarAmountPerAmount = dialogCalculateAmount.findViewById(R.id.seek_bar_show_tea_amount_per_amount);
            final TextView textViewAmountPerAmount = dialogCalculateAmount.findViewById(R.id.text_view_show_tea_show_amount_per_amount);

            assertThat(textViewAmountPerAmount.getText()).hasToString("4.0 ts / 1.0 l");

            seekBarAmountPerAmount.setProgress(5);

            assertThat(textViewAmountPerAmount.getText()).hasToString("2.0 ts / 0.5 l");
        });
    }

    @Test
    public void navigationToDetailedInformationView() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON.getText(), 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList(null),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS.getText(), false, false);

        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            final TextView toolbarTitle = showTea.findViewById(R.id.tool_bar_title);
            toolbarTitle.performClick();

            final Intent expected = new Intent(showTea, Information.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void navigationToDetailedInformationViewByMenu() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON.getText(), 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList(null),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS.getText(), false, false);

        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            showTea.onOptionsItemSelected(new RoboMenuItem(R.id.action_show_tea_information));

            final Intent expected = new Intent(showTea, Information.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void editTea() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON.getText(), 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList(null),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS.getText(), false, false);

        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            showTea.onOptionsItemSelected(new RoboMenuItem(R.id.action_show_tea_edit));

            final Intent expected = new Intent(showTea, NewTea.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void switchBetweenInfusionsCelsius() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON.getText(), 0);
        mockInfusions(
                Arrays.asList("1:00", "2:00", "3:00"), Arrays.asList(null, "5:00", null),
                Arrays.asList(100, -500, 100), Arrays.asList(212, -500, 212));
        mockActualSettings(CELSIUS.getText(), false, false);

        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            final ImageButton buttonInfusionIndex = showTea.findViewById(R.id.show_tea_tool_bar_infusion_index);
            final TextView textViewInfusionIndex = showTea.findViewById(R.id.show_tea_tool_bar_text_infusion_index);
            final ImageButton buttonNextInfusion = showTea.findViewById(R.id.show_tea_tool_bar_next_infusion);
            final Spinner spinnerMinutes = showTea.findViewById(R.id.spinner_show_tea_minutes);
            final Spinner spinnerSeconds = showTea.findViewById(R.id.spinner_show_tea_seconds);
            final TextView textViewTemperature = showTea.findViewById(R.id.text_view_show_tea_temperature);

            assertThat(buttonInfusionIndex.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(textViewInfusionIndex.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(buttonNextInfusion.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(textViewTemperature.getText()).hasToString("100 °C");
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("01");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");

            buttonNextInfusion.performClick();
            assertThat(textViewTemperature.getText()).hasToString("- °C");
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("02");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");

            buttonInfusionIndex.performClick();
            final AlertDialog dialogInfusionIndex = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(showTea, dialogInfusionIndex, R.string.show_tea_dialog_infusion_count_title);

            final ShadowAlertDialog shadowDialog = Shadows.shadowOf(dialogInfusionIndex);
            shadowDialog.clickOnItem(2);
            assertThat(textViewTemperature.getText()).hasToString("100 °C");
            assertThat(spinnerMinutes.getSelectedItem()).hasToString("03");
            assertThat(spinnerSeconds.getSelectedItem()).hasToString("00");
        });
    }

    @Test
    public void startTimer() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON.getText(), 0);
        mockInfusions(
                Arrays.asList("1:00", "2:00"), Arrays.asList("1:00", "1:00"),
                Arrays.asList(95, 95), Arrays.asList(203, 203));
        mockActualSettings(CELSIUS.getText(), false, true);
        mockCounter();

        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> checkStartOrReset(showTea, true));
    }

    @Test
    public void timerUpdate() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON.getText(), 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList(null),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS.getText(), false, true);
        mockCounter();

        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            final Button startButton = showTea.findViewById(R.id.button_show_tea_start_timer);
            final TextView textViewTimer = showTea.findViewById(R.id.text_view_show_tea_timer);
            final ImageView imageViewFill = showTea.findViewById(R.id.image_view_show_tea_fill);

            startButton.performClick();
            shadowOf(getMainLooper()).idle();

            final Intent broadcastIntent = new Intent(TimerController.COUNTDOWN_BR);
            broadcastIntent.putExtra(BROADCAST_EXTRA_COUNTDOWN, 30000L);
            broadcastIntent.putExtra(BROADCAST_EXTRA_READY, false);
            showTea.sendBroadcast(broadcastIntent);
            shadowOf(getMainLooper()).idle();

            assertThat(textViewTimer.getText()).hasToString("00 : 30");
            final int imageId = showTea.getResources().getIdentifier("cup_fill50pr", "drawable", showTea.getPackageName());
            assertThat(imageViewFill.getTag()).isEqualTo(imageId);
        });
    }

    @Test
    public void timerFinish() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON.getText(), 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList(null),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS.getText(), false, true);
        mockCounter();

        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            final Button startButton = showTea.findViewById(R.id.button_show_tea_start_timer);
            final TextView textViewTimer = showTea.findViewById(R.id.text_view_show_tea_timer);
            final ImageView imageViewSteam = showTea.findViewById(R.id.image_view_show_tea_steam);

            startButton.performClick();
            shadowOf(getMainLooper()).idle();

            final Intent broadcastIntent = new Intent(TimerController.COUNTDOWN_BR);
            broadcastIntent.putExtra(BROADCAST_EXTRA_READY, true);
            showTea.sendBroadcast(broadcastIntent);
            shadowOf(getMainLooper()).idle();

            assertThat(textViewTimer.getText()).hasToString(showTea.getString(R.string.show_tea_tea_ready));
            assertThat(imageViewSteam.getVisibility()).isEqualTo(View.VISIBLE);
        });
    }

    @Test
    public void startCoolDownTimerAndExpectNoAnimation() {
        mockDB();
        mockTea(VARIETY, 1, TEA_SPOON.getText(), 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList("1:00"),
                Collections.singletonList(95), Collections.singletonList(203));
        mockActualSettings(CELSIUS.getText(), false, true);

        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            final ImageButton buttonTemperature = showTea.findViewById(R.id.button_show_tea_temperature);
            final Button startButton = showTea.findViewById(R.id.button_show_tea_start_timer);
            final ImageView imageViewCup = showTea.findViewById(R.id.image_view_show_tea_cup);
            final ImageView imageViewFill = showTea.findViewById(R.id.image_view_show_tea_fill);
            final ImageView imageViewSteam = showTea.findViewById(R.id.image_view_show_tea_steam);

            buttonTemperature.performClick();
            startButton.performClick();

            final Intent broadcastUpdate = new Intent(TimerController.COUNTDOWN_BR);
            broadcastUpdate.putExtra(BROADCAST_EXTRA_COUNTDOWN, 30000L);
            broadcastUpdate.putExtra(BROADCAST_EXTRA_READY, false);
            showTea.sendBroadcast(broadcastUpdate);

            final Intent broadcastFinish = new Intent(TimerController.COUNTDOWN_BR);
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
        mockTea(VARIETY, 1, TEA_SPOON.getText(), 0);
        mockInfusions(Collections.singletonList("1:00"), Collections.singletonList(null),
                Collections.singletonList(100), Collections.singletonList(212));
        mockActualSettings(CELSIUS.getText(), false, false);
        mockCounter();

        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> {
            final Button startButton = showTea.findViewById(R.id.button_show_tea_start_timer);
            final ImageView imageViewCup = showTea.findViewById(R.id.image_view_show_tea_cup);
            final ImageView imageViewFill = showTea.findViewById(R.id.image_view_show_tea_fill);
            final ImageView imageViewSteam = showTea.findViewById(R.id.image_view_show_tea_steam);

            startButton.performClick();

            final Intent broadcastUpdate = new Intent(TimerController.COUNTDOWN_BR);
            broadcastUpdate.putExtra(BROADCAST_EXTRA_COUNTDOWN, 30000L);
            broadcastUpdate.putExtra(BROADCAST_EXTRA_READY, false);
            showTea.sendBroadcast(broadcastUpdate);

            final Intent broadcastFinish = new Intent(TimerController.COUNTDOWN_BR);
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
        mockTea(VARIETY, 1, TEA_SPOON.getText(), 0);
        mockInfusions(
                Arrays.asList("1:00", "2:00"), Arrays.asList("1:00", "1:00"),
                Arrays.asList(95, 95), Arrays.asList(203, 203));
        mockActualSettings(CELSIUS.getText(), false, true);
        mockCounter();

        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), ShowTea.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<ShowTea> showTeaActivityScenario = ActivityScenario.launch(intent);
        showTeaActivityScenario.onActivity(showTea -> checkStartOrReset(showTea, false));
    }

    private void checkStartOrReset(final ShowTea showTea, final boolean start) {
        final Button startButton = showTea.findViewById(R.id.button_show_tea_start_timer);
        final ImageButton buttonTemperature = showTea.findViewById(R.id.button_show_tea_temperature);
        final ImageButton buttonInfusionIndex = showTea.findViewById(R.id.show_tea_tool_bar_infusion_index);
        final ImageButton buttonNextInfusion = showTea.findViewById(R.id.show_tea_tool_bar_next_infusion);
        final Spinner spinnerMinutes = showTea.findViewById(R.id.spinner_show_tea_minutes);
        final Spinner spinnerSeconds = showTea.findViewById(R.id.spinner_show_tea_seconds);
        final TextView textViewMinutes = showTea.findViewById(R.id.text_view_show_tea_minutes);
        final TextView textViewSeconds = showTea.findViewById(R.id.text_view_show_tea_seconds);
        final TextView textViewDoublePoint = showTea.findViewById(R.id.text_view_show_tea_double_point);
        final TextView textViewTimer = showTea.findViewById(R.id.text_view_show_tea_timer);
        final ImageView imageViewCup = showTea.findViewById(R.id.image_view_show_tea_cup);
        final ImageView imageViewFill = showTea.findViewById(R.id.image_view_show_tea_fill);

        if (!start) {
            // start before reset
            startButton.performClick();
        }
        startButton.performClick();

        assertThat(startButton.getText())
                .hasToString(showTea.getString(start ? R.string.show_tea_timer_reset :
                        R.string.show_tea_timer_start));
        // disableInfusionBarAndCooldownSwitch
        assertThat(buttonTemperature.isEnabled()).isEqualTo(!start);
        assertThat(buttonInfusionIndex.isEnabled()).isEqualTo(!start);
        assertThat(buttonNextInfusion.isEnabled()).isEqualTo(!start);
        // hideTimeInputAndVisualizeTimerDisplay
        assertThat(spinnerMinutes.getVisibility()).isEqualTo(start ? View.INVISIBLE : View.VISIBLE);
        assertThat(spinnerSeconds.getVisibility()).isEqualTo(start ? View.INVISIBLE : View.VISIBLE);
        assertThat(textViewMinutes.getVisibility()).isEqualTo(start ? View.INVISIBLE : View.VISIBLE);
        assertThat(textViewSeconds.getVisibility()).isEqualTo(start ? View.INVISIBLE : View.VISIBLE);
        assertThat(textViewDoublePoint.getVisibility()).isEqualTo(start ? View.INVISIBLE : View.VISIBLE);
        assertThat(textViewTimer.getVisibility()).isEqualTo(start ? View.VISIBLE : View.INVISIBLE);
        // visualizeTeaCup
        assertThat(imageViewCup.getVisibility()).isEqualTo(start ? View.VISIBLE : View.INVISIBLE);
        assertThat(imageViewFill.getVisibility()).isEqualTo(start ? View.VISIBLE : View.INVISIBLE);
    }

    private void mockDB() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDao);
        when(teaMemoryDatabase.getInfusionDao()).thenReturn(infusionDao);
        when(teaMemoryDatabase.getCounterDao()).thenReturn(counterDao);
        when(teaMemoryDatabase.getActualSettingsDao()).thenReturn(actualSettingsDao);
    }

    private void mockTea(final String variety, final int amount, final String amountKind, final int nextInfusion) {
        tea = new Tea("name", variety, amount, amountKind, 1, nextInfusion, CurrentDate.getDate());
        tea.setId(TEA_ID);
        when(teaDao.getTeaById(TEA_ID)).thenReturn(tea);
    }

    private void mockInfusions(final List<String> time, final List<String> coolDownTime, final List<Integer> temperatureCelsius,
                               final List<Integer> temperatureFahrenheit) {
        infusions = new ArrayList<>();
        for (int i = 0; i < time.size(); i++) {
            final Infusion infusion = new Infusion(TEA_ID, i, time.get(i), coolDownTime.get(i), temperatureCelsius.get(i), temperatureFahrenheit.get(i));
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

    private void mockActualSettings(final String temperatureUnit, final boolean dialog, final boolean animation) {
        final ActualSettings actualSettings = new ActualSettings();
        actualSettings.setTemperatureUnit(temperatureUnit);
        actualSettings.setShowTeaAlert(dialog);
        actualSettings.setAnimation(animation);
        when(actualSettingsDao.getSettings()).thenReturn(actualSettings);
    }

    private void checkTitleAndMessageOfLatestDialog(final ShowTea showTea, final AlertDialog dialog, final int title) {
        checkTitleAndMessageOfLatestDialog(showTea, dialog, title, null);
    }

    private void checkTitleAndMessageOfLatestDialog(final ShowTea showTea, final AlertDialog dialog, final int title, final int message) {
        checkTitleAndMessageOfLatestDialog(showTea, dialog, title, showTea.getString(message));
    }

    private void checkTitleAndMessageOfLatestDialog(final ShowTea showTea, final AlertDialog dialog, final int title, final String message) {
        final ShadowAlertDialog shadowDialog = Shadows.shadowOf(dialog);
        assertThat(shadowDialog).isNotNull();
        assertThat(shadowDialog.getTitle()).isEqualTo(showTea.getString(title));
        if (message != null) {
            assertThat(shadowDialog.getMessage()).isEqualTo(message);
        }
    }

}
