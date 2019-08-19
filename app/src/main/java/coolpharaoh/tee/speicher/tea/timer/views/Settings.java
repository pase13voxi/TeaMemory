package coolpharaoh.tee.speicher.tea.timer.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.listadapter.ListRowItem;
import coolpharaoh.tee.speicher.tea.timer.listadapter.SettingListAdapter;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.SettingsViewModel;

public class Settings extends AppCompatActivity {

    private enum ListItems {
        Alarm, Vibration, Notification, Animation, TemperatureUnit, Hints, FactorySettings
    }

    private SettingsViewModel mSettingsViewModel;

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

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSettingsViewModel = new SettingsViewModel(getApplicationContext());

        //write ListView
        settingList = new ArrayList<>();

        refreshWindow();


        //Liste mit Adapter verknüpfen
        adapter = new SettingListAdapter(this, settingList);
        //Adapter dem Listview hinzufügen
        ListView listViewSetting = findViewById(R.id.listView_settings);
        listViewSetting.setAdapter(adapter);

        listViewSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItems item = ListItems.values()[position];
                switch (item) {
                    case Alarm:
                        settingAlarm();
                        break;
                    case Vibration:
                        settingVibration();
                        break;
                    case Notification:
                        settingNotification();
                        break;
                    case Animation:
                        settingAnimation(view);
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

            }
        });
    }

    private void settingAlarm() {
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
        if (mSettingsViewModel.isVibration()) {
            checkedItem = 0;
        } else {
            checkedItem = 1;
        }

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.MaterialThemeDialog);
        builder.setTitle(R.string.settings_vibration);
        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        mSettingsViewModel.setVibration(true);
                        break;
                    case 1:
                        mSettingsViewModel.setVibration(false);
                        break;
                }
                refreshWindow();
                adapter.notifyDataSetChanged();
                radioButtonDialog.dismiss();
            }
        });
        radioButtonDialog = builder.create();
        radioButtonDialog.show();
    }

    private void settingNotification() {
        final String[] items = getResources().getStringArray(R.array.settings_options);

        //Get CheckedItem
        int checkedItem;
        if (mSettingsViewModel.isNotification()) {
            checkedItem = 0;
        } else {
            checkedItem = 1;
        }

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.MaterialThemeDialog);
        builder.setTitle(R.string.settings_notification);
        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        mSettingsViewModel.setNotification(true);
                        break;
                    case 1:
                        mSettingsViewModel.setNotification(false);
                        break;
                }
                refreshWindow();
                adapter.notifyDataSetChanged();
                radioButtonDialog.dismiss();
            }
        });
        radioButtonDialog = builder.create();
        radioButtonDialog.show();
    }

    private void settingAnimation(View v) {
        final String[] items = getResources().getStringArray(R.array.settings_options);

        //Get CheckedItem
        int checkedItem;
        if (mSettingsViewModel.isAnimation()) {
            checkedItem = 0;
        } else {
            checkedItem = 1;
        }

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.MaterialThemeDialog);
        builder.setTitle(R.string.settings_animation);
        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        mSettingsViewModel.setAnimation(true);
                        break;
                    case 1:
                        mSettingsViewModel.setAnimation(false);
                        break;
                }
                refreshWindow();
                adapter.notifyDataSetChanged();
                radioButtonDialog.dismiss();
            }
        });
        radioButtonDialog = builder.create();
        radioButtonDialog.show();
    }

    private void settingTemperatureUnit() {
        final String[] items = getResources().getStringArray(R.array.settings_temperature_units);

        //Get CheckedItem
        int checkedItem;
        if (mSettingsViewModel.getTemperatureunit().equals(items[0])) {
            checkedItem = 0;
        } else {
            checkedItem = 1;
        }

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.MaterialThemeDialog);
        builder.setTitle(R.string.settings_temperature_unit);
        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                mSettingsViewModel.setTemperatureunit(items[item]);
                refreshWindow();
                adapter.notifyDataSetChanged();
                radioButtonDialog.dismiss();
            }
        });
        radioButtonDialog = builder.create();
        radioButtonDialog.show();
    }

    private void settingHints() {
        ViewGroup parent = findViewById(R.id.settings_parent);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayoutDialogProblem = inflater.inflate(R.layout.dialogsettingshints, parent, false);
        final CheckBox checkBoxRating = alertLayoutDialogProblem.findViewById(R.id.checkboxDialogSettingsRating);
        if (mSettingsViewModel.isMainratealert()) {
            checkBoxRating.setChecked(true);
        }
        final CheckBox checkBoxProblems = alertLayoutDialogProblem.findViewById(R.id.checkboxDialogSettingsProblems);
        if (mSettingsViewModel.isMainproblemalert()) {
            checkBoxProblems.setChecked(true);
        }
        final CheckBox checkBoxDescription = alertLayoutDialogProblem.findViewById(R.id.checkboxDialogSettingsDescription);
        if (mSettingsViewModel.isShowteaalert()) {
            checkBoxDescription.setChecked(true);
        }

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertLayoutDialogProblem);
        adb.setTitle(R.string.settings_show_hints_header);
        adb.setPositiveButton(R.string.settings_show_hints_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (checkBoxRating.isChecked()) {
                    mSettingsViewModel.setMainratealert(true);
                }else {
                    mSettingsViewModel.setMainratealert(false);
                }
                if (checkBoxProblems.isChecked()) {
                    mSettingsViewModel.setMainproblemalert(true);
                }else {
                    mSettingsViewModel.setMainproblemalert(false);
                }
                if (checkBoxDescription.isChecked()) {
                    mSettingsViewModel.setShowteaalert(true);
                }else {
                    mSettingsViewModel.setShowteaalert(true);
                }
            }
        });
        adb.setNegativeButton(R.string.settings_show_hints_cancle, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.show();

    }

    private void settingFactorySettings(View v) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Einstellungen zurücksetzen und Tees löschen
                        mSettingsViewModel.setDefaultSettings();
                        mSettingsViewModel.deleteAllTeas();
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

        ListRowItem itemSound = new ListRowItem(getResources().getString(R.string.settings_alarm), mSettingsViewModel.getMusicname());
        settingList.add(itemSound);

        //Decision for Animation, Vibration and Notification
        String[] itemsOnOff = getResources().getStringArray(R.array.settings_options);

        //Get Option for the Vibration
        int vibrationOption;
        if (mSettingsViewModel.isVibration()) {
            vibrationOption = 0;
        } else {
            vibrationOption = 1;
        }
        ListRowItem itemVibration = new ListRowItem(getResources().getString(R.string.settings_vibration), itemsOnOff[vibrationOption]);
        settingList.add(itemVibration);

        //Get Option for the Notification
        int notificationOption = -1;
        if (mSettingsViewModel.isNotification()) {
            notificationOption = 0;
        } else {
            notificationOption = 1;
        }
        ListRowItem itemNotification = new ListRowItem(getResources().getString(R.string.settings_notification), itemsOnOff[notificationOption]);
        settingList.add(itemNotification);

        //Get Option for the Animation
        int animationOption = -1;
        if (mSettingsViewModel.isAnimation()) {
            animationOption = 0;
        } else {
            animationOption = 1;
        }
        ListRowItem itemAnimation = new ListRowItem(getResources().getString(R.string.settings_animation), itemsOnOff[animationOption]);
        settingList.add(itemAnimation);

        //TemperatureUnit
        ListRowItem itemTemperatureUnit = new ListRowItem(getResources().getString(R.string.settings_temperature_unit), mSettingsViewModel.getTemperatureunit());
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
                mSettingsViewModel.setMusicchoice(uri.toString());
                mSettingsViewModel.setMusicname(name);
            } else {
                mSettingsViewModel.setMusicchoice(null);
                mSettingsViewModel.setMusicname("-");
            }
            refreshWindow();
            adapter.notifyDataSetChanged();
        }
    }
}
