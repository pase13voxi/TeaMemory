package coolpharaoh.tee.speicher.tea.timer.core.database;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

class Migrations {

    private Migrations() {
    }

    // added settingspermissionalert
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE settings "
                    + " ADD COLUMN settingspermissionalert INTEGER DEFAULT 1 NOT NULL");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create the new table
            database.execSQL(
                    "CREATE TABLE backup_settings (id INTEGER PRIMARY KEY, musicchoice TEXT, " +
                            "musicname TEXT, vibration INTEGER NOT NULL, " +
                            "notification INTEGER NOT NULL, animation INTEGER NOT NULL, " +
                            "temperatureunit TEXT, showteaalert INTEGER NOT NULL, " +
                            "mainratealert INTEGER NOT NULL, mainratecounter INTEGER NOT NULL, " +
                            "settingspermissionalert INTEGER NOT NULL, sort INTEGER NOT NULL)"
            );
            // Copy the data
            database.execSQL(
                    "INSERT INTO backup_settings (id, musicchoice, musicname, vibration, " +
                            "notification, animation, temperatureunit, showteaalert, " +
                            "mainratealert, mainratecounter, settingspermissionalert, " +
                            "sort) SELECT id, musicchoice, musicname, vibration, " +
                            "notification, animation, temperatureunit, showteaalert, " +
                            "mainratealert, mainratecounter, settingspermissionalert, " +
                            "sort FROM settings");
            // Remove the old table
            database.execSQL("DROP TABLE settings");
            // Change the table name to the correct one
            database.execSQL("ALTER TABLE backup_settings RENAME TO settings");
        }
    };

    // rename column lastInfusion to nextinfusion
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Create the new table
            database.execSQL(
                    "CREATE TABLE backup_tea (tea_id INTEGER PRIMARY KEY, name TEXT, " +
                            "variety TEXT, amount INTEGER NOT NULL, " +
                            "amountkind Text, color INTEGER NOT NULL, " +
                            "nextinfusion INTEGER NOT NULL, date INTEGER)"
            );
            // Copy the data
            database.execSQL(
                    "INSERT INTO backup_tea (tea_id, name, variety, amount, " +
                            "amountkind, color, nextinfusion, date) " +
                            "SELECT tea_id, name, variety, amount, " +
                            "amountkind, color, lastInfusion, date " +
                            "FROM tea");
            // Remove the old table
            database.execSQL("DROP TABLE tea");
            // Change the table name to the correct one
            database.execSQL("ALTER TABLE backup_tea RENAME TO tea");
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create the new table
            database.execSQL(
                    "CREATE TABLE backup_settings (settings_id INTEGER PRIMARY KEY, " +
                            "musicchoice TEXT, musicname TEXT, vibration INTEGER NOT NULL," +
                            " animation INTEGER NOT NULL, temperatureunit TEXT, " +
                            "showteaalert INTEGER NOT NULL, mainratealert INTEGER NOT NULL, " +
                            "mainratecounter INTEGER NOT NULL, " +
                            "settingspermissionalert INTEGER NOT NULL, sort INTEGER NOT NULL)"
            );
            // Copy the data
            database.execSQL(
                    "INSERT INTO backup_settings (settings_id, musicchoice, musicname, " +
                            "vibration, animation, temperatureunit, showteaalert, " +
                            "mainratealert, mainratecounter, settingspermissionalert, " +
                            "sort) SELECT id, musicchoice, musicname, vibration, animation, " +
                            "temperatureunit, showteaalert, mainratealert, mainratecounter, " +
                            "settingspermissionalert, sort FROM settings");
            // Remove the old table
            database.execSQL("DROP TABLE settings");
            // Change the table name to the correct one
            database.execSQL("ALTER TABLE backup_settings RENAME TO settings");
        }
    };
}
