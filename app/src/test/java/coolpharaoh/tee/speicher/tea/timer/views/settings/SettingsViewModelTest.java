package coolpharaoh.tee.speicher.tea.timer.views.settings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.DarkMode;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

@RunWith(MockitoJUnitRunner.class)
public class SettingsViewModelTest {
    private SettingsViewModel settingsViewModel;

    @Mock
    TeaRepository teaRepository;
    @Mock
    ActualSettingsRepository actualSettingsRepository;
    @Mock
    SharedSettings sharedSettings;

    @Before
    public void setUp() {
        when(actualSettingsRepository.getSettings()).thenReturn(new ActualSettings());

        settingsViewModel = new SettingsViewModel(teaRepository, actualSettingsRepository, sharedSettings);
    }

    @Test
    public void setMusicchoice() {
        final String musicChoice = "MUSIC_CHOICE";
        settingsViewModel.setMusicChoice(musicChoice);

        final ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
        verify(actualSettingsRepository).updateSettings((captor.capture()));
        final ActualSettings actualSettings = captor.getValue();

        assertThat(actualSettings.getMusicChoice()).isEqualTo(musicChoice);
    }

    @Test
    public void setMusicName(){
        final String musicName = "MUSIC_NAME";
        settingsViewModel.setMusicName(musicName);

        verify(actualSettingsRepository).updateSettings(any());

        assertThat(settingsViewModel.getMusicName()).isEqualTo(musicName);
    }

    @Test
    public void setVibration(){
        final boolean vibration = true;
        settingsViewModel.setVibration(vibration);

        verify(actualSettingsRepository).updateSettings(any());

        assertThat(settingsViewModel.isVibration()).isEqualTo(vibration);
    }

    @Test
    public void setAnimation() {
        final boolean animation = true;
        settingsViewModel.setAnimation(animation);

        verify(actualSettingsRepository).updateSettings(any());

        assertThat(settingsViewModel.isAnimation()).isEqualTo(animation);
    }

    @Test
    public void setTemperatureUnit() {
        final String temperatureUnit = "TEMPERATURE_UNIT";
        settingsViewModel.setTemperatureUnit(temperatureUnit);

        verify(actualSettingsRepository).updateSettings(any());

        assertThat(settingsViewModel.getTemperatureUnit()).isEqualTo(temperatureUnit);
    }

    @Test
    public void setOverviewHeader() {
        final boolean overviewHeader = true;
        settingsViewModel.setOverviewHeader(overviewHeader);
        verify(sharedSettings).setOverviewHeader(overviewHeader);
    }

    @Test
    public void isOverviewHeader() {
        when(sharedSettings.isOverviewHeader()).thenReturn(false);
        final boolean isOverviewHeader = settingsViewModel.isOverviewHeader();
        assertThat(isOverviewHeader).isFalse();
    }

    @Test
    public void setDarkMode() {
        final DarkMode darkMode = DarkMode.ENABLED;
        settingsViewModel.setDarkMode(darkMode);
        verify(sharedSettings).setDarkMode(darkMode);
    }

    @Test
    public void getDarkMode() {
        when(sharedSettings.getDarkMode()).thenReturn(DarkMode.ENABLED);
        final DarkMode darkMode = settingsViewModel.getDarkMode();
        assertThat(darkMode).isEqualTo(DarkMode.ENABLED);
    }

    @Test
    public void setShowTeaAlert() {
        final boolean showTeaAlert = true;
        settingsViewModel.setShowTeaAlert(showTeaAlert);

        verify(actualSettingsRepository).updateSettings(any());

        assertThat(settingsViewModel.isShowTeaAlert()).isEqualTo(showTeaAlert);
    }

    @Test
    public void setMainUpdateAlert() {
        final boolean mainUpdateAlert = true;
        settingsViewModel.setMainUpdateAlert(mainUpdateAlert);

        verify(actualSettingsRepository).updateSettings(any());

        assertThat(settingsViewModel.isMainUpdateAlert()).isEqualTo(mainUpdateAlert);
    }

    @Test
    public void setSettingsPermissionAlert() {
        final boolean settingsPermissionAlert = true;
        settingsViewModel.setSettingsPermissionAlert(settingsPermissionAlert);

        verify(actualSettingsRepository).updateSettings(any());

        assertThat(settingsViewModel.isSettingsPermissionAlert()).isEqualTo(settingsPermissionAlert);
    }

    @Test
    public void setDefaultSettings(){
        settingsViewModel.setDefaultSettings();

        final ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
        verify(actualSettingsRepository).updateSettings(captor.capture());
        final ActualSettings actualSettings = captor.getValue();

        assertThat(actualSettings.getMusicChoice()).isEqualTo("content://settings/system/ringtone");
        assertThat(actualSettings.getMusicName()).isEqualTo("Default");
        assertThat(actualSettings.isVibration()).isTrue();
        assertThat(actualSettings.isVibration()).isTrue();
        assertThat(actualSettings.getTemperatureUnit()).isEqualTo("Celsius");
        assertThat(actualSettings.isShowTeaAlert()).isTrue();
        assertThat(actualSettings.isMainRateAlert()).isTrue();
        assertThat(actualSettings.getMainRateCounter()).isZero();
        assertThat(actualSettings.getSort()).isZero();
    }

    @Test
    public void deleteAllTeas(){
        settingsViewModel.deleteAllTeas();
        verify(teaRepository).deleteAllTeas();
    }
}
