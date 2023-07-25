package coolpharaoh.tee.speicher.tea.timer.core.tea;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ColorConversationTest {

    @Test
    void discoverForegroundColorLight() {
        final int colorBlackTea = -15461296;
        final int colorForeground = ColorConversation.discoverForegroundColor(colorBlackTea);
        assertThat(colorForeground).isEqualTo(-1);
    }

    @Test
    void discoverForegroundColorDark() {
        final int colorWhiteTea = -1642;
        final int colorForeground = ColorConversation.discoverForegroundColor(colorWhiteTea);
        assertThat(colorForeground).isEqualTo(-16777216);
    }
}
