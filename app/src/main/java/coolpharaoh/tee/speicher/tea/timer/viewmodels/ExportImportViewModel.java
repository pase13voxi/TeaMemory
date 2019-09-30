package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.InfusionDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.NoteDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.entities.Tea;

public class ExportImportViewModel extends ViewModel {


    private TeaDAO mTeaDAO;
    private InfusionDAO mInfusionDAO;
    private CounterDAO mCounterDAO;
    private NoteDAO mNoteDAO;

    public ExportImportViewModel(Context context) {
        TeaMemoryDatabase database = Room.databaseBuilder(context, TeaMemoryDatabase.class, "teamemory")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        mTeaDAO = database.getTeaDAO();
        mInfusionDAO = database.getInfusionDAO();
        mCounterDAO = database.getCounterDAO();
        mNoteDAO = database.getNoteDAO();
    }

    //Teas
    public List<Tea> getTeaList(){
        return mTeaDAO.getTeas();
    }

    public long insertTea(Tea tea){
        return mTeaDAO.insert(tea);
    }

    public void deleteAllTeas(){
        mTeaDAO.deleteAll();
    }

    //Infusions
    public List<Infusion> getInfusionList(){
        return mInfusionDAO.getInfusions();
    }

    public void insertInfusion(Infusion infusion){
        mInfusionDAO.insert(infusion);
    }

    //Counters
    public List<Counter> getCounterList(){
        return mCounterDAO.getCounters();
    }

    public void insertCounter(Counter counter){
        mCounterDAO.insert(counter);
    }

    //Notes
    public List<Note> getNoteList(){
        return mNoteDAO.getNotes();
    }

    public void insertNote(Note note){
        mNoteDAO.insert(note);

    }

}
