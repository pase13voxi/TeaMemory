package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ServiceController;
import org.robolectric.annotation.Config;

import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

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

    @Test
    public void bind() {
        final ServiceController<NotificationService> controller = Robolectric.buildService(NotificationService.class);
        final NotificationService notificationService = controller.create().bind().get();
        assertThat(notificationService).isNotNull();
    }

    @Test
    public void startAndDestroy() {
        mockDB();

        final Intent intent = new Intent();
        intent.putExtra("teaId", 1L);
        final ServiceController<NotificationService> controller = Robolectric.buildService(NotificationService.class, intent);

        controller.startCommand(0, 0);

        final NotificationService notificationService = controller.get();
        assertThat(notificationService.audioPlayer).isNotNull();
        assertThat(notificationService.notifier).isNotNull();
        assertThat(notificationService.vibrator).isNotNull();

        controller.destroy();
        // Bad style cannot verify this comman
    }

    private void mockDB() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDao);
        final Tea tea = new Tea();
        tea.setName("Tea");
        when(teaDao.getTeaById(1L)).thenReturn(tea);

        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());
        sharedSettings.setMusicChoice(null);
        sharedSettings.setVibration(false);
    }
}
