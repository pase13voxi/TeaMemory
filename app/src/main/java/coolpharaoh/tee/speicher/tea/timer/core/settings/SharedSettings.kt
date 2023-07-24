package coolpharaoh.tee.speicher.tea.timer.core.settings

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class SharedSettings(application: Application) {
    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences =
            application.getSharedPreferences(TEA_MEMORY_SETTINGS, Context.MODE_PRIVATE)
    }

    var isFirstStart: Boolean
        get() = sharedPreferences.getBoolean(FIRST_START, true)
        set(firstStart) {
            val editor = sharedPreferences.edit()
            editor.putBoolean(FIRST_START, firstStart)
            editor.apply()
        }
    var settingsVersion: Int
        get() = sharedPreferences.getInt(SETTINGS_VERSION, 0)
        set(settingsVersion) {
            val editor = sharedPreferences.edit()
            editor.putInt(SETTINGS_VERSION, settingsVersion)
            editor.apply()
        }
    var musicChoice: String?
        get() = sharedPreferences.getString(MUSIC_CHOICE, null)
        set(musicChoice) {
            val editor = sharedPreferences.edit()
            editor.putString(MUSIC_CHOICE, musicChoice)
            editor.apply()
        }
    var musicName: String?
        get() = sharedPreferences.getString(MUSIC_NAME, null)
        set(musicName) {
            val editor = sharedPreferences.edit()
            editor.putString(MUSIC_NAME, musicName)
            editor.apply()
        }
    var isVibration: Boolean
        get() = sharedPreferences.getBoolean(VIBRATION, true)
        set(vibration) {
            val editor = sharedPreferences.edit()
            editor.putBoolean(VIBRATION, vibration)
            editor.apply()
        }
    var isAnimation: Boolean
        get() = sharedPreferences.getBoolean(ANIMATION, true)
        set(animation) {
            val editor = sharedPreferences.edit()
            editor.putBoolean(ANIMATION, animation)
            editor.apply()
        }
    var temperatureUnit: TemperatureUnit
        get() {
            val temperatureUnitString =
                sharedPreferences.getString(TEMPERATURE_UNIT, TemperatureUnit.CELSIUS.text)
            return TemperatureUnit.fromText(temperatureUnitString)
        }
        set(temperatureUnit) {
            val editor = sharedPreferences.edit()
            editor.putString(TEMPERATURE_UNIT, temperatureUnit.text)
            editor.apply()
        }
    var darkMode: DarkMode
        get() {
            val darkModeString = sharedPreferences.getString(DARK_MODE, DarkMode.SYSTEM.text)
            return DarkMode.fromText(darkModeString)
        }
        set(darkMode) {
            val editor = sharedPreferences.edit()
            editor.putString(DARK_MODE, darkMode.text)
            editor.apply()
        }
    var isOverviewUpdateAlert: Boolean
        get() = sharedPreferences.getBoolean(OVERVIEW_UPDATE_ALERT, false)
        set(overviewUpdateAlert) {
            val editor = sharedPreferences.edit()
            editor.putBoolean(OVERVIEW_UPDATE_ALERT, overviewUpdateAlert)
            editor.apply()
        }
    var isShowTeaAlert: Boolean
        get() = sharedPreferences.getBoolean(SHOW_TEA_ALERT, false)
        set(showTeaAlert) {
            val editor = sharedPreferences.edit()
            editor.putBoolean(SHOW_TEA_ALERT, showTeaAlert)
            editor.apply()
        }
    var sortMode: SortMode
        get() {
            val sortModeText = sharedPreferences.getString(SORT_MODE, SortMode.LAST_USED.text)
            return SortMode.fromText(sortModeText)
        }
        set(sortMode) {
            val editor = sharedPreferences.edit()
            editor.putString(SORT_MODE, sortMode.text)
            editor.apply()
        }
    var isOverviewHeader: Boolean
        get() = sharedPreferences.getBoolean(OVERVIEW_HEADER, false)
        set(overviewHeader) {
            val editor = sharedPreferences.edit()
            editor.putBoolean(OVERVIEW_HEADER, overviewHeader)
            editor.apply()
        }
    var isOverviewInStock: Boolean
        get() = sharedPreferences.getBoolean(OVERVIEW_IN_STOCK, false)
        set(overviewInStock) {
            val editor = sharedPreferences.edit()
            editor.putBoolean(OVERVIEW_IN_STOCK, overviewInStock)
            editor.apply()
        }

    fun setFactorySettings() {
        musicChoice = "content://settings/system/ringtone"
        musicName = "Default"
        isVibration = true
        isAnimation = true
        temperatureUnit = TemperatureUnit.CELSIUS
        darkMode = DarkMode.SYSTEM
        isOverviewUpdateAlert = false
        isShowTeaAlert = true
        sortMode = SortMode.LAST_USED
        isOverviewHeader = false
        isOverviewInStock = false
    }

    companion object {
        const val TEA_MEMORY_SETTINGS = "tea_memory_settings"
        private const val FIRST_START = "first_start"
        private const val SETTINGS_VERSION = "settings_version"
        private const val MUSIC_CHOICE = "music_choice"
        private const val MUSIC_NAME = "music_name"
        private const val VIBRATION = "vibration"
        private const val ANIMATION = "animation"
        private const val TEMPERATURE_UNIT = "temperature_unit"
        private const val DARK_MODE = "dark_mode"
        private const val OVERVIEW_UPDATE_ALERT = "overview_update_alert"
        private const val SHOW_TEA_ALERT = "show_tea_alert"
        private const val SORT_MODE = "sort_mode"
        private const val OVERVIEW_HEADER = "overview_header"
        private const val OVERVIEW_IN_STOCK = "overview_in_stock"
    }
}