package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import coolpharaoh.tee.speicher.tea.timer.R
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NotifierTest {
    @JvmField
    @Rule
    var rule = MockitoJUnit.rule()

    @Mock
    lateinit var timerViewModel: TimerViewModel

    @Before
    fun setUp() {
        `when`(timerViewModel.getName(1L)).thenReturn(TEA_NAME)
    }

    @Test
    fun getNotificationAfterAndroidO() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        val notifier = Notifier(application, 1L, timerViewModel)
        val notification = notifier.notification

        assertThat(notification.channelId).isEqualTo(CHANNEL_ID_NOTIFY)
        assertThat(notification.tickerText).isEqualTo(application.getString(R.string.show_tea_notification_ticker))
        assertThat(notification.extras["android.title"]).isEqualTo(application.getString(R.string.show_tea_notification_title))
        assertThat(notification.extras["android.text"]).isEqualTo(TEA_NAME)
    }

    companion object {
        const val CHANNEL_ID_NOTIFY = "3422"
        const val TEA_NAME = "Earl Grey"
    }
}