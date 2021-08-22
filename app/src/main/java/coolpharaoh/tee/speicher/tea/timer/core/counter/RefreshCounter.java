package coolpharaoh.tee.speicher.tea.timer.core.counter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;

public class RefreshCounter {

    private RefreshCounter() {
    }

    public static List<Counter> refreshCounters(final List<Counter> counters) {
        for (final Counter counter : counters) {
            refreshCounter(counter);
        }
        return counters;
    }

    public static void refreshCounter(final Counter counter) {
        final Date currentDate = CurrentDate.getDate();
        refreshDay(counter, currentDate);
        refreshWeek(counter, currentDate);
        refreshMonth(counter, currentDate);
    }

    private static void refreshDay(final Counter counter, final Date currentDate) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        final int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        final int currentMonth = cal.get(Calendar.MONTH);
        final int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(counter.getDayDate());
        final int countDay = cal.get(Calendar.DAY_OF_MONTH);
        final int countMonth = cal.get(Calendar.MONTH);
        final int countYear = cal.get(Calendar.YEAR);
        if (currentDay != countDay || currentMonth != countMonth || currentYear != countYear) {
            counter.setDay(0);
            counter.setDayDate(currentDate);
        }
    }

    private static void refreshWeek(final Counter counter, final Date currentDate) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        final int currentWeek = cal.get(Calendar.WEEK_OF_YEAR);
        final int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(counter.getWeekDate());
        final int countWeek = cal.get(Calendar.WEEK_OF_YEAR);
        final int countYear = cal.get(Calendar.YEAR);
        if (currentWeek != countWeek || currentYear != countYear) {
            counter.setWeek(0);
            counter.setWeekDate(currentDate);
        }
    }

    private static void refreshMonth(final Counter counter, final Date currentDate) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        final int currentMonth = cal.get(Calendar.MONTH);
        final int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(counter.getMonthDate());
        final int countMonth = cal.get(Calendar.MONTH);
        final int countYear = cal.get(Calendar.YEAR);
        if (currentMonth != countMonth || currentYear != countYear) {
            counter.setMonth(0);
            counter.setMonthDate(currentDate);
        }
    }
}
