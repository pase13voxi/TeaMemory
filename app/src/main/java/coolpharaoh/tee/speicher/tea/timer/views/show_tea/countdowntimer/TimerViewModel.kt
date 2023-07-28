package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import android.app.Application;

import androidx.annotation.VisibleForTesting;

import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

class TimerViewModel {

    private final TeaRepository teaRepository;
    private final SharedSettings sharedSettings;

    TimerViewModel(final Application application) {
        this(new TeaRepository(application), new SharedSettings(application));
    }

    @VisibleForTesting
    TimerViewModel(final TeaRepository teaRepository, final SharedSettings sharedSettings) {
        this.teaRepository = teaRepository;
        this.sharedSettings = sharedSettings;
    }

    //teaDAO
    String getName(final long teaId) {
        if (teaId == 0) {
            return "Default Tea";
        }
        return teaRepository.getTeaById(teaId).getName();
    }

    //actualSettingsDAO
    boolean isVibration() {
        return sharedSettings.isVibration();
    }

    String getMusicChoice() {
        return sharedSettings.getMusicChoice();
    }
}
