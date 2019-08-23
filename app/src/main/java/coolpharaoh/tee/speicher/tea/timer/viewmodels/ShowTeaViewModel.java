package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.Calendar;
import java.util.Date;
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
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.RefreshCounter;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.TimeHelper;

public class ShowTeaViewModel {

    Context context;

    private TeaDAO mTeaDAO;
    private InfusionDAO mInfusionDAO;
    private NoteDAO mNoteDAO;
    private CounterDAO mCounterDAO;
    private ActualSettingsDAO mActualSettingsDAO;

    private Tea mTea;
    private List<Infusion> mInfusion;
    private Counter mCounter;
    private Note mNote;
    private ActualSettings mActualSettings;

    private int mInfusionIndex = 0;

    public ShowTeaViewModel(long teaId, Context context) {

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
        mInfusion = mInfusionDAO.getInfusionsByTeaId(teaId);
        mCounter = mCounterDAO.getCounterByTeaId(teaId);
        mNote = mNoteDAO.getNoteByTeaId(teaId);
        mActualSettings = mActualSettingsDAO.getSettings();
    }

    // Tea
    public long getTeaId(){
        return mTea.getId();
    }

    public String getName() {
        return mTea.getName();
    }

    public String getVariety() {
        if(mTea.getVariety().equals("")) {
            return "-";
        }
        else {
            return LanguageConversation.convertCodeToVariety(mTea.getVariety(), context);
        }
    }

    public int getAmount() {
        return mTea.getAmount();
    }

    public String getAmountkind(){
        return mTea.getAmountkind();
    }

    public int getColor(){
        return mTea.getColor();
    }

    public void setCurrentDate(){
        mTea.setDate(Calendar.getInstance().getTime());
        mTeaDAO.update(mTea);
    }

    // Infusion
    public TimeHelper getTime(){
        return TimeHelper.getMinutesAndSeconds(mInfusion.get(mInfusionIndex).getTime());
    }

    public TimeHelper getCooldowntime(){
        return TimeHelper.getMinutesAndSeconds(mInfusion.get(mInfusionIndex).getCooldowntime());
    }

    public int getTemperature(){
        if(mActualSettings.getTemperatureunit().equals("Celsius")){
            return mInfusion.get(mInfusionIndex).getTemperaturecelsius();
        }else {
            return mInfusion.get(mInfusionIndex).getTemperaturefahrenheit();
        }
    }

    public int getInfusionSize(){
        return mInfusion.size();
    }

    public int getInfusionIndex(){
        return mInfusionIndex;
    }

    public void setInfusionIndex(int infusionIndex){
        mInfusionIndex = infusionIndex;
    }

    public void incrementInfusionIndex(){
        mInfusionIndex++;
    }

    // Notes
    public Note getNote(){
        return mNote;
    }

    public void setNote(String note){
        mNote.setDescription(note);
        mNoteDAO.update(mNote);
    }

    //Counter
    public void countCounter(){
        mCounter = RefreshCounter.refreshCounter(mCounter);
        Date currentDate = Calendar.getInstance().getTime();
        mCounter.setMonthdate(currentDate);
        mCounter.setWeekdate(currentDate);
        mCounter.setDaydate(currentDate);
        mCounter.setOverall(mCounter.getOverall()+1);
        mCounter.setMonth(mCounter.getMonth()+1);
        mCounter.setWeek(mCounter.getWeek()+1);
        mCounter.setDay(mCounter.getDay()+1);
        mCounterDAO.update(mCounter);
    }

    public Counter getCounter() {
        mCounter = RefreshCounter.refreshCounter(mCounter);
        mCounterDAO.update(mCounter);
        return mCounter;
    }
    // Settings
    public boolean isVibration() {
        return mActualSettings.isVibration();
    }

    public void setVibration(boolean vibration) {
        mActualSettings.setVibration(vibration);
        mActualSettingsDAO.update(mActualSettings);
    }

    public boolean isNotification() {
        return mActualSettings.isNotification();
    }

    public void setNotification(boolean notification) {
        mActualSettings.setNotification(notification);
        mActualSettingsDAO.update(mActualSettings);
    }

    public boolean isAnimation(){
        return mActualSettings.isAnimation();
    }

    public boolean isShowteaalert() {
        return mActualSettings.isShowteaalert();
    }

    public void setShowteaalert(boolean showteaalert) {
        mActualSettings.setShowteaalert(showteaalert);
        mActualSettingsDAO.update(mActualSettings);
    }

    public String getTemperatureunit(){
        return mActualSettings.getTemperatureunit();
    }
}
