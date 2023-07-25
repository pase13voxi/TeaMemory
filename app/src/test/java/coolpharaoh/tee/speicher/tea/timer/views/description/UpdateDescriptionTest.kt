package coolpharaoh.tee.speicher.tea.timer.views.description

import android.widget.ImageButton
import androidx.test.core.app.ActivityScenario
import androidx.viewpager.widget.ViewPager
import coolpharaoh.tee.speicher.tea.timer.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UpdateDescriptionTest {
    @Test
    fun launchActivityExpectOneImages() {
        val updateDescriptionActivityScenario = ActivityScenario.launch(UpdateDescription::class.java)
        updateDescriptionActivityScenario.onActivity { updateDescription: UpdateDescription ->
            val viewPager = updateDescription.findViewById<ViewPager>(R.id.slide_view_description_pager)
            val slideAdapter = viewPager.adapter as SlideAdapter?
            assertThat(slideAdapter!!.count).isEqualTo(2)
        }
    }

    @Test
    fun exitActivity() {
        val updateDescriptionActivityScenario = ActivityScenario.launch(UpdateDescription::class.java)
        updateDescriptionActivityScenario.onActivity { updateDescription: UpdateDescription ->
            val buttonClose = updateDescription.findViewById<ImageButton>(R.id.button_description_close)
            buttonClose.performClick()

            assertThat(updateDescription.isFinishing).isTrue()
        }
    }
}