package coolpharaoh.tee.speicher.tea.timer.views.showtea.timer;

import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;

class TimerViewModel {

    private final TeaDao teaDAO;
    private final ActualSettingsDao actualSettingsDAO;

    TimerViewModel(TeaMemoryDatabase database) {

        teaDAO = database.getTeaDao();
        actualSettingsDAO = database.getActualSettingsDao();
    }

    //teaDAO
    String getName(long teaId) {
        if (teaId == 0) {
            return "Default Tea";
        }
        return teaDAO.getTeaById(teaId).getName();
    }

    //actualSettingsDAO
    boolean isVibration() {
        return actualSettingsDAO.getSettings().isVibration();
    }

    String getMusicchoice() {
        return actualSettingsDAO.getSettings().getMusicChoice();
    }
}
