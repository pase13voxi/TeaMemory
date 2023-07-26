package coolpharaoh.tee.speicher.tea.timer.views.settings;

import android.app.Application;

import androidx.annotation.VisibleForTesting;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.settings.DarkMode;
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController;
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageControllerFactory;

class SettingsViewModel {

    private final TeaRepository teaRepository;
    private final SharedSettings sharedSettings;
    private final ImageController imageController;

    public SettingsViewModel(final Application application) {
        this(new TeaRepository(application), new SharedSettings(application),
                ImageControllerFactory.getImageController(application));
    }

    @VisibleForTesting
    public SettingsViewModel(final TeaRepository teaRepository,
                             final SharedSettings sharedSettings,
                             final ImageController imageController) {
        this.teaRepository = teaRepository;
        this.sharedSettings = sharedSettings;
        this.imageController = imageController;
    }

    //Settings
    void setMusicChoice(final String musicChoice) {
        sharedSettings.setMusicChoice(musicChoice);
    }

    String getMusicName() {
        return sharedSettings.getMusicName();
    }

    void setMusicName(final String musicName) {
        sharedSettings.setMusicName(musicName);
    }

    boolean isVibration() {
        return sharedSettings.isVibration();
    }

    void setVibration(final boolean vibration) {
        sharedSettings.setVibration(vibration);
    }

    boolean isAnimation() {
        return sharedSettings.isAnimation();
    }

    void setAnimation(final boolean animation) {
        sharedSettings.setAnimation(animation);
    }

    TemperatureUnit getTemperatureUnit() {
        return sharedSettings.getTemperatureUnit();
    }

    void setTemperatureUnit(final TemperatureUnit temperatureUnit) {
        sharedSettings.setTemperatureUnit(temperatureUnit);
    }

    void setOverviewHeader(final boolean overviewHeader) {
        sharedSettings.setOverviewHeader(overviewHeader);
    }

    boolean isOverviewHeader() {
        return sharedSettings.isOverviewHeader();
    }

    void setDarkMode(final DarkMode darkMode) {
        sharedSettings.setDarkMode(darkMode);
    }

    DarkMode getDarkMode() {
        return sharedSettings.getDarkMode();
    }

    boolean isShowTeaAlert() {
        return sharedSettings.isShowTeaAlert();
    }

    void setShowTeaAlert(final boolean showTeaAlert) {
        sharedSettings.setShowTeaAlert(showTeaAlert);
    }

    boolean isMainUpdateAlert() {
        return sharedSettings.isOverviewUpdateAlert();
    }

    void setOverviewUpdateAlert(final boolean overviewUpdateAlert) {
        sharedSettings.setOverviewUpdateAlert(overviewUpdateAlert);
    }

    void setDefaultSettings() {
        sharedSettings.setFactorySettings();
    }

    //Tea
    void deleteAllTeaImages() {
        final List<Tea> teas = teaRepository.getTeas();
        teas.forEach(tea -> imageController.removeImageByTeaId(tea.getId()));
    }

    void deleteAllTeas() {
        teaRepository.deleteAllTeas();
    }
}
