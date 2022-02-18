package coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller;

import android.content.Intent;
import android.net.Uri;

import java.io.IOException;

public interface ImageController {
    Intent getSaveOrUpdateImageIntent(long teaId) throws IOException;

    Uri getImageUriByTeaId(long teaId);

    void removeImageByTeaId(long teaId);

    String getLastModified(Uri uri);
}
