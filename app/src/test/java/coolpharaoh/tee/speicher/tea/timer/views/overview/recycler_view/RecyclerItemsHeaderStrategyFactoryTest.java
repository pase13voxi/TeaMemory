package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class RecyclerItemsHeaderStrategyFactoryTest {

    @Test
    public void getRecyclerItemsHeaderStrategyLastUsed() {
        final RecyclerItemsHeaderStrategy strategy = RecyclerItemsHeaderStrategyFactory.getStrategy(0, null);
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyLastUsed.class);
    }

    @Test
    public void getRecyclerItemsHeaderStrategyAlphabetical() {
        final RecyclerItemsHeaderStrategy strategy = RecyclerItemsHeaderStrategyFactory.getStrategy(1, null);
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyAlphabetical.class);
    }

    @Test
    public void getRecyclerItemsHeaderStrategyVariety() {
        final RecyclerItemsHeaderStrategy strategy = RecyclerItemsHeaderStrategyFactory.getStrategy(2, null);
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyVariety.class);
    }

    @Test
    public void getRecyclerItemsHeaderStrategyRating() {
        final RecyclerItemsHeaderStrategy strategy = RecyclerItemsHeaderStrategyFactory.getStrategy(3, null);
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyRating.class);
    }

    @Test
    public void getRecyclerItemsHeaderStrategyDefault() {
        final RecyclerItemsHeaderStrategy strategy = RecyclerItemsHeaderStrategyFactory.getStrategy(-1, null);
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyDefault.class);
    }

}