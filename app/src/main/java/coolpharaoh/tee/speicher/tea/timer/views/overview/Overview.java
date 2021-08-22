package coolpharaoh.tee.speicher.tea.timer.views.overview;

import android.app.AlertDialog;
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

import java.util.List;
import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.TeaMemory;
import coolpharaoh.tee.speicher.tea.timer.views.about.About;
import coolpharaoh.tee.speicher.tea.timer.views.description.UpdateDescription;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.ExportImport;
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.NewTea;
import coolpharaoh.tee.speicher.tea.timer.views.settings.Settings;
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class Overview extends AppCompatActivity implements RecyclerViewAdapterOverview.OnClickListener {
    private OverviewViewModel overviewViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        defineToolbarAsActionbar();

        overviewViewModel = new OverviewViewModel(getApplication());

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
        new SortPickerDialog(overviewViewModel)
                .show(getSupportFragmentManager(), SortPickerDialog.TAG);
    }

    private void initializeTeaList() {
        final RecyclerView recyclerViewTeaList = findViewById(R.id.recycler_view_overview_tea_list);
        recyclerViewTeaList.addItemDecoration(new DividerItemDecoration(recyclerViewTeaList.getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewTeaList.setLayoutManager(new LinearLayoutManager(this));

        bindTeaListWithTeaAdapterAndObserve(recyclerViewTeaList);
    }

    private void bindTeaListWithTeaAdapterAndObserve(final RecyclerView teaList) {
        overviewViewModel.getTeas().observe(this, teas -> {
            final List<RecyclerItemOverview> recyclerItems = RecyclerItemOverview.generateFrom(overviewViewModel.getSortWithHeader(), teas, getApplication());

            final RecyclerViewAdapterOverview teaListRecyclerViewAdapter = new RecyclerViewAdapterOverview(recyclerItems, this);
            teaList.setAdapter(teaListRecyclerViewAdapter);
        });
    }

    @Override
    public void onRecyclerItemClick(final long teaId) {
        navigateToShowTea(teaId);
    }

    private void navigateToShowTea(final long teaId) {
        final Intent showteaScreen = new Intent(Overview.this, ShowTea.class);
        showteaScreen.putExtra("teaId", teaId);
        startActivity(showteaScreen);
    }

    @Override
    public void onRecyclerItemLongClick(final View itemView, final long teaId) {
        final PopupMenu popup = new PopupMenu(getApplication(), itemView, Gravity.END);
        popup.inflate(R.menu.menu_overview_tea_list);

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_overview_tea_list_edit) {
                navigateToNewOrEditTea(teaId);
                return true;
            } else if (item.getItemId() == R.id.action_overview_tea_list_delete) {
                overviewViewModel.deleteTea((int) teaId);
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void initializeNewTeaButton() {
        final FloatingActionButton newTea = findViewById(R.id.floating_button_overview_new_tea);
        newTea.setOnClickListener(v -> navigateToNewOrEditTea(null));
    }

    private void navigateToNewOrEditTea(final Long teaId) {
        final Intent newTeaScreen = new Intent(Overview.this, NewTea.class);
        if (teaId != null) {
            newTeaScreen.putExtra("teaId", teaId);
        }
        startActivity(newTeaScreen);
    }

    private void showDialogsOnStart() {
        final TeaMemory application = (TeaMemory) getApplication();
        if (application != null && !application.isOverviewDialogsShown()) {
            application.setOverviewDialogsShown(true);

            showUpdateDialog();
            showRatingDialogOrIncrementRateCounter();
        }
    }

    private void showUpdateDialog() {
        if (overviewViewModel.isMainUpdateAlert()) {
            new AlertDialog.Builder(this, R.style.dialog_theme)
                    .setTitle(R.string.overview_dialog_update_header)
                    .setMessage(R.string.overview_dialog_update_description)
                    .setPositiveButton(R.string.overview_dialog_update_positive, (dialog, which) -> navigateToUpdateWindow())
                    .setNeutralButton(R.string.overview_dialog_update_neutral, null)
                    .setNegativeButton(R.string.overview_dialog_update_negative, (dialog, which) -> overviewViewModel.setMainUpdateAlert(false))
                    .show();
        }
    }

    private void navigateToUpdateWindow() {
        overviewViewModel.setMainUpdateAlert(false);
        final Intent intent = new Intent(Overview.this, UpdateDescription.class);
        startActivity(intent);
    }

    private void showRatingDialogOrIncrementRateCounter() {
        if (overviewViewModel.isMainRateAlert() && overviewViewModel.getMainRatecounter() >= 20) {
            showRatingDialog();
        } else if (overviewViewModel.getMainRatecounter() < 20) {
            overviewViewModel.incrementMainRatecounter();
        }
    }

    private void showRatingDialog() {
        overviewViewModel.resetMainRatecounter();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog_theme);
        builder.setTitle(R.string.overview_dialog_rating_header);
        builder.setMessage(R.string.overview_dialog_rating_description);
        builder.setPositiveButton(R.string.overview_dialog_rating_positive, (dialog, which) -> navigateToStoreForRating());
        builder.setNeutralButton(R.string.overview_dialog_rating_neutral, null);
        builder.setNegativeButton(R.string.overview_dialog_rating_negative, (dialogInterface, i) -> overviewViewModel.setMainRateAlert(false));
        builder.show();
    }

    private void navigateToStoreForRating() {
        overviewViewModel.setMainRateAlert(false);

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
        inflater.inflate(R.menu.menu_overview, menu);

        SearchView.configureSearchView(menu, overviewViewModel);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.action_overview_settings) {
            navigateToSettings();
        } else if (id == R.id.action_overview_export_import) {
            navigateToExportImport();
        } else if (id == R.id.action_overview_about) {
            navigateToAbout();
        } else if (id == R.id.action_overview_sort) {
            dialogSortOption();
        }

        return super.onOptionsItemSelected(item);
    }

    private void navigateToSettings() {
        final Intent settingScreen = new Intent(Overview.this, Settings.class);
        startActivity(settingScreen);
    }

    private void navigateToExportImport() {
        final Intent exportImportScreen = new Intent(Overview.this, ExportImport.class);
        startActivity(exportImportScreen);
    }

    private void navigateToAbout() {
        final Intent aboutScreen = new Intent(Overview.this, About.class);
        startActivity(aboutScreen);
    }

    @Override
    public void onResume() {
        super.onResume();
        overviewViewModel.refreshTeas();
    }
}
