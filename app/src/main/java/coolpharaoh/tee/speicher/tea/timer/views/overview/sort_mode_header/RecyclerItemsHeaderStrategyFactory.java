package coolpharaoh.tee.speicher.tea.timer.views.overview.sort_mode_header;

import android.app.Application;

public class RecyclerItemsHeaderStrategyFactory {

    private RecyclerItemsHeaderStrategyFactory() {
    }

    public static RecyclerItemsHeaderStrategy getStrategy(final int sorting, final Application application) {
        switch (sorting) {
            case 0:
                return new RecyclerItemsHeaderStrategyLastUsed(application);
            case 1:
                return new RecyclerItemsHeaderStrategyAlphabetical(application);
            case 2:
                return new RecyclerItemsHeaderStrategyVariety(application);
            case 3:
                return new RecyclerItemsHeaderStrategyRating(application);
            default:
                return new RecyclerItemsHeaderStrategyDefault(application);
        }
    }
}
