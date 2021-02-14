package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
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
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.views.showtea.ShowTea;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;


//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class NewTeaTest {

    public static final String CURRENT_DATE = "2020-08-19T10:15:30Z";
    public static final String CELSIUS = "Celsius";
    public static final String FIRST_INFUSION = "1. Infusion";
    public static final String SECOND_INFUSION = "2. Infusion";
    public static final String FAHRENHEIT = "Fahrenheit";
    public static final String TEA_ID = "teaId";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    TeaDao teaDao;
    @Mock
    InfusionDao infusionDao;
    @Mock
    ActualSettingsDao actualSettingsDao;

    @Before
    public void setUp() {
        mockDB();
    }

    private void mockDB() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDao);
        when(teaMemoryDatabase.getInfusionDao()).thenReturn(infusionDao);
        when(teaMemoryDatabase.getActualSettingsDao()).thenReturn(actualSettingsDao);
    }

    @Test
    public void showActivityAddModeAndExpectFilledDefaultValues() {
        mockSettings(CELSIUS);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final Spinner spinnerVariety = newTea.findViewById(R.id.spinnerTeaVariety);
            final EditText editTextName = newTea.findViewById(R.id.editTextName);
            final Button buttonAmountDialog = newTea.findViewById(R.id.buttonAmountDialog);
            final Button buttonTemperatureDialog = newTea.findViewById(R.id.buttonTemperatureDialog);
            final Button buttonCoolDownTimeDialog = newTea.findViewById(R.id.buttonCoolDownTimeDialog);
            final Button buttonTimeDialog = newTea.findViewById(R.id.buttonTimeDialog);

            assertThat(spinnerVariety.getSelectedItemId()).isZero();
            assertThat(editTextName.getText().toString()).isBlank();
            assertThat(buttonAmountDialog.getText()).hasToString(newTea.getString(R.string.newtea_button_amount_empty_text_ts));
            assertThat(buttonTemperatureDialog.getText()).hasToString(newTea.getString(R.string.newtea_button_temperature_empty_text_celsius));
            assertThat(buttonCoolDownTimeDialog.getText()).hasToString(newTea.getString(R.string.newtea_button_cool_down_time_empty_text));
            assertThat(buttonTimeDialog.getText()).hasToString(newTea.getString(R.string.newtea_button_time_empty_text));
        });
    }

    @Test
    public void showActivityAddModeAndExpectFilledDefaultValuesFahrenheit() {
        mockSettings(FAHRENHEIT);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final Button buttonTemperatureDialog = newTea.findViewById(R.id.buttonTemperatureDialog);
            assertThat(buttonTemperatureDialog.getText()).hasToString(newTea.getString(R.string.newtea_button_temperature_empty_text_fahrenheit));
        });
    }

    @Test
    public void addNewTeaAndExpectSavedDefaultValues() {
        mockSettings(CELSIUS);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextName = newTea.findViewById(R.id.editTextName);
            editTextName.setText("Name");
            newTea.onOptionsItemSelected(new RoboMenuItem(R.id.action_done));
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
                    Tea::isFavorite
            ).containsExactly(
                    "Name", "01_black", -15461296, -500, "Ts", 0, false
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
    public void addAmountAndExpectUpdatedAmount() {
        mockSettings(CELSIUS);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final Button buttonAmount = newTea.findViewById(R.id.buttonAmountDialog);
            buttonAmount.performClick();

            final AlertDialog dialog = getLatestAlertDialog();

            final NumberPicker amountPicker = dialog.findViewById(R.id.number_picker_dialog_amount);
            amountPicker.setValue(7);

            final NumberPicker amountKindPicker = dialog.findViewById(R.id.number_picker_dialog_amount_kind);
            amountKindPicker.setValue(0);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            assertThat(buttonAmount.getText()).hasToString("7 ts/L");
        });
    }

    @Test
    public void addTemperatureAndExpectUpdatedTemperature() {
        mockSettings(CELSIUS);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final Button buttonTemperature = newTea.findViewById(R.id.buttonTemperatureDialog);
            buttonTemperature.performClick();

            final AlertDialog dialog = getLatestAlertDialog();

            final NumberPicker numberPickerTemperature = dialog.findViewById(R.id.number_picker_dialog_temperature);
            numberPickerTemperature.setValue(80);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            assertThat(buttonTemperature.getText()).hasToString("80 °C");
        });
    }

    @Test
    public void showCoolDownTimeWhenTemperatureIsLessThan100Celsius() {
        mockSettings(CELSIUS);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final Button buttonTemperature = newTea.findViewById(R.id.buttonTemperatureDialog);
            buttonTemperature.performClick();

            final AlertDialog dialog = getLatestAlertDialog();

            final NumberPicker numberPickerTemperature = dialog.findViewById(R.id.number_picker_dialog_temperature);
            numberPickerTemperature.setValue(80);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            final Button buttonCoolDownTime = newTea.findViewById(R.id.buttonCoolDownTimeDialog);

            assertThat(buttonCoolDownTime.getVisibility()).isEqualTo(View.VISIBLE);
        });
    }

    @Test
    public void hideCoolDownTimeWhenTemperatureIs100Celsius() {
        mockSettings(FAHRENHEIT);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final Button buttonTemperature = newTea.findViewById(R.id.buttonTemperatureDialog);
            buttonTemperature.performClick();

            final AlertDialog dialog = getLatestAlertDialog();

            final NumberPicker numberPickerTemperature = dialog.findViewById(R.id.number_picker_dialog_temperature);
            numberPickerTemperature.setValue(212);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            final Button buttonCoolDownTime = newTea.findViewById(R.id.buttonCoolDownTimeDialog);

            assertThat(buttonCoolDownTime.getVisibility()).isEqualTo(View.GONE);
        });
    }

    @Test
    public void addCoolDownTimeAndExpectUpdatedCoolDownTime() {
        mockSettings(CELSIUS);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final Button buttonCoolDownTime = newTea.findViewById(R.id.buttonCoolDownTimeDialog);
            buttonCoolDownTime.performClick();

            final AlertDialog dialog = getLatestAlertDialog();

            final NumberPicker numberPickerTimeMinutes = dialog.findViewById(R.id.number_picker_dialog_time_minutes);
            numberPickerTimeMinutes.setValue(5);

            final NumberPicker numberPickerTimeSeconds = dialog.findViewById(R.id.number_picker_dialog_time_seconds);
            numberPickerTimeSeconds.setValue(45);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            assertThat(buttonCoolDownTime.getText()).hasToString("05:45");
        });
    }

    @Test
    public void addTimeAndExpectUpdatedTime() {
        mockSettings(CELSIUS);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final Button buttonTime = newTea.findViewById(R.id.buttonTimeDialog);
            buttonTime.performClick();

            final AlertDialog dialog = getLatestAlertDialog();

            final NumberPicker numberPickerTimeMinutes = dialog.findViewById(R.id.number_picker_dialog_time_minutes);
            numberPickerTimeMinutes.setValue(5);

            final NumberPicker numberPickerTimeSeconds = dialog.findViewById(R.id.number_picker_dialog_time_seconds);
            numberPickerTimeSeconds.setValue(45);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            assertThat(buttonTime.getText()).hasToString("05:45");
        });
    }

    @Test
    public void setVarietyByHandAndExpectThisVariety() {
        mockSettings(CELSIUS);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final Spinner spinnerVariety = newTea.findViewById(R.id.spinnerTeaVariety);
            spinnerVariety.setSelection(9);

            final CheckBox checkBoxVariety = newTea.findViewById(R.id.checkBoxSelfInput);
            checkBoxVariety.setChecked(true);

            final EditText editTextVariety = newTea.findViewById(R.id.editTextSelfInput);
            editTextVariety.setText("OtherVariety");

            final EditText editTextName = newTea.findViewById(R.id.editTextName);
            editTextName.setText("Tea");

            newTea.onOptionsItemSelected(new RoboMenuItem(R.id.action_done));

            final ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
            verify(teaDao).insert(captor.capture());
            final Tea tea = captor.getValue();

            assertThat(tea.getVariety()).isEqualTo("OtherVariety");
        });
    }

    @Test
    public void switchBetweenVarietyByHandAndBySpinner() {
        mockSettings(CELSIUS);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final Spinner spinnerVariety = newTea.findViewById(R.id.spinnerTeaVariety);
            spinnerVariety.setSelection(9);

            final CheckBox checkBoxVariety = newTea.findViewById(R.id.checkBoxSelfInput);
            assertThat(checkBoxVariety.getVisibility()).isEqualTo(View.VISIBLE);
            checkBoxVariety.setChecked(true);

            assertThat(spinnerVariety.getVisibility()).isEqualTo(View.INVISIBLE);
            final EditText editTextVariety = newTea.findViewById(R.id.editTextSelfInput);
            assertThat(editTextVariety.getVisibility()).isEqualTo(View.VISIBLE);

            checkBoxVariety.setChecked(false);
            assertThat(spinnerVariety.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(editTextVariety.getVisibility()).isEqualTo(View.INVISIBLE);

            spinnerVariety.setSelection(3);
            assertThat(checkBoxVariety.getVisibility()).isEqualTo(View.INVISIBLE);
        });
    }

    @Test
    public void showActivityEditModeAndExpectFilledFields() {
        mockSettings(FAHRENHEIT);
        final Tea tea = new Tea("Tea", "02_green", 1, "Ts", 234, 0, Date.from(getFixedDate()));
        tea.setId(1L);
        when(teaDao.getTeaById(1)).thenReturn(tea);

        final List<Infusion> infusions = new ArrayList<>();
        infusions.add(new Infusion(1, 0, "2:00", "5:00", 100, 212));
        when(infusionDao.getInfusionsByTeaId(1)).thenReturn(infusions);


        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), NewTea.class);
        intent.putExtra(TEA_ID, 1L);

        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(intent);
        newTeaActivityScenario.onActivity(newTea -> {
            final Spinner spinnerVariety = newTea.findViewById(R.id.spinnerTeaVariety);
            final EditText editTextName = newTea.findViewById(R.id.editTextName);
            final Button buttonAmount = newTea.findViewById(R.id.buttonAmountDialog);
            final Button buttonTemperature = newTea.findViewById(R.id.buttonTemperatureDialog);
            final Button buttonTime = newTea.findViewById(R.id.buttonTimeDialog);
            final Button buttonCoolDownTime = newTea.findViewById(R.id.buttonCoolDownTimeDialog);

            assertThat(spinnerVariety.getSelectedItemPosition()).isEqualTo(1);
            assertThat(editTextName.getText()).hasToString(tea.getName());
            assertThat(buttonAmount.getText()).hasToString(tea.getAmount() + " ts/L");
            assertThat(buttonTemperature.getText()).hasToString(infusions.get(0).getTemperatureFahrenheit() + " °F");
            assertThat(buttonTime.getText()).hasToString(infusions.get(0).getTime());
            assertThat(buttonCoolDownTime.getText()).hasToString(infusions.get(0).getCoolDownTime());
        });
    }

    @Test
    public void editTeaAndExpectFilledOtherVariety() {
        mockSettings(FAHRENHEIT);
        final Tea tea = new Tea("Tea", "OtherTea", 1, "Gr", 234, 0, Date.from(getFixedDate()));
        tea.setId(1L);
        when(teaDao.getTeaById(1)).thenReturn(tea);

        final List<Infusion> infusions = new ArrayList<>();
        final Infusion infusion1 = new Infusion(1, 0, "2:00", "5:00", 100, 212);
        infusions.add(infusion1);
        when(infusionDao.getInfusionsByTeaId(1)).thenReturn(infusions);


        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), NewTea.class);
        intent.putExtra(TEA_ID, 1L);

        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(intent);
        newTeaActivityScenario.onActivity(newTea -> {

            final Spinner spinnerVariety = newTea.findViewById(R.id.spinnerTeaVariety);
            final CheckBox checkBoxSelfInput = newTea.findViewById(R.id.checkBoxSelfInput);
            final EditText editTextSelfInput = newTea.findViewById(R.id.editTextSelfInput);

            assertThat(spinnerVariety.getSelectedItemPosition()).isEqualTo(9);
            assertThat(checkBoxSelfInput.isChecked()).isTrue();
            assertThat(editTextSelfInput.getText()).hasToString("OtherTea");
        });
    }

    @Test
    public void editTeaAndExpectEditedTea() {
        mockSettings(FAHRENHEIT);
        final Tea tea = new Tea("Tea", "02_green", 1, "Ts", 234, 0, Date.from(getFixedDate()));
        tea.setId(1L);
        when(teaDao.getTeaById(1)).thenReturn(tea);

        final List<Infusion> infusions = new ArrayList<>();
        infusions.add(new Infusion(1, 0, "2:00", "5:00", 100, 212));
        when(infusionDao.getInfusionsByTeaId(1)).thenReturn(infusions);


        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), NewTea.class);
        intent.putExtra(TEA_ID, 1L);

        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(intent);
        newTeaActivityScenario.onActivity(newTea -> {
            newTea.onOptionsItemSelected(new RoboMenuItem(R.id.action_done));

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
        mockSettings(CELSIUS);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final ImageButton buttonLeft = newTea.findViewById(R.id.buttonPreviousInfusion);
            final ImageButton buttonRight = newTea.findViewById(R.id.buttonNextInfusion);
            final ImageButton buttonDelete = newTea.findViewById(R.id.buttonDeleteInfusion);
            final ImageButton buttonAddInfusion = newTea.findViewById(R.id.buttonAddInfusion);
            final TextView textViewInfunsionBar = newTea.findViewById(R.id.textViewCountInfusion);

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
        mockSettings(FAHRENHEIT);
        final Tea tea = new Tea("Tea", "02_green", 1, "Ts", 234, 0, Date.from(getFixedDate()));
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
        intent.putExtra("showTea", true);

        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(intent);
        newTeaActivityScenario.onActivity(newTea -> {
            final ImageButton buttonLeft = newTea.findViewById(R.id.buttonPreviousInfusion);
            final ImageButton buttonRight = newTea.findViewById(R.id.buttonNextInfusion);
            final ImageButton buttonDeleteInfusion = newTea.findViewById(R.id.buttonDeleteInfusion);
            final ImageButton buttonAddInfusion = newTea.findViewById(R.id.buttonAddInfusion);
            final TextView textViewInfusionBar = newTea.findViewById(R.id.textViewCountInfusion);

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
        mockSettings(FAHRENHEIT);
        final Tea tea = new Tea("Tea", "02_green", 1, "Ts", 234, 0, Date.from(getFixedDate()));
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
        intent.putExtra("showTea", true);

        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(intent);
        newTeaActivityScenario.onActivity(newTea -> {
            final ImageButton buttonNextInfusion = newTea.findViewById(R.id.buttonNextInfusion);
            final ImageButton buttonDeleteInfusion = newTea.findViewById(R.id.buttonDeleteInfusion);
            final TextView textViewInfusionBar = newTea.findViewById(R.id.textViewCountInfusion);

            buttonNextInfusion.performClick();

            assertThat(textViewInfusionBar.getText()).hasToString(SECOND_INFUSION);

            buttonDeleteInfusion.performClick();

            assertThat(textViewInfusionBar.getText()).hasToString(FIRST_INFUSION);
        });
    }

    @Test
    public void exitEditModeAndExpectShowTeaActivity() {
        mockSettings(CELSIUS);
        final Tea tea = new Tea("Tea", "01_black", 1, "Gr", 1, 0, Date.from(getFixedDate()));
        tea.setId(1L);
        when(teaDao.getTeaById(1)).thenReturn(tea);

        final List<Infusion> infusions = new ArrayList<>();
        final Infusion infusion = new Infusion(1, 0, "", "", 100, 212);
        infusions.add(infusion);
        when(infusionDao.getInfusionsByTeaId(1)).thenReturn(infusions);


        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), NewTea.class);
        intent.putExtra(TEA_ID, 1L);
        intent.putExtra("showTea", true);

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
        mockSettings(FAHRENHEIT);
        final Tea tea = new Tea("Tea", "02_green", 1, "Ts", 234, 0, Date.from(getFixedDate()));
        tea.setId(1L);
        when(teaDao.getTeaById(1)).thenReturn(tea);

        final List<Infusion> infusions = new ArrayList<>();
        final Infusion infusion1 = new Infusion(1, 0, "2:00", "5:00", 100, 212);
        infusions.add(infusion1);
        when(infusionDao.getInfusionsByTeaId(1)).thenReturn(infusions);


        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), NewTea.class);
        intent.putExtra(TEA_ID, 1L);
        intent.putExtra("showTea", true);

        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(intent);
        newTeaActivityScenario.onActivity(newTea -> {
            newTea.onOptionsItemSelected(new RoboMenuItem(R.id.action_done));

            final Intent expected = new Intent(newTea, ShowTea.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    private void mockSettings(String temperatureUnit) {
        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setTemperatureUnit(temperatureUnit);
        when(actualSettingsDao.getSettings()).thenReturn(actualSettings);
    }

    private Instant getFixedDate() {
        Clock clock = Clock.fixed(Instant.parse(CURRENT_DATE), ZoneId.of("UTC"));
        return Instant.now(clock);
    }
}
