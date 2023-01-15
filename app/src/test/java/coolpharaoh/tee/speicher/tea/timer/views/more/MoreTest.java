package coolpharaoh.tee.speicher.tea.timer.views.more;

import static android.os.Looper.getMainLooper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.Shadows.shadowOf;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import coolpharaoh.tee.speicher.tea.timer.BuildConfig;
import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.contact.Contact;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.ExportImport;
import coolpharaoh.tee.speicher.tea.timer.views.software.Software;
import coolpharaoh.tee.speicher.tea.timer.views.statistics.Statistics;

@RunWith(RobolectricTestRunner.class)
public class MoreTest {

    private ActivityScenario<More> moreActivityScenario;

    @Before
    public void setUp() {
        moreActivityScenario = ActivityScenario.launch(More.class);
    }

    @Test
    public void expectMoreListAndVersion() {
        moreActivityScenario.onActivity(more -> {
            final RecyclerView moreRecyclerView = more.findViewById(R.id.recycler_view_more);

            assertThat(moreRecyclerView.getAdapter().getItemCount()).isEqualTo(6);

            moreRecyclerView.scrollToPosition(0);
            shadowOf(getMainLooper()).idle();
            checkHeaderAndPositionAtPositionInRecyclerView(moreRecyclerView, 0,
                    more.getString(R.string.more_contact_heading), more.getString(R.string.more_contact_description));

            moreRecyclerView.scrollToPosition(1);
            shadowOf(getMainLooper()).idle();
            checkHeaderAndPositionAtPositionInRecyclerView(moreRecyclerView, 1,
                    more.getString(R.string.more_rating_heading), more.getString(R.string.more_rating_description));

            moreRecyclerView.scrollToPosition(2);
            shadowOf(getMainLooper()).idle();
            checkHeaderAndPositionAtPositionInRecyclerView(moreRecyclerView, 2,
                    more.getString(R.string.more_statistics_heading), more.getString(R.string.more_statistics_description));

            moreRecyclerView.scrollToPosition(3);
            shadowOf(getMainLooper()).idle();
            checkHeaderAndPositionAtPositionInRecyclerView(moreRecyclerView, 3,
                    more.getString(R.string.more_export_import_heading), more.getString(R.string.more_export_import_description));

            moreRecyclerView.scrollToPosition(4);
            shadowOf(getMainLooper()).idle();
            checkHeaderAndPositionAtPositionInRecyclerView(moreRecyclerView, 4,
                    more.getString(R.string.more_software_heading), more.getString(R.string.more_software_description));

            moreRecyclerView.scrollToPosition(5);
            shadowOf(getMainLooper()).idle();
            checkHeaderAndPositionAtPositionInRecyclerView(moreRecyclerView, 5,
                    more.getString(R.string.more_privacy_heading), more.getString(R.string.more_privacy_description));


            final TextView textViewVersion = more.findViewById(R.id.text_view_more_version);
            assertThat(textViewVersion.getText()).isEqualTo(more.getString(R.string.more_version, BuildConfig.VERSION_NAME));
        });
    }

    @Test
    public void navigateToContact() {
        final int positionContact = 0;

        moreActivityScenario.onActivity(more -> {
            final RecyclerView moreRecyclerView = more.findViewById(R.id.recycler_view_more);

            clickAtPositionRecyclerView(moreRecyclerView, positionContact);

            final Intent expected = new Intent(more, Contact.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void navigateToRating() {
        final int positionRating = 1;

        moreActivityScenario.onActivity(more -> {
            final RecyclerView moreRecyclerView = more.findViewById(R.id.recycler_view_more);

            clickAtPositionRecyclerView(moreRecyclerView, positionRating);

            final Intent expected = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + more.getPackageName()));
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getData()).isEqualTo(expected.getData());
        });
    }

    @Test
    public void navigateToStatistics() {
        final int positionStatistics = 2;

        moreActivityScenario.onActivity(more -> {
            final RecyclerView moreRecyclerView = more.findViewById(R.id.recycler_view_more);

            clickAtPositionRecyclerView(moreRecyclerView, positionStatistics);

            final Intent expected = new Intent(more, Statistics.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void navigateToExportImport() {
        final int positionExportImport = 3;

        moreActivityScenario.onActivity(more -> {
            final RecyclerView moreRecyclerView = more.findViewById(R.id.recycler_view_more);

            clickAtPositionRecyclerView(moreRecyclerView, positionExportImport);

            final Intent expected = new Intent(more, ExportImport.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void navigateToSoftware() {
        final int positionSoftware = 4;

        moreActivityScenario.onActivity(more -> {
            final RecyclerView moreRecyclerView = more.findViewById(R.id.recycler_view_more);

            clickAtPositionRecyclerView(moreRecyclerView, positionSoftware);

            final Intent expected = new Intent(more, Software.class);
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getComponent()).isEqualTo(expected.getComponent());
        });
    }

    @Test
    public void navigateToPrivacyPolicy() {
        final int positionPrivacy = 5;

        moreActivityScenario.onActivity(more -> {
            final RecyclerView moreRecyclerView = more.findViewById(R.id.recycler_view_more);

            clickAtPositionRecyclerView(moreRecyclerView, positionPrivacy);

            final Intent expected = new Intent(Intent.ACTION_VIEW, Uri.parse(more.getString(R.string.more_privacy_policy_url)));
            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getData()).isEqualTo(expected.getData());
        });
    }

    private void clickAtPositionRecyclerView(final RecyclerView recyclerView, final int position) {
        recyclerView.scrollToPosition(position);
        shadowOf(getMainLooper()).idle();
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
