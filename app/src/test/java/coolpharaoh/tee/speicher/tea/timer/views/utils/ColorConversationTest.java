package coolpharaoh.tee.speicher.tea.timer.views.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import coolpharaoh.tee.speicher.tea.timer.R;

import static org.assertj.core.api.Assertions.assertThat;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class ColorConversationTest {
    public static final Context CONTEXT = ApplicationProvider.getApplicationContext();

    @Test
    public void getVarietyColorBlackTea() {
        int color = ColorConversation.getVarietyColor(0, CONTEXT);

        assertThat(color).isEqualTo(CONTEXT.getResources().getColor(R.color.blacktea));
    }


    @Test
    public void getVarietyColorGreenTea() {
        int color = ColorConversation.getVarietyColor(1, CONTEXT);

        assertThat(color).isEqualTo(CONTEXT.getResources().getColor(R.color.greentea));
    }

    @Test
    public void getVarietyColorYellowTea() {
        int color = ColorConversation.getVarietyColor(2, CONTEXT);

        assertThat(color).isEqualTo(CONTEXT.getResources().getColor(R.color.yellowtea));
    }

    @Test
    public void getVarietyColorWhiteTea() {
        int color = ColorConversation.getVarietyColor(3, CONTEXT);

        assertThat(color).isEqualTo(CONTEXT.getResources().getColor(R.color.whitetea));
    }

    @Test
    public void getVarietyColorOolongTea() {
        int color = ColorConversation.getVarietyColor(4, CONTEXT);

        assertThat(color).isEqualTo(CONTEXT.getResources().getColor(R.color.oolongtea));
    }

    @Test
    public void getVarietyColorPuerhTea() {
        int color = ColorConversation.getVarietyColor(5, CONTEXT);

        assertThat(color).isEqualTo(CONTEXT.getResources().getColor(R.color.puerhtea));
    }

    @Test
    public void getVarietyColorHerbalTea() {
        int color = ColorConversation.getVarietyColor(6, CONTEXT);

        assertThat(color).isEqualTo(CONTEXT.getResources().getColor(R.color.herbaltea));
    }

    @Test
    public void getVarietyColorFruitTea() {
        int color = ColorConversation.getVarietyColor(7, CONTEXT);

        assertThat(color).isEqualTo(CONTEXT.getResources().getColor(R.color.fruittea));
    }

    @Test
    public void getVarietyColorRooibusTea() {
        int color = ColorConversation.getVarietyColor(8, CONTEXT);

        assertThat(color).isEqualTo(CONTEXT.getResources().getColor(R.color.rooibustea));
    }

    @Test
    public void getVarietyColorOther() {
        int color = ColorConversation.getVarietyColor(-1, CONTEXT);

        assertThat(color).isEqualTo(CONTEXT.getResources().getColor(R.color.other));
    }

    @Test
    public void discoverForgroundColorLight() {
        int colorForeground = ColorConversation.discoverForegroundColor(CONTEXT.getResources().getColor(R.color.blacktea));
        assertThat(colorForeground).isEqualTo(Color.parseColor("#FFFFFFFF"));
    }

    @Test
    public void discoverForgroundColorDark() {
        int colorForeground = ColorConversation.discoverForegroundColor(CONTEXT.getResources().getColor(R.color.whitetea));
        assertThat(colorForeground).isEqualTo(Color.parseColor("#FF000000"));
    }
}
