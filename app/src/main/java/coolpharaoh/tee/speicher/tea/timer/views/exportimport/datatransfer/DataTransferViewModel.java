package coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer;

import androidx.lifecycle.ViewModel;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.InfusionDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.NoteDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;

class DataTransferViewModel extends ViewModel {

    private final TeaDAO teaDAO;
    private final InfusionDAO infusionDAO;
    private final CounterDAO counterDAO;
    private final NoteDAO noteDAO;

    DataTransferViewModel(TeaMemoryDatabase database) {
        teaDAO = database.getTeaDAO();
        infusionDAO = database.getInfusionDAO();
        counterDAO = database.getCounterDAO();
        noteDAO = database.getNoteDAO();
    }

    //Teas
    List<Tea> getTeaList() {
        return teaDAO.getTeas();
    }

    long insertTea(Tea tea) {
        return teaDAO.insert(tea);
    }

    void deleteAllTeas() {
        teaDAO.deleteAll();
    }

    //Infusions
    List<Infusion> getInfusionList() {
        return infusionDAO.getInfusions();
    }

    public void insertInfusion(Infusion infusion){
        infusionDAO.insert(infusion);
    }

    //Counters
    List<Counter> getCounterList() {
        return counterDAO.getCounters();
    }

    void insertCounter(Counter counter) {
        counterDAO.insert(counter);
    }

    //Notes
    List<Note> getNoteList() {
        return noteDAO.getNotes();
    }

    void insertNote(Note note) {
        noteDAO.insert(note);

    }

}
