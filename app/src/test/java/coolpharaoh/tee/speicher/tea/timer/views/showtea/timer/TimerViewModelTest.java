package coolpharaoh.tee.speicher.tea.timer.views.showtea.timer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimerViewModelTest {

    private TimerViewModel timerViewModel;

    @Mock
    ActualSettingsDao actualSettingsDAO;
    @Mock
    TeaDao teaDAO;
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;


    @Before
    public void setUp(){
        when(teaMemoryDatabase.getActualSettingsDao()).thenReturn(actualSettingsDAO);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDAO);

        timerViewModel = new TimerViewModel(teaMemoryDatabase);
    }



    @Test
    public void isVibration(){

        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setVibration(true);

        when(actualSettingsDAO.getSettings()).thenReturn(actualSettings);

        assertThat(timerViewModel.isVibration()).isTrue();

        actualSettings.setVibration(false);

        assertThat(timerViewModel.isVibration()).isFalse();

    }

    @Test
    public void getMusicChoice(){
        String musicChoice = "MUSICPATH";

        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setMusicChoice(musicChoice);

        when(actualSettingsDAO.getSettings()).thenReturn(actualSettings);

        assertThat(timerViewModel.getMusicchoice()).isEqualTo(musicChoice);
    }

    @Test
    public void getName(){
        String teaName = "TEANAME";

        Tea tea = new Tea();
        tea.setName(teaName);

        when(teaDAO.getTeaById(1L)).thenReturn(tea);

        assertThat(timerViewModel.getName(0L)).isEqualTo("Default Tea");
        assertThat(timerViewModel.getName(1L)).isEqualTo(teaName);
    }

}
