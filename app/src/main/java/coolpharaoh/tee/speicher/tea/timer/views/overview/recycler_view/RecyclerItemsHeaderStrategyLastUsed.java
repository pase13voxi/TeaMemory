package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view;

import android.app.Application;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety;

class RecyclerItemsHeaderStrategyLastUsed implements RecyclerItemsHeaderStrategy {

    private final Application application;

    RecyclerItemsHeaderStrategyLastUsed(final Application application) {
        this.application = application;
    }

    @Override
    public List<RecyclerItemOverview> generateFrom(final List<Tea> teaList) {

        final ArrayList<RecyclerItemOverview> recyclerItems = new ArrayList<>();
        String lastDate = "";

        for (final Tea tea : teaList) {
            final String date = getLastUsedHeader(tea.getDate());
            if (!date.equals(lastDate)) {
                recyclerItems.add(new RecyclerItemOverview("- " + date + " -", null, null, null, false));
                lastDate = date;
            }
            final String variety = Variety.convertStoredVarietyToText(tea.getVariety(), application);
            recyclerItems.add(new RecyclerItemOverview(null, tea.getId(), tea.getName(), variety, tea.isInStock()));
        }
        return recyclerItems;
    }

    private String getLastUsedHeader(final Date lastUsed) {
        final String[] monthNames = application.getResources().getStringArray(R.array.overview_sort_last_used_month);

        final Date today = CurrentDate.getDate();
        if (isCurrentWeek(lastUsed, today)) {
            return application.getString(R.string.overview_sort_last_used_this_week);
        } else if (isCurrentMonth(lastUsed, today)) {
            return application.getString(R.string.overview_sort_last_used_this_month);
        } else if (isCurrentYear(lastUsed, today)) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(lastUsed);
            final int lastUsedMonth = cal.get(Calendar.MONTH);
            return monthNames[lastUsedMonth];
        } else {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(lastUsed);
            final int lastUsedYear = cal.get(Calendar.YEAR);
            return String.valueOf(lastUsedYear);
        }
    }

    private static boolean isCurrentWeek(final Date lastUsed, final Date currentDate) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        final int currentWeek = cal.get(Calendar.WEEK_OF_YEAR);
        final int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(lastUsed);
        final int countWeek = cal.get(Calendar.WEEK_OF_YEAR);
        final int countYear = cal.get(Calendar.YEAR);
        return (currentWeek == countWeek && currentYear == countYear);
    }

    private static boolean isCurrentMonth(final Date lastUsed, final Date currentDate) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        final int currentMonth = cal.get(Calendar.MONTH);
        final int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(lastUsed);
        final int countMonth = cal.get(Calendar.MONTH);
        final int countYear = cal.get(Calendar.YEAR);
        return (currentMonth == countMonth && currentYear == countYear);
    }

    private static boolean isCurrentYear(final Date lastUsed, final Date currentDate) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        final int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(lastUsed);
        final int countYear = cal.get(Calendar.YEAR);
        return currentYear == countYear;
    }
}
