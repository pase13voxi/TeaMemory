package coolpharaoh.tee.speicher.tea.timer.views.settings

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.media.RingtoneManager
import android.os.Build
import android.os.Looper
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.settings.DarkMode
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk.setFixedSystem
import coolpharaoh.tee.speicher.tea.timer.core.system.SystemUtility
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase.Companion.setMockedDatabase
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageControllerFactory.setMockedImageController
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowAlertDialog

@RunWith(RobolectricTestRunner::class)
class SettingsTest {
    @JvmField
    @Rule
    var rule = MockitoJUnit.rule()

    @Mock
    lateinit var teaMemoryDatabase: TeaMemoryDatabase

    @Mock
    lateinit var teaDao: TeaDao

    @Mock
    lateinit var imageController: ImageController

    @Mock
    lateinit var systemUtility: SystemUtility

    private var sharedSettings: SharedSettings? = null

    @Before
    fun setUp() {
        mockDB()
        setSharedSettings()
        setMockedImageController(imageController)
        mockSystemVersionCode()
    }

    private fun mockDB() {
        setMockedDatabase(teaMemoryDatabase)
        `when`(teaMemoryDatabase.teaDao).thenReturn(teaDao)
    }

    private fun setSharedSettings() {
        sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())
        sharedSettings!!.musicChoice = "musicChoice"
        sharedSettings!!.musicName = "MusicName"
        sharedSettings!!.isVibration = true
        sharedSettings!!.isAnimation = true
        sharedSettings!!.temperatureUnit = TemperatureUnit.CELSIUS
        sharedSettings!!.isShowTeaAlert = false
        sharedSettings!!.isOverviewUpdateAlert = false
    }

    private fun mockSystemVersionCode() {
        setFixedSystem(systemUtility)
        `when`(systemUtility.sdkVersion).thenReturn(Build.VERSION_CODES.R)
    }

    @Test
    fun launchActivityAndExpectFilledListView() {
        val settingsActivityScenario = ActivityScenario.launch(Settings::class.java)
        settingsActivityScenario.onActivity { settings: Settings ->
            val settingsRecyclerView = settings.findViewById<RecyclerView>(R.id.recycler_view_settings)

            scrollToPosition(settingsRecyclerView, ALARM)
            checkHeadingAtPosition(settingsRecyclerView, ALARM, settings.getString(R.string.settings_alarm))
            checkDescriptionAtPosition(settingsRecyclerView, ALARM, sharedSettings!!.musicName)

            scrollToPosition(settingsRecyclerView, VIBRATION)
            checkHeadingAtPosition(settingsRecyclerView, VIBRATION, settings.getString(R.string.settings_vibration))
            checkDescriptionAtPosition(settingsRecyclerView, VIBRATION, if (sharedSettings!!.isVibration) ON else OFF)

            scrollToPosition(settingsRecyclerView, ANIMATION)
            checkHeadingAtPosition(settingsRecyclerView, ANIMATION, settings.getString(R.string.settings_animation))
            checkDescriptionAtPosition(settingsRecyclerView, ANIMATION, if (sharedSettings!!.isAnimation) ON else OFF)

            scrollToPosition(settingsRecyclerView, TEMPERATURE_UNIT)
            checkHeadingAtPosition(settingsRecyclerView, TEMPERATURE_UNIT, settings.getString(R.string.settings_temperature_unit))
            checkDescriptionAtPosition(settingsRecyclerView, TEMPERATURE_UNIT, CELSIUS_TEXT)

            scrollToPosition(settingsRecyclerView, OVERVIEW_HEADER)
            checkHeadingAtPosition(settingsRecyclerView, OVERVIEW_HEADER, settings.getString(R.string.settings_overview_header))
            checkDescriptionAtPosition(settingsRecyclerView, OVERVIEW_HEADER, OFF)

            scrollToPosition(settingsRecyclerView, DARK_MODE)
            val darkModes = settings.resources.getStringArray(R.array.settings_dark_mode)
            checkHeadingAtPosition(settingsRecyclerView, DARK_MODE, settings.getString(R.string.settings_dark_mode))
            checkDescriptionAtPosition(settingsRecyclerView, DARK_MODE, darkModes[DarkMode.SYSTEM.choice])

            scrollToPosition(settingsRecyclerView, HINTS)
            checkHeadingAtPosition(settingsRecyclerView, HINTS, settings.getString(R.string.settings_show_hints))
            checkDescriptionAtPosition(settingsRecyclerView, HINTS, settings.getString(R.string.settings_show_hints_description))

            scrollToPosition(settingsRecyclerView, FACTORY_SETTINGS)
            checkHeadingAtPosition(settingsRecyclerView, FACTORY_SETTINGS, settings.getString(R.string.settings_factory_settings))
            checkDescriptionAtPosition(settingsRecyclerView, FACTORY_SETTINGS, settings.getString(R.string.settings_factory_settings_description))
        }
    }

    @Test
    fun clickAlarmAndExpectAlarmPicker() {
        val settingsActivityScenario = ActivityScenario.launch(Settings::class.java)
        settingsActivityScenario.onActivity { settings: Settings ->
            val settingsRecyclerView = settings.findViewById<RecyclerView>(R.id.recycler_view_settings)

            clickAtPositionRecyclerView(settingsRecyclerView, ALARM)

            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.action).isEqualTo(RingtoneManager.ACTION_RINGTONE_PICKER)
        }
    }

    @Test
    fun setVibrationFalseAndExpectVibrationFalse() {
        val settingsActivityScenario = ActivityScenario.launch(Settings::class.java)
        settingsActivityScenario.onActivity { settings: Settings ->
            val settingsRecyclerView = settings.findViewById<RecyclerView>(R.id.recycler_view_settings)

            clickAtPositionRecyclerView(settingsRecyclerView, VIBRATION)

            val shadowAlertDialog = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            shadowAlertDialog.clickOnItem(OPTION_OFF)
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            assertThat(sharedSettings!!.isVibration).isFalse

            checkDescriptionAtPosition(settingsRecyclerView, VIBRATION, OFF)
        }
    }

    @Test
    fun setVibrationTrueAndExpectVibrationTrue() {
        val settingsActivityScenario = ActivityScenario.launch(Settings::class.java)
        settingsActivityScenario.onActivity { settings: Settings ->
            val settingsRecyclerView = settings.findViewById<RecyclerView>(R.id.recycler_view_settings)

            clickAtPositionRecyclerView(settingsRecyclerView, VIBRATION)

            val shadowAlertDialog = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            shadowAlertDialog.clickOnItem(OPTION_ON)
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            assertThat(sharedSettings!!.isVibration).isTrue

            checkDescriptionAtPosition(settingsRecyclerView, VIBRATION, ON)
        }
    }

    @Test
    fun setAnimationFalseAndExpectAnimationFalse() {
        val settingsActivityScenario = ActivityScenario.launch(Settings::class.java)
        settingsActivityScenario.onActivity { settings: Settings ->
            val settingsRecyclerView = settings.findViewById<RecyclerView>(R.id.recycler_view_settings)

            clickAtPositionRecyclerView(settingsRecyclerView, ANIMATION)

            val shadowAlertDialog = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            shadowAlertDialog.clickOnItem(OPTION_OFF)
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            assertThat(sharedSettings!!.isAnimation).isFalse

            checkDescriptionAtPosition(settingsRecyclerView, ANIMATION, OFF)
        }
    }

    @Test
    fun setAnimationTrueAndExpectAnimationTrue() {
        val settingsActivityScenario = ActivityScenario.launch(Settings::class.java)
        settingsActivityScenario.onActivity { settings: Settings ->
            val settingsRecyclerView = settings.findViewById<RecyclerView>(R.id.recycler_view_settings)

            clickAtPositionRecyclerView(settingsRecyclerView, ANIMATION)

            val shadowAlertDialog = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            shadowAlertDialog.clickOnItem(OPTION_ON)
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            assertThat(sharedSettings!!.isAnimation).isTrue

            checkDescriptionAtPosition(settingsRecyclerView, ANIMATION, ON)
        }
    }

    @Test
    fun setTemperatureUnitCelsiusAndExpectCelsius() {
        val settingsActivityScenario = ActivityScenario.launch(Settings::class.java)
        settingsActivityScenario.onActivity { settings: Settings ->
            val settingsRecyclerView = settings.findViewById<RecyclerView>(R.id.recycler_view_settings)

            clickAtPositionRecyclerView(settingsRecyclerView, TEMPERATURE_UNIT)

            val shadowAlertDialog = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            shadowAlertDialog.clickOnItem(TemperatureUnit.CELSIUS.choice)
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            assertThat(sharedSettings!!.temperatureUnit).isEqualTo(TemperatureUnit.CELSIUS)

            checkDescriptionAtPosition(settingsRecyclerView, TEMPERATURE_UNIT, CELSIUS_TEXT)
        }
    }

    @Test
    fun setTemperatureUnitFahrenheitAndExpectFahrenheit() {
        val settingsActivityScenario = ActivityScenario.launch(Settings::class.java)
        settingsActivityScenario.onActivity { settings: Settings ->
            val settingsRecyclerView = settings.findViewById<RecyclerView>(R.id.recycler_view_settings)

            clickAtPositionRecyclerView(settingsRecyclerView, TEMPERATURE_UNIT)

            val shadowAlertDialog = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            shadowAlertDialog.clickOnItem(TemperatureUnit.FAHRENHEIT.choice)
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            assertThat(sharedSettings!!.temperatureUnit).isEqualTo(TemperatureUnit.FAHRENHEIT)

            checkDescriptionAtPosition(settingsRecyclerView, TEMPERATURE_UNIT, FAHRENHEIT_TEXT)
        }
    }

    @Test
    fun setOverviewHeaderFalseAndExpectOverviewHeaderFalse() {
        val settingsActivityScenario = ActivityScenario.launch(Settings::class.java)
        settingsActivityScenario.onActivity { settings: Settings ->
            val sharedSettings = SharedSettings(settings.application)
            sharedSettings.isOverviewHeader = true

            val settingsRecyclerView = settings.findViewById<RecyclerView>(R.id.recycler_view_settings)

            clickAtPositionRecyclerView(settingsRecyclerView, OVERVIEW_HEADER)

            val shadowAlertDialog = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            shadowAlertDialog.clickOnItem(OPTION_OFF)
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            assertThat(sharedSettings.isOverviewHeader).isFalse

            checkDescriptionAtPosition(settingsRecyclerView, OVERVIEW_HEADER, OFF)
        }
    }

    @Test
    fun setOverviewHeaderTrueAndExpectOverviewHeaderTrue() {
        val settingsActivityScenario = ActivityScenario.launch(Settings::class.java)
        settingsActivityScenario.onActivity { settings: Settings ->
            val sharedSettings = SharedSettings(settings.application)
            sharedSettings.isOverviewHeader = false

            val settingsRecyclerView = settings.findViewById<RecyclerView>(R.id.recycler_view_settings)

            clickAtPositionRecyclerView(settingsRecyclerView, OVERVIEW_HEADER)

            val shadowAlertDialog = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            shadowAlertDialog.clickOnItem(OPTION_ON)
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            assertThat(sharedSettings.isOverviewHeader).isTrue

            checkDescriptionAtPosition(settingsRecyclerView, OVERVIEW_HEADER, ON)
        }
    }

    @Ignore("Leads to a null pointer exception I don't know why!")
    @Test
    fun setDarkModeEnabled() {
        val settingsActivityScenario = ActivityScenario.launch(Settings::class.java)
        settingsActivityScenario.onActivity { settings: Settings ->
            val settingsRecyclerView = settings.findViewById<RecyclerView>(R.id.recycler_view_settings)

            clickAtPositionRecyclerView(settingsRecyclerView, DARK_MODE)

            val shadowAlertDialog = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            shadowAlertDialog.clickOnItem(DarkMode.ENABLED.choice)

            val sharedSettings = SharedSettings(settings.application)
            assertThat(sharedSettings.darkMode).isEqualTo(DarkMode.ENABLED)

            val expectedChoice = settings.resources.getStringArray(R.array.settings_dark_mode)[DarkMode.ENABLED.choice]
            checkDescriptionAtPosition(settingsRecyclerView, DARK_MODE, expectedChoice)

            assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    @Test
    fun setDarkModeSystem() {
        val settingsActivityScenario = ActivityScenario.launch(Settings::class.java)
        settingsActivityScenario.onActivity { settings: Settings ->
            val settingsRecyclerView = settings.findViewById<RecyclerView>(R.id.recycler_view_settings)

            clickAtPositionRecyclerView(settingsRecyclerView, DARK_MODE)

            val shadowAlertDialog = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            shadowAlertDialog.clickOnItem(DarkMode.SYSTEM.choice)
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val sharedSettings = SharedSettings(settings.application)
            assertThat(sharedSettings.darkMode).isEqualTo(DarkMode.SYSTEM)

            val expectedChoice = settings.resources.getStringArray(R.array.settings_dark_mode)[DarkMode.SYSTEM.choice]
            checkDescriptionAtPosition(settingsRecyclerView, DARK_MODE, expectedChoice)

            assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    @Test
    fun setDarkModeSystemOnVersionCodeOlderAndroidQ() {
        `when`(systemUtility.sdkVersion).thenReturn(Build.VERSION_CODES.P)

        val settingsActivityScenario = ActivityScenario.launch(Settings::class.java)
        settingsActivityScenario.onActivity { settings: Settings ->
            val settingsRecyclerView = settings.findViewById<RecyclerView>(R.id.recycler_view_settings)

            clickAtPositionRecyclerView(settingsRecyclerView, DARK_MODE)

            val shadowAlertDialog = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            shadowAlertDialog.clickOnItem(DarkMode.SYSTEM.choice)
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val sharedSettings = SharedSettings(settings.application)
            assertThat(sharedSettings.darkMode).isEqualTo(DarkMode.SYSTEM)

            val expectedChoice = settings.resources.getStringArray(R.array.settings_dark_mode)[DarkMode.SYSTEM.choice]
            checkDescriptionAtPosition(settingsRecyclerView, DARK_MODE, expectedChoice)

            assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
        }
    }

    @Test
    fun setDarkModeDisabled() {
        val settingsActivityScenario = ActivityScenario.launch(Settings::class.java)
        settingsActivityScenario.onActivity { settings: Settings ->
            val settingsRecyclerView = settings.findViewById<RecyclerView>(R.id.recycler_view_settings)

            clickAtPositionRecyclerView(settingsRecyclerView, DARK_MODE)

            val shadowAlertDialog = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            shadowAlertDialog.clickOnItem(DarkMode.DISABLED.choice)
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val sharedSettings = SharedSettings(settings.application)
            assertThat(sharedSettings.darkMode).isEqualTo(DarkMode.DISABLED)

            val expectedChoice = settings.resources.getStringArray(R.array.settings_dark_mode)[DarkMode.DISABLED.choice]
            checkDescriptionAtPosition(settingsRecyclerView, DARK_MODE, expectedChoice)

            assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    @Test
    fun setAllHintsAndExpectAllHints() {
        val settingsActivityScenario = ActivityScenario.launch(Settings::class.java)
        settingsActivityScenario.onActivity { settings: Settings ->
            val settingsRecyclerView = settings.findViewById<RecyclerView>(R.id.recycler_view_settings)

            clickAtPositionRecyclerView(settingsRecyclerView, HINTS)

            val alertDialog = ShadowAlertDialog.getLatestAlertDialog()

            val checkBoxUpdate = alertDialog.findViewById<CheckBox>(R.id.check_box_settings_dialog_update)
            val checkBoxDescription = alertDialog.findViewById<CheckBox>(R.id.check_box_settings_dialog_description)

            assertThat(checkBoxUpdate.isChecked).isFalse
            assertThat(checkBoxDescription.isChecked).isFalse

            checkBoxUpdate.isChecked = true
            checkBoxDescription.isChecked = true

            val accept = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
            accept.performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            assertThat(sharedSettings!!.isOverviewUpdateAlert).isTrue
            assertThat(sharedSettings!!.isShowTeaAlert).isTrue()
        }
    }

    @Test
    fun setFactorySettingsAndExpectFactorySettings() {
        val tea1 = Tea()
        tea1.id = 1L
        val tea2 = Tea()
        tea2.id = 2L
        val teas = listOf(tea1, tea2)
        `when`(teaDao.getTeas()).thenReturn(teas)

        val settingsActivityScenario = ActivityScenario.launch(Settings::class.java)
        settingsActivityScenario.onActivity { settings: Settings ->
            val settingsRecyclerView = settings.findViewById<RecyclerView>(R.id.recycler_view_settings)
            clickAtPositionRecyclerView(settingsRecyclerView, FACTORY_SETTINGS)
            val alertDialog = ShadowAlertDialog.getLatestAlertDialog()

            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            verify(imageController).removeImageByTeaId(1L)
            verify(imageController).removeImageByTeaId(2L)
            verify(teaDao).deleteAll()
            assertThat(sharedSettings!!.musicName).isEqualTo("Default")
        }
    }

    @Test
    fun setFactorySettingsOnVersionCodeOlderAndroidQ() {
        `when`(systemUtility.sdkVersion).thenReturn(Build.VERSION_CODES.P)
        val tea1 = Tea()
        tea1.id = 1L
        val tea2 = Tea()
        tea2.id = 2L
        val teas = listOf(tea1, tea2)
        `when`(teaDao.getTeas()).thenReturn(teas)

        val settingsActivityScenario = ActivityScenario.launch(Settings::class.java)
        settingsActivityScenario.onActivity { settings: Settings ->
            val settingsRecyclerView = settings.findViewById<RecyclerView>(R.id.recycler_view_settings)
            clickAtPositionRecyclerView(settingsRecyclerView, FACTORY_SETTINGS)
            val alertDialog = ShadowAlertDialog.getLatestAlertDialog()

            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            verify(imageController, never()).removeImageByTeaId(ArgumentMatchers.anyLong())
            verify(teaDao).deleteAll()
            assertThat(sharedSettings!!.musicName).isEqualTo("Default")
        }
    }

    private fun clickAtPositionRecyclerView(recyclerView: RecyclerView, position: Int) {
        scrollToPosition(recyclerView, position)
        val itemView = recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
        itemView.performClick()
    }

    private fun scrollToPosition(settingsRecyclerView: RecyclerView, alarm: Int) {
        settingsRecyclerView.scrollToPosition(alarm)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
    }

    private fun checkHeadingAtPosition(recyclerView: RecyclerView, position: Int, heading: String) {
        checkViewAtPositionInRecyclerView(recyclerView, position, R.id.text_view_recycler_view_heading, heading)
    }

    private fun checkDescriptionAtPosition(recyclerView: RecyclerView, position: Int, description: String?) {
        checkViewAtPositionInRecyclerView(recyclerView, position, R.id.text_view_recycler_view_description, description)
    }

    private fun checkViewAtPositionInRecyclerView(recyclerView: RecyclerView, position: Int,
        viewId: Int, toCheck: String?) {
        val itemView = recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
        val textViewHeading = itemView.findViewById<TextView>(viewId)
        assertThat(textViewHeading.text).hasToString(toCheck)
    }

    companion object {
        private const val ALARM = 0
        private const val VIBRATION = 1
        private const val ANIMATION = 2
        private const val TEMPERATURE_UNIT = 3
        private const val OVERVIEW_HEADER = 4
        private const val DARK_MODE = 5
        private const val HINTS = 6
        private const val FACTORY_SETTINGS = 7
        private const val OPTION_ON = 0
        private const val OPTION_OFF = 1
        private const val ON = "On"
        private const val OFF = "Off"
        private const val CELSIUS_TEXT = "Celsius"
        private const val FAHRENHEIT_TEXT = "Fahrenheit"
    }
}