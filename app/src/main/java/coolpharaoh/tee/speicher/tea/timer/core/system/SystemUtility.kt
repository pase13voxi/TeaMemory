package coolpharaoh.tee.speicher.tea.timer.core.system

import android.os.Build

class SystemUtility {
    val sdkVersion: Int
        get() = Build.VERSION.SDK_INT
}