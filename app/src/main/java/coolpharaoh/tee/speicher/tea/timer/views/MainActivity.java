package coolpharaoh.tee.speicher.tea.timer.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tooltip.Tooltip;

import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.MainActivityViewModel;
import coolpharaoh.tee.speicher.tea.timer.views.listadapter.TeaAdapter;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {
    private MainActivityViewModel mainActivityViewModel;
    private static boolean startApplication = true;

    private TeaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defineToolbarAsActionbar();

        mainActivityViewModel = new MainActivityViewModel(TeaMemoryDatabase.getDatabaseInstance(getApplicationContext()), getApplicationContext());

        initializeSortButton();
        initializeTeaList();
        initializeNewTeaButton();
        showRatingDialogOnStart();
    }

    private void defineToolbarAsActionbar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.app_name);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void initializeSortButton() {
        Button buttonSort = findViewById(R.id.toolbar_sort);
        buttonSort.setOnClickListener(view -> dialogSortOption());
        buttonSort.setOnLongClickListener(this);
    }

    private void dialogSortOption() {
        final String[] items = getResources().getStringArray(R.array.main_sort_options);
        int checkedItem = mainActivityViewModel.getSort();

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialThemeDialog);
        builder.setIcon(R.drawable.sort_black);
        builder.setTitle(R.string.main_dialog_sort_title);
        builder.setSingleChoiceItems(items, checkedItem, this::changeSortOption);
        builder.setNegativeButton(R.string.main_dialog_sort_negative, null);
        builder.create().show();
    }

    private void changeSortOption(DialogInterface dialog, int item) {
        mainActivityViewModel.setSort(item);
        dialog.dismiss();
    }

    private void initializeTeaList() {
        ListView teaList = findViewById(R.id.listViewTealist);

        bindTeaListWithTeaAdapterAndObserve(teaList);

        teaList.setOnItemClickListener((parent, view, position, id) -> navigateToShowTea(position));

        registerForContextMenu(teaList);
    }

    private void bindTeaListWithTeaAdapterAndObserve(ListView tealist) {
        mainActivityViewModel.getTeas().observe(this, mTeas -> {
            adapter = new TeaAdapter(MainActivity.this, mTeas);
            //add adapter to listview
            tealist.setAdapter(adapter);
        });
    }

    private void navigateToShowTea(int position) {
        Intent showteaScreen = new Intent(MainActivity.this, ShowTea.class);
        showteaScreen.putExtra("teaId", mainActivityViewModel.getTeaByPosition(position).getId());
        startActivity(showteaScreen);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listViewTealist) {
            showEditAndDeleteMenu(menu, (AdapterView.AdapterContextMenuInfo) menuInfo);
        }
    }

    private void showEditAndDeleteMenu(ContextMenu menu, AdapterView.AdapterContextMenuInfo menuInfo) {
        menu.setHeaderTitle(mainActivityViewModel.getTeaByPosition(menuInfo.position).getName());

        String[] menuItems = getResources().getStringArray(R.array.itemMenu);
        for (int i = 0; i < menuItems.length; i++) {
            menu.add(Menu.NONE, i, i, menuItems[i]);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return editOrDeleteTea(item);
    }

    private boolean editOrDeleteTea(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.itemMenu);
        String menuItemName = menuItems[menuItemIndex];
        String editOption = menuItems[0];
        String deleteOption = menuItems[1];

        if (menuItemName.equals(editOption)) {
            navigateToNewOrEditTea(mainActivityViewModel.getTeaByPosition(info.position).getId());
        } else if (menuItemName.equals(deleteOption)) {
            mainActivityViewModel.deleteTea(info.position);
        }
        return true;
    }

    private void initializeNewTeaButton() {
        FloatingActionButton newTea = findViewById(R.id.newtea);
        newTea.setOnClickListener(v -> navigateToNewOrEditTea(null));
        newTea.setOnLongClickListener(this);
    }

    private void navigateToNewOrEditTea(Long teaId) {
        Intent newteaScreen = new Intent(MainActivity.this, NewTea.class);
        if (teaId != null) {
            newteaScreen.putExtra("teaId", teaId);
        }
        startActivity(newteaScreen);
    }

    private void showRatingDialogOnStart() {
        if (startApplication) {
            startApplication = false;
            showRatingDialogOrIncrementRateCounter();
        }
    }

    private void showRatingDialogOrIncrementRateCounter() {
        if (mainActivityViewModel.isMainRateAlert() && mainActivityViewModel.getMainRatecounter() >= 20) {
            showRatingDialog();
        } else {
            mainActivityViewModel.incrementMainRatecounter();
        }
    }

    private void showRatingDialog() {
        mainActivityViewModel.resetMainRatecounter();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.main_dialog_rating_header);
        builder.setMessage(R.string.main_dialog_rating);
        builder.setPositiveButton(R.string.main_dialog_rating_positive, (dialog, which) -> navigateToStoreForRating());
        builder.setNeutralButton(R.string.main_dialog_rating_neutral, null);
        builder.setNegativeButton(R.string.main_dialog_rating_negative, (dialogInterface, i) -> mainActivityViewModel.setMainRateAlert(false));
        builder.show();
    }

    private void navigateToStoreForRating() {
        mainActivityViewModel.setMainRateAlert(false);

        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
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
            navigateToSettings();
        } else if (id == R.id.action_exportImport) {
            navigateToExportImport();
        } else if (id == R.id.action_about) {
            navigateToAbout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void navigateToSettings() {
        Intent settingScreen = new Intent(MainActivity.this, Settings.class);
        startActivity(settingScreen);
    }

    private void navigateToExportImport() {
        Intent exportImportScreen = new Intent(MainActivity.this, ExportImport.class);
        startActivity(exportImportScreen);
    }

    private void navigateToAbout() {
        Intent aboutScreen = new Intent(MainActivity.this, About.class);
        startActivity(aboutScreen);
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.newtea) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.main_tooltip_newtea));
        }
        if (view.getId() == R.id.toolbar_sort) {
            showTooltip(view, Gravity.BOTTOM, getResources().getString(R.string.main_tooltip_sort));
        }
        return true;
    }

    private void showTooltip(View v, int gravity, String text) {
        new Tooltip.Builder(v)
                .setText(text)
                .setTextColor(getResources().getColor(R.color.white))
                .setGravity(gravity)
                .setCornerRadius(8f)
                .setCancelable(true)
                .setDismissOnClick(true)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivityViewModel.refreshTeas();
    }
}
