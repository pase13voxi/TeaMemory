package coolpharaoh.tee.speicher.tea.timer.views.settings;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

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
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.views.utils.ListRowItem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;


//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class SettingsTest {
    private enum ListItems {
        ALARM, VIBRATION, ANIMATION, TEMPERATURE_UNIT, HINTS, FACTORY_SETTINGS
    }

    private enum Options {
        On, Off
    }

    private enum TempartureUnit {
        Celsius, Fahrenheit
    }

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    TeaDao teaDao;
    @Mock
    ActualSettingsDao actualSettingsDao;

    ActualSettings actualSettings;

    @Before
    public void setUp() {
        mockDB();
    }

    private void mockDB() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDao);
        when(teaMemoryDatabase.getActualSettingsDao()).thenReturn(actualSettingsDao);

        actualSettings = new ActualSettings();
        actualSettings.setMusicChoice("musicChoice");
        actualSettings.setMusicName("MusicName");
        actualSettings.setVibration(true);
        actualSettings.setAnimation(true);
        actualSettings.setTemperatureUnit("Ts");
        actualSettings.setMainRateAlert(false);
        actualSettings.setShowTeaAlert(false);
        actualSettings.setSettingsPermissionAlert(false);
        when(actualSettingsDao.getSettings()).thenReturn(actualSettings);
    }

    // the cast is needed
    @SuppressWarnings("java:S1905")
    @Test
    public void launchActivityAndExpectFilledListView() {
        ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            ListRowItem itemAlarm = (ListRowItem) settingsList.getAdapter().getItem(ListItems.ALARM.ordinal());
            assertThat(itemAlarm.getHeading()).isEqualTo(settings.getString(R.string.settings_alarm));
            assertThat(itemAlarm.getDescription()).isEqualTo(actualSettings.getMusicName());

            ListRowItem itemVibration = (ListRowItem) settingsList.getAdapter().getItem(ListItems.VIBRATION.ordinal());
            assertThat(itemVibration.getHeading()).isEqualTo(settings.getString(R.string.settings_vibration));
            assertThat(itemVibration.getDescription()).isEqualTo(actualSettings.isVibration() ? Options.On.toString() : Options.Off.toString());

            ListRowItem itemAnimation = (ListRowItem) settingsList.getAdapter().getItem(ListItems.ANIMATION.ordinal());
            assertThat(itemAnimation.getHeading()).isEqualTo(settings.getString(R.string.settings_animation));
            assertThat(itemAnimation.getDescription()).isEqualTo(actualSettings.isAnimation() ? Options.On.toString() : Options.Off.toString());

            ListRowItem itemTemperatureUnit = (ListRowItem) settingsList.getAdapter().getItem(ListItems.TEMPERATURE_UNIT.ordinal());
            assertThat(itemTemperatureUnit.getHeading()).isEqualTo(settings.getString(R.string.settings_temperature_unit));
            assertThat(itemTemperatureUnit.getDescription()).isEqualTo(actualSettings.getTemperatureUnit());

            ListRowItem itemHints = (ListRowItem) settingsList.getAdapter().getItem(ListItems.HINTS.ordinal());
            assertThat(itemHints.getHeading()).isEqualTo(settings.getString(R.string.settings_show_hints));
            assertThat(itemHints.getDescription()).isEqualTo(settings.getString(R.string.settings_show_hints_description));

            ListRowItem itemFactorySettings = (ListRowItem) settingsList.getAdapter().getItem(ListItems.FACTORY_SETTINGS.ordinal());
            assertThat(itemFactorySettings.getHeading()).isEqualTo(settings.getString(R.string.settings_factory_settings));
            assertThat(itemFactorySettings.getDescription()).isEqualTo(settings.getString(R.string.settings_factory_settings_description));
        });
    }

    @Test
    public void clickAlarmAndExpectAlarmPicker() {
        ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, ListItems.ALARM.ordinal(), settingsList.getItemIdAtPosition(ListItems.ALARM.ordinal()));

            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getAction()).isEqualTo(RingtoneManager.ACTION_RINGTONE_PICKER);
        });
    }

    // the cast is needed
    @SuppressWarnings("java:S1905")
    @Test
    public void setVibrationFalseAndExpectVibrationFalse() {
        ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, ListItems.VIBRATION.ordinal(), settingsList.getItemIdAtPosition(ListItems.VIBRATION.ordinal()));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(Options.Off.ordinal());

            ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
            verify(actualSettingsDao).update(captor.capture());
            ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.isVibration()).isFalse();

            ListRowItem itemVibration = (ListRowItem) settingsList.getAdapter().getItem(ListItems.VIBRATION.ordinal());
            assertThat(itemVibration.getHeading()).isEqualTo(settings.getString(R.string.settings_vibration));
            assertThat(itemVibration.getDescription()).isEqualTo(Options.Off.toString());
        });
    }

    // the cast is needed
    @SuppressWarnings("java:S1905")
    @Test
    public void setVibrationTrueAndExpectVibrationTrue() {
        ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, ListItems.VIBRATION.ordinal(), settingsList.getItemIdAtPosition(ListItems.VIBRATION.ordinal()));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(Options.On.ordinal());

            ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
            verify(actualSettingsDao).update(captor.capture());
            ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.isVibration()).isTrue();

            ListRowItem itemVibration = (ListRowItem) settingsList.getAdapter().getItem(ListItems.VIBRATION.ordinal());
            assertThat(itemVibration.getHeading()).isEqualTo(settings.getString(R.string.settings_vibration));
            assertThat(itemVibration.getDescription()).isEqualTo(Options.On.toString());
        });
    }

    // the cast is needed
    @SuppressWarnings("java:S1905")
    @Test
    public void setAnimationFalseAndExpectAnimationFalse() {
        ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, ListItems.ANIMATION.ordinal(), settingsList.getItemIdAtPosition(ListItems.ANIMATION.ordinal()));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(Options.Off.ordinal());

            ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
            verify(actualSettingsDao).update(captor.capture());
            ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.isAnimation()).isFalse();

            ListRowItem itemVibration = (ListRowItem) settingsList.getAdapter().getItem(ListItems.ANIMATION.ordinal());
            assertThat(itemVibration.getDescription()).isEqualTo(Options.Off.toString());
        });
    }

    // the cast is needed
    @SuppressWarnings("java:S1905")
    @Test
    public void setAnimationTrueAndExpectAnimationTrue() {
        ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, ListItems.ANIMATION.ordinal(), settingsList.getItemIdAtPosition(ListItems.ANIMATION.ordinal()));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(Options.On.ordinal());

            ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
            verify(actualSettingsDao).update(captor.capture());
            ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.isAnimation()).isTrue();

            ListRowItem itemVibration = (ListRowItem) settingsList.getAdapter().getItem(ListItems.ANIMATION.ordinal());
            assertThat(itemVibration.getDescription()).isEqualTo(Options.On.toString());
        });
    }

    // the cast is needed
    @SuppressWarnings("java:S1905")
    @Test
    public void setTemperatureUnitCelsiusAndExpectCelsius() {
        ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, ListItems.TEMPERATURE_UNIT.ordinal(), settingsList.getItemIdAtPosition(ListItems.TEMPERATURE_UNIT.ordinal()));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(TempartureUnit.Celsius.ordinal());

            ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
            verify(actualSettingsDao).update(captor.capture());
            ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.getTemperatureUnit()).isEqualTo(TempartureUnit.Celsius.toString());

            ListRowItem itemVibration = (ListRowItem) settingsList.getAdapter().getItem(ListItems.TEMPERATURE_UNIT.ordinal());
            assertThat(itemVibration.getDescription()).isEqualTo(TempartureUnit.Celsius.toString());
        });
    }

    // the cast is needed
    @SuppressWarnings("java:S1905")
    @Test
    public void setTemperatureUnitFahrenheitAndExpectFahrenheit() {
        ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, ListItems.TEMPERATURE_UNIT.ordinal(), settingsList.getItemIdAtPosition(ListItems.TEMPERATURE_UNIT.ordinal()));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(TempartureUnit.Fahrenheit.ordinal());

            ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
            verify(actualSettingsDao).update(captor.capture());
            ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.getTemperatureUnit()).isEqualTo(TempartureUnit.Fahrenheit.toString());

            ListRowItem itemVibration = (ListRowItem) settingsList.getAdapter().getItem(ListItems.TEMPERATURE_UNIT.ordinal());
            assertThat(itemVibration.getDescription()).isEqualTo(TempartureUnit.Fahrenheit.toString());
        });
    }

    // the cast is needed
    @SuppressWarnings("java:S1905")
    @Test
    public void setAllHintsAndExpectAllHints() {
        ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, ListItems.HINTS.ordinal(), settingsList.getItemIdAtPosition(ListItems.HINTS.ordinal()));

            AlertDialog alertDialog = getLatestAlertDialog();

            CheckBox checkBoxRating = alertDialog.findViewById(R.id.checkboxDialogSettingsRating);
            CheckBox checkBoxDescription = alertDialog.findViewById(R.id.checkboxDialogSettingsDescription);
            CheckBox checkBoxPermission = alertDialog.findViewById(R.id.checkboxDialogSettingsPermission);

            assertThat(checkBoxRating.isChecked()).isFalse();
            assertThat(checkBoxDescription.isChecked()).isFalse();
            assertThat(checkBoxPermission.isChecked()).isFalse();

            checkBoxRating.setChecked(true);
            checkBoxDescription.setChecked(true);
            checkBoxPermission.setChecked(true);

            Button accept = (Button) alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            accept.performClick();

            ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
            verify(actualSettingsDao, times(3)).update(captor.capture());
            List<ActualSettings> updatedSettings = captor.getAllValues();

            assertThat(updatedSettings.get(2).isMainRateAlert()).isTrue();
            assertThat(updatedSettings.get(2).isShowTeaAlert()).isTrue();
            assertThat(updatedSettings.get(2).isSettingsPermissionAlert()).isTrue();
        });
    }

    @Test
    public void setFactorySettingsAndExpectFactorySettings() {
        ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, ListItems.FACTORY_SETTINGS.ordinal(), settingsList.getItemIdAtPosition(ListItems.FACTORY_SETTINGS.ordinal()));

            AlertDialog alertDialog = getLatestAlertDialog();

            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
            verify(actualSettingsDao).update(captor.capture());
            ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.getMusicName()).isEqualTo("Default");

            verify(teaDao).deleteAll();
        });
    }
}
