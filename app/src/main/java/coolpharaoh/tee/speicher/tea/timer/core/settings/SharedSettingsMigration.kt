package coolpharaoh.tee.speicher.tea.timer.core.settings

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class SharedSettingsMigration(application: Application) {
    private val sharedPreferences: SharedPreferences
    private val sharedSettings: SharedSettings

    init {
        sharedPreferences = application.getSharedPreferences(
            SharedSettings.TEA_MEMORY_SETTINGS,
            Context.MODE_PRIVATE
        )
        sharedSettings = SharedSettings(application)
    }

    fun migrate() {
        val oldVersion = sharedSettings.settingsVersion
        for (nextVersion in oldVersion + 1..NEW_VERSION) {
            delegateMigrations(nextVersion)
        }
        sharedSettings.settingsVersion = NEW_VERSION
    }

    private fun delegateMigrations(nextVersion: Int) {
        when (nextVersion) {
            1 -> migration0T1()
            2 -> migration1T2()
            3 -> migration2T3()
            else -> {}
        }
    }

    private fun migration0T1() {
        if (sharedSettings.isFirstStart) {
            sharedSettings.setFactorySettings()
        }
    }

    private fun migration1T2() {
        val editor = sharedPreferences.edit()
        editor.remove("IS_MIGRATED")
        editor.remove("settings_permission_alert")
        editor.putBoolean("overview_update_alert", true)
        editor.apply()
    }

    private fun migration2T3() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("overview_update_alert", true)
        editor.apply()
    }

    companion object {
        private const val NEW_VERSION = 3
    }
}