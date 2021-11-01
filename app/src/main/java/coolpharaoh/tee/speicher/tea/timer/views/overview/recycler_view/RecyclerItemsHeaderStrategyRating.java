package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety;

class RecyclerItemsHeaderStrategyRating implements RecyclerItemsHeaderStrategy {

    private final Application application;

    RecyclerItemsHeaderStrategyRating(final Application application) {
        this.application = application;
    }

    @Override
    public List<RecyclerItemOverview> generateFrom(final List<Tea> teaList) {

        final ArrayList<RecyclerItemOverview> recyclerItems = new ArrayList<>();
        int lastRating = -1;
        for (final Tea tea : teaList) {
            final int rating = tea.getRating();
            if (rating != lastRating) {
                final String ratingHeader = application.getString(R.string.overview_sort_header_star, rating);
                recyclerItems.add(new RecyclerItemOverview("- " + ratingHeader + " -", null, null, null, false));
                lastRating = rating;
            }
            final String variety = Variety.convertStoredVarietyToText(tea.getVariety(), application);
            recyclerItems.add(new RecyclerItemOverview(null, tea.getId(), tea.getName(), variety, tea.isInStock()));
        }
        return recyclerItems;
    }
}
