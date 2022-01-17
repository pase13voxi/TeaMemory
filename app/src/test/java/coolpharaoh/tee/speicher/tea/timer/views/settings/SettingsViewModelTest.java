package coolpharaoh.tee.speicher.tea.timer.views.settings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit.CELSIUS;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.settings.DarkMode;
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController;

@ExtendWith(MockitoExtension.class)
class SettingsViewModelTest {
    private SettingsViewModel settingsViewModel;

    @Mock
    TeaRepository teaRepository;
    @Mock
    SharedSettings sharedSettings;
    @Mock
    ImageController imageController;

    @BeforeEach
    void setUp() {
        settingsViewModel = new SettingsViewModel(teaRepository, sharedSettings, imageController);
    }

    @Test
    void setMusicChoice() {
        final String musicChoice = "MUSIC_CHOICE";
        settingsViewModel.setMusicChoice(musicChoice);

        verify(sharedSettings).setMusicChoice(musicChoice);
    }

    @Test
    void setMusicName() {
        final String musicName = "MUSIC_NAME";
        settingsViewModel.setMusicName(musicName);

        verify(sharedSettings).setMusicName(musicName);
    }

    @Test
    void getMusicName() {
        final String musicName = "MUSIC_NAME";
        when(sharedSettings.getMusicName()).thenReturn(musicName);

        assertThat(settingsViewModel.getMusicName()).isEqualTo(musicName);
    }

    @Test
    void setVibration() {
        final boolean vibration = true;
        settingsViewModel.setVibration(vibration);

        verify(sharedSettings).setVibration(vibration);
    }

    @Test
    void isVibration() {
        final boolean vibration = true;
        when(sharedSettings.isVibration()).thenReturn(vibration);

        assertThat(settingsViewModel.isVibration()).isEqualTo(vibration);
    }

    @Test
    void setAnimation() {
        final boolean animation = true;
        settingsViewModel.setAnimation(animation);

        verify(sharedSettings).setAnimation(animation);
    }

    @Test
    void isAnimation() {
        final boolean animation = true;
        when(sharedSettings.isAnimation()).thenReturn(animation);

        assertThat(settingsViewModel.isAnimation()).isEqualTo(animation);
    }

    @Test
    void setTemperatureUnit() {
        settingsViewModel.setTemperatureUnit(CELSIUS);

        verify(sharedSettings).setTemperatureUnit(CELSIUS);
    }

    @Test
    void getTemperatureUnit() {
        when(sharedSettings.getTemperatureUnit()).thenReturn(CELSIUS);

        assertThat(settingsViewModel.getTemperatureUnit()).isEqualTo(CELSIUS);
    }

    @Test
    void setOverviewHeader() {
        final boolean overviewHeader = true;
        settingsViewModel.setOverviewHeader(overviewHeader);
        verify(sharedSettings).setOverviewHeader(overviewHeader);
    }

    @Test
    void isOverviewHeader() {
        when(sharedSettings.isOverviewHeader()).thenReturn(false);
        final boolean isOverviewHeader = settingsViewModel.isOverviewHeader();
        assertThat(isOverviewHeader).isFalse();
    }

    @Test
    void setDarkMode() {
        final DarkMode darkMode = DarkMode.ENABLED;
        settingsViewModel.setDarkMode(darkMode);
        verify(sharedSettings).setDarkMode(darkMode);
    }

    @Test
    void getDarkMode() {
        when(sharedSettings.getDarkMode()).thenReturn(DarkMode.ENABLED);
        final DarkMode darkMode = settingsViewModel.getDarkMode();
        assertThat(darkMode).isEqualTo(DarkMode.ENABLED);
    }

    @Test
    void setShowTeaAlert() {
        final boolean showTeaAlert = true;
        settingsViewModel.setShowTeaAlert(showTeaAlert);

        verify(sharedSettings).setShowTeaAlert(showTeaAlert);
    }

    @Test
    void isShowTeaAlert() {
        final boolean showTeaAlert = true;
        when(sharedSettings.isShowTeaAlert()).thenReturn(showTeaAlert);

        assertThat(settingsViewModel.isShowTeaAlert()).isEqualTo(showTeaAlert);
    }

    @Test
    void setMainUpdateAlert() {
        final boolean overviewUpdateAlert = true;
        when(sharedSettings.isOverviewUpdateAlert()).thenReturn(overviewUpdateAlert);

        assertThat(settingsViewModel.isMainUpdateAlert()).isEqualTo(overviewUpdateAlert);
    }

    @Test
    void isMainUpdateAlert() {
        final boolean overviewUpdateAlert = true;
        settingsViewModel.setOverviewUpdateAlert(overviewUpdateAlert);

        verify(sharedSettings).setOverviewUpdateAlert(overviewUpdateAlert);
    }

    @Test
    void setDefaultSettings() {
        settingsViewModel.setDefaultSettings();

        verify(sharedSettings).setFactorySettings();
    }

    @Test
    void getAllTeas() {
        final Tea tea1 = new Tea();
        tea1.setId(1L);
        final Tea tea2 = new Tea();
        tea2.setId(2L);
        final List<Tea> teas = Arrays.asList(tea1, tea2);
        when(teaRepository.getTeas()).thenReturn(teas);

        settingsViewModel.deleteAllTeaImages();

        verify(imageController).removeImageByTeaId(1L);
        verify(imageController).removeImageByTeaId(2L);
    }

    @Test
    void deleteAllTeas() {
        settingsViewModel.deleteAllTeas();
        verify(teaRepository).deleteAllTeas();
    }
}
