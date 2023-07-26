package coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller

import android.content.Intent
import android.net.Uri
import java.io.IOException

interface ImageController {

    @Throws(IOException::class)
    fun getSaveOrUpdateImageIntent(teaId: Long): Intent?

    fun getImageUriByTeaId(teaId: Long): Uri?

    fun removeImageByTeaId(teaId: Long)

    fun getLastModified(uri: Uri): String
}