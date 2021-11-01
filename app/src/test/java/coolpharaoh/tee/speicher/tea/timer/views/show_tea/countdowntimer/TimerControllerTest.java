package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Application;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;

import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.date.DateUtility;

@RunWith(MockitoJUnitRunner.class)
public class TimerControllerTest {
    @Mock
    Application application;
    @Mock
    SharedTimerPreferences sharedTimerPreferences;
    @Mock
    BackgroundTimer backgroundTimer;
    @Mock
    ForegroundTimer foregroundTimer;
    @Mock
    DateUtility dateUtility;

    @Test
    public void startForegroundTimer() {
        final TimerController timerController = new TimerController(application, foregroundTimer,
                sharedTimerPreferences, backgroundTimer);
        when(sharedTimerPreferences.getStartedTime()).thenReturn(0L);

        timerController.startForegroundTimer(6000L, 1L);

        verify(foregroundTimer).start(any(TimerController.class), eq(6000L));
        verify(sharedTimerPreferences).setStartedTime(anyLong());
    }

    @Test
    public void startBackgroundTimer() {
        final TimerController timerController = new TimerController(application, foregroundTimer,
                sharedTimerPreferences, backgroundTimer);
        when(sharedTimerPreferences.getStartedTime()).thenReturn(0L);

        timerController.startForegroundTimer(6000L, 1L);
        timerController.startBackgroundTimer();

        verify(foregroundTimer).cancel();
        verify(backgroundTimer).setAlarmManager(1L, 6000L);
    }

    @Test
    public void resumeForegroundTimer() {
        final TimerController timerController = new TimerController(application, foregroundTimer,
                sharedTimerPreferences, backgroundTimer);

        when(sharedTimerPreferences.getStartedTime()).thenReturn(0L);
        timerController.startForegroundTimer(6000L, 1L);

        timerController.startBackgroundTimer();

        when(sharedTimerPreferences.getStartedTime()).thenReturn(1000L);
        mockCurrentDate(2000L);
        timerController.resumeForegroundTimer();

        verify(backgroundTimer).removeAlarmManager();
        verify(foregroundTimer).start(any(TimerController.class), eq(5000L));
        verify(sharedTimerPreferences).setStartedTime(anyLong());
    }

    @Test
    public void resumeFinishedForegroundTimer() {
        final TimerController timerController = new TimerController(application, foregroundTimer,
                sharedTimerPreferences, backgroundTimer);

        when(sharedTimerPreferences.getStartedTime()).thenReturn(0L);
        timerController.startForegroundTimer(6000L, 1L);

        timerController.startBackgroundTimer();

        when(sharedTimerPreferences.getStartedTime()).thenReturn(1000L);
        mockCurrentDate(8000L);
        timerController.resumeForegroundTimer();

        verify(sharedTimerPreferences).setStartedTime(0L);
        verify(application).sendBroadcast(any());
    }

    @Test
    public void reset() {
        final TimerController timerController = new TimerController(application, foregroundTimer,
                sharedTimerPreferences, backgroundTimer);

        timerController.reset();

        verify(foregroundTimer).cancel();
        verify(sharedTimerPreferences).setStartedTime(0L);
        verify(backgroundTimer).removeAlarmManager();
        verify(application).stopService(any());
    }

    @Test
    public void onTimerTick() {
        final TimerController timerController = new TimerController(application, foregroundTimer,
                sharedTimerPreferences, backgroundTimer);

        timerController.onTimerTick(6000L);

        verify(application).sendBroadcast(any());
    }

    @Test
    public void onTimerFinish() {
        final TimerController timerController = new TimerController(application, foregroundTimer,
                sharedTimerPreferences, backgroundTimer);

        timerController.onTimerFinish();

        verify(sharedTimerPreferences).setStartedTime(0L);
        verify(application).sendBroadcast(any());
    }

    private void mockCurrentDate(final long millis) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        when(dateUtility.getDate()).thenReturn(calendar.getTime());

        CurrentDate.setFixedDate(dateUtility);
    }
}
