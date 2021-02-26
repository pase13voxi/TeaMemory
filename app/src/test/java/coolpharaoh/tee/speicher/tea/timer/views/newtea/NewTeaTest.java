package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.View;
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
import coolpharaoh.tee.speicher.tea.timer.core.language.LanguageConversation;
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

    private static final String CURRENT_DATE = "2020-08-19T10:15:30Z";
    private static final String CELSIUS = "Celsius";
    private static final String FIRST_INFUSION = "1. Infusion";
    private static final String SECOND_INFUSION = "2. Infusion";
    private static final String FAHRENHEIT = "Fahrenheit";
    private static final String TEA_ID = "teaId";
    private static final String SHOW_TEA_FLAG = "showTea";
    private static final String CODE_GREEN_TEA = "02_green";

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
            assertThat(editTextTemperature.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_temperature_empty_text_celsius));
            assertThat(editTextCoolDownTime.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_cool_down_time_empty_text));
            assertThat(editTextTime.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_time_empty_text));
        });
    }

    @Test
    public void showActivityAddModeAndExpectFilledDefaultValuesFahrenheit() {
        mockSettings(FAHRENHEIT);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextTemperature = newTea.findViewById(R.id.edit_text_new_tea_temperature);
            assertThat(editTextTemperature.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_temperature_empty_text_fahrenheit));
        });
    }

    @Test
    public void addNewTeaAndExpectSavedDefaultValues() {
        mockSettings(CELSIUS);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextName = newTea.findViewById(R.id.edit_text_new_tea_name);
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
    public void addVarietyAndExpectUpdatedVariety() {
        mockSettings(CELSIUS);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextVariety = newTea.findViewById(R.id.edit_text_new_tea_variety);
            editTextVariety.performClick();

            final AlertDialog dialog = getLatestAlertDialog();

            final List<RadioButton> radioButtons = getRadioButtons(dialog);

            radioButtons.get(3).performClick();

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            final String[] varieties = newTea.getResources().getStringArray(R.array.new_tea_variety_teas);
            assertThat(editTextVariety.getText()).hasToString(varieties[3]);
        });
    }

    @Test
    public void addAmountAndExpectUpdatedAmount() {
        mockSettings(CELSIUS);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextAmount = newTea.findViewById(R.id.edit_text_new_tea_amount);
            editTextAmount.performClick();

            final AlertDialog dialog = getLatestAlertDialog();

            final NumberPicker amountPicker = dialog.findViewById(R.id.new_tea_number_picker_dialog_amount);
            amountPicker.setValue(7);

            final NumberPicker amountKindPicker = dialog.findViewById(R.id.new_tea_number_picker_dialog_amount_kind);
            amountKindPicker.setValue(0);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            assertThat(editTextAmount.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_amount_text_ts, 7));
        });
    }

    @Test
    public void addTemperatureAndExpectUpdatedTemperature() {
        mockSettings(CELSIUS);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextTemperature = newTea.findViewById(R.id.edit_text_new_tea_temperature);
            editTextTemperature.performClick();

            final AlertDialog dialog = getLatestAlertDialog();

            final NumberPicker numberPickerTemperature = dialog.findViewById(R.id.new_tea_number_picker_dialog_temperature);
            numberPickerTemperature.setValue(80);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            assertThat(editTextTemperature.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_temperature_text_celsius, 80));
        });
    }

    @Test
    public void showCoolDownTimeWhenTemperatureIsLessThan100Celsius() {
        mockSettings(CELSIUS);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextTemperature = newTea.findViewById(R.id.edit_text_new_tea_temperature);
            editTextTemperature.performClick();

            final AlertDialog dialog = getLatestAlertDialog();

            final NumberPicker numberPickerTemperature = dialog.findViewById(R.id.new_tea_number_picker_dialog_temperature);
            numberPickerTemperature.setValue(80);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            final EditText editTextCoolDownTime = newTea.findViewById(R.id.edit_text_new_tea_cool_down_time);

            assertThat(editTextCoolDownTime.getVisibility()).isEqualTo(View.VISIBLE);
        });
    }

    @Test
    public void hideCoolDownTimeWhenTemperatureIs100Celsius() {
        mockSettings(FAHRENHEIT);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextTemperature = newTea.findViewById(R.id.edit_text_new_tea_temperature);
            editTextTemperature.performClick();

            final AlertDialog dialog = getLatestAlertDialog();

            final NumberPicker numberPickerTemperature = dialog.findViewById(R.id.new_tea_number_picker_dialog_temperature);
            numberPickerTemperature.setValue(212);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            final LinearLayout layoutCoolDownTime = newTea.findViewById(R.id.layout_new_tea_cool_down_time);

            assertThat(layoutCoolDownTime.getVisibility()).isEqualTo(View.GONE);
        });
    }

    @Test
    public void addCoolDownTimeAndExpectUpdatedCoolDownTime() {
        mockSettings(CELSIUS);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextCoolDownTime = newTea.findViewById(R.id.edit_text_new_tea_cool_down_time);
            editTextCoolDownTime.performClick();

            final AlertDialog dialog = getLatestAlertDialog();

            final NumberPicker numberPickerTimeMinutes = dialog.findViewById(R.id.new_tea_number_picker_dialog_time_minutes);
            numberPickerTimeMinutes.setValue(5);

            final NumberPicker numberPickerTimeSeconds = dialog.findViewById(R.id.new_tea_number_picker_dialog_time_seconds);
            numberPickerTimeSeconds.setValue(45);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            assertThat(editTextCoolDownTime.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_cool_down_time_text, "05:45"));
        });
    }

    @Test
    public void addTimeAndExpectUpdatedTime() {
        mockSettings(CELSIUS);
        final ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            final EditText editTextTime = newTea.findViewById(R.id.edit_text_new_tea_time);
            editTextTime.performClick();

            final AlertDialog dialog = getLatestAlertDialog();

            final NumberPicker numberPickerTimeMinutes = dialog.findViewById(R.id.new_tea_number_picker_dialog_time_minutes);
            numberPickerTimeMinutes.setValue(5);

            final NumberPicker numberPickerTimeSeconds = dialog.findViewById(R.id.new_tea_number_picker_dialog_time_seconds);
            numberPickerTimeSeconds.setValue(45);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            assertThat(editTextTime.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_time_text, "05:45"));
        });
    }

    @Test
    public void showActivityEditModeAndExpectFilledFields() {
        mockSettings(FAHRENHEIT);
        final Tea tea = new Tea("Tea", CODE_GREEN_TEA, 1, "Ts", 234, 0, Date.from(getFixedDate()));
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

            assertThat(editTextVariety.getText()).hasToString(LanguageConversation.convertCodeToVariety(tea.getVariety(), newTea.getApplication()));
            assertThat(editTextName.getText()).hasToString(tea.getName());
            assertThat(editTextAmount.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_amount_text_ts, tea.getAmount()));
            assertThat(editTextTemperature.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_temperature_text_fahrenheit, infusions.get(0).getTemperatureFahrenheit()));
            assertThat(editTextTime.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_time_text, infusions.get(0).getTime()));
            assertThat(editTextCoolDownTime.getText()).hasToString(newTea.getString(R.string.new_tea_edit_text_cool_down_time_text, infusions.get(0).getCoolDownTime()));
        });
    }

    @Test
    public void editTeaAndExpectEditedTea() {
        mockSettings(FAHRENHEIT);
        final Tea tea = new Tea("Tea", CODE_GREEN_TEA, 1, "Ts", 234, 0, Date.from(getFixedDate()));
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
            final ImageButton buttonLeft = newTea.findViewById(R.id.new_tea_button_previous_infusion);
            final ImageButton buttonRight = newTea.findViewById(R.id.new_tea_button_next_infusion);
            final ImageButton buttonDelete = newTea.findViewById(R.id.new_tea_button_delete_infusion);
            final ImageButton buttonAddInfusion = newTea.findViewById(R.id.new_tea_button_add_infusion);
            final TextView textViewInfunsionBar = newTea.findViewById(R.id.new_tea_text_view_count_infusion);

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
        final Tea tea = new Tea("Tea", CODE_GREEN_TEA, 1, "Ts", 234, 0, Date.from(getFixedDate()));
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
            final ImageButton buttonLeft = newTea.findViewById(R.id.new_tea_button_previous_infusion);
            final ImageButton buttonRight = newTea.findViewById(R.id.new_tea_button_next_infusion);
            final ImageButton buttonDeleteInfusion = newTea.findViewById(R.id.new_tea_button_delete_infusion);
            final ImageButton buttonAddInfusion = newTea.findViewById(R.id.new_tea_button_add_infusion);
            final TextView textViewInfusionBar = newTea.findViewById(R.id.new_tea_text_view_count_infusion);

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
        final Tea tea = new Tea("Tea", CODE_GREEN_TEA, 1, "Ts", 234, 0, Date.from(getFixedDate()));
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
            final ImageButton buttonNextInfusion = newTea.findViewById(R.id.new_tea_button_next_infusion);
            final ImageButton buttonDeleteInfusion = newTea.findViewById(R.id.new_tea_button_delete_infusion);
            final TextView textViewInfusionBar = newTea.findViewById(R.id.new_tea_text_view_count_infusion);

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
        mockSettings(FAHRENHEIT);
        final Tea tea = new Tea("Tea", CODE_GREEN_TEA, 1, "Ts", 234, 0, Date.from(getFixedDate()));
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

    private List<RadioButton> getRadioButtons(AlertDialog dialog) {
        final RadioGroup radioGroup = dialog.findViewById(R.id.new_tea_radio_group_variety_input);
        final ArrayList<RadioButton> listRadioButtons = new ArrayList<>();
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View o = radioGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                listRadioButtons.add((RadioButton) o);
            }
        }
        return listRadioButtons;
    }
}
