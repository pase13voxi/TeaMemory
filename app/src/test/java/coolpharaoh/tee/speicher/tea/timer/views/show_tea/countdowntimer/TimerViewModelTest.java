package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimerViewModelTest {

    private TimerViewModel timerViewModel;

    @Mock
    ActualSettingsRepository actualSettingsRepository;
    @Mock
    TeaRepository teaRepository;


    @Before
    public void setUp() {
        timerViewModel = new TimerViewModel(teaRepository, actualSettingsRepository);
    }



    @Test
    public void isVibration(){

        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setVibration(true);

        when(actualSettingsRepository.getSettings()).thenReturn(actualSettings);

        assertThat(timerViewModel.isVibration()).isTrue();

        actualSettings.setVibration(false);

        assertThat(timerViewModel.isVibration()).isFalse();

    }

    @Test
    public void getMusicChoice(){
        String musicChoice = "MUSICPATH";

        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setMusicChoice(musicChoice);

        when(actualSettingsRepository.getSettings()).thenReturn(actualSettings);

        assertThat(timerViewModel.getMusicchoice()).isEqualTo(musicChoice);
    }

    @Test
    public void getName(){
        String teaName = "TEANAME";

        Tea tea = new Tea();
        tea.setName(teaName);

        when(teaRepository.getTeaById(1L)).thenReturn(tea);

        assertThat(timerViewModel.getName(0L)).isEqualTo("Default Tea");
        assertThat(timerViewModel.getName(1L)).isEqualTo(teaName);
    }

}
