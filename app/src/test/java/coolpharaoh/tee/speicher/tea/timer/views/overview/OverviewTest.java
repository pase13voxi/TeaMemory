package coolpharaoh.tee.speicher.tea.timer.views.overview;

import static android.os.Looper.getMainLooper;
import static android.view.Menu.FLAG_ALWAYS_PERFORM_CLOSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SortMode.ALPHABETICAL;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SortMode.BY_VARIETY;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SortMode.LAST_USED;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SortMode.RATING;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowPopupMenu;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SortMode;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.views.about.About;
import coolpharaoh.tee.speicher.tea.timer.views.description.UpdateDescription;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.ExportImport;
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.NewTea;
import coolpharaoh.tee.speicher.tea.timer.views.settings.Settings;
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea;

@RunWith(RobolectricTestRunner.class)
public class OverviewTest {
    public static final String TEA_NAME_ACTIVITY = "ACTIVITY_";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    TeaDao teaDao;
    @Mock
    InfusionDao infusionDao;
    @Mock
    ActualSettingsDao actualSettingsDao;

    @Before
    public void setUp() {
        mockDB();
    }

    private void mockDB() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDao);
        when(teaMemoryDatabase.getInfusionDao()).thenReturn(infusionDao);
        when(teaMemoryDatabase.getActualSettingsDao()).thenReturn(actualSettingsDao);
    }

    @Test
    public void launchActivityExpectTeaList() {
        mockActualSettings();
        final List<Tea> teaList = generateTeaList(TEA_NAME_ACTIVITY);
        when(teaDao.getTeasOrderByActivity()).thenReturn(teaList);

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> checkExpectedTeas(TEA_NAME_ACTIVITY, overview));
    }

    @Test
    public void launchActivityExpectUpdateDescription() {
        mockActualSettings(LAST_USED, true);
        final List<Tea> teaList = generateTeaList(TEA_NAME_ACTIVITY);
        when(teaDao.getTeasOrderByActivity()).thenReturn(teaList);

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            final AlertDialog dialogUpdate = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(overview, dialogUpdate,
                    R.string.overview_dialog_update_header, R.string.overview_dialog_update_description);
            dialogUpdate.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            assertThat(new SharedSettings(RuntimeEnvironment.getApplication()).isOverviewUpdateAlert()).isFalse();

            final Intent expected = new Intent(overview, UpdateDescription.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getData()).isEqualTo(expected.getData());
        });
    }

    @Test
    public void launchActivityExpectUpdateDescriptionClickNegative() {
        mockActualSettings(LAST_USED, true);
        final List<Tea> teaList = generateTeaList(TEA_NAME_ACTIVITY);
        when(teaDao.getTeasOrderByActivity()).thenReturn(teaList);

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            getLatestAlertDialog().getButton(DialogInterface.BUTTON_NEGATIVE).performClick();
            shadowOf(getMainLooper()).idle();

            assertThat(new SharedSettings(RuntimeEnvironment.getApplication()).isOverviewUpdateAlert()).isFalse();
        });
    }

    @Test
    public void navigateToSettingsExpectSettingsActivity() {
        mockActualSettings();

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            overview.onOptionsItemSelected(new RoboMenuItem(R.id.action_overview_settings));

            final Intent expected = new Intent(overview, Settings.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void navigateToExportImportExpectExportImportActivity() {
        mockActualSettings();

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            overview.onOptionsItemSelected(new RoboMenuItem(R.id.action_overview_export_import));

            final Intent expected = new Intent(overview, ExportImport.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void navigateToAboutExpectAboutActivity() {
        mockActualSettings();

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            overview.onOptionsItemSelected(new RoboMenuItem(R.id.action_overview_about));

            final Intent expected = new Intent(overview, About.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void searchStringExpectSearchList() {
        mockActualSettings();
        final String teaName = "SEARCH_";
        final List<Tea> teaList = generateTeaList(teaName);
        when(teaDao.getTeasBySearchString(teaName)).thenReturn(teaList);

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            overview.onOptionsItemSelected(new RoboMenuItem(R.id.action_overview_search));

            final MenuItemImpl menuItem = ((ActionMenuItemView) overview.findViewById(R.id.action_overview_search)).getItemData();
            final androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
            searchView.setQuery(teaName, false);
            shadowOf(getMainLooper()).idle();

            checkExpectedTeas(teaName, overview);
        });
    }

    @Test
    public void changeSortModeToActivityExpectTeaList() {
        mockActualSettings(ALPHABETICAL, false);
        final List<Tea> teaList = generateTeaList(TEA_NAME_ACTIVITY);
        when(teaDao.getTeasOrderByActivity()).thenReturn(teaList);

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            overview.onOptionsItemSelected(new RoboMenuItem(R.id.action_overview_sort));
            shadowOf(getMainLooper()).idle();

            final AlertDialog dialog = getLatestAlertDialog();

            final List<RadioButton> radioButtons = getRadioButtons(dialog);
            radioButtons.get(LAST_USED.getChoice()).performClick();

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            checkExpectedTeas(TEA_NAME_ACTIVITY, overview);
        });
    }

    @Test
    public void enableShowTeasInStockExpectTeasInStock() {
        mockActualSettings();
        final List<Tea> teaList = generateTeaList(TEA_NAME_ACTIVITY);
        when(teaDao.getFavoriteTeasOrderByActivity()).thenReturn(teaList);

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            overview.onOptionsItemSelected(new RoboMenuItem(R.id.action_overview_sort));
            shadowOf(getMainLooper()).idle();

            final AlertDialog dialog = getLatestAlertDialog();

            final CheckBox checkBoxInStock = dialog.findViewById(R.id.checkbox_overview_in_stock);
            checkBoxInStock.setChecked(true);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            checkExpectedTeas(TEA_NAME_ACTIVITY, overview);
        });
    }

    @Test
    public void changeSortModeToAlphabeticallyExpectTeaList() {
        mockActualSettings();
        final String teaName = "ALPHABETICALLY_";
        final List<Tea> teaList = generateTeaList(teaName);
        when(teaDao.getTeasOrderByAlphabetic()).thenReturn(teaList);

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            overview.onOptionsItemSelected(new RoboMenuItem(R.id.action_overview_sort));
            shadowOf(getMainLooper()).idle();

            final AlertDialog dialog = getLatestAlertDialog();

            final List<RadioButton> radioButtons = getRadioButtons(dialog);
            radioButtons.get(ALPHABETICAL.getChoice()).performClick();

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            checkExpectedTeas(teaName, overview);
        });
    }

    @Test
    public void changeSortModeToVarietyExpectTeaList() {
        mockActualSettings();
        final String teaName = "VARIETY_";
        final List<Tea> teaList = generateTeaList(teaName);
        when(teaDao.getTeasOrderByVariety()).thenReturn(teaList);

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            overview.onOptionsItemSelected(new RoboMenuItem(R.id.action_overview_sort));
            shadowOf(getMainLooper()).idle();

            final AlertDialog dialog = getLatestAlertDialog();

            final List<RadioButton> radioButtons = getRadioButtons(dialog);
            radioButtons.get(BY_VARIETY.getChoice()).performClick();

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            checkExpectedTeas(teaName, overview);
        });
    }

    @Test
    public void changeSortModeToRatingExpectTeaList() {
        mockActualSettings();
        final String teaName = "RATING_";
        final List<Tea> teaList = generateTeaList(teaName);
        when(teaDao.getTeasOrderByRating()).thenReturn(teaList);

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            overview.onOptionsItemSelected(new RoboMenuItem(R.id.action_overview_sort));
            shadowOf(getMainLooper()).idle();

            final AlertDialog dialog = getLatestAlertDialog();

            final List<RadioButton> radioButtons = getRadioButtons(dialog);
            radioButtons.get(RATING.getChoice()).performClick();

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            checkExpectedTeas(teaName, overview);
        });
    }

    @Test
    public void enableSortingHeaderExpectTeaListWithHeader() {
        mockActualSettings(LAST_USED, false);
        final List<Tea> teaList = generateTeaList(TEA_NAME_ACTIVITY);
        when(teaDao.getTeasOrderByActivity()).thenReturn(teaList);

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            final SharedSettings sharedSettings = new SharedSettings(overview.getApplication());
            sharedSettings.setOverviewHeader(true);

            overview.onOptionsItemSelected(new RoboMenuItem(R.id.action_overview_sort));
            shadowOf(getMainLooper()).idle();

            final AlertDialog dialog = getLatestAlertDialog();

            final List<RadioButton> radioButtons = getRadioButtons(dialog);
            radioButtons.get(LAST_USED.getChoice()).performClick();

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            final RecyclerView recyclerView = overview.findViewById(R.id.recycler_view_overview_tea_list);

            assertThat(recyclerView.getAdapter().getItemCount()).isEqualTo(4);
        });
    }

    @Test
    public void clickAddTeaExpectNewTeaActivity() {
        mockActualSettings();

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            final FloatingActionButton addTeaButton = overview.findViewById(R.id.floating_button_overview_new_tea);
            addTeaButton.performClick();

            final Intent expected = new Intent(overview, NewTea.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void clickTeaExpectShowTeaActivity() {
        final int positionTea = 1;
        mockActualSettings();
        final String teaName = "TEA_";
        final List<Tea> teaList = generateTeaList(teaName);
        when(teaDao.getTeasOrderByActivity()).thenReturn(teaList);

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            final RecyclerView teaListRecyclerView = overview.findViewById(R.id.recycler_view_overview_tea_list);
            clickAtPositionRecyclerView(teaListRecyclerView, positionTea);

            final Intent expected = new Intent(overview, ShowTea.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
            assertThat(actual.getExtras().get("teaId")).isEqualTo((long) (positionTea));
        });
    }

    @Test
    public void editTeaExpectNewTeaActivity() {
        final int teaPosition = 1;
        mockActualSettings();
        final String teaName = "TEA_";
        final List<Tea> teaList = generateTeaList(teaName);
        when(teaDao.getTeasOrderByActivity()).thenReturn(teaList);

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            final RecyclerView recyclerView = overview.findViewById(R.id.recycler_view_overview_tea_list);
            final View itemViewRecyclerItem = recyclerView.findViewHolderForAdapterPosition(teaPosition).itemView;
            itemViewRecyclerItem.performLongClick();

            selectItemPopUpMenu(R.id.action_overview_tea_list_edit);

            final Intent expected = new Intent(overview, NewTea.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
            assertThat(actual.getLongExtra("teaId", -1)).isEqualTo((teaPosition));
        });
    }

    @Test
    public void deleteTeaExpectDeletion() {
        final int teaPosition = 1;
        mockActualSettings();
        final String teaName = "TEA_";
        final List<Tea> teaList = generateTeaList(teaName);
        when(teaDao.getTeasOrderByActivity()).thenReturn(teaList);

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            final RecyclerView recyclerView = overview.findViewById(R.id.recycler_view_overview_tea_list);
            final View itemViewRecyclerItem = recyclerView.findViewHolderForAdapterPosition(teaPosition).itemView;
            itemViewRecyclerItem.performLongClick();

            selectItemPopUpMenu(R.id.action_overview_tea_list_delete);

            verify(teaDao).deleteTeaById(1L);
        });
    }

    private void mockActualSettings() {
        mockActualSettings(LAST_USED, false);
    }

    private void mockActualSettings(final SortMode sortMode, final boolean overviewUpdateAlert) {
        final SharedSettings sharedSettings = new SharedSettings(RuntimeEnvironment.getApplication());
        sharedSettings.setFirstStart(false);
        sharedSettings.setOverviewUpdateAlert(overviewUpdateAlert);
        sharedSettings.setSortMode(sortMode);
    }

    private List<Tea> generateTeaList(final String name) {
        final List<Tea> teaList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final Tea tea = new Tea(name + i, "VARIETY", i, "AMOUNT_KIND", i, 0, CurrentDate.getDate());
            tea.setId((long) i);
            tea.setInStock(true);
            teaList.add(tea);
        }
        return teaList;
    }

    private void checkExpectedTeas(final String teaName, final Overview overview) {
        final RecyclerView recyclerView = overview.findViewById(R.id.recycler_view_overview_tea_list);

        assertThat(recyclerView.getAdapter().getItemCount()).isEqualTo(3);
        for (int i = 1; i < 3; i++) {
            final View itemView = recyclerView.findViewHolderForAdapterPosition(i).itemView;
            final TextView heading = itemView.findViewById(R.id.text_view_recycler_view_heading);
            assertThat(heading.getText()).hasToString(teaName + (i));
        }
    }

    private void checkTitleAndMessageOfLatestDialog(final Overview overview, final AlertDialog dialog,
                                                    final int title, final int message) {
        final ShadowAlertDialog shadowDialog = Shadows.shadowOf(dialog);
        assertThat(shadowDialog).isNotNull();
        assertThat(shadowDialog.getTitle()).isEqualTo(overview.getString(title));
        assertThat(shadowDialog.getMessage()).isEqualTo(overview.getString(message));
    }

    private void clickAtPositionRecyclerView(final RecyclerView recyclerView, final int position) {
        recyclerView.scrollToPosition(position);
        final View itemView = recyclerView.findViewHolderForAdapterPosition(position).itemView;
        itemView.performClick();
    }

    private void selectItemPopUpMenu(final int itemId) {
        final PopupMenu latestPopupMenu = ShadowPopupMenu.getLatestPopupMenu();
        final Menu menu = latestPopupMenu.getMenu();
        menu.performIdentifierAction(itemId, FLAG_ALWAYS_PERFORM_CLOSE);
    }


    private List<RadioButton> getRadioButtons(final AlertDialog dialog) {
        final RadioGroup radioGroup = dialog.findViewById(R.id.radio_group_overview_sort_mode);
        final ArrayList<RadioButton> listRadioButtons = new ArrayList<>();
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View o = radioGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                listRadioButtons.add((RadioButton) o);
            }
        }
        return listRadioButtons;
    }
}
