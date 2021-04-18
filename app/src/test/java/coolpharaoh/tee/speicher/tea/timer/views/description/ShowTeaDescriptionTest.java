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
public class ShowTeaDescriptionTest {

    @Test
    public void launchActivityExpectThreeImages() {
        ActivityScenario<ShowTeaDescription> showTeaDescriptionActivityScenario = ActivityScenario.launch(ShowTeaDescription.class);
        showTeaDescriptionActivityScenario.onActivity(showTeaDescription -> {
            final ViewPager viewPager = showTeaDescription.findViewById(R.id.slide_view_description_pager);
            final SlideAdapter slideAdapter = (SlideAdapter) viewPager.getAdapter();
            assertThat(slideAdapter.getCount()).isEqualTo(4);
        });
    }

    @Test
    public void exitActivity() {
        ActivityScenario<ShowTeaDescription> showTeaDescriptionActivityScenario = ActivityScenario.launch(ShowTeaDescription.class);
        showTeaDescriptionActivityScenario.onActivity(showTeaDescription -> {
            ImageButton buttonClose = showTeaDescription.findViewById(R.id.button_description_close);
            buttonClose.performClick();

            assertThat(showTeaDescription.isFinishing()).isTrue();
        });
    }
}
