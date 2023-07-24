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

        if (counter.hasEmptyFields()) {
            resetCounter(counter, currentDate);
        } else {
            refreshWeek(counter, currentDate);
            refreshMonth(counter, currentDate);
            refreshYear(counter, currentDate);
        }
    }

    private static void resetCounter(final Counter counter, final Date currentDate) {
        counter.setWeek(0);
        counter.setMonth(0);
        counter.setYear(0);
        counter.setOverall(0);
        counter.setWeekDate(currentDate);
        counter.setMonthDate(currentDate);
        counter.setYearDate(currentDate);
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

    private static void refreshYear(final Counter counter, final Date currentDate) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        final int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(counter.getYearDate());
        final int countYear = cal.get(Calendar.YEAR);
        if (currentYear != countYear) {
            counter.setYear(0);
            counter.setYearDate(currentDate);
        }
    }
}
