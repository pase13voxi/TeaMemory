package coolpharaoh.tee.speicher.tea.timer.views.utils.permissions;

import android.app.Activity;

import androidx.annotation.VisibleForTesting;

public class PermissionRequester {

    private PermissionRequester(){}

    private static Permissions permissions = new Permissions();

    public static boolean checkReadPermission(Activity activity) {
        return permissions.checkReadPermission(activity);
    }

    public static boolean checkWritePermission(Activity activity) {
        return permissions.checkWritePermission(activity);
    }

    public static boolean checkReadPermissionDeniedBefore(Activity activity) {
        return permissions.checkReadPermissionDeniedBefore(activity);
    }

    public static boolean checkWritePermissionDeniedBefore(Activity activity) {
        return permissions.checkWritePermissionDeniedBefore(activity);
    }

    public static void getReadPermission(Activity activity) {
        permissions.getReadPermission(activity);

    }

    public static void getWritePermission(Activity activity) {
        permissions.getWritePermission(activity);

    }

    @VisibleForTesting
    public static void setMockedPermissions(Permissions mockedPermissions) {
        permissions = mockedPermissions;
    }
}
