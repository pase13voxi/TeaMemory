package coolpharaoh.tee.speicher.tea.timer.views.about;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import coolpharaoh.tee.speicher.tea.timer.BuildConfig;
import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.contact.Contact;
import coolpharaoh.tee.speicher.tea.timer.views.software.Software;
import coolpharaoh.tee.speicher.tea.timer.views.statistics.Statistics;
import coolpharaoh.tee.speicher.tea.timer.views.utils.ListRowItem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.Shadows.shadowOf;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class AboutTest {

    private ActivityScenario<About> aboutActivityScenario;

    @Before
    public void setUp() {
        aboutActivityScenario = ActivityScenario.launch(About.class);
    }

    @Test
    public void launchActivity() {
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

    @Test
    public void navigateToContact() {
        int positionContact = 0;

        aboutActivityScenario.onActivity(about -> {
            ListView aboutList = about.findViewById(R.id.listview_about);

            aboutList.performItemClick(aboutList, positionContact, aboutList.getItemIdAtPosition(positionContact));

            Intent expectedIntent = new Intent(about, Contact.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expectedIntent.getComponent());
        });
    }

    @Test
    public void navigateToRating() {
        int positionRating = 1;

        aboutActivityScenario.onActivity(about -> {
            ListView aboutList = about.findViewById(R.id.listview_about);

            aboutList.performItemClick(aboutList, positionRating, aboutList.getItemIdAtPosition(positionRating));

            Intent expectedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + about.getPackageName()));
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getData()).isEqualTo(expectedIntent.getData());
        });
    }

    @Test
    public void navigateToStatistics() {
        int positionStatistics = 2;

        aboutActivityScenario.onActivity(about -> {
            ListView aboutList = about.findViewById(R.id.listview_about);

            aboutList.performItemClick(aboutList, positionStatistics, aboutList.getItemIdAtPosition(positionStatistics));

            Intent expectedIntent = new Intent(about, Statistics.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expectedIntent.getComponent());
        });
    }

    @Test
    public void navigateToSoftware() {
        int positionSoftware = 3;

        aboutActivityScenario.onActivity(about -> {
            ListView aboutList = about.findViewById(R.id.listview_about);

            aboutList.performItemClick(aboutList, positionSoftware, aboutList.getItemIdAtPosition(positionSoftware));

            Intent expectedIntent = new Intent(about, Software.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expectedIntent.getComponent());
        });
    }

}
