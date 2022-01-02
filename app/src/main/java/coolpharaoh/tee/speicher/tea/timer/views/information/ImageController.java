package coolpharaoh.tee.speicher.tea.timer.views.information;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;

public class ImageController {
    private static final String MIME_TYPE = "image/jpeg";
    private static final String FOLDER = "TeaMemory";

    private final ContentResolver contentResolver;

    public ImageController(final Context context) {
        contentResolver = context.getContentResolver();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    Intent saveOrUpdateImageIntent(final String teaId) throws IOException {
        final Uri uri = getImageUriByTeaId(teaId);

        if (uri != null) {
            removeOldBitmap(uri);
        }
        return getImageIntent(teaId);
    }

    private void removeOldBitmap(final Uri uri) {
        contentResolver.delete(uri, null, null);
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private Intent getImageIntent(final String teaId) throws IOException {
        final ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, teaId);
        values.put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE);

        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + FOLDER);

        Uri uri = null;
        try {
            final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            uri = contentResolver.insert(contentUri, values);

            if (uri == null)
                throw new IOException("Failed to create new MediaStore record.");

        } catch (final IOException exception) {
            if (uri != null) {
                contentResolver.delete(uri, null, null);
            }
            throw exception;
        }

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    Uri getImageUriByTeaId(final String teaId) {
        final String[] projection = {
                BaseColumns._ID,
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.RELATIVE_PATH,
                MediaStore.MediaColumns.DATE_MODIFIED
        };

        final String selection = MediaStore.MediaColumns.RELATIVE_PATH + "='" +
                Environment.DIRECTORY_PICTURES + File.separator + FOLDER + File.separator +
                "' AND " + MediaStore.MediaColumns.DISPLAY_NAME + "='" + teaId + ".jpg'";

        final Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    final long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
                    return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                }
            }
            cursor.close();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    Bitmap loadBitmap(final Uri uri) throws IOException {
        final ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, uri);
        return ImageDecoder.decodeBitmap(source);
    }
}
