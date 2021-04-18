package coolpharaoh.tee.speicher.tea.timer.views.information;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;

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
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowPopupMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.note.Note;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

import static android.view.Menu.FLAG_ALWAYS_PERFORM_CLOSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.shadows.ShadowAlertDialog.getLatestAlertDialog;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class InformationTest {
    private static final String TEA_ID_EXTRA = "teaId";
    private static final long TEA_ID = 1L;
    private static final String HEADER = "header";
    private static final String DESCRIPTION = "description";
    private static final String TEA_NAME = "teaName";
    private static final String NOTES_HEADER = "01_notes";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    TeaDao teaDao;
    @Mock
    NoteDao noteDao;
    @Mock
    CounterDao counterDao;

    @Before
    public void setUp() {
        mockDB();
    }

    private void mockDB() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDao);
        when(teaMemoryDatabase.getNoteDao()).thenReturn(noteDao);
        when(teaMemoryDatabase.getCounterDao()).thenReturn(counterDao);
    }

    @Test
    public void launchActivityAndExpectEmptyInformation() {
        createTea(0);
        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), Information.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<Information> informationActivityScenario = ActivityScenario.launch(intent);
        informationActivityScenario.onActivity(information -> {
            final RatingBar ratingBar = information.findViewById(R.id.information_rating_bar);
            assertThat(ratingBar.getRating()).isZero();

            final RecyclerView recyclerView = information.findViewById(R.id.information_recycler_view_details);
            assertThat(recyclerView.getAdapter().getItemCount()).isZero();

            final EditText editTextNotes = information.findViewById(R.id.information_edit_text_notes);
            assertThat(editTextNotes.getText().toString()).isBlank();
            verify(noteDao).insert(any());
        });
    }

    @Test
    public void launchActivityAndExpectFilledInformation() {
        final int rating = 4;
        createTea(rating);
        final Note notes = createNotes();
        createDetails();
        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), Information.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<Information> informationActivityScenario = ActivityScenario.launch(intent);
        informationActivityScenario.onActivity(information -> {
            final TextView toolbarTitle = information.findViewById(R.id.tool_bar_title);
            assertThat(toolbarTitle.getText()).hasToString(TEA_NAME);

            final RatingBar ratingBar = information.findViewById(R.id.information_rating_bar);
            assertThat(ratingBar.getRating()).isEqualTo(4);

            final RecyclerView recyclerView = information.findViewById(R.id.information_recycler_view_details);
            assertThat(recyclerView.getAdapter().getItemCount()).isEqualTo(3);

            final EditText editTextNotes = information.findViewById(R.id.information_edit_text_notes);
            assertThat(editTextNotes.getText()).hasToString(notes.getDescription());
        });
    }

    @Test
    public void leaveActivityAndExpectStoredNotes() {
        final String notes = "New Notes";
        createTea(0);
        createNotes();
        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), Information.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<Information> informationActivityScenario = ActivityScenario.launch(intent);
        informationActivityScenario.onActivity(information -> {
            final EditText editTextNotes = information.findViewById(R.id.information_edit_text_notes);
            editTextNotes.setText(notes);
        });

        informationActivityScenario.close();

        final ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        verify(noteDao).update(captor.capture());
        assertThat(captor.getValue())
                .extracting(Note::getPosition, Note::getHeader, Note::getDescription)
                .containsExactly(-1, NOTES_HEADER, notes);
    }

    @Test
    public void updateRating() {
        final int newRating = 4;
        createTea(0);
        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), Information.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<Information> informationActivityScenario = ActivityScenario.launch(intent);
        informationActivityScenario.onActivity(information -> {
            final RatingBar ratingBar = information.findViewById(R.id.information_rating_bar);
            ratingBar.setRating(newRating);

            final ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
            verify(teaDao).update(captor.capture());
            assertThat(captor.getValue().getRating()).isEqualTo(4);
        });
    }

    @Test
    public void addDetail() {
        createTea(0);
        createNotes();
        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), Information.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<Information> informationActivityScenario = ActivityScenario.launch(intent);
        informationActivityScenario.onActivity(information -> {
            final ImageButton buttonAddDetail = information.findViewById(R.id.information_button_add_detail);
            buttonAddDetail.performClick();

            final AlertDialog dialogAddDetail = getAndCheckAlertDialog(information, R.string.information_add_detail_dialog_heading);

            checkAndSetContentInDetailsDialog(dialogAddDetail, R.id.information_edit_text_dialog_add_edit_header,
                    "", HEADER);
            checkAndSetContentInDetailsDialog(dialogAddDetail, R.id.information_edit_text_dialog_add_edit_description,
                    "", DESCRIPTION);

            dialogAddDetail.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            final ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
            verify(noteDao).insert(captor.capture());
            final Note note = captor.getValue();

            assertThat(note).extracting(Note::getHeader, Note::getDescription)
                    .containsExactly(HEADER, DESCRIPTION);
        });
    }

    @Test
    public void addBlankDetail() {
        createTea(0);
        createNotes();
        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), Information.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<Information> informationActivityScenario = ActivityScenario.launch(intent);
        informationActivityScenario.onActivity(information -> {
            final ImageButton buttonAddDetail = information.findViewById(R.id.information_button_add_detail);
            buttonAddDetail.performClick();

            final AlertDialog dialogAddDetail = getAndCheckAlertDialog(information, R.string.information_add_detail_dialog_heading);

            dialogAddDetail.getButton(DialogInterface.BUTTON_POSITIVE).performClick();


            verify(noteDao, times(0)).insert(any());
        });
    }

    @Test
    public void deleteDetail() {
        final int position = 1;
        createTea(0);
        createDetails();
        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), Information.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<Information> informationActivityScenario = ActivityScenario.launch(intent);
        informationActivityScenario.onActivity(information -> {
            final RecyclerView recyclerView = information.findViewById(R.id.information_recycler_view_details);
            final View itemViewRecyclerItem = recyclerView.findViewHolderForAdapterPosition(position).itemView;
            final Button buttonChangeItem = itemViewRecyclerItem.findViewById(R.id.button_detail_options);

            buttonChangeItem.performClick();

            selectItemPopUpMenu(R.id.action_information_details_delete);

            verify(noteDao).deleteNoteByTeaIdAndPosition(TEA_ID, position);
        });
    }

    @Test
    public void editDetail() {
        final int position = 0;
        createTea(0);
        final List<Note> details = createDetails();
        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), Information.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<Information> informationActivityScenario = ActivityScenario.launch(intent);
        informationActivityScenario.onActivity(information -> {
            final RecyclerView recyclerView = information.findViewById(R.id.information_recycler_view_details);
            final View itemViewRecyclerItem = recyclerView.findViewHolderForAdapterPosition(position).itemView;
            final Button buttonChangeItem = itemViewRecyclerItem.findViewById(R.id.button_detail_options);

            buttonChangeItem.performClick();

            selectItemPopUpMenu(R.id.action_information_details_edit);

            final AlertDialog dialogAddDetail = getAndCheckAlertDialog(information, R.string.information_edit_detail_dialog_heading);

            checkAndSetContentInDetailsDialog(dialogAddDetail, R.id.information_edit_text_dialog_add_edit_header,
                    details.get(position).getHeader(), HEADER);
            checkAndSetContentInDetailsDialog(dialogAddDetail, R.id.information_edit_text_dialog_add_edit_description,
                    details.get(position).getDescription(), DESCRIPTION);

            dialogAddDetail.getButton(DialogInterface.BUTTON_POSITIVE).performClick();

            final ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
            verify(noteDao).update(captor.capture());
            final Note note = captor.getValue();

            assertThat(note).extracting(Note::getHeader, Note::getDescription)
                    .containsExactly(HEADER, DESCRIPTION);
        });
    }

    @Test
    public void showLastUsed() {
        final Date date = CurrentDate.getDate();
        createTea(0, date);

        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), Information.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<Information> informationActivityScenario = ActivityScenario.launch(intent);
        informationActivityScenario.onActivity(information -> {
            final TextView textViewLastUsed = information.findViewById(R.id.information_text_view_last_used);

            final SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            final String strDate = formatter.format(date);
            assertThat(textViewLastUsed.getText()).hasToString(information.getString(R.string.information_counter_last_used, strDate));
        });
    }

    @Test
    public void fillCounter() {
        createTea(0);
        final Counter counter = new Counter(TEA_ID, 1, 2, 3, 4,
                CurrentDate.getDate(), CurrentDate.getDate(), CurrentDate.getDate());
        when(counterDao.getCounterByTeaId(TEA_ID)).thenReturn(counter);

        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), Information.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<Information> informationActivityScenario = ActivityScenario.launch(intent);
        informationActivityScenario.onActivity(information -> checkCounter(information, "1", "2", "3", "4"));
    }

    @Test
    public void fillCounterWhenCounterIsNotAvailable() {
        createTea(0);

        final Intent intent = new Intent(getInstrumentation().getTargetContext().getApplicationContext(), Information.class);
        intent.putExtra(TEA_ID_EXTRA, TEA_ID);

        final ActivityScenario<Information> informationActivityScenario = ActivityScenario.launch(intent);
        informationActivityScenario.onActivity(information -> checkCounter(information, "0", "0", "0", "0"));
    }

    private void createTea(final int rating, final Date date) {
        final Tea tea = new Tea(TEA_NAME, null, 0, null, 0, 0, date);
        tea.setRating(rating);
        when(teaDao.getTeaById(TEA_ID)).thenReturn(tea);
    }

    private void createTea(final int rating) {
        final Tea tea = new Tea(TEA_NAME, null, 0, null, 0, 0, CurrentDate.getDate());
        tea.setRating(rating);
        when(teaDao.getTeaById(TEA_ID)).thenReturn(tea);
    }

    private List<Note> createDetails() {
        final List<Note> details = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            details.add(new Note(TEA_ID, i, HEADER + i, DESCRIPTION + i));
        }
        when(noteDao.getNotesByTeaIdAndPositionBiggerZero(TEA_ID)).thenReturn(details);
        return details;
    }

    private Note createNotes() {
        final Note notes = new Note(TEA_ID, -1, NOTES_HEADER, "Some Notes");
        when(noteDao.getNoteByTeaIdAndPosition(TEA_ID, -1)).thenReturn(notes);
        return notes;
    }

    private void selectItemPopUpMenu(final int itemId) {
        final PopupMenu latestPopupMenu = ShadowPopupMenu.getLatestPopupMenu();
        final Menu menu = latestPopupMenu.getMenu();
        menu.performIdentifierAction(itemId, FLAG_ALWAYS_PERFORM_CLOSE);
    }

    private AlertDialog getAndCheckAlertDialog(final Information information, final int dialogHeading) {
        final AlertDialog dialogAddDetail = getLatestAlertDialog();
        final ShadowAlertDialog shadowDialogAddDetail = Shadows.shadowOf(dialogAddDetail);
        assertThat(shadowDialogAddDetail).isNotNull();
        assertThat(shadowDialogAddDetail.getTitle()).isEqualTo(information.getString(dialogHeading));
        return dialogAddDetail;
    }

    private void checkAndSetContentInDetailsDialog(final AlertDialog dialog, final int editTextId,
                                                   final String oldContent, final String newContent) {
        final EditText editTextAddHeader = dialog.findViewById(editTextId);
        assertThat(editTextAddHeader.getText()).hasToString(oldContent);
        editTextAddHeader.setText(newContent);
    }

    private void checkCounter(final Information information, final String today, final String week,
                              final String month, final String overall) {
        final TextView textViewToday = information.findViewById(R.id.information_text_view_counter_today);
        final TextView textViewWeek = information.findViewById(R.id.information_text_view_counter_week);
        final TextView textViewMonth = information.findViewById(R.id.information_text_view_counter_month);
        final TextView textViewOverall = information.findViewById(R.id.information_text_view_counter_overall);

        assertThat(textViewToday.getText()).hasToString(today);
        assertThat(textViewWeek.getText()).hasToString(week);
        assertThat(textViewMonth.getText()).hasToString(month);
        assertThat(textViewOverall.getText()).hasToString(overall);
    }
}
