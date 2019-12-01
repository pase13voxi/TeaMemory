package coolpharaoh.tee.speicher.tea.timer.models.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.InfusionDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.NoteDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;

@Database(entities = {Tea.class, Infusion.class, Counter.class, Note.class, ActualSettings.class}, version = 2, exportSchema = false)
public abstract class TeaMemoryDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "teamemory";
    private static TeaMemoryDatabase sInstance;

    public abstract TeaDAO getTeaDAO();

    public abstract InfusionDAO getInfusionDAO();

    public abstract CounterDAO getCounterDAO();

    public abstract NoteDAO getNoteDAO();

    public abstract ActualSettingsDAO getActualSettingsDAO();

    // Get a database instance
    public static synchronized TeaMemoryDatabase getDatabaseInstance(Context context) {
        if (sInstance == null) {
            sInstance = create(context);
        }
        return sInstance;
    }

    // Create the database
    private static TeaMemoryDatabase create(Context context) {
        RoomDatabase.Builder<TeaMemoryDatabase> builder = Room.databaseBuilder(context.getApplicationContext(),
                TeaMemoryDatabase.class, DATABASE_NAME);
        builder.fallbackToDestructiveMigration();
        builder.allowMainThreadQueries();
        builder.addMigrations(MIGRATION_1_2);

        return builder.build();
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE settings "
                    + " ADD COLUMN settingspermissionalert INTEGER DEFAULT 1 NOT NULL");
        }
    };

}