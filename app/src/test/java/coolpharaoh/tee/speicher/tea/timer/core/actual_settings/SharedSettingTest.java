package coolpharaoh.tee.speicher.tea.timer.core.actual_settings;

import static org.assertj.core.api.Assertions.assertThat;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.DarkMode.DISABLED;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.DarkMode.SYSTEM;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SortMode.ALPHABETICAL;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SortMode.LAST_USED;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit.CELSIUS;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit.FAHRENHEIT;

import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class SharedSettingTest {

    @Test
    public void isFirstStartIsTrue() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        assertThat(sharedSettings.isFirstStart()).isTrue();
    }

    @Test
    public void setFirstStartFalse() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setFirstStart(false);

        assertThat(sharedSettings.isFirstStart()).isFalse();
    }

    @Test
    public void getMusicChoiceIsDefault() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        assertThat(sharedSettings.getMusicChoice()).isNull();
    }

    @Test
    public void setMusicChoice() {
        final String musicChoice = "MUSIC_CHOICE";
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setMusicChoice(musicChoice);

        assertThat(sharedSettings.getMusicChoice()).isEqualTo(musicChoice);
    }

    @Test
    public void getMusicNameIsDefault() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        assertThat(sharedSettings.getMusicName()).isNull();
    }

    @Test
    public void setMusicName() {
        final String musicName = "MUSIC_NAME";
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setMusicName(musicName);

        assertThat(sharedSettings.getMusicName()).isEqualTo(musicName);
    }

    @Test
    public void isVibrationIsTrue() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        assertThat(sharedSettings.isVibration()).isTrue();
    }

    @Test
    public void setVibrationFalse() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setVibration(false);

        assertThat(sharedSettings.isVibration()).isFalse();
    }

    @Test
    public void isAnimationIsTrue() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        assertThat(sharedSettings.isAnimation()).isTrue();
    }

    @Test
    public void setAnimationFalse() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setAnimation(false);

        assertThat(sharedSettings.isAnimation()).isFalse();
    }

    @Test
    public void getTemperatureUnitIsCelsius() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        assertThat(sharedSettings.getTemperatureUnit()).isEqualTo(CELSIUS);
    }

    @Test
    public void setTemperatureFahrenheit() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setTemperatureUnit(FAHRENHEIT);

        assertThat(sharedSettings.getTemperatureUnit()).isEqualTo(FAHRENHEIT);
    }

    @Test
    public void getDarkModeIsSystem() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        assertThat(sharedSettings.getDarkMode()).isEqualTo(SYSTEM);
    }

    @Test
    public void setDarkModeDisabled() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setDarkMode(DISABLED);

        assertThat(sharedSettings.getDarkMode()).isEqualTo(DISABLED);
    }

    @Test
    public void isOverviewUpdateAlertIsFalse() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        assertThat(sharedSettings.isOverviewUpdateAlert()).isFalse();
    }

    @Test
    public void setOverviewUpdateAlertTrue() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setOverviewUpdateAlert(true);

        assertThat(sharedSettings.isOverviewUpdateAlert()).isTrue();
    }

    @Test
    public void isShowTeaAlertIsFalse() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        assertThat(sharedSettings.isShowTeaAlert()).isFalse();
    }

    @Test
    public void setShowTeaAlertTrue() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setShowTeaAlert(true);

        assertThat(sharedSettings.isShowTeaAlert()).isTrue();
    }

    @Test
    public void isSettingsPermissionAlertIsFalse() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        assertThat(sharedSettings.isSettingsPermissionAlert()).isFalse();
    }

    @Test
    public void setSettingsPermissionAlertTrue() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setSettingsPermissionAlert(true);

        assertThat(sharedSettings.isSettingsPermissionAlert()).isTrue();
    }

    @Test
    public void getSortModeIsLastUsed() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        assertThat(sharedSettings.getSortMode()).isEqualTo(LAST_USED);
    }

    @Test
    public void setSortModeAlphabetical() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setSortMode(ALPHABETICAL);

        assertThat(sharedSettings.getSortMode()).isEqualTo(ALPHABETICAL);
    }

    @Test
    public void isOverviewHeaderIsFalse() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        assertThat(sharedSettings.isOverviewHeader()).isFalse();
    }

    @Test
    public void setOverviewHeaderTrue() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setOverviewHeader(true);

        assertThat(sharedSettings.isOverviewHeader()).isTrue();
    }

    @Test
    public void isOverviewInStockIsFalse() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        assertThat(sharedSettings.isOverviewInStock()).isFalse();
    }

    @Test
    public void setOverviewInStockTrue() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setOverviewInStock(true);

        assertThat(sharedSettings.isOverviewInStock()).isTrue();
    }

    @Test
    public void setFactorySettings() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setFactorySettings();

        assertThat(sharedSettings.isFirstStart()).isFalse();
        assertThat(sharedSettings.getMusicChoice()).isEqualTo("content://settings/system/ringtone");
        assertThat(sharedSettings.getMusicName()).isEqualTo("Default");
        assertThat(sharedSettings.isVibration()).isTrue();
        assertThat(sharedSettings.isAnimation()).isTrue();
        assertThat(sharedSettings.getTemperatureUnit()).isEqualTo(CELSIUS);
        assertThat(sharedSettings.getDarkMode()).isEqualTo(SYSTEM);
        assertThat(sharedSettings.isOverviewUpdateAlert()).isFalse();
        assertThat(sharedSettings.isShowTeaAlert()).isTrue();
        assertThat(sharedSettings.isSettingsPermissionAlert()).isTrue();
        assertThat(sharedSettings.getSortMode()).isEqualTo(LAST_USED);
        assertThat(sharedSettings.isOverviewHeader()).isFalse();
        assertThat(sharedSettings.isOverviewInStock()).isFalse();
        assertThat(sharedSettings.isMigrated()).isTrue();
    }

    @Test
    public void isMigratedIsFalse() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        assertThat(sharedSettings.isMigrated()).isFalse();
    }

    @Test
    public void setMigratedTrue() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setMigrated(true);

        assertThat(sharedSettings.isMigrated()).isTrue();
    }
}
