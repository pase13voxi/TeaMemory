package coolpharaoh.tee.speicher.tea.timer.views.overview;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.Menu;
import android.view.View;
import android.widget.PopupMenu;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowPopupMenu;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettingsDao;
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

import static android.os.Looper.getMainLooper;
import static android.view.Menu.FLAG_ALWAYS_PERFORM_CLOSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class OverviewTest {
    private static final int SORT_ACTIVITY = 0;
    private static final int SORT_ALPHABETICALLY = 1;
    private static final int SORT_VARIETY = 2;
    private static final int SORT_RATING = 3;
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
        mockActualSettings(false, true, 10);
        when(teaDao.getTeasOrderByActivity()).thenReturn(generateTeaList(TEA_NAME_ACTIVITY));

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            checkExpectedTeas(TEA_NAME_ACTIVITY, overview);
            verify(actualSettingsDao).update(any());
        });
    }

    @Test
    public void launchActivityExpectUpdateDescription() {
        mockActualSettings(true, false, 20);
        when(teaDao.getTeasOrderByActivity()).thenReturn(generateTeaList(TEA_NAME_ACTIVITY));

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            final AlertDialog dialogUpdate = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(overview, dialogUpdate,
                    R.string.overview_dialog_update_header, R.string.overview_dialog_update_description);
            dialogUpdate.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            verify(actualSettingsDao).update(any());

            final Intent expected = new Intent(overview, UpdateDescription.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getData()).isEqualTo(expected.getData());
        });
    }

    @Test
    public void launchActivityExpectUpdateDescriptionClickNegative() {
        mockActualSettings(true, false, 20);
        when(teaDao.getTeasOrderByActivity()).thenReturn(generateTeaList(TEA_NAME_ACTIVITY));

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            getLatestAlertDialog().getButton(DialogInterface.BUTTON_NEGATIVE).performClick();

            verify(actualSettingsDao).update(any());
        });
    }

    @Test
    public void launchActivityExpectRatingDialog() {
        mockActualSettings(false, true, 20);

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            final AlertDialog dialogRating = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(overview, dialogRating,
                    R.string.overview_dialog_rating_header, R.string.overview_dialog_rating_description);
            dialogRating.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            final Intent expected = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + overview.getPackageName()));
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getData()).isEqualTo(expected.getData());
        });
    }

    @Test
    public void launchActivityExpectRatingDialogClickNegative() {
        mockActualSettings(false, true, 20);

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            final AlertDialog dialogRating = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(overview, dialogRating,
                    R.string.overview_dialog_rating_header, R.string.overview_dialog_rating_description);
            dialogRating.getButton(DialogInterface.BUTTON_NEGATIVE).performClick();
            shadowOf(getMainLooper()).idle();

            verify(actualSettingsDao, times(2)).update(any());
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
        when(teaDao.getTeasBySearchString(teaName)).thenReturn(generateTeaList(teaName));

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
        mockActualSettings(1);
        when(teaDao.getTeasOrderByActivity()).thenReturn(generateTeaList(TEA_NAME_ACTIVITY));

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            overview.onOptionsItemSelected(new RoboMenuItem(R.id.action_overview_sort));

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(SORT_ACTIVITY);
            shadowOf(getMainLooper()).idle();

            checkExpectedTeas(TEA_NAME_ACTIVITY, overview);
        });
    }

    @Test
    public void changeSortModeToAlphabeticallyExpectTeaList() {
        mockActualSettings();
        String teaName = "ALPHABETICALLY_";
        when(teaDao.getTeasOrderByAlphabetic()).thenReturn(generateTeaList(teaName));

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            overview.onOptionsItemSelected(new RoboMenuItem(R.id.action_overview_sort));

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(SORT_ALPHABETICALLY);
            shadowOf(getMainLooper()).idle();

            checkExpectedTeas(teaName, overview);
        });
    }

    @Test
    public void changeSortModeToVarietyExpectTeaList() {
        mockActualSettings();
        String teaName = "VARIETY_";
        when(teaDao.getTeasOrderByVariety()).thenReturn(generateTeaList(teaName));

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            overview.onOptionsItemSelected(new RoboMenuItem(R.id.action_overview_sort));

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(SORT_VARIETY);
            shadowOf(getMainLooper()).idle();

            checkExpectedTeas(teaName, overview);
        });
    }

    @Test
    public void changeSortModeToRatingExpectTeaList() {
        mockActualSettings();
        String teaName = "RATING_";
        when(teaDao.getTeasOrderByRating()).thenReturn(generateTeaList(teaName));

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            overview.onOptionsItemSelected(new RoboMenuItem(R.id.action_overview_sort));

            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(SORT_RATING);
            shadowOf(getMainLooper()).idle();

            checkExpectedTeas(teaName, overview);
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
        int positionTea = 1;
        mockActualSettings();
        final String teaName = "TEA_";
        when(teaDao.getTeasOrderByActivity()).thenReturn(generateTeaList(teaName));

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            final RecyclerView teaListRecyclerView = overview.findViewById(R.id.recycler_view_overview_tea_list);
            clickAtPositionRecyclerView(teaListRecyclerView, positionTea);

            final Intent expected = new Intent(overview, ShowTea.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
            assertThat(actual.getExtras().get("teaId")).isEqualTo((long) positionTea);
        });
    }

    @Test
    public void editTeaExpectNewTeaActivity() {
        final int teaPosition = 1;
        mockActualSettings();
        final String teaName = "TEA_";
        when(teaDao.getTeasOrderByActivity()).thenReturn(generateTeaList(teaName));

        ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            final RecyclerView recyclerView = overview.findViewById(R.id.recycler_view_overview_tea_list);
            final View itemViewRecyclerItem = recyclerView.findViewHolderForAdapterPosition(teaPosition).itemView;
            itemViewRecyclerItem.performLongClick();

            selectItemPopUpMenu(R.id.action_overview_tea_list_edit);

            final Intent expected = new Intent(overview, NewTea.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
            assertThat(actual.getLongExtra("teaId", -1)).isEqualTo(teaPosition);
        });
    }

    @Test
    public void deleteTeaExpectDeletion() {
        final int teaPosition = 1;
        mockActualSettings();
        String teaName = "TEA_";
        when(teaDao.getTeasOrderByActivity()).thenReturn(generateTeaList(teaName));

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            final RecyclerView recyclerView = overview.findViewById(R.id.recycler_view_overview_tea_list);
            final View itemViewRecyclerItem = recyclerView.findViewHolderForAdapterPosition(teaPosition).itemView;
            itemViewRecyclerItem.performLongClick();

            selectItemPopUpMenu(R.id.action_overview_tea_list_delete);

            final ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
            verify(teaDao).delete(captor.capture());
            final Tea tea = captor.getValue();

            assertThat(tea.getName()).isEqualTo(teaName + teaPosition);
        });
    }

    private void mockActualSettings() {
        mockActualSettings(0, false, 0, false);
    }

    private void mockActualSettings(final int sort) {
        mockActualSettings(sort, false, 0, false);
    }

    private void mockActualSettings(final boolean mainUpdateAlert, final boolean mainRateAlert, final int mainRateCounter) {
        mockActualSettings(0, mainRateAlert, mainRateCounter, mainUpdateAlert);
    }

    private void mockActualSettings(final int sort, final boolean mainRateAlert,
                                    final int mainRateCounter, final boolean mainUpdateAlert) {
        final ActualSettings actualSettings = new ActualSettings();
        actualSettings.setSort(sort);
        actualSettings.setMainRateAlert(mainRateAlert);
        actualSettings.setMainRateCounter(mainRateCounter);
        actualSettings.setMainUpdateAlert(mainUpdateAlert);
        when(actualSettingsDao.getSettings()).thenReturn(actualSettings);
        when(actualSettingsDao.getCountItems()).thenReturn(1);
    }

    private List<Tea> generateTeaList(final String name) {
        final List<Tea> teaList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Tea tea = new Tea(name + i, "VARIETY", i, "AMOUNT_KIND", i, 0, CurrentDate.getDate());
            tea.setId((long) i);
            teaList.add(tea);
        }
        return teaList;
    }

    private void checkExpectedTeas(final String teaName, final Overview overview) {
        final RecyclerView recyclerView = overview.findViewById(R.id.recycler_view_overview_tea_list);

        assertThat(recyclerView.getAdapter().getItemCount()).isEqualTo(3);
        for (int i = 0; i < 3; i++) {
            final View itemView = recyclerView.findViewHolderForAdapterPosition(i).itemView;
            final TextView heading = itemView.findViewById(R.id.text_view_recycler_view_heading);
            assertThat(heading.getText()).hasToString(teaName + i);
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
}
