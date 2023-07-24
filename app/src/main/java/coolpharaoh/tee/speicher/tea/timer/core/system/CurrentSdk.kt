package coolpharaoh.tee.speicher.tea.timer.core.system

import androidx.annotation.VisibleForTesting

object CurrentSdk {
    private var systemUtility = SystemUtility()
    @JvmStatic
    @VisibleForTesting
    fun setFixedSystem(fixedSystem: SystemUtility) {
        systemUtility = fixedSystem
    }

    @JvmStatic
    val sdkVersion: Int
        get() = systemUtility.sdkVersion
}