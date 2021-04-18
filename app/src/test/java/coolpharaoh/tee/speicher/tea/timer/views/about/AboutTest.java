package coolpharaoh.tee.speicher.tea.timer.views.about;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
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
    public void expectAboutListAndVersion() {
        aboutActivityScenario.onActivity(about -> {
            final RecyclerView aboutRecyclerView = about.findViewById(R.id.recycler_view_about);

            assertThat(aboutRecyclerView.getAdapter().getItemCount()).isEqualTo(4);

            aboutRecyclerView.scrollToPosition(0);
            checkHeaderAndPositionAtPositionInRecyclerView(aboutRecyclerView, 0,
                    about.getString(R.string.about_contact_heading), about.getString(R.string.about_contact_description));

            aboutRecyclerView.scrollToPosition(1);
            checkHeaderAndPositionAtPositionInRecyclerView(aboutRecyclerView, 1,
                    about.getString(R.string.about_rating_heading), about.getString(R.string.about_rating_description));

            aboutRecyclerView.scrollToPosition(2);
            checkHeaderAndPositionAtPositionInRecyclerView(aboutRecyclerView, 2,
                    about.getString(R.string.about_statistics_heading), about.getString(R.string.about_statistics_description));

            aboutRecyclerView.scrollToPosition(3);
            checkHeaderAndPositionAtPositionInRecyclerView(aboutRecyclerView, 3,
                    about.getString(R.string.about_software_heading), about.getString(R.string.about_software_description));


            TextView textViewVersion = about.findViewById(R.id.text_view_about_version);
            assertThat(textViewVersion.getText()).isEqualTo(about.getString(R.string.about_version, BuildConfig.VERSION_NAME));
        });
    }

    @Test
    public void navigateToContact() {
        int positionContact = 0;

        aboutActivityScenario.onActivity(about -> {
            final RecyclerView aboutRecyclerView = about.findViewById(R.id.recycler_view_about);

            clickAtPositionRecyclerView(aboutRecyclerView, positionContact);

            Intent expected = new Intent(about, Contact.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void navigateToRating() {
        int positionRating = 1;

        aboutActivityScenario.onActivity(about -> {
            final RecyclerView aboutRecyclerView = about.findViewById(R.id.recycler_view_about);

            clickAtPositionRecyclerView(aboutRecyclerView, positionRating);

            Intent expected = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + about.getPackageName()));
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getData()).isEqualTo(expected.getData());
        });
    }

    @Test
    public void navigateToStatistics() {
        int positionStatistics = 2;

        aboutActivityScenario.onActivity(about -> {
            final RecyclerView aboutRecyclerView = about.findViewById(R.id.recycler_view_about);

            clickAtPositionRecyclerView(aboutRecyclerView, positionStatistics);

            Intent expected = new Intent(about, Statistics.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void navigateToSoftware() {
        int positionSoftware = 3;

        aboutActivityScenario.onActivity(about -> {
            final RecyclerView aboutRecyclerView = about.findViewById(R.id.recycler_view_about);

            clickAtPositionRecyclerView(aboutRecyclerView, positionSoftware);

            Intent expected = new Intent(about, Software.class);
            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    private void clickAtPositionRecyclerView(final RecyclerView recyclerView, final int position) {
        recyclerView.scrollToPosition(position);
        final View itemView = recyclerView.findViewHolderForAdapterPosition(position).itemView;
        itemView.performClick();
    }

    private void checkHeaderAndPositionAtPositionInRecyclerView(final RecyclerView recyclerView, final int position,
                                                                final String header, final String description) {
        final View itemView = recyclerView.findViewHolderForAdapterPosition(position).itemView;
        final TextView textViewHeading = itemView.findViewById(R.id.text_view_recycler_view_heading);
        assertThat(textViewHeading.getText()).hasToString(header);
        final TextView textViewDescription = itemView.findViewById(R.id.text_view_recycler_view_description);
        assertThat(textViewDescription.getText()).hasToString(description);
    }
}
