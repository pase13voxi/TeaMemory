package coolpharaoh.tee.speicher.tea.timer

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettingsMigration
import coolpharaoh.tee.speicher.tea.timer.views.utils.ThemeManager

class TeaMemory : Application() {

    var overviewDialogsShown = false

    override fun onCreate() {
        super.onCreate()
        val sharedSettingsMigration = SharedSettingsMigration(this)
        sharedSettingsMigration.migrate()
        val sharedSettings = SharedSettings(this)
        ThemeManager.applyTheme(sharedSettings.darkMode)
    }
}