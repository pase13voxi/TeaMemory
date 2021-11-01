package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety;

public class RecyclerItemsHeaderStrategyAlphabetical implements RecyclerItemsHeaderStrategy {

    private final Application application;

    RecyclerItemsHeaderStrategyAlphabetical(final Application application) {
        this.application = application;
    }

    @Override
    public List<RecyclerItemOverview> generateFrom(final List<Tea> teaList) {

        final ArrayList<RecyclerItemOverview> recyclerItems = new ArrayList<>();
        String lastFirstLetter = "";
        for (final Tea tea : teaList) {
            final String firstLetter = tea.getName().substring(0, 1).toUpperCase();
            if (!lastFirstLetter.equals(firstLetter)) {
                recyclerItems.add(new RecyclerItemOverview("- " + firstLetter + " -", null, null, null, false));
                lastFirstLetter = firstLetter;
            }
            final String variety = Variety.convertStoredVarietyToText(tea.getVariety(), application);
            recyclerItems.add(new RecyclerItemOverview(null, tea.getId(), tea.getName(), variety, tea.isInStock()));
        }
        return recyclerItems;
    }
}
