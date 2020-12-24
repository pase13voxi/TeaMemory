package coolpharaoh.tee.speicher.tea.timer.views.main;

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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tooltip.Tooltip;

import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.about.About;
import coolpharaoh.tee.speicher.tea.timer.views.description.Description;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.ExportImport;
import coolpharaoh.tee.speicher.tea.timer.views.newtea.NewTea;
import coolpharaoh.tee.speicher.tea.timer.views.settings.Settings;
import coolpharaoh.tee.speicher.tea.timer.views.showtea.ShowTea;

public class Main extends AppCompatActivity implements View.OnLongClickListener {
    private MainViewModel mainActivityViewModel;
    private static boolean startApplication = true;

    private TeaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defineToolbarAsActionbar();

        mainActivityViewModel = new MainViewModel(getApplication());

        initializeTeaList();
        initializeNewTeaButton();
        showUpdateInformationOnStart();
        showRatingDialogOnStart();
    }

    private void defineToolbarAsActionbar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView toolbarCustomTitle = findViewById(R.id.toolbar_title);
        toolbarCustomTitle.setPadding(40, 0, 0, 0);
        toolbarCustomTitle.setText(R.string.app_name);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
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
        mainActivityViewModel.getTeas().observe(this, teas -> {
            adapter = new TeaAdapter(Main.this, teas);
            //add adapter to listview
            tealist.setAdapter(adapter);
        });
    }

    private void navigateToShowTea(int position) {
        Intent showteaScreen = new Intent(Main.this, ShowTea.class);
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
        Intent newteaScreen = new Intent(Main.this, NewTea.class);
        if (teaId != null) {
            newteaScreen.putExtra("teaId", teaId);
        }
        startActivity(newteaScreen);
    }

    private void showUpdateInformationOnStart() {
        if (mainActivityViewModel.isMainUpdateAlert()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.main_dialog_update_header)
                    .setMessage(R.string.main_dialog_update_description)
                    .setPositiveButton(R.string.main_dialog_update_positive, (dialog, which) -> navigateToUpdateWindow())
                    .setNeutralButton(R.string.main_dialog_update_neutral, null)
                    .setNegativeButton(R.string.main_dialog_update_negative, (dialog, which) -> mainActivityViewModel.setMainUpdateAlert(false))
                    .show();
        }
    }

    private void navigateToUpdateWindow() {
        Intent intent = new Intent(Main.this, Description.class);
        startActivity(intent);
    }

    private void showRatingDialogOnStart() {
        if (startApplication) {
            //TODO Make the enclosing method "static and remove this set"
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
        builder.setMessage(R.string.main_dialog_rating_description);
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

        SearchView.configureSearchView(menu, mainActivityViewModel);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            navigateToSettings();
        } else if (id == R.id.action_exportImport) {
            navigateToExportImport();
        } else if (id == R.id.action_about) {
            navigateToAbout();
        } else if (id == R.id.action_sort) {
            dialogSortOption();
        }

        return super.onOptionsItemSelected(item);
    }

    private void navigateToSettings() {
        Intent settingScreen = new Intent(Main.this, Settings.class);
        startActivity(settingScreen);
    }

    private void navigateToExportImport() {
        Intent exportImportScreen = new Intent(Main.this, ExportImport.class);
        startActivity(exportImportScreen);
    }

    private void navigateToAbout() {
        Intent aboutScreen = new Intent(Main.this, About.class);
        startActivity(aboutScreen);
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.newtea) {
            showTooltip(view, getResources().getString(R.string.main_tooltip_newtea));
        }
        return true;
    }

    private void showTooltip(View view, String text) {
        new Tooltip.Builder(view)
                .setText(text)
                .setTextColor(getResources().getColor(R.color.white))
                .setGravity(Gravity.TOP)
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
