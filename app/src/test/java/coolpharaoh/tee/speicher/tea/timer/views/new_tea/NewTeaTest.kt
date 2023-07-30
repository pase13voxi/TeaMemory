package coolpharaoh.tee.speicher.tea.timer.views.new_tea;

import static android.os.Looper.getMainLooper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit.FAHRENHEIT;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind.GRAM;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind.TEA_SPOON;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.BLACK_TEA;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.GREEN_TEA;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowAlertDialog;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea;

@RunWith(RobolectricTestRunner.class)
public class NewTeaTest {

    private static final String CURRENT_DATE = "2020-08-19T10:15:30Z";
    private static final String FIRST_INFUSION = "1. Infusion";
    private static final String SECOND_INFUSION = "2. Infusion";
    private static final String TEA_ID = "teaId";
    private static final String SHOW_TEA_FLAG = "showTea";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    TeaDao teaDao;
    @Mock
    InfusionDao infusionDao;

    @Before
    public void setUp() {
        mockDB();
    }

    private void mockDB() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDao);
        when(teaMemoryDatabase.getInfusionDao()).thenReturn(infusionDao);
    }

    @Test
    public void showActivityAddModeAndExpectFilledDefaultValues() {
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextVariety = newTea.findViewById(R.id.edit_text_new_tea_variety);
            final EditText editTextName = newTea.findViewById(R.id.edit_text_new_tea_name);
            final EditText editTextAmount = newTea.findViewById(R.id.edit_text_new_tea_amount);
            final EditText editTextTemperature = newTea.findViewById(R.id.edit_text_new_tea_temperature);
            final EditText editTextCoolDownTime = newTea.findViewById(R.id.edit_text_new_tea_cool_down_time);
            final EditText editTextTime = newTea.findViewById(R.id.edit_text_new_tea_time);

            final String[] varieties = newTea.getResources().getStringArray(R.array.new_tea_variety_teas);
            assertThat(editTextVariety.getText()).hasToString(varieties[0]);
            assertThat(editTextName.getText().toString()).isBlank();
            assertThat(editTextAmount.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_amount_empty_text_ts));
            assertThat(editTextTemperature.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_temperature_text_celsius, "-"));
            assertThat(editTextCoolDownTime.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_cool_down_time_empty_text));
            assertThat(editTextTime.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_time_empty_text));
        });
    }

    @Test
    public void addNewTeaAndExpectSavedDefaultValues() {
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextName = newTea.findViewById(R.id.edit_text_new_tea_name);
            editTextName.setText("Name");
            newTea.onOptionsItemSelected(new RoboMenuItem(R.id.action_new_tea_done));
            final ArgumentCaptor<Tea> captorTea = ArgumentCaptor.forClass(Tea.class);
            verify(teaDao).insert(captorTea.capture());
            final Tea tea = captorTea.getValue();
            assertThat(tea).extracting(
                    Tea::getName,
                    Tea::getVariety,
                    Tea::getColor,
                    Tea::getAmount,
                    Tea::getAmountKind,
                    Tea::getRating,
                    Tea::getInStock
            ).containsExactly(
                    "Name", BLACK_TEA.getCode(), -15461296, -500.0, TEA_SPOON.getText(), 0, true
            );

            final ArgumentCaptor<Infusion> captorInfusion = ArgumentCaptor.forClass(Infusion.class);
            verify(infusionDao).insert(captorInfusion.capture());
            final Infusion infusion = captorInfusion.getValue();
            assertThat(infusion).extracting(
                    Infusion::getTemperatureCelsius,
                    Infusion::getTemperatureFahrenheit,
                    Infusion::getCoolDownTime,
                    Infusion::getTime
            ).containsExactly(
                    -500, -500, null, null
            );
        });
    }

    @Test
    public void changeVarietyAndExpectUpdatedVariety() {
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextVariety = newTea.findViewById(R.id.edit_text_new_tea_variety);
            editTextVariety.performClick();
            shadowOf(getMainLooper()).idle();

            final AlertDialog dialog = getLatestAlertDialog();

            final List<RadioButton> radioButtons = getRadioButtons(dialog);

            radioButtons.get(3).performClick();

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            final String[] varieties = newTea.getResources().getStringArray(R.array.new_tea_variety_teas);
            assertThat(editTextVariety.getText()).hasToString(varieties[3]);
        });
    }

    @Test
    public void changeColorAndExpectUpdatedColor() {
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final Button buttonColor = newTea.findViewById(R.id.button_new_tea_color);
            buttonColor.performClick();
            shadowOf(getMainLooper()).idle();

            assertThat(ShadowAlertDialog.getLatestDialog()).isNotNull();
        });
    }

    @Test
    public void changeAmountAndExpectUpdatedAmount() {
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextAmount = newTea.findViewById(R.id.edit_text_new_tea_amount);
            editTextAmount.performClick();
            shadowOf(getMainLooper()).idle();

            final AlertDialog dialog = getLatestAlertDialog();

            final NumberPicker amountPicker = dialog.findViewById(R.id.number_picker_new_tea_dialog_amount);
            amountPicker.setValue(7);

            final NumberPicker amountKindPicker = dialog.findViewById(R.id.number_picker_new_tea_dialog_amount_kind);
            amountKindPicker.setValue(0);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            assertThat(editTextAmount.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_amount_text_ts, 7));
        });
    }

    @Test
    public void changeTemperatureAndExpectUpdatedTemperature() {
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextTemperature = newTea.findViewById(R.id.edit_text_new_tea_temperature);
            editTextTemperature.performClick();
            shadowOf(getMainLooper()).idle();

            final AlertDialog dialog = getLatestAlertDialog();

            final NumberPicker numberPickerTemperature = dialog.findViewById(R.id.number_picker_new_tea_dialog_temperature);
            numberPickerTemperature.setValue(80);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            assertThat(editTextTemperature.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_temperature_text_celsius, 80));
        });
    }

    @Test
    public void showCoolDownTimeWhenTemperatureIsLessThan100Celsius() {
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextTemperature = newTea.findViewById(R.id.edit_text_new_tea_temperature);
            editTextTemperature.performClick();
            shadowOf(getMainLooper()).idle();

            final AlertDialog dialog = getLatestAlertDialog();

            final NumberPicker numberPickerTemperature = dialog.findViewById(R.id.number_picker_new_tea_dialog_temperature);
            numberPickerTemperature.setValue(80);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            final EditText editTextCoolDownTime = newTea.findViewById(R.id.edit_text_new_tea_cool_down_time);

            assertThat(editTextCoolDownTime.getVisibility()).isEqualTo(View.VISIBLE);
        });
    }

    @Test
    public void hideCoolDownTimeWhenTemperatureIs100Celsius() {
        mockTemperatureUnitFahrenheit();
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextTemperature = newTea.findViewById(R.id.edit_text_new_tea_temperature);
            editTextTemperature.performClick();
            shadowOf(getMainLooper()).idle();

            final AlertDialog dialog = getLatestAlertDialog();

            final NumberPicker numberPickerTemperature = dialog.findViewById(R.id.number_picker_new_tea_dialog_temperature);
            numberPickerTemperature.setValue(212);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            final LinearLayout layoutCoolDownTime = newTea.findViewById(R.id.layout_new_tea_cool_down_time);

            assertThat(layoutCoolDownTime.getVisibility()).isEqualTo(View.GONE);
        });
    }

    @Test
    public void changeCoolDownTimeAndExpectUpdatedCoolDownTime() {
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextCoolDownTime = newTea.findViewById(R.id.edit_text_new_tea_cool_down_time);
            editTextCoolDownTime.performClick();
            shadowOf(getMainLooper()).idle();

            final AlertDialog dialog = getLatestAlertDialog();

            final NumberPicker numberPickerTimeMinutes = dialog.findViewById(R.id.number_picker_new_tea_dialog_time_minutes);
            numberPickerTimeMinutes.setValue(5);

            final NumberPicker numberPickerTimeSeconds = dialog.findViewById(R.id.number_picker_new_tea_dialog_time_seconds);
            numberPickerTimeSeconds.setValue(45);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            assertThat(editTextCoolDownTime.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_cool_down_time_text, "05:45"));
        });
    }

    @Test
    public void changeTimeAndExpectUpdatedTime() {
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextTime = newTea.findViewById(R.id.edit_text_new_tea_time);
            editTextTime.performClick();
            shadowOf(getMainLooper()).idle();

            final AlertDialog dialog = getLatestAlertDialog();

            final NumberPicker numberPickerTimeMinutes = dialog.findViewById(R.id.number_picker_new_tea_dialog_time_minutes);
            numberPickerTimeMinutes.setValue(5);

            final NumberPicker numberPickerTimeSeconds = dialog.findViewById(R.id.number_picker_new_tea_dialog_time_seconds);
            numberPickerTimeSeconds.setValue(45);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            assertThat(editTextTime.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_time_text, "05:45"));
        });
    }

    @Test
    public void showActivityEditModeAndExpectFilledFields() {
        mockTemperatureUnitFahrenheit();
        final Tea tea = new Tea("Tea", GREEN_TEA.getCode(), 1, TEA_SPOON.getText(), 234, 0, Date.from(getFixedDate()));
        tea.setId(1L);
        when(teaDao.getTeaById(1)).thenReturn(tea);

        final List<Infusion> infusions = new ArrayList<>();
        infusions.add(new Infusion(1, 0, "2:00", "5:00", 90, 194));
        when(infusionDao.getInfusionsByTeaId(1)).thenReturn(infusions);


        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), NewTea.class);
        intent.putExtra(TEA_ID, 1L);

        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(intent);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextVariety = newTea.findViewById(R.id.edit_text_new_tea_variety);
            final EditText editTextName = newTea.findViewById(R.id.edit_text_new_tea_name);
            final EditText editTextAmount = newTea.findViewById(R.id.edit_text_new_tea_amount);
            final EditText editTextTemperature = newTea.findViewById(R.id.edit_text_new_tea_temperature);
            final EditText editTextTime = newTea.findViewById(R.id.edit_text_new_tea_time);
            final EditText editTextCoolDownTime = newTea.findViewById(R.id.edit_text_new_tea_cool_down_time);

            assertThat(editTextVariety.getText()).hasToString(Variety.convertStoredVarietyToText(tea.getVariety(), newTea.getApplication()));
            assertThat(editTextName.getText()).hasToString(tea.getName());
            assertThat(editTextAmount.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_amount_text_ts, (int) tea.getAmount()));
            assertThat(editTextTemperature.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_temperature_text_fahrenheit, infusions.get(0).getTemperatureFahrenheit()));
            assertThat(editTextTime.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_time_text, infusions.get(0).getTime()));
            assertThat(editTextCoolDownTime.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_cool_down_time_text, infusions.get(0).getCoolDownTime()));
        });
    }

    @Test
    public void editTeaAndExpectEditedTea() {
        mockTemperatureUnitFahrenheit();
        final Tea tea = new Tea("Tea", GREEN_TEA.getCode(), 1, TEA_SPOON.getText(), 234, 0, Date.from(getFixedDate()));
        tea.setId(1L);
        when(teaDao.getTeaById(1)).thenReturn(tea);

        final List<Infusion> infusions = new ArrayList<>();
        infusions.add(new Infusion(1, 0, "2:00", "5:00", 100, 212));
        when(infusionDao.getInfusionsByTeaId(1)).thenReturn(infusions);


        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), NewTea.class);
        intent.putExtra(TEA_ID, 1L);

        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(intent);
        newTeaActivityScenario.onActivity(newTea -> {
            newTea.onOptionsItemSelected(new RoboMenuItem(R.id.action_new_tea_done));

            final ArgumentCaptor<Tea> captorTea = ArgumentCaptor.forClass(Tea.class);
            verify(teaDao).update(captorTea.capture());
            final Tea editedTea = captorTea.getValue();
            assertThat(editedTea.getName()).isEqualTo(tea.getName());

            final ArgumentCaptor<Infusion> captorInfusion = ArgumentCaptor.forClass(Infusion.class);
            verify(infusionDao).insert(captorInfusion.capture());
            final Infusion infusion = captorInfusion.getValue();
            assertThat(infusion.getTime()).isEqualTo(infusions.get(0).getTime());
        });
    }

    @Test
    public void addInfusion() {
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final ImageButton buttonLeft = newTea.findViewById(R.id.button_new_tea_previous_infusion);
            final ImageButton buttonRight = newTea.findViewById(R.id.button_new_tea_next_infusion);
            final ImageButton buttonDelete = newTea.findViewById(R.id.button_new_tea_delete_infusion);
            final ImageButton buttonAddInfusion = newTea.findViewById(R.id.button_new_tea_add_infusion);
            final TextView textViewInfunsionBar = newTea.findViewById(R.id.text_view_new_tea_count_infusion);

            assertThat(textViewInfunsionBar.getText()).isEqualTo(FIRST_INFUSION);
            assertThat(buttonLeft.isEnabled()).isFalse();
            assertThat(buttonRight.isEnabled()).isFalse();
            assertThat(buttonDelete.getVisibility()).isEqualTo(View.GONE);
            assertThat(buttonAddInfusion.isEnabled()).isTrue();

            buttonAddInfusion.performClick();
            assertThat(textViewInfunsionBar.getText()).isEqualTo(SECOND_INFUSION);
            assertThat(buttonLeft.isEnabled()).isTrue();
            assertThat(buttonDelete.getVisibility()).isEqualTo(View.VISIBLE);
        });
    }

    @Test
    public void performNextAndPreviousInfusion() {
        mockTemperatureUnitFahrenheit();
        final Tea tea = new Tea("Tea", GREEN_TEA.getCode(), 1, TEA_SPOON.getText(), 234, 0, Date.from(getFixedDate()));
        tea.setId(1L);
        when(teaDao.getTeaById(1)).thenReturn(tea);

        final List<Infusion> infusions = new ArrayList<>();
        final Infusion infusion1 = new Infusion(1, 0, "2:00", "5:00", 100, 212);
        infusions.add(infusion1);
        final Infusion infusion2 = new Infusion(1, 1, "4:00", "", 70, 158);
        infusions.add(infusion2);
        when(infusionDao.getInfusionsByTeaId(1)).thenReturn(infusions);


        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), NewTea.class);
        intent.putExtra(TEA_ID, 1L);
        intent.putExtra(SHOW_TEA_FLAG, true);

        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(intent);
        newTeaActivityScenario.onActivity(newTea -> {
            final ImageButton buttonLeft = newTea.findViewById(R.id.button_new_tea_previous_infusion);
            final ImageButton buttonRight = newTea.findViewById(R.id.button_new_tea_next_infusion);
            final ImageButton buttonDeleteInfusion = newTea.findViewById(R.id.button_new_tea_delete_infusion);
            final ImageButton buttonAddInfusion = newTea.findViewById(R.id.button_new_tea_add_infusion);
            final TextView textViewInfusionBar = newTea.findViewById(R.id.text_view_new_tea_count_infusion);

            assertThat(buttonLeft.isEnabled()).isFalse();
            assertThat(buttonRight.isEnabled()).isTrue();
            assertThat(buttonAddInfusion.getVisibility()).isEqualTo(View.GONE);
            assertThat(buttonDeleteInfusion.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(textViewInfusionBar.getText()).hasToString(FIRST_INFUSION);

            buttonRight.performClick();

            assertThat(buttonLeft.isEnabled()).isTrue();
            assertThat(buttonRight.isEnabled()).isFalse();
            assertThat(buttonAddInfusion.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(textViewInfusionBar.getText()).hasToString(SECOND_INFUSION);

            buttonLeft.performClick();

            assertThat(buttonLeft.isEnabled()).isFalse();
            assertThat(buttonRight.isEnabled()).isTrue();
            assertThat(buttonAddInfusion.getVisibility()).isEqualTo(View.GONE);
            assertThat(textViewInfusionBar.getText()).hasToString(FIRST_INFUSION);
        });
    }

    @Test
    public void performDeleteInfusion() {
        mockTemperatureUnitFahrenheit();
        final Tea tea = new Tea("Tea", GREEN_TEA.getCode(), 1, TEA_SPOON.getText(), 234, 0, Date.from(getFixedDate()));
        tea.setId(1L);
        when(teaDao.getTeaById(1)).thenReturn(tea);

        final List<Infusion> infusions = new ArrayList<>();
        final Infusion infusion1 = new Infusion(1, 0, "2:00", "5:00", 100, 212);
        infusions.add(infusion1);
        final Infusion infusion2 = new Infusion(1, 1, "4:00", "", 70, 158);
        infusions.add(infusion2);
        when(infusionDao.getInfusionsByTeaId(1)).thenReturn(infusions);


        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), NewTea.class);
        intent.putExtra(TEA_ID, 1L);
        intent.putExtra(SHOW_TEA_FLAG, true);

        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(intent);
        newTeaActivityScenario.onActivity(newTea -> {
            final ImageButton buttonNextInfusion = newTea.findViewById(R.id.button_new_tea_next_infusion);
            final ImageButton buttonDeleteInfusion = newTea.findViewById(R.id.button_new_tea_delete_infusion);
            final TextView textViewInfusionBar = newTea.findViewById(R.id.text_view_new_tea_count_infusion);

            buttonNextInfusion.performClick();

            assertThat(textViewInfusionBar.getText()).hasToString(SECOND_INFUSION);

            buttonDeleteInfusion.performClick();

            assertThat(textViewInfusionBar.getText()).hasToString(FIRST_INFUSION);
        });
    }

    @Test
    public void exitEditModeAndExpectShowTeaActivity() {
        final Tea tea = new Tea("Tea", BLACK_TEA.getCode(), 1, GRAM.getText(), 1, 0, Date.from(getFixedDate()));
        tea.setId(1L);
        when(teaDao.getTeaById(1)).thenReturn(tea);

        final List<Infusion> infusions = new ArrayList<>();
        final Infusion infusion = new Infusion(1, 0, "", "", 100, 212);
        infusions.add(infusion);
        when(infusionDao.getInfusionsByTeaId(1)).thenReturn(infusions);


        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), NewTea.class);
        intent.putExtra(TEA_ID, 1L);
        intent.putExtra(SHOW_TEA_FLAG, true);

        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(intent);
        newTeaActivityScenario.onActivity(newTea -> {
            newTea.onOptionsItemSelected(new RoboMenuItem(android.R.id.home));

            final Intent expected = new Intent(newTea, ShowTea.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void editTeaAndExpectShowTeaActivity() {
        mockTemperatureUnitFahrenheit();
        final Tea tea = new Tea("Tea", GREEN_TEA.getCode(), 1, TEA_SPOON.getText(), 234, 0, Date.from(getFixedDate()));
        tea.setId(1L);
        when(teaDao.getTeaById(1)).thenReturn(tea);

        final List<Infusion> infusions = new ArrayList<>();
        final Infusion infusion1 = new Infusion(1, 0, "2:00", "5:00", 100, 212);
        infusions.add(infusion1);
        when(infusionDao.getInfusionsByTeaId(1)).thenReturn(infusions);


        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), NewTea.class);
        intent.putExtra(TEA_ID, 1L);
        intent.putExtra(SHOW_TEA_FLAG, true);

        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(intent);
        newTeaActivityScenario.onActivity(newTea -> {
            newTea.onOptionsItemSelected(new RoboMenuItem(R.id.action_new_tea_done));

            final Intent expected = new Intent(newTea, ShowTea.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    private void mockTemperatureUnitFahrenheit() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());
        sharedSettings.setTemperatureUnit(FAHRENHEIT);
    }

    private Instant getFixedDate() {
        final Clock clock = Clock.fixed(Instant.parse(CURRENT_DATE), ZoneId.of("UTC"));
        return Instant.now(clock);
    }

    private List<RadioButton> getRadioButtons(final AlertDialog dialog) {
        final RadioGroup radioGroup = dialog.findViewById(R.id.radio_group_new_tea_variety_input);
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
