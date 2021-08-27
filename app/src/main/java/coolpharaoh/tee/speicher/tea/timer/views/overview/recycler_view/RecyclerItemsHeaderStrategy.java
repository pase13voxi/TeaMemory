package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;

public interface RecyclerItemsHeaderStrategy {

    public List<RecyclerItemOverview> generateFrom(final List<Tea> teaList);
}
