package coolpharaoh.tee.speicher.tea.timer.views;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tooltip.Tooltip;

import java.util.Calendar;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.InfusionDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.NoteDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.datastructure.ActualSetting;
import coolpharaoh.tee.speicher.tea.timer.datastructure.N2Tea;
import coolpharaoh.tee.speicher.tea.timer.datastructure.TeaCollection;
import coolpharaoh.tee.speicher.tea.timer.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.entities.Tea;
import coolpharaoh.tee.speicher.tea.timer.listadapter.TeaAdapter;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.MainActivityViewModel;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.ColorConversation;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.LanguageConversation;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.TemperatureConversation;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {

    public static ActualSetting settings;
    private MainActivityViewModel mMainActivityViewModel;
    static private boolean startApplication = true;

    private ListView tealist;
    private TeaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermissions();

        //Toolbar als ActionBar festlegen
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.app_name);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }

        //hole ListView
        tealist = findViewById(R.id.listViewTealist);

        TeaMemoryDatabase database = Room.databaseBuilder(this, TeaMemoryDatabase.class, "teamemory")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        TeaDAO teaDAO = database.getTeaDAO();
        InfusionDAO infusionDAO = database.getInfusionDAO();
        NoteDAO noteDAO = database.getNoteDAO();
        CounterDAO counterDAO = database.getCounterDAO();
        ActualSettingsDAO actualSettingsDAO = database.getActualSettingsDAO();

        if (actualSettingsDAO.getCountItems() == 0) {


            //temporärer Check
            boolean teasExist = true;
            //Liste aller Tees
            TeaCollection teaItems = new TeaCollection();
            if (!teaItems.loadCollection(getApplicationContext())) {
                // TODO Auto-generated method stub
                //kann später entfernt werden
                if (!teaItems.loadOld2Collection(getApplicationContext())) {
                    if (!teaItems.loadOldCollection(getApplicationContext())) {
                        teasExist = false;

                        Tea ntea1 = new Tea("Earl Grey", getApplicationContext().getResources().getStringArray(R.array.variety_codes)[0], 5, "Ts", ColorConversation.getVarietyColor(0, getApplicationContext()), 0, Calendar.getInstance().getTime());
                        teaDAO.insert(ntea1);
                        long teaId1 = teaDAO.getTeas().get(0).getId();
                        Infusion ninfusion1 = new Infusion(teaId1, 0, "3:30", TemperatureConversation.celsiusToCoolDownTime(100), 100, TemperatureConversation.celsiusToFahrenheit(100));
                        infusionDAO.insert(ninfusion1);
                        Counter ncounter1 = new Counter(teaId1, 0, 0, 0, 0, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
                        counterDAO.insert(ncounter1);
                        Note nnote1 = new Note(teaId1, 1, null, "");
                        noteDAO.insert(nnote1);

                        Tea ntea2 = new Tea("Pai Mu Tan", getApplicationContext().getResources().getStringArray(R.array.variety_codes)[3], 4, "Ts", ColorConversation.getVarietyColor(3, getApplicationContext()), 0, Calendar.getInstance().getTime());
                        teaDAO.insert(ntea2);
                        long teaId2 = teaDAO.getTeas().get(1).getId();
                        Infusion ninfusion2 = new Infusion(teaId2, 0, "2", TemperatureConversation.celsiusToCoolDownTime(85), 85, TemperatureConversation.celsiusToFahrenheit(85));
                        infusionDAO.insert(ninfusion2);
                        Counter ncounter2 = new Counter(teaId2, 0, 0, 0, 0, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
                        counterDAO.insert(ncounter2);
                        Note nnote2 = new Note(teaId2, 1, null, "");
                        noteDAO.insert(nnote2);

                        Tea ntea3 = new Tea("Sencha", getApplicationContext().getResources().getStringArray(R.array.variety_codes)[1], 4, "Ts", ColorConversation.getVarietyColor(1, getApplicationContext()), 0, Calendar.getInstance().getTime());
                        teaDAO.insert(ntea3);
                        long teaId3 = teaDAO.getTeas().get(2).getId();
                        Infusion ninfusion3 = new Infusion(teaId3, 0, "1:30", TemperatureConversation.celsiusToCoolDownTime(80), 80, TemperatureConversation.celsiusToFahrenheit(80));
                        infusionDAO.insert(ninfusion3);
                        Counter ncounter3 = new Counter(teaId3, 0, 0, 0, 0, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
                        counterDAO.insert(ncounter3);
                        Note nnote3 = new Note(teaId3, 1, null, "");
                        noteDAO.insert(nnote3);
                    } else {
                        teaItems.convertCollectionToNew();
                        teaItems.saveCollection(getApplicationContext());
                    }
                } else {
                    teaItems.convertCollection2ToNew();
                    teaItems.saveCollection(getApplicationContext());
                }
            }

            //wenn tees existieren dann konvertieren
            if (teasExist) {
                int o = 0;
                //Tee in Datenbank schreiben
                for (N2Tea otea : teaItems.getTeaItems()) {
                    Tea ntea = new Tea(otea.getName(), LanguageConversation.convertVarietyToCode(otea.getSortOfTea().getType(), getApplicationContext()), otea.getAmount().getValue(), otea.getAmount().getUnit(), otea.getColoring().getColor(), 0, otea.getDate());
                    teaDAO.insert(ntea);
                    long teaId = teaDAO.getTeas().get(o++).getId();

                    for (int i = 0; i < otea.getTime().size(); i++) {
                        Infusion infusion = new Infusion();
                        infusion.setTeaId(teaId);
                        infusion.setInfusionindex(i);
                        if (!otea.getTime().get(i).getTime().equals("-")) {
                            infusion.setTime(otea.getTime().get(i).getTime());
                        }
                        if (!otea.getCoolDownTime().get(i).getTime().equals("-")) {
                            infusion.setCooldowntime(otea.getCoolDownTime().get(i).getTime());
                        }
                        infusion.setTemperaturecelsius(otea.getTemperature().get(i).getCelsius());
                        infusion.setTemperaturefahrenheit(otea.getTemperature().get(i).getFahrenheit());

                        infusionDAO.insert(infusion);
                    }

                    Counter ncounter = new Counter(teaId, otea.getCounter().getDay(), otea.getCounter().getWeek(), otea.getCounter().getMonth(), otea.getCounter().getOverall(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
                    counterDAO.insert(ncounter);


                    Note nnote = new Note(teaId, 1, null, otea.getNote());
                    noteDAO.insert(nnote);

                }
            }

            boolean settingsExist = true;
            //Settings holen
            settings = new ActualSetting();
            if (!settings.loadSettings(getApplicationContext())) {
                if (!settings.loadOldSettings(getApplicationContext())) {
                    settingsExist = false;

                    ActualSettings actualSettings = new ActualSettings();
                    actualSettings.setMusicchoice("content://settings/system/ringtone");
                    actualSettings.setMusicname("Default");
                    actualSettings.setVibration(false);
                    actualSettings.setNotification(true);
                    actualSettings.setAnimation(true);
                    actualSettings.setTemperatureunit("Celsius");
                    actualSettings.setShowteaalert(true);
                    actualSettings.setMainproblemalert(true);
                    actualSettings.setMainratealert(true);
                    actualSettings.setMainratecounter(0);
                    actualSettings.setSort(0);
                    actualSettingsDAO.insert(actualSettings);
                }
            }

            if (settingsExist) {
                //Settings in Datenbank schreiben
                ActualSettings actualSettings = new ActualSettings();
                actualSettings.setMusicchoice(settings.getMusicChoice());
                actualSettings.setMusicname(settings.getMusicName());
                actualSettings.setVibration(settings.isVibration());
                actualSettings.setNotification(settings.isNotification());
                actualSettings.setAnimation(settings.isAnimation());
                actualSettings.setTemperatureunit(settings.getTemperatureUnit());
                actualSettings.setShowteaalert(settings.isShowteaAlert());
                actualSettings.setMainproblemalert(settings.isMainProblemAlert());
                actualSettings.setMainratealert(settings.isMainRateAlert());
                actualSettings.setMainratecounter(settings.getMainRatecounter());
                actualSettings.setSort(settings.getSort());
                actualSettingsDAO.insert(actualSettings);

            }
        }

        mMainActivityViewModel = new MainActivityViewModel(getApplicationContext());

        //Setzte Spinner Groß
        Spinner spinnerSort = findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> spinnerVarietyAdapter = ArrayAdapter.createFromResource(
                this, R.array.main_sort_menu, R.layout.spinner_item_sort);

        spinnerVarietyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_sort);
        spinnerSort.setAdapter(spinnerVarietyAdapter);

        //setzte spinner
        spinnerSort.setSelection(mMainActivityViewModel.getSort());

        //sortierung hat sich verändert
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mMainActivityViewModel.setSort(0);
                        break;
                    case 1:
                        mMainActivityViewModel.setSort(1);
                        break;
                    case 2:
                        mMainActivityViewModel.setSort(2);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Liste mit Adapter verknüpfen
        mMainActivityViewModel.getTeas().observe(this, mTeas -> {
            adapter = new TeaAdapter(MainActivity.this, mTeas);
            //Adapter dem Listview hinzufügen
            tealist.setAdapter(adapter);
        });

        //Menu wird hinzugefügt (Löschen, Ändern)
        registerForContextMenu(tealist);

        tealist.setOnItemClickListener((parent, view, position, id) -> {
            //Neues Intent anlegen
            Intent showteaScreen = new Intent(MainActivity.this, ShowTea.class);

            showteaScreen.putExtra("teaId", mMainActivityViewModel.getTeaByPosition(position).getId());
            // Intent starten und zur zweiten Activity wechseln
            startActivity(showteaScreen);
        });

        //Button NewTea + Aktion
        FloatingActionButton newTea = findViewById(R.id.newtea);
        newTea.setOnClickListener(v -> {
            //Neues Intent anlegen
            Intent newteaScreen = new Intent(MainActivity.this, NewTea.class);
            // Intent starten und zur zweiten Activity wechseln
            startActivity(newteaScreen);
        });
        newTea.setOnLongClickListener(this);

        //Show theses Hints only on start of the application
        if (startApplication) {
            startApplication = false;
            //show dialog problem
            if (mMainActivityViewModel.isMainProblemAlert()) {
                dialogMainProblem();
            }
            //show dialog rating
            if (mMainActivityViewModel.isMainRateAlert() && mMainActivityViewModel.getMainRatecounter() >= 20) {
                mMainActivityViewModel.resetMainRatecounter();
                dialogMainRating();
            } else {
                mMainActivityViewModel.incrementMainRatecounter();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //Neues Intent anlegen
            Intent settingScreen = new Intent(MainActivity.this, Settings.class);
            // Intent starten und zur zweiten Activity wechseln
            startActivity(settingScreen);
        } else if (id == R.id.action_about) {
            //Neues Intent anlegen
            Intent aboutScreen = new Intent(MainActivity.this, About.class);
            // Intent starten und zur zweiten Activity wechseln
            startActivity(aboutScreen);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listViewTealist) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(mMainActivityViewModel.getTeaByPosition(info.position).getName());

            //Übersetzung Englisch Deutsch
            String[] menuItems = getResources().getStringArray(R.array.itemMenu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.itemMenu);
        String menuItemName = menuItems[menuItemIndex];
        String editOption = menuItems[0], deleteOption = menuItems[1];

        if (menuItemName.equals(editOption)) {
            //Neues Intent anlegen
            Intent newteaScreen = new Intent(MainActivity.this, NewTea.class);
            newteaScreen.putExtra("teaId", mMainActivityViewModel.getTeaByPosition(info.position).getId());


            // Intent starten und zur zweiten Activity wechseln
            startActivity(newteaScreen);
        } else if (menuItemName.equals(deleteOption)) {
            int position = info.position;
            mMainActivityViewModel.deleteTea(position);
        }
        return true;
    }

    private void askPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {

            // If request is cancelled, the result arrays are empty.
            if (!(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                Toast.makeText(MainActivity.this, "Permission denied to read and write your External storage", Toast.LENGTH_SHORT).show();
            }
        }

        // other 'case' lines to check for other
        // permissions this app might request
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.newtea) {
            showTooltip(view, getResources().getString(R.string.main_tooltip_newtea));
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainActivityViewModel.refreshTeas();
    }

    private void showTooltip(View v, String text) {
        new Tooltip.Builder(v)
                .setText(text)
                .setTextColor(getResources().getColor(R.color.white))
                .setGravity(Gravity.TOP)
                .setCornerRadius(8f)
                .setCancelable(true)
                .setDismissOnClick(true)
                .show();
    }

    private void dialogMainProblem() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ViewGroup parent = findViewById(R.id.main_parent);

            LayoutInflater inflater = getLayoutInflater();
            View alertLayoutDialogProblem = inflater.inflate(R.layout.dialogmainproblem, parent, false);
            final CheckBox dontshowagain = alertLayoutDialogProblem.findViewById(R.id.checkboxDialogMainProblem);

            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setView(alertLayoutDialogProblem);
            adb.setTitle(R.string.main_dialog_problem_header);
            adb.setPositiveButton(R.string.main_dialog_problem_ok, (dialog, which) -> {
                if (dontshowagain.isChecked()) {
                    mMainActivityViewModel.setMainProblemAlert(false);
                }
                Intent problemsScreen = new Intent(MainActivity.this, Problems.class);
                startActivity(problemsScreen);
            });
            adb.setNegativeButton(R.string.main_dialog_problem_cancle, (dialog, which) -> {
                if (dontshowagain.isChecked()) {
                    mMainActivityViewModel.setMainProblemAlert(false);
                }
            });
            adb.show();
        }
    }

    private void dialogMainRating() {
        ViewGroup parent = findViewById(R.id.main_parent);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayoutDialogProblem = inflater.inflate(R.layout.dialogmainrating, parent, false);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertLayoutDialogProblem);
        adb.setTitle(R.string.main_dialog_rating_header);
        adb.setPositiveButton(R.string.main_dialog_rating_positive, (dialog, which) -> {
            mMainActivityViewModel.setMainRateAlert(false);

            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });
        adb.setNeutralButton(R.string.main_dialog_rating_neutral, (dialog, which) -> {
        });
        adb.setNegativeButton(R.string.main_dialog_rating_negative, (dialogInterface, i) -> mMainActivityViewModel.setMainRateAlert(false));
        adb.show();
    }
}
