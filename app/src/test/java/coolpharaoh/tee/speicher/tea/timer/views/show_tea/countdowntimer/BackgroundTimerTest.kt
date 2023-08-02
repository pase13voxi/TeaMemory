package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BackgroundTimerTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @RelaxedMockK
    lateinit var application: Application
    @RelaxedMockK
    lateinit var sharedTimerPreferences: SharedTimerPreferences
    @RelaxedMockK
    lateinit var alarmManager: AlarmManager

    @Test
    fun setAlarmManager() {
        every { sharedTimerPreferences.startedTime } returns 2000L
        every { application.getSystemService(Context.ALARM_SERVICE) } returns alarmManager

        val backgroundTimer = BackgroundTimer(application, sharedTimerPreferences)
        backgroundTimer.setAlarmManager(1L, 6000L)

        val slotAlarmClockInfo = slot<AlarmClockInfo>()
        verify { alarmManager.setAlarmClock(capture(slotAlarmClockInfo),any()) }

        val alarmClockInfo = slotAlarmClockInfo.captured
        assertThat(alarmClockInfo.triggerTime).isEqualTo(8000L)
        assertThat(alarmClockInfo.showIntent).isNotNull
    }

    @Test(expected = NullPointerException::class)
    fun whenSetAlarmManagerAndItIsNullThrowException() {
        every { application.getSystemService(Context.ALARM_SERVICE) } returns null

        val backgroundTimer = BackgroundTimer(application, sharedTimerPreferences)
        backgroundTimer.setAlarmManager(1L, 6000L)

        verify (exactly = 0) { alarmManager.setAlarmClock(any(), any()) }
    }

    @Test
    fun removeAlarmManager() {
        every { application.getSystemService(Context.ALARM_SERVICE) } returns alarmManager

        val backgroundTimer = BackgroundTimer(application, sharedTimerPreferences)
        backgroundTimer.removeAlarmManager()

        verify { alarmManager.cancel(any<PendingIntent>()) }
    }

    @Test(expected = NullPointerException::class)
    fun whenRemoveAlarmManagerAndItIsNullThrowException() {
        every { application.getSystemService(Context.ALARM_SERVICE) } returns null

        val backgroundTimer = BackgroundTimer(application, sharedTimerPreferences)
        backgroundTimer.removeAlarmManager()

        verify (exactly = 1) { alarmManager.setAlarmClock(any(), any()) }
    }
}