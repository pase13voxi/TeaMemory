package coolpharaoh.tee.speicher.tea.timer.views.overview

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.view.Menu
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.getDate
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings
import coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk.setFixedSystem
import coolpharaoh.tee.speicher.tea.timer.core.system.SystemUtility
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase.Companion.setMockedDatabase
import coolpharaoh.tee.speicher.tea.timer.views.description.UpdateDescription
import coolpharaoh.tee.speicher.tea.timer.views.more.More
import coolpharaoh.tee.speicher.tea.timer.views.new_tea.NewTea
import coolpharaoh.tee.speicher.tea.timer.views.settings.Settings
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageControllerFactory.setMockedImageController
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.fakes.RoboMenuItem
import org.robolectric.shadows.ShadowAlertDialog
import org.robolectric.shadows.ShadowPopupMenu

@RunWith(RobolectricTestRunner::class)
class OverviewTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    lateinit var teaMemoryDatabase: TeaMemoryDatabase
    @RelaxedMockK
    lateinit var teaDao: TeaDao
    @MockK
    lateinit var infusionDao: InfusionDao
    @RelaxedMockK
    lateinit var imageController: ImageController
    @MockK
    lateinit var systemUtility: SystemUtility

    @Before
    fun setUp() {
        mockDB()
        mockSystemVersionCode()
        setMockedImageController(imageController)
    }

    private fun mockDB() {
        setMockedDatabase(teaMemoryDatabase)
        every { teaMemoryDatabase.teaDao } returns teaDao
        every { teaMemoryDatabase.infusionDao } returns infusionDao
    }

    private fun mockSystemVersionCode() {
        setFixedSystem(systemUtility)
        every { systemUtility.sdkVersion } returns Build.VERSION_CODES.R
    }

    @Test
    fun launchActivityExpectTeaList() {
        mockSharedSettings()
        val teaList = generateTeaList(TEA_NAME_ACTIVITY)
        every { teaDao.getTeasOrderByActivity() } returns teaList

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview -> checkExpectedTeas(TEA_NAME_ACTIVITY, overview) }
    }

    @Test
    fun launchActivityExpectUpdateDescription() {
        mockSharedSettings(SortMode.LAST_USED, true)
        val teaList = generateTeaList(TEA_NAME_ACTIVITY)
        every { teaDao.getTeasOrderByActivity() } returns teaList

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            val dialogUpdate = ShadowAlertDialog.getLatestAlertDialog()
            checkTitleAndMessageOfLatestDialog(overview, dialogUpdate, R.string.overview_dialog_update_header, R.string.overview_dialog_update_description)
            dialogUpdate.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            assertThat(SharedSettings(RuntimeEnvironment.getApplication()).isOverviewUpdateAlert).isFalse

            val expected = Intent(overview, UpdateDescription::class.java)
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.data).isEqualTo(expected.data)
        }
    }

    @Test
    fun launchActivityExpectUpdateDescriptionClickNegative() {
        mockSharedSettings(SortMode.LAST_USED, true)
        val teaList = generateTeaList(TEA_NAME_ACTIVITY)
        every { teaDao.getTeasOrderByActivity() } returns teaList

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity {
            ShadowAlertDialog.getLatestAlertDialog().getButton(DialogInterface.BUTTON_NEGATIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            assertThat(SharedSettings(RuntimeEnvironment.getApplication()).isOverviewUpdateAlert).isFalse()
        }
    }

    @Test
    fun clickToRandomChoiceExpectRandomChoiceDialog() {
        mockSharedSettings()

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            val buttonRandomChoice = overview.findViewById<FloatingActionButton>(R.id.floating_button_overview_random_choice)
            buttonRandomChoice.performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val dialog = ShadowAlertDialog.getLatestAlertDialog()
            val shadowDialog = Shadows.shadowOf(dialog)

            assertThat(shadowDialog.title).isEqualTo(overview.getString(R.string.overview_dialog_random_choice_title))
        }
    }

    @Test
    fun navigateToSettingsExpectSettingsActivity() {
        mockSharedSettings()

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            overview.onOptionsItemSelected(RoboMenuItem(R.id.action_overview_settings))

            val expected = Intent(overview, Settings::class.java)
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.component).isEqualTo(expected.component)
        }
    }

    @Test
    fun navigateToMoreExpectMoreActivity() {
        mockSharedSettings()

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            overview.onOptionsItemSelected(RoboMenuItem(R.id.action_overview_more))

            val expected = Intent(overview, More::class.java)
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.component).isEqualTo(expected.component)
        }
    }

    @Test
    fun searchStringExpectSearchList() {
        mockSharedSettings()
        val teaName = "SEARCH_"
        val teaList = generateTeaList(teaName)
        every { teaDao.getTeasBySearchString(teaName) } returns teaList

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            overview.onOptionsItemSelected(RoboMenuItem(R.id.action_overview_search))

            val menuItem = (overview.findViewById<View>(R.id.action_overview_search) as ActionMenuItemView).itemData
            val searchView = menuItem.actionView as SearchView?
            searchView!!.setQuery(teaName, false)
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            checkExpectedTeas(teaName, overview)
        }
    }

    @Test
    fun changeSortModeToActivityExpectTeaList() {
        mockSharedSettings(SortMode.ALPHABETICAL, false)
        val teaList = generateTeaList(TEA_NAME_ACTIVITY)
        every { teaDao.getTeasOrderByActivity() } returns teaList

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            overview.onOptionsItemSelected(RoboMenuItem(R.id.action_overview_sort))
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val dialog = ShadowAlertDialog.getLatestAlertDialog()

            val radioButtons = getRadioButtons(dialog)
            radioButtons[SortMode.LAST_USED.choice].performClick()

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            checkExpectedTeas(TEA_NAME_ACTIVITY, overview)
        }
    }

    @Test
    fun enableShowTeasInStockExpectTeasInStock() {
        mockSharedSettings()
        val teaList = generateTeaList(TEA_NAME_ACTIVITY)
        every { teaDao.getTeasInStockOrderByActivity() } returns teaList

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            overview.onOptionsItemSelected(RoboMenuItem(R.id.action_overview_sort))
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val dialog = ShadowAlertDialog.getLatestAlertDialog()

            val checkBoxInStock = dialog.findViewById<CheckBox>(R.id.checkbox_overview_in_stock)
            checkBoxInStock.isChecked = true

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            checkExpectedTeas(TEA_NAME_ACTIVITY, overview)
        }
    }

    @Test
    fun showTeaListWithStoredImageAndFilledImageText() {
        val imageUri = "uri"

        mockSharedSettings()
        val teaList = generateTeaList(TEA_NAME_ACTIVITY)
        every {teaDao.getTeasInStockOrderByActivity() } returns teaList
        every {imageController.getImageUriByTeaId(0L) } returns Uri.parse(imageUri)
        every {imageController.getLastModified(Uri.parse(imageUri)) } returns "date"
        every {imageController.getImageUriByTeaId(1L) } returns null

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            overview.onOptionsItemSelected(RoboMenuItem(R.id.action_overview_sort))
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val dialog = ShadowAlertDialog.getLatestAlertDialog()

            val checkBoxInStock = dialog.findViewById<CheckBox>(R.id.checkbox_overview_in_stock)
            checkBoxInStock.isChecked = true

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val recyclerView = overview.findViewById<RecyclerView>(R.id.recycler_view_overview_tea_list)
            val itemView0 = recyclerView.findViewHolderForAdapterPosition(0)!!.itemView
            val image = itemView0.findViewById<ImageView>(R.id.image_view_recycler_view_image)
            assertThat(image.tag).isEqualTo(imageUri)

            val itemView1 = recyclerView.findViewHolderForAdapterPosition(1)!!.itemView
            val textView = itemView1.findViewById<TextView>(R.id.text_view_recycler_view_image)
            assertThat(textView.text).isEqualTo("A")
        }
    }

    @Test
    fun changeSortModeToAlphabeticallyExpectTeaList() {
        mockSharedSettings()
        val teaName = "ALPHABETICALLY_"
        val teaList = generateTeaList(teaName)
        every { teaDao.getTeasOrderByAlphabetic() } returns teaList

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            overview.onOptionsItemSelected(RoboMenuItem(R.id.action_overview_sort))
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val dialog = ShadowAlertDialog.getLatestAlertDialog()

            val radioButtons = getRadioButtons(dialog)
            radioButtons[SortMode.ALPHABETICAL.choice].performClick()

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            checkExpectedTeas(teaName, overview)
        }
    }

    @Test
    fun changeSortModeToVarietyExpectTeaList() {
        mockSharedSettings()
        val teaName = "VARIETY_"
        val teaList = generateTeaList(teaName)
        every { teaDao.getTeasOrderByVariety() } returns teaList

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            overview.onOptionsItemSelected(RoboMenuItem(R.id.action_overview_sort))
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val dialog = ShadowAlertDialog.getLatestAlertDialog()

            val radioButtons = getRadioButtons(dialog)
            radioButtons[SortMode.BY_VARIETY.choice].performClick()

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            checkExpectedTeas(teaName, overview)
        }
    }

    @Test
    fun changeSortModeToRatingExpectTeaList() {
        mockSharedSettings()
        val teaName = "RATING_"
        val teaList = generateTeaList(teaName)
        every { teaDao.getTeasOrderByRating() } returns teaList

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            overview.onOptionsItemSelected(RoboMenuItem(R.id.action_overview_sort))
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val dialog = ShadowAlertDialog.getLatestAlertDialog()

            val radioButtons = getRadioButtons(dialog)
            radioButtons[SortMode.RATING.choice].performClick()

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            checkExpectedTeas(teaName, overview)
        }
    }

    @Test
    fun enableSortingHeaderExpectTeaListWithHeader() {
        mockSharedSettings(SortMode.LAST_USED, false)
        val teaList = generateTeaList(TEA_NAME_ACTIVITY)
        every { teaDao.getTeasOrderByActivity() } returns teaList

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            val sharedSettings = SharedSettings(overview.application)
            sharedSettings.isOverviewHeader = true

            overview.onOptionsItemSelected(RoboMenuItem(R.id.action_overview_sort))
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val dialog = ShadowAlertDialog.getLatestAlertDialog()

            val radioButtons = getRadioButtons(dialog)
            radioButtons[SortMode.LAST_USED.choice].performClick()

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val recyclerView = overview.findViewById<RecyclerView>(R.id.recycler_view_overview_tea_list)

            assertThat(recyclerView.adapter!!.itemCount).isEqualTo(4)
        }
    }

    @Test
    fun clickAddTeaExpectNewTeaActivity() {
        mockSharedSettings()

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            val addTeaButton = overview.findViewById<FloatingActionButton>(R.id.floating_button_overview_new_tea)
            addTeaButton.performClick()

            val expected = Intent(overview, NewTea::class.java)
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.component).isEqualTo(expected.component)
        }
    }

    @Test
    fun clickTeaExpectShowTeaActivity() {
        val positionTea = 1
        mockSharedSettings()
        val teaName = "TEA_"
        val teaList = generateTeaList(teaName)
        every { teaDao.getTeasOrderByActivity() } returns teaList

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            val teaListRecyclerView = overview.findViewById<RecyclerView>(R.id.recycler_view_overview_tea_list)
            clickAtPositionRecyclerView(teaListRecyclerView, positionTea)

            val expected = Intent(overview, ShowTea::class.java)
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.component).isEqualTo(expected.component)
            assertThat(actual.extras!!["teaId"]).isEqualTo(positionTea.toLong())
        }
    }

    @Test
    fun setTeaInStockExpectTeaInStock() {
        val teaPosition = 1
        mockSharedSettings()
        val teaName = "TEA_"
        val teaList = generateTeaList(teaName)
        every { teaDao.getTeasOrderByActivity() } returns teaList
        every { teaDao.getTeaById(teaPosition.toLong()) } returns teaList[teaPosition]

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            val recyclerView = overview.findViewById<RecyclerView>(R.id.recycler_view_overview_tea_list)
            val itemViewRecyclerItem = recyclerView.findViewHolderForAdapterPosition(teaPosition)!!.itemView
            itemViewRecyclerItem.performLongClick()

            selectItemPopUpMenu(R.id.action_overview_tea_list_in_stock)

            val teaSlot = slot<Tea>()
            verify { teaDao.update(capture(teaSlot)) }
            assertThat(teaSlot.captured.inStock).isFalse()
        }
    }

    @Test
    fun editTeaExpectNewTeaActivity() {
        val teaPosition = 1
        mockSharedSettings()
        val teaName = "TEA_"
        val teaList = generateTeaList(teaName)
        every { teaDao.getTeasOrderByActivity() } returns teaList
        every { teaDao.getTeaById(teaPosition.toLong()) } returns teaList[teaPosition]

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            val recyclerView = overview.findViewById<RecyclerView>(R.id.recycler_view_overview_tea_list)
            val itemViewRecyclerItem = recyclerView.findViewHolderForAdapterPosition(teaPosition)!!.itemView
            itemViewRecyclerItem.performLongClick()

            selectItemPopUpMenu(R.id.action_overview_tea_list_edit)

            val expected = Intent(overview, NewTea::class.java)
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.component).isEqualTo(expected.component)
            assertThat(actual.getLongExtra("teaId", -1)).isEqualTo(teaPosition.toLong())
        }
    }

    @Test
    fun deleteTeaExpectDeletion() {
        val teaPosition = 1
        mockSharedSettings()
        val teaName = "TEA_"
        val teaList = generateTeaList(teaName)
        every { teaDao.getTeasOrderByActivity() } returns teaList
        every { teaDao.getTeaById(teaPosition.toLong()) } returns teaList[teaPosition]

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            val recyclerView = overview.findViewById<RecyclerView>(R.id.recycler_view_overview_tea_list)
            val itemViewRecyclerItem = recyclerView.findViewHolderForAdapterPosition(teaPosition)!!.itemView
            itemViewRecyclerItem.performLongClick()

            selectItemPopUpMenu(R.id.action_overview_tea_list_delete)

            val dialogDelete = ShadowAlertDialog.getLatestAlertDialog()
            dialogDelete.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            verify { teaDao.deleteTeaById(1L) }

            verify { imageController.removeImageByTeaId(1L) }
        }
    }

    @Test
    fun cancelDeleteTeaExpectCanceledDeletion() {
        val teaPosition = 1
        mockSharedSettings()
        val teaName = "TEA_"
        val teaList = generateTeaList(teaName)
        every { teaDao.getTeasOrderByActivity() } returns teaList
        every { teaDao.getTeaById(teaPosition.toLong()) } returns teaList[teaPosition]

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            val recyclerView = overview.findViewById<RecyclerView>(R.id.recycler_view_overview_tea_list)
            val itemViewRecyclerItem = recyclerView.findViewHolderForAdapterPosition(teaPosition)!!.itemView
            itemViewRecyclerItem.performLongClick()

            selectItemPopUpMenu(R.id.action_overview_tea_list_delete)

            val dialogDelete = ShadowAlertDialog.getLatestAlertDialog()
            dialogDelete.getButton(DialogInterface.BUTTON_NEGATIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            verify (exactly = 0) { teaDao.deleteTeaById(1L) }
        }
    }

    @Test
    fun deleteTeaVersionCodeOlderAndroidQExpectDeletion() {
        val teaPosition = 1
        mockSharedSettings()
        val teaName = "TEA_"
        val teaList = generateTeaList(teaName)
        every { teaDao.getTeasOrderByActivity() } returns teaList
        every { teaDao.getTeaById(teaPosition.toLong()) } returns teaList[teaPosition]
        every { systemUtility.sdkVersion } returns Build.VERSION_CODES.P

        val overviewActivityScenario = ActivityScenario.launch(Overview::class.java)
        overviewActivityScenario.onActivity { overview: Overview ->
            val recyclerView = overview.findViewById<RecyclerView>(R.id.recycler_view_overview_tea_list)
            val itemViewRecyclerItem = recyclerView.findViewHolderForAdapterPosition(teaPosition)!!.itemView
            itemViewRecyclerItem.performLongClick()

            selectItemPopUpMenu(R.id.action_overview_tea_list_delete)

            val dialogDelete = ShadowAlertDialog.getLatestAlertDialog()
            dialogDelete.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            verify { teaDao.deleteTeaById(1L) }

            verify (exactly = 0) { imageController.removeImageByTeaId(any()) }
        }
    }

    private fun mockSharedSettings(sortMode: SortMode = SortMode.LAST_USED, overviewUpdateAlert: Boolean = false) {
        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())
        sharedSettings.isFirstStart = false
        sharedSettings.isOverviewUpdateAlert = overviewUpdateAlert
        sharedSettings.sortMode = sortMode
    }

    private fun generateTeaList(name: String): List<Tea> {
        val teaList: MutableList<Tea> = ArrayList()
        for (i in 0..2) {
            val tea = Tea(name + i, "VARIETY", i.toDouble(), "AMOUNT_KIND", i, 0, getDate())
            tea.id = i.toLong()
            tea.inStock = true
            teaList.add(tea)
        }
        return teaList
    }

    private fun checkExpectedTeas(teaName: String, overview: Overview) {
        val recyclerView = overview.findViewById<RecyclerView>(R.id.recycler_view_overview_tea_list)

        assertThat(recyclerView.adapter!!.itemCount).isEqualTo(3)
        for (i in 1..2) {
            val itemView = recyclerView.findViewHolderForAdapterPosition(i)!!.itemView
            val heading = itemView.findViewById<TextView>(R.id.text_view_recycler_view_heading)
            assertThat(heading.text).hasToString(teaName + i)
        }
    }

    private fun checkTitleAndMessageOfLatestDialog(overview: Overview, dialog: AlertDialog,
                                                   title: Int, message: Int) {
        val shadowDialog = Shadows.shadowOf(dialog)
        assertThat(shadowDialog).isNotNull
        assertThat(shadowDialog.title).isEqualTo(overview.getString(title))
        assertThat(shadowDialog.message).isEqualTo(overview.getString(message))
    }

    private fun clickAtPositionRecyclerView(recyclerView: RecyclerView, position: Int) {
        recyclerView.scrollToPosition(position)
        val itemView = recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
        itemView.performClick()
    }

    private fun selectItemPopUpMenu(itemId: Int) {
        val latestPopupMenu = ShadowPopupMenu.getLatestPopupMenu()
        val menu = latestPopupMenu.menu
        menu.performIdentifierAction(itemId, Menu.FLAG_ALWAYS_PERFORM_CLOSE)
    }

    private fun getRadioButtons(dialog: AlertDialog): List<RadioButton> {
        val radioGroup = dialog.findViewById<RadioGroup>(R.id.radio_group_overview_sort_mode)
        val listRadioButtons = ArrayList<RadioButton>()
        for (i in 0 until radioGroup.childCount) {
            val o = radioGroup.getChildAt(i)
            if (o is RadioButton) {
                listRadioButtons.add(o)
            }
        }
        return listRadioButtons
    }

    companion object {
        const val TEA_NAME_ACTIVITY = "ACTIVITY_"
    }
}