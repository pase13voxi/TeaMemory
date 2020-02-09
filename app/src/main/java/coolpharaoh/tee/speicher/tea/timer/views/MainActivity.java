package coolpharaoh.tee.speicher.tea.timer.views;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tooltip.Tooltip;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.MainActivityViewModel;
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

        //define toolbar as a actionbar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.app_name);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }

        //get listView
        tealist = findViewById(R.id.listViewTealist);

        mainActivityViewModel = new MainActivityViewModel(TeaMemoryDatabase.getDatabaseInstance(getApplicationContext()), getApplicationContext());

        //set big spinner
        Spinner spinnerSort = findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> spinnerVarietyAdapter = ArrayAdapter.createFromResource(
                this, R.array.main_sort_menu, R.layout.spinner_item_sort);

        spinnerVarietyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_sort);
        spinnerSort.setAdapter(spinnerVarietyAdapter);

        //set spinner
        spinnerSort.setSelection(mainActivityViewModel.getSort());

        //sort has changed
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

        //bind list with adapter
        mainActivityViewModel.getTeas().observe(this, mTeas -> {
            adapter = new TeaAdapter(MainActivity.this, mTeas);
            //add adapter to listview
            tealist.setAdapter(adapter);
        });

        //added menu (delete, change)
        registerForContextMenu(tealist);

        tealist.setOnItemClickListener((parent, view, position, id) -> {
            //create new intent
            Intent showteaScreen = new Intent(MainActivity.this, ShowTea.class);
            showteaScreen.putExtra("teaId", mainActivityViewModel.getTeaByPosition(position).getId());
            //start Intent and switch to the other activity
            startActivity(showteaScreen);
        });

        //Button NewTea + Aktion
        FloatingActionButton newTea = findViewById(R.id.newtea);
        newTea.setOnClickListener(v -> {
            //create new intent
            Intent newteaScreen = new Intent(MainActivity.this, NewTea.class);
            //start Intent and switch to the other activity
            startActivity(newteaScreen);
        });
        newTea.setOnLongClickListener(this);

        //Show theses Hints only on start of the application
        if (startApplication) {
            startApplication = false;

            dialogMainRating();
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
            //create new intent
            Intent settingScreen = new Intent(MainActivity.this, Settings.class);
            //start intent and switch to the other activity
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
            //create new intent
            Intent newteaScreen = new Intent(MainActivity.this, NewTea.class);
            newteaScreen.putExtra("teaId", mainActivityViewModel.getTeaByPosition(info.position).getId());

            //start intent and switch to the other activity
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

    private void dialogMainRating() {
        if (mainActivityViewModel.isMainRateAlert() && mainActivityViewModel.getMainRatecounter() >= 20) {
            mainActivityViewModel.resetMainRatecounter();

            ViewGroup parent = findViewById(R.id.main_parent);

            LayoutInflater inflater = getLayoutInflater();
            View alertLayoutDialogProblem = inflater.inflate(R.layout.dialogmainrating, parent, false);

            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setView(alertLayoutDialogProblem);
            adb.setTitle(R.string.main_dialog_rating_header);
            adb.setPositiveButton(R.string.main_dialog_rating_positive, (dialog, which) -> {
                mainActivityViewModel.setMainRateAlert(false);

                final String appPackageName = getPackageName();
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

        } else {
            mainActivityViewModel.incrementMainRatecounter();
        }
    }
}
