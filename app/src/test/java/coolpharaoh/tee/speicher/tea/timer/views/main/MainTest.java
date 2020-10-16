package coolpharaoh.tee.speicher.tea.timer.views.main;

import android.app.Application;
import android.content.Intent;
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
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.views.about.About;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.ExportImport;
import coolpharaoh.tee.speicher.tea.timer.views.newtea.NewTea;
import coolpharaoh.tee.speicher.tea.timer.views.settings.Settings;
import coolpharaoh.tee.speicher.tea.timer.views.showtea.ShowTea;

import static android.view.HapticFeedbackConstants.LONG_PRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;


//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class MainTest {
    private enum SortItems {
        ACTIVITY, ALPHABETICALLY, VARIETY
    }

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    TeaDao teaDao;
    @Mock
    InfusionDao infusionDao;
    @Mock
    NoteDao noteDao;
    @Mock
    CounterDao counterDao;
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
        when(teaMemoryDatabase.getNoteDao()).thenReturn(noteDao);
        when(teaMemoryDatabase.getCounterDao()).thenReturn(counterDao);
        when(teaMemoryDatabase.getActualSettingsDao()).thenReturn(actualSettingsDao);
    }

    @Test
    public void launchActivityExpectTeaList() {
        mockActualSettings(0, true, 10);
        String teaName = "ACTIVITY_";
        when(teaDao.getTeasOrderByActivity()).thenReturn(generateTeaList(teaName));

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> checkExpectedTeas(teaName, main));
    }

    @Test
    public void launchActivityExpectRatingDialog() {
        mockActualSettings(0, true, 20);

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            assertThat(shadowAlertDialog).isNotNull();
        });
    }

    @Test
    public void navigateToSettingsExpectSettingsActivity() {
        mockActualSettings(0, false, 0);

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
        mockActualSettings(0, false, 0);

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
        mockActualSettings(0, false, 0);

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            main.onOptionsItemSelected(new RoboMenuItem(R.id.action_about));

            Intent expected = new Intent(main, About.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    // the cast is needed
    @SuppressWarnings("java:S1905")
    @Test
    public void searchStringExpectSearchList() {
        mockActualSettings(0, false, 0);
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
        mockActualSettings(1, false, 0);
        String teaName = "ACTIVITY_";
        when(teaDao.getTeasOrderByActivity()).thenReturn(generateTeaList(teaName));

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            main.onOptionsItemSelected(new RoboMenuItem(R.id.action_sort));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(SortItems.ACTIVITY.ordinal());

            checkExpectedTeas(teaName, main);
        });
    }

    @Test
    public void changeSortModeToAlphabeticallyExpectTeaList() {
        mockActualSettings(0, false, 0);
        String teaName = "ALPHABETICALLY_";
        when(teaDao.getTeasOrderByAlphabetic()).thenReturn(generateTeaList(teaName));

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            main.onOptionsItemSelected(new RoboMenuItem(R.id.action_sort));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(SortItems.ALPHABETICALLY.ordinal());

            checkExpectedTeas(teaName, main);
        });
    }

    @Test
    public void changeSortModeToVarietyExpectTeaList() {
        mockActualSettings(0, false, 0);
        String teaName = "VARIETY_";
        when(teaDao.getTeasOrderByVariety()).thenReturn(generateTeaList(teaName));

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            main.onOptionsItemSelected(new RoboMenuItem(R.id.action_sort));

            ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(getLatestAlertDialog());
            shadowAlertDialog.clickOnItem(SortItems.VARIETY.ordinal());

            checkExpectedTeas(teaName, main);
        });
    }

    @Test
    public void clickAddTeaExpectNewTeaActivity() {
        mockActualSettings(0, false, 0);

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
        mockActualSettings(0, false, 0);
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

    @Ignore
    @Test
    public void editTeaExpectNewTeaActivity() {
        mockActualSettings(0, false, 0);
        String teaName = "TEA_";
        when(teaDao.getTeasOrderByActivity()).thenReturn(generateTeaList(teaName));

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            ListView teaList = main.findViewById(R.id.listViewTealist);
            longClickItem(teaList, 1);

            //TODO how to clock contextMenu
        });
    }

    @Ignore
    @Test
    public void deleteTeaExpectDeletion() {
        mockActualSettings(0, false, 0);
        String teaName = "TEA_";
        when(teaDao.getTeasOrderByActivity()).thenReturn(generateTeaList(teaName));

        ActivityScenario<Main> mainActivityScenario = ActivityScenario.launch(Main.class);
        mainActivityScenario.onActivity(main -> {
            ListView teaList = main.findViewById(R.id.listViewTealist);
            longClickItem(teaList, 1);

            //TODO how to clock contextMenu
        });
    }

    private void mockActualSettings(int sort, boolean mainRateAlert, int mainRateCounter) {
        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setSort(sort);
        actualSettings.setMainRateAlert(mainRateAlert);
        actualSettings.setMainRateCounter(mainRateCounter);
        when(actualSettingsDao.getSettings()).thenReturn(actualSettings);
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
}
