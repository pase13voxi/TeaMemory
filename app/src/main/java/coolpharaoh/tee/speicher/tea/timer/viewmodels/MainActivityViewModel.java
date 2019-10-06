package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import java.util.List;
import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.entities.Tea;

public class MainActivityViewModel extends ViewModel {


    private TeaDAO teaDAO;
    private ActualSettingsDAO actualSettingsDAO;

    private MutableLiveData<List<Tea>> teas;
    private ActualSettings actualSettings;

    public MainActivityViewModel(Context context) {
        TeaMemoryDatabase database = Room.databaseBuilder(context, TeaMemoryDatabase.class, "teamemory")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        actualSettingsDAO = database.getActualSettingsDAO();
        actualSettings = actualSettingsDAO.getSettings();

        teaDAO = database.getTeaDAO();
        teas = new MutableLiveData<>();

        refreshTeas();
    }

    //Teas
    public LiveData<List<Tea>> getTeas() {
        return teas;
    }

    public Tea getTeaByPosition(int position) {
        return Objects.requireNonNull(teas.getValue()).get(position);
    }

    public void deleteTea(int position) {
        teaDAO.delete(Objects.requireNonNull(teas.getValue()).get(position));

        refreshTeas();
    }

    //Settings
    public int getSort() {
        return actualSettings.getSort();
    }

    public void setSort(int sort) {

        actualSettings.setSort(sort);
        actualSettingsDAO.update(actualSettings);

        refreshTeas();
    }

    public boolean isMainRateAlert() {
        return actualSettings.isMainratealert();
    }

    public void setMainRateAlert(boolean mainRateAlert) {

        actualSettings.setMainratealert(mainRateAlert);
        actualSettingsDAO.update(actualSettings);
    }

    public int getMainRatecounter() {
        return actualSettings.getMainratecounter();
    }

    public void resetMainRatecounter() {

        actualSettings.setMainratecounter(0);
        actualSettingsDAO.update(actualSettings);
    }

    public void incrementMainRatecounter() {

        actualSettings.setMainratecounter(actualSettings.getMainratecounter() + 1);
        actualSettingsDAO.update(actualSettings);
    }

    public boolean isMainProblemAlert() {
        return actualSettings.isMainproblemalert();
    }

    public void setMainProblemAlert(boolean mainProblemAlert) {

        actualSettings.setMainproblemalert(mainProblemAlert);
        actualSettingsDAO.update(actualSettings);
    }

    public void refreshTeas() {
        switch (actualSettings.getSort()) {
            //activity
            case 0:
                teas.setValue(teaDAO.getTeasOrderByActivity());
                break;
            //alphabetic
            case 1:
                teas.setValue(teaDAO.getTeasOrderByAlphabetic());
                break;
            //variety
            case 2:
                teas.setValue(teaDAO.getTeasOrderByVariety());
                break;
        }
    }

}
