package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.setFixedDate
import coolpharaoh.tee.speicher.tea.timer.core.date.DateUtility
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Calendar

@ExtendWith(MockKExtension::class)
internal class TimerControllerTest {
    @RelaxedMockK
    lateinit var application: Application

    @RelaxedMockK
    lateinit var sharedTimerPreferences: SharedTimerPreferences

    @RelaxedMockK
    lateinit var backgroundTimer: BackgroundTimer

    @RelaxedMockK
    lateinit var foregroundTimer: ForegroundTimer

    @MockK
    lateinit var dateUtility: DateUtility

    @InjectMockKs
    lateinit var timerController: TimerController

    @Test
    fun startForegroundTimer() {
        every { sharedTimerPreferences.startedTime } returns 0L

        timerController.startForegroundTimer(6000L, 1L)

        verify { foregroundTimer.start(any(), eq(6000L)) }
        verify { sharedTimerPreferences.startedTime = any() }
    }

    @Test
    fun startBackgroundTimer() {
        every { sharedTimerPreferences.startedTime } returns 0L

        timerController.startForegroundTimer(6000L, 1L)
        timerController.startBackgroundTimer()

        verify { foregroundTimer.cancel() }
        verify { backgroundTimer.setAlarmManager(1L, 6000L) }
    }

    @Test
    fun resumeForegroundTimer() {
        every { sharedTimerPreferences.startedTime } returns 0L
        timerController.startForegroundTimer(6000L, 1L)

        timerController.startBackgroundTimer()

        every { sharedTimerPreferences.startedTime } returns 1000L
        mockCurrentDate(2000L)
        timerController.resumeForegroundTimer()

        verify { backgroundTimer.removeAlarmManager() }
        verify { foregroundTimer.start(any(), eq(5000L)) }
        verify { sharedTimerPreferences.startedTime = any() }
    }

    @Test
    fun resumeFinishedForegroundTimer() {
        every { sharedTimerPreferences.startedTime } returns 0L
        timerController.startForegroundTimer(6000L, 1L)

        timerController.startBackgroundTimer()

        every { sharedTimerPreferences.startedTime } returns 1000L
        mockCurrentDate(8000L)
        timerController.resumeForegroundTimer()

        verify { sharedTimerPreferences.startedTime = 0L }
        verify { application.sendBroadcast(any()) }
    }

    @Test
    fun reset() {
        timerController.reset()

        verify { foregroundTimer.cancel() }
        verify { sharedTimerPreferences.startedTime = 0L }
        verify { backgroundTimer.removeAlarmManager() }
        verify { application.stopService(any()) }
    }

    @Test
    fun onTimerTick() {
        timerController.onTimerTick(6000L)

        verify { application.sendBroadcast(any()) }
    }

    @Test
    fun onTimerFinish() {
        timerController.onTimerFinish()

        verify { sharedTimerPreferences.startedTime = 0L }
        verify { application.sendBroadcast(any()) }
    }

    private fun mockCurrentDate(millis: Long) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        every { dateUtility.date } returns calendar.time

        setFixedDate(dateUtility)
    }
}