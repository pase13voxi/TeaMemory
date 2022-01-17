package coolpharaoh.tee.speicher.tea.timer.core.settings;

import static org.assertj.core.api.Assertions.assertThat;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.DarkMode.DISABLED;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.DarkMode.SYSTEM;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode.ALPHABETICAL;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode.LAST_USED;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit.CELSIUS;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit.FAHRENHEIT;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

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
    public void setSettingsVersionOne() {
        final int settingsVersion = 1;
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setSettingsVersion(settingsVersion);

        assertThat(sharedSettings.getSettingsVersion()).isEqualTo(settingsVersion);
    }

    @Test
    public void setMusicChoice() {
        final String musicChoice = "MUSIC_CHOICE";
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setMusicChoice(musicChoice);

        assertThat(sharedSettings.getMusicChoice()).isEqualTo(musicChoice);
    }

    @Test
    public void setMusicName() {
        final String musicName = "MUSIC_NAME";
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setMusicName(musicName);

        assertThat(sharedSettings.getMusicName()).isEqualTo(musicName);
    }

    @Test
    public void setVibrationFalse() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setVibration(false);

        assertThat(sharedSettings.isVibration()).isFalse();
    }

    @Test
    public void setAnimationFalse() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setAnimation(false);

        assertThat(sharedSettings.isAnimation()).isFalse();
    }

    @Test
    public void setTemperatureFahrenheit() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setTemperatureUnit(FAHRENHEIT);

        assertThat(sharedSettings.getTemperatureUnit()).isEqualTo(FAHRENHEIT);
    }

    @Test
    public void setDarkModeDisabled() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setDarkMode(DISABLED);

        assertThat(sharedSettings.getDarkMode()).isEqualTo(DISABLED);
    }

    @Test
    public void setOverviewUpdateAlertTrue() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setOverviewUpdateAlert(true);

        assertThat(sharedSettings.isOverviewUpdateAlert()).isTrue();
    }

    @Test
    public void setShowTeaAlertTrue() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setShowTeaAlert(true);

        assertThat(sharedSettings.isShowTeaAlert()).isTrue();
    }

    @Test
    public void setSortModeAlphabetical() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setSortMode(ALPHABETICAL);

        assertThat(sharedSettings.getSortMode()).isEqualTo(ALPHABETICAL);
    }

    @Test
    public void setOverviewHeaderTrue() {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());

        sharedSettings.setOverviewHeader(true);

        assertThat(sharedSettings.isOverviewHeader()).isTrue();
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

        assertThat(sharedSettings.getMusicChoice()).isEqualTo("content://settings/system/ringtone");
        assertThat(sharedSettings.getMusicName()).isEqualTo("Default");
        assertThat(sharedSettings.isVibration()).isTrue();
        assertThat(sharedSettings.isAnimation()).isTrue();
        assertThat(sharedSettings.getTemperatureUnit()).isEqualTo(CELSIUS);
        assertThat(sharedSettings.getDarkMode()).isEqualTo(SYSTEM);
        assertThat(sharedSettings.isOverviewUpdateAlert()).isFalse();
        assertThat(sharedSettings.isShowTeaAlert()).isTrue();
        assertThat(sharedSettings.getSortMode()).isEqualTo(LAST_USED);
        assertThat(sharedSettings.isOverviewHeader()).isFalse();
        assertThat(sharedSettings.isOverviewInStock()).isFalse();
    }
}
