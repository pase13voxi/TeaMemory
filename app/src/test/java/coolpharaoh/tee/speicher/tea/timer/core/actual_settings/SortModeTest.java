package coolpharaoh.tee.speicher.tea.timer.core.actual_settings;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SortModeTest {

    @Test
    public void getIndexVariety() {
        final SortMode sortMode = SortMode.BY_VARIETY;
        assertThat(sortMode.getChoice()).isEqualTo(2);
    }

    @Test
    public void sortModeFromIndexTwo() {
        final SortMode sortMode = SortMode.fromChoice(2);
        assertThat(sortMode).isEqualTo(SortMode.BY_VARIETY);
    }

    @Test
    public void temperatureUnitFromTextNotDefined() {
        final SortMode sortMode = SortMode.fromChoice(5);
        assertThat(sortMode).isEqualTo(SortMode.LAST_USED);
    }
}
