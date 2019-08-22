package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.entities.Tea;
import coolpharaoh.tee.speicher.tea.timer.daos.TeaDAO;

public class MainActivityViewModel extends ViewModel {


    private TeaDAO mTeaDAO;
    private ActualSettingsDAO mActualSettingsDAO;

    private MutableLiveData<List<Tea>> mTeas;
    private ActualSettings mActualSettings;

    public MainActivityViewModel(Context context) {
        TeaMemoryDatabase database = Room.databaseBuilder(context, TeaMemoryDatabase.class, "teamemory")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        mActualSettingsDAO = database.getActualSettingsDAO();
        mActualSettings = mActualSettingsDAO.getSettings();

        mTeaDAO = database.getTeaDAO();
        mTeas = new MutableLiveData<>();

        refreshTeas();
    }

    //Teas
    public LiveData<List<Tea>> getTeas() {
        return mTeas;
    }

    public Tea getTeaByPosition(int position) {
        return mTeas.getValue().get(position);
    }

    public void deleteTea(int position) {
        mTeaDAO.delete(mTeas.getValue().get(position));

        refreshTeas();
    }

    //Settings
    public int getSort() {
        return mActualSettings.getSort();
    }

    public void setSort(int sort) {

        mActualSettings.setSort(sort);
        mActualSettingsDAO.update(mActualSettings);

        refreshTeas();
    }

    public boolean isMainRateAlert() {
        return mActualSettings.isMainratealert();
    }

    public void setMainRateAlert(boolean mainRateAlert) {

        mActualSettings.setMainratealert(mainRateAlert);
        mActualSettingsDAO.update(mActualSettings);
    }

    public int getMainRatecounter() {
        return mActualSettings.getMainratecounter();
    }

    public void resetMainRatecounter() {

        mActualSettings.setMainratecounter(0);
        mActualSettingsDAO.update(mActualSettings);
    }

    public void incrementMainRatecounter() {

        mActualSettings.setMainratecounter(mActualSettings.getMainratecounter() + 1);
        mActualSettingsDAO.update(mActualSettings);
    }

    public boolean isMainProblemAlert() {
        return mActualSettings.isMainproblemalert();
    }

    public void setMainProblemAlert(boolean mainProblemAlert) {

        mActualSettings.setMainratealert(mainProblemAlert);
        mActualSettingsDAO.update(mActualSettings);
    }

    public void refreshTeas() {
        switch (mActualSettings.getSort()) {
            //activity
            case 0:
                mTeas.setValue(mTeaDAO.getTeasOrderByActivity());
                break;
            //alphabetic
            case 1:
                mTeas.setValue(mTeaDAO.getTeasOrderByAlphabetic());
                break;
            //variety
            case 2:
                mTeas.setValue(mTeaDAO.getTeasOrderByVariety());
                break;
        }
    }

}
