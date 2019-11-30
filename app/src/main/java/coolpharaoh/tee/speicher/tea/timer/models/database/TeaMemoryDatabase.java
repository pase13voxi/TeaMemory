package coolpharaoh.tee.speicher.tea.timer.models.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;
import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.InfusionDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.NoteDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;

@Database(entities = {Tea.class, Infusion.class, Counter.class, Note.class, ActualSettings.class}, version = 1, exportSchema = false)
public abstract class TeaMemoryDatabase extends RoomDatabase {
    public abstract TeaDAO getTeaDAO();
    public abstract InfusionDAO getInfusionDAO();
    public abstract CounterDAO getCounterDAO();
    public abstract NoteDAO getNoteDAO();
    public abstract ActualSettingsDAO getActualSettingsDAO();
}