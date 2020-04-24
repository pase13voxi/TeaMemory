package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.InfusionDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.NoteDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.LanguageConversation;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.RefreshCounter;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.TimeHelper;

public class ShowTeaViewModel {

    private final Context context;

    private final TeaDAO teaDAO;
    private final InfusionDAO infusionDAO;
    private final NoteDAO noteDAO;
    private final CounterDAO counterDAO;
    private final ActualSettingsDAO actualSettingsDAO;

    private long teaId;
    private int infusionIndex = 0;

    public ShowTeaViewModel(long teaId, TeaMemoryDatabase database, Context context) {
        this.teaId = teaId;
        this.context = context;

        teaDAO = database.getTeaDAO();
        infusionDAO = database.getInfusionDAO();
        noteDAO = database.getNoteDAO();
        counterDAO = database.getCounterDAO();
        actualSettingsDAO = database.getActualSettingsDAO();
    }

    // Tea
    public long getTeaId() {
        return teaDAO.getTeaById(teaId).getId();
    }

    public String getName() {
        return teaDAO.getTeaById(teaId).getName();
    }

    public String getVariety() {
        if (teaDAO.getTeaById(teaId).getVariety().equals("")) {
            return "-";
        } else {
            return LanguageConversation.convertCodeToVariety(teaDAO.getTeaById(teaId).getVariety(), context);
        }
    }

    public int getAmount() {
        return teaDAO.getTeaById(teaId).getAmount();
    }

    public String getAmountkind() {
        return teaDAO.getTeaById(teaId).getAmountkind();
    }

    public int getColor() {
        return teaDAO.getTeaById(teaId).getColor();
    }

    public void setCurrentDate() {
        Tea tea = teaDAO.getTeaById(teaId);
        tea.setDate(Calendar.getInstance().getTime());
        teaDAO.update(tea);
    }

    public int getNextInfusion() {
        return teaDAO.getTeaById(teaId).getLastInfusion();
    }

    public void updateNextInfusion() {
        Tea tea = teaDAO.getTeaById(teaId);
        if ((infusionIndex + 1) >= getInfusionSize()) {
            tea.setLastInfusion(0);
        } else {
            tea.setLastInfusion(infusionIndex + 1);
        }
        teaDAO.update(tea);
    }

    // Infusion
    public TimeHelper getTime() {
        return TimeHelper.getMinutesAndSeconds(infusionDAO.getInfusionsByTeaId(teaId).get(infusionIndex).getTime());
    }

    public TimeHelper getCooldowntime() {
        return TimeHelper.getMinutesAndSeconds(infusionDAO.getInfusionsByTeaId(teaId).get(infusionIndex).getCooldowntime());
    }

    public int getTemperature() {
        if (getTemperatureunit().equals("Celsius")) {
            return infusionDAO.getInfusionsByTeaId(teaId).get(infusionIndex).getTemperaturecelsius();
        } else {
            return infusionDAO.getInfusionsByTeaId(teaId).get(infusionIndex).getTemperaturefahrenheit();
        }
    }

    public int getInfusionSize() {
        return infusionDAO.getInfusionsByTeaId(teaId).size();
    }

    public int getInfusionIndex() {
        return infusionIndex;
    }

    public void setInfusionIndex(int infusionIndex) {
        this.infusionIndex = infusionIndex;
    }

    public void incrementInfusionIndex() {
        infusionIndex++;
    }

    // Notes
    public Note getNote() {
        return noteDAO.getNoteByTeaId(teaId);
    }

    public void setNote(String noteText) {
        Note note = noteDAO.getNoteByTeaId(teaId);
        note.setDescription(noteText);
        noteDAO.update(note);
    }

    //Counter
    public void countCounter() {
        Counter counter = counterDAO.getCounterByTeaId(teaId);
        RefreshCounter.refreshCounter(counter);
        Date currentDate = Calendar.getInstance().getTime();
        counter.setMonthdate(currentDate);
        counter.setWeekdate(currentDate);
        counter.setDaydate(currentDate);
        counter.setOverall(counter.getOverall() + 1);
        counter.setMonth(counter.getMonth() + 1);
        counter.setWeek(counter.getWeek() + 1);
        counter.setDay(counter.getDay() + 1);
        counterDAO.update(counter);
    }

    public Counter getCounter() {
        Counter counter = counterDAO.getCounterByTeaId(teaId);
        RefreshCounter.refreshCounter(counter);
        counterDAO.update(counter);
        return counter;
    }

    // Settings
    public boolean isAnimation() {
        return actualSettingsDAO.getSettings().isAnimation();
    }

    public boolean isShowteaalert() {
        return actualSettingsDAO.getSettings().isShowteaalert();
    }

    public void setShowteaalert(boolean showteaalert) {
        ActualSettings actualSettings = actualSettingsDAO.getSettings();
        actualSettings.setShowteaalert(showteaalert);
        actualSettingsDAO.update(actualSettings);
    }

    public String getTemperatureunit() {
        return actualSettingsDAO.getSettings().getTemperatureunit();
    }
}
