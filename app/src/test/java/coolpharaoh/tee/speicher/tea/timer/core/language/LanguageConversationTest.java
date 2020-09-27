package coolpharaoh.tee.speicher.tea.timer.core.language;

import android.app.Application;
import android.content.res.Resources;
import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import coolpharaoh.tee.speicher.tea.timer.R;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LanguageConversationTest {
    @Mock
    private Application application;
    @Mock
    private Resources resources;

    private String[] codes;
    private String[] varieties;

    @Before
    public void setUp() {
        mockVarietyStrings();
    }

    private void mockVarietyStrings(){
        when(application.getResources()).thenReturn(resources);
        codes = new String[]{"01_black", "02_green", "03_yellow", "04_white", "05_oolong",
                "06_pu", "07_herbal", "08_fruit", "09_rooibus", "10_other"};
        when(resources.getStringArray(R.array.variety_codes)).thenReturn(codes);
        varieties = new String[]{"Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
                "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"};
        when(resources.getStringArray(R.array.variety_teas)).thenReturn(varieties);
    }

    @Test
    public void convertBlackTeaVarietyToCode() {
        String code = LanguageConversation.convertVarietyToCode(varieties[0], application);

        assertThat(code).isEqualTo(codes[0]);

    }

    @Test
    public void convertOolongTeaVarietyToCode() {
        String code = LanguageConversation.convertVarietyToCode(varieties[4], application);

        assertThat(code).isEqualTo(codes[4]);

    }

    @Test
    public void convertVarietyToCodeReturnInputBecauseVarietyNotExist() {
        String otherVariety = "Other Variety";
        String code = LanguageConversation.convertVarietyToCode(otherVariety, application);

        assertThat(code).isEqualTo(otherVariety);
    }

    @Test
    public void convertBlackTeaCodeToVariety() {
        String variety = LanguageConversation.convertCodeToVariety(codes[0], application);

        assertThat(variety).isEqualTo(varieties[0]);
    }

    @Test
    public void convertOolongTeaCodeToVariety() {
        String variety = LanguageConversation.convertCodeToVariety(codes[4], application);

        assertThat(variety).isEqualTo(varieties[4]);
    }

    @Test
    public void convertCodeToVarietyReturnInputBecauseCodeNotExist() {
        String otherCode = "Other Code";
        String variety = LanguageConversation.convertCodeToVariety(otherCode, application);

        assertThat(variety).isEqualTo(otherCode);
    }
}
