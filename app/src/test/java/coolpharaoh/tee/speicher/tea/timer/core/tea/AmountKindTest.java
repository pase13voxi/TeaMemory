package coolpharaoh.tee.speicher.tea.timer.core.tea;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AmountKindTest {

    @Test
    public void getTextGram() {
        final AmountKind amountKind = AmountKind.GRAM;
        assertThat(amountKind.getText()).isEqualTo("Gr");
    }

    @Test
    public void getChoiceGram() {
        final AmountKind amountKind = AmountKind.TEA_SPOON;
        assertThat(amountKind.getChoice()).isZero();
    }

    @Test
    public void amountKindFromTextGram() {
        final AmountKind amountKind = AmountKind.fromText("Gr");
        assertThat(amountKind).isEqualTo(AmountKind.GRAM);
    }

    @Test
    public void amountKindFromTextNotDefined() {
        final AmountKind amountKind = AmountKind.fromText("not defined");
        assertThat(amountKind).isEqualTo(AmountKind.TEA_SPOON);
    }

    @Test
    public void amountKindFromTextNull() {
        final AmountKind amountKind = AmountKind.fromText(null);
        assertThat(amountKind).isEqualTo(AmountKind.TEA_SPOON);
    }

    @Test
    public void amountKindFromChoiceZero() {
        final AmountKind amountKind = AmountKind.fromChoice(1);
        assertThat(amountKind).isEqualTo(AmountKind.GRAM);
    }

    @Test
    public void amountKindFromChoiceMinusOne() {
        final AmountKind amountKind = AmountKind.fromChoice(-1);
        assertThat(amountKind).isEqualTo(AmountKind.TEA_SPOON);
    }
}
