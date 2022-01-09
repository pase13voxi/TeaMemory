package coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller;

import android.content.Intent;
import android.net.Uri;

import java.io.IOException;

public interface ImageController {
    Intent getSaveOrUpdateImageIntent(String teaId) throws IOException;

    Uri getImageUriByTeaId(String teaId);

    void removeImageByTeaId(String teaId);
}
