package coolpharaoh.tee.speicher.tea.timer.views.about;

import android.os.Build;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import coolpharaoh.tee.speicher.tea.timer.BuildConfig;
import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.utils.ListRowItem;

import static org.assertj.core.api.Assertions.assertThat;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class AboutTest {

    @Test
    public void launchActivity() {
        ActivityScenario<About> aboutActivityScenario = ActivityScenario.launch(About.class);

        aboutActivityScenario.onActivity(about -> {
            ListView aboutList = about.findViewById(R.id.listview_about);

            assertThat(aboutList.getAdapter().getCount()).isEqualTo(4);

            ListRowItem itemContact = (ListRowItem) aboutList.getAdapter().getItem(0);
            assertThat(itemContact.getHeading()).isEqualTo(about.getString(R.string.about_contact_heading));
            assertThat(itemContact.getDescription()).isEqualTo(about.getString(R.string.about_contact_description));

            ListRowItem itemRating = (ListRowItem) aboutList.getAdapter().getItem(1);
            assertThat(itemRating.getHeading()).isEqualTo(about.getString(R.string.about_rating_heading));
            assertThat(itemRating.getDescription()).isEqualTo(about.getString(R.string.about_rating_description));

            ListRowItem itemStatistics = (ListRowItem) aboutList.getAdapter().getItem(2);
            assertThat(itemStatistics.getHeading()).isEqualTo(about.getString(R.string.about_statistics_heading));
            assertThat(itemStatistics.getDescription()).isEqualTo(about.getString(R.string.about_statistics_description));

            ListRowItem itemSoftware = (ListRowItem) aboutList.getAdapter().getItem(3);
            assertThat(itemSoftware.getHeading()).isEqualTo(about.getString(R.string.about_software_heading));
            assertThat(itemSoftware.getDescription()).isEqualTo(about.getString(R.string.about_software_description));


            TextView textViewVersion = about.findViewById(R.id.textViewVersion);
            assertThat(textViewVersion.getText()).isEqualTo(about.getString(R.string.about_version, BuildConfig.VERSION_NAME));
        });
    }

}
