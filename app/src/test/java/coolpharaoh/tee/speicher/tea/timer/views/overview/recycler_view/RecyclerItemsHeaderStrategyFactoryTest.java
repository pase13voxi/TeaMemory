package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view;

import static org.assertj.core.api.Assertions.assertThat;

import android.app.Application;

import org.junit.jupiter.api.Test;

class RecyclerItemsHeaderStrategyFactoryTest {

    @Test
    void getRecyclerItemsHeaderStrategyLastUsed() {
        final RecyclerItemsHeaderStrategy strategy = RecyclerItemsHeaderStrategyFactory.getStrategy(0, new Application());
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyLastUsed.class);
    }

    @Test
    void getRecyclerItemsHeaderStrategyAlphabetical() {
        final RecyclerItemsHeaderStrategy strategy = RecyclerItemsHeaderStrategyFactory.getStrategy(1, new Application());
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyAlphabetical.class);
    }

    @Test
    void getRecyclerItemsHeaderStrategyVariety() {
        final RecyclerItemsHeaderStrategy strategy = RecyclerItemsHeaderStrategyFactory.getStrategy(2, new Application());
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyVariety.class);
    }

    @Test
    void getRecyclerItemsHeaderStrategyRating() {
        final RecyclerItemsHeaderStrategy strategy = RecyclerItemsHeaderStrategyFactory.getStrategy(3, new Application());
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyRating.class);
    }

    @Test
    void getRecyclerItemsHeaderStrategyDefault() {
        final RecyclerItemsHeaderStrategy strategy = RecyclerItemsHeaderStrategyFactory.getStrategy(-1, new Application());
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyDefault.class);
    }

}