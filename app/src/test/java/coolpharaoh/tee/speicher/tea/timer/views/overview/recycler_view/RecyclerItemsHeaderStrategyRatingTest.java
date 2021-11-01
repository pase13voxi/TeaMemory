package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;

@RunWith(MockitoJUnitRunner.class)
public class RecyclerItemsHeaderStrategyRatingTest {
    private static final String[] VARIETIES = {"Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea", "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"};

    @Mock
    Application application;
    @Mock
    Resources resources;

    @Before
    public void setUp() throws Exception {
        when(resources.getStringArray(R.array.new_tea_variety_teas)).thenReturn(VARIETIES);
        when(application.getString(eq(R.string.overview_sort_header_star), any())).thenReturn("3 \u2605");
        when(application.getResources()).thenReturn(resources);
    }

    @Test
    public void generateRecyclerItemsHeader() {
        final ArrayList<Tea> teas = createTeas();

        final RecyclerItemsHeaderStrategy recyclerItemsHeader = new RecyclerItemsHeaderStrategyRating(application);
        final List<RecyclerItemOverview> recyclerItems = recyclerItemsHeader.generateFrom(teas);

        assertThat(recyclerItems)
                .extracting(
                        RecyclerItemOverview::getTeaId,
                        RecyclerItemOverview::getTeaName,
                        RecyclerItemOverview::getVariety,
                        RecyclerItemOverview::isFavorite,
                        RecyclerItemOverview::getCategory
                ).contains(
                tuple(null, null, null, false, "- 3 \u2605 -"),
                tuple(teas.get(0).getId(), teas.get(0).getName(), teas.get(0).getVariety(), true, null),
                tuple(null, null, null, false, "- 3 \u2605 -"),
                tuple(teas.get(1).getId(), teas.get(1).getName(), teas.get(1).getVariety(), true, null),
                tuple(null, null, null, false, "- 3 \u2605 -"),
                tuple(teas.get(2).getId(), teas.get(2).getName(), teas.get(2).getVariety(), true, null)
        );
    }

    private ArrayList<Tea> createTeas() {
        final ArrayList<Tea> teas = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final Tea tea = new Tea();
            tea.setId((long) i);
            tea.setName("TEA" + i + 1);
            tea.setVariety("VARIETY" + i + 1);
            tea.setInStock(true);
            tea.setRating(i + 1);
            teas.add(tea);
        }
        return teas;
    }
}