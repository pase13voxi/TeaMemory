package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;

public class TimerViewModel {

    private final TeaDAO teaDAO;
    private final ActualSettingsDAO actualSettingsDAO;

    public TimerViewModel(TeaMemoryDatabase database) {

        teaDAO = database.getTeaDAO();
        actualSettingsDAO = database.getActualSettingsDAO();
    }

    //teaDAO
    public String getName(long teaId){
        if(teaId==0){
            return "Default Tea";
        }
        return teaDAO.getTeaById(teaId).getName();
    }

    //actualSettingsDAO
    public boolean isVibration(){
        return actualSettingsDAO.getSettings().isVibration();
    }

    public String getMusicchoice(){
        return actualSettingsDAO.getSettings().getMusicChoice();
    }
}
