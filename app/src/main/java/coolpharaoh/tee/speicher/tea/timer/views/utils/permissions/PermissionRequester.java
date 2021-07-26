package coolpharaoh.tee.speicher.tea.timer.views.utils.permissions;

import android.app.Activity;

import androidx.annotation.VisibleForTesting;

public class PermissionRequester {

    private PermissionRequester(){}

    private static Permissions permissions = new Permissions();

    public static boolean checkReadPermission(Activity activity) {
        return permissions.checkReadPermission(activity);
    }

    @VisibleForTesting
    public static void setMockedPermissions(Permissions mockedPermissions) {
        permissions = mockedPermissions;
    }
}
