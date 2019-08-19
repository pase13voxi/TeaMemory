package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.app.Application;
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

    public MainActivityViewModel(Context context){
        TeaMemoryDatabase database = Room.databaseBuilder(context, TeaMemoryDatabase.class, "teamemory")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        mActualSettingsDAO = database.getActualSettingsDAO();
        mActualSettings = mActualSettingsDAO.getItemById(1l);

        mTeaDAO = database.getTeaDAO();
        mTeas = new MutableLiveData<>();

        updateTeas();
    }

    //Teas
    public LiveData<List<Tea>> getTeas() {
        return mTeas;
    }

    public void removeTea(int position) {
        mTeaDAO.delete(mTeas.getValue().get(position));

        updateTeas();
    }

    public Tea getTeaByPosition(int position){
        return mTeas.getValue().get(position);
    }

    //Settings
    public int getSort(){
        return mActualSettings.getSort();
    }

    public void updateSort(int sort){

        mActualSettings.setSort(sort);
        mActualSettingsDAO.update(mActualSettings);

        updateTeas();
    }

    public boolean isMainRateAlert() {
        return mActualSettings.isMainRateAlert();
    }

    public void setMainRateAlert(boolean mainRateAlert){

        mActualSettings.setMainRateAlert(mainRateAlert);
        mActualSettingsDAO.update(mActualSettings);
    }

    public int getMainRatecounter() {
        return mActualSettings.getMainRatecounter();
    }

    public void resetMainRatecounter() {

        mActualSettings.setMainRatecounter(0);
        mActualSettingsDAO.update(mActualSettings);
    }

    public void incrementMainRatecounter() {

        mActualSettings.setMainRatecounter(mActualSettings.getMainRatecounter()+1);
        mActualSettingsDAO.update(mActualSettings);
    }

    public boolean isMainProblemAlert(){
        return mActualSettings.isMainProblemAlert();
    }

    public void setMainProblemAlert(boolean mainProblemAlert) {

        mActualSettings.setMainRateAlert(mainProblemAlert);
        mActualSettingsDAO.update(mActualSettings);
    }

    private void updateTeas(){
        switch(mActualSettings.getSort())
        {
            //activity
            case 0: mTeas.setValue(mTeaDAO.getItemsActivity()); break;
            //alphabetic
            case 1: mTeas.setValue(mTeaDAO.getItemsAlphabetic()); break;
            //variety
            case 2: mTeas.setValue(mTeaDAO.getItemsVariety()); break;
        }
    }

}
