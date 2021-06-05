package coolpharaoh.tee.speicher.tea.timer.views.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.annotation.NonNull;
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
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.views.utils.ThemeManager;
import coolpharaoh.tee.speicher.tea.timer.views.utils.permissions.PermissionRequester;
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.ListRowItem;
import coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview.RecyclerViewAdapter;

import static coolpharaoh.tee.speicher.tea.timer.views.utils.permissions.Permissions.REQUEST_CODE_READ;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class Settings extends AppCompatActivity implements RecyclerViewAdapter.OnClickListener {

    private enum ListItems {
        ALARM, VIBRATION, ANIMATION, TEMPERATURE_UNIT, DARK_MODE, HINTS, FACTORY_SETTINGS
    }

    private SettingsViewModel settingsViewModel;
    private SharedSettings sharedSettings;

    private ArrayList<ListRowItem> settingsList;
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
        sharedSettings = new SharedSettings(getApplication());

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
        addDarkModeChoiceToSettingsList();
        addHintsDesciptionToSettingsList();
        addFactorySettingsDesciptionToSettingsList();
    }

    private void addMusicChoiceToSettingsList() {
        final ListRowItem itemSound = new ListRowItem(getString(R.string.settings_alarm), settingsViewModel.getMusicname());
        settingsList.add(itemSound);
    }

    private void addVibrationChoiceToSettingsList() {
        final String[] itemsOnOff = getResources().getStringArray(R.array.settings_options);

        final int vibrationOption = settingsViewModel.isVibration() ? 0 : 1;

        settingsList.add(new ListRowItem(getString(R.string.settings_vibration), itemsOnOff[vibrationOption]));
    }

    private void addAnimationChoiceToSettingsList() {
        final String[] itemsOnOff = getResources().getStringArray(R.array.settings_options);

        final int animationOption = settingsViewModel.isAnimation() ? 0 : 1;

        settingsList.add(new ListRowItem(getString(R.string.settings_animation), itemsOnOff[animationOption]));
    }

    private void addTemperatureChoiceToSettingsList() {
        settingsList.add(new ListRowItem(getString(R.string.settings_temperature_unit), settingsViewModel.getTemperatureUnit()));
    }

    private void addDarkModeChoiceToSettingsList() {
        final DarkMode darkMode = sharedSettings.getDarkMode();
        final String[] items = getResources().getStringArray(R.array.settings_dark_mode);

        settingsList.add(new ListRowItem(getString(R.string.settings_dark_mode), items[darkMode.getChoice()]));
    }

    private void addHintsDesciptionToSettingsList() {
        settingsList.add(new ListRowItem(getString(R.string.settings_show_hints), getString(R.string.settings_show_hints_description)));
    }

    private void addFactorySettingsDesciptionToSettingsList() {
        settingsList.add(new ListRowItem(getString(R.string.settings_factory_settings), getString(R.string.settings_factory_settings_description)));
    }

    @Override
    public void onRecyclerItemClick(int position) {
        applyOptionsSelection(position);
    }

    private void applyOptionsSelection(int position) {
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
        if (!PermissionRequester.checkReadPermission(this) && settingsViewModel.isSettingsPermissionAlert()) {
            readPermissionDialog();
        } else {
            createAlarmRequest();
        }
    }

    private void readPermissionDialog() {
        final ViewGroup parent = findViewById(R.id.overview_parent);

        final LayoutInflater inflater = getLayoutInflater();
        final View alertLayoutDialogProblem = inflater.inflate(R.layout.dialog_alarm_permission, parent, false);

        new AlertDialog.Builder(this, R.style.dialog_theme)
                .setView(alertLayoutDialogProblem)
                .setTitle(R.string.settings_read_permission_dialog_header)
                .setPositiveButton(R.string.settings_read_permission_dialog_ok, (dialog, which) -> askPermissionAccepted(alertLayoutDialogProblem))
                .show();
    }

    private void askPermissionAccepted(final View alertLayoutDialogProblem) {
        final CheckBox dontShowAgain = alertLayoutDialogProblem.findViewById(R.id.check_box_settings_dialog_read_permission);

        if (dontShowAgain.isChecked()) {
            settingsViewModel.setSettingsPermissionAlert(false);
        }
        PermissionRequester.getReadPermission(this);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        if (requestCode == REQUEST_CODE_READ) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getApplicationContext(), R.string.settings_read_permission_denied, Toast.LENGTH_LONG).show();
            }
            createAlarmRequest();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
            settingsViewModel.setMusicchoice(uri.toString());
            settingsViewModel.setMusicname(name);
        } else {
            settingsViewModel.setMusicchoice(null);
            settingsViewModel.setMusicname("-");
        }
        fillAndRefreshSettingsList();
        adapter.notifyDataSetChanged();
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
        adapter.notifyDataSetChanged();
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
        adapter.notifyDataSetChanged();
        dialog.dismiss();
    }

    private void settingTemperatureUnit() {
        final String[] items = getResources().getStringArray(R.array.settings_temperature_units);

        final int checkedItem = settingsViewModel.getTemperatureUnit().equals(items[0]) ? 0 : 1;

        new AlertDialog.Builder(this, R.style.dialog_theme)
                .setTitle(R.string.settings_temperature_unit)
                .setSingleChoiceItems(items, checkedItem, (dialog, item) -> temperatureUnitChanged(items[item], dialog))
                .setNegativeButton(R.string.settings_cancel, null)
                .show();
    }

    private void temperatureUnitChanged(final String item, final DialogInterface dialog) {
        settingsViewModel.setTemperatureUnit(item);
        fillAndRefreshSettingsList();
        adapter.notifyDataSetChanged();
        dialog.dismiss();
    }

    private void settingDarkMode() {
        final String[] items = getResources().getStringArray(R.array.settings_dark_mode);

        final int checkedItem = sharedSettings.getDarkMode().getChoice();

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

        sharedSettings.setSetDarkMode(darkMode);
        ThemeManager.applyTheme(darkMode);

        fillAndRefreshSettingsList();
        adapter.notifyDataSetChanged();
        dialog.dismiss();
    }

    private void settingHints() {
        final ViewGroup parent = findViewById(R.id.settings_parent);

        final LayoutInflater inflater = getLayoutInflater();
        final View alertLayoutDialog = inflater.inflate(R.layout.dialog_settings_hints, parent, false);

        final CheckBox checkBoxUpdate = alertLayoutDialog.findViewById(R.id.check_box_settings_dialog_update);
        checkBoxUpdate.setChecked(settingsViewModel.isMainUpdateAlert());

        final CheckBox checkBoxRating = alertLayoutDialog.findViewById(R.id.check_box_settings_dialog_rating);
        checkBoxRating.setChecked(settingsViewModel.isMainRateAlert());

        final CheckBox checkBoxDescription = alertLayoutDialog.findViewById(R.id.check_box_settings_dialog_description);
        checkBoxDescription.setChecked(settingsViewModel.isShowTeaAlert());

        final CheckBox checkBoxPermission = alertLayoutDialog.findViewById(R.id.check_box_settings_dialog_permission);
        checkBoxPermission.setChecked(settingsViewModel.isSettingsPermissionAlert());


        new AlertDialog.Builder(this, R.style.dialog_theme)
                .setView(alertLayoutDialog)
                .setTitle(R.string.settings_show_hints_header)
                .setPositiveButton(R.string.settings_show_hints_ok, (dialog, which) -> displayedHintsChanged(checkBoxUpdate,
                        checkBoxRating, checkBoxDescription, checkBoxPermission))
                .setNegativeButton(R.string.settings_show_hints_cancel, null)
                .show();

    }

    private void displayedHintsChanged(final CheckBox checkBoxUpdate, final CheckBox checkBoxRating,
                                       final CheckBox checkBoxDescription, final CheckBox checkBoxPermission) {
        settingsViewModel.setMainUpdateAlert(checkBoxUpdate.isChecked());
        settingsViewModel.setMainRateAlert(checkBoxRating.isChecked());
        settingsViewModel.setShowTeaAlert(checkBoxDescription.isChecked());
        settingsViewModel.setSettingsPermissionAlert(checkBoxPermission.isChecked());
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
        settingsViewModel.setDefaultSettings();
        settingsViewModel.deleteAllTeas();

        fillAndRefreshSettingsList();
        adapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), R.string.settings_factory_settings_toast, Toast.LENGTH_SHORT).show();
    }
}
