package coolpharaoh.tee.speicher.tea.timer.views.settings;

import static android.os.Looper.getMainLooper;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.DarkMode.DISABLED;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.DarkMode.ENABLED;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.DarkMode.SYSTEM;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit.CELSIUS;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit.FAHRENHEIT;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;
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
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class SettingsTest {
    private static final int ALARM = 0;
    private static final int VIBRATION = 1;
    private static final int ANIMATION = 2;
    private static final int TEMPERATURE_UNIT = 3;
    private static final int OVERVIEW_HEADER = 4;
    private static final int DARK_MODE = 5;
    private static final int HINTS = 6;
    private static final int FACTORY_SETTINGS = 7;
    private static final int OPTION_ON = 0;
    private static final int OPTION_OFF = 1;
    private static final String ON = "On";
    private static final String OFF = "Off";

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
        actualSettings.setTemperatureUnit(CELSIUS.getText());
        actualSettings.setMainRateAlert(false);
        actualSettings.setShowTeaAlert(false);
        actualSettings.setSettingsPermissionAlert(false);
        when(actualSettingsDao.getSettings()).thenReturn(actualSettings);
    }

    @Test
    public void launchActivityAndExpectFilledListView() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            final RecyclerView settingsRecyclerView = settings.findViewById(R.id.recycler_view_settings);

            scrollToPosition(settingsRecyclerView, ALARM);
            checkHeadingAtPosition(settingsRecyclerView, ALARM, settings.getString(R.string.settings_alarm));
            checkDescriptionAtPosition(settingsRecyclerView, ALARM, actualSettings.getMusicName());

            scrollToPosition(settingsRecyclerView, VIBRATION);
            checkHeadingAtPosition(settingsRecyclerView, VIBRATION, settings.getString(R.string.settings_vibration));
            checkDescriptionAtPosition(settingsRecyclerView, VIBRATION, actualSettings.isVibration() ? ON : OFF);

            scrollToPosition(settingsRecyclerView, ANIMATION);
            checkHeadingAtPosition(settingsRecyclerView, ANIMATION, settings.getString(R.string.settings_animation));
            checkDescriptionAtPosition(settingsRecyclerView, ANIMATION, actualSettings.isAnimation() ? ON : OFF);

            scrollToPosition(settingsRecyclerView, TEMPERATURE_UNIT);
            checkHeadingAtPosition(settingsRecyclerView, TEMPERATURE_UNIT, settings.getString(R.string.settings_temperature_unit));
            checkDescriptionAtPosition(settingsRecyclerView, TEMPERATURE_UNIT, actualSettings.getTemperatureUnit());

            scrollToPosition(settingsRecyclerView, OVERVIEW_HEADER);
            checkHeadingAtPosition(settingsRecyclerView, OVERVIEW_HEADER, settings.getString(R.string.settings_overview_header));
            checkDescriptionAtPosition(settingsRecyclerView, OVERVIEW_HEADER, OFF);

            scrollToPosition(settingsRecyclerView, DARK_MODE);
            final String[] darkModes = settings.getResources().getStringArray(R.array.settings_dark_mode);
            checkHeadingAtPosition(settingsRecyclerView, DARK_MODE, settings.getString(R.string.settings_dark_mode));
            checkDescriptionAtPosition(settingsRecyclerView, DARK_MODE, darkModes[SYSTEM.getChoice()]);

            scrollToPosition(settingsRecyclerView, HINTS);
            checkHeadingAtPosition(settingsRecyclerView, HINTS, settings.getString(R.string.settings_show_hints));
            checkDescriptionAtPosition(settingsRecyclerView, HINTS, settings.getString(R.string.settings_show_hints_description));

            scrollToPosition(settingsRecyclerView, FACTORY_SETTINGS);
            checkHeadingAtPosition(settingsRecyclerView, FACTORY_SETTINGS, settings.getString(R.string.settings_factory_settings));
            checkDescriptionAtPosition(settingsRecyclerView, FACTORY_SETTINGS, settings.getString(R.string.settings_factory_settings_description));
        });
    }

    @Test
    public void clickAlarmAndExpectAlarmPicker() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            final RecyclerView settingsRecyclerView = settings.findViewById(R.id.recycler_view_settings);

            clickAtPositionRecyclerView(settingsRecyclerView, ALARM);

            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getAction()).isEqualTo(RingtoneManager.ACTION_RINGTONE_PICKER);
        });
    }

    @Test
    public void setVibrationFalseAndExpectVibrationFalse() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            final RecyclerView settingsRecyclerView = settings.findViewById(R.id.recycler_view_settings);

            clickAtPositionRecyclerView(settingsRecyclerView, VIBRATION);

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(OPTION_OFF);
            shadowOf(getMainLooper()).idle();

            verify(actualSettingsDao).update(captor.capture());
            final ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.isVibration()).isFalse();

            checkDescriptionAtPosition(settingsRecyclerView, VIBRATION, OFF);
        });
    }

    @Test
    public void setVibrationTrueAndExpectVibrationTrue() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            final RecyclerView settingsRecyclerView = settings.findViewById(R.id.recycler_view_settings);

            clickAtPositionRecyclerView(settingsRecyclerView, VIBRATION);

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(OPTION_ON);
            shadowOf(getMainLooper()).idle();

            verify(actualSettingsDao).update(captor.capture());
            final ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.isVibration()).isTrue();

            checkDescriptionAtPosition(settingsRecyclerView, VIBRATION, ON);
        });
    }

    @Test
    public void setAnimationFalseAndExpectAnimationFalse() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            final RecyclerView settingsRecyclerView = settings.findViewById(R.id.recycler_view_settings);

            clickAtPositionRecyclerView(settingsRecyclerView, ANIMATION);

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(OPTION_OFF);
            shadowOf(getMainLooper()).idle();

            verify(actualSettingsDao).update(captor.capture());
            final ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.isAnimation()).isFalse();

            checkDescriptionAtPosition(settingsRecyclerView, ANIMATION, OFF);
        });
    }

    @Test
    public void setAnimationTrueAndExpectAnimationTrue() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            final RecyclerView settingsRecyclerView = settings.findViewById(R.id.recycler_view_settings);

            clickAtPositionRecyclerView(settingsRecyclerView, ANIMATION);

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(OPTION_ON);
            shadowOf(getMainLooper()).idle();

            verify(actualSettingsDao).update(captor.capture());
            final ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.isAnimation()).isTrue();

            checkDescriptionAtPosition(settingsRecyclerView, ANIMATION, ON);
        });
    }

    @Test
    public void setTemperatureUnitCelsiusAndExpectCelsius() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            final RecyclerView settingsRecyclerView = settings.findViewById(R.id.recycler_view_settings);

            clickAtPositionRecyclerView(settingsRecyclerView, TEMPERATURE_UNIT);

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(CELSIUS.getChoice());
            shadowOf(getMainLooper()).idle();

            verify(actualSettingsDao).update(captor.capture());
            final ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.getTemperatureUnit()).isEqualTo(CELSIUS.getText());

            checkDescriptionAtPosition(settingsRecyclerView, TEMPERATURE_UNIT, CELSIUS.getText());
        });
    }

    @Test
    public void setTemperatureUnitFahrenheitAndExpectFahrenheit() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            final RecyclerView settingsRecyclerView = settings.findViewById(R.id.recycler_view_settings);

            clickAtPositionRecyclerView(settingsRecyclerView, TEMPERATURE_UNIT);

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(FAHRENHEIT.getChoice());
            shadowOf(getMainLooper()).idle();

            verify(actualSettingsDao).update(captor.capture());
            final ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.getTemperatureUnit()).isEqualTo(FAHRENHEIT.getText());

            checkDescriptionAtPosition(settingsRecyclerView, TEMPERATURE_UNIT, FAHRENHEIT.getText());
        });
    }

    @Test
    public void setOverviewHeaderFalseAndExpectOverviewHeaderFalse() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            final SharedSettings sharedSettings = new SharedSettings(settings.getApplication());
            sharedSettings.setOverviewHeader(true);

            final RecyclerView settingsRecyclerView = settings.findViewById(R.id.recycler_view_settings);

            clickAtPositionRecyclerView(settingsRecyclerView, OVERVIEW_HEADER);

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(OPTION_OFF);
            shadowOf(getMainLooper()).idle();

            assertThat(sharedSettings.isOverviewHeader()).isFalse();

            checkDescriptionAtPosition(settingsRecyclerView, OVERVIEW_HEADER, OFF);
        });
    }

    @Test
    public void setOverviewHeaderTrueAndExpectOverviewHeaderTrue() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            final SharedSettings sharedSettings = new SharedSettings(settings.getApplication());
            sharedSettings.setOverviewHeader(false);

            final RecyclerView settingsRecyclerView = settings.findViewById(R.id.recycler_view_settings);

            clickAtPositionRecyclerView(settingsRecyclerView, OVERVIEW_HEADER);

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(OPTION_ON);
            shadowOf(getMainLooper()).idle();

            assertThat(sharedSettings.isOverviewHeader()).isTrue();

            checkDescriptionAtPosition(settingsRecyclerView, OVERVIEW_HEADER, ON);
        });
    }

    @Ignore("Leads to a null pointer exception I don't know why!")
    @Test
    public void setDarkModeEnabled() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            final RecyclerView settingsRecyclerView = settings.findViewById(R.id.recycler_view_settings);

            clickAtPositionRecyclerView(settingsRecyclerView, DARK_MODE);

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(ENABLED.getChoice());

            final SharedSettings sharedSettings = new SharedSettings(settings.getApplication());
            assertThat(sharedSettings.getDarkMode()).isEqualTo(ENABLED);

            final String expectedChoice = settings.getResources().getStringArray(R.array.settings_dark_mode)[ENABLED.getChoice()];
            checkDescriptionAtPosition(settingsRecyclerView, DARK_MODE, expectedChoice);

            assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(MODE_NIGHT_YES);
        });
    }

    @Test
    public void setDarkModeSystem() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            final RecyclerView settingsRecyclerView = settings.findViewById(R.id.recycler_view_settings);

            clickAtPositionRecyclerView(settingsRecyclerView, DARK_MODE);

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(SYSTEM.getChoice());
            shadowOf(getMainLooper()).idle();

            final SharedSettings sharedSettings = new SharedSettings(settings.getApplication());
            assertThat(sharedSettings.getDarkMode()).isEqualTo(SYSTEM);

            final String expectedChoice = settings.getResources().getStringArray(R.array.settings_dark_mode)[SYSTEM.getChoice()];
            checkDescriptionAtPosition(settingsRecyclerView, DARK_MODE, expectedChoice);

            assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(MODE_NIGHT_AUTO_BATTERY);
        });
    }

    @Test
    public void setDarkModeDisabled() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            final RecyclerView settingsRecyclerView = settings.findViewById(R.id.recycler_view_settings);

            clickAtPositionRecyclerView(settingsRecyclerView, DARK_MODE);

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(DISABLED.getChoice());
            shadowOf(getMainLooper()).idle();

            final SharedSettings sharedSettings = new SharedSettings(settings.getApplication());
            assertThat(sharedSettings.getDarkMode()).isEqualTo(DISABLED);

            final String expectedChoice = settings.getResources().getStringArray(R.array.settings_dark_mode)[DISABLED.getChoice()];
            checkDescriptionAtPosition(settingsRecyclerView, DARK_MODE, expectedChoice);

            assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(MODE_NIGHT_NO);
        });
    }

    @Test
    public void setAllHintsAndExpectAllHints() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            final RecyclerView settingsRecyclerView = settings.findViewById(R.id.recycler_view_settings);

            clickAtPositionRecyclerView(settingsRecyclerView, HINTS);

            final AlertDialog alertDialog = getLatestAlertDialog();

            final CheckBox checkBoxUpdate = alertDialog.findViewById(R.id.check_box_settings_dialog_update);
            final CheckBox checkBoxDescription = alertDialog.findViewById(R.id.check_box_settings_dialog_description);
            final CheckBox checkBoxPermission = alertDialog.findViewById(R.id.check_box_settings_dialog_permission);

            assertThat(checkBoxUpdate.isChecked()).isFalse();
            assertThat(checkBoxDescription.isChecked()).isFalse();
            assertThat(checkBoxPermission.isChecked()).isFalse();

            checkBoxUpdate.setChecked(true);
            checkBoxDescription.setChecked(true);
            checkBoxPermission.setChecked(true);

            final Button accept = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            accept.performClick();
            shadowOf(getMainLooper()).idle();

            verify(actualSettingsDao, times(3)).update(captor.capture());
            final List<ActualSettings> updatedSettings = captor.getAllValues();

            assertThat(updatedSettings.get(2).isMainUpdateAlert()).isTrue();
            assertThat(updatedSettings.get(2).isShowTeaAlert()).isTrue();
            assertThat(updatedSettings.get(2).isSettingsPermissionAlert()).isTrue();
        });
    }

    @Test
    public void setFactorySettingsAndExpectFactorySettings() {
        final ActivityScenario<Settings> settingsActivityScenario = ActivityScenario.launch(Settings.class);
        settingsActivityScenario.onActivity(settings -> {
            final RecyclerView settingsRecyclerView = settings.findViewById(R.id.recycler_view_settings);

            clickAtPositionRecyclerView(settingsRecyclerView, FACTORY_SETTINGS);

            final AlertDialog alertDialog = getLatestAlertDialog();

            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            verify(actualSettingsDao).update(captor.capture());
            final ActualSettings updatedSettings = captor.getValue();
            assertThat(updatedSettings.getMusicName()).isEqualTo("Default");

            verify(teaDao).deleteAll();
        });
    }

    private void clickAtPositionRecyclerView(final RecyclerView recyclerView, final int position) {
        scrollToPosition(recyclerView, position);
        final View itemView = recyclerView.findViewHolderForAdapterPosition(position).itemView;
        itemView.performClick();
    }

    private void scrollToPosition(final RecyclerView settingsRecyclerView, final int alarm) {
        settingsRecyclerView.scrollToPosition(alarm);
        shadowOf(getMainLooper()).idle();
    }

    private void checkHeadingAtPosition(final RecyclerView recyclerView, final int position, final String heading) {
        checkViewAtPositionInRecyclerView(recyclerView, position, R.id.text_view_recycler_view_heading, heading);
    }

    private void checkDescriptionAtPosition(final RecyclerView recyclerView, final int position, final String description) {
        checkViewAtPositionInRecyclerView(recyclerView, position, R.id.text_view_recycler_view_description, description);
    }

    private void checkViewAtPositionInRecyclerView(final RecyclerView recyclerView, final int position,
                                                   final int viewId, final String toCheck) {
        final View itemView = recyclerView.findViewHolderForAdapterPosition(position).itemView;
        final TextView textViewHeading = itemView.findViewById(viewId);
        assertThat(textViewHeading.getText()).hasToString(toCheck);
    }
}
