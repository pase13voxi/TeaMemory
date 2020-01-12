package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimerViewModelTest {

    private TimerViewModel timerViewModel;

    @Mock
    ActualSettingsDAO actualSettingsDAO;
    @Mock
    TeaDAO teaDAO;
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;

    @Before
    public void setUp(){
        when(teaMemoryDatabase.getActualSettingsDAO()).thenReturn(actualSettingsDAO);
        when(teaMemoryDatabase.getTeaDAO()).thenReturn(teaDAO);

        timerViewModel = new TimerViewModel(teaMemoryDatabase);
    }

    @Test
    public void isVibration(){
        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setVibration(true);

        when(actualSettingsDAO.getSettings()).thenReturn(actualSettings);

        assertTrue(timerViewModel.isVibration());

        actualSettings.setVibration(false);

        assertFalse(timerViewModel.isVibration());
    }

    @Test
    public void getMusicChoice(){
        String musicChoice = "MUSICPATH";

        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setMusicchoice(musicChoice);

        when(actualSettingsDAO.getSettings()).thenReturn(actualSettings);

        assertEquals(musicChoice, timerViewModel.getMusicchoice());
    }

    @Test
    public void getName(){
        String teaName = "TEANAME";

        Tea tea = new Tea();
        tea.setName(teaName);

        when(teaDAO.getTeaById(1L)).thenReturn(tea);

        assertEquals("Default Tea", timerViewModel.getName(0L));
        assertEquals(teaName, timerViewModel.getName(1L));
    }
}
