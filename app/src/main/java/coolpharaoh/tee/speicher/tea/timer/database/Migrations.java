package coolpharaoh.tee.speicher.tea.timer.database;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

// no constants for the string because it's harder to read
@SuppressWarnings("java:S1192")
class Migrations {

    private Migrations() {
    }

    // added settingspermissionalert
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE settings "
                    + " ADD COLUMN settingspermissionalert INTEGER DEFAULT 1 NOT NULL");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
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
        public void migrate(@NonNull final SupportSQLiteDatabase database) {
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
        public void migrate(final SupportSQLiteDatabase database) {
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

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            // update old notes to notes with position -1 and heading "01_notes"
            database.execSQL("UPDATE note SET position = -1, header = '01_notes' WHERE position = 1");

            // add new columns rating and favorite
            database.execSQL("ALTER TABLE tea ADD COLUMN rating INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE tea ADD COLUMN favorite INTEGER NOT NULL DEFAULT 0");

            // add new columns informationalert and mainupdatealert
            database.execSQL("ALTER TABLE settings ADD COLUMN mainupdatealert INTEGER NOT NULL DEFAULT 1");
            database.execSQL("ALTER TABLE settings ADD COLUMN informationalert INTEGER NOT NULL DEFAULT 1");
        }
    };

    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            // Create the new table
            database.execSQL(
                    "CREATE TABLE backup_settings (settings_id INTEGER PRIMARY KEY, " +
                            "musicchoice TEXT, musicname TEXT, vibration INTEGER NOT NULL," +
                            " animation INTEGER NOT NULL, temperatureunit TEXT, " +
                            "showteaalert INTEGER NOT NULL, mainratealert INTEGER NOT NULL, " +
                            "mainratecounter INTEGER NOT NULL, mainupdatealert INTEGER NOT NULL," +
                            "settingspermissionalert INTEGER NOT NULL, sort INTEGER NOT NULL)"
            );
            // Copy the data
            database.execSQL(
                    "INSERT INTO backup_settings (settings_id, musicchoice, musicname, " +
                            "vibration, animation, temperatureunit, showteaalert, " +
                            "mainratealert, mainratecounter, mainupdatealert, settingspermissionalert, " +
                            "sort) SELECT settings_id, musicchoice, musicname, vibration, animation, " +
                            "temperatureunit, showteaalert, mainratealert, mainratecounter, mainupdatealert," +
                            "settingspermissionalert, sort FROM settings");
            // Remove the old table
            database.execSQL("DROP TABLE settings");
            // Change the table name to the correct one
            database.execSQL("ALTER TABLE backup_settings RENAME TO settings");
        }
    };

    static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            // new update description available!
            database.execSQL("UPDATE settings SET mainupdatealert = 1");
        }
    };

    static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            // new update description available!
            database.execSQL("UPDATE settings SET mainupdatealert = 1");
        }
    };

    static final Migration MIGRATION_9_10 = new Migration(9, 10) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            // Create the new table
            database.execSQL(
                    "CREATE TABLE backup_tea (tea_id INTEGER PRIMARY KEY, name TEXT, " +
                            "variety TEXT, amount DOUBLE NOT NULL, " +
                            "amountkind Text, color INTEGER NOT NULL, " +
                            "rating INTEGER NOT NULL, favorite INTEGER NOT NULL," +
                            "nextinfusion INTEGER NOT NULL, date INTEGER)"
            );
            // Copy the data
            database.execSQL(
                    "INSERT INTO backup_tea (tea_id, name, variety, amount, " +
                            "amountkind, color, rating, favorite, nextinfusion, date) " +
                            "SELECT tea_id, name, variety, amount, amountkind, color, " +
                            "rating, favorite, nextinfusion, date FROM tea");
            // Remove the old table
            database.execSQL("DROP TABLE tea");
            // Change the table name to the correct one
            database.execSQL("ALTER TABLE backup_tea RENAME TO tea");
        }
    };

    static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            // new update description available!
            database.execSQL("UPDATE settings SET mainupdatealert = 1");
        }
    };

    static final Migration MIGRATION_11_12 = new Migration(11, 12) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            // new update description available!
            database.execSQL("UPDATE settings SET mainupdatealert = 1");
        }
    };

    static final Migration MIGRATION_12_13 = new Migration(12, 13) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            // Create the new tea table
            database.execSQL(
                    "CREATE TABLE backup_tea (tea_id INTEGER PRIMARY KEY, name TEXT, variety TEXT, " +
                            "amount DOUBLE NOT NULL, amount_kind Text, color INTEGER NOT NULL, " +
                            "rating INTEGER NOT NULL, in_stock INTEGER NOT NULL," +
                            "next_infusion INTEGER NOT NULL, date INTEGER)"
            );
            // Copy the data
            database.execSQL(
                    "INSERT INTO backup_tea (tea_id, name, variety, amount, " +
                            "amount_kind, color, rating, in_stock, next_infusion, date) " +
                            "SELECT tea_id, name, variety, amount, amountkind, color, " +
                            "rating, favorite, nextinfusion, date FROM tea");
            // Remove the old table
            database.execSQL("DROP TABLE tea");
            // Change the table name to the correct one
            database.execSQL("ALTER TABLE backup_tea RENAME TO tea");

            // Create the new counter table
            database.execSQL(
                    "CREATE TABLE backup_counter (counter_id INTEGER PRIMARY KEY, " +
                            "tea_id INTEGER NOT NULL, day INTEGER NOT NULL, week INTEGER NOT NULL, " +
                            "month INTEGER NOT NULL, overall INTEGER NOT NULL, day_date INTEGER, " +
                            "week_date INTEGER, month_date INTEGER, " +
                            "FOREIGN KEY(tea_id) REFERENCES tea(tea_id) ON UPDATE NO ACTION ON DELETE CASCADE )"
            );
            // Copy the data
            database.execSQL(
                    "INSERT INTO backup_counter (counter_id, tea_id, day, week, month, overall, " +
                            "day_date, week_date, month_date) " +
                            "SELECT counter_id, tea_id, day, week, month, overall, daydate, " +
                            "weekdate, monthdate FROM counter");
            // Remove the old table
            database.execSQL("DROP TABLE counter");
            // Change the table name to the correct one
            database.execSQL("ALTER TABLE backup_counter RENAME TO counter");
            // Create Index
            database.execSQL("CREATE INDEX IF NOT EXISTS index_counter_tea_id ON counter(tea_id)");

            // Create the new infusion table
            database.execSQL(
                    "CREATE TABLE backup_infusion (infusion_id INTEGER PRIMARY KEY, " +
                            "tea_id INTEGER NOT NULL, infusion_index INTEGER NOT NULL, time TEXT, " +
                            "cool_down_time TEXT, temperature_celsius INTEGER NOT NULL, " +
                            "temperature_fahrenheit INTEGER NOT NULL, " +
                            "FOREIGN KEY(tea_id) REFERENCES tea(tea_id) ON UPDATE NO ACTION ON DELETE CASCADE )"
            );
            // Copy the data
            database.execSQL(
                    "INSERT INTO backup_infusion (infusion_id, tea_id, infusion_index, time, " +
                            "cool_down_time, temperature_celsius, temperature_fahrenheit) " +
                            "SELECT infusion_id, tea_id, infusionindex, time, cooldowntime, " +
                            "temperaturecelsius, temperaturefahrenheit FROM infusion");
            // Remove the old table
            database.execSQL("DROP TABLE infusion");
            // Change the table name to the correct one
            database.execSQL("ALTER TABLE backup_infusion RENAME TO infusion");
            // Create Index
            database.execSQL("CREATE INDEX IF NOT EXISTS index_infusion_tea_id ON infusion(tea_id)");
        }
    };

    static final Migration MIGRATION_13_14 = new Migration(13, 14) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE settings");
        }
    };
}
