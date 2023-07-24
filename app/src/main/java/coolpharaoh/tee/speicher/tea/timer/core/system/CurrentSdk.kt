package coolpharaoh.tee.speicher.tea.timer.core.system;

import androidx.annotation.VisibleForTesting;

public class CurrentSdk {

    private CurrentSdk() {
    }

    private static SystemUtility systemUtility = new SystemUtility();

    @VisibleForTesting
    public static void setFixedSystem(final SystemUtility fixedSystem) {
        systemUtility = fixedSystem;
    }

    public static int getSdkVersion() {
        return systemUtility.getSdkVersion();
    }
}
