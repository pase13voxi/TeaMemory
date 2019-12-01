package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.content.Context;

import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;

public class MediaServiceViewModel {

    private ActualSettings actualSettings;

    public MediaServiceViewModel(Context context) {
        TeaMemoryDatabase database = TeaMemoryDatabase.getDatabaseInstance(context);

        ActualSettingsDAO mActualSettingsDAO = database.getActualSettingsDAO();
        actualSettings = mActualSettingsDAO.getSettings();
    }

    public String getMusicchoice(){
        return actualSettings.getMusicchoice();
    }

}
