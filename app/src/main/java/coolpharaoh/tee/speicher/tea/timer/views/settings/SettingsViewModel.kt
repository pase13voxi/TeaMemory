package coolpharaoh.tee.speicher.tea.timer.views.settings

import android.app.Application
import androidx.annotation.VisibleForTesting
import coolpharaoh.tee.speicher.tea.timer.core.settings.DarkMode
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageControllerFactory.getImageController
import java.util.function.Consumer

class SettingsViewModel @VisibleForTesting constructor(
    private val teaRepository: TeaRepository,
    private val sharedSettings: SharedSettings,
    private val imageController: ImageController?
) {
    constructor(application: Application?) : this(TeaRepository(application!!),
        SharedSettings(application), getImageController(application))

    //Settings
    fun setMusicChoice(musicChoice: String?) {
        sharedSettings.musicChoice = musicChoice
    }

    var musicName: String?
        get() = sharedSettings.musicName
        set(musicName) {
            sharedSettings.musicName = musicName
        }

    var isVibration: Boolean
        get() = sharedSettings.isVibration
        set(vibration) {
            sharedSettings.isVibration = vibration
        }

    var isAnimation: Boolean
        get() = sharedSettings.isAnimation
        set(animation) {
            sharedSettings.isAnimation = animation
        }

    var temperatureUnit: TemperatureUnit?
        get() = sharedSettings.temperatureUnit
        set(temperatureUnit) {
            sharedSettings.temperatureUnit = temperatureUnit!!
        }

    var isOverviewHeader: Boolean
        get() = sharedSettings.isOverviewHeader
        set(overviewHeader) {
            sharedSettings.isOverviewHeader = overviewHeader
        }

    var darkMode: DarkMode?
        get() = sharedSettings.darkMode
        set(darkMode) {
            sharedSettings.darkMode = darkMode!!
        }

    var isShowTeaAlert: Boolean
        get() = sharedSettings.isShowTeaAlert
        set(showTeaAlert) {
            sharedSettings.isShowTeaAlert = showTeaAlert
        }

    var overviewUpdateAlert: Boolean
        get() = sharedSettings.isOverviewUpdateAlert
        set(overviewUpdateAlert) {
            sharedSettings.isOverviewUpdateAlert = overviewUpdateAlert
        }

    fun setDefaultSettings() {
        sharedSettings.setFactorySettings()
    }

    //Tea
    fun deleteAllTeaImages() {
        val teas = teaRepository.teas
        teas.forEach(Consumer { (id): Tea ->
            imageController!!.removeImageByTeaId(
                id!!
            )
        })
    }

    fun deleteAllTeas() {
        teaRepository.deleteAllTeas()
    }
}