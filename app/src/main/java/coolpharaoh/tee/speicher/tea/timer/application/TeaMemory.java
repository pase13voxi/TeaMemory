package coolpharaoh.tee.speicher.tea.timer.application;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.views.utils.ThemeManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeaMemory extends Application {
    boolean mainDialogsShown = false;

    @Override
    public void onCreate() {
        super.onCreate();

        SharedSettings sharedSettings = new SharedSettings(this);
        ThemeManager.applyTheme(sharedSettings.getDarkMode());
    }
}
