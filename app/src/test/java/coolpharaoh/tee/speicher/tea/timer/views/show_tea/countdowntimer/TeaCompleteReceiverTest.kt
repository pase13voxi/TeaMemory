package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.app.Application
import android.content.Intent
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
class TeaCompleteReceiverTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @RelaxedMockK
    lateinit var application: Application

    @Test
    fun onReceiveAfterAndroidO() {
        val teaCompleteReceiver = TeaCompleteReceiver()

        val intent = Intent()
        intent.putExtra(TEA_ID, 1L)
        teaCompleteReceiver.onReceive(application, intent)

        val slotIntent = slot<Intent>()
        verify { application.startForegroundService(capture(slotIntent)) }
        val service = slotIntent.captured

        assertThat(service.getLongExtra(TEA_ID, 0L)).isEqualTo(1L)
    }

    companion object {
        const val TEA_ID = "teaId"
    }
}