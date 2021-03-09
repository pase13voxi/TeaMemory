package coolpharaoh.tee.speicher.tea.timer.views.description;

import android.os.Build;
import android.widget.ImageButton;

import androidx.test.core.app.ActivityScenario;
import androidx.viewpager.widget.ViewPager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import coolpharaoh.tee.speicher.tea.timer.R;

import static org.assertj.core.api.Assertions.assertThat;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class UpdateDescriptionTest {

    @Test
    public void launchActivityExpectThreeImages() {
        ActivityScenario<UpdateDescription> updateDescriptionActivityScenario = ActivityScenario.launch(UpdateDescription.class);
        updateDescriptionActivityScenario.onActivity(updateDescription -> {
            final ViewPager viewPager = updateDescription.findViewById(R.id.slideViewPager);
            final SlideAdapter slideAdapter = (SlideAdapter) viewPager.getAdapter();
            assertThat(slideAdapter.getCount()).isEqualTo(2);
        });
    }

    @Test
    public void exitActivity() {
        ActivityScenario<UpdateDescription> updateDescriptionActivityScenario = ActivityScenario.launch(UpdateDescription.class);
        updateDescriptionActivityScenario.onActivity(updateDescription -> {
            ImageButton buttonClose = updateDescription.findViewById(R.id.buttonDescriptionClose);
            buttonClose.performClick();

            assertThat(updateDescription.isFinishing()).isTrue();
        });
    }
}
