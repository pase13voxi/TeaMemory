package coolpharaoh.tee.speicher.tea.timer.views.overview.sort_mode_header;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.views.overview.RecyclerItemOverview;

public interface RecyclerItemsHeaderStrategy {

    public List<RecyclerItemOverview> generateFrom(final List<Tea> teaList);
}
