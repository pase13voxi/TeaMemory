package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.app.Application
import android.content.Intent
import org.assertj.core.api.Assertions.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TeaCompleteReceiverTest {
    @JvmField
    @Rule
    var rule = MockitoJUnit.rule()

    @Mock
    lateinit var application: Application

    @Test
    fun onReceiveAfterAndroidO() {
        val teaCompleteReceiver = TeaCompleteReceiver()

        val intent = Intent()
        intent.putExtra(TEA_ID, 1L)
        teaCompleteReceiver.onReceive(application, intent)

        val serviceCaptor = ArgumentCaptor.forClass(Intent::class.java)
        verify(application).startForegroundService(serviceCaptor.capture())
        val service = serviceCaptor.value

        assertThat(service.getLongExtra(TEA_ID, 0L)).isEqualTo(1L)
    }

    companion object {
        const val TEA_ID = "teaId"
    }
}