package coolpharaoh.tee.speicher.tea.timer.core.tea;

import android.app.Application;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import coolpharaoh.tee.speicher.tea.timer.R;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ColorConversationTest {

    @Mock
    private Application application;
    @Mock
    private Resources resources;

    @Before
    public void setUp(){
        when(application.getResources()).thenReturn(resources);
    }

    @Test
    public void getVarietyColorBlackTea() {
        when(resources.getColor(R.color.blacktea)).thenReturn(0);

        int color = ColorConversation.getVarietyColor(0, application);

        assertThat(color).isEqualTo(0);
    }


    @Test
    public void getVarietyColorGreenTea() {
        when(resources.getColor(R.color.greentea)).thenReturn(1);

        int color = ColorConversation.getVarietyColor(1, application);

        assertThat(color).isEqualTo(1);
    }

    @Test
    public void getVarietyColorYellowTea() {
        when(resources.getColor(R.color.yellowtea)).thenReturn(2);

        int color = ColorConversation.getVarietyColor(2, application);

        assertThat(color).isEqualTo(2);
    }

    @Test
    public void getVarietyColorWhiteTea() {
        when(resources.getColor(R.color.whitetea)).thenReturn(3);

        int color = ColorConversation.getVarietyColor(3, application);

        assertThat(color).isEqualTo(3);
    }

    @Test
    public void getVarietyColorOolongTea() {
        when(resources.getColor(R.color.oolongtea)).thenReturn(4);

        int color = ColorConversation.getVarietyColor(4, application);

        assertThat(color).isEqualTo(4);
    }

    @Test
    public void getVarietyColorPuerhTea() {
        when(resources.getColor(R.color.puerhtea)).thenReturn(5);

        int color = ColorConversation.getVarietyColor(5, application);

        assertThat(color).isEqualTo(5);
    }

    @Test
    public void getVarietyColorHerbalTea() {
        when(resources.getColor(R.color.herbaltea)).thenReturn(6);

        int color = ColorConversation.getVarietyColor(6, application);

        assertThat(color).isEqualTo(6);
    }

    @Test
    public void getVarietyColorFruitTea() {
        when(resources.getColor(R.color.fruittea)).thenReturn(7);

        int color = ColorConversation.getVarietyColor(7, application);

        assertThat(color).isEqualTo(7);
    }

    @Test
    public void getVarietyColorRooibusTea() {
        when(resources.getColor(R.color.rooibustea)).thenReturn(8);

        int color = ColorConversation.getVarietyColor(8, application);

        assertThat(color).isEqualTo(8);
    }

    @Test
    public void getVarietyColorOther() {
        when(resources.getColor(R.color.other)).thenReturn(-1);

        int color = ColorConversation.getVarietyColor(-1, application);

        assertThat(color).isEqualTo(-1);
    }

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
