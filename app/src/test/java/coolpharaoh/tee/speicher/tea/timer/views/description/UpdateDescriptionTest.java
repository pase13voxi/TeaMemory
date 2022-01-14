package coolpharaoh.tee.speicher.tea.timer.views.description;

import static org.assertj.core.api.Assertions.assertThat;

import android.widget.ImageButton;

import androidx.test.core.app.ActivityScenario;
import androidx.viewpager.widget.ViewPager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import coolpharaoh.tee.speicher.tea.timer.R;

@RunWith(RobolectricTestRunner.class)
public class UpdateDescriptionTest {

    @Test
    public void launchActivityExpectThreeImages() {
        final ActivityScenario<UpdateDescription> updateDescriptionActivityScenario = ActivityScenario.launch(UpdateDescription.class);
        updateDescriptionActivityScenario.onActivity(updateDescription -> {
            final ViewPager viewPager = updateDescription.findViewById(R.id.slide_view_description_pager);
            final SlideAdapter slideAdapter = (SlideAdapter) viewPager.getAdapter();
            assertThat(slideAdapter.getCount()).isEqualTo(4);
        });
    }

    @Test
    public void exitActivity() {
        final ActivityScenario<UpdateDescription> updateDescriptionActivityScenario = ActivityScenario.launch(UpdateDescription.class);
        updateDescriptionActivityScenario.onActivity(updateDescription -> {
            ImageButton buttonClose = updateDescription.findViewById(R.id.button_description_close);
            buttonClose.performClick();

            assertThat(updateDescription.isFinishing()).isTrue();
        });
    }
}
