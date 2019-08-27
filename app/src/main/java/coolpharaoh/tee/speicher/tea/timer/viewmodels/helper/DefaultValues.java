package coolpharaoh.tee.speicher.tea.timer.viewmodels.helper;

import coolpharaoh.tee.speicher.tea.timer.entities.ActualSettings;

public class DefaultValues {
    public static ActualSettings createDefaultSettings(){
        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setMusicchoice("content://settings/system/ringtone");
        actualSettings.setMusicname("Default");
        actualSettings.setVibration(false);
        actualSettings.setNotification(true);
        actualSettings.setAnimation(true);
        actualSettings.setTemperatureunit("Celsius");
        actualSettings.setShowteaalert(true);
        actualSettings.setMainproblemalert(true);
        actualSettings.setMainratealert(true);
        actualSettings.setMainratecounter(0);
        actualSettings.setSort(0);
        return actualSettings;
    }
}
