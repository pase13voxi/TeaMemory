package coolpharaoh.tee.speicher.tea.timer.views.utils;

import android.app.Application;
import android.os.Build;

public class CustomResources {
    final Application application;

    public CustomResources(final Application application) {
        this.application = application;
    }

    // Can be removed once minimum sdk is greater than 22
    @SuppressWarnings("java:S1874")
    public int getColor(final int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return application.getResources().getColor(colorId, application.getTheme());
        } else {
            return application.getResources().getColor(colorId);
        }
    }
}
