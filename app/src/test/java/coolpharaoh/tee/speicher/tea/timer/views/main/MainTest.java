package coolpharaoh.tee.speicher.tea.timer.views.main;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.views.about.About;
import coolpharaoh.tee.speicher.tea.timer.views.description.UpdateDescription;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.ExportImport;
import coolpharaoh.tee.speicher.tea.timer.views.newtea.NewTea;
import coolpharaoh.tee.speicher.tea.timer.views.settings.Settings;
import coolpharaoh.tee.speicher.tea.timer.views.showtea.ShowTea;

import static android.view.HapticFeedbackConstants.LONG_PRESS;
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
public class MainTest {
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

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            checkExpectedTeas(TEA_NAME_ACTIVITY, main);
            verify(actualSettingsDao).update(any());
        });
    }

    @Test
    public void launchActivityExpectUpdateDescription() {
        mockActualSettings(true, false, 20);
        when(teaDao.getTeasOrderByActivity()).thenReturn(generateTeaList(TEA_NAME_ACTIVITY));

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            final AlertDialog dialogUpdate = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(main, dialogUpdate,
                    R.string.main_dialog_update_header, R.string.main_dialog_update_description);
            dialogUpdate.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            verify(actualSettingsDao).update(any());

            Intent expected = new Intent(main, UpdateDescription.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getData()).isEqualTo(expected.getData());
        });
    }

    @Test
    public void launchActivityExpectUpdateDescriptionClickNegative() {
        mockActualSettings(true, false, 20);
        when(teaDao.getTeasOrderByActivity()).thenReturn(generateTeaList(TEA_NAME_ACTIVITY));

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            getLatestAlertDialog().getButton(DialogInterface.BUTTON_NEGATIVE).performClick();

            verify(actualSettingsDao).update(any());
        });
    }

    @Test
    public void launchActivityExpectRatingDialog() {
        mockActualSettings(false, true, 20);

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            final AlertDialog dialogRating = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(main, dialogRating,
                    R.string.main_dialog_rating_header, R.string.main_dialog_rating_description);
            dialogRating.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            Intent expected = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + main.getPackageName()));
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getData()).isEqualTo(expected.getData());
        });
    }

    @Test
    public void launchActivityExpectRatingDialogClickNegative() {
        mockActualSettings(false, true, 20);

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            final AlertDialog dialogRating = getLatestAlertDialog();
            checkTitleAndMessageOfLatestDialog(main, dialogRating,
                    R.string.main_dialog_rating_header, R.string.main_dialog_rating_description);
            dialogRating.getButton(DialogInterface.BUTTON_NEGATIVE).performClick();

            verify(actualSettingsDao, times(2)).update(any());
        });
    }

    @Test
    public void navigateToSettingsExpectSettingsActivity() {
        mockActualSettings();

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            main.onOptionsItemSelected(new RoboMenuItem(R.id.action_settings));

            Intent expected = new Intent(main, Settings.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void navigateToExportImportExpectExportImportActivity() {
        mockActualSettings();

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            main.onOptionsItemSelected(new RoboMenuItem(R.id.action_exportImport));

            Intent expected = new Intent(main, ExportImport.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void navigateToAboutExpectAboutActivity() {
        mockActualSettings();

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            main.onOptionsItemSelected(new RoboMenuItem(R.id.action_about));

            Intent expected = new Intent(main, About.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void searchStringExpectSearchList() {
        mockActualSettings();
        String teaName = "SEARCH_";
        when(teaDao.getTeasBySearchString(teaName)).thenReturn(generateTeaList(teaName));

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            main.onOptionsItemSelected(new RoboMenuItem(R.id.action_search));

            MenuItemImpl menuItem = ((ActionMenuItemView) main.findViewById(R.id.action_search)).getItemData();
            androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
            searchView.setQuery(teaName, false);

            checkExpectedTeas(teaName, main);
        });
    }

    @Test
    public void changeSortModeToActivityExpectTeaList() {
        mockActualSettings(1);
        when(teaDao.getTeasOrderByActivity()).thenReturn(generateTeaList(TEA_NAME_ACTIVITY));

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            main.onOptionsItemSelected(new RoboMenuItem(R.id.action_sort));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(SORT_ACTIVITY);

            checkExpectedTeas(TEA_NAME_ACTIVITY, main);
        });
    }

    @Test
    public void changeSortModeToAlphabeticallyExpectTeaList() {
        mockActualSettings();
        String teaName = "ALPHABETICALLY_";
        when(teaDao.getTeasOrderByAlphabetic()).thenReturn(generateTeaList(teaName));

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            main.onOptionsItemSelected(new RoboMenuItem(R.id.action_sort));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(SORT_ALPHABETICALLY);

            checkExpectedTeas(teaName, main);
        });
    }

    @Test
    public void changeSortModeToVarietyExpectTeaList() {
        mockActualSettings();
        String teaName = "VARIETY_";
        when(teaDao.getTeasOrderByVariety()).thenReturn(generateTeaList(teaName));

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            main.onOptionsItemSelected(new RoboMenuItem(R.id.action_sort));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(SORT_VARIETY);

            checkExpectedTeas(teaName, main);
        });
    }

    @Test
    public void changeSortModeToRatingExpectTeaList() {
        mockActualSettings();
        String teaName = "RATING_";
        when(teaDao.getTeasOrderByRating()).thenReturn(generateTeaList(teaName));

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            main.onOptionsItemSelected(new RoboMenuItem(R.id.action_sort));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(SORT_RATING);

            checkExpectedTeas(teaName, main);
        });
    }

    @Test
    public void clickAddTeaExpectNewTeaActivity() {
        mockActualSettings();

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            FloatingActionButton addTeaButton = main.findViewById(R.id.newtea);
            addTeaButton.performClick();

            Intent expected = new Intent(main, NewTea.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void clickTeaExpectShowTeaActivity() {
        int positionTea = 1;
        mockActualSettings();
        String teaName = "TEA_";
        when(teaDao.getTeasOrderByActivity()).thenReturn(generateTeaList(teaName));

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            ListView teaList = main.findViewById(R.id.listViewTealist);
            teaList.performItemClick(teaList, positionTea, teaList.getItemIdAtPosition(positionTea));

            Intent expected = new Intent(main, ShowTea.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
            assertThat(actual.getExtras().get("teaId")).isEqualTo((long) positionTea);
        });
    }

    @Ignore("how to click contextMenu?")
    @Test
    public void editTeaExpectNewTeaActivity() {
        mockActualSettings();
        String teaName = "TEA_";
        when(teaDao.getTeasOrderByActivity()).thenReturn(generateTeaList(teaName));

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            ListView teaList = main.findViewById(R.id.listViewTealist);
            longClickItem(teaList, 1);

            //TODO how to click contextMenu?
        });
    }

    @Ignore("how to click contextMenu?")
    @Test
    public void deleteTeaExpectDeletion() {
        mockActualSettings();
        String teaName = "TEA_";
        when(teaDao.getTeasOrderByActivity()).thenReturn(generateTeaList(teaName));

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            ListView teaList = main.findViewById(R.id.listViewTealist);
            longClickItem(teaList, 1);

            //TODO how to click contextMenu?
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
        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setSort(sort);
        actualSettings.setMainRateAlert(mainRateAlert);
        actualSettings.setMainRateCounter(mainRateCounter);
        actualSettings.setMainUpdateAlert(mainUpdateAlert);
        when(actualSettingsDao.getSettings()).thenReturn(actualSettings);
        when(actualSettingsDao.getCountItems()).thenReturn(1);
    }

    private List<Tea> generateTeaList(String name) {
        List<Tea> teaList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Tea tea = new Tea(name + i, "VARIETY", i, "AMOUNT_KIND", i, 0, CurrentDate.getDate());
            tea.setId((long) i);
            teaList.add(tea);
        }
        return teaList;
    }

    private void checkExpectedTeas(String teaName, Main main) {
        ListView teaListView = (ListView) main.findViewById(R.id.listViewTealist);
        TeaAdapter teaAdapter = (TeaAdapter) teaListView.getAdapter();
        assertThat(teaAdapter.getCount()).isEqualTo(3);
        for (int i = 0; i < 3; i++) {
            assertThat(((Tea) teaAdapter.getItem(i)).getName()).isEqualTo(teaName + i);
        }
    }

    public static void longClickItem(ListView listView, int position) {
        if (!listView.isLongClickable())
            return;
        AdapterView.OnItemLongClickListener listener = listView.getOnItemLongClickListener();
        if (listener == null)
            return;
        ListAdapter adapter = listView.getAdapter();
        View itemView = adapter.getView(position, null, listView);
        listener.onItemLongClick(listView, itemView, position, adapter.getItemId(position));
        listView.performHapticFeedback(LONG_PRESS);
    }

    private void checkTitleAndMessageOfLatestDialog(final Main main, final AlertDialog dialog,
                                                    final int title, final int message) {
        ShadowAlertDialog shadowDialog = Shadows.shadowOf(dialog);
        assertThat(shadowDialog).isNotNull();
        assertThat(shadowDialog.getTitle()).isEqualTo(main.getString(title));
        assertThat(shadowDialog.getMessage()).isEqualTo(main.getString(message));
    }
}
