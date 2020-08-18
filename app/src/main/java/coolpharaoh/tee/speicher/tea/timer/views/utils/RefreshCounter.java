package coolpharaoh.tee.speicher.tea.timer.views.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.views.utils.date.CurrentDate;

public class RefreshCounter {

    private RefreshCounter() {
    }

    public static List<Counter> refreshCounters(List<Counter> counters) {
        for (Counter counter : counters) {
            refreshCounter(counter);
        }
        return counters;
    }

    public static void refreshCounter(Counter counter) {
        Date currentDate = CurrentDate.getDate();
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
        cal.setTime(counter.getDayDate());
        int countDay = cal.get(Calendar.DAY_OF_MONTH);
        int countMonth = cal.get(Calendar.MONTH);
        int countYear = cal.get(Calendar.YEAR);
        if (currentDay != countDay || currentMonth != countMonth || currentYear != countYear) {
            counter.setDay(0);
            counter.setDayDate(currentDate);
        }
    }

    private static void refreshWeek(Counter counter, Date currentDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int currentWeek = cal.get(Calendar.WEEK_OF_YEAR);
        int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(counter.getWeekDate());
        int countWeek = cal.get(Calendar.WEEK_OF_YEAR);
        int countYear = cal.get(Calendar.YEAR);
        if (currentWeek != countWeek || currentYear != countYear) {
            counter.setWeek(0);
            counter.setWeekDate(currentDate);
        }
    }

    private static void refreshMonth(Counter counter, Date currentDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(counter.getMonthDate());
        int countMonth = cal.get(Calendar.MONTH);
        int countYear = cal.get(Calendar.YEAR);
        if (currentMonth != countMonth || currentYear != countYear) {
            counter.setMonth(0);
            counter.setMonthDate(currentDate);
        }
    }
}
