package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.app.Application
import androidx.annotation.VisibleForTesting
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository

class TimerViewModel @VisibleForTesting constructor(private val teaRepository: TeaRepository, private val sharedSettings: SharedSettings) {
    constructor(application: Application) : this(TeaRepository(application), SharedSettings(application)) {}

    //teaDAO
    fun getName(teaId: Long): String? {
        return if (teaId == 0L) {
            "Default Tea"
        } else teaRepository.getTeaById(teaId)!!.name
    }

    //actualSettingsDAO
    val isVibration: Boolean
        get() = sharedSettings.isVibration

    val musicChoice: String?
        get() = sharedSettings.musicChoice
}