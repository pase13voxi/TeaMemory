package coolpharaoh.tee.speicher.tea.timer.views.utils.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

public class Permissions {
    public static final int REQUEST_CODE_READ = 7685;

    public boolean checkReadPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }
}
