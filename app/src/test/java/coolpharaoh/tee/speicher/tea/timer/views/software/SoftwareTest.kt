package coolpharaoh.tee.speicher.tea.timer.views.software;

import static android.os.Looper.getMainLooper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.Shadows.shadowOf;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import coolpharaoh.tee.speicher.tea.timer.R;

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
            final RecyclerView softwareRecyclerView = software.findViewById(R.id.recycler_view_software);

            assertThat(softwareRecyclerView.getAdapter().getItemCount()).isEqualTo(5);

            softwareRecyclerView.scrollToPosition(0);
            shadowOf(getMainLooper()).idle();
            checkHeaderAndPositionAtPositionInRecyclerView(softwareRecyclerView, 0,
                    software.getString(R.string.software_color_picker_heading), software.getString(R.string.software_color_picker_description));

            softwareRecyclerView.scrollToPosition(1);
            shadowOf(getMainLooper()).idle();
            checkHeaderAndPositionAtPositionInRecyclerView(softwareRecyclerView, 1,
                    software.getString(R.string.software_chart_heading), software.getString(R.string.software_chart_description));

            softwareRecyclerView.scrollToPosition(2);
            shadowOf(getMainLooper()).idle();
            checkHeaderAndPositionAtPositionInRecyclerView(softwareRecyclerView, 2,
                    software.getString(R.string.software_glide_heading), software.getString(R.string.software_glide_description));

            softwareRecyclerView.scrollToPosition(3);
            shadowOf(getMainLooper()).idle();
            checkHeaderAndPositionAtPositionInRecyclerView(softwareRecyclerView, 3,
                    software.getString(R.string.software_junit5_heading), software.getString(R.string.software_junit5_description));

            softwareRecyclerView.scrollToPosition(4);
            shadowOf(getMainLooper()).idle();
            checkHeaderAndPositionAtPositionInRecyclerView(softwareRecyclerView, 4,
                    software.getString(R.string.software_gson_heading), software.getString(R.string.software_gson_description));
        });
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
