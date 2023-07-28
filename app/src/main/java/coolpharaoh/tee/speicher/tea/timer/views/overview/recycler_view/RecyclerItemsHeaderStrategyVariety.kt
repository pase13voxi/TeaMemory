package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety;

class RecyclerItemsHeaderStrategyVariety implements RecyclerItemsHeaderStrategy {

    private final Application application;

    RecyclerItemsHeaderStrategyVariety(final Application application) {
        this.application = application;
    }

    @Override
    public List<RecyclerItemOverview> generateFrom(final List<Tea> teaList) {

        final ArrayList<RecyclerItemOverview> recyclerItems = new ArrayList<>();
        String lastVariety = "";
        for (final Tea tea : teaList) {
            final String variety = Variety.convertStoredVarietyToText(tea.getVariety(), application);
            if (!variety.equals(lastVariety)) {
                recyclerItems.add(new RecyclerItemOverview("- " + variety + " -", null, null, null, null, false));
                lastVariety = variety;
            }
            recyclerItems.add(new RecyclerItemOverview(null, tea.getId(), tea.getName(), variety, tea.getColor(), tea.getInStock()));
        }
        return recyclerItems;
    }
}
