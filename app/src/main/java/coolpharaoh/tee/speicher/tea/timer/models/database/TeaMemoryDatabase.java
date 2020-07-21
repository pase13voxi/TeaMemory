package coolpharaoh.tee.speicher.tea.timer.models.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.models.daos.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.models.daos.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.models.daos.NoteDao;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;

import static coolpharaoh.tee.speicher.tea.timer.models.database.Migrations.MIGRATION_1_2;
import static coolpharaoh.tee.speicher.tea.timer.models.database.Migrations.MIGRATION_2_3;
import static coolpharaoh.tee.speicher.tea.timer.models.database.Migrations.MIGRATION_3_4;
import static coolpharaoh.tee.speicher.tea.timer.models.database.Migrations.MIGRATION_4_5;

@Database(entities = {Tea.class, Infusion.class, Counter.class, Note.class, ActualSettings.class}, version = 5, exportSchema = false)
public abstract class TeaMemoryDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "teamemory";
    private static TeaMemoryDatabase sInstance;

    public abstract TeaDao getTeaDao();

    public abstract InfusionDao getInfusionDao();

    public abstract CounterDao getCounterDao();

    public abstract NoteDao getNoteDao();

    public abstract ActualSettingsDao getActualSettingsDao();

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
        builder.addMigrations(MIGRATION_2_3);
        builder.addMigrations(MIGRATION_3_4);
        builder.addMigrations(MIGRATION_4_5);

        return builder.build();
    }

}