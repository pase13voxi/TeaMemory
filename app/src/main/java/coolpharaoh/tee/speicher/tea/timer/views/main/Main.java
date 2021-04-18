package coolpharaoh.tee.speicher.tea.timer.views.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.TeaMemory;
import coolpharaoh.tee.speicher.tea.timer.views.about.About;
import coolpharaoh.tee.speicher.tea.timer.views.description.UpdateDescription;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.ExportImport;
import coolpharaoh.tee.speicher.tea.timer.views.newtea.NewTea;
import coolpharaoh.tee.speicher.tea.timer.views.settings.Settings;
import coolpharaoh.tee.speicher.tea.timer.views.showtea.ShowTea;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class Main extends AppCompatActivity implements TeaListRecyclerViewAdapter.OnClickListener {
    private MainViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defineToolbarAsActionbar();

        mainActivityViewModel = new MainViewModel(getApplication());

        initializeTeaList();
        initializeNewTeaButton();
        showDialogsOnStart();
    }

    private void defineToolbarAsActionbar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView toolbarCustomTitle = findViewById(R.id.tool_bar_title);
        toolbarCustomTitle.setPadding(40, 0, 0, 0);
        toolbarCustomTitle.setText(R.string.app_name);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void dialogSortOption() {
        final String[] items = getResources().getStringArray(R.array.main_sort_options);
        int checkedItem = mainActivityViewModel.getSort();

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog_theme);
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
        final RecyclerView recyclerViewTeaList = findViewById(R.id.recycler_view_tea_list);
        recyclerViewTeaList.addItemDecoration(new DividerItemDecoration(recyclerViewTeaList.getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewTeaList.setLayoutManager(new LinearLayoutManager(this));

        bindTeaListWithTeaAdapterAndObserve(recyclerViewTeaList);
    }

    private void bindTeaListWithTeaAdapterAndObserve(final RecyclerView teaList) {
        mainActivityViewModel.getTeas().observe(this, teas -> {
            final TeaListRecyclerViewAdapter teaListRecyclerViewAdapter = new TeaListRecyclerViewAdapter(teas, this, getApplication());
            teaList.setAdapter(teaListRecyclerViewAdapter);
        });
    }

    @Override
    public void onRecyclerItemClick(final int position) {
        navigateToShowTea(position);
    }

    private void navigateToShowTea(final int position) {
        final Intent showteaScreen = new Intent(Main.this, ShowTea.class);
        showteaScreen.putExtra("teaId", mainActivityViewModel.getTeaByPosition(position).getId());
        startActivity(showteaScreen);
    }

    @Override
    public void onRecyclerItemLongClick(final View itemView, final int position) {
        final PopupMenu popup = new PopupMenu(getApplication(), itemView, Gravity.END);
        popup.inflate(R.menu.menu_main_tea_list);

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_main_tea_list_edit) {
                navigateToNewOrEditTea(mainActivityViewModel.getTeaByPosition(position).getId());
                return true;
            } else if (item.getItemId() == R.id.action_main_tea_list_delete) {
                mainActivityViewModel.deleteTea(position);
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void initializeNewTeaButton() {
        final FloatingActionButton newTea = findViewById(R.id.newtea);
        newTea.setOnClickListener(v -> navigateToNewOrEditTea(null));
    }

    private void navigateToNewOrEditTea(final Long teaId) {
        final Intent newTeaScreen = new Intent(Main.this, NewTea.class);
        if (teaId != null) {
            newTeaScreen.putExtra("teaId", teaId);
        }
        startActivity(newTeaScreen);
    }

    private void showDialogsOnStart() {
        final TeaMemory application = (TeaMemory) getApplication();
        if (application != null && !application.isMainDialogsShown()) {
            application.setMainDialogsShown(true);

            showUpdateDialog();
            showRatingDialogOrIncrementRateCounter();
        }
    }

    private void showUpdateDialog() {
        if (mainActivityViewModel.isMainUpdateAlert()) {
            new AlertDialog.Builder(this, R.style.dialog_theme)
                    .setTitle(R.string.main_dialog_update_header)
                    .setMessage(R.string.main_dialog_update_description)
                    .setPositiveButton(R.string.main_dialog_update_positive, (dialog, which) -> navigateToUpdateWindow())
                    .setNeutralButton(R.string.main_dialog_update_neutral, null)
                    .setNegativeButton(R.string.main_dialog_update_negative, (dialog, which) -> mainActivityViewModel.setMainUpdateAlert(false))
                    .show();
        }
    }

    private void navigateToUpdateWindow() {
        mainActivityViewModel.setMainUpdateAlert(false);
        final Intent intent = new Intent(Main.this, UpdateDescription.class);
        startActivity(intent);
    }

    private void showRatingDialogOrIncrementRateCounter() {
        if (mainActivityViewModel.isMainRateAlert() && mainActivityViewModel.getMainRatecounter() >= 20) {
            showRatingDialog();
        } else if (mainActivityViewModel.getMainRatecounter() < 20) {
            mainActivityViewModel.incrementMainRatecounter();
        }
    }

    private void showRatingDialog() {
        mainActivityViewModel.resetMainRatecounter();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog_theme);
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
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchView.configureSearchView(menu, mainActivityViewModel);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.action_main_settings) {
            navigateToSettings();
        } else if (id == R.id.action_main_export_import) {
            navigateToExportImport();
        } else if (id == R.id.action_main_about) {
            navigateToAbout();
        } else if (id == R.id.action_main_sort) {
            dialogSortOption();
        }

        return super.onOptionsItemSelected(item);
    }

    private void navigateToSettings() {
        final Intent settingScreen = new Intent(Main.this, Settings.class);
        startActivity(settingScreen);
    }

    private void navigateToExportImport() {
        final Intent exportImportScreen = new Intent(Main.this, ExportImport.class);
        startActivity(exportImportScreen);
    }

    private void navigateToAbout() {
        final Intent aboutScreen = new Intent(Main.this, About.class);
        startActivity(aboutScreen);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivityViewModel.refreshTeas();
    }
}
