package coolpharaoh.tee.speicher.tea.timer.views.information

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.provider.MediaStore
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import com.google.android.material.floatingactionbutton.FloatingActionButton
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.getDate
import coolpharaoh.tee.speicher.tea.timer.core.note.Note
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteDao
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk.setFixedSystem
import coolpharaoh.tee.speicher.tea.timer.core.system.SystemUtility
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase.Companion.setMockedDatabase
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
import org.robolectric.Shadows
import org.robolectric.fakes.RoboMenuItem
import org.robolectric.shadows.ShadowAlertDialog
import org.robolectric.shadows.ShadowInstrumentation
import org.robolectric.shadows.ShadowPopupMenu
import java.text.SimpleDateFormat
import java.util.Date

@RunWith(RobolectricTestRunner::class)
class InformationTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    lateinit var teaMemoryDatabase: TeaMemoryDatabase
    @RelaxedMockK
    lateinit var teaDao: TeaDao
    @RelaxedMockK
    lateinit var noteDao: NoteDao
    @RelaxedMockK
    lateinit var counterDao: CounterDao
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
        every { teaMemoryDatabase.noteDao } returns noteDao
        every { teaMemoryDatabase.counterDao } returns counterDao
    }

    private fun mockSystemVersionCode() {
        setFixedSystem(systemUtility)
        every { systemUtility.sdkVersion } returns Build.VERSION_CODES.R
    }

    @Test
    fun launchActivityAndExpectEmptyInformation() {
        every { noteDao.getNoteByTeaIdAndPosition(TEA_ID, -1) } returns null
        createTea(0)
        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, Information::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val informationActivityScenario = ActivityScenario.launch<Information>(intent)
        informationActivityScenario.onActivity { information: Information ->
            val textViewTeaName = information.findViewById<TextView>(R.id.text_view_information_tea_name)
            assertThat(textViewTeaName.text).isEqualTo(TEA_NAME)

            val textViewVariety = information.findViewById<TextView>(R.id.text_view_information_variety)
            assertThat(textViewVariety.text).isEqualTo(TEA_VARIETY)

            val ratingBar = information.findViewById<RatingBar>(R.id.rating_bar_information)
            assertThat(ratingBar.rating).isZero

            val recyclerView = information.findViewById<RecyclerView>(R.id.recycler_view_information_details)
            assertThat(recyclerView.adapter!!.itemCount).isZero

            val editTextNotes = information.findViewById<EditText>(R.id.edit_text_information_notes)
            assertThat(editTextNotes.text.toString()).isBlank
            verify { noteDao.insert(any()) }
        }
    }

    @Test
    fun launchActivityAndExpectFilledInformation() {
        val uri = Uri.parse("Test")
        every { imageController.getImageUriByTeaId(TEA_ID) } returns uri
        val rating = 4
        val inStock = true
        createTea(rating, inStock)
        val (_, _, _, _, description) = createNotes()
        createDetails()
        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, Information::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val informationActivityScenario = ActivityScenario.launch<Information>(intent)
        informationActivityScenario.onActivity { information: Information ->
            val imageViewImage = information.findViewById<ImageView>(R.id.image_view_information_image)
            assertThat(imageViewImage.tag).isEqualTo(uri.toString())

            val textViewTeaName = information.findViewById<TextView>(R.id.text_view_information_tea_name)
            assertThat(textViewTeaName.text).isEqualTo(TEA_NAME)
            assertThat(textViewTeaName.currentTextColor).isEqualTo(ContextCompat.getColor(information, R.color.text_white))

            val textViewVariety = information.findViewById<TextView>(R.id.text_view_information_variety)
            assertThat(textViewVariety.text).isEqualTo(TEA_VARIETY)

            val ratingBar = information.findViewById<RatingBar>(R.id.rating_bar_information)
            assertThat(ratingBar.rating).isEqualTo(4f)

            val recyclerView = information.findViewById<RecyclerView>(R.id.recycler_view_information_details)
            assertThat(recyclerView.adapter!!.itemCount).isEqualTo(3)

            val editTextNotes = information.findViewById<EditText>(R.id.edit_text_information_notes)
            assertThat(editTextNotes.text).hasToString(description)
        }
    }

    @Test
    fun launchActivityWithSystemOlderAndroidQAndExpectNoFilledImageAndCameraButtonGone() {
        every { systemUtility.sdkVersion } returns Build.VERSION_CODES.P
        createTea(0)
        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, Information::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val informationActivityScenario = ActivityScenario.launch<Information>(intent)
        informationActivityScenario.onActivity { information: Information ->
            val texViewTeaName = information.findViewById<TextView>(R.id.text_view_information_tea_name)
            assertThat(texViewTeaName.currentTextColor).isEqualTo(ContextCompat.getColor(information, R.color.text_black))

            val buttonCamera = information.findViewById<FloatingActionButton>(R.id.button_information_camera)
            assertThat(buttonCamera.visibility).isEqualTo(View.GONE)

            verify(exactly = 0) { imageController.getImageUriByTeaId(any()) }
        }
    }

    @Test
    fun leaveActivityAndExpectStoredNotes() {
        val notes = "New Notes"
        createTea(0)
        createNotes()
        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, Information::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val informationActivityScenario = ActivityScenario.launch<Information>(intent)
        informationActivityScenario.onActivity { information: Information ->
            val editTextNotes = information.findViewById<EditText>(R.id.edit_text_information_notes)
            editTextNotes.setText(notes)
        }

        informationActivityScenario.close()

        val slotNote = slot<Note>()
        verify { noteDao.update(capture(slotNote)) }
        assertThat(slotNote.captured)
            .extracting(Note::position, Note::header, Note::description)
            .containsExactly(-1, NOTES_HEADER, notes)
    }

    @Test
    @Throws(Exception::class)
    fun updateImage() {
        createTea(0)
        every { imageController.getSaveOrUpdateImageIntent(TEA_ID) } returns Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, Information::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val informationActivityScenario = ActivityScenario.launch<Information>(intent)
        informationActivityScenario.onActivity { information: Information ->
            val buttonCamera = information.findViewById<FloatingActionButton>(R.id.button_information_camera)
            buttonCamera.performClick()

            val uri = Uri.parse("Test")
            every { imageController.getImageUriByTeaId(TEA_ID) } returns uri
            mockReturnActionActivityResult(information)

            val imageViewImage = information.findViewById<ImageView>(R.id.image_view_information_image)
            assertThat(imageViewImage.tag).isEqualTo(uri.toString())

            val texViewTeaName = information.findViewById<TextView>(R.id.text_view_information_tea_name)
            assertThat(texViewTeaName.currentTextColor).isEqualTo(ContextCompat.getColor(information, R.color.text_white))
        }
    }

    @Test
    fun updateRating() {
        val newRating = 4
        createTea(0)
        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, Information::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val informationActivityScenario = ActivityScenario.launch<Information>(intent)
        informationActivityScenario.onActivity { information: Information ->
            val ratingBar = information.findViewById<RatingBar>(R.id.rating_bar_information)
            ratingBar.rating = newRating.toFloat()

            val slotTea = slot<Tea>()
            verify { teaDao.update(capture(slotTea)) }
            assertThat(slotTea.captured.rating).isEqualTo(4)
        }
    }

    @Test
    fun updateInStock() {
        createTea(0)
        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, Information::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val informationActivityScenario = ActivityScenario.launch<Information>(intent)
        informationActivityScenario.onActivity { information: Information ->
            information.onOptionsItemSelected(RoboMenuItem(R.id.action_information_in_stock))

            val slotTea = slot<Tea>()
            verify { teaDao.update(capture(slotTea)) }
            assertThat(slotTea.captured.inStock).isTrue

            information.onOptionsItemSelected(RoboMenuItem(R.id.action_information_in_stock))

            assertThat(slotTea.captured.inStock).isFalse
        }
    }

    @Test
    fun addDetail() {
        createTea(0)
        createNotes()
        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, Information::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val informationActivityScenario = ActivityScenario.launch<Information>(intent)
        informationActivityScenario.onActivity { information: Information ->
            val buttonAddDetail = information.findViewById<ImageButton>(R.id.button_information_add_detail)
            buttonAddDetail.performClick()

            val dialogAddDetail = getAndCheckAlertDialog(information, R.string.information_add_detail_dialog_heading)

            checkAndSetContentInDetailsDialog(dialogAddDetail, R.id.edit_text_information_dialog_add_edit_header, "", HEADER)
            checkAndSetContentInDetailsDialog(dialogAddDetail, R.id.edit_text_information_dialog_add_edit_description, "", DESCRIPTION)

            dialogAddDetail.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val slotNote = slot<Note>()
            verify { noteDao.insert(capture(slotNote)) }
            assertThat(slotNote.captured)
                .extracting(Note::header, Note::description)
                .containsExactly(HEADER, DESCRIPTION)
        }
    }

    @Test
    fun addBlankDetail() {
        createTea(0)
        createNotes()
        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, Information::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val informationActivityScenario = ActivityScenario.launch<Information>(intent)
        informationActivityScenario.onActivity { information: Information ->
            val buttonAddDetail = information.findViewById<ImageButton>(R.id.button_information_add_detail)
            buttonAddDetail.performClick()

            val dialogAddDetail = getAndCheckAlertDialog(information, R.string.information_add_detail_dialog_heading)

            dialogAddDetail.getButton(DialogInterface.BUTTON_POSITIVE).performClick()

            verify (exactly = 0) { noteDao.insert(any()) }
        }
    }

    @Test
    fun deleteDetail() {
        val position = 1
        createTea(0)
        createDetails()
        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, Information::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val informationActivityScenario = ActivityScenario.launch<Information>(intent)
        informationActivityScenario.onActivity { information: Information ->
            val recyclerView = information.findViewById<RecyclerView>(R.id.recycler_view_information_details)
            val itemViewRecyclerItem = recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
            val buttonChangeItem = itemViewRecyclerItem.findViewById<Button>(R.id.button_detail_options)

            buttonChangeItem.performClick()

            selectItemPopUpMenu(R.id.action_information_details_delete)

            verify { noteDao.deleteNoteByTeaIdAndPosition(TEA_ID, position) }
        }
    }

    @Test
    fun editDetail() {
        val position = 0
        createTea(0)
        val details = createDetails()
        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, Information::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val informationActivityScenario = ActivityScenario.launch<Information>(intent)
        informationActivityScenario.onActivity { information: Information ->
            val recyclerView = information.findViewById<RecyclerView>(R.id.recycler_view_information_details)
            val itemViewRecyclerItem = recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
            val buttonChangeItem = itemViewRecyclerItem.findViewById<Button>(R.id.button_detail_options)

            buttonChangeItem.performClick()

            selectItemPopUpMenu(R.id.action_information_details_edit)

            val dialogAddDetail = getAndCheckAlertDialog(information, R.string.information_edit_detail_dialog_heading)

            checkAndSetContentInDetailsDialog(
                dialogAddDetail, R.id.edit_text_information_dialog_add_edit_header,
                details[position].header, HEADER
            )
            checkAndSetContentInDetailsDialog(
                dialogAddDetail, R.id.edit_text_information_dialog_add_edit_description,
                details[position].description, DESCRIPTION
            )

            dialogAddDetail.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
            Shadows.shadowOf(Looper.getMainLooper()).idle()

            val slotNote = slot<Note>()
            verify { noteDao.update(capture(slotNote)) }
            assertThat(slotNote.captured)
                .extracting(Note::header, Note::description)
                .containsExactly(HEADER, DESCRIPTION)
        }
    }

    @Test
    fun showLastUsed() {
        val date = getDate()
        createTea(0, date)

        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, Information::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val informationActivityScenario = ActivityScenario.launch<Information>(intent)
        informationActivityScenario.onActivity { information: Information ->
            val textViewLastUsed = information.findViewById<TextView>(R.id.text_view_information_last_used)

            val formatter = SimpleDateFormat("dd MMMM yyyy")
            val strDate = formatter.format(date)
            assertThat(textViewLastUsed.text).hasToString(information.getString(R.string.information_counter_last_used, strDate))
        }
    }

    @Test
    fun fillCounter() {
        createTea(0)
        val counter = Counter(TEA_ID, 1, 2, 3, 4, getDate(), getDate(), getDate())
        every { counterDao.getCounterByTeaId(TEA_ID) } returns counter

        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, Information::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val informationActivityScenario = ActivityScenario.launch<Information>(intent)
        informationActivityScenario.onActivity { information: Information -> checkCounter(information, "1", "2", "3", "4") }
    }

    @Test
    fun fillCounterWhenCounterIsNotAvailable() {
        createTea(0)

        val intent = Intent(ShadowInstrumentation.getInstrumentation().targetContext.applicationContext, Information::class.java)
        intent.putExtra(TEA_ID_EXTRA, TEA_ID)

        val informationActivityScenario = ActivityScenario.launch<Information>(intent)
        informationActivityScenario.onActivity { information: Information -> checkCounter(information, "0", "0", "0", "0") }
    }

    private fun createTea(rating: Int, date: Date) {
        val tea = Tea(TEA_NAME, null, 0.0, null, 0, 0, date)
        tea.rating = rating
        every { teaDao.getTeaById(TEA_ID) } returns tea
    }

    private fun createTea(rating: Int, inStock: Boolean = false) {
        val tea = Tea(TEA_NAME, TEA_VARIETY, 0.0, null, 0, 0, getDate())
        tea.rating = rating
        tea.inStock = inStock
        every { teaDao.getTeaById(TEA_ID) } returns tea
    }

    private fun createDetails(): List<Note> {
        val details: MutableList<Note> = ArrayList()
        for (i in 0..2) {
            details.add(Note(TEA_ID, i, HEADER + i, DESCRIPTION + i))
        }
        every { noteDao.getNotesByTeaIdAndPositionBiggerZero(TEA_ID) } returns details
        return details
    }

    private fun createNotes(): Note {
        val notes = Note(TEA_ID, -1, NOTES_HEADER, "Some Notes")
        every { noteDao.getNoteByTeaIdAndPosition(TEA_ID, -1) } returns notes
        return notes
    }

    private fun selectItemPopUpMenu(itemId: Int) {
        val latestPopupMenu = ShadowPopupMenu.getLatestPopupMenu()
        val menu = latestPopupMenu.menu
        menu.performIdentifierAction(itemId, Menu.FLAG_ALWAYS_PERFORM_CLOSE)
    }

    private fun getAndCheckAlertDialog(information: Information, dialogHeading: Int): AlertDialog {
        val dialogAddDetail = ShadowAlertDialog.getLatestAlertDialog()
        val shadowDialogAddDetail = Shadows.shadowOf(dialogAddDetail)
        assertThat(shadowDialogAddDetail).isNotNull
        assertThat(shadowDialogAddDetail.title).isEqualTo(information.getString(dialogHeading))
        return dialogAddDetail
    }

    private fun checkAndSetContentInDetailsDialog(dialog: AlertDialog, editTextId: Int, oldContent: String?, newContent: String) {
        val editTextAddHeader = dialog.findViewById<EditText>(editTextId)
        assertThat(editTextAddHeader.text).hasToString(oldContent)
        editTextAddHeader.setText(newContent)
    }

    private fun checkCounter(information: Information, week: String, month: String, year: String, overall: String) {
        val textViewWeek = information.findViewById<TextView>(R.id.text_view_information_counter_week)
        val textViewMonth = information.findViewById<TextView>(R.id.text_view_information_counter_month)
        val textViewYear = information.findViewById<TextView>(R.id.text_view_information_counter_year)
        val textViewOverall = information.findViewById<TextView>(R.id.text_view_information_counter_overall)

        assertThat(textViewWeek.text).hasToString(week)
        assertThat(textViewMonth.text).hasToString(month)
        assertThat(textViewYear.text).hasToString(year)
        assertThat(textViewOverall.text).hasToString(overall)
    }

    private fun mockReturnActionActivityResult(information: Information) {
        val shadowActivity = Shadows.shadowOf(information)
        shadowActivity.receiveResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), Activity.RESULT_OK, Intent())
    }

    companion object {
        private const val TEA_ID_EXTRA = "teaId"
        private const val TEA_ID = 1L
        private const val HEADER = "header"
        private const val DESCRIPTION = "description"
        private const val TEA_NAME = "teaName"
        private const val TEA_VARIETY = "teaVariety"
        private const val NOTES_HEADER = "01_notes"
    }
}