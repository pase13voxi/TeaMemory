package coolpharaoh.tee.speicher.tea.timer.views.overview;

import static android.os.Build.VERSION_CODES.Q;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.TeaMemory;
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk;
import coolpharaoh.tee.speicher.tea.timer.views.description.UpdateDescription;
import coolpharaoh.tee.speicher.tea.timer.views.more.More;
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.NewTea;
import coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view.RecyclerItemOverview;
import coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view.RecyclerItemsHeaderStrategy;
import coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view.RecyclerItemsHeaderStrategyFactory;
import coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view.RecyclerViewAdapterOverview;
import coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view.StickyHeaderItemDecoration;
import coolpharaoh.tee.speicher.tea.timer.views.settings.Settings;
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea;
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController;
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageControllerFactory;

// This class has 9 Parent because of AppCompatActivity
@SuppressWarnings("java:S110")
public class Overview extends AppCompatActivity implements RecyclerViewAdapterOverview.OnClickListener {
    private final List<RecyclerItemOverview> teaListData = new ArrayList<>();

    private OverviewViewModel overviewViewModel;
    private RecyclerViewAdapterOverview teaListAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        defineToolbarAsActionbar();

        overviewViewModel = new OverviewViewModel(getApplication());

        initializeTeaList();
        initializeNewTeaButton();
        showUpdateDialogOnStart();
    }

    private void defineToolbarAsActionbar() {
        final Toolbar toolbar = findViewById(R.id.tool_bar);
        final TextView toolbarCustomTitle = findViewById(R.id.tool_bar_title);
        toolbarCustomTitle.setPadding(40, 0, 0, 0);
        toolbarCustomTitle.setText(R.string.app_name);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    private void initializeTeaList() {
        final RecyclerView recyclerViewTeaList = findViewById(R.id.recycler_view_overview_tea_list);
        recyclerViewTeaList.setLayoutManager(new LinearLayoutManager(this));
        teaListAdapter = new RecyclerViewAdapterOverview(teaListData, this);
        recyclerViewTeaList.setAdapter(teaListAdapter);

        bindTeaListWithTeaAdapterAndObserve(recyclerViewTeaList);
    }

    private void bindTeaListWithTeaAdapterAndObserve(final RecyclerView teaList) {
        overviewViewModel.getTeas().observe(this, teas -> {
            final RecyclerItemsHeaderStrategy recyclerItemsHeader = RecyclerItemsHeaderStrategyFactory.getStrategy(overviewViewModel.getSortWithHeader(), getApplication());
            teaListData.clear();
            teaListData.addAll(recyclerItemsHeader.generateFrom(teas));

            teaListAdapter.notifyDataSetChanged();

            updateStickyHeaderOnRecyclerView(teaList, teaListAdapter);
        });
    }

    private void updateStickyHeaderOnRecyclerView(final RecyclerView teaList, final RecyclerViewAdapterOverview adapter) {
        if (overviewViewModel.getSortWithHeader() != -1) {
            teaList.addItemDecoration(new StickyHeaderItemDecoration(adapter));
        }

        if (teaList.getItemDecorationCount() > 1) {
            teaList.removeItemDecorationAt(1);
        }

        if (teaList.getItemDecorationCount() > 0 && overviewViewModel.getSortWithHeader() == -1) {
            teaList.removeItemDecorationAt(0);
        }
    }

    @Override
    public void onRecyclerItemClick(final long teaId) {
        navigateToShowTea(teaId);
    }

    private void navigateToShowTea(final long teaId) {
        final Intent showTeaScreen = new Intent(Overview.this, ShowTea.class);
        showTeaScreen.putExtra("teaId", teaId);
        startActivity(showTeaScreen);
    }

    @Override
    public void onRecyclerItemLongClick(final View itemView, final long teaId) {
        final PopupMenu popup = new PopupMenu(getApplication(), itemView, Gravity.END);
        popup.inflate(R.menu.menu_overview_tea_list);

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_overview_tea_list_in_stock) {
                dialogUpdateTeaInStock(teaId);
                return true;
            } else if (item.getItemId() == R.id.action_overview_tea_list_edit) {
                navigateToNewOrEditTea(teaId);
                return true;
            } else if (item.getItemId() == R.id.action_overview_tea_list_delete) {
                removeTeaByTeaId(teaId);
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void dialogUpdateTeaInStock(final long teaId) {
        final String[] items = getResources().getStringArray(R.array.overview_dialog_tea_in_stock_options);

        final int checkedItem = overviewViewModel.isTeaInStock(teaId) ? 0 : 1;

        new AlertDialog.Builder(this, R.style.dialog_theme)
                .setTitle(R.string.overview_dialog_tea_in_stock)
                .setSingleChoiceItems(items, checkedItem, (DialogInterface dialog, int item) -> updateTeaInStock(teaId, dialog, item))
                .setNegativeButton(R.string.overview_dialog_tea_in_stock_negative, null)
                .show();
    }

    private void updateTeaInStock(final long teaId, final DialogInterface dialog, final int item) {
        overviewViewModel.updateInStockOfTea(teaId, item == 0);
        dialog.dismiss();
    }

    private void removeTeaByTeaId(final long teaId) {
        overviewViewModel.deleteTea((int) teaId);

        if (CurrentSdk.getSdkVersion() >= Q) {
            final ImageController imageController = ImageControllerFactory.getImageController(this);
            imageController.removeImageByTeaId(teaId);
        }
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

    private void showUpdateDialogOnStart() {
        final TeaMemory application = (TeaMemory) getApplication();
        if (application != null && !application.isOverviewDialogsShown()) {
            application.setOverviewDialogsShown(true);

            showUpdateDialog();
        }
    }

    private void showUpdateDialog() {
        if (CurrentSdk.getSdkVersion() >= Q) {
            if (overviewViewModel.isOverviewUpdateAlert()) {
                new AlertDialog.Builder(this, R.style.dialog_theme)
                        .setTitle(R.string.overview_dialog_update_header)
                        .setMessage(R.string.overview_dialog_update_description)
                        .setPositiveButton(R.string.overview_dialog_update_positive, (dialog, which) -> navigateToUpdateWindow())
                        .setNeutralButton(R.string.overview_dialog_update_neutral, null)
                        .setNegativeButton(R.string.overview_dialog_update_negative, (dialog, which) -> overviewViewModel.setOverviewUpdateAlert(false))
                        .show();
            }
        }
    }

    private void navigateToUpdateWindow() {
        overviewViewModel.setOverviewUpdateAlert(false);
        final Intent intent = new Intent(Overview.this, UpdateDescription.class);
        startActivity(intent);
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

        if (id == R.id.action_overview_random_choice) {
            dialogRandomChoice();
        } else if (id == R.id.action_overview_settings) {
            navigateToSettings();
        } else if (id == R.id.action_overview_more) {
            navigateToMore();
        } else if (id == R.id.action_overview_sort) {
            dialogSortOption();
        }

        return super.onOptionsItemSelected(item);
    }

    private void dialogRandomChoice() {
        final RandomChoiceDialog randomChoiceDialog = new RandomChoiceDialog(overviewViewModel,
                ImageControllerFactory.getImageController(this));
        randomChoiceDialog.show(getSupportFragmentManager(), RandomChoiceDialog.TAG);
    }

    private void navigateToSettings() {
        final Intent settingScreen = new Intent(Overview.this, Settings.class);
        startActivity(settingScreen);
    }

    private void navigateToMore() {
        final Intent moreScreen = new Intent(Overview.this, More.class);
        startActivity(moreScreen);
    }

    private void dialogSortOption() {
        final RecyclerViewConfigurationDialog recyclerViewConfigurationDialog = new RecyclerViewConfigurationDialog(overviewViewModel);
        recyclerViewConfigurationDialog.show(getSupportFragmentManager(), RecyclerViewConfigurationDialog.TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        overviewViewModel.refreshTeas();
    }
}
