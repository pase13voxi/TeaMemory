package coolpharaoh.tee.speicher.tea.timer.core.database;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;

import static coolpharaoh.tee.speicher.tea.timer.core.database.Migrations.MIGRATION_1_2;
import static coolpharaoh.tee.speicher.tea.timer.core.database.Migrations.MIGRATION_2_3;
import static coolpharaoh.tee.speicher.tea.timer.core.database.Migrations.MIGRATION_3_4;
import static coolpharaoh.tee.speicher.tea.timer.core.database.Migrations.MIGRATION_4_5;
import static coolpharaoh.tee.speicher.tea.timer.core.database.Migrations.MIGRATION_5_6;

@Database(entities = {Tea.class, Infusion.class, Counter.class, Note.class, ActualSettings.class}, version = 6, exportSchema = false)
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
        builder.addMigrations(MIGRATION_5_6);

        return builder.build();
    }

    @VisibleForTesting
    public static void setMockedDatabase(TeaMemoryDatabase mockedDatabase) {
        sInstance = mockedDatabase;
    }

}