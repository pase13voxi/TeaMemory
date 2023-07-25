package coolpharaoh.tee.speicher.tea.timer.core.tea

import coolpharaoh.tee.speicher.tea.timer.core.tea.ColorConversation.discoverForegroundColor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ColorConversationTest {
    @Test
    fun discoverForegroundColorLight() {
        val colorBlackTea = -15461296
        val colorForeground = discoverForegroundColor(colorBlackTea)
        assertThat(colorForeground).isEqualTo(-1)
    }

    @Test
    fun discoverForegroundColorDark() {
        val colorWhiteTea = -1642
        val colorForeground = discoverForegroundColor(colorWhiteTea)
        assertThat(colorForeground).isEqualTo(-16777216)
    }
}