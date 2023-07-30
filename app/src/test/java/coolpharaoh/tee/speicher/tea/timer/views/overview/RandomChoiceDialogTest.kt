package coolpharaoh.tee.speicher.tea.timer.views.overview

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.test.core.app.ApplicationProvider
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.getDate
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.convertStoredVarietyToText
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowAlertDialog
import java.util.function.Function

@RunWith(RobolectricTestRunner::class)
class RandomChoiceDialogTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    lateinit var overviewViewModel: OverviewViewModel
    @RelaxedMockK
    lateinit var imageController: ImageController
    @InjectMockKs
    lateinit var dialogFragment: RandomChoiceDialog

    private var fragmentManager: FragmentManager? = null

    @Before
    fun setUp() {
        val activity = Robolectric.buildActivity(FragmentActivity::class.java).create().start().resume().get()
        fragmentManager = activity.supportFragmentManager
    }

    @Test
    fun showDialogAndExpectFilledViewWithoutImage() {
        every { imageController.getImageUriByTeaId(any()) } returns null

        val tea = Tea("TeaName", Variety.BLACK_TEA.code, 1.0, AmountKind.TEA_SPOON.text, 1, 1, getDate())
        tea.id = 1L
        every { overviewViewModel.randomTeaInStock } returns tea

        dialogFragment.show(fragmentManager!!, RandomChoiceDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val textViewTeaName = dialog.findViewById<TextView>(R.id.text_view_random_choice_dialog_tea_name)
        assertThat(textViewTeaName.text).isEqualTo(tea.name)

        val textViewVariety = dialog.findViewById<TextView>(R.id.text_view_random_choice_dialog_variety)
        assertThat(textViewVariety.text).isEqualTo(convertStoredVarietyToText(tea.variety, ApplicationProvider.getApplicationContext()))
        val textViewImage = dialog.findViewById<TextView>(R.id.text_view_random_choice_dialog_image)
        assertThat(textViewImage)
            .extracting(Function<TextView, Any> { obj: TextView -> obj.visibility }, Function<TextView, Any> { obj: TextView -> obj.text })
            .contains(View.VISIBLE, "T")

        val imageViewImage = dialog.findViewById<ImageView>(R.id.image_view_random_tea_choice_image)
        assertThat(imageViewImage.tag).isNull()

        val textViewNoTea = dialog.findViewById<TextView>(R.id.text_view_random_choice_no_tea)
        assertThat(textViewNoTea.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun showDialogAndExpectImage() {
        val uri = "image/uri"
        every { imageController.getImageUriByTeaId(1L) } returns Uri.parse(uri)

        val tea = Tea("TeaName", Variety.BLACK_TEA.code, 1.0, AmountKind.TEA_SPOON.text, 1, 1, getDate())
        tea.id = 1L
        every { overviewViewModel.randomTeaInStock } returns tea

        dialogFragment.show(fragmentManager!!, RandomChoiceDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val textViewImage = dialog.findViewById<TextView>(R.id.text_view_random_choice_dialog_image)
        assertThat(textViewImage.visibility).isEqualTo(View.INVISIBLE)

        val imageViewImage = dialog.findViewById<ImageView>(R.id.image_view_random_tea_choice_image)
        assertThat(imageViewImage.tag).isEqualTo(uri)
    }

    @Test
    fun refreshDialogAndExpectOtherTea() {
        val teaName1 = "TeaName 1"
        val teaName2 = "TeaName 2"
        val tea = Tea()
        tea.id = 1L
        tea.name = teaName1
        every { overviewViewModel.randomTeaInStock } returns tea

        dialogFragment.show(fragmentManager!!, RandomChoiceDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val textViewTeaName = dialog.findViewById<TextView>(R.id.text_view_random_choice_dialog_tea_name)
        assertThat(textViewTeaName.text).isEqualTo(teaName1)

        tea.name = teaName2

        val buttonRefresh = dialog.findViewById<ImageButton>(R.id.button_random_choice_dialog_refresh)
        buttonRefresh.performClick()

        assertThat(textViewTeaName.text).isEqualTo(teaName2)
    }

    @Test
    fun whenNoRandomChoiceIsAvailableShowMessage() {
        every { overviewViewModel.randomTeaInStock } returns null

        dialogFragment.show(fragmentManager!!, RandomChoiceDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()
        val textViewNoTea = dialog.findViewById<TextView>(R.id.text_view_random_choice_no_tea)
        assertThat(textViewNoTea.visibility).isEqualTo(View.VISIBLE)

        val layoutTeaAvailable = dialog.findViewById<RelativeLayout>(R.id.layout_random_choice_tea_available)
        assertThat(layoutTeaAvailable.visibility).isEqualTo(View.GONE)

        val textViewHint = dialog.findViewById<TextView>(R.id.text_view_random_choice_hint)
        assertThat(textViewHint.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun chooseRandomTeaAndExpectShowTeaActivity() {
        val tea = Tea("TeaName", Variety.BLACK_TEA.code, 1.0, AmountKind.TEA_SPOON.text, 1, 1, getDate())
        tea.id = 1L
        every { overviewViewModel.randomTeaInStock } returns tea

        dialogFragment.show(fragmentManager!!, RandomChoiceDialog.TAG)
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val dialog = ShadowAlertDialog.getLatestAlertDialog()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        val expected = Intent(ApplicationProvider.getApplicationContext(), ShowTea::class.java)
        val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

        assertThat(actual.component).isEqualTo(expected.component)
        assertThat(actual.extras!!["teaId"]).isEqualTo(tea.id as Long)
    }
}