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
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.utils.ListRowItem;
import coolpharaoh.tee.speicher.tea.timer.views.utils.permissions.PermissionRequester;

import static coolpharaoh.tee.speicher.tea.timer.views.utils.permissions.Permissions.REQUEST_CODE_READ;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class Settings extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final int REQUEST_CODE_MUSIC_CHOICE = 4532;

    private enum ListItems {
        ALARM, VIBRATION, ANIMATION, TEMPERATURE_UNIT, HINTS, FACTORY_SETTINGS
    }

    private SettingsViewModel settingsViewModel;

    private ArrayList<ListRowItem> settingsList;
    private SettingsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        defineToolbarAsActionbar();
        enableAndShowBackButton();

        settingsViewModel = new SettingsViewModel(getApplication());

        initializeSettingsListView();
    }

    private void defineToolbarAsActionbar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView mToolbarCustomTitle = findViewById(R.id.toolbar_title);
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

        adapter = new SettingsListAdapter(this, settingsList);

        ListView listViewSetting = findViewById(R.id.listView_settings);
        listViewSetting.setAdapter(adapter);

        listViewSetting.setOnItemClickListener(this);
    }

    private void fillAndRefreshSettingsList() {
        settingsList.clear();

        addMusicChoiceToSettingsList();
        addVibrationChoiceToSettingsList();
        addAnimationChoiceToSettingsList();
        addTemperatureChoiceToSettingsList();
        addHintsDesciptionToSettingsList();
        addFactorySettingsDesciptionToSettingsList();
    }

    private void addMusicChoiceToSettingsList() {
        ListRowItem itemSound = new ListRowItem(getResources().getString(R.string.settings_alarm), settingsViewModel.getMusicname());
        settingsList.add(itemSound);
    }

    private void addVibrationChoiceToSettingsList() {
        String[] itemsOnOff = getResources().getStringArray(R.array.settings_options);

        int vibrationOption = settingsViewModel.isVibration() ? 0 : 1;

        settingsList.add(new ListRowItem(getResources().getString(R.string.settings_vibration), itemsOnOff[vibrationOption]));
    }

    private void addAnimationChoiceToSettingsList() {
        String[] itemsOnOff = getResources().getStringArray(R.array.settings_options);

        int animationOption = settingsViewModel.isAnimation() ? 0 : 1;

        settingsList.add(new ListRowItem(getResources().getString(R.string.settings_animation), itemsOnOff[animationOption]));
    }

    private void addTemperatureChoiceToSettingsList() {
        settingsList.add(new ListRowItem(getResources().getString(R.string.settings_temperature_unit), settingsViewModel.getTemperatureunit()));
    }

    private void addHintsDesciptionToSettingsList() {
        settingsList.add(new ListRowItem(getResources().getString(R.string.settings_show_hints), getResources().getString(R.string.settings_show_hints_description)));
    }

    private void addFactorySettingsDesciptionToSettingsList() {
        settingsList.add(new ListRowItem(getResources().getString(R.string.settings_factory_settings), getResources().getString(R.string.settings_factory_settings_description)));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        ListItems item = ListItems.values()[position];
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
            case HINTS:
                settingHints();
                break;
            case FACTORY_SETTINGS:
                settingFactorySettings(view);
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
        ViewGroup parent = findViewById(R.id.main_parent);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayoutDialogProblem = inflater.inflate(R.layout.dialog_alarm_permission, parent, false);

        AlertDialog.Builder adb = new AlertDialog.Builder(this, R.style.DialogTheme);
        adb.setView(alertLayoutDialogProblem);
        adb.setTitle(R.string.settings_read_permission_dialog_header);
        adb.setPositiveButton(R.string.settings_read_permission_dialog_ok, (dialog, which) -> askPermissionAccepted(alertLayoutDialogProblem));
        adb.show();
    }

    private void askPermissionAccepted(View alertLayoutDialogProblem) {
        final CheckBox dontShowAgain = alertLayoutDialogProblem.findViewById(R.id.checkboxDialogSettingsReadPermission);

        if (dontShowAgain.isChecked()) {
            settingsViewModel.setSettingsPermissionAlert(false);
        }
        PermissionRequester.getReadPermission(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_READ) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getApplicationContext(), R.string.settings_read_permission_denied, Toast.LENGTH_LONG).show();
            }
            createAlarmRequest();
        }
    }

    private void createAlarmRequest() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, R.string.settings_alarm_selection_title);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        startActivityForResult(intent, REQUEST_CODE_MUSIC_CHOICE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_MUSIC_CHOICE) {
            processMusicChoice(intent);
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    private void processMusicChoice(Intent intent) {
        Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
        String name = ringtone.getTitle(this);
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

        //Get CheckedItem
        int checkedItem = settingsViewModel.isVibration() ? 0 : 1;

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setTitle(R.string.settings_vibration);
        builder.setSingleChoiceItems(items, checkedItem, this::vibrationChanged);
        builder.setNegativeButton(R.string.settings_cancel, null);
        builder.create().show();
    }

    private void vibrationChanged(DialogInterface dialog, int item) {
        if (item == 0) {
            settingsViewModel.setVibration(true);
        } else if (item == 1) {
            settingsViewModel.setVibration(false);
        }
        fillAndRefreshSettingsList();
        adapter.notifyDataSetChanged();
        dialog.dismiss();
    }

    private void settingAnimation() {
        final String[] items = getResources().getStringArray(R.array.settings_options);

        //Get CheckedItem
        int checkedItem = settingsViewModel.isAnimation() ? 0 : 1;

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setTitle(R.string.settings_animation);
        builder.setSingleChoiceItems(items, checkedItem, this::animationChanged);
        builder.setNegativeButton(R.string.settings_cancel, null);
        builder.create().show();
    }

    private void animationChanged(DialogInterface dialog, int item) {
        if (item == 0) {
            settingsViewModel.setAnimation(true);
        } else if (item == 1) {
            settingsViewModel.setAnimation(false);
        }
        fillAndRefreshSettingsList();
        adapter.notifyDataSetChanged();
        dialog.dismiss();
    }

    private void settingTemperatureUnit() {
        final String[] items = getResources().getStringArray(R.array.settings_temperature_units);

        //Get CheckedItem
        int checkedItem = settingsViewModel.getTemperatureunit().equals(items[0]) ? 0 : 1;

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setTitle(R.string.settings_temperature_unit);
        builder.setSingleChoiceItems(items, checkedItem, (dialog, item) -> temperatureUnitChanged(items[item], dialog));
        builder.setNegativeButton(R.string.settings_cancel, null);
        builder.create().show();
    }

    private void temperatureUnitChanged(String item, DialogInterface dialog) {
        settingsViewModel.setTemperatureunit(item);
        fillAndRefreshSettingsList();
        adapter.notifyDataSetChanged();
        dialog.dismiss();
    }

    private void settingHints() {
        ViewGroup parent = findViewById(R.id.settings_parent);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayoutDialog = inflater.inflate(R.layout.dialog_settings_hints, parent, false);

        final CheckBox checkBoxUpdate = alertLayoutDialog.findViewById(R.id.checkboxDialogSettingsUpdate);
        checkBoxUpdate.setChecked(settingsViewModel.isMainUpdateAlert());

        final CheckBox checkBoxRating = alertLayoutDialog.findViewById(R.id.checkboxDialogSettingsRating);
        checkBoxRating.setChecked(settingsViewModel.isMainRateAlert());

        final CheckBox checkBoxDescription = alertLayoutDialog.findViewById(R.id.checkboxDialogSettingsDescription);
        checkBoxDescription.setChecked(settingsViewModel.isShowTeaAlert());

        final CheckBox checkBoxPermission = alertLayoutDialog.findViewById(R.id.checkboxDialogSettingsPermission);
        checkBoxPermission.setChecked(settingsViewModel.isSettingsPermissionAlert());


        new AlertDialog.Builder(this, R.style.DialogTheme)
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

    private void settingFactorySettings(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setMessage(R.string.settings_factory_settings_text);
        builder.setTitle(R.string.settings_factory_settings);
        builder.setPositiveButton(R.string.settings_factory_settings_ok, (dialogInterface, i) -> resetToFactorySettings());
        builder.setNegativeButton(R.string.settings_factory_settings_cancel, null);
        builder.show();
    }

    private void resetToFactorySettings() {
        settingsViewModel.setDefaultSettings();
        settingsViewModel.deleteAllTeas();

        fillAndRefreshSettingsList();
        adapter.notifyDataSetChanged();
        Toast toast = Toast.makeText(getApplicationContext(), R.string.settings_factory_settings_toast, Toast.LENGTH_SHORT);
        toast.show();
    }
}
