package coolpharaoh.tee.speicher.tea.timer.views;

import android.app.AlertDialog;
import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tooltip.Tooltip;

import java.util.Calendar;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.InfusionDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.NoteDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.MainActivityViewModel;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.ColorConversation;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.TemperatureConversation;
import coolpharaoh.tee.speicher.tea.timer.views.listadapter.TeaAdapter;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {
    private MainActivityViewModel mainActivityViewModel;
    static private boolean startApplication = true;

    private ListView tealist;
    private TeaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

            Tea ntea1 = new Tea("Earl Grey", getApplicationContext().getResources().getStringArray(R.array.variety_codes)[0], 5, "Ts", ColorConversation.getVarietyColor(0, getApplicationContext()), 0, Calendar.getInstance().getTime());
            long teaId1 = teaDAO.insert(ntea1);
            Infusion ninfusion1 = new Infusion(teaId1, 0, "3:30", TemperatureConversation.celsiusToCoolDownTime(100), 100, TemperatureConversation.celsiusToFahrenheit(100));
            infusionDAO.insert(ninfusion1);
            Counter ncounter1 = new Counter(teaId1, 0, 0, 0, 0, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
            counterDAO.insert(ncounter1);
            Note nnote1 = new Note(teaId1, 1, null, "");
            noteDAO.insert(nnote1);

            Tea ntea2 = new Tea("Pai Mu Tan", getApplicationContext().getResources().getStringArray(R.array.variety_codes)[3], 4, "Ts", ColorConversation.getVarietyColor(3, getApplicationContext()), 0, Calendar.getInstance().getTime());
            long teaId2 = teaDAO.insert(ntea2);
            Infusion ninfusion2 = new Infusion(teaId2, 0, "2", TemperatureConversation.celsiusToCoolDownTime(85), 85, TemperatureConversation.celsiusToFahrenheit(85));
            infusionDAO.insert(ninfusion2);
            Counter ncounter2 = new Counter(teaId2, 0, 0, 0, 0, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
            counterDAO.insert(ncounter2);
            Note nnote2 = new Note(teaId2, 1, null, "");
            noteDAO.insert(nnote2);

            Tea ntea3 = new Tea("Sencha", getApplicationContext().getResources().getStringArray(R.array.variety_codes)[1], 4, "Ts", ColorConversation.getVarietyColor(1, getApplicationContext()), 0, Calendar.getInstance().getTime());
            long teaId3 = teaDAO.insert(ntea3);
            Infusion ninfusion3 = new Infusion(teaId3, 0, "1:30", TemperatureConversation.celsiusToCoolDownTime(80), 80, TemperatureConversation.celsiusToFahrenheit(80));
            infusionDAO.insert(ninfusion3);
            Counter ncounter3 = new Counter(teaId3, 0, 0, 0, 0, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
            counterDAO.insert(ncounter3);
            Note nnote3 = new Note(teaId3, 1, null, "");
            noteDAO.insert(nnote3);


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

        mainActivityViewModel = new MainActivityViewModel(getApplicationContext());

        //Setzte Spinner Groß
        Spinner spinnerSort = findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> spinnerVarietyAdapter = ArrayAdapter.createFromResource(
                this, R.array.main_sort_menu, R.layout.spinner_item_sort);

        spinnerVarietyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_sort);
        spinnerSort.setAdapter(spinnerVarietyAdapter);

        //setzte spinner
        spinnerSort.setSelection(mainActivityViewModel.getSort());

        //sortierung hat sich verändert
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mainActivityViewModel.setSort(0);
                        break;
                    case 1:
                        mainActivityViewModel.setSort(1);
                        break;
                    case 2:
                        mainActivityViewModel.setSort(2);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Liste mit Adapter verknüpfen
        mainActivityViewModel.getTeas().observe(this, mTeas -> {
            adapter = new TeaAdapter(MainActivity.this, mTeas);
            //Adapter dem Listview hinzufügen
            tealist.setAdapter(adapter);
        });

        //Menu wird hinzugefügt (Löschen, Ändern)
        registerForContextMenu(tealist);

        tealist.setOnItemClickListener((parent, view, position, id) -> {
            //Neues Intent anlegen
            Intent showteaScreen = new Intent(MainActivity.this, ShowTea.class);

            showteaScreen.putExtra("teaId", mainActivityViewModel.getTeaByPosition(position).getId());
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
            if (mainActivityViewModel.isMainProblemAlert()) {
                dialogMainProblem();
            }
            //show dialog rating
            if (mainActivityViewModel.isMainRateAlert() && mainActivityViewModel.getMainRatecounter() >= 20) {
                mainActivityViewModel.resetMainRatecounter();
                dialogMainRating();
            } else {
                mainActivityViewModel.incrementMainRatecounter();
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
            //create new Intent
            Intent settingScreen = new Intent(MainActivity.this, Settings.class);
            // start Intent and switch to the other activity
            startActivity(settingScreen);
        } else if (id == R.id.action_exportImport) {
            Intent exportImportScreen = new Intent(MainActivity.this, ExportImport.class);
            startActivity(exportImportScreen);
        } else if (id == R.id.action_about) {
            Intent aboutScreen = new Intent(MainActivity.this, About.class);
            startActivity(aboutScreen);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listViewTealist) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(mainActivityViewModel.getTeaByPosition(info.position).getName());

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
            newteaScreen.putExtra("teaId", mainActivityViewModel.getTeaByPosition(info.position).getId());


            // Intent starten und zur zweiten Activity wechseln
            startActivity(newteaScreen);
        } else if (menuItemName.equals(deleteOption)) {
            int position = info.position;
            mainActivityViewModel.deleteTea(position);
        }
        return true;
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
        mainActivityViewModel.refreshTeas();
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
                    mainActivityViewModel.setMainProblemAlert(false);
                }
                Intent problemsScreen = new Intent(MainActivity.this, Problems.class);
                startActivity(problemsScreen);
            });
            adb.setNegativeButton(R.string.main_dialog_problem_cancle, (dialog, which) -> {
                if (dontshowagain.isChecked()) {
                    mainActivityViewModel.setMainProblemAlert(false);
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
            mainActivityViewModel.setMainRateAlert(false);

            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });
        adb.setNeutralButton(R.string.main_dialog_rating_neutral, (dialog, which) -> {
        });
        adb.setNegativeButton(R.string.main_dialog_rating_negative, (dialogInterface, i) -> mainActivityViewModel.setMainRateAlert(false));
        adb.show();
    }
}
