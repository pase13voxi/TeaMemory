package coolpharaoh.tee.speicher.tea.timer.views.show_tea

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.getDate
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase.Companion.setMockedDatabase
import coolpharaoh.tee.speicher.tea.timer.views.description.ShowTeaDescription
import coolpharaoh.tee.speicher.tea.timer.views.information.Information
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.NewTea
import coolpharaoh.tee.speicher.tea.timer.views.overview.Overview
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer.TimerController
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.fakes.RoboMenuItem
import org.robolectric.shadows.ShadowAlertDialog

@RunWith(RobolectricTestRunner::class)
class ShowTeaTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    lateinit var teaMemoryDatabase: TeaMemoryDatabase
    @RelaxedMockK
    lateinit var teaDao: TeaDao
    @MockK
    lateinit var infusionDao: InfusionDao
    @RelaxedMockK
    lateinit var counterDao: CounterDao

    var tea: Tea? = null
    private var infusions: ArrayList<Infusion> = ArrayList()
    var counter: Counter? = null

    @Test
    fun launchActivityWithNoTeaIdAndExpectFailingDialog() {
        val newTeaActivityScenario = ActivityScenario.launch(ShowTea::class.java)
        newTeaActivityScenario.onActivity { showTea: ShowTea ->
            val dialogFail = ShadowAlertDialog.getLatestAlertDialog()
            checkTitleAndMessageOfLatestDialog(showTea, dialogFail, R.string.show_tea_dialog_tea_missing_header,
                R.string.show_tea_dialog_tea_missing_description)
            dialogFail.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val expected = Intent(showTea, Overview::class.java)
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.component).isEqualTo(expected.component)
        }
    }

    @Test
    fun launchActivityWithNotExistingTeaIdExpectFailingDialog() {
        mockDB()
        every { teaDao.getTeaById(5L) } returns null

        val intent = Intent(RuntimeEnvironment.getApplication(), ShowTea::class.java)
        intent.putExtra(TEA_ID_EXTRA, 5L)

        val showTeaActivityScenario = ActivityScenario.launch<ShowTea>(intent)
        showTeaActivityScenario.onActivity { showTea: ShowTea ->
            val dialogFail = ShadowAlertDialog.getLatestAlertDialog()
            checkTitleAndMessageOfLatestDialog(showTea, dialogFail, R.string.show_tea_dialog_tea_missing_header,
                R.string.show_tea_dialog_tea_missing_description)
            dialogFail.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val expected = Intent(showTea, Overview::class.java)
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.component).isEqualTo(expected.component)
        }
    }

    @Test
    fun launchActivityAndExpectDescriptionDialog() {
        mockDB()
        mockTea(VARIETY, 1, AmountKind.TEA_SPOON.text, 0)
        mockInfusions(listOf("1:00"), listOf<String?>(null), listOf(100), listOf(212))
        setSharedSettings(TemperatureUnit.CELSIUS, true, false)

        val intent = Intent(RuntimeEnvironment.getApplication(), ShowTea::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val showTeaActivityScenario = ActivityScenario.launch<ShowTea>(intent)
        showTeaActivityScenario.onActivity { showTea: ShowTea ->
            val dialogDescription = ShadowAlertDialog.getLatestAlertDialog()
            checkTitleAndMessageOfLatestDialog(showTea, dialogDescription, R.string.show_tea_dialog_description_header)

            val checkBox = dialogDescription.findViewById<CheckBox>(R.id.check_box_show_tea_dialog_description)
            checkBox.isChecked = true

            dialogDescription.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val sharedSettings = SharedSettings(showTea.application)
            assertThat(sharedSettings.isShowTeaAlert).isFalse

            val expected = Intent(showTea, ShowTeaDescription::class.java)
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.data).isEqualTo(expected.data)
        }
    }

    @Test
    fun launchActivityAndExpectDisplayNextInfusionDialog() {
        mockDB()
        mockTea(VARIETY, 1, AmountKind.TEA_SPOON.text, 2)
        mockInfusions(listOf("1:00", "2:00", "3:00"), listOf<String?>(null, null, null), listOf(100, 100, 90), listOf(212, 212, 176))
        setSharedSettings(TemperatureUnit.CELSIUS, false, false)

        val intent = Intent(RuntimeEnvironment.getApplication(), ShowTea::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val showTeaActivityScenario = ActivityScenario.launch<ShowTea>(intent)
        showTeaActivityScenario.onActivity { showTea: ShowTea ->
            val dialogNextInfusion = ShadowAlertDialog.getLatestAlertDialog()
            checkTitleAndMessageOfLatestDialog(showTea, dialogNextInfusion, R.string.show_tea_dialog_following_infusion_header, showTea.getString(R.string.show_tea_dialog_following_infusion_description, 2, 3))

            dialogNextInfusion.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val textViewInfusionIndex = showTea.findViewById<TextView>(R.id.show_tea_tool_bar_text_infusion_index)
            val textViewTemperature = showTea.findViewById<TextView>(R.id.text_view_show_tea_temperature)
            val spinnerMinutes = showTea.findViewById<Spinner>(R.id.spinner_show_tea_minutes)
            val spinnerSeconds = showTea.findViewById<Spinner>(R.id.spinner_show_tea_seconds)

            assertThat(textViewInfusionIndex.text).hasToString("3.")
            assertThat(textViewTemperature.text).isEqualTo(infusions[2].temperatureCelsius.toString() + " °C")
            assertThat(spinnerMinutes.selectedItem).hasToString("03")
            assertThat(spinnerSeconds.selectedItem).hasToString("00")
        }
    }

    @Test
    fun displayNextInfusionDialogClickCancelAndExpectNextInfusionZero() {
        mockDB()
        mockTea(VARIETY, 1, AmountKind.TEA_SPOON.text, 2)
        mockInfusions(listOf("1:00", "2:00", "3:00"), listOf<String?>(null, null, null), listOf(100, 100, 90), listOf(212, 212, 176))
        setSharedSettings(TemperatureUnit.CELSIUS, false, false)

        val intent = Intent(RuntimeEnvironment.getApplication(), ShowTea::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val showTeaActivityScenario = ActivityScenario.launch<ShowTea>(intent)
        showTeaActivityScenario.onActivity { showTea: ShowTea ->
            val dialogNextInfusion = ShadowAlertDialog.getLatestAlertDialog()
            checkTitleAndMessageOfLatestDialog(showTea, dialogNextInfusion, R.string.show_tea_dialog_following_infusion_header, showTea.getString(R.string.show_tea_dialog_following_infusion_description, 2, 3))

            dialogNextInfusion.getButton(DialogInterface.BUTTON_NEGATIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val teaSlot = slot<Tea>()
            verify { teaDao.update(capture(teaSlot)) }

            assertThat(teaSlot.captured.nextInfusion).isZero()
        }
    }

    @Test
    fun launchActivityWithCelsiusAndTeaSpoonStandardValuesAndExpectFilledActivity() {
        mockDB()
        mockTea(VARIETY, 1, AmountKind.TEA_SPOON.text, 0)
        mockInfusions(listOf("1:00"), listOf<String?>(null), listOf(100), listOf(212))
        setSharedSettings(TemperatureUnit.CELSIUS, false, false)

        val intent = Intent(RuntimeEnvironment.getApplication(), ShowTea::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val showTeaActivityScenario = ActivityScenario.launch<ShowTea>(intent)
        showTeaActivityScenario.onActivity { showTea: ShowTea ->
            val buttonInfusionIndex = showTea.findViewById<ImageButton>(R.id.show_tea_tool_bar_infusion_index)
            val textViewInfusionIndex = showTea.findViewById<TextView>(R.id.show_tea_tool_bar_text_infusion_index)
            val buttonNextInfusion = showTea.findViewById<ImageButton>(R.id.show_tea_tool_bar_next_infusion)
            val textViewName = showTea.findViewById<TextView>(R.id.text_view_show_tea_name)
            val textViewVariety = showTea.findViewById<TextView>(R.id.text_view_show_tea_variety)
            val textViewTemperature = showTea.findViewById<TextView>(R.id.text_view_show_tea_temperature)
            val textViewAmount = showTea.findViewById<TextView>(R.id.text_view_show_tea_amount)
            val spinnerMinutes = showTea.findViewById<Spinner>(R.id.spinner_show_tea_minutes)
            val spinnerSeconds = showTea.findViewById<Spinner>(R.id.spinner_show_tea_seconds)

            assertThat(buttonInfusionIndex.visibility).isEqualTo(View.GONE)
            assertThat(textViewInfusionIndex.visibility).isEqualTo(View.GONE)
            assertThat(buttonNextInfusion.visibility).isEqualTo(View.GONE)
            assertThat(textViewName.text).isEqualTo(tea!!.name)
            assertThat(textViewVariety.text).isEqualTo(tea!!.variety)
            assertThat(textViewTemperature.text).isEqualTo(infusions[0].temperatureCelsius.toString() + " °C")
            assertThat(textViewAmount.text).contains(tea!!.amount.toInt().toString() + " ts/l")
            assertThat(spinnerMinutes.selectedItem).hasToString("01")
            assertThat(spinnerSeconds.selectedItem).hasToString("00")
        }
    }

    @Test
    fun launchActivityWithEmptyValuesCelsiusAndTeaSpoonAndExpectFilledActivity() {
        mockDB()
        mockTea(null, -500, AmountKind.TEA_SPOON.text, 0)
        mockInfusions(listOf<String?>(null), listOf<String?>(null), listOf(-500), listOf(-500))
        setSharedSettings(TemperatureUnit.CELSIUS, false, false)

        val intent = Intent(RuntimeEnvironment.getApplication(), ShowTea::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val showTeaActivityScenario = ActivityScenario.launch<ShowTea>(intent)
        showTeaActivityScenario.onActivity { showTea: ShowTea ->
            val textViewVariety = showTea.findViewById<TextView>(R.id.text_view_show_tea_variety)
            val textViewTemperature = showTea.findViewById<TextView>(R.id.text_view_show_tea_temperature)
            val textViewAmount = showTea.findViewById<TextView>(R.id.text_view_show_tea_amount)
            val spinnerMinutes = showTea.findViewById<Spinner>(R.id.spinner_show_tea_minutes)
            val spinnerSeconds = showTea.findViewById<Spinner>(R.id.spinner_show_tea_seconds)

            assertThat(textViewVariety.text).isEqualTo("-")
            assertThat(textViewTemperature.text).isEqualTo("- °C")
            assertThat(textViewAmount.text).contains("- ts/l")
            assertThat(spinnerMinutes.selectedItem).hasToString("00")
            assertThat(spinnerSeconds.selectedItem).hasToString("00")
        }
    }

    @Test
    fun switchBetweenTimerAndCoolDownTimer() {
        mockDB()
        mockTea(VARIETY, 1, AmountKind.TEA_SPOON.text, 0)
        mockInfusions(listOf("1:00"), listOf("4:00"), listOf(100), listOf(212))
        setSharedSettings(TemperatureUnit.CELSIUS, false, false)

        val intent = Intent(RuntimeEnvironment.getApplication(), ShowTea::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val showTeaActivityScenario = ActivityScenario.launch<ShowTea>(intent)
        showTeaActivityScenario.onActivity { showTea: ShowTea ->
            val buttonTemperature = showTea.findViewById<ImageButton>(R.id.button_show_tea_temperature)
            val buttonInfo = showTea.findViewById<ImageButton>(R.id.button_show_tea_info)
            val spinnerMinutes = showTea.findViewById<Spinner>(R.id.spinner_show_tea_minutes)
            val spinnerSeconds = showTea.findViewById<Spinner>(R.id.spinner_show_tea_seconds)

            assertThat(buttonTemperature.isEnabled).isTrue
            assertThat(spinnerMinutes.selectedItem).hasToString("01")
            assertThat(spinnerSeconds.selectedItem).hasToString("00")

            buttonTemperature.performClick()
            assertThat(buttonInfo.visibility).isEqualTo(View.VISIBLE)
            assertThat(spinnerMinutes.selectedItem).hasToString("04")
            assertThat(spinnerSeconds.selectedItem).hasToString("00")

            buttonInfo.performClick()
            val dialogInfo = ShadowAlertDialog.getLatestAlertDialog()
            checkTitleAndMessageOfLatestDialog(showTea, dialogInfo, R.string.show_tea_cool_down_time_header, R.string.show_tea_cool_down_time_description)
            dialogInfo.getButton(DialogInterface.BUTTON_POSITIVE).performClick()

            buttonTemperature.performClick()
            assertThat(buttonInfo.visibility).isEqualTo(View.INVISIBLE)
            assertThat(spinnerMinutes.selectedItem).hasToString("01")
            assertThat(spinnerSeconds.selectedItem).hasToString("00")
        }
    }

    @Test
    fun showDialogAmountAndCalculateAmountTeaSpoon() {
        mockDB()
        mockTea(VARIETY, 4, AmountKind.TEA_SPOON.text, 0)
        mockInfusions(listOf("1:00"), listOf("4:00"), listOf(100), listOf(212))
        setSharedSettings(TemperatureUnit.CELSIUS, false, false)

        val intent = Intent(RuntimeEnvironment.getApplication(), ShowTea::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val showTeaActivityScenario = ActivityScenario.launch<ShowTea>(intent)
        showTeaActivityScenario.onActivity { showTea: ShowTea ->
            val buttonCalculateAmount = showTea.findViewById<ImageButton>(R.id.button_show_tea_calculate_amount)

            buttonCalculateAmount.performClick()
            val dialogCalculateAmount = ShadowAlertDialog.getLatestAlertDialog()
            checkTitleAndMessageOfLatestDialog(showTea, dialogCalculateAmount, R.string.show_tea_dialog_amount)

            val seekBarAmountPerAmount = dialogCalculateAmount.findViewById<SeekBar>(R.id.seek_bar_show_tea_amount_per_amount)
            val textViewAmountPerAmount = dialogCalculateAmount.findViewById<TextView>(R.id.text_view_show_tea_show_amount_per_amount)

            assertThat(textViewAmountPerAmount.text).hasToString("4.0 ts / 1.0 l")

            seekBarAmountPerAmount.progress = 5

            assertThat(textViewAmountPerAmount.text).hasToString("2.0 ts / 0.5 l")
        }
    }

    @Test
    fun navigationToDetailedInformationView() {
        mockDB()
        mockTea(VARIETY, 1, AmountKind.TEA_SPOON.text, 0)
        mockInfusions(listOf("1:00"), listOf<String?>(null), listOf(100), listOf(212))
        setSharedSettings(TemperatureUnit.CELSIUS, false, false)

        val intent = Intent(RuntimeEnvironment.getApplication(), ShowTea::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val showTeaActivityScenario = ActivityScenario.launch<ShowTea>(intent)
        showTeaActivityScenario.onActivity { showTea: ShowTea ->
            val toolbarTitle = showTea.findViewById<TextView>(R.id.tool_bar_title)
            toolbarTitle.performClick()

            val expected = Intent(showTea, Information::class.java)
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.component).isEqualTo(expected.component)
        }
    }

    @Test
    fun navigationToDetailedInformationViewByMenu() {
        mockDB()
        mockTea(VARIETY, 1, AmountKind.TEA_SPOON.text, 0)
        mockInfusions(listOf("1:00"), listOf<String?>(null), listOf(100), listOf(212))
        setSharedSettings(TemperatureUnit.CELSIUS, false, false)

        val intent = Intent(RuntimeEnvironment.getApplication(), ShowTea::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val showTeaActivityScenario = ActivityScenario.launch<ShowTea>(intent)
        showTeaActivityScenario.onActivity { showTea: ShowTea ->
            showTea.onOptionsItemSelected(RoboMenuItem(R.id.action_show_tea_information))

            val expected = Intent(showTea, Information::class.java)
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.component).isEqualTo(expected.component)
        }
    }

    @Test
    fun editTea() {
        mockDB()
        mockTea(VARIETY, 1, AmountKind.TEA_SPOON.text, 0)
        mockInfusions(listOf("1:00"), listOf<String?>(null), listOf(100), listOf(212))
        setSharedSettings(TemperatureUnit.CELSIUS, false, false)

        val intent = Intent(RuntimeEnvironment.getApplication(), ShowTea::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val showTeaActivityScenario = ActivityScenario.launch<ShowTea>(intent)
        showTeaActivityScenario.onActivity { showTea: ShowTea ->
            showTea.onOptionsItemSelected(RoboMenuItem(R.id.action_show_tea_edit))

            val expected = Intent(showTea, NewTea::class.java)
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.component).isEqualTo(expected.component)
        }
    }

    @Test
    fun switchBetweenInfusionsCelsius() {
        mockDB()
        mockTea(VARIETY, 1, AmountKind.TEA_SPOON.text, 0)
        mockInfusions(listOf("1:00", "2:00", "3:00"), listOf(null, "5:00", null), listOf(100, -500, 100), listOf(212, -500, 212))
        setSharedSettings(TemperatureUnit.CELSIUS, false, false)

        val intent = Intent(RuntimeEnvironment.getApplication(), ShowTea::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val showTeaActivityScenario = ActivityScenario.launch<ShowTea>(intent)
        showTeaActivityScenario.onActivity { showTea: ShowTea ->
            val buttonInfusionIndex = showTea.findViewById<ImageButton>(R.id.show_tea_tool_bar_infusion_index)
            val textViewInfusionIndex = showTea.findViewById<TextView>(R.id.show_tea_tool_bar_text_infusion_index)
            val buttonNextInfusion = showTea.findViewById<ImageButton>(R.id.show_tea_tool_bar_next_infusion)
            val spinnerMinutes = showTea.findViewById<Spinner>(R.id.spinner_show_tea_minutes)
            val spinnerSeconds = showTea.findViewById<Spinner>(R.id.spinner_show_tea_seconds)
            val textViewTemperature = showTea.findViewById<TextView>(R.id.text_view_show_tea_temperature)

            assertThat(buttonInfusionIndex.visibility).isEqualTo(View.VISIBLE)
            assertThat(textViewInfusionIndex.visibility).isEqualTo(View.VISIBLE)
            assertThat(buttonNextInfusion.visibility).isEqualTo(View.VISIBLE)
            assertThat(textViewTemperature.text).hasToString("100 °C")
            assertThat(spinnerMinutes.selectedItem).hasToString("01")
            assertThat(spinnerSeconds.selectedItem).hasToString("00")

            buttonNextInfusion.performClick()
            assertThat(textViewTemperature.text).hasToString("- °C")
            assertThat(spinnerMinutes.selectedItem).hasToString("02")
            assertThat(spinnerSeconds.selectedItem).hasToString("00")

            buttonInfusionIndex.performClick()
            val dialogInfusionIndex = ShadowAlertDialog.getLatestAlertDialog()
            checkTitleAndMessageOfLatestDialog(showTea, dialogInfusionIndex, R.string.show_tea_dialog_infusion_count_title)

            val shadowDialog = Shadows.shadowOf(dialogInfusionIndex)
            shadowDialog.clickOnItem(2)
            assertThat(textViewTemperature.text).hasToString("100 °C")
            assertThat(spinnerMinutes.selectedItem).hasToString("03")
            assertThat(spinnerSeconds.selectedItem).hasToString("00")
        }
    }

    @Test
    fun startTimer() {
        mockDB()
        mockTea(VARIETY, 1, AmountKind.TEA_SPOON.text, 0)
        mockInfusions(listOf("1:00", "2:00"), listOf("1:00", "1:00"), listOf(95, 95), listOf(203, 203))
        mockCounter()
        setSharedSettings(TemperatureUnit.CELSIUS, false, true)

        val intent = Intent(RuntimeEnvironment.getApplication(), ShowTea::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val showTeaActivityScenario = ActivityScenario.launch<ShowTea>(intent)
        showTeaActivityScenario.onActivity { showTea: ShowTea -> checkStartOrReset(showTea, true) }
    }

    @Test
    fun timerUpdate() {
        mockDB()
        mockTea(VARIETY, 1, AmountKind.TEA_SPOON.text, 0)
        mockInfusions(listOf("1:00"), listOf<String?>(null), listOf(100), listOf(212))
        mockCounter()
        setSharedSettings(TemperatureUnit.CELSIUS, false, true)

        val intent = Intent(RuntimeEnvironment.getApplication(), ShowTea::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val showTeaActivityScenario = ActivityScenario.launch<ShowTea>(intent)
        showTeaActivityScenario.onActivity { showTea: ShowTea ->
            val startButton = showTea.findViewById<Button>(R.id.button_show_tea_start_timer)
            val textViewTimer = showTea.findViewById<TextView>(R.id.text_view_show_tea_timer)
            val imageViewFill = showTea.findViewById<ImageView>(R.id.image_view_show_tea_fill)

            startButton.performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val broadcastIntent = Intent(TimerController.COUNTDOWN_BR)
            broadcastIntent.putExtra(BROADCAST_EXTRA_COUNTDOWN, 30000L)
            broadcastIntent.putExtra(BROADCAST_EXTRA_READY, false)
            showTea.sendBroadcast(broadcastIntent)
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            assertThat(textViewTimer.text).hasToString("00 : 30")
            val imageId = showTea.resources.getIdentifier("cup_fill50pr", "drawable", showTea.packageName)
            assertThat(imageViewFill.tag).isEqualTo(imageId)
        }
    }

    @Test
    fun timerFinish() {
        mockDB()
        mockTea(VARIETY, 1, AmountKind.TEA_SPOON.text, 0)
        mockInfusions(listOf("1:00"), listOf<String?>(null), listOf(100), listOf(212))
        mockCounter()
        setSharedSettings(TemperatureUnit.CELSIUS, false, true)

        val intent = Intent(RuntimeEnvironment.getApplication(), ShowTea::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val showTeaActivityScenario = ActivityScenario.launch<ShowTea>(intent)
        showTeaActivityScenario.onActivity { showTea: ShowTea ->
            val startButton = showTea.findViewById<Button>(R.id.button_show_tea_start_timer)
            val textViewTimer = showTea.findViewById<TextView>(R.id.text_view_show_tea_timer)
            val imageViewSteam = showTea.findViewById<ImageView>(R.id.image_view_show_tea_steam)

            startButton.performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val broadcastIntent = Intent(TimerController.COUNTDOWN_BR)
            broadcastIntent.putExtra(BROADCAST_EXTRA_READY, true)
            showTea.sendBroadcast(broadcastIntent)
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            assertThat(textViewTimer.text).hasToString(showTea.getString(R.string.show_tea_tea_ready))
            assertThat(imageViewSteam.visibility).isEqualTo(View.VISIBLE)
        }
    }

    @Test
    fun startCoolDownTimerAndExpectNoAnimation() {
        mockDB()
        mockTea(VARIETY, 1, AmountKind.TEA_SPOON.text, 0)
        mockInfusions(listOf("1:00"), listOf("1:00"), listOf(95), listOf(203))
        setSharedSettings(TemperatureUnit.CELSIUS, false, true)

        val intent = Intent(RuntimeEnvironment.getApplication(), ShowTea::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val showTeaActivityScenario = ActivityScenario.launch<ShowTea>(intent)
        showTeaActivityScenario.onActivity { showTea: ShowTea ->
            val buttonTemperature = showTea.findViewById<ImageButton>(R.id.button_show_tea_temperature)
            val startButton = showTea.findViewById<Button>(R.id.button_show_tea_start_timer)
            val imageViewCup = showTea.findViewById<ImageView>(R.id.image_view_show_tea_cup)
            val imageViewFill = showTea.findViewById<ImageView>(R.id.image_view_show_tea_fill)
            val imageViewSteam = showTea.findViewById<ImageView>(R.id.image_view_show_tea_steam)

            buttonTemperature.performClick()
            startButton.performClick()

            val broadcastUpdate = Intent(TimerController.COUNTDOWN_BR)
            broadcastUpdate.putExtra(BROADCAST_EXTRA_COUNTDOWN, 30000L)
            broadcastUpdate.putExtra(BROADCAST_EXTRA_READY, false)
            showTea.sendBroadcast(broadcastUpdate)

            val broadcastFinish = Intent(TimerController.COUNTDOWN_BR)
            broadcastFinish.putExtra(BROADCAST_EXTRA_READY, true)
            showTea.sendBroadcast(broadcastFinish)

            verify (exactly = 0) { counterDao.update(any()) }
            assertThat(imageViewCup.visibility).isEqualTo(View.INVISIBLE)
            assertThat(imageViewFill.visibility).isEqualTo(View.INVISIBLE)
            assertThat(imageViewSteam.visibility).isEqualTo(View.INVISIBLE)
        }
    }

    @Test
    fun startTimerWithSettingAnimationFalseAndExpectNoAnimation() {
        mockDB()
        mockTea(VARIETY, 1, AmountKind.TEA_SPOON.text, 0)
        mockInfusions(listOf("1:00"), listOf<String?>(null), listOf(100), listOf(212))
        mockCounter()
        setSharedSettings(TemperatureUnit.CELSIUS, false, false)

        val intent = Intent(RuntimeEnvironment.getApplication(), ShowTea::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val showTeaActivityScenario = ActivityScenario.launch<ShowTea>(intent)
        showTeaActivityScenario.onActivity { showTea: ShowTea ->
            val startButton = showTea.findViewById<Button>(R.id.button_show_tea_start_timer)
            val imageViewCup = showTea.findViewById<ImageView>(R.id.image_view_show_tea_cup)
            val imageViewFill = showTea.findViewById<ImageView>(R.id.image_view_show_tea_fill)
            val imageViewSteam = showTea.findViewById<ImageView>(R.id.image_view_show_tea_steam)

            startButton.performClick()

            val broadcastUpdate = Intent(TimerController.COUNTDOWN_BR)
            broadcastUpdate.putExtra(BROADCAST_EXTRA_COUNTDOWN, 30000L)
            broadcastUpdate.putExtra(BROADCAST_EXTRA_READY, false)
            showTea.sendBroadcast(broadcastUpdate)

            val broadcastFinish = Intent(TimerController.COUNTDOWN_BR)
            broadcastFinish.putExtra(BROADCAST_EXTRA_READY, true)
            showTea.sendBroadcast(broadcastFinish)

            assertThat(imageViewCup.visibility).isEqualTo(View.INVISIBLE)
            assertThat(imageViewFill.visibility).isEqualTo(View.INVISIBLE)
            assertThat(imageViewSteam.visibility).isEqualTo(View.INVISIBLE)
        }
    }

    @Test
    fun resetTimer() {
        mockDB()
        mockTea(VARIETY, 1, AmountKind.TEA_SPOON.text, 0)
        mockInfusions(listOf("1:00", "2:00"), listOf("1:00", "1:00"), listOf(95, 95), listOf(203, 203))
        mockCounter()
        setSharedSettings(TemperatureUnit.CELSIUS, false, true)

        val intent = Intent(RuntimeEnvironment.getApplication(), ShowTea::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val showTeaActivityScenario = ActivityScenario.launch<ShowTea>(intent)
        showTeaActivityScenario.onActivity { showTea: ShowTea -> checkStartOrReset(showTea, false) }
    }

    private fun checkStartOrReset(showTea: ShowTea, start: Boolean) {
        val startButton = showTea.findViewById<Button>(R.id.button_show_tea_start_timer)
        val buttonTemperature = showTea.findViewById<ImageButton>(R.id.button_show_tea_temperature)
        val buttonInfusionIndex = showTea.findViewById<ImageButton>(R.id.show_tea_tool_bar_infusion_index)
        val buttonNextInfusion = showTea.findViewById<ImageButton>(R.id.show_tea_tool_bar_next_infusion)
        val spinnerMinutes = showTea.findViewById<Spinner>(R.id.spinner_show_tea_minutes)
        val spinnerSeconds = showTea.findViewById<Spinner>(R.id.spinner_show_tea_seconds)
        val textViewMinutes = showTea.findViewById<TextView>(R.id.text_view_show_tea_minutes)
        val textViewSeconds = showTea.findViewById<TextView>(R.id.text_view_show_tea_seconds)
        val textViewDoublePoint = showTea.findViewById<TextView>(R.id.text_view_show_tea_double_point)
        val textViewTimer = showTea.findViewById<TextView>(R.id.text_view_show_tea_timer)
        val imageViewCup = showTea.findViewById<ImageView>(R.id.image_view_show_tea_cup)
        val imageViewFill = showTea.findViewById<ImageView>(R.id.image_view_show_tea_fill)

        if (!start) {
            // start before reset
            startButton.performClick()
        }
        startButton.performClick()

        assertThat(startButton.text)
            .hasToString(showTea.getString(if (start) R.string.show_tea_timer_reset else R.string.show_tea_timer_start))
        // disableInfusionBarAndCooldownSwitch
        assertThat(buttonTemperature.isEnabled).isEqualTo(!start)
        assertThat(buttonInfusionIndex.isEnabled).isEqualTo(!start)
        assertThat(buttonNextInfusion.isEnabled).isEqualTo(!start)
        // hideTimeInputAndVisualizeTimerDisplay
        assertThat(spinnerMinutes.visibility).isEqualTo(if (start) View.INVISIBLE else View.VISIBLE)
        assertThat(spinnerSeconds.visibility).isEqualTo(if (start) View.INVISIBLE else View.VISIBLE)
        assertThat(textViewMinutes.visibility).isEqualTo(if (start) View.INVISIBLE else View.VISIBLE)
        assertThat(textViewSeconds.visibility).isEqualTo(if (start) View.INVISIBLE else View.VISIBLE)
        assertThat(textViewDoublePoint.visibility).isEqualTo(if (start) View.INVISIBLE else View.VISIBLE)
        assertThat(textViewTimer.visibility).isEqualTo(if (start) View.VISIBLE else View.INVISIBLE)
        // visualizeTeaCup
        assertThat(imageViewCup.visibility).isEqualTo(if (start) View.VISIBLE else View.INVISIBLE)
        assertThat(imageViewFill.visibility).isEqualTo(if (start) View.VISIBLE else View.INVISIBLE)
    }

    private fun mockDB() {
        setMockedDatabase(teaMemoryDatabase)
        every { teaMemoryDatabase.teaDao } returns teaDao
        every { teaMemoryDatabase.infusionDao } returns infusionDao
        every { teaMemoryDatabase.counterDao } returns counterDao
    }

    private fun mockTea(variety: String?, amount: Int, amountKind: String, nextInfusion: Int) {
        tea = Tea("name", variety, amount.toDouble(), amountKind, 1, nextInfusion, getDate())
        tea!!.id = TEA_ID
        every { teaDao.getTeaById(TEA_ID) } returns tea
    }

    private fun mockInfusions(time: List<String?>, coolDownTime: List<String?>, temperatureCelsius: List<Int>, temperatureFahrenheit: List<Int>) {
        for (i in time.indices) {
            val infusion = Infusion(TEA_ID, i, time[i], coolDownTime[i], temperatureCelsius[i], temperatureFahrenheit[i])
            infusion.id = (i + 1).toLong()
            infusions.add(infusion)
        }
        every { infusionDao.getInfusionsByTeaId(TEA_ID) } returns infusions
    }

    private fun mockCounter() {
        counter = Counter(TEA_ID, 1, 2, 3, 4, getDate(), getDate(), getDate())
        counter!!.id = 1L
        every { counterDao.getCounterByTeaId(TEA_ID) } returns counter
    }

    private fun setSharedSettings(temperatureUnit: TemperatureUnit, dialog: Boolean, animation: Boolean) {
        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())
        sharedSettings.temperatureUnit = temperatureUnit
        sharedSettings.isShowTeaAlert = dialog
        sharedSettings.isAnimation = animation
    }

    private fun checkTitleAndMessageOfLatestDialog(showTea: ShowTea, dialog: AlertDialog, title: Int, message: Int) {
        checkTitleAndMessageOfLatestDialog(showTea, dialog, title, showTea.getString(message))
    }

    private fun checkTitleAndMessageOfLatestDialog(showTea: ShowTea, dialog: AlertDialog, title: Int, message: String? = null) {
        val shadowDialog = Shadows.shadowOf(dialog)
        assertThat(shadowDialog).isNotNull
        assertThat(shadowDialog.title).isEqualTo(showTea.getString(title))
        if (message != null) {
            assertThat(shadowDialog.message).isEqualTo(message)
        }
    }

    companion object {
        private const val TEA_ID_EXTRA = "teaId"
        private const val TEA_ID = 1L
        private const val VARIETY = "variety"
        private const val BROADCAST_EXTRA_READY = "ready"
        private const val BROADCAST_EXTRA_COUNTDOWN = "countdown"
    }
}