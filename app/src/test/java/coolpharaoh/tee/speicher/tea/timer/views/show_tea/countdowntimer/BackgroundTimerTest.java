package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class BackgroundTimerTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    Application application;
    @Mock
    SharedTimerPreferences sharedTimerPreferences;
    @Mock
    AlarmManager alarmManager;

    @Test
    public void setAlarmManager() {
        when(sharedTimerPreferences.getStartedTime()).thenReturn(2000L);
        when(application.getSystemService(Context.ALARM_SERVICE)).thenReturn(alarmManager);

        BackgroundTimer backgroundTimer = new BackgroundTimer(application, sharedTimerPreferences);
        backgroundTimer.setAlarmManager(1L, 6000L);

        ArgumentCaptor<AlarmManager.AlarmClockInfo> captorAlarmClockInfo = ArgumentCaptor.forClass(AlarmManager.AlarmClockInfo.class);
        verify(alarmManager).setAlarmClock(captorAlarmClockInfo.capture(), any(PendingIntent.class));

        AlarmManager.AlarmClockInfo alarmClockInfo = captorAlarmClockInfo.getValue();
        assertThat(alarmClockInfo.getTriggerTime()).isEqualTo(8000L);
        assertThat(alarmClockInfo.getShowIntent()).isNotNull();
    }

    @Test(expected = NullPointerException.class)
    public void whenSetAlarmManagerAndItIsNullThrowException() {
        when(application.getSystemService(Context.ALARM_SERVICE)).thenReturn(null);

        BackgroundTimer backgroundTimer = new BackgroundTimer(application, sharedTimerPreferences);
        backgroundTimer.setAlarmManager(1L, 6000L);

        verify(alarmManager, times(0)).setAlarmClock(any(), any());
    }

    @Test
    public void removeAlarmManager() {
        when(application.getSystemService(Context.ALARM_SERVICE)).thenReturn(alarmManager);

        BackgroundTimer backgroundTimer = new BackgroundTimer(application, sharedTimerPreferences);
        backgroundTimer.removeAlarmManager();

        verify(alarmManager).cancel(any(PendingIntent.class));
    }

    @Test(expected = NullPointerException.class)
    public void whenRemoveAlarmManagerAndItIsNullThrowException() {
        when(application.getSystemService(Context.ALARM_SERVICE)).thenReturn(null);

        BackgroundTimer backgroundTimer = new BackgroundTimer(application, sharedTimerPreferences);
        backgroundTimer.removeAlarmManager();

        verify(alarmManager, times(0)).setAlarmClock(any(), any());
    }
}
