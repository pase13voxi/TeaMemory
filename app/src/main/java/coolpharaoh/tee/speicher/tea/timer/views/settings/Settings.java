package coolpharaoh.tee.speicher.tea.timer.views.settings;

import static android.os.Build.VERSION_CODES.Q;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.DarkMode;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit;
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk;
import coolpharaoh.tee.speicher.tea.timer.views.utils.ThemeManager;
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.RecyclerItem;
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.RecyclerViewAdapter;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class Settings extends AppCompatActivity implements RecyclerViewAdapter.OnClickListener {

    private enum ListItems {
        ALARM, VIBRATION, ANIMATION, TEMPERATURE_UNIT, OVERVIEW_HEADER, DARK_MODE, HINTS, FACTORY_SETTINGS
    }

    private SettingsViewModel settingsViewModel;

    private ArrayList<RecyclerItem> settingsList;
    private RecyclerViewAdapter adapter;

    private final ActivityResultLauncher<Intent> alarmRequestActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    processMusicChoice(result.getData());
                }
            });

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        defineToolbarAsActionbar();
        enableAndShowBackButton();

        settingsViewModel = new SettingsViewModel(getApplication());

        initializeSettingsListView();
    }

    private void defineToolbarAsActionbar() {
        final Toolbar toolbar = findViewById(R.id.tool_bar);
        final TextView mToolbarCustomTitle = findViewById(R.id.tool_bar_title);
        mToolbarCustomTitle.setText(R.string.settings_heading);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void enableAndShowBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeSettingsListView() {
        settingsList = new ArrayList<>();
        fillAndRefreshSettingsList();

        adapter = new RecyclerViewAdapter(R.layout.list_single_layout_setting, settingsList, this);

        final RecyclerView recyclerViewDetails = findViewById(R.id.recycler_view_settings);
        recyclerViewDetails.addItemDecoration(new DividerItemDecoration(recyclerViewDetails.getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewDetails.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDetails.setAdapter(adapter);
    }

    private void fillAndRefreshSettingsList() {
        settingsList.clear();

        addMusicChoiceToSettingsList();
        addVibrationChoiceToSettingsList();
        addAnimationChoiceToSettingsList();
        addTemperatureChoiceToSettingsList();
        addOverviewHeaderToSettingsList();
        addDarkModeChoiceToSettingsList();
        addHintsDescriptionToSettingsList();
        addFactorySettingsDescriptionToSettingsList();
    }

    private void addMusicChoiceToSettingsList() {
        final RecyclerItem itemSound = new RecyclerItem(getString(R.string.settings_alarm), settingsViewModel.getMusicName());
        settingsList.add(itemSound);
    }

    private void addVibrationChoiceToSettingsList() {
        final String[] itemsOnOff = getResources().getStringArray(R.array.settings_options);

        final int vibrationOption = settingsViewModel.isVibration() ? 0 : 1;

        settingsList.add(new RecyclerItem(getString(R.string.settings_vibration), itemsOnOff[vibrationOption]));
    }

    private void addAnimationChoiceToSettingsList() {
        final String[] itemsOnOff = getResources().getStringArray(R.array.settings_options);

        final int animationOption = settingsViewModel.isAnimation() ? 0 : 1;

        settingsList.add(new RecyclerItem(getString(R.string.settings_animation), itemsOnOff[animationOption]));
    }

    private void addTemperatureChoiceToSettingsList() {
        final String[] itemTemperature = getResources().getStringArray(R.array.settings_temperature_units);

        settingsList.add(new RecyclerItem(getString(R.string.settings_temperature_unit), itemTemperature[settingsViewModel.getTemperatureUnit().getChoice()]));
    }

    private void addOverviewHeaderToSettingsList() {
        final String[] itemsOnOff = getResources().getStringArray(R.array.settings_options);

        final int overviewHeaderOption = settingsViewModel.isOverviewHeader() ? 0 : 1;

        settingsList.add(new RecyclerItem(getString(R.string.settings_overview_header), itemsOnOff[overviewHeaderOption]));
    }

    private void addDarkModeChoiceToSettingsList() {
        final DarkMode darkMode = settingsViewModel.getDarkMode();
        final String[] items = getResources().getStringArray(R.array.settings_dark_mode);

        settingsList.add(new RecyclerItem(getString(R.string.settings_dark_mode), items[darkMode.getChoice()]));
    }

    private void addHintsDescriptionToSettingsList() {
        settingsList.add(new RecyclerItem(getString(R.string.settings_show_hints), getString(R.string.settings_show_hints_description)));
    }

    private void addFactorySettingsDescriptionToSettingsList() {
        settingsList.add(new RecyclerItem(getString(R.string.settings_factory_settings), getString(R.string.settings_factory_settings_description)));
    }

    @Override
    public void onRecyclerItemClick(final int position) {
        applyOptionsSelection(position);
    }

    private void applyOptionsSelection(final int position) {
        final ListItems item = ListItems.values()[position];
        switch (item) {
            case ALARM:
                settingAlarm();
                break;
            case VIBRATION:
                settingVibration();
                break;
            case ANIMATION:
                settingAnimation();
                break;
            case TEMPERATURE_UNIT:
                settingTemperatureUnit();
                break;
            case OVERVIEW_HEADER:
                settingOverviewHeader();
                break;
            case DARK_MODE:
                settingDarkMode();
                break;
            case HINTS:
                settingHints();
                break;
            case FACTORY_SETTINGS:
                settingFactorySettings();
                break;
            default:
        }
    }

    private void settingAlarm() {
        createAlarmRequest();
    }

    private void createAlarmRequest() {
        final Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, R.string.settings_alarm_selection_title);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        alarmRequestActivityResultLauncher.launch(intent);
    }

    private void processMusicChoice(final Intent intent) {
        final Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        final Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
        final String name = ringtone.getTitle(this);
        if (uri != null) {
            settingsViewModel.setMusicChoice(uri.toString());
            settingsViewModel.setMusicName(name);
        } else {
            settingsViewModel.setMusicChoice(null);
            settingsViewModel.setMusicName("-");
        }
        fillAndRefreshSettingsList();
        adapter.notifyItemChanged(ListItems.ALARM.ordinal());
    }

    private void settingVibration() {
        final String[] items = getResources().getStringArray(R.array.settings_options);

        final int checkedItem = settingsViewModel.isVibration() ? 0 : 1;

        new AlertDialog.Builder(this, R.style.dialog_theme)
                .setTitle(R.string.settings_vibration)
                .setSingleChoiceItems(items, checkedItem, this::vibrationChanged)
                .setNegativeButton(R.string.settings_cancel, null)
                .show();
    }

    private void vibrationChanged(final DialogInterface dialog, final int item) {
        settingsViewModel.setVibration(item == 0);

        fillAndRefreshSettingsList();
        adapter.notifyItemChanged(ListItems.VIBRATION.ordinal());
        dialog.dismiss();
    }

    private void settingAnimation() {
        final String[] items = getResources().getStringArray(R.array.settings_options);

        final int checkedItem = settingsViewModel.isAnimation() ? 0 : 1;

        new AlertDialog.Builder(this, R.style.dialog_theme)
                .setTitle(R.string.settings_animation)
                .setSingleChoiceItems(items, checkedItem, this::animationChanged)
                .setNegativeButton(R.string.settings_cancel, null)
                .show();
    }

    private void animationChanged(final DialogInterface dialog, final int item) {
        settingsViewModel.setAnimation(item == 0);

        fillAndRefreshSettingsList();
        adapter.notifyItemChanged(ListItems.ANIMATION.ordinal());
        dialog.dismiss();
    }

    private void settingTemperatureUnit() {
        final String[] items = getResources().getStringArray(R.array.settings_temperature_units);

        final int checkedItem = settingsViewModel.getTemperatureUnit().getChoice();

        new AlertDialog.Builder(this, R.style.dialog_theme)
                .setTitle(R.string.settings_temperature_unit)
                .setSingleChoiceItems(items, checkedItem, this::temperatureUnitChanged)
                .setNegativeButton(R.string.settings_cancel, null)
                .show();
    }

    private void temperatureUnitChanged(final DialogInterface dialog, final int item) {
        settingsViewModel.setTemperatureUnit(TemperatureUnit.fromChoice(item));
        fillAndRefreshSettingsList();
        adapter.notifyItemChanged(ListItems.TEMPERATURE_UNIT.ordinal());
        dialog.dismiss();
    }

    private void settingOverviewHeader() {
        final String[] items = getResources().getStringArray(R.array.settings_options);

        final int checkedItem = settingsViewModel.isOverviewHeader() ? 0 : 1;

        new AlertDialog.Builder(this, R.style.dialog_theme)
                .setTitle(R.string.settings_overview_header)
                .setSingleChoiceItems(items, checkedItem, this::overviewHeaderChanged)
                .setNegativeButton(R.string.settings_cancel, null)
                .show();
    }

    private void overviewHeaderChanged(final DialogInterface dialog, final int item) {
        settingsViewModel.setOverviewHeader(item == 0);

        fillAndRefreshSettingsList();
        adapter.notifyItemChanged(ListItems.OVERVIEW_HEADER.ordinal());
        dialog.dismiss();
    }

    private void settingDarkMode() {
        final String[] items = getResources().getStringArray(R.array.settings_dark_mode);

        final int checkedItem = settingsViewModel.getDarkMode().getChoice();

        new AlertDialog.Builder(this, R.style.dialog_theme)
                .setTitle(R.string.settings_dark_mode)
                .setSingleChoiceItems(items, checkedItem, (dialog, item) -> darkModeChanged(items[item], dialog))
                .setNegativeButton(R.string.settings_cancel, null)
                .show();
    }

    private void darkModeChanged(final String item, final DialogInterface dialog) {
        final String[] items = getResources().getStringArray(R.array.settings_dark_mode);
        final int choice = Arrays.asList(items).indexOf(item);
        final DarkMode darkMode = DarkMode.fromChoice(choice);

        settingsViewModel.setDarkMode(darkMode);
        ThemeManager.applyTheme(darkMode);

        fillAndRefreshSettingsList();
        adapter.notifyItemChanged(ListItems.DARK_MODE.ordinal());
        dialog.dismiss();
    }

    private void settingHints() {
        final ViewGroup parent = findViewById(R.id.settings_parent);

        final LayoutInflater inflater = getLayoutInflater();
        final View alertLayoutDialog = inflater.inflate(R.layout.dialog_settings_hints, parent, false);

        final CheckBox checkBoxUpdate = alertLayoutDialog.findViewById(R.id.check_box_settings_dialog_update);
        checkBoxUpdate.setChecked(settingsViewModel.isMainUpdateAlert());

        final CheckBox checkBoxDescription = alertLayoutDialog.findViewById(R.id.check_box_settings_dialog_description);
        checkBoxDescription.setChecked(settingsViewModel.isShowTeaAlert());

        new AlertDialog.Builder(this, R.style.dialog_theme)
                .setView(alertLayoutDialog)
                .setTitle(R.string.settings_show_hints_header)
                .setPositiveButton(R.string.settings_show_hints_ok, (dialog, which) -> displayedHintsChanged(checkBoxUpdate,
                        checkBoxDescription))
                .setNegativeButton(R.string.settings_show_hints_cancel, null)
                .show();

    }

    private void displayedHintsChanged(final CheckBox checkBoxUpdate, final CheckBox checkBoxDescription) {
        settingsViewModel.setOverviewUpdateAlert(checkBoxUpdate.isChecked());
        settingsViewModel.setShowTeaAlert(checkBoxDescription.isChecked());
    }

    private void settingFactorySettings() {
        new AlertDialog.Builder(this, R.style.dialog_theme)
                .setMessage(R.string.settings_factory_settings_text)
                .setTitle(R.string.settings_factory_settings)
                .setPositiveButton(R.string.settings_factory_settings_ok, (dialogInterface, i) -> resetToFactorySettings())
                .setNegativeButton(R.string.settings_factory_settings_cancel, null)
                .show();
    }

    private void resetToFactorySettings() {
        if (CurrentSdk.getSdkVersion() >= Q) {
            settingsViewModel.deleteAllTeaImages();
        }
        settingsViewModel.deleteAllTeas();
        settingsViewModel.setDefaultSettings();

        fillAndRefreshSettingsList();
        adapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), R.string.settings_factory_settings_toast, Toast.LENGTH_SHORT).show();
    }
}
