package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

@RunWith(MockitoJUnitRunner.class)
public class TimerViewModelTest {

    private TimerViewModel timerViewModel;

    @Mock
    SharedSettings sharedSettings;
    @Mock
    TeaRepository teaRepository;


    @Before
    public void setUp() {
        timerViewModel = new TimerViewModel(teaRepository, sharedSettings);
    }



    @Test
    public void isVibration(){
        when(sharedSettings.isVibration()).thenReturn(true);

        assertThat(timerViewModel.isVibration()).isTrue();

        when(sharedSettings.isVibration()).thenReturn(false);

        assertThat(timerViewModel.isVibration()).isFalse();

    }

    @Test
    public void getMusicChoice(){
        final String musicChoice = "MUSICPATH";

        when(sharedSettings.getMusicChoice()).thenReturn(musicChoice);

        assertThat(timerViewModel.getMusicChoice()).isEqualTo(musicChoice);
    }

    @Test
    public void getName(){
        final String teaName = "TEANAME";

        final Tea tea = new Tea();
        tea.setName(teaName);

        when(teaRepository.getTeaById(1L)).thenReturn(tea);

        assertThat(timerViewModel.getName(0L)).isEqualTo("Default Tea");
        assertThat(timerViewModel.getName(1L)).isEqualTo(teaName);
    }

}
