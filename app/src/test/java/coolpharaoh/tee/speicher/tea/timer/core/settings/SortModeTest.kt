package coolpharaoh.tee.speicher.tea.timer.core.settings;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SortModeTest {

    @Test
    void getIndexVariety() {
        final SortMode sortMode = SortMode.BY_VARIETY;
        assertThat(sortMode.getChoice()).isEqualTo(2);
    }

    @Test
    void sortModeFromIndexTwo() {
        final SortMode sortMode = SortMode.fromChoice(2);
        assertThat(sortMode).isEqualTo(SortMode.BY_VARIETY);
    }

    @Test
    void temperatureUnitFromTextNotDefined() {
        final SortMode sortMode = SortMode.fromChoice(5);
        assertThat(sortMode).isEqualTo(SortMode.LAST_USED);
    }
}
