package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.content.Context;

import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;

public class TimerViewModel {

    private TeaDAO teaDAO;
    private ActualSettings actualSettings;

    public TimerViewModel(Context context) {
        TeaMemoryDatabase database = TeaMemoryDatabase.getDatabaseInstance(context);

        teaDAO = database.getTeaDAO();

        ActualSettingsDAO mActualSettingsDAO = database.getActualSettingsDAO();
        actualSettings = mActualSettingsDAO.getSettings();
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
        return actualSettings.isVibration();
    }

    public boolean isNotification(){
        return actualSettings.isNotification();
    }

    public String getMusicchoice(){
        return actualSettings.getMusicchoice();
    }
}
