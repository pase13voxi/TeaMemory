package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety;

class RecyclerItemsHeaderStrategyDefault implements RecyclerItemsHeaderStrategy {

    private final Application application;

    RecyclerItemsHeaderStrategyDefault(final Application application) {
        this.application = application;
    }

    @Override
    public List<RecyclerItemOverview> generateFrom(final List<Tea> teaList) {

        final ArrayList<RecyclerItemOverview> recyclerItems = new ArrayList<>();
        for (final Tea tea : teaList) {
            final String variety = Variety.convertStoredVarietyToText(tea.getVariety(), application);
            recyclerItems.add(new RecyclerItemOverview(null, tea.getId(), tea.getName(), variety, tea.getColor(), tea.isInStock()));
        }
        return recyclerItems;
    }
}
