package coolpharaoh.tee.speicher.tea.timer.viewmodels.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;

public class RefreshCounter {

    public static List<Counter> refreshCounters(List<Counter> counters) {
        for (Counter counter : counters) {
            refreshCounter(counter);
        }
        return counters;
    }

    public static void refreshCounter(Counter counter){
        Date currentDate = Calendar.getInstance().getTime();
        refreshDay(counter, currentDate);
        refreshWeek(counter, currentDate);
        refreshMonth(counter, currentDate);
    }

    private static void refreshDay(Counter counter, Date currentDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(counter.getDaydate());
        int countDay = cal.get(Calendar.DAY_OF_MONTH);
        int countMonth = cal.get(Calendar.MONTH);
        int countYear = cal.get(Calendar.YEAR);
        if (currentDay != countDay || currentMonth != countMonth || currentYear != countYear) {
            counter.setDay(0);
            counter.setDaydate(currentDate);
        }
    }

    private static void refreshWeek(Counter counter, Date currentDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int currentWeek = cal.get(Calendar.WEEK_OF_YEAR);
        int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(counter.getWeekdate());
        int countWeek = cal.get(Calendar.WEEK_OF_YEAR);
        int countYear = cal.get(Calendar.YEAR);
        if (currentWeek != countWeek || currentYear != countYear) {
            counter.setWeek(0);
            counter.setWeekdate(currentDate);
        }
    }

    private static void refreshMonth(Counter counter, Date currentDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(counter.getMonthdate());
        int countMonth = cal.get(Calendar.MONTH);
        int countYear = cal.get(Calendar.YEAR);
        if (currentMonth != countMonth || currentYear != countYear) {
            counter.setMonth(0);
            counter.setMonthdate(currentDate);
        }
    }
}
