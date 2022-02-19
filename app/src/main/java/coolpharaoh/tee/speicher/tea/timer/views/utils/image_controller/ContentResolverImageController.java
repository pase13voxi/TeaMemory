package coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller;

import static android.provider.MediaStore.MediaColumns.DATE_MODIFIED;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;

public class ContentResolverImageController implements ImageController {
    private static final String MIME_TYPE = "image/jpeg";
    private static final String FOLDER = "TeaMemory";

    private final ContentResolver contentResolver;

    public ContentResolverImageController(final Context context) {
        contentResolver = context.getContentResolver();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public Intent getSaveOrUpdateImageIntent(final long teaId) throws IOException {
        Uri uri = getImageUriByTeaId(teaId);

        if (uri == null) {
            uri = createNewImageUri(teaId);
        }

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public Uri getImageUriByTeaId(final long teaId) {
        final String[] projection = {
                BaseColumns._ID,
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.RELATIVE_PATH,
                DATE_MODIFIED
        };

        final String selection = MediaStore.MediaColumns.RELATIVE_PATH + "='" +
                Environment.DIRECTORY_PICTURES + File.separator + FOLDER + File.separator +
                "' AND " + MediaStore.MediaColumns.DISPLAY_NAME + "='" + teaId + ".jpg'";

        final Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                final long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
                return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            }
            cursor.close();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private Uri createNewImageUri(final long teaId) throws IOException {
        final ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, teaId);
        values.put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE);
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + FOLDER);

        final Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        if (uri == null)
            throw new IOException("Failed to create new MediaStore record.");

        return uri;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void removeImageByTeaId(final long teaId) {
        final Uri imageUri = getImageUriByTeaId(teaId);
        if (imageUri != null) {
            contentResolver.delete(imageUri, null, null);
        }
    }

    @Override
    public String getLastModified(final Uri uri) {
        final Cursor cursor = contentResolver.query(uri, null, null, null, null);

        String dateModified = null;
        if (cursor != null) {
            final int dateIndex = cursor.getColumnIndex(DATE_MODIFIED);
            cursor.moveToFirst();
            dateModified = cursor.getString(dateIndex);
            cursor.close();
        }

        if (dateModified == null) {
            return "";
        } else {
            return dateModified;
        }
    }
}
