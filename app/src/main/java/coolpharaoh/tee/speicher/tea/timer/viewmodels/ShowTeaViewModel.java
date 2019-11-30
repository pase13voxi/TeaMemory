package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.content.Context;

import androidx.room.Room;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.InfusionDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.NoteDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Note;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.LanguageConversation;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.RefreshCounter;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.TimeHelper;

public class ShowTeaViewModel {

    private Context context;

    private TeaDAO teaDAO;
    private NoteDAO noteDAO;
    private CounterDAO counterDAO;
    private ActualSettingsDAO actualSettingsDAO;

    private Tea tea;
    private List<Infusion> infusions;
    private Counter counter;
    private Note note;
    private ActualSettings actualSettings;

    private int infusionIndex = 0;

    public ShowTeaViewModel(long teaId, Context context) {

        this.context = context;

        TeaMemoryDatabase database = Room.databaseBuilder(context, TeaMemoryDatabase.class, "teamemory")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        teaDAO = database.getTeaDAO();
        InfusionDAO mInfusionDAO = database.getInfusionDAO();
        noteDAO = database.getNoteDAO();
        counterDAO = database.getCounterDAO();
        actualSettingsDAO = database.getActualSettingsDAO();

        tea = teaDAO.getTeaById(teaId);
        infusions = mInfusionDAO.getInfusionsByTeaId(teaId);
        counter = counterDAO.getCounterByTeaId(teaId);
        note = noteDAO.getNoteByTeaId(teaId);
        actualSettings = actualSettingsDAO.getSettings();
    }

    // Tea
    public long getTeaId(){
        return tea.getId();
    }

    public String getName() {
        return tea.getName();
    }

    public String getVariety() {
        if(tea.getVariety().equals("")) {
            return "-";
        }
        else {
            return LanguageConversation.convertCodeToVariety(tea.getVariety(), context);
        }
    }

    public int getAmount() {
        return tea.getAmount();
    }

    public String getAmountkind(){
        return tea.getAmountkind();
    }

    public int getColor(){
        return tea.getColor();
    }

    public void setCurrentDate(){
        tea.setDate(Calendar.getInstance().getTime());
        teaDAO.update(tea);
    }

    // Infusion
    public TimeHelper getTime(){
        return TimeHelper.getMinutesAndSeconds(infusions.get(infusionIndex).getTime());
    }

    public TimeHelper getCooldowntime(){
        return TimeHelper.getMinutesAndSeconds(infusions.get(infusionIndex).getCooldowntime());
    }

    public int getTemperature(){
        if(actualSettings.getTemperatureunit().equals("Celsius")){
            return infusions.get(infusionIndex).getTemperaturecelsius();
        }else {
            return infusions.get(infusionIndex).getTemperaturefahrenheit();
        }
    }

    public int getInfusionSize(){
        return infusions.size();
    }

    public int getInfusionIndex(){
        return infusionIndex;
    }

    public void setInfusionIndex(int infusionIndex){
        this.infusionIndex = infusionIndex;
    }

    public void incrementInfusionIndex(){
        infusionIndex++;
    }

    // Notes
    public Note getNote(){
        return note;
    }

    public void setNote(String note){
        this.note.setDescription(note);
        noteDAO.update(this.note);
    }

    //Counter
    public void countCounter(){
        RefreshCounter.refreshCounter(counter);
        Date currentDate = Calendar.getInstance().getTime();
        counter.setMonthdate(currentDate);
        counter.setWeekdate(currentDate);
        counter.setDaydate(currentDate);
        counter.setOverall(counter.getOverall()+1);
        counter.setMonth(counter.getMonth()+1);
        counter.setWeek(counter.getWeek()+1);
        counter.setDay(counter.getDay()+1);
        counterDAO.update(counter);
    }

    public Counter getCounter() {
        RefreshCounter.refreshCounter(counter);
        counterDAO.update(counter);
        return counter;
    }
    // Settings
    public boolean isVibration() {
        return actualSettings.isVibration();
    }

    public void setVibration(boolean vibration) {
        actualSettings.setVibration(vibration);
        actualSettingsDAO.update(actualSettings);
    }

    public boolean isNotification() {
        return actualSettings.isNotification();
    }

    public void setNotification(boolean notification) {
        actualSettings.setNotification(notification);
        actualSettingsDAO.update(actualSettings);
    }

    public boolean isAnimation(){
        return actualSettings.isAnimation();
    }

    public boolean isShowteaalert() {
        return actualSettings.isShowteaalert();
    }

    public void setShowteaalert(boolean showteaalert) {
        actualSettings.setShowteaalert(showteaalert);
        actualSettingsDAO.update(actualSettings);
    }

    public String getTemperatureunit(){
        return actualSettings.getTemperatureunit();
    }
}
