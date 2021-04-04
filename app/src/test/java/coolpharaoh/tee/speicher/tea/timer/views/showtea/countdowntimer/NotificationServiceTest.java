package coolpharaoh.tee.speicher.tea.timer.views.showtea.countdowntimer;

import android.content.Intent;
import android.os.Build;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ServiceController;
import org.robolectric.annotation.Config;

import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class NotificationServiceTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    TeaDao teaDao;
    @Mock
    ActualSettingsDao actualSettingsDao;

    @Test
    public void bind() {
        ServiceController<NotificationService> controller = Robolectric.buildService(NotificationService.class);
        NotificationService notificationService = controller.create().bind().get();
        assertThat(notificationService).isNotNull();
    }

    @Test
    public void startAndDestroy() {
        mockDB();

        Intent intent = new Intent();
        intent.putExtra("teaId", 1L);
        ServiceController<NotificationService> controller = Robolectric.buildService(NotificationService.class, intent);

        controller.startCommand(0, 0);

        NotificationService notificationService = controller.get();
        assertThat(notificationService.audioPlayer).isNotNull();
        assertThat(notificationService.notifier).isNotNull();
        assertThat(notificationService.vibrator).isNotNull();

        controller.destroy();
        // Bad style cannot verify this comman
    }

    private void mockDB() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDao);
        when(teaMemoryDatabase.getActualSettingsDao()).thenReturn(actualSettingsDao);
        Tea tea = new Tea();
        tea.setName("Tea");
        when(teaDao.getTeaById(1L)).thenReturn(tea);
        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setVibration(false);
        actualSettings.setMusicChoice(null);
        when(actualSettingsDao.getSettings()).thenReturn(actualSettings);
    }
}
