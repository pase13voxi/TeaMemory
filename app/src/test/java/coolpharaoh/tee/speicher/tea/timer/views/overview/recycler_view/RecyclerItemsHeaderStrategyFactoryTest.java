package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RecyclerItemsHeaderStrategyFactoryTest {

    @Test
    void getRecyclerItemsHeaderStrategyLastUsed() {
        final RecyclerItemsHeaderStrategy strategy = RecyclerItemsHeaderStrategyFactory.getStrategy(0, null);
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyLastUsed.class);
    }

    @Test
    void getRecyclerItemsHeaderStrategyAlphabetical() {
        final RecyclerItemsHeaderStrategy strategy = RecyclerItemsHeaderStrategyFactory.getStrategy(1, null);
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyAlphabetical.class);
    }

    @Test
    void getRecyclerItemsHeaderStrategyVariety() {
        final RecyclerItemsHeaderStrategy strategy = RecyclerItemsHeaderStrategyFactory.getStrategy(2, null);
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyVariety.class);
    }

    @Test
    void getRecyclerItemsHeaderStrategyRating() {
        final RecyclerItemsHeaderStrategy strategy = RecyclerItemsHeaderStrategyFactory.getStrategy(3, null);
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyRating.class);
    }

    @Test
    void getRecyclerItemsHeaderStrategyDefault() {
        final RecyclerItemsHeaderStrategy strategy = RecyclerItemsHeaderStrategyFactory.getStrategy(-1, null);
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyDefault.class);
    }

}