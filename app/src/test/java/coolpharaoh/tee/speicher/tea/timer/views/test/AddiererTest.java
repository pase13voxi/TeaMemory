package coolpharaoh.tee.speicher.tea.timer.views.test;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AddiererTest {
    @Test
    public void testAdd() {
        Addierer test = new Addierer(4, 7);
        assertThat(test.add()).isEqualTo(11);

    }
}
