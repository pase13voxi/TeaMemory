package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SettingsViewModelTest {
    private SettingsViewModel settingsViewModel;

    @Mock
    ActualSettingsDAO actualSettingsDAO;
    @Mock
    TeaDAO teaDAO;
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;

    @Before
    public void setUp() {
        when(teaMemoryDatabase.getActualSettingsDAO()).thenReturn(actualSettingsDAO);
        when(actualSettingsDAO.getSettings()).thenReturn(new ActualSettings());
        when(teaMemoryDatabase.getTeaDAO()).thenReturn(teaDAO);

        settingsViewModel = new SettingsViewModel(teaMemoryDatabase);
    }

    @Test
    public void setMusicchoice(){
        String musicChoice = "MUSIC_CHOICE";
        settingsViewModel.setMusicchoice(musicChoice);

        ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
        verify(actualSettingsDAO).update((captor.capture()));
        ActualSettings actualSettings = captor.getValue();

        assertEquals(musicChoice, actualSettings.getMusicchoice());
    }

    @Test
    public void setMusicName(){
        String musicName = "MUSIC_NAME";
        settingsViewModel.setMusicname(musicName);

        verify(actualSettingsDAO).update(any());

        assertEquals(musicName, settingsViewModel.getMusicname());
    }

    @Test
    public void setVibration(){
        boolean vibration = true;
        settingsViewModel.setVibration(vibration);

        verify(actualSettingsDAO).update(any());

        assertEquals(vibration, settingsViewModel.isVibration());
    }

    @Test
    public void setAnimation(){
        boolean animation = true;
        settingsViewModel.setAnimation(animation);

        verify(actualSettingsDAO).update(any());

        assertEquals(animation, settingsViewModel.isAnimation());
    }

    @Test
    public void setTemperatureunit(){
        String temperatureUnit = "TEMPERATURE_UNIT";
        settingsViewModel.setTemperatureunit(temperatureUnit);

        verify(actualSettingsDAO).update(any());

        assertEquals(temperatureUnit, settingsViewModel.getTemperatureunit());
    }

    @Test
    public void setShowTeaAlert(){
        boolean showTeaAlert = true;
        settingsViewModel.setShowteaalert(showTeaAlert);

        verify(actualSettingsDAO).update(any());

        assertEquals(showTeaAlert, settingsViewModel.isShowteaalert());
    }

    @Test
    public void setMainRateAlert(){
        boolean mainRateAlert = true;
        settingsViewModel.setMainratealert(mainRateAlert);

        verify(actualSettingsDAO).update(any());

        assertEquals(mainRateAlert, settingsViewModel.isMainratealert());
    }

    @Test
    public void setSettingsPermissionAlert(){
        boolean settingsPermissionAlert = true;
        settingsViewModel.setSettingsPermissionAlert(settingsPermissionAlert);

        verify(actualSettingsDAO).update(any());

        assertEquals(settingsPermissionAlert, settingsViewModel.isSettingspermissionalert());
    }

    @Test
    public void setDefaultSettings(){
        settingsViewModel.setDefaultSettings();

        ArgumentCaptor<ActualSettings> captor = ArgumentCaptor.forClass(ActualSettings.class);
        verify(actualSettingsDAO).update(captor.capture());
        ActualSettings actualSettings = captor.getValue();

        assertEquals("content://settings/system/ringtone", actualSettings.getMusicchoice());
        assertEquals("Default", actualSettings.getMusicname());
        assertTrue(actualSettings.isVibration());
        assertTrue(actualSettings.isNotification());
        assertTrue(actualSettings.isVibration());
        assertEquals("Celsius", actualSettings.getTemperatureunit());
        assertTrue(actualSettings.isShowteaalert());
        assertTrue(actualSettings.isMainratealert());
        assertEquals(0, actualSettings.getMainratecounter());
        assertEquals(0, actualSettings.getSort());
    }

    @Test
    public void deleteAllTeas(){
        settingsViewModel.deleteAllTeas();
        verify(teaDAO).deleteAll();
    }
}
