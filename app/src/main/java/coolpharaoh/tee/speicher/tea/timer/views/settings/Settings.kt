package coolpharaoh.tee.speicher.tea.timer.views.settings

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.settings.DarkMode
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk.sdkVersion
import coolpharaoh.tee.speicher.tea.timer.views.utils.ThemeManager.applyTheme
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.RecyclerItem
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.RecyclerViewAdapter
import java.util.Arrays
import java.util.Objects

class Settings : AppCompatActivity(), RecyclerViewAdapter.OnClickListener {

    private enum class ListItems {
        ALARM, VIBRATION, ANIMATION, TEMPERATURE_UNIT, OVERVIEW_HEADER, DARK_MODE, HINTS, FACTORY_SETTINGS
    }

    private var settingsViewModel: SettingsViewModel? = null

    private var settingsList: ArrayList<RecyclerItem>? = null
    private var adapter: RecyclerViewAdapter? = null

    private val alarmRequestActivityResultLauncher = registerForActivityResult(StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                processMusicChoice(result.data)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        defineToolbarAsActionbar()
        enableAndShowBackButton()

        settingsViewModel = SettingsViewModel(application)

        initializeSettingsListView()
    }

    private fun defineToolbarAsActionbar() {
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        val mToolbarCustomTitle = findViewById<TextView>(R.id.tool_bar_title)
        mToolbarCustomTitle.setText(R.string.settings_heading)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)?.title = null
    }

    private fun enableAndShowBackButton() {
        Objects.requireNonNull(supportActionBar)?.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initializeSettingsListView() {
        settingsList = ArrayList()
        fillAndRefreshSettingsList()

        adapter = RecyclerViewAdapter(R.layout.list_single_layout_setting, settingsList!!, this)

        val recyclerViewDetails = findViewById<RecyclerView>(R.id.recycler_view_settings)
        recyclerViewDetails.addItemDecoration(DividerItemDecoration(recyclerViewDetails.context,
            DividerItemDecoration.VERTICAL))
        recyclerViewDetails.layoutManager = LinearLayoutManager(this)
        recyclerViewDetails.adapter = adapter
    }

    private fun fillAndRefreshSettingsList() {
        settingsList!!.clear()

        addMusicChoiceToSettingsList()
        addVibrationChoiceToSettingsList()
        addAnimationChoiceToSettingsList()
        addTemperatureChoiceToSettingsList()
        addOverviewHeaderToSettingsList()
        addDarkModeChoiceToSettingsList()
        addHintsDescriptionToSettingsList()
        addFactorySettingsDescriptionToSettingsList()
    }

    private fun addMusicChoiceToSettingsList() {
        val itemSound = RecyclerItem(getString(R.string.settings_alarm), settingsViewModel!!.musicName!!)
        settingsList!!.add(itemSound)
    }

    private fun addVibrationChoiceToSettingsList() {
        val itemsOnOff = resources.getStringArray(R.array.settings_options)

        val vibrationOption = if (settingsViewModel!!.isVibration) 0 else 1

        settingsList!!.add(RecyclerItem(getString(R.string.settings_vibration), itemsOnOff[vibrationOption]))
    }

    private fun addAnimationChoiceToSettingsList() {
        val itemsOnOff = resources.getStringArray(R.array.settings_options)

        val animationOption = if (settingsViewModel!!.isAnimation) 0 else 1

        settingsList!!.add(RecyclerItem(getString(R.string.settings_animation), itemsOnOff[animationOption]))
    }

    private fun addTemperatureChoiceToSettingsList() {
        val itemTemperature = resources.getStringArray(R.array.settings_temperature_units)

        settingsList!!.add(RecyclerItem(getString(R.string.settings_temperature_unit), itemTemperature[settingsViewModel!!.temperatureUnit!!.choice]))
    }

    private fun addOverviewHeaderToSettingsList() {
        val itemsOnOff = resources.getStringArray(R.array.settings_options)

        val overviewHeaderOption = if (settingsViewModel!!.isOverviewHeader) 0 else 1

        settingsList!!.add(RecyclerItem(getString(R.string.settings_overview_header), itemsOnOff[overviewHeaderOption]))
    }

    private fun addDarkModeChoiceToSettingsList() {
        val darkMode = settingsViewModel!!.darkMode
        val items = resources.getStringArray(R.array.settings_dark_mode)

        settingsList!!.add(RecyclerItem(getString(R.string.settings_dark_mode), items[darkMode!!.choice]))
    }

    private fun addHintsDescriptionToSettingsList() {
        settingsList!!.add(RecyclerItem(getString(R.string.settings_show_hints), getString(R.string.settings_show_hints_description)))
    }

    private fun addFactorySettingsDescriptionToSettingsList() {
        settingsList!!.add(RecyclerItem(getString(R.string.settings_factory_settings), getString(R.string.settings_factory_settings_description)))
    }

    override fun onRecyclerItemClick(position: Int) {
        applyOptionsSelection(position)
    }

    private fun applyOptionsSelection(position: Int) {
        when (ListItems.values()[position]) {
            ListItems.ALARM -> settingAlarm()
            ListItems.VIBRATION -> settingVibration()
            ListItems.ANIMATION -> settingAnimation()
            ListItems.TEMPERATURE_UNIT -> settingTemperatureUnit()
            ListItems.OVERVIEW_HEADER -> settingOverviewHeader()
            ListItems.DARK_MODE -> settingDarkMode()
            ListItems.HINTS -> settingHints()
            ListItems.FACTORY_SETTINGS -> settingFactorySettings()
        }
    }

    private fun settingAlarm() {
        createAlarmRequest()
    }

    private fun createAlarmRequest() {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, R.string.settings_alarm_selection_title)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, null as Uri?)
        alarmRequestActivityResultLauncher.launch(intent)
    }

    private fun processMusicChoice(intent: Intent?) {
        val uri = intent!!.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
        val ringtone = RingtoneManager.getRingtone(this, uri)
        val name = ringtone.getTitle(this)
        if (uri != null) {
            settingsViewModel!!.setMusicChoice(uri.toString())
            settingsViewModel!!.musicName = name
        } else {
            settingsViewModel!!.setMusicChoice(null)
            settingsViewModel!!.musicName = "-"
        }
        fillAndRefreshSettingsList()
        adapter!!.notifyItemChanged(ListItems.ALARM.ordinal)
    }

    private fun settingVibration() {
        val items = resources.getStringArray(R.array.settings_options)

        val checkedItem = if (settingsViewModel!!.isVibration) 0 else 1

        AlertDialog.Builder(this, R.style.dialog_theme)
            .setTitle(R.string.settings_vibration)
            .setSingleChoiceItems(items, checkedItem)
            { dialog: DialogInterface, item: Int -> vibrationChanged(dialog, item) }
            .setNegativeButton(R.string.settings_cancel, null)
            .show()
    }

    private fun vibrationChanged(dialog: DialogInterface, item: Int) {
        settingsViewModel!!.isVibration = item == 0

        fillAndRefreshSettingsList()
        adapter!!.notifyItemChanged(ListItems.VIBRATION.ordinal)
        dialog.dismiss()
    }

    private fun settingAnimation() {
        val items = resources.getStringArray(R.array.settings_options)

        val checkedItem = if (settingsViewModel!!.isAnimation) 0 else 1

        AlertDialog.Builder(this, R.style.dialog_theme)
            .setTitle(R.string.settings_animation)
            .setSingleChoiceItems(items, checkedItem)
            { dialog: DialogInterface, item: Int -> animationChanged(dialog, item) }
            .setNegativeButton(R.string.settings_cancel, null)
            .show()
    }

    private fun animationChanged(dialog: DialogInterface, item: Int) {
        settingsViewModel!!.isAnimation = item == 0

        fillAndRefreshSettingsList()
        adapter!!.notifyItemChanged(ListItems.ANIMATION.ordinal)
        dialog.dismiss()
    }

    private fun settingTemperatureUnit() {
        val items = resources.getStringArray(R.array.settings_temperature_units)

        val checkedItem = settingsViewModel!!.temperatureUnit!!.choice

        AlertDialog.Builder(this, R.style.dialog_theme)
            .setTitle(R.string.settings_temperature_unit)
            .setSingleChoiceItems(items, checkedItem)
            { dialog: DialogInterface, item: Int -> temperatureUnitChanged(dialog, item) }
            .setNegativeButton(R.string.settings_cancel, null)
            .show()
    }

    private fun temperatureUnitChanged(dialog: DialogInterface, item: Int) {
        settingsViewModel!!.temperatureUnit = TemperatureUnit.fromChoice(item)
        fillAndRefreshSettingsList()
        adapter!!.notifyItemChanged(ListItems.TEMPERATURE_UNIT.ordinal)
        dialog.dismiss()
    }

    private fun settingOverviewHeader() {
        val items = resources.getStringArray(R.array.settings_options)

        val checkedItem = if (settingsViewModel!!.isOverviewHeader) 0 else 1

        AlertDialog.Builder(this, R.style.dialog_theme)
            .setTitle(R.string.settings_overview_header)
            .setSingleChoiceItems(items, checkedItem)
            { dialog: DialogInterface, item: Int -> overviewHeaderChanged(dialog, item) }
            .setNegativeButton(R.string.settings_cancel, null)
            .show()
    }

    private fun overviewHeaderChanged(dialog: DialogInterface, item: Int) {
        settingsViewModel!!.isOverviewHeader = item == 0

        fillAndRefreshSettingsList()
        adapter!!.notifyItemChanged(ListItems.OVERVIEW_HEADER.ordinal)
        dialog.dismiss()
    }

    private fun settingDarkMode() {
        val items = resources.getStringArray(R.array.settings_dark_mode)

        val checkedItem = settingsViewModel!!.darkMode!!.choice

        AlertDialog.Builder(this, R.style.dialog_theme)
            .setTitle(R.string.settings_dark_mode)
            .setSingleChoiceItems(items, checkedItem)
            { dialog: DialogInterface, item: Int -> darkModeChanged(items[item], dialog) }
            .setNegativeButton(R.string.settings_cancel, null)
            .show()
    }

    private fun darkModeChanged(item: String, dialog: DialogInterface) {
        val items = resources.getStringArray(R.array.settings_dark_mode)
        val choice = Arrays.asList(*items).indexOf(item)
        val darkMode = DarkMode.fromChoice(choice)

        settingsViewModel!!.darkMode = darkMode
        applyTheme(darkMode)

        fillAndRefreshSettingsList()
        adapter!!.notifyItemChanged(ListItems.DARK_MODE.ordinal)
        dialog.dismiss()
    }

    private fun settingHints() {
        val parent = findViewById<ViewGroup>(R.id.settings_parent)

        val inflater = layoutInflater
        val alertLayoutDialog = inflater.inflate(R.layout.dialog_settings_hints, parent, false)

        val checkBoxUpdate = alertLayoutDialog.findViewById<CheckBox>(R.id.check_box_settings_dialog_update)
        checkBoxUpdate.isChecked = settingsViewModel!!.overviewUpdateAlert

        val checkBoxDescription = alertLayoutDialog.findViewById<CheckBox>(R.id.check_box_settings_dialog_description)
        checkBoxDescription.isChecked = settingsViewModel!!.isShowTeaAlert

        AlertDialog.Builder(this, R.style.dialog_theme)
            .setView(alertLayoutDialog)
            .setTitle(R.string.settings_show_hints_header)
            .setPositiveButton(R.string.settings_show_hints_ok) { _, _ -> displayedHintsChanged(checkBoxUpdate, checkBoxDescription) }
            .setNegativeButton(R.string.settings_show_hints_cancel, null)
            .show()
    }

    private fun displayedHintsChanged(checkBoxUpdate: CheckBox, checkBoxDescription: CheckBox) {
        settingsViewModel!!.overviewUpdateAlert = checkBoxUpdate.isChecked
        settingsViewModel!!.isShowTeaAlert = checkBoxDescription.isChecked
    }

    private fun settingFactorySettings() {
        AlertDialog.Builder(this, R.style.dialog_theme)
            .setMessage(R.string.settings_factory_settings_text)
            .setTitle(R.string.settings_factory_settings)
            .setPositiveButton(R.string.settings_factory_settings_ok) { _, _ -> resetToFactorySettings() }
            .setNegativeButton(R.string.settings_factory_settings_cancel, null)
            .show()
    }

    private fun resetToFactorySettings() {
        if (sdkVersion >= VERSION_CODES.Q) {
            settingsViewModel!!.deleteAllTeaImages()
        }
        settingsViewModel!!.deleteAllTeas()
        settingsViewModel!!.setDefaultSettings()

        fillAndRefreshSettingsList()
        adapter!!.notifyDataSetChanged()
        Toast.makeText(applicationContext, R.string.settings_factory_settings_toast, Toast.LENGTH_SHORT).show()
    }
}