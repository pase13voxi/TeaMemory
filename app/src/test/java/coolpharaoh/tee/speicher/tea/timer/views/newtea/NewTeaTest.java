package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.assertj.core.groups.Tuple;
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
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.views.showtea.ShowTea;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
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
    CounterDao counterDao;
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
        when(teaMemoryDatabase.getCounterDao()).thenReturn(counterDao);
        when(teaMemoryDatabase.getActualSettingsDao()).thenReturn(actualSettingsDao);
    }

    @Test
    public void addNewTeaAndExpectSavedTea() {
        mockSettings(CELSIUS);
        ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            Spinner spinnerVariety = newTea.findViewById(R.id.spinnerTeaVariety);
            EditText editTextName = newTea.findViewById(R.id.editTextName);
            Spinner spinnerAmountKind = newTea.findViewById(R.id.spinnerAmountUnit);
            EditText editTextAmount = newTea.findViewById(R.id.editTextAmount);
            Button buttonAddInfusion = newTea.findViewById(R.id.buttonAddInfusion);
            EditText editTextTemperature = newTea.findViewById(R.id.editTextTemperature);
            EditText editTextTime = newTea.findViewById(R.id.editTextTime);
            EditText editTextCoolDownTime = newTea.findViewById(R.id.editTextCoolDownTime);

            spinnerVariety.setSelection(2);
            editTextName.setText("Name");
            spinnerAmountKind.setSelection(1);
            inputInfusion(editTextAmount, editTextTemperature, editTextTime, "15", "95", "2:30");
            editTextCoolDownTime.setText("5:00");
            //second Infusion
            buttonAddInfusion.performClick();
            inputInfusion(editTextTemperature, editTextTime, editTextCoolDownTime, "70", "5:00", "15:00");

            newTea.onOptionsItemSelected(new RoboMenuItem(R.id.action_done));

            ArgumentCaptor<Tea> captorTea = ArgumentCaptor.forClass(Tea.class);
            verify(teaDao).insert(captorTea.capture());
            Tea tea = captorTea.getValue();
            assertThat(tea).extracting(
                    Tea::getName,
                    Tea::getVariety,
                    Tea::getColor,
                    Tea::getAmount,
                    Tea::getAmountKind
            ).containsExactly(
                    "Name",
                    "03_yellow",
                    -15797,
                    15,
                    "Gr"
            );

            ArgumentCaptor<Infusion> captorInfusions = ArgumentCaptor.forClass(Infusion.class);
            verify(infusionDao, times(2)).insert(captorInfusions.capture());
            List<Infusion> infusions = captorInfusions.getAllValues();
            assertThat(infusions).extracting(
                    Infusion::getTemperatureCelsius,
                    Infusion::getTime,
                    Infusion::getCoolDownTime
            ).containsExactly(
                    Tuple.tuple(
                            95,
                            "2:30",
                            "5:00"
                    ),
                    Tuple.tuple(
                            70,
                            "5:00",
                            "15:00"
                    )
            );

            verify(counterDao).insert(any());
        });
    }

    @Test
    public void setVarietyByHandAndExpectThisVariety() {
        mockSettings(CELSIUS);
        ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            Spinner spinnerVariety = newTea.findViewById(R.id.spinnerTeaVariety);
            spinnerVariety.setSelection(9);

            CheckBox checkBoxVariety = newTea.findViewById(R.id.checkBoxSelfInput);
            checkBoxVariety.setChecked(true);

            EditText editTextVariety = newTea.findViewById(R.id.editTextSelfInput);
            editTextVariety.setText("OtherVariety");

            EditText editTextName = newTea.findViewById(R.id.editTextName);
            editTextName.setText("Tea");

            newTea.onOptionsItemSelected(new RoboMenuItem(R.id.action_done));

            ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
            verify(teaDao).insert(captor.capture());
            Tea tea = captor.getValue();

            assertThat(tea.getVariety()).isEqualTo("OtherVariety");
        });
    }

    @Test
    public void switchBetweenSettingVarietyByHandOrBySpinner() {
        mockSettings(CELSIUS);
        ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            Spinner spinnerVariety = newTea.findViewById(R.id.spinnerTeaVariety);
            spinnerVariety.setSelection(9);

            CheckBox checkBoxVariety = newTea.findViewById(R.id.checkBoxSelfInput);
            assertThat(checkBoxVariety.getVisibility()).isEqualTo(View.VISIBLE);
            checkBoxVariety.setChecked(true);

            assertThat(spinnerVariety.getVisibility()).isEqualTo(View.INVISIBLE);
            EditText editTextVariety = newTea.findViewById(R.id.editTextSelfInput);
            assertThat(editTextVariety.getVisibility()).isEqualTo(View.VISIBLE);

            checkBoxVariety.setChecked(false);
            assertThat(spinnerVariety.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(editTextVariety.getVisibility()).isEqualTo(View.INVISIBLE);

            spinnerVariety.setSelection(3);
            assertThat(checkBoxVariety.getVisibility()).isEqualTo(View.INVISIBLE);
        });
    }

    @Test
    public void addInfusionsNavigateBetweenThemAndDeleteInfusion() {
        mockSettings(CELSIUS);
        ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            Button buttonLeft = newTea.findViewById(R.id.buttonArrowLeft);
            Button buttonRight = newTea.findViewById(R.id.buttonArrowRight);
            Button buttonDelete = newTea.findViewById(R.id.buttonDeleteInfusion);
            Button buttonAddInfusion = newTea.findViewById(R.id.buttonAddInfusion);
            TextView textViewInfunsionBar = newTea.findViewById(R.id.textViewCountInfusion);
            EditText editTextTemperature = newTea.findViewById(R.id.editTextTemperature);
            EditText editTextTime = newTea.findViewById(R.id.editTextTime);
            EditText editTextCoolDownTime = newTea.findViewById(R.id.editTextCoolDownTime);

            inputInfusion(editTextTemperature, editTextTime, editTextCoolDownTime, "1", "1", "1");
            assertThat(textViewInfunsionBar.getText()).isEqualTo(FIRST_INFUSION);
            assertThat(buttonLeft.isEnabled()).isFalse();
            assertThat(buttonRight.isEnabled()).isFalse();
            assertThat(buttonDelete.getVisibility()).isEqualTo(View.GONE);
            assertThat(buttonAddInfusion.isEnabled()).isTrue();

            buttonAddInfusion.performClick();
            checkInfusion(editTextTemperature, editTextTime, editTextCoolDownTime, "", "", "");
            inputInfusion(editTextTemperature, editTextTime, editTextCoolDownTime, "2", "2", "2");
            assertThat(textViewInfunsionBar.getText()).isEqualTo(SECOND_INFUSION);
            assertThat(buttonLeft.isEnabled()).isTrue();
            assertThat(buttonDelete.getVisibility()).isEqualTo(View.VISIBLE);

            buttonAddInfusion.performClick();
            checkInfusion(editTextTemperature, editTextTime, editTextCoolDownTime, "", "", "");
            inputInfusion(editTextTemperature, editTextTime, editTextCoolDownTime, "3", "3", "3");
            assertThat(textViewInfunsionBar.getText()).isEqualTo("3. Infusion");

            buttonLeft.performClick();
            checkInfusion(editTextTemperature, editTextTime, editTextCoolDownTime, "2", "2", "2");
            assertThat(textViewInfunsionBar.getText()).isEqualTo(SECOND_INFUSION);
            assertThat(buttonRight.isEnabled()).isTrue();
            assertThat(buttonAddInfusion.getVisibility()).isEqualTo(View.GONE);

            buttonDelete.performClick();
            checkInfusion(editTextTemperature, editTextTime, editTextCoolDownTime, "3", "3", "3");
            assertThat(textViewInfunsionBar.getText()).isEqualTo(SECOND_INFUSION);
            assertThat(buttonRight.isEnabled()).isFalse();
            assertThat(buttonAddInfusion.getVisibility()).isEqualTo(View.VISIBLE);

            buttonLeft.performClick();
            checkInfusion(editTextTemperature, editTextTime, editTextCoolDownTime, "1", "1", "1");
            assertThat(textViewInfunsionBar.getText()).isEqualTo(FIRST_INFUSION);
            assertThat(buttonLeft.isEnabled()).isFalse();
            assertThat(buttonRight.isEnabled()).isTrue();
            assertThat(buttonAddInfusion.getVisibility()).isEqualTo(View.GONE);

            buttonRight.performClick();
            checkInfusion(editTextTemperature, editTextTime, editTextCoolDownTime, "3", "3", "3");
            assertThat(textViewInfunsionBar.getText()).isEqualTo(SECOND_INFUSION);
            assertThat(buttonLeft.isEnabled()).isTrue();
            assertThat(buttonRight.isEnabled()).isFalse();
            assertThat(buttonAddInfusion.getVisibility()).isEqualTo(View.VISIBLE);
        });
    }

    @Test
    public void setWrongTemperatureAndAddInfusionAndAutofillCoolDownTimeExpectNothing() {
        mockSettings(CELSIUS);
        ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            EditText editTextName = newTea.findViewById(R.id.editTextName);
            Button buttonAddInfusion = newTea.findViewById(R.id.buttonAddInfusion);
            TextView textViewCountInfusion = newTea.findViewById(R.id.textViewCountInfusion);
            Button buttonAutofillCoolDownTime = newTea.findViewById(R.id.buttonAutofillCoolDownTime);
            EditText editTextTemperature = newTea.findViewById(R.id.editTextTemperature);
            EditText editTextTime = newTea.findViewById(R.id.editTextTime);
            EditText editTextCoolDownTime = newTea.findViewById(R.id.editTextCoolDownTime);

            editTextName.setText("Name");

            inputInfusion(editTextTemperature, editTextTime, editTextCoolDownTime, "120", "2:00", "");

            buttonAutofillCoolDownTime.performClick();

            assertThat(editTextCoolDownTime.getText().toString()).isEmpty();

            buttonAddInfusion.performClick();

            assertThat(textViewCountInfusion.getText()).isEqualTo(FIRST_INFUSION);
        });
    }

    @Test
    public void showCoolDownTimeAndCalculateCoolDownTime() {
        mockSettings(FAHRENHEIT);
        ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            Button buttonShowCoolDownTime = newTea.findViewById(R.id.buttonShowCoolDownTime);
            Button buttonAutofillCoolDownTime = newTea.findViewById(R.id.buttonAutofillCoolDownTime);
            EditText editTextTemperature = newTea.findViewById(R.id.editTextTemperature);
            EditText editTextTime = newTea.findViewById(R.id.editTextTime);
            EditText editTextCoolDownTime = newTea.findViewById(R.id.editTextCoolDownTime);

            inputInfusion(editTextTemperature, editTextTime, editTextCoolDownTime, "194", "2", "");

            buttonShowCoolDownTime.performClick();
            assertThat(buttonAutofillCoolDownTime.getVisibility()).isEqualTo(View.VISIBLE);
            assertThat(editTextCoolDownTime.getVisibility()).isEqualTo(View.VISIBLE);

            buttonAutofillCoolDownTime.performClick();
            assertThat(editTextCoolDownTime.getText().toString()).isNotBlank();

            buttonShowCoolDownTime.performClick();
            assertThat(buttonAutofillCoolDownTime.getVisibility()).isEqualTo(View.GONE);
            assertThat(editTextCoolDownTime.getVisibility()).isEqualTo(View.GONE);
        });
    }

    @Test
    public void editTeaAndExpectEditedTeaAndShowTeaActivity() {
        mockSettings(FAHRENHEIT);
        Tea tea = new Tea("Tea", "02_green", 1, "Ts", 234, 0, Date.from(getFixedDate()));
        tea.setId(1l);
        when(teaDao.getTeaById(1)).thenReturn(tea);

        List<Infusion> infusions = new ArrayList<>();
        Infusion infusion1 = new Infusion(1, 0, "2:00", "5:00", 100, 212);
        infusions.add(infusion1);
        Infusion infusion2 = new Infusion(1, 1, "4:00", "", 70, 158);
        infusions.add(infusion2);
        when(infusionDao.getInfusionsByTeaId(1)).thenReturn(infusions);


        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), NewTea.class);
        intent.putExtra(TEA_ID, 1l);
        intent.putExtra("showTea", true);

        ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(intent);
        newTeaActivityScenario.onActivity(newTea -> {

            Spinner spinnerVariety = newTea.findViewById(R.id.spinnerTeaVariety);
            EditText editTextName = newTea.findViewById(R.id.editTextName);
            Spinner spinnerAmountKind = newTea.findViewById(R.id.spinnerAmountUnit);
            EditText editTextAmount = newTea.findViewById(R.id.editTextAmount);
            Button buttonRight = newTea.findViewById(R.id.buttonArrowRight);
            EditText editTextTemperature = newTea.findViewById(R.id.editTextTemperature);
            EditText editTextTime = newTea.findViewById(R.id.editTextTime);
            EditText editTextCoolDownTime = newTea.findViewById(R.id.editTextCoolDownTime);

            assertThat(spinnerVariety.getSelectedItemPosition()).isEqualTo(1);
            assertThat(editTextName.getText()).hasToString(tea.getName());
            assertThat(spinnerAmountKind.getSelectedItemPosition()).isZero();
            assertThat(editTextAmount.getText()).hasToString(String.valueOf(tea.getAmount()));
            assertThat(editTextTemperature.getText()).hasToString(String.valueOf(infusions.get(0).getTemperatureFahrenheit()));
            assertThat(editTextTime.getText()).hasToString(infusions.get(0).getTime());
            assertThat(editTextCoolDownTime.getText()).hasToString(infusions.get(0).getCoolDownTime());

            buttonRight.performClick();

            assertThat(editTextTemperature.getText()).hasToString(String.valueOf(infusions.get(1).getTemperatureFahrenheit()));
            assertThat(editTextTime.getText()).hasToString(infusions.get(1).getTime());
            assertThat(editTextCoolDownTime.getText()).hasToString(infusions.get(1).getCoolDownTime());

            newTea.onOptionsItemSelected(new RoboMenuItem(R.id.action_done));

            ArgumentCaptor<Tea> captorTea = ArgumentCaptor.forClass(Tea.class);
            verify(teaDao).update(captorTea.capture());
            Tea insertedTea = captorTea.getValue();
            assertThat(insertedTea).isEqualTo(tea);

            ArgumentCaptor<Infusion> captorInfusions = ArgumentCaptor.forClass(Infusion.class);
            verify(infusionDao, times(2)).insert(captorInfusions.capture());
            List<Infusion> insertedInfusions = captorInfusions.getAllValues();
            assertThat(insertedInfusions).isEqualTo(infusions);

            Intent expected = new Intent(newTea, ShowTea.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void editTeaAndExpectFilledOtherVarietyAndGr() {
        mockSettings(FAHRENHEIT);
        Tea tea = new Tea("Tea", "OtherTea", 1, "Gr", 234, 0, Date.from(getFixedDate()));
        tea.setId(1l);
        when(teaDao.getTeaById(1)).thenReturn(tea);

        List<Infusion> infusions = new ArrayList<>();
        Infusion infusion1 = new Infusion(1, 0, "2:00", "5:00", 100, 212);
        infusions.add(infusion1);
        when(infusionDao.getInfusionsByTeaId(1)).thenReturn(infusions);


        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), NewTea.class);
        intent.putExtra(TEA_ID, 1l);

        ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(intent);
        newTeaActivityScenario.onActivity(newTea -> {

            Spinner spinnerVariety = newTea.findViewById(R.id.spinnerTeaVariety);
            CheckBox checkBoxSelfInput = newTea.findViewById(R.id.checkBoxSelfInput);
            EditText editTextSelfInput = newTea.findViewById(R.id.editTextSelfInput);

            assertThat(spinnerVariety.getSelectedItemPosition()).isEqualTo(9);
            assertThat(checkBoxSelfInput.isChecked()).isTrue();
            assertThat(editTextSelfInput.getText()).hasToString("OtherTea");
        });
    }


    @Test
    public void exitActivityAndExpectMainActivity() {
        mockSettings(CELSIUS);
        Tea tea = new Tea("Tea", "01_black", 1, "Gr", 1, 0, Date.from(getFixedDate()));
        tea.setId(1l);
        when(teaDao.getTeaById(1)).thenReturn(tea);

        List<Infusion> infusions = new ArrayList<>();
        Infusion infusion = new Infusion(1, 0, "", "", 100, 212);
        infusions.add(infusion);
        when(infusionDao.getInfusionsByTeaId(1)).thenReturn(infusions);


        Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), NewTea.class);
        intent.putExtra(TEA_ID, 1l);
        intent.putExtra("showTea", true);

        ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(intent);
        newTeaActivityScenario.onActivity(newTea -> {
            newTea.onOptionsItemSelected(new RoboMenuItem(android.R.id.home));

            Intent expected = new Intent(newTea, ShowTea.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

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

    private void inputInfusion(EditText editTextTemperature, EditText editTextTime,
                               EditText editTextCoolDownTime, String temperature, String time,
                               String coolDownTime) {
        editTextTemperature.setText(temperature);
        editTextTime.setText(time);
        editTextCoolDownTime.setText(coolDownTime);
    }

    private void checkInfusion(EditText editTextTemperature, EditText editTextTime,
                               EditText editTextCoolDownTime, String temperature,
                               String time, String coolDowntime) {
        assertThat(editTextTemperature.getText()).hasToString(temperature);
        assertThat(editTextTime.getText()).hasToString(time);
        assertThat(editTextCoolDownTime.getText()).hasToString(coolDowntime);
    }
}
