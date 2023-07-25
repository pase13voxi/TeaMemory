package coolpharaoh.tee.speicher.tea.timer.core.settings

import coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode.Companion.fromChoice
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SortModeTest {
    @Test
    fun getIndexVariety() {
        val sortMode = SortMode.BY_VARIETY
        assertThat(sortMode.choice).isEqualTo(2)
    }

    @Test
    fun sortModeFromIndexTwo() {
        val sortMode = fromChoice(2)
        assertThat(sortMode).isEqualTo(SortMode.BY_VARIETY)
    }

    @Test
    fun temperatureUnitFromTextNotDefined() {
        val sortMode = fromChoice(5)
        assertThat(sortMode).isEqualTo(SortMode.LAST_USED)
    }
}