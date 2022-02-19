package coolpharaoh.tee.speicher.tea.timer.views.overview;

import static android.os.Looper.getMainLooper;
import static android.view.Menu.FLAG_ALWAYS_PERFORM_CLOSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode.ALPHABETICAL;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode.BY_VARIETY;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode.LAST_USED;
import static coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode.RATING;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import org.mockito.ArgumentCaptor;
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
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode;
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk;
import coolpharaoh.tee.speicher.tea.timer.core.system.SystemUtility;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.views.description.UpdateDescription;
import coolpharaoh.tee.speicher.tea.timer.views.more.More;
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.NewTea;
import coolpharaoh.tee.speicher.tea.timer.views.settings.Settings;
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea;
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController;
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageControllerFactory;

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
    ImageController imageController;
    @Mock
    SystemUtility systemUtility;

    @Before
    public void setUp() {
        mockDB();
        mockSystemVersionCode();
        ImageControllerFactory.setMockedImageController(imageController);
    }

    private void mockDB() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDao);
        when(teaMemoryDatabase.getInfusionDao()).thenReturn(infusionDao);
    }

    private void mockSystemVersionCode() {
        CurrentSdk.setFixedSystem(systemUtility);
        when(systemUtility.getSdkVersion()).thenReturn(Build.VERSION_CODES.R);
    }

    @Test
    public void launchActivityExpectTeaList() {
        mockSharedSettings();
        final List<Tea> teaList = generateTeaList(TEA_NAME_ACTIVITY);
        when(teaDao.getTeasOrderByActivity()).thenReturn(teaList);

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> checkExpectedTeas(TEA_NAME_ACTIVITY, overview));
    }

    @Test
    public void launchActivityExpectUpdateDescription() {
        mockSharedSettings(LAST_USED, true);
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
        mockSharedSettings(LAST_USED, true);
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
    public void clickToRandomChoiceExpectRandomChoiceDialog() {
        mockSharedSettings();

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            final FloatingActionButton buttonRandomChoice = overview.findViewById(R.id.floating_button_overview_random_choice);
            buttonRandomChoice.performClick();
            shadowOf(getMainLooper()).idle();

            final AlertDialog dialog = getLatestAlertDialog();
            final ShadowAlertDialog shadowDialog = Shadows.shadowOf(dialog);

            assertThat(shadowDialog.getTitle()).isEqualTo(overview.getString(R.string.overview_dialog_random_choice_title));
        });
    }

    @Test
    public void navigateToSettingsExpectSettingsActivity() {
        mockSharedSettings();

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            overview.onOptionsItemSelected(new RoboMenuItem(R.id.action_overview_settings));

            final Intent expected = new Intent(overview, Settings.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void navigateToMoreExpectMoreActivity() {
        mockSharedSettings();

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            overview.onOptionsItemSelected(new RoboMenuItem(R.id.action_overview_more));

            final Intent expected = new Intent(overview, More.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void searchStringExpectSearchList() {
        mockSharedSettings();
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
        mockSharedSettings(ALPHABETICAL, false);
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
        mockSharedSettings();
        final List<Tea> teaList = generateTeaList(TEA_NAME_ACTIVITY);
        when(teaDao.getTeasInStockOrderByActivity()).thenReturn(teaList);

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
    public void showTeaListWithStoredImageAndFilledImageText() {
        final String imageUri = "uri";

        mockSharedSettings();
        final List<Tea> teaList = generateTeaList(TEA_NAME_ACTIVITY);
        when(teaDao.getTeasInStockOrderByActivity()).thenReturn(teaList);
        when(imageController.getImageUriByTeaId(0L)).thenReturn(Uri.parse(imageUri));
        when(imageController.getLastModified(Uri.parse(imageUri))).thenReturn("date");

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            overview.onOptionsItemSelected(new RoboMenuItem(R.id.action_overview_sort));
            shadowOf(getMainLooper()).idle();

            final AlertDialog dialog = getLatestAlertDialog();

            final CheckBox checkBoxInStock = dialog.findViewById(R.id.checkbox_overview_in_stock);
            checkBoxInStock.setChecked(true);

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            final RecyclerView recyclerView = overview.findViewById(R.id.recycler_view_overview_tea_list);
            final View itemView0 = recyclerView.findViewHolderForAdapterPosition(0).itemView;
            final ImageView image = itemView0.findViewById(R.id.image_view_recycler_view_image);
            assertThat(image.getTag()).isEqualTo(imageUri);

            final View itemView1 = recyclerView.findViewHolderForAdapterPosition(1).itemView;
            final TextView textView = itemView1.findViewById(R.id.text_view_recycler_view_image);
            assertThat(textView.getText()).isEqualTo("A");
        });
    }

    @Test
    public void changeSortModeToAlphabeticallyExpectTeaList() {
        mockSharedSettings();
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
        mockSharedSettings();
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
        mockSharedSettings();
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
        mockSharedSettings(LAST_USED, false);
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
        mockSharedSettings();

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
        mockSharedSettings();
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
    public void setTeaInStockExpectTeaInStock() {
        final int teaPosition = 1;
        mockSharedSettings();
        final String teaName = "TEA_";
        final List<Tea> teaList = generateTeaList(teaName);
        when(teaDao.getTeasOrderByActivity()).thenReturn(teaList);
        when(teaDao.getTeaById(teaPosition)).thenReturn(teaList.get(teaPosition));

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            final RecyclerView recyclerView = overview.findViewById(R.id.recycler_view_overview_tea_list);
            final View itemViewRecyclerItem = recyclerView.findViewHolderForAdapterPosition(teaPosition).itemView;
            itemViewRecyclerItem.performLongClick();

            selectItemPopUpMenu(R.id.action_overview_tea_list_in_stock);

            final ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
            verify(teaDao).update(captor.capture());
            final Tea tea = captor.getValue();

            assertThat(tea.isInStock()).isFalse();
        });
    }

    @Test
    public void editTeaExpectNewTeaActivity() {
        final int teaPosition = 1;
        mockSharedSettings();
        final String teaName = "TEA_";
        final List<Tea> teaList = generateTeaList(teaName);
        when(teaDao.getTeasOrderByActivity()).thenReturn(teaList);
        when(teaDao.getTeaById(teaPosition)).thenReturn(teaList.get(teaPosition));

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
        mockSharedSettings();
        final String teaName = "TEA_";
        final List<Tea> teaList = generateTeaList(teaName);
        when(teaDao.getTeasOrderByActivity()).thenReturn(teaList);
        when(teaDao.getTeaById(teaPosition)).thenReturn(teaList.get(teaPosition));

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            final RecyclerView recyclerView = overview.findViewById(R.id.recycler_view_overview_tea_list);
            final View itemViewRecyclerItem = recyclerView.findViewHolderForAdapterPosition(teaPosition).itemView;
            itemViewRecyclerItem.performLongClick();

            selectItemPopUpMenu(R.id.action_overview_tea_list_delete);

            final AlertDialog dialogDelete = getLatestAlertDialog();
            dialogDelete.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            verify(teaDao).deleteTeaById(1L);

            verify(imageController).removeImageByTeaId(1L);
        });
    }

    @Test
    public void cancelDeleteTeaExpectCanceledDeletion() {
        final int teaPosition = 1;
        mockSharedSettings();
        final String teaName = "TEA_";
        final List<Tea> teaList = generateTeaList(teaName);
        when(teaDao.getTeasOrderByActivity()).thenReturn(teaList);
        when(teaDao.getTeaById(teaPosition)).thenReturn(teaList.get(teaPosition));

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            final RecyclerView recyclerView = overview.findViewById(R.id.recycler_view_overview_tea_list);
            final View itemViewRecyclerItem = recyclerView.findViewHolderForAdapterPosition(teaPosition).itemView;
            itemViewRecyclerItem.performLongClick();

            selectItemPopUpMenu(R.id.action_overview_tea_list_delete);

            final AlertDialog dialogDelete = getLatestAlertDialog();
            dialogDelete.getButton(DialogInterface.BUTTON_NEGATIVE).performClick();
            shadowOf(getMainLooper()).idle();

            verify(teaDao, never()).deleteTeaById(1L);
        });
    }

    @Test
    public void deleteTeaVersionCodeOlderAndroidQExpectDeletion() {
        final int teaPosition = 1;
        mockSharedSettings();
        final String teaName = "TEA_";
        final List<Tea> teaList = generateTeaList(teaName);
        when(teaDao.getTeasOrderByActivity()).thenReturn(teaList);
        when(teaDao.getTeaById(teaPosition)).thenReturn(teaList.get(teaPosition));
        when(systemUtility.getSdkVersion()).thenReturn(Build.VERSION_CODES.P);

        final ActivityScenario<Overview> overviewActivityScenario = ActivityScenario.launch(Overview.class);
        overviewActivityScenario.onActivity(overview -> {
            final RecyclerView recyclerView = overview.findViewById(R.id.recycler_view_overview_tea_list);
            final View itemViewRecyclerItem = recyclerView.findViewHolderForAdapterPosition(teaPosition).itemView;
            itemViewRecyclerItem.performLongClick();

            selectItemPopUpMenu(R.id.action_overview_tea_list_delete);

            final AlertDialog dialogDelete = getLatestAlertDialog();
            dialogDelete.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            shadowOf(getMainLooper()).idle();

            verify(teaDao).deleteTeaById(1L);

            verify(imageController, never()).removeImageByTeaId(anyLong());
        });
    }

    private void mockSharedSettings() {
        mockSharedSettings(LAST_USED, false);
    }

    private void mockSharedSettings(final SortMode sortMode, final boolean overviewUpdateAlert) {
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
            final View o = radioGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                listRadioButtons.add((RadioButton) o);
            }
        }
        return listRadioButtons;
    }
}
