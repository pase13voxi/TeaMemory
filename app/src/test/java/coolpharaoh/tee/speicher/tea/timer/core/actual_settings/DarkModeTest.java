package coolpharaoh.tee.speicher.tea.timer.core.actual_settings;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DarkModeTest {

    @Test
    public void getTextSystem() {
        final DarkMode darkMode = DarkMode.ENABLED;
        assertThat(darkMode.getText()).isEqualTo("enabled");
    }

    @Test
    public void getChoiceSystem() {
        final DarkMode darkMode = DarkMode.ENABLED;
        assertThat(darkMode.getChoice()).isOne();
    }

    @Test
    public void darkModeFromTextSystem() {
        final DarkMode darkMode = DarkMode.fromText("enabled");
        assertThat(darkMode).isEqualTo(DarkMode.ENABLED);
    }

    @Test
    public void darkModeFromTextNotDefined() {
        final DarkMode darkMode = DarkMode.fromText("not defined");
        assertThat(darkMode).isEqualTo(DarkMode.SYSTEM);
    }

    @Test
    public void darkModeFromTextNull() {
        final DarkMode darkMode = DarkMode.fromText(null);
        assertThat(darkMode).isEqualTo(DarkMode.SYSTEM);
    }

    @Test
    public void darkModeFromChoiceZero() {
        final DarkMode darkMode = DarkMode.fromChoice(1);
        assertThat(darkMode).isEqualTo(DarkMode.ENABLED);
    }

    @Test
    public void darkModeFromChoiceMinusOne() {
        final DarkMode darkMode = DarkMode.fromChoice(-1);
        assertThat(darkMode).isEqualTo(DarkMode.SYSTEM);
    }
}
