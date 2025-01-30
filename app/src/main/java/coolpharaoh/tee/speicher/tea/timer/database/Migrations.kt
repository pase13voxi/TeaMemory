package coolpharaoh.tee.speicher.tea.timer.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal object Migrations {
    // added settingspermissionalert
    @JvmField
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "ALTER TABLE settings "
                        + " ADD COLUMN settingspermissionalert INTEGER DEFAULT 1 NOT NULL"
            )
        }
    }
    @JvmField
    val MIGRATION_2_3: Migration = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Create the new table
            db.execSQL(
                "CREATE TABLE backup_settings (id INTEGER PRIMARY KEY, musicchoice TEXT, " +
                        "musicname TEXT, vibration INTEGER NOT NULL, " +
                        "notification INTEGER NOT NULL, animation INTEGER NOT NULL, " +
                        "temperatureunit TEXT, showteaalert INTEGER NOT NULL, " +
                        "mainratealert INTEGER NOT NULL, mainratecounter INTEGER NOT NULL, " +
                        "settingspermissionalert INTEGER NOT NULL, sort INTEGER NOT NULL)"
            )
            // Copy the data
            db.execSQL(
                "INSERT INTO backup_settings (id, musicchoice, musicname, vibration, " +
                        "notification, animation, temperatureunit, showteaalert, " +
                        "mainratealert, mainratecounter, settingspermissionalert, " +
                        "sort) SELECT id, musicchoice, musicname, vibration, " +
                        "notification, animation, temperatureunit, showteaalert, " +
                        "mainratealert, mainratecounter, settingspermissionalert, " +
                        "sort FROM settings"
            )
            // Remove the old table
            db.execSQL("DROP TABLE settings")
            // Change the table name to the correct one
            db.execSQL("ALTER TABLE backup_settings RENAME TO settings")
        }
    }

    // rename column lastInfusion to nextinfusion
    @JvmField
    val MIGRATION_3_4: Migration = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Create the new table
            db.execSQL(
                "CREATE TABLE backup_tea (tea_id INTEGER PRIMARY KEY, name TEXT, " +
                        "variety TEXT, amount INTEGER NOT NULL, " +
                        "amountkind Text, color INTEGER NOT NULL, " +
                        "nextinfusion INTEGER NOT NULL, date INTEGER)"
            )
            // Copy the data
            db.execSQL(
                "INSERT INTO backup_tea (tea_id, name, variety, amount, " +
                        "amountkind, color, nextinfusion, date) " +
                        "SELECT tea_id, name, variety, amount, " +
                        "amountkind, color, lastInfusion, date " +
                        "FROM tea"
            )
            // Remove the old table
            db.execSQL("DROP TABLE tea")
            // Change the table name to the correct one
            db.execSQL("ALTER TABLE backup_tea RENAME TO tea")
        }
    }
    @JvmField
    val MIGRATION_4_5: Migration = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Create the new table
            db.execSQL(
                "CREATE TABLE backup_settings (settings_id INTEGER PRIMARY KEY, " +
                        "musicchoice TEXT, musicname TEXT, vibration INTEGER NOT NULL," +
                        " animation INTEGER NOT NULL, temperatureunit TEXT, " +
                        "showteaalert INTEGER NOT NULL, mainratealert INTEGER NOT NULL, " +
                        "mainratecounter INTEGER NOT NULL, " +
                        "settingspermissionalert INTEGER NOT NULL, sort INTEGER NOT NULL)"
            )
            // Copy the data
            db.execSQL(
                "INSERT INTO backup_settings (settings_id, musicchoice, musicname, " +
                        "vibration, animation, temperatureunit, showteaalert, " +
                        "mainratealert, mainratecounter, settingspermissionalert, " +
                        "sort) SELECT id, musicchoice, musicname, vibration, animation, " +
                        "temperatureunit, showteaalert, mainratealert, mainratecounter, " +
                        "settingspermissionalert, sort FROM settings"
            )
            // Remove the old table
            db.execSQL("DROP TABLE settings")
            // Change the table name to the correct one
            db.execSQL("ALTER TABLE backup_settings RENAME TO settings")
        }
    }
    @JvmField
    val MIGRATION_5_6: Migration = object : Migration(5, 6) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // update old notes to notes with position -1 and heading "01_notes"
            db.execSQL("UPDATE note SET position = -1, header = '01_notes' WHERE position = 1")

            // add new columns rating and favorite
            db.execSQL("ALTER TABLE tea ADD COLUMN rating INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE tea ADD COLUMN favorite INTEGER NOT NULL DEFAULT 0")

            // add new columns informationalert and mainupdatealert
            db.execSQL("ALTER TABLE settings ADD COLUMN mainupdatealert INTEGER NOT NULL DEFAULT 1")
            db.execSQL("ALTER TABLE settings ADD COLUMN informationalert INTEGER NOT NULL DEFAULT 1")
        }
    }
    @JvmField
    val MIGRATION_6_7: Migration = object : Migration(6, 7) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Create the new table
            db.execSQL(
                "CREATE TABLE backup_settings (settings_id INTEGER PRIMARY KEY, " +
                        "musicchoice TEXT, musicname TEXT, vibration INTEGER NOT NULL," +
                        " animation INTEGER NOT NULL, temperatureunit TEXT, " +
                        "showteaalert INTEGER NOT NULL, mainratealert INTEGER NOT NULL, " +
                        "mainratecounter INTEGER NOT NULL, mainupdatealert INTEGER NOT NULL," +
                        "settingspermissionalert INTEGER NOT NULL, sort INTEGER NOT NULL)"
            )
            // Copy the data
            db.execSQL(
                "INSERT INTO backup_settings (settings_id, musicchoice, musicname, " +
                        "vibration, animation, temperatureunit, showteaalert, " +
                        "mainratealert, mainratecounter, mainupdatealert, settingspermissionalert, " +
                        "sort) SELECT settings_id, musicchoice, musicname, vibration, animation, " +
                        "temperatureunit, showteaalert, mainratealert, mainratecounter, mainupdatealert," +
                        "settingspermissionalert, sort FROM settings"
            )
            // Remove the old table
            db.execSQL("DROP TABLE settings")
            // Change the table name to the correct one
            db.execSQL("ALTER TABLE backup_settings RENAME TO settings")
        }
    }
    @JvmField
    val MIGRATION_7_8: Migration = object : Migration(7, 8) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // new update description available!
            db.execSQL("UPDATE settings SET mainupdatealert = 1")
        }
    }
    @JvmField
    val MIGRATION_8_9: Migration = object : Migration(8, 9) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // new update description available!
            db.execSQL("UPDATE settings SET mainupdatealert = 1")
        }
    }
    @JvmField
    val MIGRATION_9_10: Migration = object : Migration(9, 10) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Create the new table
            db.execSQL(
                "CREATE TABLE backup_tea (tea_id INTEGER PRIMARY KEY, name TEXT, " +
                        "variety TEXT, amount DOUBLE NOT NULL, " +
                        "amountkind Text, color INTEGER NOT NULL, " +
                        "rating INTEGER NOT NULL, favorite INTEGER NOT NULL," +
                        "nextinfusion INTEGER NOT NULL, date INTEGER)"
            )
            // Copy the data
            db.execSQL(
                "INSERT INTO backup_tea (tea_id, name, variety, amount, " +
                        "amountkind, color, rating, favorite, nextinfusion, date) " +
                        "SELECT tea_id, name, variety, amount, amountkind, color, " +
                        "rating, favorite, nextinfusion, date FROM tea"
            )
            // Remove the old table
            db.execSQL("DROP TABLE tea")
            // Change the table name to the correct one
            db.execSQL("ALTER TABLE backup_tea RENAME TO tea")
        }
    }
    @JvmField
    val MIGRATION_10_11: Migration = object : Migration(10, 11) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // new update description available!
            db.execSQL("UPDATE settings SET mainupdatealert = 1")
        }
    }
    @JvmField
    val MIGRATION_11_12: Migration = object : Migration(11, 12) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // new update description available!
            db.execSQL("UPDATE settings SET mainupdatealert = 1")
        }
    }
    @JvmField
    val MIGRATION_12_13: Migration = object : Migration(12, 13) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Create the new tea table
            db.execSQL(
                "CREATE TABLE backup_tea (tea_id INTEGER PRIMARY KEY, name TEXT, variety TEXT, " +
                        "amount DOUBLE NOT NULL, amount_kind Text, color INTEGER NOT NULL, " +
                        "rating INTEGER NOT NULL, in_stock INTEGER NOT NULL," +
                        "next_infusion INTEGER NOT NULL, date INTEGER)"
            )
            // Copy the data
            db.execSQL(
                "INSERT INTO backup_tea (tea_id, name, variety, amount, " +
                        "amount_kind, color, rating, in_stock, next_infusion, date) " +
                        "SELECT tea_id, name, variety, amount, amountkind, color, " +
                        "rating, favorite, nextinfusion, date FROM tea"
            )
            // Remove the old table
            db.execSQL("DROP TABLE tea")
            // Change the table name to the correct one
            db.execSQL("ALTER TABLE backup_tea RENAME TO tea")

            // Create the new counter table
            db.execSQL(
                "CREATE TABLE backup_counter (counter_id INTEGER PRIMARY KEY, " +
                        "tea_id INTEGER NOT NULL, day INTEGER NOT NULL, week INTEGER NOT NULL, " +
                        "month INTEGER NOT NULL, overall INTEGER NOT NULL, day_date INTEGER, " +
                        "week_date INTEGER, month_date INTEGER, " +
                        "FOREIGN KEY(tea_id) REFERENCES tea(tea_id) ON UPDATE NO ACTION ON DELETE CASCADE )"
            )
            // Copy the data
            db.execSQL(
                "INSERT INTO backup_counter (counter_id, tea_id, day, week, month, overall, " +
                        "day_date, week_date, month_date) " +
                        "SELECT counter_id, tea_id, day, week, month, overall, daydate, " +
                        "weekdate, monthdate FROM counter"
            )
            // Remove the old table
            db.execSQL("DROP TABLE counter")
            // Change the table name to the correct one
            db.execSQL("ALTER TABLE backup_counter RENAME TO counter")
            // Create Index
            db.execSQL("CREATE INDEX IF NOT EXISTS index_counter_tea_id ON counter(tea_id)")

            // Create the new infusion table
            db.execSQL(
                "CREATE TABLE backup_infusion (infusion_id INTEGER PRIMARY KEY, " +
                        "tea_id INTEGER NOT NULL, infusion_index INTEGER NOT NULL, time TEXT, " +
                        "cool_down_time TEXT, temperature_celsius INTEGER NOT NULL, " +
                        "temperature_fahrenheit INTEGER NOT NULL, " +
                        "FOREIGN KEY(tea_id) REFERENCES tea(tea_id) ON UPDATE NO ACTION ON DELETE CASCADE )"
            )
            // Copy the data
            db.execSQL(
                "INSERT INTO backup_infusion (infusion_id, tea_id, infusion_index, time, " +
                        "cool_down_time, temperature_celsius, temperature_fahrenheit) " +
                        "SELECT infusion_id, tea_id, infusionindex, time, cooldowntime, " +
                        "temperaturecelsius, temperaturefahrenheit FROM infusion"
            )
            // Remove the old table
            db.execSQL("DROP TABLE infusion")
            // Change the table name to the correct one
            db.execSQL("ALTER TABLE backup_infusion RENAME TO infusion")
            // Create Index
            db.execSQL("CREATE INDEX IF NOT EXISTS index_infusion_tea_id ON infusion(tea_id)")
        }
    }
    @JvmField
    val MIGRATION_13_14: Migration = object : Migration(13, 14) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("DROP TABLE settings")
        }
    }
    @JvmField
    val MIGRATION_14_15: Migration = object : Migration(14, 15) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Create the new counter table
            db.execSQL(
                "CREATE TABLE backup_counter (counter_id INTEGER PRIMARY KEY, " +
                        "tea_id INTEGER NOT NULL, week INTEGER NOT NULL, " +
                        "month INTEGER NOT NULL, year INTEGER NOT NULL, overall INTEGER NOT NULL, " +
                        "week_date INTEGER, month_date INTEGER, year_date INTEGER, " +
                        "FOREIGN KEY(tea_id) REFERENCES tea(tea_id) ON UPDATE NO ACTION ON DELETE CASCADE )"
            )
            // Copy the data
            db.execSQL(
                "INSERT INTO backup_counter (counter_id, tea_id, week, month, year, overall, " +
                        "week_date, month_date, year_date) " +
                        "SELECT counter_id, tea_id, week, month, month, overall, " +
                        "week_date, month_date, month_date FROM counter"
            )
            // Remove the old table
            db.execSQL("DROP TABLE counter")
            // Change the table name to the correct one
            db.execSQL("ALTER TABLE backup_counter RENAME TO counter")
            // Create Index
            db.execSQL("CREATE INDEX IF NOT EXISTS index_counter_tea_id ON counter(tea_id)")
        }
    }
}