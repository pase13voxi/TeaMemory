package coolpharaoh.tee.speicher.tea.timer.views.settings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SettingsViewModelTest {
    private SettingsViewModel settingsViewModel;

    @Mock
    ActualSettingsDao actualSettingsDAO;
    @Mock
    TeaDAO teaDAO;
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;

    @Before
    public void setUp() {
        when(teaMemoryDatabase.getActualSettingsDao()).thenReturn(actualSettingsDAO);
        when(actualSettingsDAO.getSettings()).thenReturn(new ActualSettings());
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDAO);

        settingsViewModel = new SettingsViewModel(teaMemoryDatabase);
    }

    @Test
    public void setMusicchoice(){
        String musicChoice = "MUSIC_CHOICE";
        settingsViewModel.setMusicchoice(musicChoice);

        ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
        verify(actualSettingsDAO).update((captor.capture()));
        ActualSettings actualSettings = captor.getValue();

        assertThat(actualSettings.getMusicChoice()).isEqualTo(musicChoice);
    }

    @Test
    public void setMusicName(){
        String musicName = "MUSIC_NAME";
        settingsViewModel.setMusicname(musicName);

        verify(actualSettingsDAO).update(any());

        assertThat(settingsViewModel.getMusicname()).isEqualTo(musicName);
    }

    @Test
    public void setVibration(){
        boolean vibration = true;
        settingsViewModel.setVibration(vibration);

        verify(actualSettingsDAO).update(any());

        assertThat(settingsViewModel.isVibration()).isEqualTo(vibration);
    }

    @Test
    public void setAnimation(){
        boolean animation = true;
        settingsViewModel.setAnimation(animation);

        verify(actualSettingsDAO).update(any());

        assertThat(settingsViewModel.isAnimation()).isEqualTo(animation);
    }

    @Test
    public void setTemperatureunit(){
        String temperatureUnit = "TEMPERATURE_UNIT";
        settingsViewModel.setTemperatureunit(temperatureUnit);

        verify(actualSettingsDAO).update(any());

        assertThat(settingsViewModel.getTemperatureunit()).isEqualTo(temperatureUnit);
    }

    @Test
    public void setShowTeaAlert(){
        boolean showTeaAlert = true;
        settingsViewModel.setShowteaalert(showTeaAlert);

        verify(actualSettingsDAO).update(any());

        assertThat(settingsViewModel.isShowteaalert()).isEqualTo(showTeaAlert);
    }

    @Test
    public void setMainRateAlert(){
        boolean mainRateAlert = true;
        settingsViewModel.setMainratealert(mainRateAlert);

        verify(actualSettingsDAO).update(any());

        assertThat(settingsViewModel.isMainratealert()).isEqualTo(mainRateAlert);
    }

    @Test
    public void setSettingsPermissionAlert(){
        boolean settingsPermissionAlert = true;
        settingsViewModel.setSettingsPermissionAlert(settingsPermissionAlert);

        verify(actualSettingsDAO).update(any());

        assertThat(settingsViewModel.isSettingspermissionalert()).isEqualTo(settingsPermissionAlert);
    }

    @Test
    public void setDefaultSettings(){
        settingsViewModel.setDefaultSettings();

        ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
        verify(actualSettingsDAO).update(captor.capture());
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
        verify(teaDAO).deleteAll();
    }
}
