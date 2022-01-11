package coolpharaoh.tee.speicher.tea.timer.views.settings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit.CELSIUS;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.DarkMode;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController;

@RunWith(MockitoJUnitRunner.class)
public class SettingsViewModelTest {
    private SettingsViewModel settingsViewModel;

    @Mock
    TeaRepository teaRepository;
    @Mock
    SharedSettings sharedSettings;
    @Mock
    ImageController imageController;

    @Before
    public void setUp() {
        settingsViewModel = new SettingsViewModel(teaRepository, sharedSettings, imageController);
    }

    @Test
    public void setMusicChoice() {
        final String musicChoice = "MUSIC_CHOICE";
        settingsViewModel.setMusicChoice(musicChoice);

        verify(sharedSettings).setMusicChoice(musicChoice);
    }

    @Test
    public void setMusicName() {
        final String musicName = "MUSIC_NAME";
        settingsViewModel.setMusicName(musicName);

        verify(sharedSettings).setMusicName(musicName);
    }

    @Test
    public void getMusicName() {
        final String musicName = "MUSIC_NAME";
        when(sharedSettings.getMusicName()).thenReturn(musicName);

        assertThat(settingsViewModel.getMusicName()).isEqualTo(musicName);
    }

    @Test
    public void setVibration() {
        final boolean vibration = true;
        settingsViewModel.setVibration(vibration);

        verify(sharedSettings).setVibration(vibration);
    }

    @Test
    public void isVibration() {
        final boolean vibration = true;
        when(sharedSettings.isVibration()).thenReturn(vibration);

        assertThat(settingsViewModel.isVibration()).isEqualTo(vibration);
    }

    @Test
    public void setAnimation() {
        final boolean animation = true;
        settingsViewModel.setAnimation(animation);

        verify(sharedSettings).setAnimation(animation);
    }

    @Test
    public void isAnimation() {
        final boolean animation = true;
        when(sharedSettings.isAnimation()).thenReturn(animation);

        assertThat(settingsViewModel.isAnimation()).isEqualTo(animation);
    }

    @Test
    public void setTemperatureUnit() {
        settingsViewModel.setTemperatureUnit(CELSIUS);

        verify(sharedSettings).setTemperatureUnit(CELSIUS);
    }

    @Test
    public void getTemperatureUnit() {
        when(sharedSettings.getTemperatureUnit()).thenReturn(CELSIUS);

        assertThat(settingsViewModel.getTemperatureUnit()).isEqualTo(CELSIUS);
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

        verify(sharedSettings).setShowTeaAlert(showTeaAlert);
    }

    @Test
    public void isShowTeaAlert() {
        final boolean showTeaAlert = true;
        when(sharedSettings.isShowTeaAlert()).thenReturn(showTeaAlert);

        assertThat(settingsViewModel.isShowTeaAlert()).isEqualTo(showTeaAlert);
    }

    @Test
    public void setMainUpdateAlert() {
        final boolean overviewUpdateAlert = true;
        when(sharedSettings.isOverviewUpdateAlert()).thenReturn(overviewUpdateAlert);

        assertThat(settingsViewModel.isMainUpdateAlert()).isEqualTo(overviewUpdateAlert);
    }

    @Test
    public void isMainUpdateAlert() {
        final boolean overviewUpdateAlert = true;
        settingsViewModel.setOverviewUpdateAlert(overviewUpdateAlert);

        verify(sharedSettings).setOverviewUpdateAlert(overviewUpdateAlert);
    }

    @Test
    public void setDefaultSettings() {
        settingsViewModel.setDefaultSettings();

        verify(sharedSettings).setFactorySettings();
    }

    @Test
    public void getAllTeas() {
        final Tea tea1 = new Tea();
        tea1.setId(1L);
        final Tea tea2 = new Tea();
        tea2.setId(2L);
        final List<Tea> teas = Arrays.asList(tea1, tea2);
        when(teaRepository.getTeas()).thenReturn(teas);

        settingsViewModel.deleteAllTeaImages();

        verify(imageController).removeImageByTeaId("1");
        verify(imageController).removeImageByTeaId("2");
    }

    @Test
    public void deleteAllTeas() {
        settingsViewModel.deleteAllTeas();
        verify(teaRepository).deleteAllTeas();
    }
}
