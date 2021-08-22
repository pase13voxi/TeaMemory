package coolpharaoh.tee.speicher.tea.timer.views.overview;

import android.app.Application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RecyclerItemOverview {
    private static final String DATE_FORMAT = "dd MMMM yyyy";

    final String category;
    final Long teaId;
    final String teaName;
    final String variety;

    static List<RecyclerItemOverview> generateFrom(final int sorting, final List<Tea> teaList, final Application application) {
        switch (sorting) {
            case 0:
                return generateWithTimestampStrategy(teaList, application);
            case 1:
                return generateWithAlphabeticalStrategy(teaList, application);
            case 2:
                return generateWithVarietyStrategy(teaList, application);
            case 3:
                return generateWithRatingStrategy(teaList, application);
            default:
                return generateWithDefaultStrategy(teaList, application);
        }
    }

    private static List<RecyclerItemOverview> generateWithTimestampStrategy(final List<Tea> teaList, final Application application) {

        final ArrayList<RecyclerItemOverview> recyclerItems = new ArrayList<>();
        String lastDate = "";

        for (final Tea tea : teaList) {
            final String date = extractDate(tea.getDate());
            if (!date.equals(lastDate)) {
                recyclerItems.add(new RecyclerItemOverview(date, null, null, null));
                lastDate = date;
            }
            final String variety = Variety.convertStoredVarietyToText(tea.getVariety(), application);
            recyclerItems.add(new RecyclerItemOverview(null, tea.getId(), tea.getName(), variety));
        }
        return recyclerItems;
    }

    private static String extractDate(final Date teaDate) {
        final Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, -1);
        final Date yesterday = calendar.getTime();

        final SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        final String dateString = formatter.format(teaDate);
        final String todayString = formatter.format(today);
        final String yesterdayString = formatter.format(yesterday);
        if (dateString.equals(todayString)) {
            return "today";
        } else if (dateString.equals(yesterdayString)) {
            return "yesterday";
        } else {
            return dateString;
        }


    }

    private static List<RecyclerItemOverview> generateWithAlphabeticalStrategy(final List<Tea> teaList, final Application application) {
        final ArrayList<RecyclerItemOverview> recyclerItems = new ArrayList<>();
        String lastFirstLetter = "";
        for (final Tea tea : teaList) {
            final String firstLetter = tea.getName().substring(0, 1).toUpperCase();
            if (!lastFirstLetter.equals(firstLetter)) {
                recyclerItems.add(new RecyclerItemOverview(firstLetter, null, null, null));
                lastFirstLetter = firstLetter;
            }
            final String variety = Variety.convertStoredVarietyToText(tea.getVariety(), application);
            recyclerItems.add(new RecyclerItemOverview(null, tea.getId(), tea.getName(), variety));
        }
        return recyclerItems;
    }

    private static List<RecyclerItemOverview> generateWithVarietyStrategy(final List<Tea> teaList, final Application application) {
        final ArrayList<RecyclerItemOverview> recyclerItems = new ArrayList<>();
        String lastVariety = "";
        for (final Tea tea : teaList) {
            final String variety = Variety.convertStoredVarietyToText(tea.getVariety(), application);
            if (!variety.equals(lastVariety)) {
                recyclerItems.add(new RecyclerItemOverview(variety, null, null, null));
                lastVariety = variety;
            }
            recyclerItems.add(new RecyclerItemOverview(null, tea.getId(), tea.getName(), variety));
        }
        return recyclerItems;
    }

    static List<RecyclerItemOverview> generateWithRatingStrategy(final List<Tea> teaList, final Application application) {
        final ArrayList<RecyclerItemOverview> recyclerItems = new ArrayList<>();
        int lastRating = -1;
        for (final Tea tea : teaList) {
            final int rating = tea.getRating();
            if (rating != lastRating) {
                final String ratingHeader = application.getString(R.string.overview_sorting_header_star, rating);
                recyclerItems.add(new RecyclerItemOverview(ratingHeader, null, null, null));
                lastRating = rating;
            }
            final String variety = Variety.convertStoredVarietyToText(tea.getVariety(), application);
            recyclerItems.add(new RecyclerItemOverview(null, tea.getId(), tea.getName(), variety));
        }
        return recyclerItems;
    }

    static List<RecyclerItemOverview> generateWithDefaultStrategy(final List<Tea> teaList, final Application application) {
        final ArrayList<RecyclerItemOverview> recyclerItems = new ArrayList<>();
        for (final Tea tea : teaList) {
            final String variety = Variety.convertStoredVarietyToText(tea.getVariety(), application);
            recyclerItems.add(new RecyclerItemOverview(null, tea.getId(), tea.getName(), variety));
        }
        return recyclerItems;
    }
}
