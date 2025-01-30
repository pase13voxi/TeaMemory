package coolpharaoh.tee.speicher.tea.timer.database

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao
import coolpharaoh.tee.speicher.tea.timer.core.note.Note
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteDao
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao
import coolpharaoh.tee.speicher.tea.timer.db.Migrations.MIGRATION_10_11
import coolpharaoh.tee.speicher.tea.timer.db.Migrations.MIGRATION_11_12
import coolpharaoh.tee.speicher.tea.timer.db.Migrations.MIGRATION_12_13
import coolpharaoh.tee.speicher.tea.timer.db.Migrations.MIGRATION_13_14
import coolpharaoh.tee.speicher.tea.timer.db.Migrations.MIGRATION_14_15
import coolpharaoh.tee.speicher.tea.timer.db.Migrations.MIGRATION_1_2
import coolpharaoh.tee.speicher.tea.timer.db.Migrations.MIGRATION_2_3
import coolpharaoh.tee.speicher.tea.timer.db.Migrations.MIGRATION_3_4
import coolpharaoh.tee.speicher.tea.timer.db.Migrations.MIGRATION_4_5
import coolpharaoh.tee.speicher.tea.timer.db.Migrations.MIGRATION_5_6
import coolpharaoh.tee.speicher.tea.timer.db.Migrations.MIGRATION_6_7
import coolpharaoh.tee.speicher.tea.timer.db.Migrations.MIGRATION_7_8
import coolpharaoh.tee.speicher.tea.timer.db.Migrations.MIGRATION_8_9
import coolpharaoh.tee.speicher.tea.timer.db.Migrations.MIGRATION_9_10

@Database(
    entities = [Tea::class, Infusion::class, Counter::class, Note::class],
    version = 15,
    exportSchema = false
)
abstract class TeaMemoryDatabase : RoomDatabase() {
    abstract val teaDao: TeaDao
    abstract val infusionDao: InfusionDao
    abstract val counterDao: CounterDao
    abstract val noteDao: NoteDao

    companion object {
        private const val DATABASE_NAME = "teamemory"

        @Volatile
        private var INSTANCE: TeaMemoryDatabase? = null

        fun getDatabase(context: Context): TeaMemoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = create(context)
                INSTANCE = instance
                instance
            }
        }

        // Create the database
        private fun create(context: Context): TeaMemoryDatabase {
            val builder = databaseBuilder(
                context.applicationContext,
                TeaMemoryDatabase::class.java, DATABASE_NAME
            )
            builder.fallbackToDestructiveMigration()
            builder.allowMainThreadQueries()
            builder.addMigrations(MIGRATION_1_2)
            builder.addMigrations(MIGRATION_2_3)
            builder.addMigrations(MIGRATION_3_4)
            builder.addMigrations(MIGRATION_4_5)
            builder.addMigrations(MIGRATION_5_6)
            builder.addMigrations(MIGRATION_6_7)
            builder.addMigrations(MIGRATION_7_8)
            builder.addMigrations(MIGRATION_8_9)
            builder.addMigrations(MIGRATION_9_10)
            builder.addMigrations(MIGRATION_10_11)
            builder.addMigrations(MIGRATION_11_12)
            builder.addMigrations(MIGRATION_12_13)
            builder.addMigrations(MIGRATION_13_14)
            builder.addMigrations(MIGRATION_14_15)
            return builder.build()
        }

        @JvmStatic
        @VisibleForTesting
        fun setMockedDatabase(mockedDatabase: TeaMemoryDatabase?) {
            INSTANCE = mockedDatabase
        }
    }
}