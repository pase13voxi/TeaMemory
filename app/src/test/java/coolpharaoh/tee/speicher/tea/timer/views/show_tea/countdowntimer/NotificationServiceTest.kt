package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.content.Intent
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase.Companion.setMockedDatabase
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer.NotificationService
import org.assertj.core.api.Assertions.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class NotificationServiceTest {
    @JvmField
    @Rule
    var rule = MockitoJUnit.rule()

    @Mock
    lateinit var teaMemoryDatabase: TeaMemoryDatabase

    @Mock
    lateinit var teaDao: TeaDao

    @Test
    fun bind() {
        val controller = Robolectric.buildService(NotificationService::class.java)
        val notificationService = controller.create().bind().get()
        assertThat(notificationService).isNotNull
    }

    @Test
    fun startAndDestroy() {
        mockDB()

        val intent = Intent()
        intent.putExtra("teaId", 1L)
        val controller = Robolectric.buildService(NotificationService::class.java, intent)

        controller.startCommand(0, 0)

        val notificationService = controller.get()
        assertThat(notificationService.audioPlayer).isNotNull
        assertThat(notificationService.notifier).isNotNull
        assertThat(notificationService.vibrator).isNotNull

        controller.destroy()
        // Bad style cannot verify this comman
    }

    private fun mockDB() {
        setMockedDatabase(teaMemoryDatabase)
        `when`(teaMemoryDatabase.teaDao).thenReturn(teaDao)
        val tea = Tea()
        tea.name = "Tea"
        `when`(teaDao.getTeaById(1L)).thenReturn(tea)

        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())
        sharedSettings.musicChoice = null
        sharedSettings.isVibration = false
    }
}