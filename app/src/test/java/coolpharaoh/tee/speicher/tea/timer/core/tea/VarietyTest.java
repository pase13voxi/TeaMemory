package coolpharaoh.tee.speicher.tea.timer.core.tea;

import android.app.Application;
import android.content.res.Resources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import coolpharaoh.tee.speicher.tea.timer.R;

import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.BLACK_TEA;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.GREEN_TEA;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.OOLONG_TEA;
import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.OTHER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VarietyTest {
    @Mock
    private Application application;
    @Mock
    private Resources resources;

    private String[] varieties;

    @Test
    public void getStoredTextFromGreenTea() {
        assertThat(GREEN_TEA.getCode()).isEqualTo("02_green");
    }

    @Test
    public void getColorFromGreenTea() {
        assertThat(GREEN_TEA.getColor()).isEqualTo(R.color.greentea);
    }

    @Test
    public void getChoiceFromGreenTea() {
        assertThat(GREEN_TEA.getChoice()).isEqualTo(1);
    }

    @Test
    public void varietyFromStoredTextGreenTea() {
        final Variety variety = Variety.fromStoredText(GREEN_TEA.getCode());

        assertThat(variety).isEqualTo(GREEN_TEA);
    }

    @Test
    public void varietyFromStoredTextNotDefined() {
        final String notDefinedText = "not defined";

        final Variety variety = Variety.fromStoredText(notDefinedText);

        assertThat(variety).isEqualTo(OTHER);
    }

    @Test
    public void varietyFromStoredTextNull() {
        final Variety variety = Variety.fromStoredText(null);

        assertThat(variety).isEqualTo(OTHER);
    }

    @Test
    public void varietyFromChoiceGreenTea() {
        final Variety variety = Variety.fromChoice(GREEN_TEA.getChoice());

        assertThat(variety).isEqualTo(GREEN_TEA);
    }

    @Test
    public void varietyFromChoiceMinusValue() {
        final Variety variety = Variety.fromChoice(-1);

        assertThat(variety).isEqualTo(OTHER);
    }

    @Test
    public void convertBlackTeaVarietyToCode() {
        mockVarietyStrings();
        String code = Variety.convertTextToStoredVariety(varieties[0], application);

        assertThat(code).isEqualTo(BLACK_TEA.getCode());

    }

    @Test
    public void convertOolongTeaVarietyToCode() {
        mockVarietyStrings();
        String code = Variety.convertTextToStoredVariety(varieties[4], application);

        assertThat(code).isEqualTo(OOLONG_TEA.getCode());

    }

    @Test
    public void convertVarietyToCodeReturnInputBecauseVarietyNotExist() {
        mockVarietyStrings();
        String otherVariety = "Other Variety";
        String code = Variety.convertTextToStoredVariety(otherVariety, application);

        assertThat(code).isEqualTo(otherVariety);
    }

    @Test
    public void convertBlackTeaCodeToVariety() {
        mockVarietyStrings();
        String variety = Variety.convertStoredVarietyToText(BLACK_TEA.getCode(), application);

        assertThat(variety).isEqualTo(varieties[0]);
    }

    @Test
    public void convertOolongTeaCodeToVariety() {
        mockVarietyStrings();
        String variety = Variety.convertStoredVarietyToText(OOLONG_TEA.getCode(), application);

        assertThat(variety).isEqualTo(varieties[4]);
    }

    @Test
    public void convertCodeToVarietyReturnInputBecauseCodeNotExist() {
        mockVarietyStrings();
        String otherCode = "Other Code";
        String variety = Variety.convertStoredVarietyToText(otherCode, application);

        assertThat(variety).isEqualTo(otherCode);
    }

    private void mockVarietyStrings() {
        when(application.getResources()).thenReturn(resources);
        varieties = new String[]{"Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
                "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"};
        when(resources.getStringArray(R.array.new_tea_variety_teas)).thenReturn(varieties);
    }
}
