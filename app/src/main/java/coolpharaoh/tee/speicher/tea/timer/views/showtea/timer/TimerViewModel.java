package coolpharaoh.tee.speicher.tea.timer.views.showtea.timer;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.models.repository.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.models.repository.TeaRepository;

class TimerViewModel {

    private final TeaRepository teaRepository;
    private final ActualSettingsRepository actualSettingsRepository;

    TimerViewModel(Application application) {

        teaRepository = new TeaRepository(application);
        actualSettingsRepository = new ActualSettingsRepository(application);
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
