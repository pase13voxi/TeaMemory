package coolpharaoh.tee.speicher.tea.timer.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import coolpharaoh.tee.speicher.tea.timer.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.entities.Tea;
import coolpharaoh.tee.speicher.tea.timer.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.InfusionDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.NoteDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.TeaDAO;

@Database(entities = {Tea.class, Infusion.class, Counter.class, Note.class, ActualSettings.class}, version = 1, exportSchema = false)
public abstract class TeaMemoryDatabase extends RoomDatabase {
    public abstract TeaDAO getTeaDAO();
    public abstract InfusionDAO getInfusionDAO();
    public abstract CounterDAO getCounterDAO();
    public abstract NoteDAO getNoteDAO();
    public abstract ActualSettingsDAO getActualSettingsDAO();
}