package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

@ExtendWith(MockitoExtension.class)
class TimerViewModelTest {
    private TimerViewModel timerViewModel;

    @Mock
    SharedSettings sharedSettings;
    @Mock
    TeaRepository teaRepository;


    @BeforeEach
    void setUp() {
        timerViewModel = new TimerViewModel(teaRepository, sharedSettings);
    }


    @Test
    void isVibration() {
        when(sharedSettings.isVibration()).thenReturn(true);

        assertThat(timerViewModel.isVibration()).isTrue();

        when(sharedSettings.isVibration()).thenReturn(false);

        assertThat(timerViewModel.isVibration()).isFalse();

    }

    @Test
    void getMusicChoice() {
        final String musicChoice = "MUSICPATH";

        when(sharedSettings.getMusicChoice()).thenReturn(musicChoice);

        assertThat(timerViewModel.getMusicChoice()).isEqualTo(musicChoice);
    }

    @Test
    void getName() {
        final String teaName = "TEANAME";

        final Tea tea = new Tea();
        tea.setName(teaName);

        when(teaRepository.getTeaById(1L)).thenReturn(tea);

        assertThat(timerViewModel.getName(0L)).isEqualTo("Default Tea");
        assertThat(timerViewModel.getName(1L)).isEqualTo(teaName);
    }

}
