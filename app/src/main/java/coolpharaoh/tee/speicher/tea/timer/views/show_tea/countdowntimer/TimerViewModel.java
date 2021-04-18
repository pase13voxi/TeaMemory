package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import android.app.Application;

import androidx.annotation.VisibleForTesting;

import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

class TimerViewModel {

    private final TeaRepository teaRepository;
    private final ActualSettingsRepository actualSettingsRepository;

    TimerViewModel(Application application) {
        this(new TeaRepository(application), new ActualSettingsRepository(application));
    }

    @VisibleForTesting
    TimerViewModel(TeaRepository teaRepository, ActualSettingsRepository actualSettingsRepository) {
        this.teaRepository = teaRepository;
        this.actualSettingsRepository = actualSettingsRepository;
    }

    //teaDAO
    String getName(long teaId) {
        if (teaId == 0) {
            return "Default Tea";
        }
        return teaRepository.getTeaById(teaId).getName();
    }

    //actualSettingsDAO
    boolean isVibration() {
        return actualSettingsRepository.getSettings().isVibration();
    }

    String getMusicchoice() {
        return actualSettingsRepository.getSettings().getMusicChoice();
    }
}
