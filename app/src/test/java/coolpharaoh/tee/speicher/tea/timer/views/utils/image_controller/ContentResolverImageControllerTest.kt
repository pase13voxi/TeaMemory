package coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.BaseColumns
import android.provider.MediaStore
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class ContentResolverImageControllerTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    lateinit var context: Context
    @RelaxedMockK
    lateinit var contentResolver: ContentResolver
    @RelaxedMockK
    lateinit var cursor: Cursor

    @Before
    @Throws(Exception::class)
    fun setUp() {
        every { context.contentResolver } returns contentResolver
    }

    @Test
    fun getImageUriByTeaId() {
        mockImageAvailable()

        val contentResolverImageController = ContentResolverImageController(context)
        val uri = contentResolverImageController.getImageUriByTeaId(TEA_ID)

        assertThat(uri).isEqualTo(Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + "/2"))
    }

    @Test
    fun getImageUriByTeaIdImageNotAvailable() {
        mockImageNotAvailable()

        val contentResolverImageController = ContentResolverImageController(context)
        val uri = contentResolverImageController.getImageUriByTeaId(TEA_ID)

        assertThat(uri).isNull()
    }

    @Throws(Exception::class)
    @Test
    fun getSaveImageIntent() {
        mockImageNotAvailable()
        val uri = Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + "/13")
        mockContentResolverInsert(uri)

        val contentResolverImageController = ContentResolverImageController(context)
        val saveIntent = contentResolverImageController.getSaveOrUpdateImageIntent(TEA_ID)

        assertThat(saveIntent.action).isEqualTo(MediaStore.ACTION_IMAGE_CAPTURE)
        assertThat(saveIntent.extras!![MediaStore.EXTRA_OUTPUT]).isEqualTo(uri)
    }

    @Test
    fun getSaveImageIntentCouldNotCreateUri() {
        every { contentResolver.insert(any(), any()) } returns null
        mockImageNotAvailable()

        val contentResolverImageController = ContentResolverImageController(context)
        assertThatExceptionOfType(IOException::class.java)
            .isThrownBy { contentResolverImageController.getSaveOrUpdateImageIntent(TEA_ID) }
            .withMessage("Failed to create new MediaStore record.")
    }

    @Throws(Exception::class)
    @Test
    fun getUpdateImageIntent() {
        mockImageAvailable()

        val contentResolverImageController = ContentResolverImageController(context)
        val updateIntent = contentResolverImageController.getSaveOrUpdateImageIntent(TEA_ID)

        assertThat(updateIntent.action).isEqualTo(MediaStore.ACTION_IMAGE_CAPTURE)
        assertThat(updateIntent.extras!![MediaStore.EXTRA_OUTPUT])
            .hasToString(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + "/2")
    }

    @Test
    fun removeImageByTeaId() {
        mockImageAvailable()

        val contentResolverImageController = ContentResolverImageController(context)
        contentResolverImageController.removeImageByTeaId(TEA_ID)

        val uri = Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + "/2")
        verify { contentResolver.delete(uri, null, null) }
    }

    @Test
    fun removeImageByTeaIdImageNotAvailable() {
        mockImageNotAvailable()

        val contentResolverImageController = ContentResolverImageController(context)
        contentResolverImageController.removeImageByTeaId(TEA_ID)

        verify(exactly = 0) { contentResolver.delete(any(), null, null) }
    }

    @Test
    fun getLastModified() {
        val uri = Uri.parse("Uri")
        val modificationDate = "1234"
        every { contentResolver.query(uri, null, null, null, null) } returns cursor
        every { cursor.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED) } returns 2
        every { cursor.getString(2) } returns modificationDate

        val contentResolverImageController = ContentResolverImageController(context)
        val lastModified = contentResolverImageController.getLastModified(uri)

        assertThat(lastModified).isEqualTo(modificationDate)
    }

    @Test
    fun getLastModifiedReturnEmpty() {
        val contentResolverImageController = ContentResolverImageController(context)
        val lastModified = contentResolverImageController.getLastModified(Uri.parse("Uri"))

        assertThat(lastModified).isEmpty()
    }

    private fun mockImageAvailable() {
        mockContentResolverQuery()

        every { cursor.count } returns 1
        every { cursor.moveToFirst() } returns true
        every { cursor.getColumnIndexOrThrow(BaseColumns._ID) } returns 2
        every { cursor.getLong(2) } returns 2L
    }

    private fun mockImageNotAvailable() {
        mockContentResolverQuery()

        every { cursor.count } returns 0
    }

    private fun mockContentResolverQuery() {
        val projection = arrayOf(
            BaseColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.RELATIVE_PATH,
            MediaStore.MediaColumns.DATE_MODIFIED
        )

        val selection = MediaStore.MediaColumns.RELATIVE_PATH + "='" +
                Environment.DIRECTORY_PICTURES + File.separator + "TeaMemory" + File.separator +
                "' AND " + MediaStore.MediaColumns.DISPLAY_NAME + "='" + TEA_ID + ".jpg'"

        every { contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null) } returns cursor
    }

    private fun mockContentResolverInsert(uri: Uri) {
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, TEA_ID)
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "TeaMemory")

        every { contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) } returns uri
    }

    companion object {
        const val TEA_ID = 2L
    }
}