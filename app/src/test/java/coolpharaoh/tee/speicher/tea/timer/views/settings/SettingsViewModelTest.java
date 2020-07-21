package coolpharaoh.tee.speicher.tea.timer.views.settings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.models.repository.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.models.repository.TeaRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "coolpharaoh.tee.speicher.tea.timer.*")
public class SettingsViewModelTest {
    private SettingsViewModel settingsViewModel;

    @Mock
    ActualSettingsRepository actualSettingsRepository;
    @Mock
    TeaRepository teaRepository;

    @Before
    public void setUp() throws Exception {
        whenNew(ActualSettingsRepository.class).withAnyArguments().thenReturn(actualSettingsRepository);
        when(actualSettingsRepository.getSettings()).thenReturn(new ActualSettings());

        whenNew(TeaRepository.class).withAnyArguments().thenReturn(teaRepository);

        settingsViewModel = new SettingsViewModel(null);
    }

    @Test
    public void setMusicchoice(){
        String musicChoice = "MUSIC_CHOICE";
        settingsViewModel.setMusicchoice(musicChoice);

        ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
        verify(actualSettingsRepository).updateSettings((captor.capture()));
        ActualSettings actualSettings = captor.getValue();

        assertThat(actualSettings.getMusicChoice()).isEqualTo(musicChoice);
    }

    @Test
    public void setMusicName(){
        String musicName = "MUSIC_NAME";
        settingsViewModel.setMusicname(musicName);

        verify(actualSettingsRepository).updateSettings(any());

        assertThat(settingsViewModel.getMusicname()).isEqualTo(musicName);
    }

    @Test
    public void setVibration(){
        boolean vibration = true;
        settingsViewModel.setVibration(vibration);

        verify(actualSettingsRepository).updateSettings(any());

        assertThat(settingsViewModel.isVibration()).isEqualTo(vibration);
    }

    @Test
    public void setAnimation(){
        boolean animation = true;
        settingsViewModel.setAnimation(animation);

        verify(actualSettingsRepository).updateSettings(any());

        assertThat(settingsViewModel.isAnimation()).isEqualTo(animation);
    }

    @Test
    public void setTemperatureunit(){
        String temperatureUnit = "TEMPERATURE_UNIT";
        settingsViewModel.setTemperatureunit(temperatureUnit);

        verify(actualSettingsRepository).updateSettings(any());

        assertThat(settingsViewModel.getTemperatureunit()).isEqualTo(temperatureUnit);
    }

    @Test
    public void setShowTeaAlert(){
        boolean showTeaAlert = true;
        settingsViewModel.setShowteaalert(showTeaAlert);

        verify(actualSettingsRepository).updateSettings(any());

        assertThat(settingsViewModel.isShowteaalert()).isEqualTo(showTeaAlert);
    }

    @Test
    public void setMainRateAlert(){
        boolean mainRateAlert = true;
        settingsViewModel.setMainratealert(mainRateAlert);

        verify(actualSettingsRepository).updateSettings(any());

        assertThat(settingsViewModel.isMainratealert()).isEqualTo(mainRateAlert);
    }

    @Test
    public void setSettingsPermissionAlert(){
        boolean settingsPermissionAlert = true;
        settingsViewModel.setSettingsPermissionAlert(settingsPermissionAlert);

        verify(actualSettingsRepository).updateSettings(any());

        assertThat(settingsViewModel.isSettingspermissionalert()).isEqualTo(settingsPermissionAlert);
    }

    @Test
    public void setDefaultSettings(){
        settingsViewModel.setDefaultSettings();

        ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
        verify(actualSettingsRepository).updateSettings(captor.capture());
        ActualSettings actualSettings = captor.getValue();

        assertThat(actualSettings.getMusicChoice()).isEqualTo("content://settings/system/ringtone");
        assertThat(actualSettings.getMusicName()).isEqualTo("Default");
        assertThat(actualSettings.isVibration()).isTrue();
        assertThat(actualSettings.isVibration()).isTrue();
        assertThat(actualSettings.getTemperatureUnit()).isEqualTo("Celsius");
        assertThat(actualSettings.isShowTeaAlert()).isTrue();
        assertThat(actualSettings.isMainRateAlert()).isTrue();
        assertThat(actualSettings.getMainRateCounter()).isEqualTo(0);
        assertThat(actualSettings.getSort()).isEqualTo(0);
    }

    @Test
    public void deleteAllTeas(){
        settingsViewModel.deleteAllTeas();
        verify(teaRepository).deleteAllTeas();
    }
}
