package coolpharaoh.tee.speicher.tea.timer.views.description

import android.widget.ImageButton
import androidx.test.core.app.ActivityScenario
import androidx.viewpager.widget.ViewPager
import coolpharaoh.tee.speicher.tea.timer.R
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ShowTeaDescriptionTest {
    @Test
    fun launchActivityExpectThreeImages() {
        val showTeaDescriptionActivityScenario = ActivityScenario.launch(ShowTeaDescription::class.java)
        showTeaDescriptionActivityScenario.onActivity { showTeaDescription: ShowTeaDescription ->
            val viewPager = showTeaDescription.findViewById<ViewPager>(R.id.slide_view_description_pager)
            val slideAdapter = viewPager.adapter as SlideAdapter?
            Assertions.assertThat(slideAdapter!!.count).isEqualTo(4)
        }
    }

    @Test
    fun exitActivity() {
        val showTeaDescriptionActivityScenario = ActivityScenario.launch(ShowTeaDescription::class.java)
        showTeaDescriptionActivityScenario.onActivity { showTeaDescription: ShowTeaDescription ->
            val buttonClose = showTeaDescription.findViewById<ImageButton>(R.id.button_description_close)
            buttonClose.performClick()
            Assertions.assertThat(showTeaDescription.isFinishing).isTrue()
        }
    }
}