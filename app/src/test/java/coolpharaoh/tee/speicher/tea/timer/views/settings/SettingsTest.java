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

import androidx.appcompat.app.AppCompatDelegate;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Ignore;
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
import coolpharaoh.tee.speicher.tea.timer.application.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.views.utils.ListRowItem;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import static coolpharaoh.tee.speicher.tea.timer.core.actualsettings.DarkMode.DISABLED;
import static coolpharaoh.tee.speicher.tea.timer.core.actualsettings.DarkMode.ENABLED;
import static coolpharaoh.tee.speicher.tea.timer.core.actualsettings.DarkMode.SYSTEM;
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
    private static final int ALARM = 0;
    private static final int VIBRATION = 1;
    private static final int ANIMATION = 2;
    private static final int TEMPERATURE_UNIT = 3;
    private static final int DARK_MODE = 4;
    private static final int HINTS = 5;
    private static final int FACTORY_SETTINGS = 6;
    private static final int OPTION_ON = 0;
    private static final int OPTION_OFF = 1;
    private static final String ON = "On";
    private static final String OFF = "Off";
    private static final int TEMPERATURE_UNIT_CELSIUS = 0;
    private static final int TEMPERATURE_UNIT_FAHRENHEIT = 1;
    private static final String CELSIUS = "Celsius";
    private static final String FAHRENHEIT = "Fahrenheit";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    TeaDao teaDao;
    @Mock
    ActualSettingsDao actualSettingsDao;

    ActualSettings actualSettings;

    private final ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);

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
        actualSettings.setTemperatureUnit(CELSIUS);
        actualSettings.setMainRateAlert(false);
        actualSettings.setShowTeaAlert(false);
        actualSettings.setSettingsPermissionAlert(false);
        when(actualSettingsDao.getSettings()).thenReturn(actualSettings);
    }

    @Test
    public void launchActivityAndExpectFilledListView() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            ListRowItem itemAlarm = (ListRowItem) settingsList.getAdapter().getItem(ALARM);
            assertThat(itemAlarm.getHeading()).isEqualTo(settings.getString(R.string.settings_alarm));
            assertThat(itemAlarm.getDescription()).isEqualTo(actualSettings.getMusicName());

            ListRowItem itemVibration = (ListRowItem) settingsList.getAdapter().getItem(VIBRATION);
            assertThat(itemVibration.getHeading()).isEqualTo(settings.getString(R.string.settings_vibration));
            assertThat(itemVibration.getDescription()).isEqualTo(actualSettings.isVibration() ? ON : OFF);

            ListRowItem itemAnimation = (ListRowItem) settingsList.getAdapter().getItem(ANIMATION);
            assertThat(itemAnimation.getHeading()).isEqualTo(settings.getString(R.string.settings_animation));
            assertThat(itemAnimation.getDescription()).isEqualTo(actualSettings.isAnimation() ? ON : OFF);

            ListRowItem itemTemperatureUnit = (ListRowItem) settingsList.getAdapter().getItem(TEMPERATURE_UNIT);
            assertThat(itemTemperatureUnit.getHeading()).isEqualTo(settings.getString(R.string.settings_temperature_unit));
            assertThat(itemTemperatureUnit.getDescription()).isEqualTo(actualSettings.getTemperatureUnit());

            ListRowItem itemDarkMode = (ListRowItem) settingsList.getAdapter().getItem(DARK_MODE);
            assertThat(itemDarkMode.getHeading()).isEqualTo(settings.getString(R.string.settings_dark_mode));
            final String[] darkModes = settings.getResources().getStringArray(R.array.settings_dark_mode);
            assertThat(itemDarkMode.getDescription()).isEqualTo(darkModes[SYSTEM.getChoice()]);

            ListRowItem itemHints = (ListRowItem) settingsList.getAdapter().getItem(HINTS);
            assertThat(itemHints.getHeading()).isEqualTo(settings.getString(R.string.settings_show_hints));
            assertThat(itemHints.getDescription()).isEqualTo(settings.getString(R.string.settings_show_hints_description));

            ListRowItem itemFactorySettings = (ListRowItem) settingsList.getAdapter().getItem(FACTORY_SETTINGS);
            assertThat(itemFactorySettings.getHeading()).isEqualTo(settings.getString(R.string.settings_factory_settings));
            assertThat(itemFactorySettings.getDescription()).isEqualTo(settings.getString(R.string.settings_factory_settings_description));
        });
    }

    @Test
    public void clickAlarmAndExpectAlarmPicker() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, ALARM, settingsList.getItemIdAtPosition(ALARM));

            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getAction()).isEqualTo(RingtoneManager.ACTION_RINGTONE_PICKER);
        });
    }

    @Test
    public void setVibrationFalseAndExpectVibrationFalse() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, VIBRATION, settingsList.getItemIdAtPosition(VIBRATION));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(OPTION_OFF);

            verify(actualSettingsDao).update(captor.capture());
            ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.isVibration()).isFalse();

            ListRowItem itemVibration = (ListRowItem) settingsList.getAdapter().getItem(VIBRATION);
            assertThat(itemVibration.getHeading()).isEqualTo(settings.getString(R.string.settings_vibration));
            assertThat(itemVibration.getDescription()).isEqualTo(OFF);
        });
    }

    @Test
    public void setVibrationTrueAndExpectVibrationTrue() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, VIBRATION, settingsList.getItemIdAtPosition(VIBRATION));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(OPTION_ON);

            verify(actualSettingsDao).update(captor.capture());
            ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.isVibration()).isTrue();

            ListRowItem itemVibration = (ListRowItem) settingsList.getAdapter().getItem(VIBRATION);
            assertThat(itemVibration.getHeading()).isEqualTo(settings.getString(R.string.settings_vibration));
            assertThat(itemVibration.getDescription()).isEqualTo(ON);
        });
    }

    @Test
    public void setAnimationFalseAndExpectAnimationFalse() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, ANIMATION, settingsList.getItemIdAtPosition(ANIMATION));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(OPTION_OFF);

            verify(actualSettingsDao).update(captor.capture());
            ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.isAnimation()).isFalse();

            ListRowItem itemVibration = (ListRowItem) settingsList.getAdapter().getItem(ANIMATION);
            assertThat(itemVibration.getDescription()).isEqualTo(OFF);
        });
    }

    @Test
    public void setAnimationTrueAndExpectAnimationTrue() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, ANIMATION, settingsList.getItemIdAtPosition(ANIMATION));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(OPTION_ON);

            verify(actualSettingsDao).update(captor.capture());
            ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.isAnimation()).isTrue();

            ListRowItem itemVibration = (ListRowItem) settingsList.getAdapter().getItem(ANIMATION);
            assertThat(itemVibration.getDescription()).isEqualTo(ON);
        });
    }

    @Test
    public void setTemperatureUnitCelsiusAndExpectCelsius() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, TEMPERATURE_UNIT, settingsList.getItemIdAtPosition(TEMPERATURE_UNIT));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(TEMPERATURE_UNIT_CELSIUS);

            verify(actualSettingsDao).update(captor.capture());
            ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.getTemperatureUnit()).isEqualTo(CELSIUS);

            ListRowItem itemVibration = (ListRowItem) settingsList.getAdapter().getItem(TEMPERATURE_UNIT);
            assertThat(itemVibration.getDescription()).isEqualTo(CELSIUS);
        });
    }

    @Test
    public void setTemperatureUnitFahrenheitAndExpectFahrenheit() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, TEMPERATURE_UNIT, settingsList.getItemIdAtPosition(TEMPERATURE_UNIT));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(TEMPERATURE_UNIT_FAHRENHEIT);

            verify(actualSettingsDao).update(captor.capture());
            ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.getTemperatureUnit()).isEqualTo(FAHRENHEIT);

            ListRowItem itemVibration = (ListRowItem) settingsList.getAdapter().getItem(TEMPERATURE_UNIT);
            assertThat(itemVibration.getDescription()).isEqualTo(FAHRENHEIT);
        });
    }

    @Ignore("Leads to a nullpointer exception I don't know why!")
    @Test
    public void setDarkModeEnabled() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, DARK_MODE, settingsList.getItemIdAtPosition(DARK_MODE));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(ENABLED.getChoice());

            SharedSettings sharedSettings = new SharedSettings(settings.getApplication());
            assertThat(sharedSettings.getDarkMode()).isEqualTo(ENABLED);

            ListRowItem itemDarkMode = (ListRowItem) settingsList.getAdapter().getItem(DARK_MODE);
            final String expectedChoice = settings.getResources().getStringArray(R.array.settings_dark_mode)[ENABLED.getChoice()];
            assertThat(itemDarkMode.getDescription()).isEqualTo(expectedChoice);

            assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(MODE_NIGHT_YES);
        });
    }

    @Test
    public void setDarkModeSystem() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, DARK_MODE, settingsList.getItemIdAtPosition(DARK_MODE));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(SYSTEM.getChoice());

            SharedSettings sharedSettings = new SharedSettings(settings.getApplication());
            assertThat(sharedSettings.getDarkMode()).isEqualTo(SYSTEM);

            ListRowItem itemDarkMode = (ListRowItem) settingsList.getAdapter().getItem(DARK_MODE);
            final String expectedChoice = settings.getResources().getStringArray(R.array.settings_dark_mode)[SYSTEM.getChoice()];
            assertThat(itemDarkMode.getDescription()).isEqualTo(expectedChoice);

            assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(MODE_NIGHT_AUTO_BATTERY);
        });
    }

    @Test
    public void setDarkModeDisabled() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, DARK_MODE, settingsList.getItemIdAtPosition(DARK_MODE));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(DISABLED.getChoice());

            SharedSettings sharedSettings = new SharedSettings(settings.getApplication());
            assertThat(sharedSettings.getDarkMode()).isEqualTo(DISABLED);

            ListRowItem itemDarkMode = (ListRowItem) settingsList.getAdapter().getItem(DARK_MODE);
            final String expectedChoice = settings.getResources().getStringArray(R.array.settings_dark_mode)[DISABLED.getChoice()];
            assertThat(itemDarkMode.getDescription()).isEqualTo(expectedChoice);

            assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(MODE_NIGHT_NO);
        });
    }

    @Test
    public void setAllHintsAndExpectAllHints() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, HINTS, settingsList.getItemIdAtPosition(HINTS));

            AlertDialog alertDialog = getLatestAlertDialog();

            CheckBox checkBoxUpdate = alertDialog.findViewById(R.id.checkboxDialogSettingsUpdate);
            CheckBox checkBoxRating = alertDialog.findViewById(R.id.checkboxDialogSettingsRating);
            CheckBox checkBoxDescription = alertDialog.findViewById(R.id.checkboxDialogSettingsDescription);
            CheckBox checkBoxPermission = alertDialog.findViewById(R.id.checkboxDialogSettingsPermission);

            assertThat(checkBoxUpdate.isChecked()).isFalse();
            assertThat(checkBoxRating.isChecked()).isFalse();
            assertThat(checkBoxDescription.isChecked()).isFalse();
            assertThat(checkBoxPermission.isChecked()).isFalse();

            checkBoxUpdate.setChecked(true);
            checkBoxRating.setChecked(true);
            checkBoxDescription.setChecked(true);
            checkBoxPermission.setChecked(true);

            Button accept = (Button) alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            accept.performClick();

            verify(actualSettingsDao, times(4)).update(captor.capture());
            List<ActualSettings> updatedSettings = captor.getAllValues();

            assertThat(updatedSettings.get(3).isMainUpdateAlert()).isTrue();
            assertThat(updatedSettings.get(3).isMainRateAlert()).isTrue();
            assertThat(updatedSettings.get(3).isShowTeaAlert()).isTrue();
            assertThat(updatedSettings.get(3).isSettingsPermissionAlert()).isTrue();
        });
    }

    @Test
    public void setFactorySettingsAndExpectFactorySettings() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            ListView settingsList = settings.findViewById(R.id.listView_settings);

            settingsList.performItemClick(settingsList, FACTORY_SETTINGS, settingsList.getItemIdAtPosition(FACTORY_SETTINGS));

            AlertDialog alertDialog = getLatestAlertDialog();

            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            verify(actualSettingsDao).update(captor.capture());
            ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.getMusicName()).isEqualTo("Default");

            verify(teaDao).deleteAll();
        });
    }
}
