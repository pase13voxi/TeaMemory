package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.InfusionDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.NoteDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.entities.Tea;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.LanguageConversation;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.TemperatureConversation;

public class NewTeaViewModel {

    Context context;

    private TeaDAO mTeaDAO;
    private InfusionDAO mInfusionDAO;
    private NoteDAO mNoteDAO;
    private CounterDAO mCounterDAO;
    private ActualSettingsDAO mActualSettingsDAO;

    private Tea mTea;
    private List<Infusion> mInfusions;
    private ActualSettings mActualSettings;

    private int mInfusionIndex = 0;

    public NewTeaViewModel(long teaId, Context context) {
        this.context = context;

        TeaMemoryDatabase database = Room.databaseBuilder(context, TeaMemoryDatabase.class, "teamemory")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        mTeaDAO = database.getTeaDAO();
        mInfusionDAO = database.getInfusionDAO();
        mNoteDAO = database.getNoteDAO();
        mCounterDAO = database.getCounterDAO();
        mActualSettingsDAO = database.getActualSettingsDAO();

        mTea = mTeaDAO.getTeaById(teaId);
        mInfusions = mInfusionDAO.getInfusionsByTeaId(teaId);
        mActualSettings = mActualSettingsDAO.getSettings();
    }

    public NewTeaViewModel(Context context) {
        this.context = context;

        TeaMemoryDatabase database = Room.databaseBuilder(context, TeaMemoryDatabase.class, "teamemory")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        mTeaDAO = database.getTeaDAO();
        mInfusionDAO = database.getInfusionDAO();
        mNoteDAO = database.getNoteDAO();
        mCounterDAO = database.getCounterDAO();
        mActualSettingsDAO = database.getActualSettingsDAO();

        mTea = new Tea();
        mInfusions = new ArrayList<>();
        addInfusion(true);
        mActualSettings = mActualSettingsDAO.getSettings();
    }

    // Tea
    public long getTeaId() {
        return mTea.getId();
    }

    public String getName() {
        return mTea.getName();
    }

    public String getVariety() {
        return mTea.getVariety();
    }

    public int getAmount() {
        return mTea.getAmount();
    }

    public String getAmountkind() {
        return mTea.getAmountkind();
    }

    public int getColor() {
        return mTea.getColor();
    }

    // Infusion
    public void addInfusion(boolean first) {
        Infusion infusion = new Infusion();
        infusion.setTemperaturecelsius(-500);
        infusion.setTemperaturefahrenheit(-500);
        mInfusions.add(infusion);
        if (!first){
            mInfusionIndex++;
        }
    }

    public void takeInfusionInformation(String time, String cooldowntime, int temperature) {
        mInfusions.get(mInfusionIndex).setTime(time);
        mInfusions.get(mInfusionIndex).setCooldowntime(cooldowntime);
        if (mActualSettings.getTemperatureunit().equals("Celsius")) {
            mInfusions.get(mInfusionIndex).setTemperaturecelsius(temperature);
            mInfusions.get(mInfusionIndex).setTemperaturefahrenheit(TemperatureConversation.celsiusToFahrenheit(temperature));
        } else {
            mInfusions.get(mInfusionIndex).setTemperaturefahrenheit(temperature);
            mInfusions.get(mInfusionIndex).setTemperaturecelsius(TemperatureConversation.fahrenheitToCelsius(temperature));
        }
    }

    public void deleteInfusion() {
        if (mInfusions.size() > 1) {
            mInfusions.remove(mInfusionIndex);
            if (mInfusionIndex == mInfusions.size()) {
                mInfusionIndex--;
            }
        }
    }

    public String getInfusionTime() {
        return mInfusions.get(mInfusionIndex).getTime();
    }

    public String getInfusionCooldowntime() {
        return mInfusions.get(mInfusionIndex).getCooldowntime();
    }

    public int getInfusionTemperature() {
        if (mActualSettings.getTemperatureunit().equals("Celsius")) {
            return mInfusions.get(mInfusionIndex).getTemperaturecelsius();
        } else {
            return mInfusions.get(mInfusionIndex).getTemperaturefahrenheit();
        }
    }

    public boolean previousInfusion() {
        if (mInfusionIndex - 1 >= 0) {
            mInfusionIndex--;
            return true;
        } else {
            return false;
        }
    }

    public boolean nextInfusion() {
        if (mInfusionIndex + 1 < mInfusions.size()) {
            mInfusionIndex++;
            return true;
        } else {
            return false;
        }
    }

    public int getInfusionIndex() {
        return mInfusionIndex;
    }

    public int getInfusionSize() {
        return mInfusions.size();
    }

    // Settings
    public String getTemperatureunit(){
        return mActualSettings.getTemperatureunit();
    }

    // Overall
    public void editTea(String name, String variety, int amount, String amountkind, int color) {
        setTeaInformation(name, variety, amount, amountkind, color);

        mTeaDAO.update(mTea);
        long teaId = mTeaDAO.getLastEditedTea().getId();

        setInfusionInformation(teaId);
    }

    public void createNewTea(String name, String variety, int amount, String amountkind, int color) {
        setTeaInformation(name, variety, amount, amountkind, color);
        mTea.setLastInfusion(0);

        mTeaDAO.insert(mTea);
        long teaId = mTeaDAO.getLastEditedTea().getId();

        setInfusionInformation(teaId);

        // create new counter
        Counter counter = new Counter();
        counter.setTeaId(teaId);
        counter.setDay(0);
        counter.setWeek(0);
        counter.setMonth(0);
        counter.setOverall(0);
        counter.setDaydate(Calendar.getInstance().getTime());
        counter.setWeekdate(Calendar.getInstance().getTime());
        counter.setMonthdate(Calendar.getInstance().getTime());

        mCounterDAO.insert(counter);

        // create standard note
        Note note = new Note();
        note.setTeaId(teaId);
        note.setPosition(1);
        note.setDescription("");
        mNoteDAO.insert(note);
    }

    private void setTeaInformation(String name, String variety, int amount, String amountkind, int color) {
        mTea.setName(name);
        mTea.setVariety(LanguageConversation.convertVarietyToCode(variety,context));
        mTea.setAmount(amount);
        mTea.setAmountkind(amountkind);
        mTea.setColor(color);
        mTea.setDate(Calendar.getInstance().getTime());
    }

    private void setInfusionInformation(long teaId) {
        mInfusionDAO.deleteInfusionByTeaId(teaId);
        for (int i = 0; i < mInfusions.size(); i++) {
            mInfusions.get(i).setTeaId(teaId);
            mInfusions.get(i).setInfusion(i);
            mInfusionDAO.insert(mInfusions.get(i));
        }
    }
}
