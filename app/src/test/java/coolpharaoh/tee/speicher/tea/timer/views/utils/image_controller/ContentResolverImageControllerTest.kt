package coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.BaseColumns
import android.provider.MediaStore
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner
import java.io.File
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class ContentResolverImageControllerTest {

    @JvmField
    @Rule
    var rule = MockitoJUnit.rule()

    @Mock
    var context: Context? = null

    @Mock
    var contentResolver: ContentResolver? = null

    @Mock
    var cursor: Cursor? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        `when`(context!!.contentResolver).thenReturn(contentResolver)
    }

    @Test
    fun getImageUriByTeaId() {
        mockImageAvailable()

        val contentResolverImageController = ContentResolverImageController(context!!)
        val uri = contentResolverImageController.getImageUriByTeaId(TEA_ID)

        assertThat(uri).isEqualTo(Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + "/2"))
    }

    @Test
    fun getImageUriByTeaIdImageNotAvailable() {
        mockImageNotAvailable()

        val contentResolverImageController = ContentResolverImageController(context!!)
        val uri = contentResolverImageController.getImageUriByTeaId(TEA_ID)

        assertThat(uri).isNull()
    }

    @Throws(Exception::class)
    @Test
    fun getSaveImageIntent() {
        mockImageNotAvailable()
        val uri = Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + "/13")
        mockContentResolverInsert(uri)

        val contentResolverImageController = ContentResolverImageController(context!!)
        val saveIntent = contentResolverImageController.getSaveOrUpdateImageIntent(TEA_ID)

        assertThat(saveIntent.action).isEqualTo(MediaStore.ACTION_IMAGE_CAPTURE)
        assertThat(saveIntent.extras!![MediaStore.EXTRA_OUTPUT]).isEqualTo(uri)
    }

    @Test
    fun getSaveImageIntentCouldNotCreateUri() {
        mockImageNotAvailable()

        val contentResolverImageController = ContentResolverImageController(context!!)
        assertThatExceptionOfType(IOException::class.java)
            .isThrownBy { contentResolverImageController.getSaveOrUpdateImageIntent(TEA_ID) }
            .withMessage("Failed to create new MediaStore record.")
    }

    @Throws(Exception::class)
    @Test
    fun getUpdateImageIntent() {
        mockImageAvailable()

        val contentResolverImageController = ContentResolverImageController(context!!)
        val updateIntent = contentResolverImageController.getSaveOrUpdateImageIntent(TEA_ID)

        assertThat(updateIntent.action).isEqualTo(MediaStore.ACTION_IMAGE_CAPTURE)
        assertThat(updateIntent.extras!![MediaStore.EXTRA_OUTPUT])
            .hasToString(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + "/2")
    }

    @Test
    fun removeImageByTeaId() {
        mockImageAvailable()

        val contentResolverImageController = ContentResolverImageController(context!!)
        contentResolverImageController.removeImageByTeaId(TEA_ID)

        val uri = Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + "/2")
        verify(contentResolver)?.delete(uri, null, null)
    }

    @Test
    fun removeImageByTeaIdImageNotAvailable() {
        mockImageNotAvailable()

        val contentResolverImageController = ContentResolverImageController(context!!)
        contentResolverImageController.removeImageByTeaId(TEA_ID)

        verify(contentResolver, Mockito.never())?.delete(
            ArgumentMatchers.any(),
            ArgumentMatchers.eq<String?>(null),
            ArgumentMatchers.eq<Array<String>?>(null)
        )
    }

    @Test
    fun getLastModified() {
        val uri = Uri.parse("Uri")
        val modificationDate = "1234"
        `when`(contentResolver!!.query(uri, null, null, null, null)).thenReturn(cursor)
        `when`(cursor!!.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED)).thenReturn(2)
        `when`(cursor!!.getString(2)).thenReturn(modificationDate)

        val contentResolverImageController = ContentResolverImageController(context!!)
        val lastModified = contentResolverImageController.getLastModified(uri)

        assertThat(lastModified).isEqualTo(modificationDate)
    }

    @Test
    fun getLastModifiedReturnEmpty() {
        val contentResolverImageController = ContentResolverImageController(context!!)
        val lastModified = contentResolverImageController.getLastModified(Uri.parse("Uri"))

        assertThat(lastModified).isEmpty()
    }

    private fun mockImageAvailable() {
        mockContentResolverQuery()

        `when`(cursor!!.count).thenReturn(1)
        `when`(cursor!!.moveToFirst()).thenReturn(true)
        `when`(cursor!!.getColumnIndexOrThrow(BaseColumns._ID)).thenReturn(2)
        `when`(cursor!!.getLong(2)).thenReturn(2L)
    }

    private fun mockImageNotAvailable() {
        mockContentResolverQuery()

        `when`(cursor!!.count).thenReturn(0)
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

        `when`(contentResolver!!.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null)).thenReturn(cursor)
    }

    private fun mockContentResolverInsert(uri: Uri) {
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, TEA_ID)
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "TeaMemory")

        `when`(contentResolver!!.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)).thenReturn(uri)
    }

    companion object {
        const val TEA_ID = 2L
    }
}