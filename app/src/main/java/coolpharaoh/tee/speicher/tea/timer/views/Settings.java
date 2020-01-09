package coolpharaoh.tee.speicher.tea.timer.views;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import java.util.ArrayList;
import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.SettingsViewModel;
import coolpharaoh.tee.speicher.tea.timer.views.helper.Permissions;
import coolpharaoh.tee.speicher.tea.timer.views.listadapter.ListRowItem;
import coolpharaoh.tee.speicher.tea.timer.views.listadapter.SettingListAdapter;

import static coolpharaoh.tee.speicher.tea.timer.views.helper.Permissions.CODE_REQUEST_READ;

public class Settings extends AppCompatActivity {

    private enum ListItems {
        Alarm, Vibration, Animation, TemperatureUnit, Hints, FactorySettings
    }

    private SettingsViewModel settingsViewModel;

    private ArrayList<ListRowItem> settingList;
    private SettingListAdapter adapter;
    private AlertDialog radioButtonDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Toolbar als ActionBar festlegen
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.settings_heading);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settingsViewModel = new SettingsViewModel(getApplicationContext());

        //write ListView
        settingList = new ArrayList<>();

        refreshWindow();


        //Liste mit Adapter verknüpfen
        adapter = new SettingListAdapter(this, settingList);
        //Adapter dem Listview hinzufügen
        ListView listViewSetting = findViewById(R.id.listView_settings);
        listViewSetting.setAdapter(adapter);

        listViewSetting.setOnItemClickListener((parent, view, position, id) -> {
            ListItems item = ListItems.values()[position];
            switch (item) {
                case Alarm:
                    settingAlarm();
                    break;
                case Vibration:
                    settingVibration();
                    break;
                case Animation:
                    settingAnimation();
                    break;
                case TemperatureUnit:
                    settingTemperatureUnit();
                    break;
                case Hints:
                    settingHints();
                    break;
                case FactorySettings:
                    settingFactorySettings(view);
                    break;
            }

        });
    }

    private void settingAlarm() {
        if (!Permissions.checkReadPermission(this) && settingsViewModel.isSettingspermissionalert()) {
            readPermissionDialog();
        } else {
            createAlarmRequest();
        }
    }

    private void readPermissionDialog() {
        ViewGroup parent = findViewById(R.id.main_parent);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayoutDialogProblem = inflater.inflate(R.layout.dialogalarmpermission, parent, false);
        final CheckBox dontshowagain = alertLayoutDialogProblem.findViewById(R.id.checkboxDialogSettingsReadPermission);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertLayoutDialogProblem);
        adb.setTitle(R.string.settings_read_permission_dialog_header);
        adb.setPositiveButton(R.string.settings_read_permission_dialog_ok, (dialog, which) -> {
            if (dontshowagain.isChecked()) {
                settingsViewModel.setSettingsPermissionAlert(false);
            }
            Permissions.getReadPermission(this);
        });
        adb.show();
    }

    private void createAlarmRequest() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, R.string.settings_alarm_selection_title);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        startActivityForResult(intent, 5);
    }

    private void settingVibration() {
        final String[] items = getResources().getStringArray(R.array.settings_options);

        //Get CheckedItem
        int checkedItem;
        if (settingsViewModel.isVibration()) {
            checkedItem = 0;
        } else {
            checkedItem = 1;
        }

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.MaterialThemeDialog);
        builder.setTitle(R.string.settings_vibration);
        builder.setSingleChoiceItems(items, checkedItem, (dialog, item) -> {
            switch (item) {
                case 0:
                    settingsViewModel.setVibration(true);
                    break;
                case 1:
                    settingsViewModel.setVibration(false);
                    break;
            }
            refreshWindow();
            adapter.notifyDataSetChanged();
            radioButtonDialog.dismiss();
        });
        radioButtonDialog = builder.create();
        radioButtonDialog.show();
    }

    private void settingAnimation() {
        final String[] items = getResources().getStringArray(R.array.settings_options);

        //Get CheckedItem
        int checkedItem;
        if (settingsViewModel.isAnimation()) {
            checkedItem = 0;
        } else {
            checkedItem = 1;
        }

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.MaterialThemeDialog);
        builder.setTitle(R.string.settings_animation);
        builder.setSingleChoiceItems(items, checkedItem, (dialog, item) -> {
            switch (item) {
                case 0:
                    settingsViewModel.setAnimation(true);
                    break;
                case 1:
                    settingsViewModel.setAnimation(false);
                    break;
            }
            refreshWindow();
            adapter.notifyDataSetChanged();
            radioButtonDialog.dismiss();
        });
        radioButtonDialog = builder.create();
        radioButtonDialog.show();
    }

    private void settingTemperatureUnit() {
        final String[] items = getResources().getStringArray(R.array.settings_temperature_units);

        //Get CheckedItem
        int checkedItem;
        if (settingsViewModel.getTemperatureunit().equals(items[0])) {
            checkedItem = 0;
        } else {
            checkedItem = 1;
        }

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.MaterialThemeDialog);
        builder.setTitle(R.string.settings_temperature_unit);
        builder.setSingleChoiceItems(items, checkedItem, (dialog, item) -> {
            settingsViewModel.setTemperatureunit(items[item]);
            refreshWindow();
            adapter.notifyDataSetChanged();
            radioButtonDialog.dismiss();
        });
        radioButtonDialog = builder.create();
        radioButtonDialog.show();
    }

    private void settingHints() {
        ViewGroup parent = findViewById(R.id.settings_parent);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayoutDialogProblem = inflater.inflate(R.layout.dialogsettingshints, parent, false);
        final CheckBox checkBoxRating = alertLayoutDialogProblem.findViewById(R.id.checkboxDialogSettingsRating);
        checkBoxRating.setChecked(settingsViewModel.isMainratealert());

        final CheckBox checkBoxDescription = alertLayoutDialogProblem.findViewById(R.id.checkboxDialogSettingsDescription);
        checkBoxDescription.setChecked(settingsViewModel.isShowteaalert());

        final CheckBox checkBoxPermission = alertLayoutDialogProblem.findViewById(R.id.checkboxDialogSettingsPermission);
        checkBoxPermission.setChecked(settingsViewModel.isSettingspermissionalert());


        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertLayoutDialogProblem);
        adb.setTitle(R.string.settings_show_hints_header);
        adb.setPositiveButton(R.string.settings_show_hints_ok, (dialog, which) -> {
            settingsViewModel.setMainratealert(checkBoxRating.isChecked());
            settingsViewModel.setShowteaalert(checkBoxDescription.isChecked());
            settingsViewModel.setSettingsPermissionAlert(checkBoxPermission.isChecked());
        });
        adb.setNegativeButton(R.string.settings_show_hints_cancle, (dialog, which) -> {

        });
        adb.show();

    }

    private void settingFactorySettings(View v) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Einstellungen zurücksetzen und Tees löschen
                    settingsViewModel.setDefaultSettings();
                    settingsViewModel.deleteAllTeas();
                    //Felder ändern
                    refreshWindow();
                    adapter.notifyDataSetChanged();
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.settings_factory_settings_toast, Toast.LENGTH_SHORT);
                    toast.show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage(R.string.settings_factory_settings_text)
                .setTitle(R.string.settings_factory_settings)
                .setPositiveButton(R.string.settings_factory_settings_ok, dialogClickListener)
                .setNegativeButton(R.string.settings_factory_settings_cancel, dialogClickListener).show();
    }

    private void refreshWindow() {
        settingList.clear();

        ListRowItem itemSound = new ListRowItem(getResources().getString(R.string.settings_alarm), settingsViewModel.getMusicname());
        settingList.add(itemSound);

        //Decision for Vibration and Animation
        String[] itemsOnOff = getResources().getStringArray(R.array.settings_options);

        //Get Option for the Vibration
        int vibrationOption;
        if (settingsViewModel.isVibration()) {
            vibrationOption = 0;
        } else {
            vibrationOption = 1;
        }
        ListRowItem itemVibration = new ListRowItem(getResources().getString(R.string.settings_vibration), itemsOnOff[vibrationOption]);
        settingList.add(itemVibration);

        //Get Option for the Animation
        int animationOption;
        if (settingsViewModel.isAnimation()) {
            animationOption = 0;
        } else {
            animationOption = 1;
        }
        ListRowItem itemAnimation = new ListRowItem(getResources().getString(R.string.settings_animation), itemsOnOff[animationOption]);
        settingList.add(itemAnimation);

        //TemperatureUnit
        ListRowItem itemTemperatureUnit = new ListRowItem(getResources().getString(R.string.settings_temperature_unit), settingsViewModel.getTemperatureunit());
        settingList.add(itemTemperatureUnit);

        //Hints
        ListRowItem itemHints = new ListRowItem(getResources().getString(R.string.settings_show_hints), getResources().getString(R.string.settings_show_hints_description));
        settingList.add(itemHints);

        //Factory Setting
        ListRowItem itemFactorySettings = new ListRowItem(getResources().getString(R.string.settings_factory_settings), getResources().getString(R.string.settings_factory_settings_description));
        settingList.add(itemFactorySettings);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == 5) {
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
            refreshWindow();
            adapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODE_REQUEST_READ: {
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(getApplicationContext(), R.string.settings_read_permission_denied, Toast.LENGTH_LONG).show();
                }
                createAlarmRequest();
            } break;
        }
    }
}
