package coolpharaoh.tee.speicher.tea.timer.views.software;

import android.os.Build;
import android.widget.ListView;

import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.utils.ListRowItem;

import static org.assertj.core.api.Assertions.assertThat;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class SoftwareTest {

    private ActivityScenario<Software> softwareActivityScenario;

    @Before
    public void setUp() {
        softwareActivityScenario = ActivityScenario.launch(Software.class);
    }

    @Test
    public void expectSoftwareList() {
        softwareActivityScenario.onActivity(software -> {
            ListView aboutList = software.findViewById(R.id.listview_software);

            assertThat(aboutList.getAdapter().getCount()).isEqualTo(2);

            ListRowItem itemPicker = (ListRowItem) aboutList.getAdapter().getItem(0);
            assertThat(itemPicker.getHeading()).isEqualTo(software.getString(R.string.software_colorpicker_heading));
            assertThat(itemPicker.getDescription()).isEqualTo(software.getString(R.string.software_colorpicker_description));

            ListRowItem itemStatistic = (ListRowItem) aboutList.getAdapter().getItem(1);
            assertThat(itemStatistic.getHeading()).isEqualTo(software.getString(R.string.software_statistic_heading));
            assertThat(itemStatistic.getDescription()).isEqualTo(software.getString(R.string.software_statistic_description));
        });
    }
}
