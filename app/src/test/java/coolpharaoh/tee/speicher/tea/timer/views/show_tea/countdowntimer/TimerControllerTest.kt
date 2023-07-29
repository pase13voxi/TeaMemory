package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.setFixedDate
import coolpharaoh.tee.speicher.tea.timer.core.date.DateUtility
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.util.Calendar

@ExtendWith(MockitoExtension::class)
internal class TimerControllerTest {
    @Mock
    lateinit var application: Application

    @Mock
    lateinit var sharedTimerPreferences: SharedTimerPreferences

    @Mock
    lateinit var backgroundTimer: BackgroundTimer

    @Mock
    lateinit var foregroundTimer: ForegroundTimer

    @Mock
    lateinit var dateUtility: DateUtility

    @InjectMocks
    lateinit var timerController: TimerController

    @Test
    fun startForegroundTimer() {
        `when`(sharedTimerPreferences.startedTime).thenReturn(0L)

        timerController.startForegroundTimer(6000L, 1L)

        verify(foregroundTimer).start(any(), ArgumentMatchers.eq(6000L))
        verify(sharedTimerPreferences).startedTime = ArgumentMatchers.anyLong()
    }

    @Test
    fun startBackgroundTimer() {
        `when`(sharedTimerPreferences.startedTime).thenReturn(0L)

        timerController.startForegroundTimer(6000L, 1L)
        timerController.startBackgroundTimer()

        verify(foregroundTimer).cancel()
        verify(backgroundTimer).setAlarmManager(1L, 6000L)
    }

    @Test
    fun resumeForegroundTimer() {
        `when`(sharedTimerPreferences.startedTime).thenReturn(0L)
        timerController.startForegroundTimer(6000L, 1L)

        timerController.startBackgroundTimer()

        `when`(sharedTimerPreferences.startedTime).thenReturn(1000L)
        mockCurrentDate(2000L)
        timerController.resumeForegroundTimer()

        verify(backgroundTimer).removeAlarmManager()
        verify(foregroundTimer).start(any(), ArgumentMatchers.eq(5000L))
        verify(sharedTimerPreferences).startedTime = ArgumentMatchers.anyLong()
    }

    @Test
    fun resumeFinishedForegroundTimer() {
        `when`(sharedTimerPreferences.startedTime).thenReturn(0L)
        timerController.startForegroundTimer(6000L, 1L)

        timerController.startBackgroundTimer()

        `when`(sharedTimerPreferences.startedTime).thenReturn(1000L)
        mockCurrentDate(8000L)
        timerController.resumeForegroundTimer()

        verify(sharedTimerPreferences).startedTime = 0L
        verify(application).sendBroadcast(any())
    }

    @Test
    fun reset() {
        timerController.reset()

        verify(foregroundTimer).cancel()
        verify(sharedTimerPreferences).startedTime = 0L
        verify(backgroundTimer).removeAlarmManager()
        verify(application).stopService(any())
    }

    @Test
    fun onTimerTick() {
        timerController.onTimerTick(6000L)

        verify(application).sendBroadcast(any())
    }

    @Test
    fun onTimerFinish() {
        timerController.onTimerFinish()

        verify(sharedTimerPreferences).startedTime = 0L
        verify(application).sendBroadcast(any())
    }

    private fun mockCurrentDate(millis: Long) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        `when`(dateUtility.date).thenReturn(calendar.time)

        setFixedDate(dateUtility)
    }
}