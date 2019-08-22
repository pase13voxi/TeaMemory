package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.arch.persistence.room.Room;
import android.content.Context;

import coolpharaoh.tee.speicher.tea.timer.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.entities.ActualSettings;

public class CountDownServiceViewModel {

    private ActualSettingsDAO mActualSettingsDAO;
    private TeaDAO mTeaDAO;

    private ActualSettings mActualSettings;

    public CountDownServiceViewModel(Context context) {
        TeaMemoryDatabase database = Room.databaseBuilder(context, TeaMemoryDatabase.class, "teamemory")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        mActualSettingsDAO = database.getActualSettingsDAO();
        mActualSettings = mActualSettingsDAO.getSettings();

        mTeaDAO = database.getTeaDAO();
    }

    public boolean isVibration(){
        return mActualSettings.isVibration();
    }

    public boolean isNotification(){
        return mActualSettings.isNotification();
    }
}
