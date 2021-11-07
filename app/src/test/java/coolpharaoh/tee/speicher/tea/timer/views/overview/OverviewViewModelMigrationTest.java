package coolpharaoh.tee.speicher.tea.timer.views.overview;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SortMode.LAST_USED;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SortMode;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

// Could be removed after the successful migration (In half a year 1.6.2022)
@RunWith(MockitoJUnitRunner.class)
public class OverviewViewModelMigrationTest {

    @Mock
    Application application;
    @Mock
    TeaRepository teaRepository;
    @Mock
    InfusionRepository infusionRepository;
    @Mock
    ActualSettingsRepository actualSettingsRepository;
    @Mock
    SharedSettings sharedSettings;
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Test
    public void migrateActualSettingsToSharedSettings() {
        when(actualSettingsRepository.getCountItems()).thenReturn(1);
        final ActualSettings actualSettings = new ActualSettings();
        actualSettings.setMusicChoice("MUSIC_CHOICE");
        actualSettings.setMusicName("MUSIC_NAME");
        actualSettings.setVibration(true);
        actualSettings.setAnimation(true);
        actualSettings.setTemperatureUnit("Celsius");
        actualSettings.setMainUpdateAlert(false);
        actualSettings.setShowTeaAlert(true);
        actualSettings.setSettingsPermissionAlert(true);
        actualSettings.setSort(2);
        when(actualSettingsRepository.getSettings()).thenReturn(actualSettings);
        when(sharedSettings.isMigrated()).thenReturn(false);
        when(sharedSettings.getSortMode()).thenReturn(LAST_USED);

        new OverviewViewModel(application, teaRepository, infusionRepository,
                actualSettingsRepository, sharedSettings);

        verify(sharedSettings).setFirstStart(false);
        verify(sharedSettings).setMusicChoice(actualSettings.getMusicChoice());
        verify(sharedSettings).setMusicName(actualSettings.getMusicName());
        verify(sharedSettings).setVibration(actualSettings.isVibration());
        verify(sharedSettings).setAnimation(actualSettings.isAnimation());
        verify(sharedSettings).setTemperatureUnit(TemperatureUnit.fromText(actualSettings.getTemperatureUnit()));
        verify(sharedSettings).setOverviewUpdateAlert(actualSettings.isMainUpdateAlert());
        verify(sharedSettings).setShowTeaAlert(actualSettings.isShowTeaAlert());
        verify(sharedSettings).setSettingsPermissionAlert(actualSettings.isSettingsPermissionAlert());
        verify(sharedSettings).setSortMode(SortMode.fromChoice(actualSettings.getSort()));
        verify(sharedSettings).setMigrated(true);
    }

}
