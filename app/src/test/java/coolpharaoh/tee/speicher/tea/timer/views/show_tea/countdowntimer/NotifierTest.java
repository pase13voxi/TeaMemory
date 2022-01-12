package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.app.Notification;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import coolpharaoh.tee.speicher.tea.timer.R;

@RunWith(RobolectricTestRunner.class)
public class NotifierTest {
    public static final String CHANNEL_ID_NOTIFY = "3422";
    public static final String TEA_NAME = "Earl Grey";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    TimerViewModel timerViewModel;

    @Before
    public void setUp() {
        when(timerViewModel.getName(1L)).thenReturn(TEA_NAME);
    }

    @Test
    public void getNotificationAfterAndroidO() {
        final Application application = ApplicationProvider.getApplicationContext();
        final Notifier notifier = new Notifier(application, 1L, timerViewModel);
        final Notification notification = notifier.getNotification();

        assertThat(notification.getChannelId()).isEqualTo(CHANNEL_ID_NOTIFY);
        assertThat(notification.tickerText).isEqualTo(application.getString(R.string.show_tea_notification_ticker));
        assertThat(notification.extras.get("android.title")).isEqualTo(application.getString(R.string.show_tea_notification_title));
        assertThat(notification.extras.get("android.text")).isEqualTo(TEA_NAME);
    }
}
