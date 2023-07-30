package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view.RecyclerItemsHeaderStrategyFactory.getStrategy
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RecyclerItemsHeaderStrategyFactoryTest {
    @Test
    fun getRecyclerItemsHeaderStrategyLastUsed() {
        val strategy = getStrategy(0, Application())
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyLastUsed::class.java)
    }

    @Test
    fun getRecyclerItemsHeaderStrategyAlphabetical() {
        val strategy = getStrategy(1, Application())
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyAlphabetical::class.java)
    }

    @Test
    fun getRecyclerItemsHeaderStrategyVariety() {
        val strategy = getStrategy(2, Application())
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyVariety::class.java)
    }

    @Test
    fun getRecyclerItemsHeaderStrategyRating() {
        val strategy = getStrategy(3, Application())
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyRating::class.java)
    }

    @Test
    fun getRecyclerItemsHeaderStrategyDefault() {
        val strategy = getStrategy(-1, Application())
        assertThat(strategy).isInstanceOf(RecyclerItemsHeaderStrategyDefault::class.java)
    }
}