package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.content.Context;

import androidx.room.Room;

import coolpharaoh.tee.speicher.tea.timer.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.entities.ActualSettings;

public class CountDownServiceViewModel {

    private ActualSettings actualSettings;

    public CountDownServiceViewModel(Context context) {
        TeaMemoryDatabase database = Room.databaseBuilder(context, TeaMemoryDatabase.class, "teamemory")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        ActualSettingsDAO mActualSettingsDAO = database.getActualSettingsDAO();
        actualSettings = mActualSettingsDAO.getSettings();
    }

    public boolean isVibration(){
        return actualSettings.isVibration();
    }

    public boolean isNotification(){
        return actualSettings.isNotification();
    }
}
