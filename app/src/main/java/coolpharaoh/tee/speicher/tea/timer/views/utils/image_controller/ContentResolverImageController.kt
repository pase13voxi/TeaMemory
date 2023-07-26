package coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.BaseColumns
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File
import java.io.IOException

class ContentResolverImageController(context: Context) : ImageController {
    private val contentResolver: ContentResolver

    init {
        contentResolver = context.contentResolver
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Throws(IOException::class)
    override fun getSaveOrUpdateImageIntent(teaId: Long): Intent {
        var uri = getImageUriByTeaId(teaId)

        if (uri == null) {
            uri = createNewImageUri(teaId)
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)

        return intent
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    override fun getImageUriByTeaId(teaId: Long): Uri? {
        val projection = arrayOf(
            BaseColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.RELATIVE_PATH,
            MediaStore.MediaColumns.DATE_MODIFIED
        )

        val selection = MediaStore.MediaColumns.RELATIVE_PATH + "='" +
                Environment.DIRECTORY_PICTURES + File.separator + FOLDER + File.separator +
                "' AND " + MediaStore.MediaColumns.DISPLAY_NAME + "='" + teaId + ".jpg'"

        val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, selection, null, null)

        if (cursor != null) {
            if (cursor.count > 0 && cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            }
            cursor.close()
        }
        return null
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Throws(IOException::class)
    private fun createNewImageUri(teaId: Long): Uri {
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, teaId)
        values.put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE)
        values.put(MediaStore.MediaColumns.RELATIVE_PATH,
            Environment.DIRECTORY_PICTURES + File.separator + FOLDER)

        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            ?: throw IOException("Failed to create new MediaStore record.")
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    override fun removeImageByTeaId(teaId: Long) {
        val imageUri = getImageUriByTeaId(teaId)
        if (imageUri != null) {
            contentResolver.delete(imageUri, null, null)
        }
    }

    override fun getLastModified(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)

        var dateModified: String? = null
        if (cursor != null) {
            val dateIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED)
            cursor.moveToFirst()
            dateModified = cursor.getString(dateIndex)
            cursor.close()
        }

        return dateModified ?: ""
    }

    companion object {
        private const val MIME_TYPE = "image/jpeg"
        private const val FOLDER = "TeaMemory"
    }
}