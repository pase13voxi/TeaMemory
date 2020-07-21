package coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.daos.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.models.daos.InfusionDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.NoteDao;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataTransferViewModelTest {

    private DataTransferViewModel dataTransferViewModel;

    @Mock
    TeaDAO teaDAO;
    @Mock
    InfusionDAO infusionDAO;
    @Mock
    CounterDao counterDAO;
    @Mock
    NoteDao noteDAO;
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;

    @Before
    public void setUp() {
        when(teaMemoryDatabase.getTeaDAO()).thenReturn(teaDAO);
        when(teaMemoryDatabase.getInfusionDAO()).thenReturn(infusionDAO);
        when(teaMemoryDatabase.getCounterDAO()).thenReturn(counterDAO);
        when(teaMemoryDatabase.getNoteDAO()).thenReturn(noteDAO);

        dataTransferViewModel = new DataTransferViewModel(teaMemoryDatabase);
    }

    @Test
    public void getTeaList() {
        Date date = new GregorianCalendar(2020, 1, 18).getTime();

        List<Tea> teasBefore = new ArrayList<>();
        Tea tea1 = new Tea("Tea1", "Variety1", 1, "Kind1", 1, 1, date);
        teasBefore.add(tea1);
        Tea tea2 = new Tea("Tea2", "Variety2", 2, "Kind2", 2, 2, date);
        teasBefore.add(tea2);
        when(teaDAO.getTeas()).thenReturn(teasBefore);

        List<Tea> teasAfter = dataTransferViewModel.getTeaList();

        assertThat(teasAfter).isEqualTo(teasBefore);
    }

    @Test
    public void insertTea() {
        Date date = new GregorianCalendar(2020, 1, 18).getTime();

        Tea teaBefore = new Tea("Tea", "Variety", 1, "Kind", 1, 1, date);

        dataTransferViewModel.insertTea(teaBefore);

        ArgumentCaptor<Tea> captor = ArgumentCaptor.forClass(Tea.class);
        verify(teaDAO).insert(captor.capture());
        Tea teaAfter = captor.getValue();

        assertThat(teaAfter).isEqualTo(teaBefore);
    }

    @Test
    public void deleteAll() {
        dataTransferViewModel.deleteAllTeas();
        verify(teaDAO).deleteAll();
    }

    @Test
    public void getInfusionList() {
        List<Infusion> infusionsBefore = new ArrayList<>();
        Infusion infusion1 = new Infusion(1L, 1, "1", "1", 1, 1);
        infusionsBefore.add(infusion1);
        Infusion infusion2 = new Infusion(2L, 2, "2", "2", 2, 2);
        infusionsBefore.add(infusion2);

        when(infusionDAO.getInfusions()).thenReturn(infusionsBefore);

        List<Infusion> infusionsAfter = dataTransferViewModel.getInfusionList();

        assertThat(infusionsAfter).isEqualTo(infusionsBefore);
    }

    @Test
    public void insertInfusion() {
        Infusion infusionBefore = new Infusion(1L, 1, "1", "1", 1, 1);

        dataTransferViewModel.insertInfusion(infusionBefore);

        ArgumentCaptor<Infusion> captor = ArgumentCaptor.forClass(Infusion.class);
        verify(infusionDAO).insert(captor.capture());
        Infusion infusionAfter = captor.getValue();

        assertThat(infusionAfter).isEqualTo(infusionBefore);
    }

    @Test
    public void getCounterList() {
        Date date = new GregorianCalendar(2020, 1, 18).getTime();

        List<Counter> countersBefore = new ArrayList<>();
        Counter counter1 = new Counter(1L, 1, 1, 1, 1L, date, date, date);
        countersBefore.add(counter1);
        Counter counter2 = new Counter(2L, 2, 2, 2, 2L, date, date, date);
        countersBefore.add(counter2);

        when(counterDAO.getCounters()).thenReturn(countersBefore);

        List<Counter> countersAfter = dataTransferViewModel.getCounterList();

        assertThat(countersAfter).isEqualTo(countersBefore);
    }

    @Test
    public void insertCounter() {
        Date date = new GregorianCalendar(2020, 1, 18).getTime();
        Counter counterBefore = new Counter(1L, 1, 1, 1, 1L, date, date, date);

        dataTransferViewModel.insertCounter(counterBefore);

        ArgumentCaptor<Counter> captor = ArgumentCaptor.forClass(Counter.class);
        verify(counterDAO).insert(captor.capture());
        Counter counterAfter = captor.getValue();

        assertThat(counterAfter).isEqualTo(counterBefore);
    }

    @Test
    public void getNoteList() {
        List<Note> notesBefore = new ArrayList<>();
        Note note1 = new Note(1L,1,"Header1","Description1");
        notesBefore.add(note1);
        Note note2 = new Note(2L,2,"Header2","Description2");
        notesBefore.add(note2);

        when(noteDAO.getNotes()).thenReturn(notesBefore);

        List<Note> notesAfter = dataTransferViewModel.getNoteList();

        assertThat(notesAfter).isEqualTo(notesBefore);
    }

    @Test
    public void insertNote() {
        Note noteBefore = new Note(1L,1,"Header","Description");

        dataTransferViewModel.insertNote(noteBefore);

        ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        verify(noteDAO).insert(captor.capture());
        Note noteAfter = captor.getValue();

        assertThat(noteAfter).isEqualTo(noteBefore);
    }
}
