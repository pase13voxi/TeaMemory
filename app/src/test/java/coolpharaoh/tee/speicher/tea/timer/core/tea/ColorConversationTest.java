package coolpharaoh.tee.speicher.tea.timer.core.tea;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ColorConversationTest {

    @Test
    public void discoverForgroundColorLight() {
        int colorBlackTea = -15461296;
        int colorForeground = ColorConversation.discoverForegroundColor(colorBlackTea);
        assertThat(colorForeground).isEqualTo(-1);
    }

    @Test
    public void discoverForgroundColorDark() {
        int colorWhiteTea = -1642;
        int colorForeground = ColorConversation.discoverForegroundColor(colorWhiteTea);
        assertThat(colorForeground).isEqualTo(-16777216);
    }
}
