package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import org.assertj.core.api.Assertions.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BackgroundTimerTest {
    @JvmField
    @Rule
    var rule = MockitoJUnit.rule()

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var sharedTimerPreferences: SharedTimerPreferences

    @Mock
    lateinit var alarmManager: AlarmManager

    @Test
    fun setAlarmManager() {
        `when`(sharedTimerPreferences.startedTime).thenReturn(2000L)
        `when`(application.getSystemService(Context.ALARM_SERVICE)).thenReturn(alarmManager)

        val backgroundTimer = BackgroundTimer(application, sharedTimerPreferences)
        backgroundTimer.setAlarmManager(1L, 6000L)

        val captorAlarmClockInfo = ArgumentCaptor.forClass(AlarmClockInfo::class.java)
        verify(alarmManager).setAlarmClock(captorAlarmClockInfo.capture(), ArgumentMatchers.any(PendingIntent::class.java))

        val alarmClockInfo = captorAlarmClockInfo.value
        assertThat(alarmClockInfo.triggerTime).isEqualTo(8000L)
        assertThat(alarmClockInfo.showIntent).isNotNull
    }

    @Test(expected = NullPointerException::class)
    fun whenSetAlarmManagerAndItIsNullThrowException() {
        `when`(application.getSystemService(Context.ALARM_SERVICE)).thenReturn(null)

        val backgroundTimer = BackgroundTimer(application, sharedTimerPreferences)
        backgroundTimer.setAlarmManager(1L, 6000L)

        verify(alarmManager, times(0)).setAlarmClock(ArgumentMatchers.any(), ArgumentMatchers.any())
    }

    @Test
    fun removeAlarmManager() {
        `when`(application.getSystemService(Context.ALARM_SERVICE)).thenReturn(alarmManager)

        val backgroundTimer = BackgroundTimer(application, sharedTimerPreferences)
        backgroundTimer.removeAlarmManager()

        verify(alarmManager).cancel(ArgumentMatchers.any(PendingIntent::class.java))
    }

    @Test(expected = NullPointerException::class)
    fun whenRemoveAlarmManagerAndItIsNullThrowException() {
        `when`(application.getSystemService(Context.ALARM_SERVICE)).thenReturn(null)

        val backgroundTimer = BackgroundTimer(application, sharedTimerPreferences)
        backgroundTimer.removeAlarmManager()

        verify(alarmManager, times(0)).setAlarmClock(ArgumentMatchers.any(), ArgumentMatchers.any())
    }
}